#include <algorithm>
#include <chrono>
#include <cstring>
#include <fstream>
#include <iostream>
#include <mutex>
#include <sstream>
#include <string>
#include <thread>
#include <vector>

#include <schwa/dr.h>
#include <schwa/pool.h>

#include <sys/stat.h>
#include <sys/types.h>
#include <unistd.h>

#include "db.h"
#include "db-to-dr.h"
#include "models-db.h"
#include "models-dr.h"

static unsigned int NTHREADS = 16;
static const char *const OUTPUT_DIR = "/tmp/cc-dr";


namespace ontonotes5 {
namespace to_dr {

using clock_t = std::chrono::high_resolution_clock;
using duration_t = clock_t::duration;
using time_point_t = clock_t::time_point;

static std::vector<std::string> doc_ids;
static volatile size_t doc_ids_upto = 0;
static std::mutex doc_ids_mutex;

static duration_t total_times[3] = {duration_t::zero(), duration_t::zero(), duration_t::zero()};
static std::mutex total_times_mutex;


static void
process_document(MYSQL *const mysql, const std::string &doc_id, Converter &converter, Doc::Schema &schema, duration_t *times) {
  schwa::Pool pool(4 * 1024 * 1024);
  const time_point_t loading_start = clock_t::now();
  ODocument *odoc = new (pool) ODocument(mysql, pool, doc_id.c_str());
  const time_point_t loading_end = clock_t::now();

  converter.reset();
  const time_point_t convert_start = clock_t::now();
  Doc *doc = converter.convert(*odoc);
  const time_point_t convert_end = clock_t::now();

  std::ostringstream output_path;
  output_path << OUTPUT_DIR << '/' << doc_id.substr(doc_id.rfind('/') + 1) << ".dr";

  std::ofstream out(output_path.str());
  const time_point_t write_start = clock_t::now();
  schwa::dr::Writer writer(out, schema);
  writer << *doc;
  const time_point_t write_end = clock_t::now();
  out.close();

  delete doc;

  times[0] += loading_end - loading_start;
  times[1] += convert_end - convert_start;
  times[2] += write_end - write_start;
}


static void
process_documents_thread(void) {
  MYSQL mysql;
  mysql_init(&mysql);
  db_connect(&mysql);

  std::string doc_id;
  Converter converter;
  Doc::Schema schema;
  duration_t times[3] = {duration_t::zero(), duration_t::zero(), duration_t::zero()};
  while (true) {
    doc_ids_mutex.lock();
    if (doc_ids_upto == doc_ids.size())
      doc_id.clear();
    else
      doc_id = doc_ids[doc_ids_upto++];
    doc_ids_mutex.unlock();

    if (doc_id.empty())
      break;
    process_document(&mysql, doc_id, converter, schema, times);
  }

  total_times_mutex.lock();
  for (unsigned int i = 0; i != 3; ++i)
    total_times[i] += times[i];
  total_times_mutex.unlock();

  db_disconnect(&mysql);
}


static void
main(void) {
  MYSQL mysql;
  mysql_init(&mysql);
  db_connect(&mysql);

  find_documents(&mysql, doc_ids);
  std::random_shuffle(doc_ids.begin(), doc_ids.end());

  if (NTHREADS < 2) {
    Converter converter;
    Doc::Schema schema;
    for (auto &doc_id : doc_ids)
      process_document(&mysql, doc_id, converter, schema, total_times);
  }
  else {
    std::vector<std::thread *> threads;
    for (unsigned int i = 0; i != NTHREADS; ++i)
      threads.push_back(new std::thread(process_documents_thread));
    for (auto &thread : threads) {
      thread->join();
      delete thread;
    }
  }


  std::chrono::milliseconds::rep ms;
  ms = std::chrono::duration_cast<std::chrono::milliseconds>(total_times[0]).count();
  std::cout << "Loading from MySQL: " << ms << "ms" << std::endl;
  ms = std::chrono::duration_cast<std::chrono::milliseconds>(total_times[1]).count();
  std::cout << "Converting to docrep: " << ms << "ms" << std::endl;
  ms = std::chrono::duration_cast<std::chrono::milliseconds>(total_times[2]).count();
  std::cout << "Writing to disk: " << ms << "ms" << std::endl;

  db_disconnect(&mysql);
}

}  // namespace to_dr
}  // namespace ontonotes5


int
main(void) {
  struct stat st;
  int ret = stat(OUTPUT_DIR, &st);
  if (ret == -1) {
    const int errnum = errno;
    if (errnum == ENOENT)
      mkdir(OUTPUT_DIR, 0755);
    else {
      std::cerr << std::strerror(errnum) << std::endl;
      return 1;
    }
  }

  ontonotes5::to_dr::main();
  return 0;
}

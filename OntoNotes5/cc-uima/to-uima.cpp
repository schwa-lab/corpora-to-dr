#include <algorithm>
#include <cassert>
#include <chrono>
#include <cstring>
#include <fstream>
#include <iostream>
#include <mutex>
#include <sstream>
#include <string>
#include <thread>
#include <vector>

#include <uima/api.hpp>

#include <sys/stat.h>
#include <sys/types.h>
#include <unistd.h>

#include "db.h"
#include "models-db.h"
#include "times.h"


namespace ontonotes5 {
namespace to_uima {

static std::vector<std::string> doc_ids;
static volatile size_t doc_ids_upto = 0;
static std::mutex doc_ids_lock;

static std::mutex uima_framework_lock;


static void
check_error(const uima::TyErrorId err, const uima::AnalysisEngine &ae) {
  if (err != UIMA_ERR_NONE) {
    std::cerr << std::endl << "   *** ExampleApplication - Error info:" << std::endl;
    std::cerr << "Error number        : " << err << std::endl;
    std::cerr << "Error string        : " << uima::AnalysisEngine::getErrorIdAsCString(err) << std::endl;
    const TCHAR *errStr = ae.getAnnotatorContext().getLogger().getLastErrorAsCStr();
    if (errStr != nullptr)
      std::cerr << "  Last logged message : "  << errStr << std::endl;
    std::exit((int)err);
  }
}


static void
check_error(const uima::ErrorInfo &err) {
  if (err.getErrorId() != UIMA_ERR_NONE) {
    std::cerr << std::endl << "   *** ExampleApplication - Error info:" << std::endl;
    std::cerr << "Error string  : " << uima::AnalysisEngine::getErrorIdAsCString(err.getErrorId());
    std::cerr << err << std::endl;
    std::exit((int)err.getErrorId());
  }
}


static std::string
build_document_text(MYSQL *const mysql, const std::string &doc_id) {
  std::ostringstream ss;
  bool first = true;

  std::stringstream query_ss;
  MYSQL_RES *result;
  MYSQL_ROW row;
  unsigned long *lengths;
  unsigned int nrows;

  query_ss << "SELECT string FROM sentence WHERE document_id = \"" << doc_id << "\" ORDER BY sentence_index";
  const std::string query = query_ss.str();
  db_query(mysql, query.c_str());
  result = mysql_use_result(mysql);
  for (nrows = 0; (row = mysql_fetch_row(result)); ++nrows) {
    lengths = mysql_fetch_lengths(result);
    if (first)
      first = false;
    else
      ss << ' ';
    ss.write(row[0], lengths[0]);
  }
  mysql_free_result(result);
  assert(nrows > 0);

  return ss.str();
}


static void
process_doc(MYSQL *const mysql, const std::string &doc_id, uima::AnalysisEngine &ae, uima::CAS &cas, uima::Type Document, uima::Feature Document__docId) {
  icu::UnicodeString ustr;
  const time_point_t loading_start = clock_t::now();

  // Construct a unicode version of the document text and set it on the CAS.
  std::string doc_text = build_document_text(mysql, doc_id);
  ustr = icu::UnicodeString(doc_text.c_str(), doc_text.size(), "utf-8");
  cas.setDocumentText(ustr.getBuffer(), ustr.length(), true);

  // Annotate the document with its doc_id.
  ustr = icu::UnicodeString(doc_id.c_str(), doc_id.size(), "utf-8");
  uima::AnnotationFS fs = cas.createAnnotation(Document, 0, ustr.length());
  fs.setStringValue(Document__docId, ustr);
  cas.getIndexRepository().addFS(fs);

  const time_point_t loading_end = clock_t::now();
  add_loading_time(loading_end - loading_start);

  // Convert the object to UIMA annotations.
  uima::TyErrorId err = ae.process(cas);
  check_error(err, ae);
}


static uima::AnalysisEngine *
load_ae(void) {
  // Initialize engine with filename of config file.
  uima::ErrorInfo err;
  uima_framework_lock.lock();
  uima::AnalysisEngine *const ae = uima::Framework::createAnalysisEngine("desc/DBAnnotatorDescriptor.xml", err);
  uima_framework_lock.unlock();
  check_error(err);
  return ae;
}


static void
main_thread(void) {
  MYSQL mysql;
  mysql_init(&mysql);
  db_connect(&mysql);

  // Load the UIMA AE and construct a CAS.
  uima::AnalysisEngine *const ae = load_ae();
  uima::CAS *const cas = ae->newCAS();

  // Load the document annotation type fropm the type system.
  const uima::TypeSystem &types = cas->getTypeSystem();
  uima::Type Document = types.getType("ontonotes5.to_uima.types.Document");
  uima::Feature Document__docId = Document.getFeatureByBaseName("docId");

  // Process each document.
  bool found = true;
  size_t doc_id_index = 0;
  while (found) {
    doc_ids_lock.lock();
    if (doc_ids_upto == doc_ids.size())
      found = false;
    else {
      found = true;
      doc_id_index = doc_ids_upto++;
    }
    doc_ids_lock.unlock();

    if (found) {
      std::cout << "Processing " << doc_id_index << "/" << doc_ids.size() << ": " << doc_ids[doc_id_index] << std::endl;
      process_doc(&mysql, doc_ids[doc_id_index], *ae, *cas, Document, Document__docId);
      cas->reset();
    }
  }

  // Call collectionProcessComplete and free up resources.
  uima::TyErrorId err;
  err = ae->collectionProcessComplete();
  check_error(err, *ae);
  err = ae->destroy();
  check_error(err, *ae);
  delete cas;
  delete ae;

  db_disconnect(&mysql);
}


static void
main(void) {
  // Create/link up to a resource manager instance (singleton)
  uima::ResourceManager::createInstance("UIMACPP_EXAMPLE_APPLICATION");

  // Load the list of documents from the DB.
  MYSQL mysql;
  mysql_init(&mysql);
  db_connect(&mysql);
  find_documents(&mysql, doc_ids);
  std::random_shuffle(doc_ids.begin(), doc_ids.end());
  db_disconnect(&mysql);

//#if 0
  // Process the documents in parallel.
  std::vector<std::thread *> threads;
  for (size_t i = 0; i != 16; ++i)
    threads.push_back(new std::thread(main_thread));
  for (auto &thread : threads) {
    thread->join();
    delete thread;
  }
//#endif
  //main_thread();

  output_times();
}

}  // namespace to_uima
}  // namespace ontonotes5


int
main(void) {
  struct stat st;
  int ret = ::stat(ontonotes5::to_uima::OUTPUT_DIR, &st);
  if (ret == -1) {
    const int errnum = errno;
    if (errnum == ENOENT)
      ::mkdir(ontonotes5::to_uima::OUTPUT_DIR, 0755);
    else {
      std::cerr << std::strerror(errnum) << std::endl;
      return 1;
    }
  }

  ontonotes5::to_uima::main();
  return 0;
}

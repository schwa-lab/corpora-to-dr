#include "times.h"

#include <iostream>
#include <mutex>


namespace ontonotes5 {
namespace to_uima {

const char *OUTPUT_DIR = "/tmp/cc-uima";

static volatile uint64_t times[5] = {0, 0, 0, 0, 0};
static std::mutex times_lock;


static inline void
add_delta(const size_t index, const duration_t delta) {
  times_lock.lock();
  times[index] += std::chrono::duration_cast<std::chrono::milliseconds>(delta).count();
  times_lock.unlock();
}


void
add_loading_time(const duration_t delta) {
  add_delta(0, delta);
}


void
add_converting_delta(const duration_t delta) {
  add_delta(1, delta);
}


void
add_writing_binary_delta(const duration_t delta) {
  add_delta(2, delta);
}


void
add_writing_xcas_delta(const duration_t delta) {
  add_delta(3, delta);
}


void
add_writing_xmi_delta(const duration_t delta) {
  add_delta(4, delta);
}



void
output_times(void) {
  times_lock.lock();
  std::cout << "Loading from MySQL: " << times[0] << "ms" << std::endl;
  std::cout << "Converting to docrep: " << times[1] << "ms" << std::endl;
  std::cout << "Writing binary disk: " << times[2] << "ms" << std::endl;
  std::cout << "  Writing XCAS disk: " << times[3] << "ms" << std::endl;
  std::cout << "   Writing XMI disk: " << times[4] << "ms" << std::endl;
  times_lock.unlock();
}

}  // namespace to_uima
}  // namespace ontonotes5

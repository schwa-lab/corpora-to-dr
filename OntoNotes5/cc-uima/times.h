#ifndef STATS_H_
#define STATS_H_

#include <chrono>


namespace ontonotes5 {
  namespace to_uima {

    extern const char *OUTPUT_DIR;

    using clock_t = std::chrono::high_resolution_clock;
    using duration_t = clock_t::duration;
    using time_point_t = clock_t::time_point;

    void add_loading_time(duration_t delta);
    void add_converting_delta(duration_t delta);
    void add_writing_binary_delta(duration_t delta);
    void add_writing_xcas_delta(duration_t delta);
    void add_writing_xmi_delta(duration_t delta);

    void output_times(void);

  }
}

#endif // STATS_H_

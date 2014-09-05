DEPS=$(shell cd $(shell pwd)/../../dr-benchmark/deps/prefix && pwd)
PKG_CONFIG_PATH=$(DEPS)/lib/pkgconfig/

MYSQL_CFLAGS = $(shell mysql_config --cflags)
MYSQL_LIBS = $(shell mysql_config --libs_r)

BSON_CFLAGS = $(shell env PKG_CONFIG_PATH=$(PKG_CONFIG_PATH) pkg-config --cflags libbson-1.0 | sed 's/-I/-isystem /g')
BSON_LIBS = $(shell env PKG_CONFIG_PATH=$(PKG_CONFIG_PATH) pkg-config --libs libbson-1.0)

DOCREP_CFLAGS = -I$(DEPS)/include
DOCREP_LIBS = -L$(DEPS)/lib -lschwa

PROTOC = $(DEPS)/bin/protoc
PROTO_HEADERS = gen-protobuf/conll03.pb.h
PROTO_OBJS = gen-protobuf/conll03.pb.o
PROTO_CFLAGS = $(shell env PKG_CONFIG_PATH=$(PKG_CONFIG_PATH) pkg-config --cflags protobuf)
PROTO_LIBS = $(shell env PKG_CONFIG_PATH=$(PKG_CONFIG_PATH) pkg-config --libs protobuf)

THRIFT = $(DEPS)/bin/thrift
THRIFT_HEADERS = gen-thrift/conll03_types.h gen-thrift/conll03_constants.h
THRIFT_OBJS = gen-thrift/conll03_types.o gen-thrift/conll03_constants.o
THRIFT_CFLAGS = $(shell env PKG_CONFIG_PATH=$(PKG_CONFIG_PATH) pkg-config --cflags thrift)
THRIFT_LIBS = $(shell env PKG_CONFIG_PATH=$(PKG_CONFIG_PATH) pkg-config --libs thrift)

LZMA_CFLAGS = $(shell env PKG_CONFIG_PATH=$(PKG_CONFIG_PATH) pkg-config --cflags liblzma)
LZMA_LIBS = $(shell env PKG_CONFIG_PATH=$(PKG_CONFIG_PATH) pkg-config --libs liblzma)

SNAPPY_CFLAGS = -I$(DEPS)/include
SNAPPY_LIBS = -L$(DEPS)/lib -lsnappy

ZLIB_CFLAGS = $(shell pkg-config --cflags zlib)
ZLIB_LIBS = $(shell pkg-config --libs zlib)

CXX = g++
CXXFLAGS = -std=gnu++11 -W -Wall -Wextra -pedantic -O4 -g $(MYSQL_CFLAGS) $(DOCREP_CFLAGS)
LDFLAGS = $(MYSQL_LIBS) $(DOCREP_LIBS)
#CXXFLAGS = -std=gnu++11 -W -Wall -Wextra -pedantic -O4 -g $(MYSQL_CFLAGS) $(BSON_CFLAGS) $(DOCREP_CFLAGS) $(PROTO_CFLAGS) $(THRIFT_CFLAGS) $(LZMA_CFLAGS) $(SNAPPY_CFLAGS) $(ZLIB_CFLAGS)
#LDFLAGS = $(MYSQL_LIBS) $(BSON_LIBS) $(DOCREP_LIBS) $(PROTO_LIBS) $(THRIFT_LIBS) $(LZMA_LIBS) $(SNAPPY_LIBS) $(ZLIB_LIBS)

BINARIES = to-dr  # benchmark

BENCHMARK_OBJS = benchmark.o compressor.o compressor_lzma.o compressor_snappy.o compressor_zlib.o manager.o manager_bson.o manager_docrep.o manager_protobuf.o manager_thrift.o models.o


.PHONY: all clean depends generated run

all: $(BINARIES)

clean:
	-rm -f *.o
	-rm -f $(BINARIES)
	-rm -rf gen-protobuf gen-thrift

depends: generated
	rm -f Makefile.depends
	for f in `find . -name '*.cc'`; do \
		$(CXX) $(CXXFLAGS) -MM $$f >> Makefile.depends; \
	done

generated:

run: benchmark
	./benchmark

benchmark: $(BENCHMARK_OBJS) $(PROTO_OBJS) $(THRIFT_OBJS) $(BSON_OBJS)
	$(CXX) -static-libgcc -Wl,-static -o $@ $^ $(LDFLAGS)

to-dr: to-dr.o db.o db-to-dr.o models-db.o models-dr.o
	$(CXX) -o $@ $^ $(LDFLAGS)

run-to-dr: to-dr
	env LD_LIBRARY_PATH="$(DEPS)/lib" ./to-dr

gen-protobuf/conll03.pb.h: conll03.proto
	mkdir -p gen-protobuf
	$(PROTOC) $< --cpp_out=gen-protobuf

gen-protobuf/conll03.pb.cc: conll03.proto
	mkdir -p gen-protobuf
	$(PROTOC) $< --cpp_out=gen-protobuf

gen-thrift/%.cpp: conll03.thrift
	mkdir -p gen-thrift
	$(THRIFT) --gen cpp -out gen-thrift $<

gen-thrift/%.h: conll03.thrift
	mkdir -p gen-thrift
	$(THRIFT) --gen cpp -out gen-thrift $<

-include Makefile.depends

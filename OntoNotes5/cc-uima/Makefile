include Makefile.common

.PHONY: all clean compile run

all: compile

clean:
	rm -f *.so *.o
	rm -f to-uima

compile: to-uima

to-uima: .force
	$(MAKE) -f Makefile.DBAnnotator
	$(MAKE) -f Makefile.to-uima

run: to-uima
	env LD_LIBRARY_PATH="$(DEPS)/lib:." ./to-uima

run-gdb: to-uima
	env LD_LIBRARY_PATH="$(DEPS)/lib:." gdb --args ./to-uima

.force:

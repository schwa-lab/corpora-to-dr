#!/bin/bash
# =================================================================================================
# The code to convert the WSJ sections of the Tipster corpus (LDC93T3A) to docrep is implemented in
# C++ and is provided as part of libschwa. This is a simple shell script to perform the
# (de)compression of inputs and outputs.
#
# The code below uses GNU `parallel` to perform the conversion in parallel across all available
# cores. If you don't have parallel installed, you can simply replace it with a bash for loop,
# updating the parallel special variables `{}` and `{/.}` appropriately.
#
# As a rough estimate of conversion time, this conversion process took 55 seconds to run on one of
# our servers, utilising 16 cores.
# =================================================================================================
set -e

# =================================================================================================
# Input and output locations. These should be changed appropriately.
# =================================================================================================
OUTPUT_DIR=$(pwd)
TIPSTER_DIR=~schwa/corpora/raw/Tipster

# =================================================================================================
# Main code below.
# =================================================================================================
TIPSTER1_DIR=${TIPSTER_DIR}/Tipster1/WSJ
TIPSTER2_DIR=${TIPSTER_DIR}/Tipster2/WSJ

# Create the output directory.
mkdir -p ${OUTPUT_DIR}/Tipster

# Process each year of the WSJ data in turn.
for year in 1987 1988 1989; do
  mkdir -p ${OUTPUT_DIR}/Tipster/${year}
  find ${TIPSTER1_DIR}/${year} -name '*.Z' \
    | parallel "zcat {} \
      | corpora-to-dr --corpus tipster 2> ${OUTPUT_DIR}/Tipster/${year}/{/.}.log \
      | gzip -9 > ${OUTPUT_DIR}/Tipster/${year}/{/.}.dr.gz"
done

for year in 1990 1991 1992; do
  mkdir -p ${OUTPUT_DIR}/Tipster/${year}
  find ${TIPSTER2_DIR}/${year} -name '*.Z' \
    | parallel "zcat {} \
      | corpora-to-dr --corpus tipster 2> ${OUTPUT_DIR}/Tipster/${year}/{/.}.log \
      | gzip -9 > ${OUTPUT_DIR}/Tipster/${year}/{/.}.dr.gz"
done

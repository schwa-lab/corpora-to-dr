#!/bin/bash
# =================================================================================================
# The code to convert the NANC corpus (LDC2008T15) to docrep is implemented in C++ and is provided
# as part of libschwa. This is a simple shell script to perform the (de)compression of inputs and
# outputs, as well as to do necessary pre-processing for the WSJ articles in the corpus. The WSJ
# articles have not been properly SGML-escaped before being placed into a SGML document, so a
# standard SGML/XML parser dies when trying to parse them.
#
# The code below uses GNU `parallel` to perform the conversion in parallel across all available
# cores. If you don't have parallel installed, you can simply replace it with a bash for loop,
# updating the parallel special variables `{}` and `{/.}` appropriately.
#
# As a rough estimate of conversion time, this conversion process took 20 minutes to run on one of
# our servers, utilising 16 cores.
# =================================================================================================
set -e

# =================================================================================================
# Input and output locations. These should be changed appropriately.
# =================================================================================================
NANC_DIR=~schwa/corpora/raw/NANC/data
OUTPUT_DIR=$(pwd)


# =================================================================================================
# Main code below.
# =================================================================================================
# Create the output directory.
mkdir -p ${OUTPUT_DIR}/NANC

# Process each input directory in turn.
for d in $(find ${NANC_DIR} -maxdepth 1 -mindepth 1 -type d | sort); do
  # Create the output directory for the input directory.
  echo ${d}
  d=$(basename ${d})
  mkdir -p ${OUTPUT_DIR}/NANC/${d}

  # Process all non-WSJ articles as is.
  find ${NANC_DIR}/${d} -type f -name '*.gz' \
    | grep -v '/ws[0-9]*\.gz$' \
    | parallel "zcat {} \
      | corpora-to-dr --corpus=nanc 2> ${OUTPUT_DIR}/NANC/${d}/{/.}.log \
      | gzip -9 > ${OUTPUT_DIR}/NANC/${d}/{/.}.dr.gz"

  # Pre-process the WSJ articles before attempting to convert them as their content isn't SGML
  # escaped ... why?! >___< The sed replacements below are a complete hack and will only work for
  # the data that's in this corpus. They were manually found in the data.
  find ${NANC_DIR}/${d} -type f -name '*.gz' \
    | grep '/ws[0-9]*\.gz$' \
    | parallel "zcat {} \
      | sed 's/&/\\&amp;/g; s/<----/\\&lt;----/g; s/<</\\&lt;\\&lt;/g; s/<0\\.05/\\&lt;0.05/g; s/<\$/\\&lt;/; s/>>/\\&gt;\\&gt;/g; s/Delaware>/Delaware\\&gt;/g; s/CATEGORY>/CATEGORY\\&gt;/g;' \
      | corpora-to-dr --corpus=nanc 2> ${OUTPUT_DIR}/NANC/${d}/{/.}.log \
      | gzip -9 > ${OUTPUT_DIR}/NANC/${d}/{/.}.dr.gz"
done

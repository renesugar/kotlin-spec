source directories.sh
source ../../../scripts/build/settings.sh

init_settings "pdf"

export PROJECT_DIR

cd ${PROJECT_DIR}/src/md

gpp -H ./index.md | pandoc \
--filter ${FILTERS_DIR}/processTodoFilter.sh \
--filter ${FILTERS_DIR}/markSentencesFilter.sh \
--filter ${FILTERS_DIR}/copyPasteFilter.sh \
--filter ${FILTERS_DIR}/inlineDiagrams.sh \
--filter ${FILTERS_DIR}/inlineCodeIndenter.sh \
--filter ${FILTERS_DIR}/mathCleanUpFilter.sh \
${PREAMBLE_OPTIONS} \
${COMMON_PANDOC_OPTIONS} \
${TOC_PANDOC_OPTIONS} \
--variable documentclass=book \
-o ${BUILD_DIR}/pdf/kotlin-spec.pdf

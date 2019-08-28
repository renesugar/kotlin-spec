source directories.sh

export PROJECT_DIR

cd ${PROJECT_DIR}/src/md

gpp -H ./index.md | pandoc \
--filter ${FILTERS_DIR}/processTodoFilter.sh \
--filter ${FILTERS_DIR}/markSentencesFilter.sh \
--filter ${FILTERS_DIR}/copyPasteFilter.sh \
--filter ${FILTERS_DIR}/inlineDiagrams.sh \
--filter ${FILTERS_DIR}/inlineCodeIndenter.sh \
--filter ${FILTERS_DIR}/mathCleanUpFilter.sh \
--toc --toc-depth=6 -H ./preamble.tex --variable documentclass=book \
-s -f markdown-raw_html+smart+tex_math_double_backslash \
-o ${BUILD_DIR}/pdf/kotlin-spec.pdf

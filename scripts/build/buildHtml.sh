source directories.sh

export PROJECT_DIR

cd ${PROJECT_DIR}/docs/src/md

gpp -H ./index.md | pandoc \
--filter ${FILTERS_DIR}/processTodoFilter.sh \
--filter ${FILTERS_DIR}/markSentencesFilter.sh \
--filter ${FILTERS_DIR}/copyPasteFilter.sh \
--filter ${FILTERS_DIR}/inlineDiagrams.sh \
--filter ${FILTERS_DIR}/inlineCodeIndenter.sh \
--toc --toc-depth=6 -c ./${RESOURCES_DIR}/css/main.css \
--katex=${BUILD_DIR}/html/resources/js/katex/ \
-H ./preamble.md -s -f markdown-raw_html+smart+tex_math_double_backslash \
-o ${BUILD_DIR}/html/kotlin-spec.html

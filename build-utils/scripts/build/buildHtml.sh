source directories.sh

export PROJECT_DIR

cd ${PROJECT_DIR}/docs

gpp -H ./index.md | pandoc \
--filter ../${HELPERS_DIRECTORY}/processTodoFilter.sh \
--filter ../${HELPERS_DIRECTORY}/markSentencesFilter.sh \
--filter ../${HELPERS_DIRECTORY}/copyPasteFilter.sh \
--filter ../${HELPERS_DIRECTORY}/inlineDiagramFilter.sh \
--filter ../${HELPERS_DIRECTORY}/inlineCodeIndentFilter.sh \
--filter ../${HELPERS_DIRECTORY}/mathCleanUpFilter.sh \
--toc --toc-depth=6 -c ./${ASSETS_DIRECTORY}/css/main.css \
--katex=./${ASSETS_DIRECTORY}/js/katex/ \
-H ./preamble.md -s -f markdown-raw_html+smart+tex_math_double_backslash \
-o ../${BUILD_DIRECTORY}/html/kotlin-spec.html

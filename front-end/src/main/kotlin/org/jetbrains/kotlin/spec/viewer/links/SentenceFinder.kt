package org.jetbrains.kotlin.spec.viewer.links

import js.externals.jquery.`$`
import org.jetbrains.kotlin.spec.viewer.links.SpecPlaceHighlighter.getSentenceInfoFromSearchField
import org.jetbrains.kotlin.spec.viewer.links.SpecPlaceHighlighter.highlightParagraph
import org.jetbrains.kotlin.spec.viewer.links.SpecPlaceHighlighter.highlightSentence

object SentenceFinder {
    const val FINDER_BAR_HTML = """<div class="sentence-finder-bar">
        <div class="icon-menu">
                <span class="divide"></span>
                <span class="divide"></span>
                <span class="divide"></span>
        </div>
        <div class="spec-location-search">
            <input type="text" name="spec-sentence-location" />
            <button class="spec-sentence-find">Find sentence</button>
            <a href="#" class="{1}-markup-link">{2} markup</a>
        </div>
        <div class="spec-location-format">Format: sections-hierarchy{,} -> paragraph {n} -> sentence {m}</div>
        </div>"""

    fun findSentence() =
            findSentence(`$`(".spec-location-search input[name=\"spec-sentence-location\"]").`val`().toString())

    private fun findSentence(place: String) {
        val specPlaceComponents = getSentenceInfoFromSearchField(place.trimEnd())

        if (specPlaceComponents.sentenceNumber != null) {
            highlightSentence(specPlaceComponents)
        } else {
            highlightParagraph(specPlaceComponents)
        }
    }
}

package com.niyuva.app.domain.model

data class Article(
    val topicId: String,
    val title: String,
    val blocks: List<ContentBlock>
)

sealed class ContentBlock {
    data class SectionHeader(val text: String) : ContentBlock()
    data class BodyText(val text: String) : ContentBlock()
    data class BulletList(val items: List<String>) : ContentBlock()
    data class MythFact(val myth: String, val fact: String) : ContentBlock()
    data class QuoteBlock(val quote: String, val attribution: String? = null) : ContentBlock()
    data class InfoCallout(val label: String, val body: String, val accentColor: String = "#D4956A") : ContentBlock()
    data class DividerBlock(val label: String? = null) : ContentBlock()
    data class ComparisonTable(
        val headers: List<String>,
        val rows: List<List<String>>
    ) : ContentBlock()
}

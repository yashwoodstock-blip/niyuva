package com.niyuva.app.domain.model

data class StoryCard(
    val id: String,
    val title: String,
    val category: String,
    val thumbnailDescription: String,  // owner replaces with actual images
    val previewText: String,
    val isNew: Boolean = false
)

data class VedicCard(
    val id: String,
    val chapterTitle: String,
    val firstLine: String,
    val chapterNumber: Int
)

data class AnimationCard(
    val id: String,
    val title: String,
    val durationSeconds: Int,
    val thumbnailDescription: String
)

data class PehliBaarStory(
    val id: String,
    val title: String,
    val subtitle: String,
    val paragraphs: List<String>,
    val quote: String?,
    val keyMessage: String,
    val source: String,
    val backgroundColorHex: String,
    val textColorHex: String
)


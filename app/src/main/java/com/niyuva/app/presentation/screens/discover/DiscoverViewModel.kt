package com.niyuva.app.presentation.screens.discover

import androidx.lifecycle.ViewModel
import com.niyuva.app.data.local.content.DiscoverContentRepository
import com.niyuva.app.domain.model.StoryCard
import com.niyuva.app.domain.model.VedicCard
import com.niyuva.app.domain.model.AnimationCard
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val repository: DiscoverContentRepository
) : ViewModel() {

    private val _stories = MutableStateFlow<List<StoryCard>>(emptyList())
    val stories: StateFlow<List<StoryCard>> = _stories.asStateFlow()

    private val _vedicCards = MutableStateFlow<List<VedicCard>>(emptyList())
    val vedicCards: StateFlow<List<VedicCard>> = _vedicCards.asStateFlow()

    private val _animations = MutableStateFlow<List<AnimationCard>>(emptyList())
    val animations: StateFlow<List<AnimationCard>> = _animations.asStateFlow()

    init {
        _stories.value = repository.stories
        _vedicCards.value = repository.vedicCards
        _animations.value = repository.animations
    }
}

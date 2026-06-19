package com.niyuva.app.presentation.screens.body

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class BodyViewModel @Inject constructor() : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    val filteredTopics: StateFlow<List<BodyTopic>> = _searchQuery
        .map { query ->
            if (query.isBlank()) {
                BodyTopics.all
            } else {
                BodyTopics.all.filter { topic ->
                    topic.title.contains(query, ignoreCase = true)
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = BodyTopics.all
        )

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }
}

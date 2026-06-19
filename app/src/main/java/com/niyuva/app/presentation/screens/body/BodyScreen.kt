package com.niyuva.app.presentation.screens.body

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.compose.ui.text.style.TextAlign
import com.niyuva.app.presentation.components.BodyTopicCard
import com.niyuva.app.presentation.components.NiyuvaSearchBar
import com.niyuva.app.presentation.components.NiyuvaTextLink
import com.niyuva.app.presentation.navigation.NavRoutes
import com.niyuva.app.presentation.theme.DeepWarmBrown
import com.niyuva.app.presentation.theme.DustyMauve
import com.niyuva.app.presentation.theme.NunitoFamily
import com.niyuva.app.presentation.theme.WarmIvory
import com.niyuva.app.presentation.theme.WarmLinen

@Composable
fun BodyScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    viewModel: BodyViewModel = hiltViewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val filteredTopics by viewModel.filteredTopics.collectAsStateWithLifecycle()

    val view = androidx.compose.ui.platform.LocalView.current
    if (!view.isInEditMode) {
        androidx.compose.runtime.SideEffect {
            val window = (view.context as? android.app.Activity)?.window
            if (window != null) {
                window.statusBarColor = android.graphics.Color.TRANSPARENT
                val insetsController = androidx.core.view.WindowCompat.getInsetsController(window, view)
                insetsController.isAppearanceLightStatusBars = true // Dark icons
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(WarmIvory)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                top = contentPadding.calculateTopPadding(),
                bottom = androidx.compose.foundation.layout.WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 64.dp + 16.dp
            )
        ) {
            // Status bar spacer
            item(key = "status_bar_spacer") {
                Spacer(modifier = Modifier.statusBarsPadding())
            }

            // Search Bar
            item(key = "search_bar") {
                Spacer(modifier = Modifier.height(12.dp))
                NiyuvaSearchBar(
                    query = searchQuery,
                    onQueryChanged = { viewModel.onSearchQueryChanged(it) },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            // Section label "Aaj ke liye padho 📚"
            item(key = "section_label") {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Aaj ke liye padho 📚",
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = DustyMauve,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            if (filteredTopics.isEmpty()) {
                item(key = "empty_state") {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp, horizontal = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = androidx.compose.ui.res.stringResource(id = com.niyuva.app.R.string.empty_search_results, searchQuery),
                            fontFamily = NunitoFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            color = DustyMauve,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        NiyuvaTextLink(
                            text = androidx.compose.ui.res.stringResource(id = com.niyuva.app.R.string.link_clear_search),
                            onClick = { viewModel.onSearchQueryChanged("") }
                        )
                    }
                }
            } else {
                // 2-column topic grid
                val topicRows = filteredTopics.chunked(2)
                topicRows.forEachIndexed { index, rowTopics ->
                    item(key = "topic_row_${rowTopics.map { it.id }.joinToString("_")}_$index") {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            rowTopics.forEach { topic ->
                                BodyTopicCard(
                                    topic = topic,
                                    onClick = {
                                        navController.navigate(NavRoutes.BodyArticle.createRoute(topic.id))
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            if (rowTopics.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}

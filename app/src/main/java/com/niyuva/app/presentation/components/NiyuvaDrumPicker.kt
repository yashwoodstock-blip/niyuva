package com.niyuva.app.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.niyuva.app.presentation.theme.*
import kotlin.math.abs

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NiyuvaDrumPicker(
    values: List<Int>,
    defaultValue: Int,
    onValueSelected: (Int?) -> Unit,
    isUnknown: Boolean,
    onScrollStarted: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    val initialIndex = remember { values.indexOf(defaultValue).coerceAtLeast(0) }
    val lazyListState = rememberLazyListState(initialFirstVisibleItemIndex = initialIndex)
    val snapFlingBehavior = rememberSnapFlingBehavior(lazyListState = lazyListState)

    // Track scroll start to clear "Pata nahi" state
    LaunchedEffect(lazyListState.isScrollInProgress) {
        if (lazyListState.isScrollInProgress) {
            onScrollStarted()
        }
    }

    // Determine the index closest to the center
    val centerIndex by remember {
        derivedStateOf {
            val layoutInfo = lazyListState.layoutInfo
            val visibleItemsInfo = layoutInfo.visibleItemsInfo
            if (visibleItemsInfo.isEmpty()) return@derivedStateOf initialIndex

            val viewportCenter = (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2
            visibleItemsInfo.minByOrNull {
                val itemCenter = (it.offset + it.offset + it.size) / 2
                abs(itemCenter - viewportCenter)
            }?.index ?: initialIndex
        }
    }

    // Notify listener when snapped to centerIndex (only if not in unknown state)
    LaunchedEffect(centerIndex) {
        if (!isUnknown && centerIndex in values.indices) {
            onValueSelected(values[centerIndex])
            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth(0.85f)
            .height(260.dp) // 52dp * 5 items
            .clip(RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        // Highlighted background for selected item
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .background(WarmLinen)
                .align(Alignment.Center)
        ) {
            // Left border indicator
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(DeepPlumRose)
                    .align(Alignment.CenterStart)
            )
        }

        LazyColumn(
            state = lazyListState,
            flingBehavior = snapFlingBehavior,
            contentPadding = PaddingValues(vertical = 104.dp), // 52dp * 2 padding top/bottom
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(values) { index, item ->
                val distance = abs(index - centerIndex)

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (distance == 0 && isUnknown) {
                        // Display "Pata nahi" inside the selected band
                        Text(
                            text = "Pata nahi",
                            fontFamily = NunitoFamily,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Italic,
                            color = DustyMauve,
                            textAlign = TextAlign.Center
                        )
                    } else {
                        val opacity = when (distance) {
                            0 -> 1.0f
                            1 -> 0.6f
                            2 -> 0.3f
                            else -> 0.1f
                        }
                        val fontSize = when (distance) {
                            0 -> 22.sp
                            1 -> 18.sp
                            2 -> 16.sp
                            else -> 14.sp
                        }
                        val fontWeight = if (distance == 0) FontWeight.Bold else FontWeight.Normal
                        val textColor = if (distance == 0) DeepPlumRose else DeepWarmBrown

                        Text(
                            text = item.toString(),
                            fontFamily = NunitoFamily,
                            fontSize = fontSize,
                            fontWeight = fontWeight,
                            color = textColor,
                            modifier = Modifier.alpha(opacity),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

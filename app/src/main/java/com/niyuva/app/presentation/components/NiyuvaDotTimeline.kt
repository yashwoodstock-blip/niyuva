package com.niyuva.app.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.niyuva.app.presentation.theme.BlushMist
import com.niyuva.app.presentation.theme.DustyMauve
import com.niyuva.app.presentation.theme.NunitoFamily
import com.niyuva.app.presentation.theme.Terracotta
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun NiyuvaDotTimeline(
    cycleLength: Int,
    periodLength: Int,
    startDate: LocalDate,
    endDate: LocalDate,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val dotRadiusPx = with(density) { 4.dp.toPx() }
    val lineHeightPx = with(density) { 2.dp.toPx() }

    val totalDots = cycleLength.coerceAtMost(35)

    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val width = size.width
                val height = size.height
                val centerY = height / 2f

                val startX = dotRadiusPx
                val endX = width - dotRadiusPx
                val stepX = if (totalDots > 1) (endX - startX) / (totalDots - 1) else 0f

                // Draw connecting line
                drawLine(
                    color = BlushMist,
                    start = Offset(startX, centerY),
                    end = Offset(endX, centerY),
                    strokeWidth = lineHeightPx
                )

                // Draw dots
                for (i in 0 until totalDots) {
                    val cx = startX + i * stepX
                    val isPeriod = i < periodLength
                    val color = if (isPeriod) Terracotta else BlushMist
                    drawCircle(
                        color = color,
                        radius = dotRadiusPx,
                        center = Offset(cx, centerY)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Date labels below
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val formatter = DateTimeFormatter.ofPattern("MMM d")
            Text(
                text = startDate.format(formatter),
                fontFamily = NunitoFamily,
                fontSize = 11.sp,
                color = DustyMauve
            )
            Text(
                text = endDate.format(formatter),
                fontFamily = NunitoFamily,
                fontSize = 11.sp,
                color = DustyMauve
            )
        }
    }
}

package com.niyuva.app.presentation.components

import android.graphics.PointF
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.niyuva.app.presentation.theme.DeepWarmBrown
import com.niyuva.app.presentation.theme.DustyMauve
import com.niyuva.app.presentation.theme.NunitoFamily
import com.niyuva.app.presentation.theme.PureWhite

data class ChartLine(
    val label: String,
    val points: List<PointF>, // x is normalized 0..1 representing position, y is normalized 0..1 representing value
    val color: Color,
    val opacity: Float = 1.0f
)

@Composable
fun SymptomTrendChart(
    yAxisLabels: List<String>,
    xAxisLabels: List<String>,
    chartLines: List<ChartLine>,
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = PureWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(260.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val paddingLeft = 60.dp.toPx()
                    val paddingBottom = 30.dp.toPx()
                    val paddingTop = 10.dp.toPx()
                    val paddingRight = 10.dp.toPx()

                    val chartWidth = size.width - paddingLeft - paddingRight
                    val chartHeight = size.height - paddingTop - paddingBottom

                    // Draw Y-axis grid lines & labels
                    val yLabelCount = yAxisLabels.size
                    if (yLabelCount > 0) {
                        for (i in 0 until yLabelCount) {
                            val fraction = if (yLabelCount > 1) i.toFloat() / (yLabelCount - 1) else 0.5f
                            val y = paddingTop + (1f - fraction) * chartHeight

                            // Grid line
                            drawLine(
                                color = Color(0xFFF0E5E8),
                                start = Offset(paddingLeft, y),
                                end = Offset(paddingLeft + chartWidth, y),
                                strokeWidth = 1.dp.toPx()
                            )

                            // Label
                            val label = yAxisLabels[i]
                            val measuredText = textMeasurer.measure(
                                text = label,
                                style = TextStyle(
                                    fontFamily = NunitoFamily,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 11.sp,
                                    color = DustyMauve
                                )
                            )
                            drawText(
                                textLayoutResult = measuredText,
                                topLeft = Offset(
                                    x = paddingLeft - measuredText.size.width - 8.dp.toPx(),
                                    y = y - measuredText.size.height / 2f
                                )
                            )
                        }
                    }

                    // Draw X-axis labels
                    val xLabelCount = xAxisLabels.size
                    if (xLabelCount > 0) {
                        for (i in 0 until xLabelCount) {
                            val fraction = if (xLabelCount > 1) i.toFloat() / (xLabelCount - 1) else 0.5f
                            val x = paddingLeft + fraction * chartWidth

                            // Label
                            val label = xAxisLabels[i]
                            val measuredText = textMeasurer.measure(
                                text = label,
                                style = TextStyle(
                                    fontFamily = NunitoFamily,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 10.sp,
                                    color = DustyMauve
                                )
                            )
                            drawText(
                                textLayoutResult = measuredText,
                                topLeft = Offset(
                                    x = x - measuredText.size.width / 2f,
                                    y = paddingTop + chartHeight + 8.dp.toPx()
                                )
                            )
                        }
                    }

                    // Draw lines
                    chartLines.forEach { line ->
                        if (line.points.isNotEmpty()) {
                            val path = Path()
                            val sortedPoints = line.points.sortedBy { it.x }
                            
                            // Map points to canvas coordinates
                            val screenPoints = sortedPoints.map { p ->
                                Offset(
                                    x = paddingLeft + p.x * chartWidth,
                                    y = paddingTop + (1f - p.y) * chartHeight
                                )
                            }

                            // Draw smooth curve using Cubic Bezier
                            path.moveTo(screenPoints[0].x, screenPoints[0].y)
                            for (i in 0 until screenPoints.size - 1) {
                                val p1 = screenPoints[i]
                                val p2 = screenPoints[i + 1]
                                val controlX1 = p1.x + (p2.x - p1.x) / 2f
                                val controlY1 = p1.y
                                val controlX2 = p1.x + (p2.x - p1.x) / 2f
                                val controlY2 = p2.y
                                path.cubicTo(
                                    controlX1, controlY1,
                                    controlX2, controlY2,
                                    p2.x, p2.y
                                )
                            }

                            // Draw path
                            drawPath(
                                path = path,
                                color = line.color.copy(alpha = line.opacity),
                                style = Stroke(
                                    width = 3.dp.toPx(),
                                    cap = StrokeCap.Round
                                )
                            )

                            // Draw dot markers
                            screenPoints.forEach { point ->
                                drawCircle(
                                    color = PureWhite,
                                    radius = 5.dp.toPx(),
                                    center = point
                                )
                                drawCircle(
                                    color = line.color.copy(alpha = line.opacity),
                                    radius = 3.dp.toPx(),
                                    center = point
                                )
                            }
                        }
                    }
                }
            }

            // Legend for multi-line flow comparison
            val legendLines = chartLines.filter { it.label.isNotBlank() }
            if (legendLines.size > 1) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    legendLines.forEachIndexed { idx, line ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(width = 12.dp, height = 4.dp)
                                    .background(line.color.copy(alpha = line.opacity), RoundedCornerShape(2.dp))
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = line.label,
                                fontFamily = NunitoFamily,
                                fontSize = 11.sp,
                                color = DeepWarmBrown
                            )
                        }
                    }
                }
            }
        }
    }
}

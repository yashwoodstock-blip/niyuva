package com.niyuva.app.presentation.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.niyuva.app.presentation.theme.BlushMist
import com.niyuva.app.presentation.theme.CyclePhase
import com.niyuva.app.presentation.theme.DeepWarmBrown
import com.niyuva.app.presentation.theme.DustyMauve
import com.niyuva.app.presentation.theme.NunitoFamily
import com.niyuva.app.presentation.theme.PhaseThemeData
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import com.niyuva.app.presentation.theme.PhaseColorMenstruation

// ─────────────────────────────────────────────────────────────────────────────
// Phase display helpers
// ─────────────────────────────────────────────────────────────────────────────

internal fun CyclePhase.displayName(): String = when (this) {
    CyclePhase.MENSTRUATION -> "Menstruation Phase"
    CyclePhase.FOLLICULAR   -> "Follicular Phase"
    CyclePhase.OVULATION    -> "Ovulation Phase"
    CyclePhase.LUTEAL       -> "Luteal Phase"
}

internal fun CyclePhase.moodEmoji(): String = when (this) {
    CyclePhase.MENSTRUATION -> "😌"
    CyclePhase.FOLLICULAR   -> "☺️"
    CyclePhase.OVULATION    -> "✨"
    CyclePhase.LUTEAL       -> "🌙"
}

// ─────────────────────────────────────────────────────────────────────────────
// NiyuvaCycleRingArc
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Animated semicircular arc that represents cycle day progress.
 *
 * Architecture:
 *  - A [Box] at 180dp height hosts the [Canvas] (fillMaxSize) and a centered text overlay.
 *  - The circle center is anchored at the BOTTOM of the canvas so that only the top half
 *    (startAngle=180°, sweepAngle=180°) is visible — producing the dome/arch shape.
 *  - The fill arc animates from 0° to the target sweep on first composition.
 *  - A 14dp indicator dot rides at the tip of the fill arc.
 *
 * @param currentDayInCycle Day number within the current cycle (1-indexed).
 * @param totalCycleDays    Predicted full cycle length in days.
 * @param currentPhase      Active [CyclePhase] — drives ring colour and text labels.
 * @param phaseTheme        Active [PhaseThemeData] — provides [ringColor].
 * @param modifier          Outer modifier — should include [fillMaxWidth].
 */
@Composable
fun NiyuvaCycleRingArc(
    currentDayInCycle: Int,
    totalCycleDays: Int,
    currentPhase: CyclePhase,
    phaseTheme: PhaseThemeData,
    confidenceLevel: com.niyuva.app.domain.model.ConfidenceLevel = com.niyuva.app.domain.model.ConfidenceLevel.ESTIMATED,
    irregularityFlag: Boolean = false,
    modifier: Modifier = Modifier
) {
    val safeTotal    = totalCycleDays.coerceAtLeast(1)
    val targetSweep  = (currentDayInCycle.toFloat() / safeTotal.toFloat()) * 180f

    // Animate from 0 → target on composition; re-animates on value changes
    val animatedSweep by animateFloatAsState(
        targetValue   = targetSweep.coerceIn(0f, 180f),
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label         = "arcSweep"
    )

    val ringColor     = phaseTheme.ringColor
    val strokeWidthDp = 8.dp
    val dotRadiusDp   = 7.dp

    val labelText = when (confidenceLevel) {
        com.niyuva.app.domain.model.ConfidenceLevel.ESTIMATED -> androidx.compose.ui.res.stringResource(com.niyuva.app.R.string.confidence_estimated)
        com.niyuva.app.domain.model.ConfidenceLevel.LIKELY -> androidx.compose.ui.res.stringResource(com.niyuva.app.R.string.confidence_likely)
        com.niyuva.app.domain.model.ConfidenceLevel.CONFIDENT -> androidx.compose.ui.res.stringResource(com.niyuva.app.R.string.confidence_confident)
    }

    Box(
        modifier         = modifier
            .height(180.dp)
            .semantics {
                this.contentDescription = "Cycle day $currentDayInCycle of $totalCycleDays. Current phase: ${currentPhase.name}"
            },
        contentAlignment = Alignment.Center
    ) {

        // ── Canvas layer — arc drawing ────────────────────────────────────────
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokePx    = strokeWidthDp.toPx()
            val dotPx       = dotRadiusDp.toPx()

            // Circle center sits at the BOTTOM of the canvas.
            // This makes the top semicircle (startAngle=180°) the visible portion.
            val centerX = size.width / 2f
            val centerY = size.height

            // Radius: half the available width, shrunk by stroke half-width and dot radius
            // so neither the stroke edge nor the dot ever clip outside the bounds.
            val radius = (size.width / 2f) - strokePx / 2f - dotPx

            val arcLeft   = centerX - radius
            val arcTop    = centerY - radius
            val arcSize   = Size(radius * 2f, radius * 2f)

            // 1. Background track — full 180° semicircle in BlushMist
            drawArc(
                color      = BlushMist,
                startAngle = 180f,
                sweepAngle = 180f,
                useCenter  = false,
                topLeft    = Offset(arcLeft, arcTop),
                size       = arcSize,
                style      = Stroke(width = strokePx, cap = StrokeCap.Round)
            )

            // 2. Phase fill arc — 0° → animatedSweep (clockwise from left)
            if (animatedSweep > 0f) {
                drawArc(
                    color      = ringColor,
                    startAngle = 180f,
                    sweepAngle = animatedSweep,
                    useCenter  = false,
                    topLeft    = Offset(arcLeft, arcTop),
                    size       = arcSize,
                    style      = Stroke(width = strokePx, cap = StrokeCap.Round)
                )

                // 3. Indicator dot at the tip of the fill arc.
                //    In Android Canvas angles: 0° = right (3 o'clock), 90° = down.
                //    Start = 180° (left/9 o'clock). After sweeping, tip angle = 180 + sweep.
                val tipAngleRad = ((180.0 + animatedSweep) * PI / 180.0)
                val dotX = centerX + radius * cos(tipAngleRad).toFloat()
                val dotY = centerY + radius * sin(tipAngleRad).toFloat()

                drawCircle(
                    color  = ringColor,
                    radius = dotPx,
                    center = Offset(dotX, dotY)
                )
            }

            // 4. Predicted period segment at the end of the track
            val segmentDays = if (irregularityFlag) 3 else 1
            val sweep = segmentDays * (180f / safeTotal)
            val start = 360f - sweep
            val pathEffect = if (confidenceLevel == com.niyuva.app.domain.model.ConfidenceLevel.ESTIMATED) {
                androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
            } else {
                null
            }
            val alpha = if (confidenceLevel == com.niyuva.app.domain.model.ConfidenceLevel.ESTIMATED) 0.6f else 1.0f

            drawArc(
                color      = PhaseColorMenstruation,
                startAngle = start,
                sweepAngle = sweep,
                useCenter  = false,
                topLeft    = Offset(arcLeft, arcTop),
                size       = arcSize,
                style      = Stroke(width = strokePx, cap = StrokeCap.Round, pathEffect = pathEffect),
                alpha      = alpha
            )

            // Draw label & optional sparkle near the segment midpoint
            val midAngle = start + sweep / 2f
            val midAngleRad = (midAngle * PI / 180.0)
            val labelRadius = radius + 20.dp.toPx()
            val labelX = centerX + labelRadius * cos(midAngleRad).toFloat()
            val labelY = centerY + labelRadius * sin(midAngleRad).toFloat()

            val labelPaint = android.graphics.Paint().apply {
                color = PhaseColorMenstruation.toArgb()
                textSize = 9.dp.toPx()
                textAlign = android.graphics.Paint.Align.CENTER
                typeface = android.graphics.Typeface.create("Nunito", android.graphics.Typeface.BOLD)
            }

            drawIntoCanvas { canvas ->
                canvas.nativeCanvas.drawText(labelText, labelX, labelY, labelPaint)
            }

            if (confidenceLevel == com.niyuva.app.domain.model.ConfidenceLevel.CONFIDENT) {
                val sparkleX = centerX + radius * cos(midAngleRad).toFloat()
                val sparkleY = centerY + radius * sin(midAngleRad).toFloat()
                val sparklePaint = android.graphics.Paint().apply {
                    color = PhaseColorMenstruation.toArgb()
                    textSize = 12.dp.toPx()
                    textAlign = android.graphics.Paint.Align.CENTER
                }
                drawIntoCanvas { canvas ->
                    canvas.nativeCanvas.drawText("✦", sparkleX, sparkleY + 4.dp.toPx(), sparklePaint)
                }
            }
        }

        // ── Text overlay — day / phase / emoji ───────────────────────────────
        // Box contentAlignment = Center places this at (width/2, 90dp) which is well
        // inside the arc interior (arc spans from ~40dp to 180dp on a typical phone).
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Day number — large headline
            Text(
                text       = currentDayInCycle.toString(),
                fontFamily = NunitoFamily,
                fontWeight = FontWeight.ExtraBold,
                fontSize   = 36.sp,
                color      = ringColor,
                lineHeight  = 40.sp
            )
            // "Day" label
            Text(
                text       = "Day",
                fontFamily = NunitoFamily,
                fontWeight = FontWeight.Normal,
                fontSize   = 11.sp,
                color      = DustyMauve
            )
            Spacer(modifier = Modifier.height(2.dp))
            // Phase name
            Text(
                text       = currentPhase.displayName(),
                fontFamily = NunitoFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize   = 12.sp,
                color      = DustyMauve
            )
            Spacer(modifier = Modifier.height(2.dp))
            // Mood emoji
            Text(
                text     = currentPhase.moodEmoji(),
                fontSize = 22.sp
            )
        }
    }
}

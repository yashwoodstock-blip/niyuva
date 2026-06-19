package com.niyuva.app.presentation.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.niyuva.app.presentation.theme.DeepWarmBrown
import com.niyuva.app.presentation.theme.NunitoFamily

/** Compact data holder for log section icon grid items. */
data class LogIconData(
    val key: String,
    val emoji: String,
    val label: String
)

/**
 * A circular 72dp icon tile used inside log section grids.
 *
 * Visual states:
 *  - **Unselected**: [categoryColor] at 30% alpha background, no border.
 *  - **Selected**: [categoryColor] at 50% alpha, 2.5dp solid border, green ✓ badge at bottom-right.
 *
 * Animation: `animateFloatAsState` spring-pops the scale from 0 → 1 when [isSelected] flips to true.
 *
 * @param emoji         Emoji rendered as Text (not Image).
 * @param label         Short label below the circle — max 2 lines.
 * @param categoryColor Colour token for the log section (e.g. CategoryFlow, CategoryPain).
 * @param isSelected    Whether this tile is currently active.
 * @param onTap         Callback on tap — caller decides single vs. multi-select toggle.
 * @param modifier      Outer modifier.
 */
@Composable
fun LogSymptomIcon(
    emoji: String,
    label: String,
    categoryColor: Color,
    isSelected: Boolean,
    onTap: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Badge scale — springs from 0 → 1 when selected, shrinks immediately when deselected
    val badgeScale by animateFloatAsState(
        targetValue   = if (isSelected) 1f else 0f,
        animationSpec = spring(
            dampingRatio = 0.6f,
            stiffness    = Spring.StiffnessMedium
        ),
        label = "iconBadgeScale"
    )

    Column(
        modifier            = modifier
            .width(80.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication        = null,
                onClick           = onTap
            )
            .semantics {
                this.contentDescription = "$label log"
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // ── Circle icon ───────────────────────────────────────────────────────
        Box(
            modifier         = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(
                    if (isSelected) categoryColor.copy(alpha = 0.50f)
                    else categoryColor.copy(alpha = 0.30f)
                )
                .then(
                    if (isSelected) Modifier.border(2.5.dp, categoryColor, CircleShape)
                    else Modifier
                ),
            contentAlignment = Alignment.Center
        ) {
            // Emoji — always rendered as Text per spec
            Text(
                text     = emoji,
                fontSize = 32.sp
            )

            // Green checkmark badge — bottom-right, spring-animated scale
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(20.dp)
                        .scale(badgeScale)
                        .clip(CircleShape)
                        .background(Color(0xFF4CAF50)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text     = "✓",
                        fontSize = 10.sp,
                        color    = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // ── Label ─────────────────────────────────────────────────────────────
        Text(
            text       = label,
            fontFamily = NunitoFamily,
            fontWeight = FontWeight.Normal,
            fontSize   = 12.sp,
            color      = DeepWarmBrown,
            textAlign  = TextAlign.Center,
            maxLines   = 2,
            overflow   = TextOverflow.Ellipsis
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Section container — horizontal scrolling row of icons
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Renders a labelled horizontal scrollable row of [LogSymptomIcon] tiles.
 *
 * @param title        Section header — Nunito Bold 16sp, DeepWarmBrown.
 * @param items        The icon data list.
 * @param selectedKey  Currently selected item key (null = nothing selected).
 * @param categoryColor Colour token for all icons in this section.
 * @param onItemSelected Callback with the tapped key; caller handles toggle/clear logic.
 */
@Composable
fun LogSection(
    title: String,
    items: List<LogIconData>,
    selectedKey: String?,
    categoryColor: Color,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text       = title,
            fontFamily = NunitoFamily,
            fontWeight = FontWeight.Bold,
            fontSize   = 16.sp,
            color      = DeepWarmBrown
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
        ) {
            items(items, key = { it.key }) { item ->
                LogSymptomIcon(
                    emoji         = item.emoji,
                    label         = item.label,
                    categoryColor = categoryColor,
                    isSelected    = item.key == selectedKey,
                    onTap         = { onItemSelected(item.key) }
                )
            }
        }
    }
}

package com.niyuva.app.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.niyuva.app.presentation.theme.BlushMist
import com.niyuva.app.presentation.theme.DeepPlumRose
import com.niyuva.app.presentation.theme.DeepWarmBrown
import com.niyuva.app.presentation.theme.NunitoFamily

private val ChipShape = RoundedCornerShape(24.dp)

/**
 * A toggleable chip for multi-select or single-select chip groups.
 *
 * Unselected: transparent fill + 1.5dp [BlushMist] border + [DeepWarmBrown] text.
 * Selected:   [DeepPlumRose] fill + no border + White text.
 *
 * @param text       Display label.
 * @param isSelected Whether this chip is currently active.
 * @param onToggle   Callback fired on tap — caller decides single vs. multi-select behaviour.
 * @param modifier   Outer modifier.
 */
@Composable
fun NiyuvaChip(
    text: String,
    isSelected: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
    selectedColor: Color = DeepPlumRose
) {
    val chipModifier = if (isSelected) {
        modifier
            .clip(ChipShape)
            .background(selectedColor)
    } else {
        modifier
            .clip(ChipShape)
            .background(Color.Transparent)
            .border(1.5.dp, BlushMist, ChipShape)
    }

    Text(
        text       = text,
        fontFamily = NunitoFamily,
        fontWeight = FontWeight.Normal,
        fontSize   = 13.sp,
        color      = if (isSelected) Color.White else DeepWarmBrown,
        modifier   = chipModifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication        = null,
                onClick           = onToggle
            )
            .padding(vertical = 10.dp, horizontal = 16.dp)
    )
}

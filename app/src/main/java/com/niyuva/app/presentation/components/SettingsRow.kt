package com.niyuva.app.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.niyuva.app.presentation.theme.BlushMist
import com.niyuva.app.presentation.theme.DeepPlumRose
import com.niyuva.app.presentation.theme.DeepWarmBrown
import com.niyuva.app.presentation.theme.DustyMauve
import com.niyuva.app.presentation.theme.NunitoFamily

@Composable
fun SettingsRow(
    icon: ImageVector,
    label: String,
    value: String? = null,
    subLabel: String? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val rowModifier = if (onClick != null) {
        modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    } else {
        modifier.fillMaxWidth()
    }

    Column(modifier = rowModifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(40.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = DeepPlumRose,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Label & Sub-label
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = label,
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = DeepWarmBrown
                )
                if (subLabel != null) {
                    Text(
                        text = subLabel,
                        fontFamily = NunitoFamily,
                        fontSize = 12.sp,
                        color = DustyMauve
                    )
                }
            }

            // Right-side value or trailing content
            if (value != null) {
                Text(
                    text = value,
                    fontFamily = NunitoFamily,
                    fontSize = 14.sp,
                    color = DustyMauve,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            } else if (trailingContent != null) {
                trailingContent()
            }

            // Chevron
            if (onClick != null) {
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Outlined.ChevronRight,
                    contentDescription = null,
                    tint = DustyMauve,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        HorizontalDivider(
            thickness = 1.dp,
            color = BlushMist,
            modifier = Modifier.padding(start = 56.dp)
        )
    }
}

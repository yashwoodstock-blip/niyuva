package com.niyuva.app.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.niyuva.app.presentation.theme.DeepWarmBrown
import com.niyuva.app.presentation.theme.NunitoFamily

@Composable
fun <T> DiscoverSection(
    title: String,
    seeAllLabel: String = "See all",
    onSeeAll: () -> Unit = {},
    items: List<T>,
    key: ((T) -> Any)? = null,
    itemContent: @Composable (T) -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontFamily = NunitoFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = DeepWarmBrown
            )
            NiyuvaTextLink(
                text = seeAllLabel,
                onClick = onSeeAll
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items, key = key) { item -> itemContent(item) }
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

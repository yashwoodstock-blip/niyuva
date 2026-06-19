package com.niyuva.app.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.niyuva.app.presentation.theme.DeepPlumRose
import com.niyuva.app.presentation.theme.DeepWarmBrown
import com.niyuva.app.presentation.theme.DustyMauve
import com.niyuva.app.presentation.theme.NunitoFamily
import com.niyuva.app.presentation.theme.WarmLinen

@Composable
fun NiyuvaSearchBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(false) }

    BasicTextField(
        value = query,
        onValueChange = onQueryChanged,
        textStyle = TextStyle(
            fontFamily = NunitoFamily,
            fontSize = 14.sp,
            color = DeepWarmBrown
        ),
        cursorBrush = SolidColor(DeepPlumRose),
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(WarmLinen)
            .border(
                width = if (isFocused) 1.5.dp else 0.dp,
                color = if (isFocused) DeepPlumRose else Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            )
            .onFocusChanged { isFocused = it.isFocused },
        singleLine = true,
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = "Search",
                    tint = DustyMauve,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (query.isEmpty()) {
                        Text(
                            text = "Health ke baare mein khojo...",
                            fontFamily = NunitoFamily,
                            fontSize = 14.sp,
                            color = DustyMauve
                        )
                    }
                    innerTextField()
                }
            }
        }
    )
}

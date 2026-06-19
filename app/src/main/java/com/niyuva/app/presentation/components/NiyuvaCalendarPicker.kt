package com.niyuva.app.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.niyuva.app.presentation.theme.*
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

@Composable
fun NiyuvaCalendarPicker(
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val today = remember { LocalDate.now() }
    val maxPastDate = remember { today.minusMonths(6) }

    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    var selectedStartDate by remember { mutableStateOf<LocalDate?>(null) }
    var selectedEndDate by remember { mutableStateOf<LocalDate?>(null) }

    val daysInMonth = currentMonth.lengthOfMonth()
    val firstDayOfWeek = currentMonth.atDay(1).dayOfWeek.value // 1 (Mon) to 7 (Sun)
    val daysBefore = firstDayOfWeek % 7 // Sunday is 0, Monday is 1, etc.

    val canGoBack = currentMonth.isAfter(YearMonth.from(maxPastDate))
    val canGoForward = currentMonth.isBefore(YearMonth.now())

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(WarmIvory)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Month/Year navigation header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { if (canGoBack) currentMonth = currentMonth.minusMonths(1) },
                enabled = canGoBack,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Previous Month",
                    tint = if (canGoBack) Terracotta else BlushMist
                )
            }

            Text(
                text = "${currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${currentMonth.year}",
                fontFamily = NunitoFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = DeepWarmBrown,
                textAlign = TextAlign.Center
            )

            IconButton(
                onClick = { if (canGoForward) currentMonth = currentMonth.plusMonths(1) },
                enabled = canGoForward,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Next Month",
                    tint = if (canGoForward) Terracotta else BlushMist
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Day of week headers
        val daysOfWeek = listOf("S", "M", "T", "W", "T", "F", "S")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            daysOfWeek.forEach { day ->
                Text(
                    text = day,
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 11.sp,
                    color = DustyMauve,
                    modifier = Modifier.width(40.dp),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Days Grid
        val totalCells = daysBefore + daysInMonth
        val rows = (totalCells + 6) / 7

        Column {
            for (row in 0 until rows) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    for (col in 0 until 7) {
                        val cellIndex = row * 7 + col
                        val day = cellIndex - daysBefore + 1

                        if (day in 1..daysInMonth) {
                            val date = currentMonth.atDay(day)
                            val isFuture = date.isAfter(today)
                            val isPastLimit = date.isBefore(maxPastDate)
                            val isClickable = !isFuture && !isPastLimit
                            val isToday = date == today

                            val isSelectedStart = date == selectedStartDate
                            val isSelectedEnd = date == selectedEndDate
                            val isInRange = selectedStartDate != null && selectedEndDate != null &&
                                    date.isAfter(selectedStartDate) && date.isBefore(selectedEndDate)

                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .then(
                                        if (isInRange) {
                                            Modifier.background(Color(0xFFF5D0B5))
                                        } else if (isSelectedStart && selectedEndDate != null) {
                                            Modifier.background(Color(0xFFF5D0B5), RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp))
                                        } else if (isSelectedEnd && selectedStartDate != null) {
                                            Modifier.background(Color(0xFFF5D0B5), RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp))
                                        } else {
                                            Modifier
                                        }
                                    )
                                    .clip(CircleShape)
                                    .then(
                                        if (isSelectedStart || isSelectedEnd) {
                                            Modifier.background(Terracotta)
                                        } else if (isToday && !isSelectedStart && !isSelectedEnd) {
                                            Modifier.background(DeepPlumRose)
                                        } else {
                                            Modifier
                                        }
                                    )
                                    .clickable(enabled = isClickable) {
                                        if (selectedStartDate == null || (selectedStartDate != null && selectedEndDate != null)) {
                                            selectedStartDate = date
                                            selectedEndDate = null
                                            onDateSelected(date)
                                        } else {
                                            if (date.isBefore(selectedStartDate)) {
                                                selectedStartDate = date
                                                onDateSelected(date)
                                            } else {
                                                selectedEndDate = date
                                            }
                                        }
                                    }
                                    .alpha(if (isFuture) 0.5f else 1.0f),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = day.toString(),
                                    fontFamily = NunitoFamily,
                                    fontSize = 14.sp,
                                    fontWeight = if (isToday || isSelectedStart || isSelectedEnd) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelectedStart || isSelectedEnd || isToday) Color.White else DeepWarmBrown,
                                    textAlign = TextAlign.Center
                                )
                            }
                        } else {
                            Spacer(modifier = Modifier.size(40.dp))
                        }
                    }
                }
            }
        }
    }
}

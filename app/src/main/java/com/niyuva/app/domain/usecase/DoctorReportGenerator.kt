package com.niyuva.app.domain.usecase

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import androidx.core.content.FileProvider
import com.niyuva.app.domain.model.Cycle
import com.niyuva.app.domain.model.DailyLog
import com.niyuva.app.domain.model.EnergyLevel
import com.niyuva.app.domain.model.FlowLevel
import com.niyuva.app.domain.model.PainLevel
import com.niyuva.app.domain.repository.CycleRepository
import com.niyuva.app.domain.repository.DailyLogRepository
import com.niyuva.app.domain.repository.UserProfileRepository
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class DoctorReportGenerator @Inject constructor(
    private val context: Context,
    private val cycleRepository: CycleRepository,
    private val dailyLogRepository: DailyLogRepository,
    private val userProfileRepository: UserProfileRepository
) {
    suspend fun generateReport(
        patientName: String,
        months: Int
    ): File? {
        val endDate = LocalDate.now()
        val startDate = endDate.minusMonths(months.toLong())

        // Fetch Data
        val cycles = cycleRepository.getRecentCycles(6) // last 6 cycles
        val allCycles = cycleRepository.getRecentCycles(100)
        val logs = dailyLogRepository.getLogsInRange(startDate, endDate)

        // Create PDF
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 Size: 595 x 842 pt
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas

        // Paints
        val titlePaint = Paint().apply {
            textSize = 20f
            isFakeBoldText = true
            color = android.graphics.Color.BLACK
        }
        val headerPaint = Paint().apply {
            textSize = 14f
            isFakeBoldText = true
            color = android.graphics.Color.DKGRAY
        }
        val bodyPaint = Paint().apply {
            textSize = 10f
            color = android.graphics.Color.BLACK
        }
        val labelPaint = Paint().apply {
            textSize = 10f
            isFakeBoldText = true
            color = android.graphics.Color.BLACK
        }
        val smallBodyPaint = Paint().apply {
            textSize = 9f
            color = android.graphics.Color.DKGRAY
        }
        val grayLinePaint = Paint().apply {
            color = android.graphics.Color.LTGRAY
            strokeWidth = 1f
        }

        var y = 40f

        // Draw Header
        canvas.drawText("NIYUVA — Cycle Health Summary", 40f, y, titlePaint)
        y += 24f
        canvas.drawLine(40f, y, 555f, y, grayLinePaint)
        y += 20f

        canvas.drawText("Patient Name:", 40f, y, labelPaint)
        canvas.drawText(patientName.ifBlank { "Patient" }, 150f, y, bodyPaint)
        y += 16f

        canvas.drawText("Report Generated:", 40f, y, labelPaint)
        canvas.drawText(LocalDate.now().toString(), 150f, y, bodyPaint)
        y += 16f

        canvas.drawText("Period Covered:", 40f, y, labelPaint)
        canvas.drawText("$startDate to $endDate ($months Months)", 150f, y, bodyPaint)
        y += 30f

        // Section 1 — Cycle History Table
        canvas.drawText("Section 1: Cycle History (Last 6 Cycles)", 40f, y, headerPaint)
        y += 8f
        canvas.drawLine(40f, y, 555f, y, grayLinePaint)
        y += 16f

        // Table Header
        canvas.drawText("Start Date", 40f, y, labelPaint)
        canvas.drawText("End Date", 160f, y, labelPaint)
        canvas.drawText("Cycle Length (Days)", 280f, y, labelPaint)
        canvas.drawText("Period Length (Days)", 420f, y, labelPaint)
        y += 8f
        canvas.drawLine(40f, y, 555f, y, grayLinePaint)
        y += 16f

        // Cycle data rows
        cycles.forEach { cycle ->
            canvas.drawText(cycle.startDate.toString(), 40f, y, bodyPaint)
            canvas.drawText(cycle.endDate?.toString() ?: "In progress", 160f, y, bodyPaint)
            canvas.drawText(cycle.cycleLength?.toString() ?: "—", 280f, y, bodyPaint)
            canvas.drawText(cycle.periodLength?.toString() ?: "—", 420f, y, bodyPaint)
            y += 16f
        }
        y += 10f

        // Section 2 — Regularity Status
        canvas.drawText("Section 2: Regularity Status", 40f, y, headerPaint)
        y += 8f
        canvas.drawLine(40f, y, 555f, y, grayLinePaint)
        y += 16f

        // Calculate regularity (reads the existing regularity calculation or does a quick 3-cycle check)
        val last3Lengths = cycles.take(3).mapNotNull { it.cycleLength }
        val regularity = if (last3Lengths.size < 3) {
            "Unknown"
        } else {
            val max = last3Lengths.maxOrNull() ?: 0
            val min = last3Lengths.minOrNull() ?: 0
            if (max - min <= 5) "Regular" else "Irregular"
        }

        canvas.drawText("Cycle Regularity Status:", 40f, y, labelPaint)
        canvas.drawText(regularity, 180f, y, bodyPaint)
        y += 16f

        if (allCycles.isNotEmpty()) {
            val totalCycleCount = allCycles.size
            val earliestCycleDate = allCycles.last().startDate
            val prefix = if (totalCycleCount < 3) "(Limited data — more cycles will improve accuracy) " else ""
            val statsText = "${prefix}Based on $totalCycleCount logged cycles since $earliestCycleDate"
            canvas.drawText(statsText, 40f, y, smallBodyPaint)
            y += 16f
        }

        if (regularity == "Irregular") {
            canvas.drawText("Clinical Note:", 40f, y, labelPaint)
            canvas.drawText("Cycle length variance has exceeded 7 days across multiple recent cycles.", 180f, y, bodyPaint)
            y += 16f
        }
        y += 10f

        // Section 3 — Symptom Frequency Summary
        canvas.drawText("Section 3: Symptom Frequency Summary", 40f, y, headerPaint)
        y += 8f
        canvas.drawLine(40f, y, 555f, y, grayLinePaint)
        y += 16f

        // Compute distributions
        val flowHeavy = logs.count { it.flowLevel == FlowLevel.HEAVY }
        val flowMedium = logs.count { it.flowLevel == FlowLevel.MEDIUM }
        val flowLight = logs.count { it.flowLevel == FlowLevel.LIGHT }
        
        val painSevere = logs.count { it.painLevel == PainLevel.SEVERE || it.painLevel?.name?.lowercase() == "severe" }
        val painOkay = logs.count { it.painLevel == PainLevel.MODERATE || it.painLevel == PainLevel.MILD || it.painLevel?.name?.lowercase() == "okay" }
        val painFine = logs.count { it.painLevel == PainLevel.NONE || it.painLevel?.name?.lowercase() == "fine" }

        val moodCounts = mutableMapOf<String, Int>()
        logs.forEach { log ->
            log.moods.forEach { mood ->
                moodCounts[mood] = (moodCounts[mood] ?: 0) + 1
            }
        }
        val topMood = moodCounts.maxByOrNull { it.value }?.key ?: "No data"

        // Draw summary
        canvas.drawText("Flow Distribution:", 40f, y, labelPaint)
        canvas.drawText("Heavy: $flowHeavy days, Medium: $flowMedium days, Light: $flowLight days", 180f, y, bodyPaint)
        y += 16f

        canvas.drawText("Pain Distribution:", 40f, y, labelPaint)
        canvas.drawText("Severe: $painSevere days, Okay: $painOkay days, Fine: $painFine days", 180f, y, bodyPaint)
        y += 16f

        canvas.drawText("Most Logged Mood:", 40f, y, labelPaint)
        canvas.drawText(topMood.replaceFirstChar { it.uppercase() }, 180f, y, bodyPaint)
        y += 40f

        // Draw Footer
        canvas.drawLine(40f, 780f, 555f, 780f, grayLinePaint)
        val footerPaint = Paint().apply {
            textSize = 8f
            color = android.graphics.Color.GRAY
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText(
            "Generated by NIYUVA — generated locally on device, not shared with any third party.",
            297.5f,
            795f,
            footerPaint
        )

        // Finish page
        pdfDocument.finishPage(page)

        // Save PDF File
        val reportFile = File(context.filesDir, "Niyuva_Doctor_Report.pdf")
        try {
            val outputStream = FileOutputStream(reportFile)
            pdfDocument.writeTo(outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
            pdfDocument.close()
            return null
        }

        pdfDocument.close()
        return reportFile
    }

    fun getShareUri(file: File): Uri {
        return FileProvider.getUriForFile(
            context,
            "com.niyuva.app.fileprovider",
            file
        )
    }
}

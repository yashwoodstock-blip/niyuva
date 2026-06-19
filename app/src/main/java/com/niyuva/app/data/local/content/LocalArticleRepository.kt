package com.niyuva.app.data.local.content

import android.content.Context
import android.util.Log
import com.niyuva.app.domain.model.Article
import com.niyuva.app.domain.model.ContentBlock
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalArticleRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    // Fallback static article for Animations which has no local .md file
    private val animationsArticle = Article(
        topicId = "animations",
        title = "Animations",
        blocks = listOf(
            ContentBlock.SectionHeader("Visual Guides"),
            ContentBlock.BodyText("Animation videos se biology ko samajhna aur bhi simple ho jata hai."),
            ContentBlock.BulletList(listOf(
                "Uterus lining kaise banti aur shed hoti hai",
                "Egg release hone ka clear process",
                "Products use karne ka correct step-by-step guide"
            )),
            ContentBlock.QuoteBlock("Watch and learn simple biology concepts! 🎬")
        )
    )

    fun getArticle(topicId: String): Article? {
        if (topicId == "animations") {
            return animationsArticle
        }
        return try {
            val fileName = when (topicId) {
                "my_cycle" -> "my_cycle.md"
                "hormones" -> "hormones.md"
                "hygiene" -> "hygiene.md"
                "period_products" -> "period_products.md"
                "pcos", "pcos_health" -> "pcos.md"
                "health_diet" -> "health_diet.md"
                "during_periods" -> "during_periods.md"
                "my_body" -> "my_body.md"
                else -> null
            } ?: return null

            val inputStream = context.assets.open(fileName)
            val reader = BufferedReader(InputStreamReader(inputStream))
            val content = reader.use { it.readText() }
            parseMarkdown(content, topicId)
        } catch (e: Exception) {
            Log.e("LocalArticleRepository", "Error loading article $topicId", e)
            null
        }
    }

    private fun parseMarkdown(content: String, topicId: String): Article {
        val lines = content.lines()
        val blocks = mutableListOf<ContentBlock>()
        var title = "Article"
        
        var inList = false
        val currentListItems = mutableListOf<String>()
        
        var inTable = false
        val tableHeaders = mutableListOf<String>()
        val tableRows = mutableListOf<List<String>>()
        
        var i = 0
        while (i < lines.size) {
            val rawLine = lines[i]
            var line = rawLine.trim()
            
            // Handle list close
            if (inList && !line.startsWith("-")) {
                blocks.add(ContentBlock.BulletList(currentListItems.toList()))
                currentListItems.clear()
                inList = false
            }
            
            // Handle table close
            if (inTable && !line.startsWith("|")) {
                // Check if it's a Myth/Fact table
                val isMythTable = tableHeaders.any { it.contains("Myth", ignoreCase = true) } &&
                        tableHeaders.any { it.contains("Sach", ignoreCase = true) || it.contains("Fact", ignoreCase = true) }
                
                if (isMythTable && tableHeaders.size >= 2) {
                    val mythIndex = tableHeaders.indexOfFirst { it.contains("Myth", ignoreCase = true) }
                    val factIndex = tableHeaders.indexOfFirst { it.contains("Sach", ignoreCase = true) || it.contains("Fact", ignoreCase = true) }
                    
                    tableRows.forEach { row ->
                        if (row.size > mythIndex && row.size > factIndex) {
                            val mythText = row[mythIndex].replace("^\"|\"$".toRegex(), "").trim()
                            val factText = row[factIndex].replace("^\"|\"$".toRegex(), "").trim()
                            blocks.add(ContentBlock.MythFact(mythText, factText))
                        }
                    }
                } else {
                    blocks.add(ContentBlock.ComparisonTable(tableHeaders.toList(), tableRows.toList()))
                }
                tableHeaders.clear()
                tableRows.clear()
                inTable = false
            }
            
            // Remove *** horizontal rule symbol from any line
            if (line.contains("***")) {
                line = line.replace("***", "").trim()
            }
            
            if (line.isEmpty()) {
                i++
                continue
            }
            
            // Check for developer guides / representation guide headings to stop parsing immediately
            val upper = line.uppercase()
            if (upper.startsWith("## ") || upper.startsWith("### ")) {
                if (upper.contains("APP REPRESENTATION") ||
                    upper.contains("TONE RULES") ||
                    upper.contains("VISUAL DESIGN") ||
                    upper.contains("TYPOGRAPHY") ||
                    upper.contains("MICRO-INTERACTIONS") ||
                    upper.contains("SCREEN LAYOUT") ||
                    upper.contains("SCREEN-BY-SCREEN")
                ) {
                    break
                }
            }
            
            // Horizontal Divider (---)
            if (line == "---") {
                blocks.add(ContentBlock.DividerBlock())
                i++
                continue
            }
            
            // Title (# Title)
            if (line.startsWith("# ")) {
                title = line.substring(2).trim()
                i++
                continue
            }
            
            // Headers (##, ###, ####)
            if (line.startsWith("## ") || line.startsWith("### ") || line.startsWith("#### ")) {
                val text = line.replace("^#+\\s+".toRegex(), "").trim()
                blocks.add(ContentBlock.SectionHeader(text))
                i++
                continue
            }
            
            // Blockquotes
            if (line.startsWith(">")) {
                val rawQuote = line.substring(1).trim()
                if (rawQuote.isNotEmpty()) {
                    if (rawQuote.contains("**Sach:**", ignoreCase = true) || rawQuote.contains("**Asli Sach:**", ignoreCase = true)) {
                        val clean = rawQuote.replace("💡", "").replace("✔️", "").trim()
                        blocks.add(ContentBlock.QuoteBlock(clean))
                    } else if (rawQuote.contains("**Note:**", ignoreCase = true)) {
                        val clean = rawQuote.replace("💡", "").replace("**Note:**", "").trim()
                        blocks.add(ContentBlock.InfoCallout("Note", clean))
                    } else {
                        val clean = rawQuote.replace("^\\*|\\*$".toRegex(), "").trim()
                        blocks.add(ContentBlock.QuoteBlock(clean))
                    }
                }
                i++
                continue
            }
            
            // List Item (- item)
            if (line.startsWith("- ")) {
                inList = true
                currentListItems.add(line.substring(2).trim())
                i++
                continue
            }
            
            // Table (| Col 1 | Col 2 |)
            if (line.startsWith("|")) {
                val parts = line.split("|").map { it.trim() }.filter { it.isNotEmpty() }
                if (parts.all { it.startsWith("-") }) {
                    i++
                    continue
                }
                if (!inTable) {
                    inTable = true
                    tableHeaders.addAll(parts)
                } else {
                    tableRows.add(parts)
                }
                i++
                continue
            }
            
            // Default: Body Text
            blocks.add(ContentBlock.BodyText(line))
            i++
        }
        
        // Clean up remaining blocks if file ends
        if (inList) {
            blocks.add(ContentBlock.BulletList(currentListItems.toList()))
        }
        if (inTable) {
            val isMythTable = tableHeaders.any { it.contains("Myth", ignoreCase = true) } &&
                    tableHeaders.any { it.contains("Sach", ignoreCase = true) || it.contains("Fact", ignoreCase = true) }
            
            if (isMythTable && tableHeaders.size >= 2) {
                val mythIndex = tableHeaders.indexOfFirst { it.contains("Myth", ignoreCase = true) }
                val factIndex = tableHeaders.indexOfFirst { it.contains("Sach", ignoreCase = true) || it.contains("Fact", ignoreCase = true) }
                
                tableRows.forEach { row ->
                    if (row.size > mythIndex && row.size > factIndex) {
                        val mythText = row[mythIndex].replace("^\"|\"$".toRegex(), "").trim()
                        val factText = row[factIndex].replace("^\"|\"$".toRegex(), "").trim()
                        blocks.add(ContentBlock.MythFact(mythText, factText))
                    }
                }
            } else {
                blocks.add(ContentBlock.ComparisonTable(tableHeaders.toList(), tableRows.toList()))
            }
        }
        
        return Article(topicId, title, blocks)
    }
}

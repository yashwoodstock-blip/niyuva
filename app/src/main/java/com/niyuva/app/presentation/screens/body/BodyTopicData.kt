package com.niyuva.app.presentation.screens.body

import androidx.compose.ui.graphics.Color
import com.niyuva.app.presentation.theme.TopicAnimations
import com.niyuva.app.presentation.theme.TopicDuringPeriods
import com.niyuva.app.presentation.theme.TopicHormones
import com.niyuva.app.presentation.theme.TopicHygiene
import com.niyuva.app.presentation.theme.TopicMyBody
import com.niyuva.app.presentation.theme.TopicMyCycle
import com.niyuva.app.presentation.theme.TopicPCOS
import com.niyuva.app.presentation.theme.TopicPeriodProducts

data class BodyTopic(
    val id: String,
    val title: String,
    val description: String,
    val emoji: String,
    val cardColor: Color,
    val categoryLabel: String
)

object BodyTopics {
    val all = listOf(
        BodyTopic("my_cycle",         "My Cycle",        "Apne cycle ko samjho",        "🌙", TopicMyCycle,          "Basics"),
        BodyTopic("hormones",         "Hormones",        "Science — but relatable",     "⚗️", TopicHormones,         "Science"),
        BodyTopic("hygiene",          "Hygiene",         "Myths busted, facts shared",  "🚿", TopicHygiene,          "Care"),
        BodyTopic("period_products",  "Period Products", "Sabse detailed guide",        "📦", TopicPeriodProducts,   "Products"),
        BodyTopic("pcos",             "PCOS",            "Awareness — not fear",        "💛", TopicPCOS,             "Health"),
        BodyTopic("health_diet",      "Health",          "Apni body ko fuel do",        "🥗", TopicMyBody,           "Health"),
        BodyTopic("during_periods",   "During Periods",  "Kya khayen, kaise chalein",  "🌸", TopicDuringPeriods,    "Guide"),
        BodyTopic("my_body",          "My Body",         "Sab kuch — warmly explained", "🌺", TopicMyBody,           "Body"),
        BodyTopic("animations",       "Animations",      "Watch & understand",          "🎬", TopicAnimations,       "Watch")
    )
}

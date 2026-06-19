package com.niyuva.app.domain.usecase

import com.niyuva.app.presentation.screens.home.DailyTip
import com.niyuva.app.presentation.theme.CyclePhase
import javax.inject.Inject

/**
 * Returns the [DailyTip] for the current cycle phase and day.
 *
 * Tips are hardcoded in Hinglish. Each phase contains 5 tips; the selection rotates daily
 * using `(dayInCycle - 1) % tipCount` so the tip changes each day without repetition within
 * the same phase length.
 */
class GetDailyTipUseCase @Inject constructor() {

    operator fun invoke(phase: CyclePhase, dayInCycle: Int): DailyTip {
        val tips = tipMap[phase] ?: return DailyTip.default()
        val index = ((dayInCycle - 1).coerceAtLeast(0)) % tips.size
        return tips[index]
    }

    // ─────────────────────────────────────────────
    // Tip catalogue (Hinglish)
    // ─────────────────────────────────────────────

    private val tipMap: Map<CyclePhase, List<DailyTip>> = mapOf(

        CyclePhase.MENSTRUATION to listOf(
            DailyTip(
                tipText = "Aaj rest karna zaroor hai — tera body kaam kar rahi hai andar se. Iron wali cheezein kha 🌸",
                phase   = CyclePhase.MENSTRUATION,
                icon    = "🌸",
                category = "Rest 🌙"
            ),
            DailyTip(
                tipText = "Pani khub pee aaj — hydration cramps mein madad karta hai 💧",
                phase   = CyclePhase.MENSTRUATION,
                icon    = "💧",
                category = "Hydration 💧"
            ),
            DailyTip(
                tipText = "Heating pad lower abdomen pe try kar — genuinely kaam karta hai 🌡️",
                phase   = CyclePhase.MENSTRUATION,
                icon    = "🌡️",
                category = "Self-care 🌡️"
            ),
            DailyTip(
                tipText = "Aaj light walk try kar — prostaglandins reduce hote hain movement se 🚶\u200D♀️",
                phase   = CyclePhase.MENSTRUATION,
                icon    = "🚶\u200D♀️",
                category = "Exercise 🚶‍♀️"
            ),
            DailyTip(
                tipText = "Spinach, dal, jaggery — iron wali cheezein aaj ke liye perfect hain 🍃",
                phase   = CyclePhase.MENSTRUATION,
                icon    = "🍃",
                category = "Nutrition 🥗"
            )
        ),

        CyclePhase.FOLLICULAR to listOf(
            DailyTip(
                tipText = "Energy build ho rahi hai — naya project start karne ka perfect time hai ✨",
                phase   = CyclePhase.FOLLICULAR,
                icon    = "✨",
                category = "Activity ⚡"
            ),
            DailyTip(
                tipText = "Aaj cardio ya strength training try kar — body strong feel kar rahi hai! 💪",
                phase   = CyclePhase.FOLLICULAR,
                icon    = "💪",
                category = "Exercise 💪"
            ),
            DailyTip(
                tipText = "Fresh fermented foods teri gut health support karte hain is phase mein 🥗",
                phase   = CyclePhase.FOLLICULAR,
                icon    = "🥗",
                category = "Nutrition 🥗"
            ),
            DailyTip(
                tipText = "Skin best dikhti hai is phase mein — minimal effort, maximum glow ✨",
                phase   = CyclePhase.FOLLICULAR,
                icon    = "✨",
                category = "Skin ✨"
            ),
            DailyTip(
                tipText = "Kuch naya seekhne ka sabse acha time hai yeh — energy aur curiosity dono peak pe 🌱",
                phase   = CyclePhase.FOLLICULAR,
                icon    = "🌱",
                category = "Growth 🌱"
            )
        ),

        CyclePhase.OVULATION to listOf(
            DailyTip(
                tipText = "Aaj tu apne best pe hai — energy, confidence, sab peak pe 🔥 Kuch bhi bada karna ho toh aaj kar!",
                phase   = CyclePhase.OVULATION,
                icon    = "🔥",
                category = "Mindset ⚡"
            ),
            DailyTip(
                tipText = "High-intensity workout? Aaj perfect time hai — body apne peak physical performance pe hai 💃",
                phase   = CyclePhase.OVULATION,
                icon    = "💃",
                category = "Exercise 🏃‍♀️"
            ),
            DailyTip(
                tipText = "Stretchy discharge dikh raha hai? Bilkul normal — ovulation ka sign hai 🥚",
                phase   = CyclePhase.OVULATION,
                icon    = "🥚",
                category = "Hygiene 🥚"
            ),
            DailyTip(
                tipText = "Badi baat karni ho, presentation deni ho — aaj ka din perfect hai ✨",
                phase   = CyclePhase.OVULATION,
                icon    = "✨",
                category = "Career 💼"
            ),
            DailyTip(
                tipText = "Anti-inflammatory foods kha aaj — leafy greens, zinc-rich cheezein best hain 🥬",
                phase   = CyclePhase.OVULATION,
                icon    = "🥬",
                category = "Nutrition 🥗"
            )
        ),

        CyclePhase.LUTEAL to listOf(
            DailyTip(
                tipText = "Luteal phase hai — thoda irritable ya tired feel ho toh normal hai. Magnesium wali cheezein kha 💛",
                phase   = CyclePhase.LUTEAL,
                icon    = "💛",
                category = "Self-care 💛"
            ),
            DailyTip(
                tipText = "Dark chocolate, nuts, seeds — magnesium rich cheezein PMS mein madad karti hain 🍫",
                phase   = CyclePhase.LUTEAL,
                icon    = "🍫",
                category = "Nutrition 🥗"
            ),
            DailyTip(
                tipText = "Extra sleep aur warm bath teri best self-care hai aaj 🛁",
                phase   = CyclePhase.LUTEAL,
                icon    = "🛁",
                category = "Rest 🛁"
            ),
            DailyTip(
                tipText = "Salt aur sugar thoda kam karo — bloating aur mood swings reduce hote hain 💛",
                phase   = CyclePhase.LUTEAL,
                icon    = "💛",
                category = "Nutrition 🥗"
            ),
            DailyTip(
                tipText = "Hormones winding down hain — journaling ya light yoga try kar aaj 🌙",
                phase   = CyclePhase.LUTEAL,
                icon    = "🌙",
                category = "Mindset 🧘‍♀️"
            )
        )
    )
}

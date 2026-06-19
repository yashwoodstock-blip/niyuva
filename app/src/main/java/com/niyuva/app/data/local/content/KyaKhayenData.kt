package com.niyuva.app.data.local.content

import com.niyuva.app.presentation.theme.CyclePhase

data class FoodItem(
    val emoji: String,
    val name: String,
    val reason: String
)

data class KyaKhayenPhaseData(
    val phase: CyclePhase,
    val phaseName: String,
    val headline: String,
    val subtext: String,
    val accentColorHex: String,
    val backgroundColorHex: String,
    val foods: List<FoodItem>,
    val avoidNote: String,
    val bonusTip: String
)

object KyaKhayenData {

    val byPhase: Map<CyclePhase, KyaKhayenPhaseData> = mapOf(

        CyclePhase.MENSTRUATION to KyaKhayenPhaseData(
            phase = CyclePhase.MENSTRUATION,
            phaseName = "Period Phase",
            headline = "Kha woh jo tujhe andar se bharey 🌹",
            subtext = "Blood loss ke saath iron aur energy bhi jaati hai. Yeh foods teri body ko wapas strong banate hain.",
            accentColorHex = "#C0445A",
            backgroundColorHex = "#FFF0F3",
            foods = listOf(
                FoodItem("🌿", "Palak aur Methi", "Iron ki kaami poori karta hai — cramps bhi kum hote hain"),
                FoodItem("🫘", "Rajma aur Dal", "Plant-based iron + protein — body ko repair karta hai"),
                FoodItem("🍫", "Dark Chocolate (70%+)", "Magnesium se mood lift hota hai, cramps relax hote hain"),
                FoodItem("🌰", "Kaju, Badam, Til", "Healthy fats aur zinc — energy aur mood dono ke liye"),
                FoodItem("🍋", "Nimbu paani ya amla", "Vitamin C iron absorb karne mein madad karta hai"),
                FoodItem("🫙", "Gud (Jaggery)", "Iron ka desi source — chai ya halwe mein daal lo"),
                FoodItem("🐟", "Salmon ya Rawa Macchi", "Omega-3 se inflammation kum, periods less painful"),
                FoodItem("🫖", "Adrak-tulsi chai", "Cramps aur nausea mein natural relief deti hai")
            ),
            avoidNote = "❌ Thanda paani, maida, processed food aur zyada caffeine avoid karo — inflammation aur cramps badhate hain.",
            bonusTip = "💡 Didi ki salah: Warm water ke saath ajwain aur kala namak peena — cramps mein instant aaraam deta hai."
        ),

        CyclePhase.FOLLICULAR to KyaKhayenPhaseData(
            phase = CyclePhase.FOLLICULAR,
            phaseName = "Follicular Phase",
            headline = "Fresh start, fresh energy — tu phool rahi hai 🌸",
            subtext = "Period ke baad body rebuild hoti hai. Estrogen badhta hai — teri energy aur mood dono peak pe hote hain!",
            accentColorHex = "#3A7D5E",
            backgroundColorHex = "#F0FAF5",
            foods = listOf(
                FoodItem("🥚", "Ande (Eggs)", "High protein — naye cells banane mein madad karta hai"),
                FoodItem("🌾", "Daliya aur Oats", "Slow-releasing carbs — poore din energy stable rehti hai"),
                FoodItem("🥦", "Broccoli aur Gobhi", "Estrogen metabolism ke liye zaroori cruciferous veggies"),
                FoodItem("🍓", "Strawberry, Amrud, Kiwi", "Antioxidants se skin glow karta hai, immunity boost hoti hai"),
                FoodItem("🫒", "Olive oil ya Ghee", "Healthy fats hormones ke production mein help karte hain"),
                FoodItem("🐔", "Chicken ya Paneer", "Lean protein — muscles aur energy dono ke liye"),
                FoodItem("🌱", "Sprouted moong", "Zinc aur folate — egg quality ke liye bhi accha hai"),
                FoodItem("🍵", "Green Tea", "Light detox + gentle energy boost without jitteriness")
            ),
            avoidNote = "❌ Zyada meetha, packaged juice aur refined carbs se door raho — blood sugar spike karte hain.",
            bonusTip = "💡 Didi ki salah: Subah ek glass warm lemon water peena — liver detox mein madad karta hai aur digestion acchi hoti hai."
        ),

        CyclePhase.OVULATION to KyaKhayenPhaseData(
            phase = CyclePhase.OVULATION,
            phaseName = "Ovulation Phase",
            headline = "Tu apni peak pe hai — shine kar! ✨",
            subtext = "LH surge hota hai, energy aur confidence highest hoti hai. Anti-inflammatory foods is time body ko protect karte hain.",
            accentColorHex = "#B5860D",
            backgroundColorHex = "#FFFBF0",
            foods = listOf(
                FoodItem("🥬", "Saag aur Palak", "Folate aur iron — reproductive health ke liye zaroori"),
                FoodItem("🎃", "Kaddu ke beej (Pumpkin Seeds)", "Zinc ka best source — ovulation support karta hai"),
                FoodItem("🐠", "Tuna ya Sarson ki Macchi", "Omega-3 LH surge ke time inflammation control karta hai"),
                FoodItem("🍅", "Tamatar aur Shakarkand", "Lycopene antioxidant — uterine health ke liye excellent"),
                FoodItem("🧄", "Lahsun aur Pyaaz", "Natural anti-bacterial aur anti-inflammatory properties"),
                FoodItem("🍇", "Angoor aur Blueberries", "Resveratrol hormone balance mein help karta hai"),
                FoodItem("🥜", "Moongphali aur Akhrot", "Brain food — is phase mein focus aur clarity boost hoti hai"),
                FoodItem("🫙", "Greek Yogurt ya Dahi", "Probiotics gut health ke through hormones support karta hai")
            ),
            avoidNote = "❌ Alcohol, zyada spicy food aur trans fats avoid karo — yeh inflammation badhate hain aur hormones disturb karte hain.",
            bonusTip = "💡 Didi ki salah: Is phase mein khana banana, social milna aur naye kaam shuru karna — energy peak pe hoti hai!"
        ),

        CyclePhase.LUTEAL to KyaKhayenPhaseData(
            phase = CyclePhase.LUTEAL,
            phaseName = "Luteal Phase",
            headline = "PMS se pehle apna khayal rakh 🤍",
            subtext = "Progesterone peak hota hai, cravings aate hain aur mood swings ho sakte hain. Sahi khaana PMS ke symptoms drastically kam karta hai.",
            accentColorHex = "#6B4C8E",
            backgroundColorHex = "#F8F0FF",
            foods = listOf(
                FoodItem("🍫", "Dark Chocolate", "Magnesium se anxiety aur bloating dono kum hoti hai"),
                FoodItem("🥑", "Avocado ya Nariyal", "Healthy fats progesterone production support karte hain"),
                FoodItem("🌰", "Kaju, Pista, Walnut", "B6 vitamin mood ko stabilize karta hai — anxiety kum hoti hai"),
                FoodItem("🫛", "Lentils aur Chole", "Complex carbs serotonin badhate hain — mood lift hota hai"),
                FoodItem("🌾", "Brown Rice aur Jowar", "Slow carbs blood sugar stable rakhte hain — mood swings kum"),
                FoodItem("🥕", "Gajar, Beet, Kaddu", "Beta-carotene aur fiber — bloating aur digestion ke liye"),
                FoodItem("🫖", "Chamomile ya Ashwagandha chai", "Natural stress reliever — neend acchi hoti hai"),
                FoodItem("🐔", "Turkey ya Chicken", "Tryptophan se serotonin banta hai — feel-good hormone!")
            ),
            avoidNote = "❌ Salt, caffeine, alcohol aur refined sugar bilkul limit karo — yeh bloating, cramps aur mood swings seriously badhate hain.",
            bonusTip = "💡 Didi ki salah: Har 3-4 ghante mein thoda khaao — blood sugar stable rakhne se 70% PMS symptoms apne aap kum ho jaate hain."
        )
    )

    fun forPhase(phase: CyclePhase): KyaKhayenPhaseData =
        byPhase[phase] ?: byPhase[CyclePhase.MENSTRUATION]!!
}

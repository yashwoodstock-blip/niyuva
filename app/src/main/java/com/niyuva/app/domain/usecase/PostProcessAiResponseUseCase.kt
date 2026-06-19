package com.niyuva.app.domain.usecase

import javax.inject.Inject

class PostProcessAiResponseUseCase @Inject constructor() {
    operator fun invoke(rawResponse: String): String {
        var processed = rawResponse.trim()

        // 1. Off-domain safety filter
        val offDomainKeywords = listOf("recipe", "fashion", "boyfriend", "weight loss")
        if (offDomainKeywords.any { processed.contains(it, ignoreCase = true) }) {
            return "Yeh meri field nahi hai yaar 😊 Main toh sirf teri body aur menstrual health ke baare mein baat kar sakti hoon — kuch aur poochna ho health ke baare mein toh bata!"
        }

        // 2. Remove markdown bold/italic
        processed = processed.replace("*", "").replace("_", "")

        // 3. Trim to max 400 characters
        if (processed.length > 400) {
            processed = processed.substring(0, 397) + "..."
        }

        // 4. Ensure response ends with a warm emoji
        if (processed.isNotEmpty()) {
            val lastChar = processed.last()
            if (lastChar.isLetterOrDigit()) {
                processed = "$processed 💛"
            }
        }

        return processed
    }
}

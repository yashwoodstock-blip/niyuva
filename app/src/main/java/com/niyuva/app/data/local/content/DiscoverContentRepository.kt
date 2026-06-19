package com.niyuva.app.data.local.content

import com.niyuva.app.domain.model.StoryCard
import com.niyuva.app.domain.model.VedicCard
import com.niyuva.app.domain.model.AnimationCard
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DiscoverContentRepository @Inject constructor() {
    val stories = listOf(
        StoryCard("story_1", "Pehli baar period — kya feel hua?", "Real Stories", "Placeholder image", "Teen ladkiyon ki baatein...", isNew = true)
    )

    val vedicCards = listOf(
        VedicCard("vedic_1", "Shakti ki pehchan", "Stri shakti ke baare mein shloka...", 1),
        VedicCard("vedic_2", "Prakruti aur Shareer", "Body ke cycles ko samjhna...", 2),
        VedicCard("vedic_3", "Ritukaal — a sacred time", "Vedic perspective on menstruation...", 3)
    )

    val animations = listOf(
        AnimationCard("anim_1", "How ovulation works", 90, "Ovulation animation thumbnail"),
        AnimationCard("anim_2", "What happens during your period", 75, "Period animation thumbnail"),
        AnimationCard("anim_3", "Your hormones across the cycle", 85, "Hormones animation thumbnail")
    )
}

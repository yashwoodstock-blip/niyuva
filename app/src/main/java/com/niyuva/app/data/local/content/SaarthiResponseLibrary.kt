package com.niyuva.app.data.local.content

import com.niyuva.app.domain.model.SaarthiResponse
import com.niyuva.app.domain.model.LogExtraction
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SaarthiResponseLibrary @Inject constructor() {
    val responses: List<SaarthiResponse> = listOf(
        SaarthiResponse(
            id = "period_late",
            triggerKeywords = listOf("late", "period nahi aaya", "period miss", "period late", "delay"),
            response = "Behna, period late hona bahut common hai — stress, weight change, bimari ya travel sab affect kar sakte hain 💛 Agar 7 din se zyada late hai, ek pregnancy test le lo bas safety ke liye. Aur agar regularly late hoti hai, toh teri didi suggest karegi ki ek baar gynecologist se milna chahiye — woh sab clearly samjhayengi 🌸"
        ),
        SaarthiResponse(
            id = "pain_cramps",
            triggerKeywords = listOf("dard", "cramps", "pain", "tez dard", "bahut dard"),
            response = "Oof choti, cramps kitna takleef dete hain — samajhti hoon 💛 Heating pad lower abdomen pe lagao, genuinely kaam karta hai. Ibuprofen paracetamol se zyada effective hoti hai cramps mein kyunki yeh prostaglandins ko block karta hai. Aur garam paani peeti raho — teri body ko hydration chahiye abhi 🌸"
        ),
        SaarthiResponse(
            id = "pcos_awareness",
            triggerKeywords = listOf("pcos", "polycystic", "irregular", "irregular periods"),
            response = "Meri pyari bhen, PCOS bahut common hai — India mein 5 mein se 1 ladki ko hota hai aur kaafi ko pata bhi nahi hota 💛 Symptoms mein irregular periods, chin pe acne, aur extra facial hair hote hain. Ghabrana mat — lifestyle se bahut improve hota hai. Body tab mein 'PCOS & Health' section padh — teri didi ne sab detail mein samjhaya hai wahan 🌺"
        ),
        SaarthiResponse(
            id = "ovulation",
            triggerKeywords = listOf("ovulation", "ovulation kya", "egg", "fertile"),
            response = "Behna, ovulation tab hota hai jab teri ovary se ek egg release hoti hai — usually cycle ke middle mein, approximately Day 14 🥚 Is waqt stretchy, egg-white jaisa discharge hona bilkul normal hai, yeh ovulation ka clear sign hai. Is phase mein teri energy bhi high hoti hai aur mood bhi best hota hai — enjoy kar! 🌸"
        ),
        SaarthiResponse(
            id = "menstrual_cup",
            triggerKeywords = listOf("cup", "menstrual cup", "cup use", "cup safe"),
            response = "Behna, menstrual cup bilkul safe hai aur ek baar seekh lo toh life change ho jaati hai 😊 Virginity se koi lena dena nahi — yeh ek purana myth hai. Pehle 2-3 cycles thodi practice lagti hai, phir second nature ban jaati hai. Body tab mein detailed guide hai — teri didi ka recommendation hai try karo zaroor 🌸"
        ),
        SaarthiResponse(
            id = "discharge",
            triggerKeywords = listOf("discharge", "white discharge", "yellow discharge", "discharge kyu"),
            response = "Choti, discharge completely normal hai — yeh teri body ka khud ko saaf karne ka tarika hai 💛 Clear ya white hona normal hai. Stretchy egg-white jaisa = ovulation ka sign. Agar strong smell ho, ya green/grey color ho, toh teri didi bolugi ek doctor se zaroor milo — par aksar yeh bilkul normal hota hai 🌸"
        ),
        SaarthiResponse(
            id = "first_period",
            triggerKeywords = listOf("pehla period", "first period", "menarche", "starting period"),
            response = "Arey, pehla period aana ek bada milestone hai meri pyari bhen! Ghabrao bilkul mat — yeh normal aur healthy growth ka part hai 🌸 Kisi trusted elder, mummy ya doctor se pads ke baare mein poocho. Aur yaad rakho — teri didi hamesha yahan hai baat karne ke liye 💛"
        ),
        SaarthiResponse(
            id = "cycle_length",
            triggerKeywords = listOf("cycle length", "cycle kitne din", "period cycle"),
            response = "Behna, normal period cycle 21 se 35 din ki hoti hai, aur bleeding 2 se 7 din tak chal sakti hai 💛 Har kisi ki body alag hoti hai — comparison mat karo apne aap ko. Agar teri cycle is range se bahut alag hai regularly, toh ek baar doctor se baat karna useful hoga 🌸"
        ),
        SaarthiResponse(
            id = "tampons",
            triggerKeywords = listOf("tampon", "tampons", "tampon kaisa"),
            response = "Behna, tampons use karna bilkul safe hai — bas sahi size lo aur har 4-6 ghante mein zaroor change karo 💛 TSS (Toxic Shock Syndrome) rare hai par cleanliness se completely avoid hota hai. Raat ko sone se pehle pad use karna safer rehta hai 🌸"
        ),
        SaarthiResponse(
            id = "hygiene",
            triggerKeywords = listOf("hygiene", "saaf safai", "pad change", "cleanliness"),
            response = "Period hygiene ka dhyan rakhna bahut zaroori hai behna 💛 Pad har 4-6 ghante mein badlo, menstrual cup 8-12 ghante mein saaf karo. Comfortable cotton innerwear pehno — teri skin ko breathe karna chahiye. Teri didi ka rule: jitni saafi, utna comfort 🌸"
        ),
        SaarthiResponse(
            id = "flow_heavy",
            triggerKeywords = listOf("flow zyada", "bahut flow", "heavy bleeding"),
            response = "Heavy flow thakaan deta hai behna — samajhti hoon 💛 Iron-rich foods khao abhi: palak, rajma, gud — anemia se bachna hai. Agar har cycle mein bahut zyada bleeding ho rahi hai, toh teri didi strongly suggest karegi ek baar gynecologist se milna — kai baar koi treatable reason hota hai 🌸",
            logExtracted = LogExtraction("flow_level", "heavy")
        ),
        SaarthiResponse(
            id = "flow_light",
            triggerKeywords = listOf("flow kam", "light flow", "kam bleeding", "spotting"),
            response = "Light flow ya spotting period ke shuruat ya end mein normal ho sakta hai behna 💛 Par agar regularly bahut kam flow ho raha hai, toh hormones check karana useful hoga. Teri didi bolugi — body ke signals sunna seekho, woh sab bataati hai 🌺",
            logExtracted = LogExtraction("flow_level", "light")
        ),
        SaarthiResponse(
            id = "energy_low",
            triggerKeywords = listOf("thaka", "thaki", "energy nahi", "tired", "fatigue", "weakness"),
            response = "Arre choti, thakaan feel hona periods ke time bilkul normal hai 💛 Body ko extra rest do aur paani khoob peeo. Ek light stretch ya 10 minute ki walk bhi energy surprisingly boost kar deti hai. Aur ek kela ya mutthi bhar kaju khao — instant energy milti hai behna ⚡",
            logExtracted = LogExtraction("energy_level", "low")
        ),
        SaarthiResponse(
            id = "energy_high",
            triggerKeywords = listOf("bahut energy", "active hoon", "energetic", "high energy"),
            response = "Wah behna, high energy feel karna follicular ya ovulation phase ka classic sign hai ⚡ Yeh tera peak time hai — naye kaam shuru karo, gym jao, logon se milo. Teri didi bolugi is energy ka poora fayda uthaao 🌸",
            logExtracted = LogExtraction("energy_level", "high")
        ),
        SaarthiResponse(
            id = "mood_low",
            triggerKeywords = listOf("mood nahi", "irritated", "chidchidi", "sad", "anxious", "stressed"),
            response = "Behna, mood swings periods se pehle PMS ki wajah se hote hain — tu akeli nahi hai 💛 Thodi deep breathing karo, ya koi achhi chai banao aur ek moment apne liye nikalo. Magnesium-rich foods jaise dark chocolate aur kaju bhi mood boost karte hain. Teri didi hamesha yahan hai baat karne ke liye 🤍",
            logExtracted = LogExtraction("mood", "stressed")
        ),
        SaarthiResponse(
            id = "mood_happy",
            triggerKeywords = listOf("khush", "happy", "excited", "achha mood"),
            response = "Yay behna! Khush rehna ovulation phase ke peak estrogen se match karta hai 🌸 Enjoy kar yeh positive state ko — is time tera confidence aur creativity dono peak pe hote hain. Teri didi ki taraf se — tu bahut achhi kar rahi hai 💛",
            logExtracted = LogExtraction("mood", "happy")
        ),
        SaarthiResponse(
            id = "off_domain",
            triggerKeywords = listOf("recipe", "boyfriend", "fashion", "diet", "weight", "skin", "hair", "makeup"),
            response = "Haha behna, yeh toh meri specialty nahi hai 😊 Main sirf teri body aur menstrual health ki expert hoon. Kuch aur poochho — period pain, hormones, discharge, cycle — teri didi sab jawab degi 💛"
        ),
        SaarthiResponse(
            id = "doctor_recommendation",
            triggerKeywords = listOf("bahut zyada dard", "severe pain", "hospital", "doctor", "worried", "dar lag raha"),
            response = "Behna, yeh sunke mujhe thoda chinta ho raha hai 💛 Teri didi strongly bol rahi hai — please ek gynecologist se zaroor milo. Koi bhi health concern ko serious lena chahiye aur doctor best guide kar sakti hain. Main hoon yahan baat karne ke liye, par ek professional se milna abhi important hai. Apna khayal rakho 🌸"
        )
    )
}

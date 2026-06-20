package com.niyuva.app.data.local.content

import com.niyuva.app.domain.model.DidiResponse
import com.niyuva.app.domain.model.LogExtraction
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DidiResponseLibrary @Inject constructor() {
    val responses: List<DidiResponse> = listOf(
        DidiResponse(
            id = "period_late",
            triggerKeywords = listOf("late", "period nahi aaya", "period miss", "period late", "delay"),
            response = "Period late hona bahut common hai — stress, weight change, bimari ya travel sab affect kar sakte hain 💛 Agar 7 din se zyada late hai, ek pregnancy test le lo bas safety ke liye. Regularly late ho toh ek baar gynecologist se milna chahiye taaki sab clear ho jaye 🌸"
        ),
        DidiResponse(
            id = "pain_cramps",
            triggerKeywords = listOf("dard", "cramps", "pain", "tez dard", "bahut dard"),
            response = "Cramps kitna takleef dete hain, I understand 💛 Lower abdomen pe heating pad lagane se aaram milega. Ibuprofen paracetamol se zyada effective hoti hai cramps mein kyunki yeh prostaglandins ko block karta hai. Aur garam paani peeti raho taaki body well-hydrated rahe 🌸"
        ),
        DidiResponse(
            id = "pcos_awareness",
            triggerKeywords = listOf("pcos", "polycystic", "irregular", "irregular periods"),
            response = "PCOS bahut common hai — India mein 5 mein se 1 ladki ko hota hai aur kaafi ko pata bhi nahi hota 💛 Symptoms mein irregular periods, chin pe acne, aur extra facial hair hote hain. Ghabrana mat — lifestyle changes aur regular physical activity se yeh control ho jata hai. App ke 'PCOS & Health' section mein iski saari details hain, ek baar zaroor padhna 🌺"
        ),
        DidiResponse(
            id = "ovulation",
            triggerKeywords = listOf("ovulation", "ovulation kya", "egg", "fertile"),
            response = "Ovulation tab hota hai jab ovary se ek egg release hota hai — usually cycle ke middle mein, approximately Day 14 🥚 Is waqt stretchy, egg-white jaisa clear discharge hona bilkul normal hai. Is phase mein body energy aur mood naturally high rehte hain 🌸"
        ),
        DidiResponse(
            id = "menstrual_cup",
            triggerKeywords = listOf("cup", "menstrual cup", "cup use", "cup safe"),
            response = "Menstrual cup use karna bilkul safe hai aur ek baar seekh lo toh life bahut convenient ho jaati hai 😊 Virginity se iska koi lena-dena nahi hai, yeh sirf ek myth hai. Shuru mein 2-3 cycles practice lag sakti hai, par yeh kaafi eco-friendly aur clean option hai. App ke guide section mein iska details hai, try kar sakti ho 🌸"
        ),
        DidiResponse(
            id = "discharge",
            triggerKeywords = listOf("discharge", "white discharge", "yellow discharge", "discharge kyu"),
            response = "Discharge completely normal hai — yeh body ka khud ko clean aur protected rakhne ka tarika hai 💛 Clear ya cloudy white discharge normal hai. Agar discharge ka color green/grey ho, itchy ho ya strong smell ho, toh doctor se check-up karana chahiye, par clean discharge bilkul healthy hai 🌸"
        ),
        DidiResponse(
            id = "first_period",
            triggerKeywords = listOf("pehla period", "first period", "menarche", "starting period"),
            response = "Pehla period aana ek bada milestone hai. Ghabrao bilkul mat — yeh normal aur healthy growth ka important part hai 🌸 Kisi trusted family member, mother ya doctor se pads/hygiene ke baare mein baat karo. Aur koi bhi doubt ho toh yahan bejijhak pooch sakti ho 💛"
        ),
        DidiResponse(
            id = "cycle_length",
            triggerKeywords = listOf("cycle length", "cycle kitne din", "period cycle"),
            response = "Normal period cycle 21 se 35 din ki hoti hai, aur bleeding 2 se 7 din tak chal sakti hai 💛 Har kisi ki body alag hoti hai, isliye cycle vary kar sakti hai. Agar cycle length is range se regularly bahut bahar hai, toh gynecologist se consult karna sahi rahega 🌸"
        ),
        DidiResponse(
            id = "tampons",
            triggerKeywords = listOf("tampon", "tampons", "tampon kaisa"),
            response = "Tampons use karna bilkul safe hai — bas sahi size lo aur har 4-6 ghante mein badalte raho 💛 Cleanliness rakhne se infections aur Toxic Shock Syndrome (TSS) ka risk nahi rehta. Raat ko sone ke time pad use karna zyada comfortable ho sakta hai 🌸"
        ),
        DidiResponse(
            id = "hygiene",
            triggerKeywords = listOf("hygiene", "saaf safai", "pad change", "cleanliness"),
            response = "Period hygiene ka dhyan rakhna bahut zaroori hai 💛 Pad har 4-6 ghante mein badle, aur comfortable cotton innerwear pehne taaki skin breathe kar sake. Cleanliness se irritation aur infections dono se bacha ja sakta hai 🌸"
        ),
        DidiResponse(
            id = "flow_heavy",
            triggerKeywords = listOf("flow zyada", "bahut flow", "heavy bleeding"),
            response = "Heavy flow mein weakness ya fatigue hona normal hai 💛 Apni diet mein iron-rich foods jaise palak, rajma, aur gud shamil karein. Agar har cycle mein bleeding bahut zyada ho, toh safe side rehne ke liye gynecologist se consult karein 🌸",
            logExtracted = LogExtraction("flow_level", "heavy")
        ),
        DidiResponse(
            id = "flow_light",
            triggerKeywords = listOf("flow kam", "light flow", "kam bleeding", "spotting"),
            response = "Light flow ya spotting periods ke shuruat ya end mein bilkul normal hai 💛 Par agar har baar flow bahut hi kam hota hai, toh hormone levels check karana sahi rahega. Apne body ke signals ko track karte rahein 🌺",
            logExtracted = LogExtraction("flow_level", "light")
        ),
        DidiResponse(
            id = "energy_low",
            triggerKeywords = listOf("thaka", "thaki", "energy nahi", "tired", "fatigue", "weakness"),
            response = "Periods ke time body ka fatigue ya energy low hona normal hai 💛 Abhi body ko achha rest dein aur hydration ka dhyan rakhein. Ek chhoti walk ya banana/dry fruits khane se energy boost ho sakti hai ⚡",
            logExtracted = LogExtraction("energy_level", "low")
        ),
        DidiResponse(
            id = "energy_high",
            triggerKeywords = listOf("bahut energy", "active hoon", "energetic", "high energy"),
            response = "High energy feel karna follicular ya ovulation phase ka classic sign hai ⚡ Is phase mein focus aur stamina naturally high rehte hain, isliye naye tasks ya exercises handle karne ke liye yeh perfect time hai 🌸",
            logExtracted = LogExtraction("energy_level", "high")
        ),
        DidiResponse(
            id = "mood_low",
            triggerKeywords = listOf("mood nahi", "irritated", "chidchidi", "sad", "anxious", "stressed"),
            response = "Mood swings ya irritability periods se pehle PMS ki wajah se ho sakte hain 💛 Thodi deep breathing karein ya cold/warm beverage enjoy karein. Dark chocolate aur kaju jaise magnesium-rich foods bhi mood behtar karne mein help karte hain. Yahan bejijhak share kar sakti ho 🤍",
            logExtracted = LogExtraction("mood", "stressed")
        ),
        DidiResponse(
            id = "mood_happy",
            triggerKeywords = listOf("khush", "happy", "excited", "achha mood"),
            response = "Khush aur positive feel karna ovulation phase ke high estrogen levels se linked hai 🌸 Is phase mein energy, confidence aur creativity naturally acchi rehti hai. It's a great time to be active! 💛",
            logExtracted = LogExtraction("mood", "happy")
        ),
        DidiResponse(
            id = "off_domain",
            triggerKeywords = listOf("recipe", "boyfriend", "fashion", "diet", "weight", "skin", "hair", "makeup"),
            response = "Yeh query mere medical support domain se bahar hai. Main aapke period, symptoms, lifestyle, cycle, aur general health queries mein help kar sakti hoon. In topics se related kuch bhi poochiye 💛"
        ),
        DidiResponse(
            id = "doctor_recommendation",
            triggerKeywords = listOf("bahut zyada dard", "severe pain", "hospital", "doctor", "worried", "dar lag raha"),
            response = "Agar cramps ya discomfort severe hai, toh please use ignore na karein 💛 Safe aur healthy rehne ke liye ek gynecologist se direct consult karna sabse sahi rahega. Apna dhyan rakhein aur jaldi check-up karayein 🌸"
        )
    )
}

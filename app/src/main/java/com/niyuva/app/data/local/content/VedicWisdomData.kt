package com.niyuva.app.data.local.content

data class VedicSectionContent(
    val id: String,
    val title: String,
    val subtitle: String,
    val icon: String,
    val description: String,
    val paragraphs: List<String>,
    val quote: String?,
    val keyTakeaway: String,
    val backgroundColorHex: String,
    val textColorHex: String
)

object VedicWisdomData {
    val list = listOf(
        VedicSectionContent(
            id = "sec_1",
            title = "Shuru Karte Hain Ek Sawaal Se",
            subtitle = "Sadiyon Pehle Teri Period Sacred Thi — Sirf Tu Bhool Gayi Thi",
            icon = "❓",
            description = "Kya tumne socha hai ki jis period ko aaj 'ganda' kaha jata hai, wo 3000 saal pehle ek festival tha?",
            paragraphs = listOf(
                "Tune kabhi socha hai ki jis cheez ko aaj kuch log \"ganda\" kehte hain — woh cheez 3,000 saal pehle ek festival tha?",
                "Jis period ke liye aaj bhi kai ghar mein alag thali milti hai — usi period ki celebration mein poora ek state ka farming band ho jaata tha. Ek pura river laal ho jaata tha. Ek temple ke darwaze teen din ke liye band ho jaate the — isliye nahi ki period impure tha, balki isliye ki devi khud period mein thi aur use rest chahiye tha.",
                "Yeh koi feel-good quote nahi hai. Yeh koi manufactured feminist story nahi hai. Yeh actual history hai. Documented history. Chisi hui history.",
                "Hamari bhasha mein, hamare devi-devtaon mein, hamare granth mein, hamare doctors mein, hamare festivals mein — period ko hamesha ek sacred, powerful, aur celebrated cheez ki tarah describe kiya gaya hai. Sadiyon tak kisine poochha nahi tha — \"kya period acha hai?\" — kyunki yeh obvious tha. Jaise koi nahi poochha ki \"kya monsoon zaroori hai?\" Ya \"kya seed se ped banana chahiye?\" Period tha, toh fertility thi. Fertility thi, toh life thi. Life thi, toh sacred tha.",
                "Aur phir kuch hua. Slowly, over centuries, uss celebration ko darwaze ke bahar kar diya gaya. Uss reverence ko restriction mein badal diya gaya. Uss sacred time ko shame mein convert kar diya gaya. Aur yeh itna slowly hua ki hum dekh bhi na sake.",
                "Aaj hum usi original story ko wapas laate hain. Source se. Research se. Ancient texts se. Aur living traditions se — jo aaj bhi hain. Jo abhi bhi hain. Jo Assam mein aaj bhi mela banata hai. Jo Odisha mein aaj bhi khet band karta hai. Jo Kerala mein aaj bhi pehle period pe coconut oil lagaata hai aur bolta hai — devi aa gayi.",
                "Yeh teri history hai. Seedha teri."
            ),
            quote = "Period tha, toh fertility thi. Fertility thi, toh life thi. Life thi, toh sacred tha.",
            keyTakeaway = "Period ko badla gaya restrictions aur sharm mein, par ye hamesha se sacred tha 🌸",
            backgroundColorHex = "#F0D5DC",
            textColorHex = "#8B5E6D"
        ),
        VedicSectionContent(
            id = "sec_2",
            title = "Sanskrit Mein Period Ke Naam",
            subtitle = "Bhasha Ke Root Words Mein Chupi Original Ideology",
            icon = "🗣️",
            description = "Artava, Rajas, Rajaswala, Rituchakra... Har shabd ka deep scientific aur cosmic meaning.",
            paragraphs = listOf(
                "Sanskrit — hamare sabse purane texts ki bhasha — mein period ke liye sirf ek word nahi tha. Multiple words the. Aur har word ek alag dimension bataata tha. Naam mein intention hoti hai, naam mein perspective aur poori culture ki soch hoti hai.",
                "Artava — yeh sabse common medical term tha period ke liye. \"Artava\" ka root hai \"Ritu\" — season. \"Artava\" literally translates to \"that which comes from the seasons\" ya \"that which is connected to the rhythm of seasons.\" Hamare poorvaj samajhte the ki period aur seasons ek hi type ki phenomenon hain. Dono cyclical hain. Dono life ko sustain karte hain.",
                "Rajas ya Raja — yeh period blood ka naam tha. \"Rajas\" ka root \"raj\" hai — woh jo chamakta hai, woh jo active energy hai, woh jo movement aur dynamism ko represent karta hai. Sanskrit mein \"rajas\" ek guna bhi hai — active energy, the energy that creates and transforms. Period blood ka naam \"chamakti, transforming energy\" tha. Not waste. Not impurity.",
                "Rajaswala — yeh tha ek menstruating woman ke liye. \"Woh jo Rajas se yukta hai.\" \"Woh jo active divine energy se bhari hai.\" Soch ek second — teri poori language mein, period mein hone wali aurat ke liye word tha \"divinely energized woman.\"",
                "Rituchakra — period cycle ka naam. \"Ritu\" matlab season. \"Chakra\" matlab wheel ya cycle. The Wheel of Seasons. Teri body ek cosmic wheel of seasons chala rahi thi — yeh unka perspective tha. Jaise dharti ke seasons cycle karte hain, teri body bhi cycle karti hai.",
                "Rajahkala — period bleeding ka specific phase. \"Raja\" + \"kala\" = \"the time of royal blood.\" Interesting, right? Royalty associated with period.",
                "Ritumati — woh woman jo just period ke baad fertile phase mein hai. Acharya Sushruta ne \"Ritumati\" ka description diya: \"Jab period ke baad fertile phase aata hai, woman ka chehra alive aur radiant hota hai. Uska body moist aur nourished feel karta hai.\"",
                "Rajonivrutti — menopause ka naam. \"Rajas\" + \"nivrutti\" = \"The stopping of royal energy.\" Menopause bhi respected tha — yeh period ki \"completion\" thi, \"failure\" nahi.",
                "In sab naamon mein ek common thread hai. Koi bhi word \"unclean,\" \"impure,\" \"forbidden,\" ya \"shameful\" connotation carry nahi karta. Every single word is either medically neutral, or positively charged — seasons, royal blood, divine energy, cycles."
            ),
            quote = "In ancient Sanskrit, a menstruating woman was called 'Rajaswala' — one filled with active, glowing, creative energy.",
            keyTakeaway = "Sanskrit ke root words batate hain ki period hamesha se royal aur seasonal energy tha 👑",
            backgroundColorHex = "#F0E5C8",
            textColorHex = "#856404"
        ),
        VedicSectionContent(
            id = "sec_3",
            title = "Charaka Aur Sushruta Samhita",
            subtitle = "Jab Ancient Doctors Ne Period Ko Health Ka Sign Kaha",
            icon = "🩺",
            description = "Ayurveda ke legendary physicians ke views: healthy period blood (Shudha Artava) ki pehchan.",
            paragraphs = listOf(
                "2,000+ saal pehle, hamare doctors ne ek medical system banaya jo aaj ki modern gynecology se surprisingly similar hai — bina microscope ke, bina hormonal assays ke, sirf observation aur logic se.",
                "Acharya Charaka approximately 4th century BCE ke physician the. Unki \"Charaka Samhita\" Ayurveda ka foundational text hai. Aur iss text mein women's health — specifically period health — ka ek dedicated section tha. Kyunki Charaka jaante the ki women's health = community's health.",
                "Charaka ne explain kiya ki Artava — period blood — kaise banta hai. Yeh Rasa Dhatu ka ek \"Upadhatu\" hai. Rasa Dhatu humare digestion se banne wala highly refined nourishing fluid (plasma) hai. Iska matlab yeh tha: period blood body ki most nourishing, vital energy se directly connected hai. Yeh monthly renewing precious output thi.",
                "Charaka aur Sushruta ne \"Shudha Artava\" (normal, healthy period blood) describe kiya. Unke anusar, healthy period blood ka rang laal hona chahiye (na dark, na light), metallic ya slightly sweet smell honi chahiye (foul nahi), aur cycle approx 28-30 din ka hona chahiye bina severe pain ke.",
                "Sushruta — India ke greatest ancient surgeon — ne period ke ek phase ke baare mein ek description diya jo dynamic hai: \"Rajahkala woh samay hai jab vagina uss ovum ke liye rota hai jo fertilize nahi hua.\" 2000 saal purani line mein ovulation aur period ka correlation poetic aur real tarike se diya gaya hai.",
                "Ayurveda ne cycle durations aur bleeding rates mein flexibility ko acknowledge kiya. Vagbhatta ne 3 din, Harita ne 7 din normal mana. Rigid rules nahi the — they observed that every woman's body has its own natural rhythm."
            ),
            quote = "Sushruta wrote: 'Rajahkala is the time when the body sheds tears for the unfertilized ovum.'",
            keyTakeaway = "Ancient India mein period koi bimari ya gandagi nahi, balki wellness ka main parameter tha 🩺",
            backgroundColorHex = "#D5E8D2",
            textColorHex = "#155724"
        ),
        VedicSectionContent(
            id = "sec_4",
            title = "Rituchakra: Cycle Aur Cosmos",
            subtitle = "Rajahsravakala, Ritukala Aur Luteal Phase Ka Sync",
            icon = "🌙",
            description = "Menstrual cycle ke phases ka lunar cycles aur universe ke rhythm se synchronisation.",
            paragraphs = listOf(
                "Rituchakra — The Wheel of Seasons. Ayurveda ne menstrual cycle ko teen key phases mein divide kiya hai, aur inka biological matches modern science ke observations se perfectly match karta hai.",
                "Phase 1 — Rajahsravakala (The Bleeding Phase): Day 1 se Day 3-5. Ayurveda mein isse Vata Dosha govern karta hai. Vata movement aur elimination ko rules karta hai. Agar Vata balanced hai, toh flow bina dard ke smooth hota hai. Modern science mein, estrogen aur progesterone drop hote hain aur contractions se lining shed hoti hai.",
                "Phase 2 — Ritukala (The Fertile Phase): Day 4 se Day 16. Kapha Dosha is phase ko govern karta hai — building and nourishment. Modern science mein, estrogen badhta hai, follicles mature hote hain, aur uterine lining rebuild hoti hai. Sushruta ne likha ki is phase mein woman ka face radiant aur energized hota hai.",
                "Phase 3 — Rituvyatita Kala (The Luteal Phase): Day 17 se cycle ke end tak. Pitta Dosha is phase mein dominant hoti hai, jo transformation aur body heat ko govern karti hai. Modern science mein, progesterone peak pe hota hai jo body temperature aur nesting behaviour badhata hai. Imbalance hone par heat issues aur mood shifts (PMS) hote hain.",
                "Chandramasa Connection: average menstrual cycle (28 days) aur average lunar cycle (29.5 days) ka sync ancient observers ke liye coincidence nahi tha. In a world without artificial lights, women's cycles naturally synchronized with moon phases. You are literally connected to the cosmic rhythm."
            ),
            quote = "Your cycle is a wheel of biological seasons, mirroring the waxing and waning of the moon.",
            keyTakeaway = "Humare shareer ke hormones moon cycles aur natural seasons ke rhythm par chalte hain 🌙",
            backgroundColorHex = "#E8E0F0",
            textColorHex = "#6A5ACD"
        ),
        VedicSectionContent(
            id = "sec_5",
            title = "Shakti: Divine Feminine Energy",
            subtitle = "Sri Vidya, Devipuram Aur Tantric Traditions Mein Sacred Period",
            icon = "🧘‍♀️",
            description = "Tantric traditions aur Devipuram jahan menstruating woman ko living temple mana jata hai.",
            paragraphs = listOf(
                "Vedic aur Tantric wisdom mein period ko seedha divine creative power — Shakti — se connect kiya gaya hai.",
                "Sri Vidya Tradition (Goddess worship tradition) kehta hai ki menstrual cycle ke dauran ek woman Shakti energy se maximum connect hoti hai. Period ke 3-4 days mein woman ka energy field most open, receptive, aur spiritually sensitive hota hai. Unka observation tha ki is time dreams, intuition, aur emotional depth active ho jati hain.",
                "Tantric Shakta traditions mein period blood (Rajas/Rakta) ko life ka sacred fluid aur creative energy ka physical manifestation mana jata tha.",
                "Devipuram Temple (Andhra Pradesh) iska ek living example hai. Yahan menstruating women ko restricted nahi kiya jata, balki unhe living temple ki tarah aadar diya jata hai. Devipuram ke founder Sri Amritananda Natha Saraswati ne samjhaya: \"Menstruating woman ko touch na karne ka original root alag tha. Wo ek living goddess thi, uske energy field ko rest aur space dena respect tha, restrictions nahi.\"",
                "Bauls of Bengal (mystic folk singers) period ke time ko 'Mahayoga' (The Great Yoga) kehte hain. Unke songs mein period blood ko 'life-giving river' aur cosmic lifecycle ka heartbeat bataya gaya hai. Yeh folk beliefs centuries se sadiyo purane respect ko live rakh rahi hain."
            ),
            quote = "At Devipuram, Sri Amritananda said: 'A menstruating woman is a living temple. She does not need brick walls; she is the goddess itself.'",
            keyTakeaway = "Tantric aur folk traditions period ko creation aur cosmic energy ki power mante hain 🧘‍♀️",
            backgroundColorHex = "#D2EDE8",
            textColorHex = "#0E6251"
        ),
        VedicSectionContent(
            id = "sec_6",
            title = "Vedic Rishikayen Aur Unka Gyaan",
            subtitle = "Rigveda Ke Hymns Likhnewali Menstruating Women",
            icon = "✍️",
            description = "Lopamudra, Gargi aur Maitreyi ki stories jinhe original Vedic texts mein credit aur respect mila.",
            paragraphs = listOf(
                "Rigveda — humara sabse purana sacred text — usme 17 confirmed female rishis (Rishikas) ne philosophical aur spiritual hymns likhe hain.",
                "In female seers ke naam hain: Romasa, Lopamudra, Apala, Kadru, Vishvavara, Ghosha, Juhu, Vagambhrini, Paulomi, Jarita, Shraddha-Kamayani, Urvashi, Sharnga, Yami, Indrani, Savitri, aur Devayani. Sama Veda mein bhi Nodha, Shikatanivavari jaise rishikayein hain.",
                "In sab women ko period hota tha. Unhe cycles aate the. Par original Vedic tradition ne unhe 'seers' aur spiritual equal mana. Unke compose kiye gaye mantras aaj bhi traditional ceremonies mein hum bolte hain.",
                "Gargi Vachaknavi ki story Yajnavalkya ke sath upanishad debates ki hume batati hai ki unhone court mein khade hokar deep philosophical sawaal pooche. Maitreyi ne sage Yajnavalkya se physical wealth ki jagah Brahman-Atman ki wisdom mangi.",
                "Lopamudra ke Rigvedic mantras aaj bhi dynamic and respected hain. Vedic original tradition mein period hona, bleeding hona, ya womanhood kabhi bhi religious equality aur wisdom ke aage barrier nahi bani. They were respected for their minds and souls."
            ),
            quote = "Seventeen women composed the hymns of the Rigveda. Their periods were no barrier to receiving divine wisdom.",
            keyTakeaway = "Ancient India ki female seers ne periods ke sath-sath Vedas ka divinely gyaan likha ✍️",
            backgroundColorHex = "#DDF2FA",
            textColorHex = "#1F4E5B"
        ),
        VedicSectionContent(
            id = "sec_7",
            title = "Kamakhya Aur Ambubachi Mela",
            subtitle = "Jab Devi Ko Period Hota Hai Aur Pura Desh Celebrate Karta Hai",
            icon = "🕌",
            description = "Guwahati ka famous temple jahan yoni formation ko worship kiya jata hai aur devi ka period prasad banta hai.",
            paragraphs = listOf(
                "Kamakhya Temple, Guwahati (Assam) — Nilachal Hills par Brahmaputra river ke kinare sthit ek extremely powerful Shakti Peetha hai. Yahan koi human-made statue ya idol nahi hai. Worship hoti hai ek natural rock formation ki, jo 'yoni' (creative source) ki shape mein hai aur ek spring ke pani se wet rehti hai.",
                "Har saal June-July ke aaspas, yahan ek mela lagta hai — Ambubachi Mela. Traditional belief hai ki is samay Devi menstruate karti hain.",
                "Devi ke period ke 3 din temple ke doors close ho jate hain taaki devi rest kar sake. Par bahar hazaron sadhus, tantrics aur devotees meditation aur chanting karte hain. Isse 'Mahakumbh of the East' kaha jata hai.",
                "Chauthe din, jab temple khulta hai, devi ke period prasad ke roop mein 'Angabastra' (red cloth) aur 'Angodak' (spring water) distribute kiya jata hai. Log door-door se ye prasad lene aate hain.",
                "Brahmaputra ka laal hona: Ambubachi ke time monsoon rains iron-oxide rich red soil ke contact mein aati hain, jisse river red ho jati hai. Science mechanism batata hai, aur tradition meaning deti hai — earth aur creation cycle ka sync celebrate ho raha hai."
            ),
            quote = "During Ambubachi, the doors close so the goddess can rest, and the cloth that touched her bleed becomes the holiest prasad.",
            keyTakeaway = "Kamakhya temple hume sikhata hai ki Devi ka period hona pure aur prasad ke barabar hai 🕌",
            backgroundColorHex = "#F2B5C5",
            textColorHex = "#C2405A"
        ),
        VedicSectionContent(
            id = "sec_8",
            title = "Raja Parba: Odisha Ka Utsav",
            subtitle = "Bhudevi (Mother Earth) Ka Period Aur Farming Ka Teen-din Rest",
            icon = "🌾",
            description = "Odisha ka unique festival jahan Earth ki menstruation pe swings, alata aur poda pitha se celebration hoti hai.",
            paragraphs = listOf(
                "Odisha mein ek extremely unique festival hota hai — Raja Parba. Raja word 'Rajas' (menstruation) se aaya hai. Is festival mein Mother Earth (Bhudevi) ke menstrual cycle ko celebrate kiya jata hai.",
                "Agricultural Rest: Odisha ke log maante hain ki is time Mother Earth menstruate karti hain. Isliye kisan teen din tak farming activities band rakhte hain. Soil mein ploughing, sowing, digging bilkul nahi hoti. Earth ko complete rest diya jata hai.",
                "Feminine Celebration: Raja Parba mein girls aur women main highlight hoti hain. Unhe 3 days ke liye household chores se complete freedom milta hai. Naye kapde pehnna, aalta lagana, swings par jhulna aur poda pitha khana unka routine hota hai.",
                "Pahili Raja (Day 1) se Sajabaja aur Basumati Snana (Day 4) tak, Earth ki bleeding cycle ko respect karke end mein ceremonial wash diya jata hai, aur fir fresh farming cycles restart hote hain.",
                "Ek village woman ne explain kiya: \"Hum khet mein nahi jate jab Earth rest karti hai. Hum bhi earth ka hi ek part hain. Hamara cycle aur earth ka cycle ek hi hai.\""
            ),
            quote = "Raja Parba celebrates the menstruation of the Earth. A festival of rest, swings, and sisterhood.",
            keyTakeaway = "Odisha ka Raja festival batata hai ki female period is earth's energy cycle 🌾",
            backgroundColorHex = "#FFD6C0",
            textColorHex = "#E07A5F"
        ),
        VedicSectionContent(
            id = "sec_9",
            title = "Draupadi Ka Sacred Time",
            subtitle = "Mahabharata Mein Period Rest Aur Dignity Ka Violation",
            icon = "⚔️",
            description = "Draupadi ka Rajaswala state mein sabha mein khichna aur adharma ki shuruat.",
            paragraphs = listOf(
                "Mahabharata ke sabse intense scene — Cheer Haran — mein Draupadi ka period mein hona ek major point hai jo social taboos mein hum aksar bhool jate hain.",
                "Dice game harne ke baad Duryodhana ne order diya Draupadi ko court mein lane ka. Draupadi apne chamber mein thi. Wo Rajaswala thi (period bleeding phase) aur single cloth (ek kapde) mein rest par thi. Dushasana ne force se uske privacy ko violate kiya aur hair pakadkar sabha mein laaya.",
                "Uss time ki ancient Indian society mein, menstruating woman ka space sacred aur protected hota tha. Uske privacy ko violate karna aur forced drag karna sabse bada 'adharma' mana gaya. Bhishma, Drona aur poori sabha is violation par chup rahi jo unki moral failure thi.",
                "Mythologist Devdutt Pattanaik ke research ke according, early Sanskrit texts (jaise Bhasha ke plays in 100 CE) mein Draupadi ke Rajaswala state ko explicitly highlighted kiya gaya hai. Bad ki centuries mein updates ke time social sharm ke karan log is detail ko quiet kar gaye.",
                "Draupadi ne period state mein khade hokar court se dharma aur dignity ke sawaal pooche jo bade se bade panditon ko silence kar gaye. Uski bleeding state uski safety aur moral boundary thi, jise todkar Kauravas ne apna sarvanash bulaya."
            ),
            quote = "Draupadi was dragged into the court while she was Rajaswala — a violation of her sacred period rest that shook the cosmos.",
            keyTakeaway = "Mahabharata kehta hai ki period mein hone wali woman ki boundaries aur rest ko respect karna param dharma hai ⚔️",
            backgroundColorHex = "#B5D5C5",
            textColorHex = "#1E5E4A"
        ),
        VedicSectionContent(
            id = "sec_10",
            title = "Ritu Kala Samskara",
            subtitle = "Desh Ke Alag-Alag States Mein Pehle Period Ke Celebrations",
            icon = "🎉",
            description = "Manjal Neerattu Vizha, Tuloni Biya aur Thirandukalyanam jaise premium traditional festivals.",
            paragraphs = listOf(
                "Ancient aur rural India mein pehla period aane par sharam nahi, balki bade paimane par community celebration hoti thi. Alag-alag states mein iske unique naam hain:",
                "Tamil Nadu — Manjal Neerattu Vizha (Turmeric Bathing Ceremony): Pehli period aane par ladki ko special space mein bithakar haldi ka snaan diya jata hai. Married women aarti karti hain, aur maternal uncle (mama) silk saree gift karte hain. Pura mohalla treat dene aata hai.",
                "Karnataka — Ritu Shuddhi: Karnataka mein ladki ko Ritu Kala Samskara ke dauran tambula aur sweets (jaise til aur gud) offer kiye jate hain. Usse special treats aur health tips older women se milti hain.",
                "Kerala — Thirandukalyanam: Kerala ke traditional communities mein mana jata hai ki pehli period ke time ladki mein Devi (Bhagwati) ka avtaar hua hai. Usse custom coconut oil massages aur special food items diye jate hain.",
                "Assam — Tuloni Biya (Small Wedding): Assam mein pehle period ko mini-wedding ki tarah celebrate kiya jata hai. Traditional customs, music, aur feasts ke sath ladki ka womanhood zone mein swagat hota hai.",
                "In sab celebrations ka main common root yehi tha: young girl ko reassure karna ki period natural aur respected event hai, taaki wo sharm se akele bathroom mein na roye, balki pride aur community support mehsoos kare."
            ),
            quote = "In South and Northeast India, a girl's first period was celebrated like a wedding — welcoming a new creator into the tribe.",
            keyTakeaway = "Tuloni Biya aur Manjal Vizha batate hain ki pehla period celebration ka mauka hai, sharam ka nahi 🎉",
            backgroundColorHex = "#F0D5DC",
            textColorHex = "#8B5E6D"
        ),
        VedicSectionContent(
            id = "sec_11",
            title = "Dharti Maata Ka Period",
            subtitle = "Poori India Mein Living Earth Aur Agriculture Rest Ke Beliefs",
            icon = "🌍",
            description = "Punjab, Deccan aur Tulu community ke festivals jahan dharti ko cyclic rest diya jata hai.",
            paragraphs = listOf(
                "Mother Earth ka cyclic period belief sirf Odisha tak limited nahi tha. Poori Indian subcontinent mein log Earth ko ek live, feeling organism mante the, jiske cycles ko respect kiya jata tha.",
                "Punjab: Kheti ke traditional beliefs mein ek week aana tha jahan mana jata tha ki 'Dharti Maata soti hain' (her seasonal rest). Is period mein farmers land ko plow nahi karte the.",
                "Deccan & Malabar Coast: Monsoon aane ke time, zameen garmi ke baad dry ho jati hai. First monsoon shower ko 'Earth's period' mana jata hai jisse fertility restore hoti hai. Kuch temples is rest period mein gates band rakhte the devi ke aadar mein.",
                "Karnataka border: Tulu community 'Keddasa' festival manati hai. Ye 3 days ka earth festival hota hai jahan mitti ko dig karna completely prohibited hota hai, Earth's monthly rest cycle ko respect karne ke liye.",
                "Ye sab practices batati hain ki periods and cyclical rest agricultural sustainability ke liye important hain. Humare ancestors ne ecological wisdom ko period ki sacred language di taaki ye kabhi bhooli na jaye."
            ),
            quote = "When the Earth rests, the farmers rest. A deep harmony between the seasons of the land and the seasons of the body.",
            keyTakeaway = "Keddasa aur local traditions dikhate hain ki earth ke cycles aur humare period cycles aaps mein connected hain 🌍",
            backgroundColorHex = "#F0E5C8",
            textColorHex = "#856404"
        ),
        VedicSectionContent(
            id = "sec_12",
            title = "Lal Rang Ka Raaz",
            subtitle = "Feminine creative energy, Sindoor aur Kumkum ka ancient connection",
            icon = "🔴",
            description = "Historians ke findings ke anusar red color aur menstrual blood ka potential fertility se gehra rishta.",
            paragraphs = listOf(
                "Humare festivals, rituals aur pujas mein red color (lal rang) sabse dominant aur auspicious mana jata hai. Tikka, kumkum, sindoor, gulal, devi ki chunri, aur aarti ki thali — har jagah lal rang.",
                "Historian Narendra Nath Bhattacharyya ke research ke according, ancient civilization mein red color directly human aur earth fertility se associated tha. Red blood was the indicator of life, aur specifically period blood (Rajas) creative force ka symbol tha.",
                "Janet Chawla ne research article 'Rig Veda and Menstrual Origins' mein explain kiya ki kumkum aur red paste lagane ki custom ancient India ki yoni/fertility respect se heavily linked hai. Red color dynamic growth aur life-giving fluid ko represent karta hai.",
                "Sindoor marks: Shadi ke dauran head parting mein red sindoor lagana is a symbol of 'the one who holds creative power.' Menstrual potential ko auspicious mark ki tarah celebrate kiya jata tha.",
                "Goa ke Shigmo harvest festival mein red mitti aur red powders ek dusre par lagana earth ki seasonal fertility aur bleeding phase ko celebrate karna hi tha. Ye rituals batate hain ki humara red markup period blood ki ancient creative identity hai."
            ),
            quote = "Red is the color of life, growth, and fertility. Every red dot of kumkum traces back to the creative power of blood.",
            keyTakeaway = "Kumkum aur red sindoor feminine potential aur period blood ki energy ke symbols hain 🔴",
            backgroundColorHex = "#D5E8D2",
            textColorHex = "#155724"
        ),
        VedicSectionContent(
            id = "sec_13",
            title = "Impure Ka Idea: Ek Honest History",
            subtitle = "Vedic Original Guidelines Aur Smritis Ke Badlte Rules",
            icon = "📜",
            description = "Puranic aur colonial periods mein kaise protective boundaries ko rigid constraints mein badla gaya.",
            paragraphs = listOf(
                "Humare original Vedic texts mein period ke dauran jo guidelines thin, wo pure protection aur rest ke liye thin, na ki exclusion aur sharm ke liye.",
                "Angirasa Smriti aur Vashishta Dharmasutra kehte hain ki period ke time women ko strenuous physical labour (chulha jalana, heavy grinding, deep cleaning) nahi karna chahiye. Is time unhe pure, calm space aur rest dena family ki obligation thi.",
                "Par samay ke sath kya hua? Humare desh mein jab physical labour rules badle aur society patriarchal and rigid hone lagi, tab 'rest' ko 'exclusion' (kitchen se bahar, temple se door) bana diya gaya. Protective guidelines ko restrictions aur punishment mein convert kar diya gaya.",
                "Puranic periods aur colonial era (British rule) ke dauran, jab texts ko strictly interpret kiya gaya bina context ke, tab humari physiological safety boundaries sharm aur taboo mein badal gayin. Jo space health ke liye thi, wo socially and mentally painful ban gayi.",
                "Modern science aur humari purani wisdom dono ek hi baat kehti hain: period rest, hygiene aur wellness ka samay hai. Hume fir se restrictions ko door karke original respect aur boundaries ko health support mein badalna hoga."
            ),
            quote = "Original texts prescribed rest to protect women from heavy physical labour. Later, this rest was twisted into exclusion.",
            keyTakeaway = "History batati hai ki period rest ko humne social taboos mein badla, hume fir se azaadi aur support chunna hai 📜",
            backgroundColorHex = "#E8E0F0",
            textColorHex = "#6A5ACD"
        )
    )
}

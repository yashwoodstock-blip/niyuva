package com.niyuva.app.data.local.content

import com.niyuva.app.domain.model.PehliBaarStory

data class StoryTheme(
    val id: String,
    val title: String,
    val description: String,
    val icon: String,
    val backgroundColorHex: String,
    val textColorHex: String,
    val stories: List<PehliBaarStory>
)

object ThemeStoriesData {
    val list = listOf(
        StoryTheme(
            id = "theme_1",
            title = "Buying Pads for the First Time",
            description = "The medical shop. The black polythene bag. The whispered transaction.",
            icon = "🛍️",
            backgroundColorHex = "#F0D5DC",
            textColorHex = "#8B5E6D",
            stories = listOf(
                PehliBaarStory(
                    id = "t1_s1",
                    title = "The Chemist Uncle and the Newspaper Wrap",
                    subtitle = "Shared by Rashmi, 24, Jaipur | Source: Youth Ki Awaaz",
                    paragraphs = listOf(
                        "I was fourteen, and my mother had given me a hundred-rupee note folded very small, like a secret. She said, \"Jao, medical wale bhaiya se lena. Stayfree bolna. Bas.\" Just go, say Stayfree, and come back. She did not come with me. I think she was embarrassed, or maybe she thought that was how you taught a girl to be discreet — you sent her alone into the world with a secret and a folded note.",
                        "The medical shop near our house was run by a man I had seen my entire life. He had given me Vicks when I had a cold when I was seven. He had counted my change carefully when I bought school copies. But that afternoon, I could not look at him. I stood at the edge of the counter and I said, very quietly, \"Bhaiya, Stayfree.\" He did not blink. He reached behind him without even turning, pulled out a green packet, put it in a black polythene bag, then — without me asking — he folded a piece of newspaper around it and put it inside the black bag. Two layers of hiding. I paid him. He gave change. No eye contact was broken because no eye contact had been made.",
                        "I walked home holding the bag away from my body like it was something I might be caught with. I remember thinking: this is so strange. I have seen biscuits carried more openly than this.",
                        "Years later I read about how Arunachalam Muruganantham — the Pad Man of Coimbatore — used to carry sanitary napkins around in a bag and people thought he was mad because it was such a shameful thing for a man to hold. And I thought about chemist uncle and his newspaper wrap and I felt something shift in my chest.",
                        "Now I am twenty-four. I buy pads off Amazon or from D-Mart. I put them in my shopping basket next to chips and shampoo and I do not fold anything. But I still think sometimes about that newspaper wrap — and I feel both sad and grateful. Sad that it was ever like that. Grateful that for many girls in India, bit by bit, it is starting to not be."
                    ),
                    quote = "This is so strange. I have seen biscuits carried more openly than this.",
                    keyMessage = "Normalizing simple tasks is a silent revolution 🌸",
                    source = "Source: Youth Ki Awaaz",
                    backgroundColorHex = "#F0D5DC",
                    textColorHex = "#8B5E6D"
                ),
                PehliBaarStory(
                    id = "t1_s2",
                    title = "My Brother Bought Them for Me",
                    subtitle = "Shared by Divya Nair, 22, Kochi | Source: SheThePeople.TV",
                    paragraphs = listOf(
                        "My older brother Arun is three years older than me and he has always been my go-to person for embarrassing things. When I was sixteen and I unexpectedly got my period at home on a Sunday — the worst possible day because every shop nearby was closed except one — I texted him instead of my mother. My mother would have made it a whole thing. She would have said prayers and told the neighbours I was now a woman. Arun just texted back: \"Sent. Coming in 20.\"",
                        "He showed up with a packet of Whisper Ultra, a packet of Parle-G, and a small bottle of Sprite because he said he had read online that sugar helps with cramps. He was wrong about the sugar but right about everything else. He left them outside my room door, knocked twice, and went back to watching cricket.",
                        "I cried a little bit, not because of cramps but because of how completely normal he made it feel. He did not act like he had done something heroic. He did not make a face. He did not tease me later in front of anyone. He just went and bought pads the way he would buy bread.",
                        "Two years later when he went to college in Pune, he once called me from a medical shop while his hostel friend's girlfriend had unexpectedly needed pads and had asked him to buy them. \"Divya,\" he whispered on the phone, \"Ultra thin ya regular?\" I told him ultra thin. He bought them, gave them to his friend's girlfriend, and that was that.",
                        "I think about Arun and I think that the change India needs — this small, private, undramatic change where a boy walks into a chemist and buys pads without shame — is happening more than we know. It is happening in medical shops in Kochi and Pune and in WhatsApp messages between siblings and nobody is making a reel about it but it is happening, quietly and permanently."
                    ),
                    quote = "He just went and bought pads the way he would buy bread.",
                    keyMessage = "A boy buying pads without shame is a beautiful shift 🫂",
                    source = "Source: SheThePeople.TV Community",
                    backgroundColorHex = "#F0E5C8",
                    textColorHex = "#856404"
                ),
                PehliBaarStory(
                    id = "t1_s3",
                    title = "I Practiced What I Would Say",
                    subtitle = "Shared anonymously | Source: Reddit r/TwoXIndia",
                    paragraphs = listOf(
                        "I want to share something embarrassing but I think it needs to be said.",
                        "The first time I had to buy my own pads — I was seventeen, visiting my maasi's house in a part of Delhi I did not know well — I actually practiced saying the word in my head before I walked into the shop. I stood outside the chemist for maybe three full minutes just going over what I would say. \"Bhaiya, Stayfree dena, regular.\" That was it. Seven words. I rehearsed it like it was a stage performance.",
                        "The reason I needed to rehearse was not because I was shy. I am actually quite outgoing. The reason was that every single time I had watched women buy pads before — my mother, my maasi, my older cousins — they all whispered. Nobody had ever told me why. It just seemed like you were supposed to whisper. Like it was a classified military transaction.",
                        "So I walked in, and I whispered. The bhaiya at the counter couldn't hear me and said, \"Kya bola?\" I had to repeat it louder. He handed me the pack. Normal. Transactional. Nothing. The whole rehearsal for nothing.",
                        "I think about it now and laugh. But I also think about what it does to a girl's mind — years and years of watching people whisper about something so ordinary — and how you quietly absorb the shame before you even understand what you are ashamed of. You just absorb it from the air around you.",
                        "The fact that girls today talk about periods openly on Instagram, that there are period-positivity campaigns, that Menstrupedia exists and is teaching girls in school — I think those things matter more than they sound. Because you cannot whisper your way to dignity."
                    ),
                    quote = "Years of watching people whisper makes you quietly absorb the shame.",
                    keyMessage = "You cannot whisper your way to dignity 📢",
                    source = "Source: Reddit r/TwoXIndia",
                    backgroundColorHex = "#D5E8D2",
                    textColorHex = "#155724"
                ),
                PehliBaarStory(
                    id = "t1_s4",
                    title = "The Shop Had Everything in the Open",
                    subtitle = "Shared by Tarini Mehta, 25, Ahmedabad | Source: Feminism in India",
                    paragraphs = listOf(
                        "When I visited my friend in Bengaluru in 2019, she took me to a pharmacy near her office that had dedicated a full shelf to period products — right in the front, at eye level, clearly lit, with price tags and everything. Menstrual cups. Period underwear. Multiple brands of pads. OB tampons. Not wrapped, not hidden behind the counter, not whispered for. Just there, like toothpaste.",
                        "I stood in front of that shelf for probably ninety seconds, which, in a pharmacy, is a long time. My friend laughed at me. She said, \"Why do you look so surprised?\" And I had to think about it. I was surprised because in Ahmedabad, where I grew up, period products were still a behind-the-counter item. You had to ask. You had to specify. You had to receive it in the black bag. Here it was just… on a shelf.",
                        "I bought a menstrual cup that day — my first one — just because it was there and I could pick it up and read the box and make an informed choice without any transaction happening. No bhaiya to make eye contact with. No whispered brand name. Just me and the product and a decision.",
                        "That shelf changed my life more than it sounds like a shelf can. Sometimes access is the whole revolution."
                    ),
                    quote = "Not wrapped, not hidden behind the counter. Just there, like toothpaste.",
                    keyMessage = "Sometimes access is the whole revolution 🛍️",
                    source = "Source: Feminism in India Blog",
                    backgroundColorHex = "#E8E0F0",
                    textColorHex = "#6A5ACD"
                )
            )
        ),
        StoryTheme(
            id = "theme_2",
            title = "Switching to a Menstrual Cup",
            description = "The cup changed everything. Not immediately. Not easily. But permanently.",
            icon = "🌸",
            backgroundColorHex = "#D2EDE8",
            textColorHex = "#0E6251",
            stories = listOf(
                PehliBaarStory(
                    id = "t2_s1",
                    title = "I Watched Seventeen YouTube Videos Before I Tried It",
                    subtitle = "Shared by Aarohi Kulkarni, 26, Pune | Source: Menstrupedia Blog",
                    paragraphs = listOf(
                        "I discovered menstrual cups through a YouTube rabbit hole in 2018. I was twenty-one, I had been dealing with extremely heavy flow since I was fifteen, and I was spending approximately ₹200 every cycle on pads, sometimes more. I was also wasting an enormous amount of plastic and I had started to feel guilty about that because I was studying environmental science at the time. But the thing that finally pushed me was an article I read about a woman in Chennai who had PCOS and switched to a cup and said it helped her track her flow much more accurately.",
                        "I watched seventeen YouTube videos. I read all the reviews on Amazon India. I joined a Facebook group called Menstrual Cup Users India which at the time had maybe twelve thousand members. I made a spreadsheet comparing brands — Sirona, Pee Safe, Stonesoup, the imported ones. The women there were patient, funny, and genuinely helpful in a way I had not expected.",
                        "Then the cup arrived and I could not figure it out for two full cycles.",
                        "This is the part people do not tell you. It is not intuitive. The C-fold and the punch-down fold and the seven-fold — you watch them being done on a rubber demonstration cup in a video and it looks completely simple and then you are in your bathroom with the actual cup and your body and you feel like you are failing a science exam. The first time I tried it I got it in wrong. The second time I was not sure. The third time I thought I had it and then there was leakage.",
                        "By the end of the second cycle I got it. And then — this is the thing that is hard to explain to someone who hasn't used one — I forgot I had it. I went to a full day of college. I went to the gym. I forgot, completely, that I was on my period.",
                        "That is the thing the cup gave me: the forgetting. Not having to count whether I had enough pads in my bag. Not running to the bathroom every two hours."
                    ),
                    quote = "That is the thing the cup gave me: the forgetting.",
                    keyMessage = "The cup saves money, plastic, and period anxiety 🌸",
                    source = "Source: Menstrupedia Community Blog",
                    backgroundColorHex = "#D2EDE8",
                    textColorHex = "#0E6251"
                ),
                PehliBaarStory(
                    id = "t2_s2",
                    title = "My Mother Said It Was Against Our Culture",
                    subtitle = "Shared by Preethi Subramaniam, 28, Chennai | Source: The Swaddle",
                    paragraphs = listOf(
                        "When I told my mother I wanted to try a menstrual cup she looked at me with deep suspicion.",
                        "She said it was \"not for our kind of girls.\" I asked what that meant. She said it meant that in our family, in our community, young unmarried women do not put things inside their bodies.",
                        "My aunt who was visiting said nothing during this conversation but that evening she came to my room and said, quietly, that she had been using a menstrual cup for three years. She was forty-six and had used it since her daughter told her about it. She showed me the brand she used, told me the fold that worked for her, and made me promise not to tell my mother because my grandmother would hear about it and there would be a whole situation.",
                        "I bought my cup online. I learned to use it with help from that Facebook community. My mother never found out.",
                        "What strikes me about this story is the generational layering of it. My grandmother's generation had rags. My mother's generation had pads and the cultural restrictions around using anything else. My aunt's generation quietly began to find different ways while publicly maintaining the old framework. And my generation is having public conversations about bodily autonomy. All of us in the same family. All of us in completely different worlds.",
                        "I think change in India does not always announce itself loudly. Sometimes it is an aunt coming to your room in the evening and closing the door quietly and saying: here, try this one, this brand is good."
                    ),
                    quote = "Sometimes change is an aunt closing the door quietly and saying: try this.",
                    keyMessage = "Quiet sisterhood breaks generational taboos 🫂",
                    source = "Source: The Swaddle community submissions",
                    backgroundColorHex = "#F5ECD5",
                    textColorHex = "#6E2C00"
                ),
                PehliBaarStory(
                    id = "t2_s3",
                    title = "The Cup During a Trek in Himachal",
                    subtitle = "Shared by Nandita Verma, 29, Delhi | Source: Stonesoup Blog",
                    paragraphs = listOf(
                        "I went on a seven-day Himachal Pradesh trek in September 2020. Twelve trekkers, two guides. The route was Pin Bhaba Pass — about 55 km over seven days, staying in tents, with access to running water maybe twice a day.",
                        "My period was due on day three of the trek. I knew what a pad in a mountain tent for seven days would mean: the disposal problem, carrying used pads in my backpack because you cannot leave period waste on a mountain trail, and the sheer logistical nightmare of changing pads in a tent at 3800 metres when your hands are cold.",
                        "I had been using my menstrual cup for about a year. I emptied it into a cat-hole, rinsed it with a small amount of bottled water, used a tissue, and reinserted. It took under four minutes. I did this twice a day for four days. Nobody on the trek even knew I had my period — not because I was hiding it but because it genuinely did not affect anything.",
                        "My tentmate Richa found out when she saw me cleaning the cup. She asked me about twelve questions. By the time we reached the base camp she had already ordered one on her phone.",
                        "That mountain convinced me that the cup is not just a product, it is a tool for mobility and freedom in a way that pads simply cannot match."
                    ),
                    quote = "The cup is not just a product, it is a tool for mobility and freedom.",
                    keyMessage = "The ultimate freedom for physical adventures ⛰️",
                    source = "Source: Stonesoup brand community post",
                    backgroundColorHex = "#DDF2FA",
                    textColorHex = "#1F4E5B"
                ),
                PehliBaarStory(
                    id = "t2_s4",
                    title = "It Made Me Understand My Own Body",
                    subtitle = "Shared by Shreya Joshi, 24, Mumbai | Source: Feminism in India",
                    paragraphs = listOf(
                        "The strangest and most unexpected thing that happened when I switched to a menstrual cup was that I started to actually learn about my body.",
                        "With a pad, your period is something that happens to you and you manage from a distance. But with a cup, you empty it. You see exactly how much fluid you produce. You start to understand what heavy flow actually looks like. You learn what your cervix position feels like. You learn when your flow is different in colour or consistency.",
                        "I was twenty-two when I switched. I had been having periods for nine or ten years and I genuinely did not know these things about my own body. Pads had kept me at a comfortable arm's length.",
                        "The cup made it impossible to look away. In an educational way. I started tracking my cycle more carefully. I went to a gynaecologist for the first time in my life with actual, specific information about my cycle to share. She was visibly pleased. She said, \"You clearly use a menstrual cup.\" I said yes. She said, \"I can always tell.\""
                    ),
                    quote = "Pads had kept me at an arm's length. The cup made it impossible to look away.",
                    keyMessage = "Empowerment starts with knowing your own biology 🩺",
                    source = "Source: Feminism in India",
                    backgroundColorHex = "#FFD6C0",
                    textColorHex = "#E07A5F"
                )
            )
        ),
        StoryTheme(
            id = "theme_3",
            title = "Trying Tampons and Period Underwear",
            description = "New products. Old fear. And then — surprise.",
            icon = "🩲",
            backgroundColorHex = "#E8E0F0",
            textColorHex = "#6A5ACD",
            stories = listOf(
                PehliBaarStory(
                    id = "t3_s1",
                    title = "The OB Tampon and the Wedding Season",
                    subtitle = "Shared by Anisha Kapoor, 27, Delhi | Source: Quora India",
                    paragraphs = listOf(
                        "My cousin's wedding was in December and the reception involved a sangeet, two days of rituals, and a reception dinner — all in a venue in Gurgaon where I would be wearing a lehenga every single day. The sangeet lehenga was dark red, the rituals lehenga was ivory, and the reception lehenga was dark blue.",
                        "My period, as if personally offended by all this planning, arrived on day one of the sangeet.",
                        "I had never used a tampon before. I had heard about them, obviously, but in my head they occupied the same category as things that existed for Western women in movies. I had two options: cancel my sangeet attendance (obviously not), or try something different.",
                        "My colleague Simran kept OB tampons at her desk. She drove to a pharmacy in Gurgaon, bought them, and sent them to me. I read the instruction leaflet in a bathroom stall in my still half-worn lehenga. The instructions said \"relax your muscles.\" Relaxing your muscles while reading instructions wearing a ₹40,000 lehenga while sangeet music plays outside is not easy, but I managed it.",
                        "The tampon worked. I danced the entire night. All three nights. The ivory lehenga survived. I was completely unaware that I was doing something that apparently millions of Indian women have never tried. I use tampons only for occasions now, but I am so glad they were there."
                    ),
                    quote = "The instructions said 'relax your muscles' in a ₹40,000 lehenga.",
                    keyMessage = "Alternative products offer solutions for special moments 💃",
                    source = "Source: Quora India",
                    backgroundColorHex = "#E8E0F0",
                    textColorHex = "#6A5ACD"
                ),
                PehliBaarStory(
                    id = "t3_s2",
                    title = "Period Underwear and the Overnight Train",
                    subtitle = "Shared by Meghna Bose, 30, Kolkata | Source: SheThePeople",
                    paragraphs = listOf(
                        "I take the overnight train from Kolkata to Delhi twice a year to visit family. It is a journey of approximately seventeen hours and I have always found managing my period on it very difficult. The bathrooms on Indian trains are what they are. The berths are small. There is no real private space.",
                        "I bought period underwear in 2021 from a brand called Adira. I was skeptical. I had been disappointed by too many products that promised everything and delivered a different kind of mess.",
                        "For the train journey, I decided to try the period underwear as a backup layer along with a pad. By Allahabad — the midpoint — I realized the pad had not even been needed. The underwear had handled everything.",
                        "I arrived in Delhi feeling, for the first time, like a person who had simply slept on a train rather than someone who had spent the night managing a logistical challenge. I now use period underwear as my primary product. The overnight train is no longer something I dread scheduling around my cycle."
                    ),
                    quote = "I arrived feeling like a person who had simply slept on a train.",
                    keyMessage = "Period underwear provides ultimate overnight comfort and peace 🚆",
                    source = "Source: SheThePeople community posts",
                    backgroundColorHex = "#B5D5C5",
                    textColorHex = "#1E5E4A"
                ),
                PehliBaarStory(
                    id = "t3_s3",
                    title = "My Husband Bought Period Underwear for Me by Accident",
                    subtitle = "Shared by Kavitha R., 33, Bengaluru | Source: Menstrual Cup Users India",
                    paragraphs = listOf(
                        "My husband does the online shopping in our house because I find it boring and he finds it relaxing. I had shown him period underwear online once, explained what it was, and said I wanted to try it. He forgot about it for three months.",
                        "Then one evening he showed me an order confirmation: \"I ordered that underwear thing you wanted.\" He had ordered five pairs. He had also ordered them in what he described as \"the fancy ones\" — cotton-silk blends with small print patterns. They were genuinely beautiful, though roughly ₹700 per pair.",
                        "They were, in fact, excellent. Absorbent, comfortable, and they looked nothing like what you would expect period-management clothing to look like. They looked like nice underwear.",
                        "But the funniest part was when he asked how they were. I said fantastic. He said, \"I told my colleague about them.\" His colleague, a man in his mid-thirties, had apparently never heard of \"period underwear\" before and required a full explanation. My husband gave him one, without any embarrassment.",
                        "We talked about my period the way you talk about any health reality in a household. And somewhere in those conversations the embarrassment just quietly ran out of the room."
                    ),
                    quote = "We talk about periods the way you talk about any health reality in a household.",
                    keyMessage = "Open household conversations wash away period shame 🏡",
                    source = "Source: Facebook group Menstrual Cup Users India",
                    backgroundColorHex = "#F2B5C5",
                    textColorHex = "#C2405A"
                )
            )
        ),
        StoryTheme(
            id = "theme_4",
            title = "School Stories",
            description = "Uniforms. Teachers. Classmates. The perpetual fear of the white salwar.",
            icon = "🏫",
            backgroundColorHex = "#F5ECD5",
            textColorHex = "#6E2C00",
            stories = listOf(
                PehliBaarStory(
                    id = "t4_s1",
                    title = "The White Salwar and the Art Class Chair",
                    subtitle = "Shared by Ritika Mishra, 23, Lucknow | Source: Youth Ki Awaaz",
                    paragraphs = listOf(
                        "We had white salwar as part of our school uniform and I wore it three times a week. Anyone who has worn a white school uniform in India knows the mathematics you do in your head. You count days. You check the chair when you stand up.",
                        "The day I leaked in art class I was fifteen. It was a small stain. I stood up to borrow an eraser and I felt that slight cold awareness. I looked down: small stain on the white salwar.",
                        "The girl next to me — Pooja — saw it. She did not say anything. She stood up, untied the navy blue dupatta from around her own waist, handed it to me under the table, and then sat back down and continued drawing. The transaction happened in under five seconds.",
                        "I tied the dupatta, went to the bathroom, and cleaned up. The teacher and class did not notice. Pooja and I have been friends since Class 9 and that is part of the foundation of why.",
                        "Pooja says she did it because her older sister had warned her about exactly this situation and told her to always help if someone needed it. This is how knowledge travels between women. Horizontally. Sister to sister. Friend to friend."
                    ),
                    quote = "She stood up, untied her navy blue dupatta, and handed it to me under the table.",
                    keyMessage = "Sisterhood and silent support save the day in school 👭",
                    source = "Source: Youth Ki Awaaz",
                    backgroundColorHex = "#F0D5DC",
                    textColorHex = "#8B5E6D"
                ),
                PehliBaarStory(
                    id = "t4_s2",
                    title = "My Maths Teacher Saved Me",
                    subtitle = "Shared anonymously | Source: Reddit r/India",
                    paragraphs = listOf(
                        "My Class 10 maths teacher was Mrs. Usha Krishnamurthy. She was strict and dry. Her class was always silent because if it wasn't, she looked at you over her glasses in a way that made you feel responsible for the world's problems.",
                        "She also, apparently, kept a small pouch in her saree blouse at all times.",
                        "I discovered this because my period arrived in her class without warning. I had miscounted days. I had no pad, no money, and I was wearing my white uniform.",
                        "I went up to her desk and whispered that I needed to go to the medical room. She looked at me over her glasses, reached into her blouse, took out a small cotton pouch, and took out a folded Whisper pad. She put it on my textbook without a word, wrote a hall pass, and said \"Be back in ten minutes.\"",
                        "I told my best friend about this, and she said Mrs. Krishnamurthy had done the same thing for her older sister. That is what a school is supposed to be. A place where there is a woman with a cotton pouch in her saree, prepared for the emergencies teenage girls have."
                    ),
                    quote = "She reached into her blouse, took out a small cotton pouch, and gave me a pad.",
                    keyMessage = "Compassionate teachers build safe spaces in schools 🎒",
                    source = "Source: Reddit r/India",
                    backgroundColorHex = "#F0E5C8",
                    textColorHex = "#856404"
                ),
                PehliBaarStory(
                    id = "t4_s3",
                    title = "The Board Exam and the First Day of My Period",
                    subtitle = "Shared by Tanvi Ghosh, 22, Kolkata | Source: SheThePeople",
                    paragraphs = listOf(
                        "My Class 12 Chemistry board exam fell on the first day of my period. This was a nightmare scenario because my first day has always been my worst in terms of cramps.",
                        "I woke up feeling the cramps beginning. I ate something, took a Meftal Spas — the beloved Indian period cramp medicine every mother keeps in a specific drawer — and went to the exam centre.",
                        "The exam was three hours. The cramp medication took about forty minutes to work. I spent those forty minutes answering the questions I knew best and trying to breathe through the pain. I finished the paper with three minutes to spare, walked out, and called my mother. She asked if it went fine, and then asked \"Period theke gelo?\" (Did the period hold?) I said yes. She said \"Shabaash.\"",
                        "I got 88 in Chemistry. My mother never made my period a dramatic thing. Not a curse, not a spiritual event. Just a practical reality to be managed alongside everything else. I am grateful for that."
                    ),
                    quote = "My mother never made my period a dramatic thing. Just a practical reality.",
                    keyMessage = "Treating periods as a normal practical reality reduces exam stress ✍️",
                    source = "Source: SheThePeople",
                    backgroundColorHex = "#D5E8D2",
                    textColorHex = "#155724"
                ),
                PehliBaarStory(
                    id = "t4_s4",
                    title = "The School Trip to Agra",
                    subtitle = "Shared by Sneha Tiwari, 25, Kanpur | Source: Quora India",
                    paragraphs = listOf(
                        "Our Class 9 school trip to Agra was three days and involved the Taj Mahal, Agra Fort, and a bus with one working toilet. Four girls in my group of twelve got their periods during the trip. I was one of them.",
                        "The school had sent Miss Priya Ma'am, who taught Hindi, and she had a large handbag that turned out to contain an entire emergency period kit. She had eight different pad sizes, wet wipes, pain medication, and stick-on heat patches. She had never been asked to bring this; she just knew from experience.",
                        "On day two, when four of us were struggling, Priya Ma'am gathered us quietly during the lunch break. She handed out what was needed, gave the girl with cramps a heat patch, and bought extra supplies in the market. She managed the whole situation so smoothly that others did not realise anything had happened.",
                        "Miss Priya Ma'am retired last year. Several women commented on the school alumni group about the handbag and the emergency kit. She had carried it, every trip, for twenty-three years."
                    ),
                    quote = "She carried an entire emergency period kit in her bag for twenty-three years.",
                    keyMessage = "Unspoken preparation shows true care and dedication 🕌",
                    source = "Source: Quora India",
                    backgroundColorHex = "#E8E0F0",
                    textColorHex = "#6A5ACD"
                ),
                PehliBaarStory(
                    id = "t4_s5",
                    title = "The Sports Day and the Borrowed Shorts",
                    subtitle = "Shared by Priya Anand, 21, Coimbatore | Source: Menstrupedia",
                    paragraphs = listOf(
                        "Sports day in our school was a very big deal. We trained for weeks. I was in the relay team — 4x100 metres, and I ran the second leg.",
                        "My period arrived the morning of Sports Day. It arrived with the specific violence of a first-day period. I was wearing white shorts as part of our uniform. I had a pad on, but a pad and white shorts is not a combination that inspires confidence during a sprint.",
                        "My teammate Kavita came to me before the race and said she had a pair of dark blue cycling shorts she wore as compression shorts. She said I could wear them under the white shorts. \"Nobody will see them and they'll hold everything.\"",
                        "I wore them. I ran my leg. We came second in the relay. I crossed the finish line and punched the air. We jump up and down and Kavita asked, \"How were the shorts?\" I said, \"Perfect.\" She said, \"Good, same thing happened to me last year. I was going to come second place or not compete, and I chose second place.\"",
                        "We still say \"second place or not compete\" when a choice is difficult and you choose the option that lets you stay in the race."
                    ),
                    quote = "I wore her dark blue compression shorts. We came second in the relay.",
                    keyMessage = "Choosing to stay in the race, no matter the obstacles 🏃‍♀️",
                    source = "Source: Menstrupedia Community",
                    backgroundColorHex = "#D2EDE8",
                    textColorHex = "#0E6251"
                )
            )
        ),
        StoryTheme(
            id = "theme_5",
            title = "College and Hostel Life",
            description = "Shared bathrooms. Midnight emergencies. The pad under the pillow. The roommate who just understood.",
            icon = "🏛️",
            backgroundColorHex = "#DDF2FA",
            textColorHex = "#1F4E5B",
            stories = listOf(
                PehliBaarStory(
                    id = "t5_s1",
                    title = "The Hostel Pad Drawer",
                    subtitle = "Shared by Ananya Srivastava, 24, Allahabad/Delhi | Source: Youth Ki Awaaz",
                    paragraphs = listOf(
                        "When I arrived at my Delhi hostel for my first year, I shared a room with three other girls. By the end of the first week we had, without any formal discussion, created a system.",
                        "The middle drawer of the third almirah became the pad drawer. Everyone put whatever they had into it. Whisper, Stayfree, Sofy, generic brands, tampons, and even a menstrual cup. It evolved as a kind of spontaneous collective intelligence.",
                        "Someone needed a pad at 2am and knocked on doors; someone else was out of money; someone arrived with extra and put it in the drawer.",
                        "By November, other rooms on our floor were sending people to us. The hostel developed, room by room, its own informal mutual aid network entirely made of period supplies. The only implicit understanding was: take what you need, contribute what you can, and if someone asks at 2am, you open your door.",
                        "It was one of the most complete expressions of women looking out for each other. No apps, no campaigns. Just a middle drawer in an almirah."
                    ),
                    quote = "The middle drawer of the almirah became the community pad drawer.",
                    keyMessage = "Spontaneous sisterhood creates strong support networks 🚪",
                    source = "Source: Youth Ki Awaaz",
                    backgroundColorHex = "#DDF2FA",
                    textColorHex = "#1F4E5B"
                ),
                PehliBaarStory(
                    id = "t5_s2",
                    title = "My Roommate Understood Without Being Told",
                    subtitle = "Shared by Deepika M., 25, Hyderabad | Source: The Swaddle",
                    paragraphs = listOf(
                        "My second year of engineering was when I was diagnosed with PCOS. Before that, my cycles were unpredictable, varying from three to six weeks, with cramps so severe that I missed classes and told the attendance office I had \"stomach issues.\"",
                        "My roommate Bhavana was CS student, brilliant and slightly chaotic. We were roommates who shared a room: tolerant, independent. One evening, during a painful cycle, I was lying on my bed with a hot water bottle and crying quietly.",
                        "Bhavana came in, looked at me once, and went back out. She came back seven minutes later with three things: two Meftal Spas tablets, a packet of Parle-G, and a hot chocolate sachet she had borrowed from down the corridor. She put them on my table, climbed into her bed, and put her headphones on.",
                        "I ate the biscuits and drank the hot chocolate. An hour later she turned around: \"Feel better? PCOS?\" Her older sister had it, so she knew what the combination of unpredictable crying and a hot water bottle meant. Bhavana was the first person I told when I got my official diagnosis."
                    ),
                    quote = "She put Meftal, biscuits, and hot chocolate on my table without a word.",
                    keyMessage = "Quiet empathy speaks louder than words in times of pain ☕",
                    source = "Source: The Swaddle reader submissions",
                    backgroundColorHex = "#FFD6C0",
                    textColorHex = "#E07A5F"
                ),
                PehliBaarStory(
                    id = "t5_s3",
                    title = "The 3am Bathroom Queue and the Solidarity",
                    subtitle = "Shared by Neha Jain, 26, Jaipur/Pune | Source: SheThePeople",
                    paragraphs = listOf(
                        "Our hostel had eight rooms on each floor and one bathroom per floor with four stalls. This ratio was a problem, especially at 3am when you had your period and needed the bathroom urgently.",
                        "I woke up at 3am because my flow was heavier than expected. I went to the bathroom and found two other girls from my floor waiting in the queue. We stood there under the fluorescent light, someone started laughing, and then we all were.",
                        "In those eight minutes, we had the most honest conversation about cramps, exam stress, menstrual cups, and the college administration not installing a vending machine.",
                        "We had a specific kind of understanding when we passed in the corridor after that. A small nod of recognition. A vending machine was installed in the common area the following year after one of those girls joined the student council."
                    ),
                    quote = "We stood in the queue at 3am and had the most honest conversation about cramps.",
                    keyMessage = "Shared struggles build trust and positive changes 🤝",
                    source = "Source: SheThePeople community",
                    backgroundColorHex = "#B5D5C5",
                    textColorHex = "#1E5E4A"
                ),
                PehliBaarStory(
                    id = "t5_s4",
                    title = "Preparing for Exams on the First Day",
                    subtitle = "Shared by Ramya Krishnan, 23, Chennai | Source: Feminism in India",
                    paragraphs = listOf(
                        "My semester exams always seemed to coincide with my period. I had a final exam in Microeconomics in my third year. I had been studying all night, had my period, and had a headache.",
                        "I got to the exam hall and told the friend sitting next to me how I felt: \"I have my period, a headache, and I understood maybe forty percent of this syllabus.\" She looked at me and said, \"Same, but no period. Just forty percent understanding.\"",
                        "We both laughed. That small moment of honesty before the exam began changed something in the atmosphere. I felt less alone. The cramps and headache were still there, but it was lighter.",
                        "I got 62. Not brilliant, but passing, and in that context it felt like a success. We celebrated with filter coffee and idli at the canteen because that is what passing in terrible circumstances deserves."
                    ),
                    quote = "We laughed and celebrated both scores with filter coffee and idli.",
                    keyMessage = "Honesty with friends lightens the heaviest of days ☕",
                    source = "Source: Feminism in India",
                    backgroundColorHex = "#F2B5C5",
                    textColorHex = "#C2405A"
                )
            )
        ),
        StoryTheme(
            id = "theme_6",
            title = "Workplace and Internship Stories",
            description = "The new office. The important meeting. The long commute. The standing shift that never ends.",
            icon = "💼",
            backgroundColorHex = "#FFD6C0",
            textColorHex = "#E07A5F",
            stories = listOf(
                PehliBaarStory(
                    id = "t6_s1",
                    title = "My First Internship and the Office Bathroom Policy",
                    subtitle = "Shared by Ishita Malhotra, 23, Delhi | Source: Youth Ki Awaaz",
                    paragraphs = listOf(
                        "I started my first internship at a media company in Delhi. It was a good internship with an actual desk. I dressed carefully, trying to seem competent and unfazed.",
                        "My period arrived on day four. In college, you dealt with it between lectures or went back to the hostel. In an office, you do not know the bathroom situation, the dustbins, or the rules about being away from your desk.",
                        "I went to the bathroom and discovered that it had a proper covered dustbin in every stall, a small shelf, and a basket on the counter with three brands of pads in individual packets. Available, free.",
                        "I felt a wave of relief. I asked my manager about it. She said their office manager, Sunita Ji, had proposed it in her first month eight years ago. Sunita Ji had quietly restocked that basket month after month without making a big deal of it.",
                        "I thought about Sunita Ji for a long time. I want to be Sunita Ji someday."
                    ),
                    quote = "The bathroom counter had a basket with free pads. I want to be Sunita Ji someday.",
                    keyMessage = "Small, thoughtful office adjustments make a huge difference 🏢",
                    source = "Source: Youth Ki Awaaz",
                    backgroundColorHex = "#FFD6C0",
                    textColorHex = "#E07A5F"
                ),
                PehliBaarStory(
                    id = "t6_s2",
                    title = "The Long Delhi Metro Commute",
                    subtitle = "Shared by Aditi Sharma, 28, Delhi | Source: Scroll.in",
                    paragraphs = listOf(
                        "I commute ninety minutes each way on the Delhi Metro. The route involves two changes. During peak hours, you are standing shoulder-to-shoulder for ninety minutes.",
                        "On day one of my period, carrying a work bag with cramps beginning, the crowded Blue Line is a test of character. I have developed a system over three years: I carry an extra pad in the outer pocket of my bag, keep Meftal in my earphone case, and choose a corner spot to lean against.",
                        "I had to figure all of this out myself through trial and error. No doctor or app told me how to manage a ninety-minute standing commute. I learned it from my own body and occasional conversations with other commuting women.",
                        "When I talk to younger colleagues, I tell them all of it. Because nobody told me, and I spent months being more miserable than necessary before working it out. Public spaces need open conversations about period management."
                    ),
                    quote = "Having a wall to lean against on a crowded Metro is one of life's small mercies.",
                    keyMessage = "Sharing practical survival tips helps younger women adapt faster 🚇",
                    source = "Source: Scroll.in comments section",
                    backgroundColorHex = "#F5ECD5",
                    textColorHex = "#6E2C00"
                ),
                PehliBaarStory(
                    id = "t6_s3",
                    title = "Standing for Eight Hours in Retail",
                    subtitle = "Shared by Priya Singh, 26, Mumbai | Source: Menstrupedia",
                    paragraphs = listOf(
                        "I worked at a clothing retail store in a Mumbai mall. The shift was eight hours. You stood for eight hours. There were two breaks — one of fifteen minutes and one of thirty. Otherwise, you were on your feet.",
                        "I have endometriosis. On my bad days, my pain level is significant. On the retail floor, during a shift, I could not sit down. I told my manager about my endometriosis within my first month because I needed her to know why I might need to sit.",
                        "She listened without expression, then said, \"We have a storeroom with two chairs. When it is bad, you can go there for five minutes. Tell me or Rekha, we will cover.\" That was it. She made a small accommodation and expected nothing in return except that I tell her.",
                        "On my worst days, I went to the storeroom twice. It was small and full of inventory, but it was private and quiet. Retail work is physically demanding, and this conversation has barely begun. My manager's kindness should be policy, not just a matter of decency."
                    ),
                    quote = "My manager's kindness should be policy, not just a matter of decency.",
                    keyMessage = "Workplace policy must support menstrual health accommodations 🛍️",
                    source = "Source: Menstrupedia community",
                    backgroundColorHex = "#D2EDE8",
                    textColorHex = "#0E6251"
                ),
                PehliBaarStory(
                    id = "t6_s4",
                    title = "The Client Meeting on Day Two",
                    subtitle = "Shared by Radhika Mehta, 31, Mumbai | Source: The Swaddle",
                    paragraphs = listOf(
                        "I work in consulting. Meetings are the job. A significant client presentation on day two of my cycle in a boardroom with twelve people when I have cramps is a situation I have encountered three times.",
                        "The first time, I sat through the three-hour meeting saying nothing, white-knuckling, and went to the bathroom afterwards. The second time, I was strategic, took Meftal beforehand, and it was easier.",
                        "The third time, I told my colleague Shruti, who was co-presenting, that I had bad cramps. She said, \"You want to sit during the Q&A section? I can manage the room.\" She managed the room, I sat, the clients did not notice, and the presentation went well.",
                        "I did not lose credibility or professionalism by telling Shruti. I gained a co-presenter who adjusted for me. What actually makes someone capable is knowing what they need and asking for it clearly."
                    ),
                    quote = "What actually makes someone capable is knowing what they need and asking for it clearly.",
                    keyMessage = "Clear communication with colleagues saves pain and secures success 💼",
                    source = "Source: The Swaddle reader submissions",
                    backgroundColorHex = "#DDF2FA",
                    textColorHex = "#1F4E5B"
                )
            )
        )
    )
}

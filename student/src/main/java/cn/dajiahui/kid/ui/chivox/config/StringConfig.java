package cn.dajiahui.kid.ui.chivox.config;

import com.chivox.cube.pattern.LmText;

import java.util.ArrayList;
import java.util.List;

import cn.dajiahui.kid.R;


public class StringConfig {
    public static String[] refTextWord={"project"};
    //public static String[] refTextWord={"past","school","goddess","apple","tomorrow","good","teacher","computer","photo","relative","energy","analysis","mutton","pork","beaf"};
    //public static String[] refTextWord={"A","B","C","D","E","F","G","H"};
    //public static String[] refTextWord={"strike","schedule","un-satisfied","disrespectful","star","icecream","school","speak","student"};
    public static String[] refTextSent = {"Hi, may I speak to Tim"};
    public static String[] refTextEnSentRec = {"Good Morning, teacher.|Good afternoon, teacher.|Good night,teacher."};
    public static String[] refTextpred = {"It was a beautiful day, the sun was kissing the horizon at the edge of the sea when she met her Mr.right. They fell in love with each other at first sight. After forty five years, they lie down in their yard look at their family, everything was just like yesterday."};
    private static List<LmText> refTextStoryRepeat = new ArrayList<LmText>();
    private static List<LmText> targetConversation = new ArrayList<LmText>();
    private static List<LmText> refTextConversation1 = new ArrayList<LmText>();
    private static List<LmText> refTextConversation2 = new ArrayList<LmText>();
    private static List<LmText> refTextConversation3 = new ArrayList<LmText>();
    private static List<LmText> refTextConversation4 = new ArrayList<LmText>();
    private static List<LmText> refTextPicTalk = new ArrayList<LmText>();
    private static List<LmText> refTextOralWriting = new ArrayList<LmText>();

    public static  List<LmText> getStoryRepeatRefText (){
        refTextStoryRepeat.clear();
        refTextStoryRepeat.add(new LmText("As we all know, different countries have their own special body language. If you don't know the differences, maybe you will misunderstand them. Jack is a good case in point. One day, he was travelling on a train to a small town in India. Next to him was a young man, who was a native. Jack was a talkative person and soon they became familiar to each other. \"Are you from India?\" asked Jack, the young man smiled and shook his head. So where are you from? Jack asked again. To Jack's surprise, the young man said that he was indeed a native. But why did you shake your head? Jack was a little confused. Luckily, the young man was very smart, he suddenly became aware that Jack must misunderstand him because he knew a little body language in different countries when expressing agree or disagree. After explaining to Jack, Jack's confusion had been melted. With time going on, Jack wanted to go to the restroom, he asked the young man, \"Is it the right way to the restroom\", and the young man nodded his head this time. \"Oh, I understand, it is on the opposite side.\"","1.0"));
        refTextStoryRepeat.add(new LmText("As we all know, different countries have different body language, if you don't know it, you will misunderstand them. Jack is a case in point, one day he was on the train to a small town of India and next to him is an India young man. Jack is a talkative person, so they get familiar with each other quickly. Jack asked the young man whether he is from Indian, then the young man shook his head. So where are you from? Jack asked him, the young man said he was indeed a native, jack was confused about it, you are a native, but why you shook your head. Fortunately, the young man is a very smart and he knew something about body language he thought Jack was misunderstood his meaning, he explained it to Jack, and at last Jack made clear of it. With time going on, Jack went to a restaurant, he asked the waiter: is this the right way to the rest room? The waiter nodded his head, and then Jack knew that the opposite way is the right way of it.","1.0"));
        refTextStoryRepeat.add(new LmText("As we all know different countries have their own special body languages. If you don't know the difference, you may misunderstand them. Jack is a typical case in point. One day he was on a small train in India. He met a young Indian man .Because Jack was an active talker, they became familiar with each other very soon. Then Jack asked the young man whether he came from India or not. The young Indian smiled and shook his head. So Jack continued to ask the young man the same question. The young man said???\"You are a complete idiot!\" Jack was so confused. The Indian was smarted and explained the meaning of shaking head in India. Shaking head means agreement while nodding head means disagreement. After that, Jack's confusion melted. Later Jack was going to a restaurant. He asked the young man: \"Is it the right way?\" The young man nodded his head. Jack understood that the restaurant was on the opposite side.","1.0"));
        refTextStoryRepeat.add(new LmText("Different countries have different body languages. If you have no idea on this issue, you may get yourself in big trouble while travelling in foreign countries. Jack exemplifies the point when he travelled in India. One day during his journey in India, Jack encountered a native young man, and they soon became familiar with each other. Jack asked the young man whether he was native. The young man smiled and shook his head, and Jack felt so confused and continued to ask him where he was from. Suddenly, the young man became aware that Jack knew little about the body language in India and then explained the difference to Jack. Therefore, Jack's confuse melted away. Later, Jack asked the young man whether it was the right way to the restaurant, and the young man nodded this time. Jack clearly knew it meant not.","1.0"));
        refTextStoryRepeat.add(new LmText("Different countries have different body language. If people don't know about it, misunderstanding may happen. Jack travelled in India, confused about nodding head and snaking head. In India, nodding head means \"No\", but shaking head means \"Yes\". So, misunderstanding happened between Jack and his friend during their talk with body language.","1.0"));
        refTextStoryRepeat.add(new LmText("Different countries have their different body languages. If you don't know, you will misunderstand them. Jack's example is very typical of this phenomenon. Because of Jack's little knowing of body language, he made a lot of embarrassment in public.","1.0"));
        return refTextStoryRepeat;
    }

    public static List<LmText> getTargetConversation (int conversation) {
        refTextConversation1.clear();
        refTextConversation2.clear();
        refTextConversation3.clear();
        refTextConversation4.clear();
        //init answer for conversation1
        refTextConversation1.add(new LmText("This is Tom's father. ","1.0"));
        refTextConversation1.add(new LmText("This is Tom's Dad.","1.0"));
        refTextConversation1.add(new LmText("I'm Tom's father.","1.0"));
        refTextConversation1.add(new LmText("I'm Tom's Dad.","1.0"));
        refTextConversation1.add(new LmText("I am Tom's father.","1.0"));
        refTextConversation1.add(new LmText("I am Tom's dad.","1.0"));
        refTextConversation1.add(new LmText("This is Tom's father.","1.0"));
        refTextConversation1.add(new LmText("This is Tom's father speaking.","1.0"));
        refTextConversation1.add(new LmText("Tom's father is speaking.","1.0"));
        refTextConversation1.add(new LmText("This is Tom's father.","1.0"));
        refTextConversation1.add(new LmText("This is Tom's father speaking.","1.0"));
        //init answer for conversation2
        refTextConversation2.add(new LmText("Sorry, he isn't in.","1.0"));
        refTextConversation2.add(new LmText("Sorry, he is out.","1.0"));
        refTextConversation2.add(new LmText("Sorry. He isn't in.","1.0"));
        refTextConversation2.add(new LmText("Sorry. He is outside","1.0"));
        refTextConversation2.add(new LmText("I'm sorry, he is out.","1.0"));
        refTextConversation2.add(new LmText("I'm sorry, he is not in.","1.0"));
        refTextConversation2.add(new LmText("I'm sorry. He is not at home.","1.0"));
        refTextConversation2.add(new LmText("I'm sorry. He is not at home now.","1.0"));
        refTextConversation2.add(new LmText("I'm afraid that he is not at home now.","1.0"));
        refTextConversation2.add(new LmText("Sorry. Tom is not in.","1.0"));
        refTextConversation2.add(new LmText("Sorry. Tom is not Home","1.0"));
        //init answer for conversation3
        refTextConversation3.add(new LmText("Can I take a message for him?","1.0"));
        refTextConversation3.add(new LmText("Let me take a message for him, will you? ","1.0"));
        refTextConversation3.add(new LmText("May I take a message for him?","1.0"));
        refTextConversation3.add(new LmText("Do you want to leave a message?","1.0"));
        refTextConversation3.add(new LmText("Can I give a message to him?","1.0"));
        refTextConversation3.add(new LmText("Can I take a massage for him?","1.0"));
        refTextConversation3.add(new LmText("May I leave him a message?","1.0"));
        refTextConversation3.add(new LmText("May I send a message for him?","1.0"));
        refTextConversation3.add(new LmText("Do I have to take him a message?","1.0"));
        refTextConversation3.add(new LmText("Could I leave him a message?","1.0"));
        refTextConversation3.add(new LmText("Can I keep him informed of what you said?","1.0"));
        //init answer for conversation4
        refTextConversation4.add(new LmText("Anything else?","1.0"));
        refTextConversation4.add(new LmText("Is there anything else?","1.0"));
        refTextConversation4.add(new LmText("Is there anything else you want to tell him?","1.0"));
        refTextConversation4.add(new LmText("Any other things?","1.0"));
        refTextConversation4.add(new LmText("Do you have anything else?","1.0"));
        refTextConversation4.add(new LmText("What else?","1.0"));
        refTextConversation4.add(new LmText("What else do you want to say?","1.0"));
        refTextConversation4.add(new LmText("Other things? ","1.0"));
        refTextConversation4.add(new LmText("What other things do you have?","1.0"));
        refTextConversation4.add(new LmText("Is that all?","1.0"));
        refTextConversation4.add(new LmText("Do you have other things?","1.0"));

        switch (conversation) {
            case 1:
                targetConversation = refTextConversation1;
                break;
            case 2:
                targetConversation =refTextConversation2;
                break;
            case 3:
                targetConversation = refTextConversation3;
                break;
            case 4:
                targetConversation = refTextConversation4;
        }
        return targetConversation;
    }

    public static List<LmText> getRefTextPicTalk (){
        refTextPicTalk.clear();
        refTextPicTalk.add(new LmText("Lucy and Kate are neighbors. They have a good relationship. They often get together to kill time and help each other to do the homework. They do the sports together. They often go out together the walk the babies. Sometimes when the weather is bad, they will visit each other and enjoy a cup of tea in each other's home. At this time, their babies will stay together and have a good time, too.","1.0"));
        refTextPicTalk.add(new LmText("Lucy and Kate are good friends. And they live next door. So they do a lot of things together. For example, they usually have tea in the beautiful garden in the afternoon. Also, their brothers like to play football at school. Their mothers prefer to babysit while jogging in the morning. After that, they will have a nice chat in the kitchen with babies playing around.","1.0"));
        refTextPicTalk.add(new LmText("Lucy and Kate are neighbors. The two families get along well with each other. Women often stay together for afternoon tea. Their children are best friends and they often take part in the sports game together.","1.0"));
        refTextPicTalk.add(new LmText("Lucy and Kate are neighbors and they have a good relationship when they were little girls, they often played at having a light meal with toys, chatting and sharing felling. When they were in the high school, Lucy and Kate liked to play football games together as they are kink football fans. After they got married, their friendship became better and better. Through they have their own babies, they still spend time together jogging or wandering. Since the babies grow older, Lucy and Kate can really share their light meal with each other while kids are playing around.","1.0"));
        refTextPicTalk.add(new LmText("Kate and Lucy are good neighbors. They always have some tea and chat with each other in the afternoon. They even have the same crazy hobby: playing football. They love doing exercise, so they always run to go shopping. Sometimes, they invite each other to their home to have dinner together. Their children play with each other happily.","1.0"));
        refTextPicTalk.add(new LmText("Lucy and Kate are neighbors. They get on well with each other. Their family members often play football together. Sometimes in winter, they take a walk or jog in the morning. They also like to sit together to drink coffee or tea. They usually have a wonderful time.","1.0"));
        refTextPicTalk.add(new LmText("Kate and Lucy are neighbors and they get on well with each other. They often have drinks and meals together. In their spare time, they usually play football in the garden. In winter, they can come out and run along the river. They can even make snow man happily. They will always be best friends.","1.0"));
        refTextPicTalk.add(new LmText("Lucy and Kate are neighbors. They are very friendly to each other. They often drink tea together. They often play football with other people in the football field. They work together. In the afternoon, they often take their children to the sports field to play. They are very happy","1.0"));
        refTextPicTalk.add(new LmText("Lucy and Kate are neighbors. They are also good friends. They often play football together. Both Lucy and Kate have babies. Sometimes they take their babies out, and sometimes they chat at home while their babies are playing together. They enjoy the time very much.","1.0"));
        refTextPicTalk.add(new LmText("Lucy and Kate are neighbors and also very good friends. If the weather is nice in spring and summer, Lucy and Kate will go out to sit in the garden, drinking some tea and talking. At the same time, the children will be playing football nearby. In winter, Lucy and Kate also often spend time together, for instance, they always take a walk in the park with their babies in the prams. Sometimes, Lucy and Kate will stay in the house, talking and drinking tea, with their kids playing around.","1.0"));
        refTextPicTalk.add(new LmText("Lucy and Kate are neighbor. They know well each other. In spring, they often drink the tea together in the yard. In summer, they often play football outside. In autumn, they often do more exercise in the morning. In winter, they often have a chant in the house to keep warm.","1.0"));
        refTextPicTalk.add(new LmText("Lucy and Kate are neighbors. They get on well with each other. They often have a big dinner together. Sometimes they go to play football with friends. In the morning, they usually do morning exercise together because they both think it's good for health. At weekends, they like to stay at home to have a chat.","1.0"));
        refTextPicTalk.add(new LmText("Lucy and Kate are having a tea party. They're sat at a table with some teddy bears. Next they are playing football together. One of the girls is kicking the ball at the goal. Next they are going for a walk together and one of them is pushing a pram. Lastly, they are sitting in the kitchen looking after their children.","1.0"));
        return refTextPicTalk;
    }

    public static List<LmText> getRefTextOralWriting (){
        refTextOralWriting.clear();
        refTextOralWriting.add(new LmText("I was in the cinema when the earthquake started. The earth started to shake hard. I tried to run out side, but pieces of glass and bricks fell down, and the walls began to come down. Finally, the noise and shaking ended. I realized my leg was trapped. I screamed for help. I was afraid that no one would hear me. Luckily, a dog found me and someone pulled me out.","1.0"));
        refTextOralWriting.add(new LmText("When I was in the cinema the earthquake happened. The earth started to shake hard. I tried to run out, but pieces of glass and bricks fell down, and the walls began to collapse. At last, the noises and shaking ended. I realized my leg was stuck. I screamed for help. I was afraid that nobody would hear me. Luckily, a dog found me and someone pulled me out.","1.0"));
        refTextOralWriting.add(new LmText("The earth shook when I was reading in the cinema, the earth was shaking heavily. I tried to run out, but the glass and bricks fell down, the wall began to fell down. At last, all the noises and shaking stopped. Then I realized that one of my legs got stuck. I yielded for help because I was worried nobody could hear me. Luckily, a dog found me and then someone pulled me out.","1.0"));
        refTextOralWriting.add(new LmText("I was in the cinema when the earthquake occurred. The ground started shaking violently. I tried to run outside. But the glass fragments and bricks smashed down. The wall started to collapse. At last, both the Noise and shaking stopped. I realized that one of my legs was stuck. I screamed for help. And I was worried that nobody could hear me. Luckily, a dog found me and someone pulled me out.","1.0"));
        refTextOralWriting.add(new LmText("When the earthquake occurred, I was in the cinema. The floor shook violently. I tried to run to the outside, but the glasses and bricks smashed down, and the wall began to collapse. At last, the noise and shaking stopped. I realized that one of my legs was stuck. So I screamed loudly for help, for the fear that no one could hear me. Luckily, a dog saw me, and someone dragged me out.","1.0"));
        refTextOralWriting.add(new LmText("I was in the cinema when the earthquake happened. The floor began to shake strongly. I tried to run out of the cinema. However, the pieces of glass and bricks fell down. The wall started to collapse. At last, the noise and shaking stopped. I realized that my leg got stuck. Then I shouted loudly, because I am worried that nobody could hear me. Luckily, a dog found me, and somebody pulled me out.","1.0"));
        refTextOralWriting.add(new LmText("I was in the cinema when the earthquake happened. The ground began to shake very fiercely, and I tried to run out, but many pieces of glass rags and bricks fell down, then the wall started to collapse. At last, the noise and shake stopped. I realized that one of my legs got stuck, and I screamed for help, worrying that no one would hear me. The lucky thing is that a dog found me and then someone dragged me out.","1.0"));
        refTextOralWriting.add(new LmText("I was in the cinema when the earthquake occurred. The ground started to shake violently. I tried to escape, but glass shards and bricks fell down and the wall began to collapse. Finally, the noise and shaking stopped. I realized that one of my legs got stuck. I screamed for help and I was worried whether nobody would hear me or not. Fortunately, a dog found me, so I was dragged out by a kind person.","1.0"));
        refTextOralWriting.add(new LmText("I was in the cinema when the earthquake occurred. The floor shook heavily, I tried to run out, but the fragment of glass and bricks fell down, the wall began to collapse. At last the noise and shaking stopped. Then I realized one of my legs was stuck, I screamed help, but no one heard me, fortunately, a dog found me, and people pulled me out of it at last.","1.0"));
        refTextOralWriting.add(new LmText("I was in the cinema when the earthquake happened. The floor started shaking violently. I tried to run out, but glass pieces and bricks fell down and the walls started crumbling down. At last, all the noise and shaking stopped. I realized that one of my legs was stuck. I screamed for help, for fear that no one would hear me. Luckily a dog found me. Then someone pulled me out.","1.0"));
        refTextOralWriting.add(new LmText("I was in the cinema when the earthquake broke out. The ground started to shake violently. I tried to run out, but the glass fragment and bricks had already begun to fall down, and the walls started to collapse. All of a sudden, the noise and shaking came to an end. When I regained consciousness, I realized my leg got stuck in ruins. I screamed for help and I was afraid that no one would hear me. Luckily, a dog found me and someone pulled me out.","1.0"));
        refTextOralWriting.add(new LmText("I was in the cinema when the earthquake started. The earth started to shake hard. I tried to run outside, but pieces of glass and bricks fell down, and the walls began to collapse. Finally, the noise and shaking all stopped. I realized that one of my legs was stuck. I screamed for help, worried no one would hear me. Fortunately a dog found me and then someone pulled me out.","1.0"));
        refTextOralWriting.add(new LmText("When the earthquake happened, I was in the cinema. The floor started to shake violently. I tried to run out of the cinema. Broken glasses and bricks started to fall down, and so did the walls. At last, all sound and shaking stopped. I realized that one of my legs was stuck. I cried for help at the top of my voice, for fear that I cannot make myself heard by others. Luckily, a dog found me, and someone pulled me out.","1.0"));
        refTextOralWriting.add(new LmText("I was in the cinema when the earthquake occurred. The ground began to shake fiercely. I tried to run out of the cinema but the glass fragment and brick smashed down. The wall collapsed. At last the sound and shaking was stopped. I felt that one of my leg was stuck. I shouted for help. Finally a dog found me, someone pulled me out.","1.0"));
        refTextOralWriting.add(new LmText("I was in the cinema when the earthquake started. The ground began to shake strongly. I tried to run outside. But the pieces of the glass and the bricks fell down. The wall began to collapse. At last, the noise and the shaking stopped. I realized that one of my legs was stuck. I shouted for help for fear that nobody would notice me. Luckily, a dog found me and someone pulled me out.","1.0"));
        return refTextOralWriting;
    }

    public static int[] sentRecTextView = {R.string.tvSentRec,R.string.tvSentRec1,R.string.tvSentRec2};
    public static String[] sentRec = {"I need a pair of sizsse 8 shoe.|I need a cup of coffe.|I need fried chicken wings and chips.|I need a ticket for \"The lord of the Ring\"",
            "How many people are there?|Do you have a pen?|Can you call him?",
            "I'm looking for the map of China.|I'm looking for some shoes.|You look nine, do you have a boyfriend?"};
    public static String[] sentRecAnswers = {"I need a cup of coffe.","Can you call him?","I'm looking for some shoes."};
}

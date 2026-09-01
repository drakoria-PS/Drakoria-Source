package com.skyfire.world.content;

import com.skyfire.util.Misc;
import com.skyfire.world.World;
import com.skyfire.world.entity.impl.player.Player;

/*
 * @author Arlania - Arlania rsps
 */
public class TriviaBot {
	
	public static final int TIMER = 1400; //1800
	public static int botTimer = TIMER;
	
	public static int answerCount;
	public static String firstPlace;
	//public static String secondPlace;
	//public static String thirdPlace;

	//public static List<String> attempts = new ArrayList<>(); 

	public static void sequence() {
		
		if(botTimer > 0)
			botTimer--;
		if(botTimer <= 0) {
			botTimer = TIMER;
			didSend = false;
			askQuestion();
		}
	}
	
	public static void attemptAnswer(Player p, String attempt) {
		
		if (!currentQuestion.equals("") && attempt.replaceAll("_", " ").equalsIgnoreCase(currentAnswer)) {
			
			if (answerCount == 0) {
				answerCount++;
				p.getPointsHandler().incrementTriviaPoints(10);
				p.getPacketSender().sendMessage("@dre@You Received @red@[10]@dre@ trivia points for @red@1st Place.");
				p.getPointsHandler().refreshPanel();
				firstPlace = p.getUsername();
				World.sendMessage("@blu@[TRIVIA] @red@[Winner:@blu@" +firstPlace+"@red@ [10] pts@bla@], Nice!");
				//String[] s = Arrays.asList(attempts);
				//World.sendMessage("@blu@[TRIVIA] @red@Failed attempts: "+s);
				currentQuestion = "";
				didSend = false;
				botTimer = TIMER;
				answerCount = 0;
				return;
			}
		} else {
			if(attempt.contains("question") || attempt.contains("repeat")){
				p.getPacketSender().sendMessage("<col=800000>"+(currentQuestion));
				return;
			}

			//attempts.add(attempt); // need to add a filter for bad strings (advs, curses)
			p.getPacketSender().sendMessage("@blu@[TRIVIA]@red@ Sorry! Wrong answer! The current question is: +");
			p.getPacketSender().sendMessage("@blu@[TRIVIA]@red@ "+(currentQuestion));
			return;
		}
		
	}
	
	public static boolean acceptingQuestion() {
		return !currentQuestion.equals("");
	}
	
	private static void askQuestion() {
		for (int i = 0; i < TRIVIA_DATA.length; i++) {
			if (Misc.getRandom(TRIVIA_DATA.length - 1) == i) {
				if(!didSend) {
					didSend = true;
				currentQuestion = TRIVIA_DATA[i][0];
				currentAnswer = TRIVIA_DATA[i][1];
				World.sendMessage(currentQuestion);
				
				
				}
			}
		}
	}
	
	public static boolean didSend = false;
	
	private static final String[][] TRIVIA_DATA = {
			{"@blu@[TRIVIA]@red@ Name the world's biggest island", "Greenland"},
			{"@blu@[TRIVIA]@red@ What is the world's longest river?", "Amazon river"},
			{"@blu@[TRIVIA]@red@ What is the capital city of Spain?", "Madrid"},
			{"@blu@[TRIVIA]@red@ Which country is Prague in?", "Czech Republic"},
			{"@blu@[TRIVIA]@red@ What colour is a Welsh poppy? ", "Yellow"},
			{"@blu@[TRIVIA]@red@ What colour is a Himalayan poppy?", "Blue"},
			{"@blu@[TRIVIA]@red@ Give the alternative name for a Mountain Ash tree", "Rowan"},
			{"@blu@[TRIVIA]@red@ Name the game played on a lawn called a 'crown green'.", "Bowls"},
			{"@blu@[TRIVIA]@red@ Which chess piece can only move diagonally?", "bishop"},
			{"@blu@[TRIVIA]@red@ How many valves does a trumpet have?", "3"},
			{"@blu@[TRIVIA]@red@ What is John Leach famous for making?", "Pottery"},
			
			{"@blu@[TRIVIA]@red@ When did the Cold War end?", "1989"},
			{"@blu@[TRIVIA]@red@ In publishing, what does POD mean?", "Print on demand"},
			{"@blu@[TRIVIA]@red@ How tall would a double elephant folio book be?", "50 inches"},
			{"@blu@[TRIVIA]@red@ On 'Blue Peter', what was John Noakes's dog called?", "Shep"},
			{"@blu@[TRIVIA]@red@ What is sushi traditionally wrapped in?", "Edible seaweed"},
			{"@blu@[TRIVIA]@red@ What is allspice alternatively known as?", "Pimento"},
			{"@blu@[TRIVIA]@red@ What does the 'N' stand for in NATO?", "North"},
			{"@blu@[TRIVIA]@red@ What genre of music did Taylor Swift start in?", "Country"},
			{"@blu@[TRIVIA]@red@ What is Fergie’s real name?", "Stacie Ann Ferguson"},
			{"@blu@[TRIVIA]@red@ Who killed the Minotaur?", "Theseus"},
			{"@blu@[TRIVIA]@red@ Who is the Greek Goddess of love?", "Aphrodite"},
			
			
			{"@blu@[TRIVIA]@red@ Who was Brad Pitt’s first wife?", "Jennifer Aniston"},
			{"@blu@[TRIVIA]@red@ What year was Johnny Depp born?", "1963"},
			{"@blu@[TRIVIA]@red@ In which year died Michael Jackson? ", "2009"},
			{"@blu@[TRIVIA]@red@ How many siblings has Paris Hilton?", "3"},
			{"@blu@[TRIVIA]@red@ What is tattooed on Ozzy Osbourne’s knees?", "Smiley Faces"},
			{"@blu@[TRIVIA]@red@ What is Pinks real name?", "Alecia Beth Moore"},
			{"@blu@[TRIVIA]@red@ What is acrophobia?", "fear of heights"},
			{"@blu@[TRIVIA]@red@ Who painted the ceiling of the Sistine Chapel?", "Michelangelo"},
			{"@blu@[TRIVIA]@red@ Which artist said Everything you can imagine is real?", "Pablo Picasso"},
			{"@blu@[TRIVIA]@red@ The Lady Lever Art Gallery is in which English city?", "Liverpool"},
			{"@blu@[TRIVIA]@red@ What nationality was painter Frida Kahlo?", "Mexico"},
			
			
			
			
			{"@blu@[TRIVIA]@red@ What Greek city-state was named for the goddess of wisdom?", "athens"},
		
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
		{"@blu@[TRIVIA]@red@ raeldu", "Durael"},
		{"@blu@[TRIVIA]@red@ dol si namwe", "Wise Old Man"},
		{"@blu@[TRIVIA]@red@ yfiskre", "Skyfire"},
		{"@blu@[TRIVIA]@red@ acnmsdodl", "mcdonalds"},
		{"@blu@[TRIVIA]@red@ elaiynl", "yanille"},
		{"@blu@[TRIVIA]@red@ dagru", "guard"},
		{"@blu@[TRIVIA]@red@ nrogadeu", "ardougne"},
		{"@blu@[TRIVIA]@red@ koaamzr", "zamorak"},
		{"@blu@[TRIVIA]@red@ aodbns", "bandos"},
		{"@blu@[TRIVIA]@red@ ipaehklt", "kalphite"},
		{"@blu@[TRIVIA]@red@ esindwserl", "wilderness"},
		{"@blu@[TRIVIA]@red@ ngrado", "dragon"},
		{"@blu@[TRIVIA]@red@ erfolw", "flower"},
		{"@blu@[TRIVIA]@red@ cusenpre", "runescape"},
		{"@blu@[TRIVIA]@red@ nkba", "bank"},
		{"@blu@[TRIVIA]@red@ umsrme", "summer"},
		{"@blu@[TRIVIA]@red@ ohtn", "thon"},
		{"@blu@[TRIVIA]@red@ iezazm", "zezima"},
		{"@blu@[TRIVIA]@red@ 3dfiedl", "defil3d"},
		{"@blu@[TRIVIA]@red@ ktoarvh", "vorkath"},
		{"@blu@[TRIVIA]@red@ haetr eunr", "earth rune"},
		{"@blu@[TRIVIA]@red@ csrylat eky", "crystal key"},
		{"@blu@[TRIVIA]@red@ darwzi", "wizard"},
	{"@blu@[TRIVIA]@red@ Batman lives in which city?", "Gotham"},
		{"@blu@[TRIVIA]@red@ Who came up with the three laws of motion?", "Isaac newton"},
		{"@blu@[TRIVIA]@red@ What is a baby goat called?", "Kid"},
		{"@blu@[TRIVIA]@red@ What is the usual postal service for wizards and witches?", "Owl post"},
		{"@blu@[TRIVIA]@red@ What is a group of jelly fish called?", "Alaska"},
		{"@blu@[TRIVIA]@red@ What city is the most populated city on earth?", "Tokyo"},
		{"@blu@[TRIVIA]@red@ Which team plays at the oldest stadium in the NFL?", "Chicago bears"},
		{"@blu@[TRIVIA]@red@ What house at Hogwarts does Harry belong to?", "Gryffindor"},
		{"@blu@[TRIVIA]@red@ What is the world's longest river?", "Amazon"},
		{"@blu@[TRIVIA]@red@ Name the world's largest ocean.", "Pacific ocean"},
		{"@blu@[TRIVIA]@red@ Who played Neo in The Matrix?", "Keanu reeves"},
		{"@blu@[TRIVIA]@red@ What year did the Cold War end?", "1989"},
		{"@blu@[TRIVIA]@red@ What colour is Absynthe?", "Green"},
		{"@blu@[TRIVIA]@red@ The hashtag symbol is technically called?", "Octothorpe"},
		{"@blu@[TRIVIA]@red@ What is Cookie Monsters actual name?", "Sid"},
		{"@blu@[TRIVIA]@red@ What science element begins with the letter: K", "Krypton"},
		{"@blu@[TRIVIA]@red@ What continent has the fewest flowering plants?", "Antarctica"},
		{"@blu@[TRIVIA]@red@ Davids father has three sons, Snap, Crackle and ___?", "David"},
		{"@blu@[TRIVIA]@red@ What runs around the whole yard without moving?", "Fence"},
		{"@blu@[TRIVIA]@red@ What Gets wet when drying?", "Towel"},
		{"@blu@[TRIVIA]@red@ In what year did it snow in the Sahara Desert?", "1979"},
		{"@blu@[TRIVIA]@red@ What Gets Bigger The More You Take Away?", "A hole"},
		{"@blu@[TRIVIA]@red@ Whats the capital of Russia", "Moscow"},
		{"@blu@[TRIVIA]@red@ What goes up, but never comes down...", "Age"},
		{"@blu@[TRIVIA]@red@ What has a head, a tail, but no body", "Coin"},
	{"@blu@[TRIVIA]@red@ Guess a number 0-10?", "0"},
		{"@blu@[TRIVIA]@red@ Guess a number 0-10?", "3"},
		{"@blu@[TRIVIA]@red@ Guess a number 0-10?", "9"},
		
		{"@blu@[TRIVIA]@red@ Guess a number 0-10?", "3"},
		{"@blu@[TRIVIA]@red@ Guess a number 0-10?", "5"},
		{"@blu@[TRIVIA]@red@ Guess a number 0-20?", "5"},
		{"@blu@[TRIVIA]@red@ Guess a number 0-20?", "14"},
		{"@blu@[TRIVIA]@red@ Guess a number 0-20?", "16"},
		{"@blu@[TRIVIA]@red@ Guess a number 0-20?", "0"},
		{"@blu@[TRIVIA]@red@ Guess a number 0-20?", "8"},
		{"@blu@[TRIVIA]@red@ Guess a number 0-20?", "13"},
		{"@blu@[TRIVIA]@red@ Guess a number 0-5?", "5"},
		{"@blu@[TRIVIA]@red@ Guess a number 0-5?", "4"},
	{"@blu@[TRIVIA]@red@ What is the money of Japan?", "Yen"},
		{"@blu@[TRIVIA]@red@ Whose nickname was Mr. October?", "Reggie Jackson"},
		{"@blu@[TRIVIA]@red@ People and chimpanzees share approx how many DNA?", "98%"},
		{"@blu@[TRIVIA]@red@ What 2 planets dont have moons?", "Mercury and venus"},
		{"@blu@[TRIVIA]@red@ In technology, what does DC stand for?", "Direct Current"},
		{"@blu@[TRIVIA]@red@ How many Earths could go inside the big sun?", "1300000"},
		{"@blu@[TRIVIA]@red@ How much percent in Shakespeares plays are made by women?", "17"},
		{"@blu@[TRIVIA]@red@ What do you call a duel of 3 people legally?", "A truel"},
		{"@blu@[TRIVIA]@red@ What is the smaller bone in the human body?", "stapes"},
		{"@blu@[TRIVIA]@red@ What country least changes their flag design?", "Denmark"},
		{"@blu@[TRIVIA]@red@ Which state makes half of America rice?", "Arkansas"},
		{"@blu@[TRIVIA]@red@ What animal can be the loudest?", "sperm whale"},
		{"@blu@[TRIVIA]@red@ How much languages are made from right to left?", "12"},
		{"@blu@[TRIVIA]@red@ How many trees can you count on earth?", "3 trillion"},
		{"@blu@[TRIVIA]@red@ How long for food to path through body in hours?", "53 hours"},
		{"@blu@[TRIVIA]@red@ What would you call a space between 2 windows?", "Interfenestration"},
		{"@blu@[TRIVIA]@red@ Whats the only parrot that wont fly?", "Kakapo"},
	{"@blu@[TRIVIA]@red@ Guess a number 0-5?", "1"},
		{"@blu@[TRIVIA]@red@ Guess a number 0-5?", "4"},
		{"@blu@[TRIVIA]@red@ Guess a number 0-5?", "0"},
		{"@blu@[TRIVIA]@red@ Guess a number 0-5?", "2"},
	{"@blu@[TRIVIA]@red@ How long does a bond last?", "14 days"},
		{"@blu@[TRIVIA]@red@ Which barrows brother hits through prayer?", "Verac"},
		{"@blu@[TRIVIA]@red@ What town is most known for its fishing spots?", "Catherby"},
{"@blu@[TRIVIA]@red@ type the following ::anwser 63fg65ff456gh3", "63fg65ff456gh3"},
		{"@blu@[TRIVIA]@red@ type the following ::anwser jiijiiiijjjiiiiij", "jiijiiiijjjiiiiij"},
		{"@blu@[TRIVIA]@red@ type the following ::anwser h1psd0ntguidol", "h1psd0ntguidol"},
		{"@blu@[TRIVIA]@red@ type the following ::anwser %#fd2345%$ff2%", "%#fd2345%$ff2%"},
		{"@blu@[TRIVIA]@red@ type the following ::anwser 1111111222233333", "1111111222233333"},
		{"@blu@[TRIVIA]@red@ type the following ::anwser exp0033kddn111n523", "exp0033kddn111n523"},
		{"@blu@[TRIVIA]@red@ type the following ::anwser 9587476356346533", "9587476356346533"},
		{"@blu@[TRIVIA]@red@ type the following ::anwser SkyfireSkyfireFireSkySkyfire", "SkyfireSkyfireFireSkySkyfire"},
		{"@blu@[TRIVIA]@red@ type the following ::anwser 12331223456432134", "12331223456432134"},
		{"@blu@[TRIVIA]@red@ type the following ::anwser likeamikewithalikehikemikebikeflikea", "likeamikewithalikehikemikebikeflikea"},
		{"@blu@[TRIVIA]@red@ type the following ::anwser Sea Shell by the Sea Shore", "Sea Shell by the Sea Shore"}
	};
	
	public static String currentQuestion;
	private static String currentAnswer;
}
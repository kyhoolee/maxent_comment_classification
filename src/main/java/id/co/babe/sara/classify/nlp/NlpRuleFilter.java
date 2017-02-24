package id.co.babe.sara.classify.nlp;

import java.util.HashSet;

import cc.mallet.util.CharSequenceLexer;
import id.co.babe.sara.filter.model.Komen;

public class NlpRuleFilter {
	public static String[] pejoratives = {
		"No",
		"Tidak",
		"Game Over",
		"Ke Laut Aja",
		"Ogah",
		"Bodong",
		"tamat",
		"kelaut",
		"haram",
		"gak bakal",
		"ga",
		"ga bakal",
		"ga akan",
		"asal bukan",
		"bakal",
		"end",
		"Wassalam",
		"Anti",
	};
	
	public static String[] profanities = {
		"Ahoax",
		"cino",
		"lgbt",
		"homo",
		"lesbi",
		"bencong",
		"sengkuni",
		"jongos",
		"PKI",
		"BABIBURUKMAN",
		"babihok",
		"babib",
		"antababi",
		"agnostic",
		"atheis",
		"tidak percaya Tuhan",
		"banci",
		"cukong",
		"cina",
		"lonte",
		"psk",
		"FPI",
		"ZONK",
		"FADLI ZONK",
		"germo",
		"ISIS",
		"aseng",
		"babilurokhman",
		"kaffar",
		"hoker",
		"haters",
		"a hok",
		"ateis",
		"ahiok",
		"hoek",
		"pecun",

	};
	
	public static String[] obscenities = {
		"fuck",
		"shit",
		"damn",
		"onani",
		"kontol",
		"ngehe",
		"tai",
		"tae",
		"telek",
		"tot",
		"puki",
		"pukimay",
		"congok",
		"kutil",
		"itil",
		"berak",
		"boker",
		"eek",
		"ee",
		"taik",
		"kotoran",
		"ent0t",
		"nafsu",
		"biji",
		"dower",
		"ndower",
	};
	
	public static String[] offensive = {
		"bego",
		"tolol",
		"dongo",
		"dongok",
		"goblok",
		"anjing",
		"babi",
		"setan",
		"dajjal",
		"njing",
		"jing",
		"asu",
		"su",
		"kenthir",
		"kafir",
		"munafik",
		"muna",
		"muka dua",
		"monyet",
		"nyet",
		"brengsek",
		"bangsat",
		"anjing",
		"bangs",
		"mati",
		"bahlul",
		"dancok",
		"jancok",
		"begok",
		"cabul",
		"keok",
		"dungu",
		"konyol",
		"gendeng",
		"guoblok",
		"tetek",
		"konthoul",
		"oon",
		"bodoh",
		"IQ jongkok",
		"culun",
		"cemen",
		"copo",
		"cops",
		"koplak",
		"geblek",
		"biawak",
		"sapi",
		"kuda",
		"badak",
		"babu",
		"kebo",
		"kumpret",
		"kampret",
		"sianjing",
		"sianjir",
		"pus",
		"ai",
		"kucing",
		"ucing",
		"jahanam",
		"kambing",
		"Jancokk",
		"sinting",
		"gila",
		"orgil",
		"gelo",
		"kentut",
		"sableng",
		"keblinger",
		"bajingan",
		"pekok",
		"anjir",
		"jir",
		"pat kay",
		"bacot",
		"begundal",
		"bangke",
		"cok",
		"cuk",
		"celeng",
		"peak",
		"peak",
		"pe'a",
		"bebek",
		"belegug",
		"biadap",
		"biadab",
		"sengle",
		"fuddul",
		"bantai",
		"bante",
		"kodok",
		"borokokok",
		"najis",
		"ngeles",
		"kurang ajar",
		"kutu",
		"anarkis",
		"sengkuni",
		"bacot",
		"dazal",
		"conk",
		"wedus",
	};
	
	public static String[] provocatives = {
		"Tabok",
		"hajar",
		"bubarkan",
		"penjarakan",
		"mencla mencle",
		"belagu",
		"songong",
		"sotoy",
		"sok tahu",
		"sok tau",
		"sirik",
		"didor",
		"di dor",
		"dor",
		"kw",
		"abal",
		"abal abal",
		"palsu",
		"cincong",
		"perpecahan",
		"memecah",
		"pecah",
		"fitnah",
		"tipu",
		"sok",
		"pengecut",
		"penipu",
		"boneka",
		"murahan",
		"hancurkan",
		"sok suci",
		"kaya preman",
		"bui",
		"racun",
		"meracuni",
		"mracuni",
		"sontoloyo",
		"mesum",
		"porno",
		"play boy",
		"playboy",
		"Gundulmu",
		"kangker",
		"kanker",
		"PENGKHIANAT",
		"DIBINASAKAN",
		"binasa",
		"sesat",
		"tersesatkan",
		"ngajak",
		"ribut",
		"berantem",
		"lintah darat",
		"pendengki",
		"dengki",
		"neraka",
		"bokep",
		"bokeb",
		"ngok",
		"biang kerok",
		"hilangkan",
		"musnah",
		"musnahkan",
		"ancur",
		"dibodohi",
		"dibohongi",
		"tangkap",
		"antek",
		"onar",
		"beking",
		"bekingan",
		"bekingannya",
		"bekingnya",
		"bekokok",
		"benci",
		"hate",
		"jelek",
		"provokator",
		"gaduh",
		"perang",
		"kobarkan",
		"bayaran",
		"pemaki",
		"cangkulin",
		"menghancurkan",
		"arogan",
		"sombong",
		"disetir",
		"malapetaka",
		"anti NKRI",
		"otoriter",
		"dibui",
		"licik",
		"menuai badai",
		"makar",
		"penggal",
		"calon koruptor",
		"korup",
		"koruptor",
		"paksa",
		"sate",
		"pemuja",
		"basmi",
		"beri pelajaran",
		"biang",
		"bejat",
		"bosok",
		"busuk",
		"brisik",
		"brutal",
		"buburin",
		"egois",
		"mencuri",
		"caper",
		"carmuk",
		"borok",
		"kambing hitam",
		"kentut",
		"kena stroke",
		"stroke",
		"darah",
		"dilecehkan",
		"pembohong",
		"dasar",
		"baper",
		"deportasi",
		"di obok obok",
		"diobok obok",
		"dipetrus",
	};
	
	public static String[] adjective  ={
		"harus",
		"kayak",
		"kaya",
		"seperti",
		"macam",
		"ngakunya",
		"ngaku",
		"si",
		"mabok",
		"tukang",
		"keturunan",
		"bunting",
		"paling",
		"amat",
		"banget",
		"gembong",
		"kumpulan",
		"kelompok",
		"daripada",
		"dari pada",
		"mulut",
		"nungging",
	};
	
	
	public static String[] subject_bullied = {
		"SBY",
		"PKI",
		"FBI",
		"AHOK",
		"MUI",
		"FBR",
		"BEYE",
		"BEYEK",
		"HOK",
		"FADLI ZON",
		"non muslim",
		"muslim",
		"kristen",
		"kresten",
		"katholic",
		"katholik",
		"katolik",
		"budha",
		"kong hu cu",
		"hindu",
		"agama",
		"islam",
		"muslimin",
		"babe",
		"ahoker",
		"ahokers",
		"jokowers",
		"komunis",
		"Tuhan",
		"buzzer",
		"buzzernya",
		"polisi",
		"pemerintah",
		"pejabat",
		"pengamat",
		"pengacara",
		"AHY",
		"Agus",
		"Anas",
		"Antasari",
		"Anis",
		"NKRI",
		"manusia",
		"tionghoa",
		"RRC",
		"parpol",
		"PDIP",
		"GOLKAR",
		"DEMOKRAT",
		"kitab",
		"al quran",
		"alkitab",
		"fahri",
	};
	
	public static String[] subject = {
		"lu",
		"loe",
		"anda",
		"kamu",
		"dia",
		"doi",
		"mereka",
		"kalian",
		"orang",
		"bapak",
		"ibu",
		"paman",
		"kakek",
		"nenek",
		"keluarga",
		"kau",
		"kao",
		"ente",
		"elo",
		"eloe",
	};
	
	public static String[] adverb = {
		
	};
	
	/**
		SUBJECT + PEJORATIVE
		SUBJECT + PROFANITY
		SUBJECT + OBSECENITY
		SUBJECT + OFFENSIVE
		SUBJECT + PROVOCATIVE
		
		PROFANITIES
		OBSCENITIES
		OFFENSIVE
		PROFANITIES + ADJ
		OBSCENITIES + ADJ
		OFFENSIVE + ADJ
		PROVOCATIVES + ADJ
		PEJORATIVE + PROFANITIES
		PROFANITIES + ADV
		OBSCENITIES + ADV
		OFFENSIVE + ADV
	 * @param input
	 * @return
	 */
	public static String ruleInference(String input) {
		String result = Komen.PROBABLE;
		
		
		boolean subject_pejorative = checkDict(input, pejoratives) && checkDict(input, subject);
		boolean subject_profanity = checkDict(input, profanities) && checkDict(input, subject);
		boolean subject_obsecenity = checkDict(input, obscenities) && checkDict(input, subject);
		boolean subject_offensive = checkDict(input, offensive) && checkDict(input, subject);
		boolean subject_provocative = checkDict(input, provocatives) && checkDict(input, subject);
		
		
		boolean _profanities = checkDict(input, profanities) ;
		boolean _obscenities = checkDict(input, obscenities) ;
		boolean _offensive = checkDict(input, offensive) ;
		
		boolean profanities_adj = checkDict(input, profanities) && checkDict(input, adjective);
		boolean obscentities_adj = checkDict(input, obscenities) && checkDict(input, adjective);
		boolean provocatives_adj = checkDict(input, provocatives) && checkDict(input, adjective);
		
		boolean profanity_pejorative = checkDict(input, pejoratives) && checkDict(input, subject);
		boolean adv_profanity = checkDict(input, profanities) && checkDict(input, adverb);
		boolean adv_obsecenity = checkDict(input, obscenities) && checkDict(input, adverb);
		boolean adv_offensive = checkDict(input, offensive) && checkDict(input, adverb);
		
		boolean is_sara = 
				//subject_obsecenity || subject_offensive || subject_pejorative || subject_profanity || subject_provocative //|| 
				_profanities || obscentities_adj || _offensive
				//|| profanities_adj || obscentities_adj 
				//|| provocatives_adj
				//|| profanity_pejorative || adv_profanity || adv_obsecenity || adv_offensive
				;
				
		if(is_sara) {
			result = Komen.SARA;
		} else {
			result = Komen.NORMAL;
		}
		
		return result;
	}
	
	public static HashSet<String> parseWord(String input) {
		HashSet<String> result = new HashSet<>();
		CharSequenceLexer csl = new CharSequenceLexer (input, CharSequenceLexer.LEX_ALPHA );
		while (csl.hasNext()) {
			result.add((String)csl.next());
		}
		
		return result;
	}
	
	public static boolean checkDict(String input, String[] dict) {
		boolean result = false;
		
		
		HashSet<String> words = parseWord(input.toLowerCase());
		for(int i = 0 ; i < dict.length ; i ++) {
			String word = dict[i].toLowerCase();
			if(words.contains(word)) {
				result = true;
				break;
			}
		}
		
		
		return result;
	}

}

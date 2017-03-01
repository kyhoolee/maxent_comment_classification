package id.co.babe.sara.classify.nlp;

import id.co.babe.sara.filter.TextfileIO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cc.mallet.util.CharSequenceLexer;

/**
 * The ideas of probability and edit-distance from Peter Norvig's idea
 * http://norvig.com/spell-correct.html
 * @author mainspring
 *
 */
public class NorvigSpellCorrector {
	public static String letter = "";
	public static SpellDict dict;
	
	
	public static void main(String[] args) {
		NorvigSpellCorrector.init("nlp_data/indo_dict/id_full.txt");
		System.out.println(NorvigSpellCorrector.correct("elektatsbilitas", dict));//elektabilitas
		System.out.println(NorvigSpellCorrector.correct("fictim", dict));//victim
		System.out.println(NorvigSpellCorrector.correct("penikut", dict));//pengikut
		System.out.println(NorvigSpellCorrector.correct("srng", dict));//sering
	}
	
	public static void init(String dict_path) {
		dict = SpellDict.readIndoDict(dict_path);//new SpellDict();
		
	}
	
	public static List<String> readDict(String file_path) {
		return TextfileIO.readFile(file_path);
	}
	
	public static SpellDict creatDict(List<String> data) {
		SpellDict dict = new SpellDict();
		for(String line : data) {
			dict.updateDict(line);
		}
		
		return dict;
	}
	
	
	
	public static List<String> know(SpellDict dict, Collection<String> words) {
		List<String> result = new ArrayList<String>();
		
		
		for(String w : words) {
			if(dict.contains(w))
				result.add(w);
		}
		
		
		
		return result;
	}
	
	public static List<String> know(SpellDict dict, String... words) {
		List<String> result = new ArrayList<String>();
		
		
		for(String w : words) {
			if(dict.contains(w))
				result.add(w);
		}
		
		
		
		return result;
	}
	
	public static Set<String> candidates(String word, SpellDict dict) {
		Set<String> result = new HashSet<String>();
		
		result.addAll(know(dict,word));
		result.addAll(know(dict, StringEditor.edit1(word)));
		//result.addAll(know(dict, StringEditor.edit2(word)));
		
		return result;
	}
	
	public static String correct(String word, SpellDict dict) {
		return chooseMostFrequent(candidates(word, dict), dict);
	}
	
	
	
	public static String chooseMostFrequent(Set<String> words, SpellDict dict) {
		return dict.mostFreqWord(words);
	}
	

	public static String correctSentence(String sentence, SpellDict dict) {
		String result = "";
		
		for(String w : parse(sentence)) {
			if(dict.contains(w))
				result += " " + w;
			else 
				result += " " + correct(w, dict);
		}
		
		return result;
	}
	
	public static List<String> parse(String sentence) {
		List<String> result = new ArrayList<String>();
		CharSequenceLexer csl = new CharSequenceLexer (sentence.toLowerCase(), CharSequenceLexer.LEX_ALPHA );
		while (csl.hasNext()) {
			//System.out.println (csl.next());
			result.add(csl.next().toString());
		}
		
		return result;
	}
	
	

}

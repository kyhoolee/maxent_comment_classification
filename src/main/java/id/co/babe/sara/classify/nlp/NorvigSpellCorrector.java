package id.co.babe.sara.classify.nlp;

import id.co.babe.sara.filter.TextfileIO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The ideas of probability and edit-distance from Peter Norvig's idea
 * http://norvig.com/spell-correct.html
 * @author mainspring
 *
 */
public class NorvigSpellCorrector {
	public static String letter = "";
	
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
	
	public static String correction(String word) {
		String result = word;
		
		return result;
	}
	
	public static List<String> know(List<String> words, SpellDict dict) {
		List<String> result = new ArrayList<String>();
		
		
		return result;
	}
	
	public static List<String> edit1(String word) {
		List<String> result = new ArrayList<String>();
		
		
		
		return result;
	}
	
	public static List<String> edit2(String word) {
		List<String> result = new ArrayList<String>();
		
		
		return result;
	}
	
	

}

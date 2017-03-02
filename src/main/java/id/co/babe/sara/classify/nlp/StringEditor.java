package id.co.babe.sara.classify.nlp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StringEditor {
	
	public static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz";//1234567890!@#$%^&*()-_+={}[],.?;:";
	
	public static void main(String[] args) {
		
		//System.out.println(StringEditor.splits("0123456", 8));
		
		//System.out.println(StringEditor.deduplicates("hiiiiiii"));
		System.out.println();
		
	}
	
	public static Pair<String, String> splits(String origin, int pos) {
		if(pos < 0)
			pos = 0;
		if(pos > origin.length())
			pos = origin.length();
		
		Pair<String, String> result = new Pair<String, String>(
				origin.substring(0, pos), origin.substring(pos, origin.length()));
		
		return result;
	}
	
	public static List<Pair<String, String>> splits(String origin) {
		List<Pair<String, String>> result = new ArrayList<>();
		
		for(int i = 0 ; i <= origin.length() ; i ++) {
			result.add(splits(origin, i));
		}
		
		return result;
	}
	
	
	
	public static String deletes(String origin, int pos) {
		if(origin.length() <= 1)
			return "";
		
		if(pos < 0)
			pos = 0;
		if(pos >= origin.length())
			pos = origin.length() - 1;
		StringBuilder sb = new StringBuilder(origin);
		sb.deleteCharAt(pos);
		
		String result = sb.toString();
		
		return result;
	}
	
	public static List<String> deletes(String origin) {
		List<String> result = new ArrayList<>();
		
		for(int i = 0 ; i < origin.length() ; i ++) {
			result.add(deletes(origin, i));
		}
		
		return result;
	}
	
	private static String swap(String origin, int first, int second) {
		StringBuilder sb = new StringBuilder(origin);
		Character f = origin.charAt(first);
		Character s = origin.charAt(second);
		sb.setCharAt(first, s);
		sb.setCharAt(second, f);
		
		return sb.toString();
	}
	
	public static String transpose(String origin, int pos) {
		String result = origin;
		if(origin.length() <= 1)
			return result;
		
		if(pos < 0)
			pos = 0;
		if(pos >= origin.length() - 1)
			pos = origin.length() - 2;
		
		result = swap(origin, pos, pos + 1);
		
		
		return result;
	}
	
	public static List<String> transpose(String origin) {
		List<String> result = new ArrayList<>();
		
		for(int i = 0 ; i < origin.length() - 1 ; i ++) {
			result.add(transpose(origin, i));
		}
		
		return result;
	}
	
	private static String replaces(String origin, Character c, int pos) {
		StringBuilder sb = new StringBuilder(origin);
		sb.setCharAt(pos, c);
		
		return sb.toString();
	}
	
	private static List<String> replaces(String origin, String letters, int pos) {
		List<String> result = new ArrayList<>();
		
		for(int i = 0 ; i < letters.length() ; i ++) {
			Character c = letters.charAt(i);
			result.add(replaces(origin, c, pos));
		}
		
		return result;
	}
	
	
	
	public static List<String> replaces(String origin, int pos) {
		List<String> result = new ArrayList<>();
		
		if(pos < 0)
			pos = 0;
		if(pos > origin.length() - 1)
			pos = origin.length() - 1;
		
		result = replaces(origin, CHARACTERS, pos);
		
		return result;
	}
	
	public static List<String> replaces(String origin) {
		List<String> result = new ArrayList<>();
		
		for(int i = 0 ; i < origin.length() ; i ++) {
			result.addAll(replaces(origin, i));
		}
		
		return result;
	}
	
	
	
	private static String inserts(String origin, Character c, int pos) {
		StringBuilder sb = new StringBuilder(origin);
		sb.insert(pos, c);
		
		return sb.toString();
	}
	
	private static List<String> inserts(String origin, String letters, int pos) {
		List<String> result = new ArrayList<>();
		
		for(int i = 0 ; i < letters.length() ; i ++) {
			Character c = letters.charAt(i);
			result.add(inserts(origin, c, pos));
		}
		
		return result;
	}
	
	
	
	public static List<String> inserts(String origin, int pos) {
		List<String> result = new ArrayList<>();
		
		if(pos < 0)
			pos = 0;
		if(pos > origin.length())
			pos = origin.length();
		
		result = inserts(origin, CHARACTERS, pos);
		
		return result;
	}
	
	public static List<String> inserts(String origin) {
		List<String> result = new ArrayList<>();
		
		for(int i = 0 ; i < origin.length() ; i ++) {
			result.addAll(replaces(origin, i));
		}
		
		return result;
	}
	
	
	public static String deduplicates(String origin) {
		String result = "";
		
		List<Set<Character>> table = new ArrayList<>();
		Set<Character> current = new HashSet<>();
		for(int i = 0 ; i < origin.length() ; i ++) {
			Character c = origin.charAt(i);
			if(table.size() == 0) {
				current.add(c);
				table.add(current);
				result += c;
			} else {
				if(current.size() > 0) {
					if(current.contains(c)) {
						// Nothing to do
					} else {
						current = new HashSet<>();
						current.add(c);
						table.add(current);
						result += c;
					}
				} else {
					current.add(c);
					table.add(current);
				}
			}
		}
		
		
		return result;
		
	}
	
	
	public static Set<String> edit1(String word) {
		Set<String> result = new HashSet<>();
		
		result.addAll(deletes(word));
		result.addAll(transpose(word));
		result.addAll(replaces(word));
		result.addAll(inserts(word));
		result.add(deduplicates(word));
		
		return result;
	}
	
	public static Set<String> edit2(String word) {
		Set<String> result = new HashSet<>();
		
		for(String s : edit1(word)) {
			result.addAll(edit1(s));
		}
		
		return result;
	}


}

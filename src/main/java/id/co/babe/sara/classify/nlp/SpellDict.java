package id.co.babe.sara.classify.nlp;

import id.co.babe.sara.filter.TextfileIO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpellDict {
	public Map<String, Double> wordCount;
	public double total;
	
	
	public SpellDict() {
		wordCount = new HashMap<String, Double>();
		total = 0;
	}
	
	public static List<String> parseWord(String input) {
		String[] words = input.split("\\W+");
		return new ArrayList<String>(Arrays.asList(words));
	}
	
	public void updateDict(String data) {
		
		List<String> words = parseWord(data);
		total += words.size();
		for(String w : words) {
			w = w.toLowerCase();
			if(wordCount.containsKey(w)) {
				wordCount.put(w, wordCount.get(w) + 1.0);
			} else {
				wordCount.put(w, 1.0);
			}
		}
		
	}
	
	public void updateDict(List<String> data) {
		for(String line: data) {
			updateDict(line);
		}
	}
	
	public void writeFile(String filePath) {
		List<String> data = new ArrayList<String>();
		for(String word: wordCount.keySet()) {
			if(word.length() > 2) {
				String line = word + "\t" + wordCount.get(word);
				data.add(line);
			}
		}
		TextfileIO.writeFile(filePath, data);
	}
	
	
	
	public static SpellDict buildDict(String target, String... filePath) {
		SpellDict dict = new SpellDict();
		for(String path : filePath) {
			List<String> data = TextfileIO.readFile(path);
			dict.updateDict(data);
					
		}
		
		dict.writeFile(target);
		
		return dict;
	}
	
	
	public static void main(String[] args) {
		SpellDict.buildDict("nlp_data/comment_dict.tsv", "work_data/242_data/non.txt", "work_data/242_data/sara.txt");
	}
	
	

}

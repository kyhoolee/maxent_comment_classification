package id.co.babe.sara.classify.nlp;

import id.co.babe.sara.filter.TextfileIO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SpellDict {
	public Map<String, Double> wordCount;
	public double total;
	
	
	public SpellDict() {
		wordCount = new HashMap<String, Double>();
		total = 0;
	}
	
	private static List<String> parseWord(String input) {
		String[] words = input.split("\\W+");
		return new ArrayList<String>(Arrays.asList(words));
	}
	
	public boolean contains(String word) {
		return wordCount.keySet().contains(word);
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
	
	public String mostFreqWord(Set<String> words) {
		if(words.isEmpty()) {
			return "";
		} else {
			String result = "";
			double v = -1;
			
			for(String w: words) {
				double val = this.wordCount.get(w);
				if(val > v) {
					result = w;
					v = val;
				}
			}
			
			
			return result;
		}
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
	
	public static SpellDict readIndoDict(String file) {
		SpellDict dict = new SpellDict();
		
		List<String> data = TextfileIO.readFile(file);
		
		for(String pair: data) {
			List<String> p = parseWord(pair);
			if(p.size() == 2) {
				String w = p.get(0);
				double v = Double.parseDouble(p.get(1));
				dict.wordCount.put(w, v);
				dict.total += v;
			}
		}
		
		
		return dict;
	}
	
	
	public static void main(String[] args) {
		SpellDict.buildDict("nlp_data/comment_dict.tsv", "work_data/242_data/non.txt", "work_data/242_data/sara.txt");
	}
	
	

}

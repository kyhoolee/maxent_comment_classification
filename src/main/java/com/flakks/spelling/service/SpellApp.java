package com.flakks.spelling.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.flakks.spelling.Dictionary;
import com.flakks.spelling.QueryMapper;
import com.flakks.spelling.SpecialRule;
import com.flakks.spelling.SpellingLookup;
import com.flakks.spelling.SpellingSuggestor;
import com.flakks.spelling.Suggestion;
import com.flakks.spelling.TrieNode;

public class SpellApp {
	public static Map<String, Dictionary> dictionaries;
	public static Map<String, TrieNode> trieNodes;

	public static void initIndo(String indo_dict_path) {
		List<String> lines = new ArrayList<String>();

		try {
			lines.addAll(Files.readAllLines(Paths.get(indo_dict_path),StandardCharsets.UTF_8));
		} catch (IOException e) {
			e.printStackTrace();
		}

		dictionaries = createIndoDictionary(lines);
		trieNodes = createTrieNodes(dictionaries);
	}

	public static Map<String, Dictionary> createIndoDictionary(
			List<String> lines) {
		Map<String, Dictionary> dictionaries = new HashMap<String, Dictionary>();

		Dictionary dictionary;
		dictionary = new Dictionary();
		dictionaries.put("id", dictionary);

		for (String line : lines) {
			String[] columns = line.split(" ");
			dictionary.put(columns[0].trim().toLowerCase(),
					Integer.parseInt(columns[1]));
		}

		return dictionaries;
	}

	public static Map<String, Dictionary> createDictionaries(List<String> lines) {
		Map<String, Dictionary> dictionaries = new HashMap<String, Dictionary>();

		for (String line : lines) {
			String[] columns = line.split("\t");

			Dictionary dictionary = dictionaries.get(columns[0]);

			if (dictionary == null) {
				dictionary = new Dictionary();
				dictionaries.put(columns[0], dictionary);
			}

			dictionary.put(columns[1].trim().toLowerCase(),
					Integer.parseInt(columns[2]));
		}

		return dictionaries;
	}

	public static Map<String, TrieNode> createTrieNodes(
			Map<String, Dictionary> dictionaries) {
		Map<String, TrieNode> rootNodes = new HashMap<String, TrieNode>();

		for (Map.Entry<String, Dictionary> entry : dictionaries.entrySet()) {
			TrieNode rootNode = rootNodes.get(entry.getKey());

			if (rootNode == null) {
				rootNode = new TrieNode();
				rootNodes.put(entry.getKey(), rootNode);
			}

			for (Map.Entry<String, Integer> dictionaryEntry : entry.getValue()
					.entrySet()) {
				rootNode.insert(dictionaryEntry.getKey(),
						dictionaryEntry.getValue());
			}
		}

		return rootNodes;
	}
	
	public static void initAll(String[] files) {
		List<String> lines = new ArrayList<String>();

		for (int i = 0; i < files.length; i++) {
			try {
				lines.addAll(Files.readAllLines(Paths.get(files[i]),StandardCharsets.UTF_8));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		dictionaries = createDictionaries(lines);
		trieNodes = createTrieNodes(dictionaries);
	}
	
	
	public static String indoCorrect(String word) {
		long time = System.currentTimeMillis();
		String locale = "id";
		String query = word.toLowerCase();
		
		query = SpecialRule.deduplicates(query);
		SpellingLookup spellingLookup = new SpellingLookup(locale);
		QueryMapper queryMapper = new QueryMapper(spellingLookup);
			
		String mappedQuery = queryMapper.map(query);
			
		time = System.currentTimeMillis() - time;
		
		//System.out.println(mappedQuery + " " + time + " " + spellingLookup.getSumDistance());
		
		return mappedQuery;
	}
	
	public static JSONObject indoSuggest(String word) {
		long time = System.currentTimeMillis();
		String locale = "id";
		String query = word;
		
		List<Suggestion> suggestions = new SpellingSuggestor(locale).suggest(query);
		JSONArray jsonSuggestions = new JSONArray();

		for(Suggestion suggestion : suggestions)
			jsonSuggestions.put(new JSONObject().put("query", suggestion.getToken()).put("frequency", suggestion.getFrequency()));
	
		time = System.currentTimeMillis() - time;
					
		JSONObject json = new JSONObject();
		json.put("suggestions", jsonSuggestions);
		json.put("took", time);
		
		return json;
	}

	public static void main(String[] args) throws Exception {
		String indo_dict_path = "data/id_full.txt";
		initIndo(indo_dict_path);

		new HttpServer().start();
	}
}
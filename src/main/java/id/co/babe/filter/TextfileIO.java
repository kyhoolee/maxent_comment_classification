package id.co.babe.filter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TextfileIO {
	public static final String SOURCE = "/home/mainspring/tutorial/learn/text-classifier/data/komen";
	public static final String SOURCE_KOMEN = "/home/mainspring/tutorial/learn/text-classifier/data_komen/";

	public static void main(String[] args) {
		//checkRegex();
		//preProcessSpam("/home/mainspring/tutorial/learn/text-classifier/data/spam_output.txt");
		//preProcessCsv("/home/mainspring/tutorial/learn/text-classifier/data/spam_unique.txt");
		preProcessStopwords();
	}
	
	public static void checkRegex() {
		List<String> data= readFile("/home/mainspring/tutorial/learn/text-classifier/data/pure_spam_1.txt");
		for(String input : data) {
			RuleFilter.regexMatch(input);
		}
	}
	
	public static void preProcessSpam(String filePath) {
		List<String> data = readFile(filePath);
		List<String> res = new ArrayList<>();
		for(int i = 0 ; i < data.size() ; i ++) {
			String d = data.get(i);
			if(d.contains(" "))
				res.add(d);
		}
		writeFile(filePath + ".1" , res);
	}
	
	public static void preProcess() {
		List<String> data = readFile(SOURCE_KOMEN + "Comment(Bad).txt");
		data = removeDoubleQuote(data);
		writeFile(SOURCE_KOMEN + "bad_komen.txt", data);
		
		data = readFile(SOURCE_KOMEN + "Comment(Good).txt");
		data = removeDoubleQuote(data);
		writeFile(SOURCE_KOMEN + "good_komen.txt", data);
	}
	
	public static void preProcessStopwords() {
		List<String> data = readFile(SOURCE_KOMEN + "stopwords.txt");
		data = removeComma(data);
		writeFile(SOURCE_KOMEN + "stopword.txt", data);
	}
	
	public static List<String> removeComma(List<String> data) {
		List<String> result = new ArrayList<>();
		for(String line : data) {
			line = line.trim();
			line = line.replace(",", "");
			result.add(line);
		}
		
		return result;
	}
	
	public static List<String> removeDoubleQuote(List<String> data) {
		List<String> result = new ArrayList<>();
		for(String line : data) {
			line = line.trim();
			if(line != null && line.length() > 0) {
				if(line.startsWith("\""))
					line = line.substring(1, line.length());
				if(line.endsWith("\""))
					line = line.substring(0, line.length() - 1);
				
				result.add(line);
			}
			
			
		}
		
		return result;
	}
	
	public static void preProcessSample() {
		//preProcessCsv("/home/mainspring/tutorial/learn/text-classifier/data/pure_spam.txt");
		preProcessCsv("/home/mainspring/tutorial/learn/text-classifier/data/pure_spam_1.txt");
		//preProcessCsv("/home/mainspring/tutorial/learn/text-classifier/data/pure_comments.txt");
	}
	
	public static void preProcessCsv(String filePath) {
		List<String> data = readFile(filePath);
		
		for(int i = 0 ; i < data.size() ; i ++) {
			String d = data.get(i);
			if(d != null && d.length() > 2) {
				d = d.substring(1, d.length() - 1);
				data.set(i, d);
			}
			
		}
		writeFile(filePath + ".1" , data);
	}
	
	public static List<String> readFile(InputStream is) {
		List<String> result = new ArrayList<>();
		
		BufferedReader br = null;
		FileReader fr = null;

		try {
			br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

			String line;
			while ((line = br.readLine()) != null) {
				result.add(line);
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
				if (fr != null)
					fr.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		return result;
	}
	
	public static List<String> readFile(String filePath) {
		List<String> result = new ArrayList<>();
		
		BufferedReader br = null;
		FileReader fr = null;

		try {
			fr = new FileReader(filePath);
			br = new BufferedReader(fr);
			br = new BufferedReader(new FileReader(filePath));

			String line;
			while ((line = br.readLine()) != null) {
				if(line != null && !line.isEmpty())
					result.add(line);
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
				if (fr != null)
					fr.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		return result;
	}
	
	public static void writeFile(String filePath, List<String> data) {
		BufferedWriter bw = null;
		FileWriter fw = null;

		try {


			fw = new FileWriter(filePath);
			bw = new BufferedWriter(fw);
			
			for(int i = 0 ; i < data.size() ; i ++) {
				bw.write(data.get(i));
				bw.newLine();
			}

			System.out.println("Done");

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}

	}

}

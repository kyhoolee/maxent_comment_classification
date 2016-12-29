package id.co.babe.filter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TextfileIO {
	public static final String SOURCE = "/home/mainspring/tutorial/learn/text-classifier/data/komen";
	
	public static void main(String[] args) {
		//checkRegex();
		//preProcessSpam("/home/mainspring/tutorial/learn/text-classifier/data/spam_output.txt");
		preProcessCsv("/home/mainspring/tutorial/learn/text-classifier/data/spam_unique.txt");
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
		for(int i = 1; i < 11; i ++) {
			List<String> data = readFile(SOURCE + "/komen" + i + ".csv");
			writeFile(SOURCE + "/l_" + "komen" + i + ".txt", data);
		}
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

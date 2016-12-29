package id.co.babe.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

	public static void printList(List<?> data) {
		for(int i = 0 ; i < data.size() ; i ++) {
			System.out.println(data.get(i));
		}
	}
	
	public static String stringList(List<?> data) {
		String result = " :: ";
		for(int i = 0 ; i < data.size() ; i ++) {
			result += " - " + data.get(i);
		}
		return result;
	}
	
	public static String toString(double[] x) {
		String result = "[ ";
		for(int i = 0 ; i < x.length ; i ++) {
			result += " " + x[i] + " ";
		}
		result += " ] ";
		return result;
	}
	
	public static boolean checkNan(double[] x) {
		for(int i = 0 ; i < x.length ; i ++) {
			if (Double.isNaN(x[i])) {
			    return true;
			}
		}
		
		return false;
	}
	
	public static int countSub(String input, String sub) {
		int lastIndex = 0;
		int count = 0;

		while(lastIndex != -1){

		    lastIndex = input.indexOf(sub,lastIndex);

		    if(lastIndex != -1){
		        count ++;
		        lastIndex += sub.length();
		    }
		}
		return count;
	}
	
	public static String filter(String input) {
		input = input.replace("\\n", " ");
		input = input.replace(",", " ");
		input = input.replace("\",\"", " ");
		return input;
	}
	
	public static String phoneNumber(String input) {
		String result = "";
		input = "serius hubungi hp/sms/wa: 0821 8788 1779 pin bb: 5f4665db";
		Pattern p = Pattern.compile("-?\\d+");
		Matcher m = p.matcher(input);
		while (m.find()) {
		  System.out.println(m.group());
		}
		
		return result;
	}
	
	public static List<String> bbPin(String input) {
		input = input.toLowerCase();
		//if(input.contains("pin") || input.contains("bb")) {
			
			List<String> result = new ArrayList<String>();
			Pattern pattern = Pattern.compile("[a-zA-Z0-9]{8}");
			Matcher matcher = pattern.matcher(input);
			while (matcher.find())
			{
				String str = matcher.group();
				//System.out.println(str);
				if(isLetterNumber(str)) {
					result.add(str);
					//System.out.println(str);
				}
			}
			
			return result;
		//}
		//return new ArrayList<String>();
	}
	
	public static boolean isLetterNumber(String input) {
	    int countLetter = 0;
	    Pattern pattern = Pattern.compile("[a-zA-Z]{1}");
		Matcher matcher = pattern.matcher(input);
		while (matcher.find()) {
			countLetter ++;
		}
		
		int countNumber = 0;
	    pattern = Pattern.compile("[0-9]{1}");
		matcher = pattern.matcher(input);
		while (matcher.find()) {
			countNumber ++;
		}
		
		
	    return (countLetter > 1 && countNumber > 1);
	}
	
	public static void main(String[] args) {
		//bbPin("di mana belinya ? apa i u add 7 cdb7a35 adult only");
		bbPin("bua yg cowo "
				+ "jangan ml sama pacar kalo belom nikah api bingung kalo sange mau nyelupin siapa? "
				+ "kalo sangek udah gelisah mau main sama psk pas i kena penyaki kelamin mendingan "
				+ "main sama me##k mainan aja rasanya legi bange kayak daging me##k asli bisa dipake "
				+ "kapan aja yang mina add aja 7cdb7a35");
	}
} 

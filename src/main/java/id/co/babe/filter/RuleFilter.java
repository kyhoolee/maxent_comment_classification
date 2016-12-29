package id.co.babe.filter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import id.co.babe.filter.model.Komen;
import id.co.babe.util.Util;

public class RuleFilter {
	

	public static final Set<String> blackWords = new HashSet<String>(Arrays.asList(
			"service", "sofa", "servis", 
			
			"obat", "perangsang",  "kuat",
			"sex", "sedia", "penis", "seks", "adult", "onani", "syahwat", "bokep", "khusus",
			"vagina", "ejakulasi", "hubungan",
			"besar", "panjang", "keras",
			//"mencintai", "hubungan", "hasil", "germo",
			
			"0813", "0851", "0856", "0857", "0858","0821","0812", "0878", "0853", "0852",
			"0818", "0877","081", "0822", "0811", "0877",
			
			"fb.com", "bbm", "bb", "pin:", "idline"
			
			));
	
	
	
	public static String ruleSpam(String input) {
		
		//0.07453416149068323 -- 76.0 -- 1 -- 0.15789473684210525 -- 2.0 --
		// 0.016666666666666666 -- 14.0 -- 0 -- 0.0 -- 2.0 --  ::  -- Abis enak sih ni no hp ak klo mo nlf umur ak 25 081271181335
		// 0.07453416149068323 -- 76.0 -- 1 -- 0.15789473684210525 -- 2.0 --  ::  -- BFI Finance siap memban","u anda dalam kebu","uhan modal usaha, a","au keperluan lain nya , Tanpa BI Checking, Proses SE-Indonesia  jaminan BPKB Mobil/","ruk/sp.mo","or,\nHub :\n0852 7009 1231.                                P i n BB  :  5 9 E 9 7 2 8 
		// 0.007936507936507936 -- 16.0 -- 0 -- 0.0625 -- 2.0 --  ::  -- Bagi ","eman ","eman yg bu","uh imformasi ","","g bisnis syariah bisa lia"," www.k-link.co.id a","au hubungi/wa : 081252194883
		
		if(  //(uppercaseRule(input) > 0.7 && lengthRule(input) > 5 && specialWordRule(input) > 1) 
			(uppercaseRule(input) > 0.1 && lengthRule(input) > 5 && specialWordRule(input) > 3)  
				|| (singlecharacterRule(input) > 0.3 && lengthRule(input) > 10)
				|| (uppercaseRule(input) > 0 && lengthRule(input) > 5 && specialWordRule(input) >= 1 && blackWord(input) > 1)  
				|| (uppercaseRule(input) > 0.5 && blackWord(input) >= 1)
				|| (specialWordRule(input) > 10 && blackWord(input) >= 1)
				|| (blackWord(input) >= 3 && lengthRule(input) < 10)
				|| (blackWord(input) >= 3 && lengthRule(input) > 10 && uppercaseRule(input) > 0.1) 
				) {
			return Komen.SPAM;
		}
		
		return Komen.NORMAL;
	}
	

	public static String ruleNormal(String input) {
		//0.017857142857142856 -- 42.0 -- 12 -- 0.0 -- 0.0 -- 
		//0.027522935779816515 -- 19.0 -- 0 -- 0.10526315789473684 -- 0.0 
		if( (uppercaseRule(input) < 0.4 && specialWordRule(input) < 20 && blackWord(input) < 1)
				|| (blackWord(input) <= 1 && specialWordRule(input) <= 5 && singlecharacterRule(input) <= 1)
				) {
			return Komen.NORMAL;
		}
		
		return Komen.SPAM;
	}
	
	public static String printRule(String input) {
		String sep = " -- ";
		String result = (
				uppercaseRule(input) + sep
				+ lengthRule(input) + sep
				+ specialWordRule(input) + sep 
				+ singlecharacterRule(input) + sep
				+ blackWord(input) + sep 
				+ Util.stringList(pinWord(input)) + sep
				+ input
				);

		// System.out.println(result);
		return result;
	}
	
	public static List<String> pinWord(String input) {
		return Util.bbPin(input);
	}
	
	public static double blackWord(String input) {
		double result = 0; 
		
		for(String black : blackWords) {
			result += Util.countSub(input.toLowerCase(), black);
		}
		
		
		result += Util.bbPin(input).size();
		
		return result;
	}
	
	public static double uppercaseRule(String input) {
		
		if(input  == null || input.length() == 0)
			return 0;
		
		int upperCase = 0;
		for (int k = 0; k < input.length(); k++) {
		    if (Character.isUpperCase(input.charAt(k))) upperCase++;
		}
		
		double check = (double) upperCase * 1.0 / input.length();
		return check;
	}
	
	public static double singlecharacterRule(String input) {
		if(input  == null || input.length() == 0)
			return 0;
		
		String[] words = input.toLowerCase().split(" ");
		if(words.length == 0)
			return 0;
		int count = 0;
				
		for(int i = 0 ; i < words.length ; i ++) {
			if(words[i].length() == 1) 
				count ++;
		}
		
		double check = (double) count * 1.0 / words.length;
		
		return check;
	}
	
	
	public static double lengthRule(String input) {
		String[] split = input.split(" ");
		
		return split.length;
	}
	
	public static int specialWordRule(String input) {
		if(input  == null || input.length() == 0)
			return 0;
		
		String[] words = input.toLowerCase().split(" ");
		int count = 0;
				
		boolean nc = false;
		boolean ns = false;
		boolean cs = false;
		for(int i = 0 ; i < words.length ; i ++) {
			boolean n = words[i].matches(".*\\d+.*");
			boolean c = words[i].matches(".*[a-zA-Z]+.*");
			
//			Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
//			Matcher m = p.matcher("I am a string");
//			boolean s = m.find();
			boolean s = words[i].matches(".*[!@#$%^&*€∆]+.*");
			
			if((n&&c) || (n&&s) || (c&&s)) // 
				count ++;
//			if(n&&c&&s)
//				count += 10;
			
			if(n&&c) nc = true;
			if(n&&s) ns = true;
			if(c&&s) cs = true;
		}
		
		if((nc&&cs) || (ns&&cs) || (nc&&ns)) // 
			count += 10;
		
		
		return count;
	}
	
	public static void regexMatch(String input) {
		if(input.matches("/\\s+[Oo]\\s*[Bb]\\s*[Aa]\\*[Tt]\\s+/"))
			System.out.println(input);
		
		if(input.matches("/\\s+0[0-9]{2,}\\s*[0-9]{3,}/"))
			System.out.println(input);
		
		
		if(input.matches("/\\s+([0-9A-F]{2}\\s*){3,}/"))
			System.out.println(input);
		
		if(input.matches("/[0-9][ABCDEFabcdef]\\s*[ABCDEFabcdef]{2,}/"))
			System.out.println(input);
	}

}

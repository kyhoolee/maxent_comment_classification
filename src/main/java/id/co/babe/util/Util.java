package id.co.babe.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

	public static void printList(List<?> data) {
		for (int i = 0; i < data.size(); i++) {
			System.out.println(data.get(i));
		}
	}

	public static String stringList(List<?> data) {
		String result = " :: ";
		for (int i = 0; i < data.size(); i++) {
			result += " - " + data.get(i);
		}
		return result;
	}

	public static String toString(double[] x) {
		String result = "[ ";
		for (int i = 0; i < x.length; i++) {
			result += " " + x[i] + " ";
		}
		result += " ] ";
		return result;
	}

	public static boolean checkNan(double[] x) {
		for (int i = 0; i < x.length; i++) {
			if (Double.isNaN(x[i])) {
				return true;
			}
		}

		return false;
	}

	public static int countSub(String input, String sub) {
		int lastIndex = 0;
		int count = 0;

		while (lastIndex != -1) {

			lastIndex = input.indexOf(sub, lastIndex);

			if (lastIndex != -1) {
				count++;
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

	public static List<String> phoneNumber(String input) {
		List<String> result = new ArrayList<>();
		Pattern p = Pattern.compile("(0|62)[0-9]{2,}(\\s*)[0-9]{3,}");
		Pattern np = Pattern.compile("10{3,}");
		Pattern np1 = Pattern.compile("[0\\s*]{5,}");
		Matcher m = p.matcher(input);
		while (m.find()) {
			String w = m.group();
			Matcher nm = np.matcher(w);
			Matcher nm1 = np1.matcher(w);
			if (!nm.find() && !nm1.find()) {
				//System.out.println(w);
				result.add(w);
			}
		}
		p = Pattern.compile("0[0-9]{8,}");
		m = p.matcher(input);
		while (m.find()) {
			String w = m.group();
			Matcher nm = np.matcher(w);
			Matcher nm1 = np1.matcher(w);
			if (!nm.find() && !nm1.find()) {
				//System.out.println(w);
				result.add(w);
			}
		}
		p = Pattern.compile("0\\s*([0-9]\\s*){8,}");
		m = p.matcher(input);
		while (m.find()) {
			String w = m.group();
			Matcher nm = np.matcher(w);
			Matcher nm1 = np1.matcher(w);
			if (!nm.find() && !nm1.find()) {
				//System.out.println(w);
				result.add(w);
			}
		}

		return result;
	}

	public static List<String> bbPin(String input) {
		input = input.toLowerCase();
		// if(input.contains("pin") || input.contains("bb")) {

		List<String> result = new ArrayList<String>();
		Pattern pattern = Pattern.compile("[0-9](\\s*)[A-Fa-f](\\s*)[A-Fa-f0-9]{2,}");// "[a-zA-Z0-9]{8}");
		Pattern pattern2 = Pattern.compile("[0-9](\\s*)([A-Fa-f0-9](\\s*)){4,}");// "[a-zA-Z0-9]{8}");
		//D18A5778
		Matcher matcher = pattern.matcher(input);
		Matcher matcher2 = pattern2.matcher(input);
		while (matcher.find()) {
			String str = matcher.group();
			//System.out.println(str + " " + isLetterNumber(str) + " " + isLength8(str));
			if(isLetterNumber(str) && isLength8(str)) {
				result.add(str);
				//System.out.println(str);
			}
		}
		while (matcher2.find()) {
			String str = matcher2.group();
			//System.out.println(str + " " + isLetterNumber(str) + " " + isLength8(str));
			if(isLetterNumber(str) && isLength8(str)) {
				result.add(str);
				//System.out.println(str);
			}
		}

		return result;
		// }
		// return new ArrayList<String>();
	}
	
	public static boolean isLength8(String input) {
		String reduced = input.replaceAll("\\s+","");
		return (reduced.length() == 8 || reduced.length() == 7);
	}

	public static boolean isLetterNumber(String input) {
		int countLetter = 0;
		Pattern pattern = Pattern.compile("[a-zA-Z]{1}");
		Matcher matcher = pattern.matcher(input);
		while (matcher.find()) {
			
			countLetter++;
		}

		int countNumber = 0;
		pattern = Pattern.compile("[0-9]{1}");
		matcher = pattern.matcher(input);
		while (matcher.find()) {
			countNumber++;
		}

		return (countLetter >= 1 && countNumber >= 1);
	}

	public static void main(String[] args) {
		// bbPin("di mana belinya ? apa i u add 7 cdb7a35 adult only");
//		bbPin("bua yg cowo " + "jangan ml sama pacar kalo belom nikah api bingung kalo sange mau nyelupin siapa? "
//				+ "kalo sangek udah gelisah mau main sama psk pas i kena penyaki kelamin mendingan "
//				+ "main sama me##k mainan aja rasanya legi bange kayak daging me##k asli bisa dipake "
//				+ "kapan aja yang mina add aja 7 c d b 7 a 3 5");

		// phoneNumber("serius hubungi hp/sms/wa: 0 8 2 1 8 7 8 8 1779 pin bb:
		// 5f4665db");
		
		//System.out.println(isLetterNumber("5e 69 44 67"));
		//bbPin("V.1.A.G.R.A USA O.B.A.T K.U.A.T PATEN\n\n          ASLI ORIGINAL IMPORT\n\nO.B.A.T K.U.A.T NO 1\n\nMGATASI DIABET- KENCANG.T.LAMA\nTlp:0.81.2.28.4.49.4.99/pin:D18A5778 ※ VI@GRA USA,KLG USA,CIALIS\n※ PEMUTIH WAJAH/BADAN \n※ ALAT BANTU P/W DLL..");
		bbPin("PEMBESAR PAYUDARA\nPIN.D2E22009");
	}
}

package id.co.babe.sara.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import id.co.babe.sara.classifier.bayes.BayesClassifier;
import id.co.babe.sara.filter.model.Komen;
import id.co.babe.sara.filter.model.KomenDataset;
import id.co.babe.sara.util.Util;

public class KomenFilterExample {

	public static void main(String[] args) {
		// ruleFilter();
		// logisticFilter();
		//trainModel();
		estimate();
	}

	public static final String ROOT = "/home/mainspring/tutorial/learn/text-classifier/data/";

	private static KomenDataset buildData() {
		KomenDataset data = new KomenDataset();

		//data.updateData(DataReader.readSpamKomens(ROOT + "neg_words.txt"), 1);
		data.updateData(DataReader.readSpamKomens(ROOT + "spam_output.txt.1"), 0.8);
		data.updateData(DataReader.readNormalKomens(ROOT + "pure_comments.txt.1"), 0.8);
		data.updateData(DataReader.readSpamKomens(ROOT + "pure_spam.txt.1"), 0.8);
		data.updateData(DataReader.readSpamKomens(ROOT + "pure_spam_1.txt.1"), 0.8);
		data.updateData(DataReader.readSpamKomens(ROOT + "spam_unique.txt.1"), 0.8);

		System.out.println("Train data: ");
		System.out.println(data.train_pos + " -- " + data.train_neg);

		System.out.println("\n\nTest data: ");
		System.out.println(data.test_pos + " -- " + data.test_neg);
		System.out.println("\n\n-------------------");

		return data;
	}



	
	public static String ruleInference(String content) {
		String res = Komen.NORMAL;

		content = Util.filter(content);
		if (res == Komen.NORMAL) {
			res = RuleFilter.ruleSpam(content);
		} 
		
		if(res == Komen.SARA) {
			res = RuleFilter.ruleNormal(content);
		}

		return res;
	}

	public static void estimate() {
		KomenDataset data = buildData();


		// Test the model

		int false_pos = 0;
		int false_neg = 0;
		int true_pos = 0;
		int true_neg = 0;

		List<String> falseNegList = new ArrayList<>();
		List<String> falsePosList = new ArrayList<>();

		for (int i = 0; i < data.train.size(); i++) {
			Komen k = data.train.get(i);
			
			String res = ruleInference(k.content);
			
			if (k.label == Komen.SARA && res == Komen.NORMAL) {
				falsePosList.add(RuleFilter.printRule(k.content));//
			}

			if (k.label == Komen.NORMAL && res == Komen.SARA) {
				falseNegList.add(RuleFilter.printRule(k.content));
			}

			if (k.label == Komen.NORMAL) {
				if (res == Komen.NORMAL) {
					true_neg++;
				} else {
					false_neg++;
				}
			} else {
				if (res == Komen.SARA) {
					true_pos++;
				} else {
					false_pos++;
				}
			}

		}

		System.out.println("\n\n");
		System.out.println("False_pos: " + false_pos + " -- Total_pos: " + (false_pos + true_pos));
		System.out.println("False_neg: " + false_neg + " -- Total_neg: " + (false_neg + true_neg));

		TextfileIO.writeFile(ROOT + "false_negative.txt", falseNegList);
		TextfileIO.writeFile(ROOT + "false_positive.txt", falsePosList);

		double precision = true_pos * 1.0 / (true_pos + false_pos);
		double recall = true_pos * 1.0 / (false_neg + true_pos);
		System.out.println("Precision: " + precision + " -- Recall: " + recall);

		double f_score = 2 * precision * recall / (precision + recall);
		System.out.println("F-Score: " + f_score);
	}



}

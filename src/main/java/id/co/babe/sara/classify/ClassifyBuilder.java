package id.co.babe.sara.classify;

import id.co.babe.sara.filter.model.Komen;
import id.co.babe.sara.filter.model.KomenDataset;

import java.util.ArrayList;
import java.util.List;

import com.flakks.spelling.service.SpellApp;

import cc.mallet.classify.Classification;
import cc.mallet.classify.Classifier;
import cc.mallet.types.Label;

public class ClassifyBuilder {
	
	public static void main(String[] args) {
		String sara_file = "work_data/242_data/review_sara.txt";
		String normal_file = "work_data/242_data/review_non.txt";
		String classifier_file = "model_data/maxent_classifier.data";
		//System.out.println("init spell");
		//SpellApp.initIndo("nlp_data/indo_dict/id_full.txt");
		System.out.println("init data");
		KomenDataset data = SaraCommentAPI.buildData(sara_file, normal_file, 0.8, 0.8);
		System.out.println("build classifier");
		Classifier classifier = SaraCommentAPI.buildClassifier(data, classifier_file);
		validateClassifier(data, classifier);
		
	}

	
	public static void validateClassifier(KomenDataset data, Classifier c) {


		// Test the model

		int false_pos = 0;
		int false_neg = 0;
		int true_pos = 0;
		int true_neg = 0;

		int high_false_pos = 0;
		int high_false_neg = 0;
		int high_true_pos = 0;
		int high_true_neg = 0;
		
		List<String> highTrust = new ArrayList<>();
		List<String> highFalseNegList = new ArrayList<>();
		List<String> highFalsePosList = new ArrayList<>();
		
		List<String> falseNegList = new ArrayList<>();
		List<String> falsePosList = new ArrayList<>();

		for (int i = 0; i < data.test.size(); i++) {
			Komen k = data.test.get(i);
			String test_data = k.content;//NorvigSpellCorrector.correctSentence(k.content, NorvigSpellCorrector.dict);
			Classification cf = c.classify(test_data);
			Label label = cf.getLabeling().getBestLabel();
			String res = cf.getLabeling().getBestLabel().toString();
			double score = cf.getLabeling().value(label);
			
			if((res.equals(Komen.NORMAL) && score > 0.8)
					|| (res.equals(Komen.SARA) /*&& score > 0.7*/)
					) {
				highTrust.add(k.content);
				if (k.label.equals(Komen.SARA) && res.equals(Komen.NORMAL)) {
					highFalseNegList.add(k.content);
				}
				if (k.label.equals(Komen.NORMAL) && res.equals(Komen.SARA)) {
					highFalsePosList.add(k.content);
				}
				if (k.label.equals(Komen.NORMAL)) {
					if (res.equals(Komen.NORMAL)) {
						high_true_neg++;
					} else {
						high_false_pos++;
					}
				} else { // label == SARA
					if (res.equals(Komen.SARA)) {
						high_true_pos++;
					} else {
						high_false_neg++; // res == Normal
					}
				}
			} else {
				//System.out.println(k.content);
				
			}
			
			

			if (k.label.equals(Komen.SARA) && res.equals(Komen.NORMAL)) {
				falseNegList.add(k.content);
			}
			if (k.label.equals(Komen.NORMAL) && res.equals(Komen.SARA)) {
				falsePosList.add(k.content);
			}
			if (k.label.equals(Komen.NORMAL)) {
				if (res.equals(Komen.NORMAL)) {
					true_neg++;
				} else {
					false_pos++;
				}
			} else { // k.label == Komen.SARA
				if (res.equals(Komen.SARA)) {
					true_pos++;
				} else {
					false_neg++; // k.label == Komen.SARA && res = Komen.Normal
				}
			}

		}

		SaraCommentAPI.showResult(true_pos, false_neg, false_pos, true_neg);
		SaraCommentAPI.showResult(high_true_pos, high_false_neg, high_false_pos, high_true_neg);
		
		System.out.println("\n\n");
		System.out.println("sara : " + high_true_pos + " normal: " + high_true_neg 
				+ " probable: " + (data.test.size() - high_true_pos - high_true_neg)
				+ " probable: " + (data.test.size() - highTrust.size()));
		
		
		
	}
}

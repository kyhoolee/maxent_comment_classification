package id.co.babe.sara.classify;

import id.co.babe.sara.filter.model.Komen;
import id.co.babe.sara.filter.model.KomenDataset;

import java.util.ArrayList;
import java.util.List;

import cc.mallet.classify.Classifier;

public class ClassifyEstimator {
	
	public static void main(String[] args) {
		loadAndEstimate("work_data/test_sara_komen.txt", "work_data/test_non_sara_komen.txt", 0.2,0.0, "model_data/maxent_classifier.data");
	}
	
	public static void loadAndEstimate(String sara_file, String normal_file,
			double train_sara_percent, double train_normal_percent,String classifier_file) {
		
		KomenDataset data = SaraCommentAPI.buildData(sara_file, normal_file, train_sara_percent, train_normal_percent);
		try {
			//maxent_classifier.data
			Classifier c = SaraCommentAPI.loadClassifier(classifier_file);
			estimateClassifier(c, data);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	

	public static void estimateClassifier(Classifier c, KomenDataset data) {
		// Test the model

		int false_pos = 0;
		int false_neg = 0;
		int true_pos = 0;
		int true_neg = 0;
		int probable = 0;
		
		int h_false_pos = 0;
		int h_false_neg = 0;
		int h_true_pos = 0;
		int h_true_neg = 0;
		

		List<String> falseNegList = new ArrayList<>();
		List<String> falsePosList = new ArrayList<>();

		for (int i = 0; i < data.test.size(); i++) {
			Komen k = data.test.get(i);
			String res = SaraCommentAPI.classify(c, k.content);
			
			if (k.label.equals(Komen.SARA) && res.equals(Komen.NORMAL)) {
				falsePosList.add(k.content);
			}

			if (k.label.equals(Komen.NORMAL) && res.equals(Komen.SARA)) {
				falseNegList.add(k.content);
			}

			if (k.label.equals(Komen.NORMAL)) {
				if (res.equals(Komen.NORMAL)) {
					true_neg++;
				} else {
					false_pos++;
				}
			} else {
				if (res.equals(Komen.SARA)) {
					true_pos++;
				} else {
					false_neg++;
				}
			}
			
			
			res = SaraCommentAPI.classifyConfident(c, k.content, 0.9, 0.6);
			if(res.equals(Komen.NORMAL) || res.equals(Komen.SARA)) {
				if (k.label.equals(Komen.NORMAL)) {
					if (res.equals(Komen.NORMAL)) {
						h_true_neg++;
					} else {
						h_false_pos++;
					}
				} else {
					if (res.equals(Komen.SARA)) {
						h_true_pos++;
					} else {
						h_false_neg++;
					}
				}
			}
			if(res.equals(Komen.PROBABLE))
				probable ++;

		}
		
		
		
		SaraCommentAPI.showResult(true_pos, false_neg, false_pos, true_neg);
		SaraCommentAPI.showResult(h_true_pos, h_false_neg, h_false_pos, h_true_neg);
		System.out.println("Probable: " + probable);
		

	}
	


}

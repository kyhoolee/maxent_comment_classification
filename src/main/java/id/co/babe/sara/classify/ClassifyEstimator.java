package id.co.babe.sara.classify;

//import id.co.babe.sara.classify.nlp.NorvigSpellCorrector;
import id.co.babe.sara.filter.TextfileIO;
import id.co.babe.sara.filter.model.Komen;
import id.co.babe.sara.filter.model.KomenDataset;

import java.util.ArrayList;
import java.util.List;

import com.flakks.spelling.service.SpellApp;

import cc.mallet.classify.Classifier;

public class ClassifyEstimator {
	
	public static void main(String[] args) {
		//NorvigSpellCorrector.init("nlp_data/indo_dict/id_full.txt");
		//SpellApp.initIndo("nlp_data/indo_dict/id_full.txt");
		loadAndEstimate("work_data/test_sara.txt", "work_data/test_non_sara_komen.txt", 0.2,0.0, "model_data/maxent_classifier.data");
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
			String test_data = k.content;//NorvigSpellCorrector.correctSentence(k.content, NorvigSpellCorrector.dict);
			String res = SaraCommentAPI.classify(c, test_data);
			
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
		
		TextfileIO.writeFile("model_result/false_neg.txt", falseNegList);
		TextfileIO.writeFile("model_result/false_pos.txt", falsePosList);
		
		SaraCommentAPI.showResult(true_pos, false_neg, false_pos, true_neg);
		SaraCommentAPI.showResult(h_true_pos, h_false_neg, h_false_pos, h_true_neg);
		System.out.println("Probable: " + probable);
		

	}
	


}

package id.co.babe.sara.classify;

import id.co.babe.sara.filter.model.Komen;
import id.co.babe.sara.filter.model.KomenDataset;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import utils.DataFileReader;
import ca.uwo.csd.ai.nlp.kernel.KernelManager;
import ca.uwo.csd.ai.nlp.kernel.LinearKernel;
import ca.uwo.csd.ai.nlp.libsvm.svm_model;
import ca.uwo.csd.ai.nlp.libsvm.svm_parameter;
import ca.uwo.csd.ai.nlp.libsvm.ex.Instance;
import ca.uwo.csd.ai.nlp.libsvm.ex.SVMPredictor;
import ca.uwo.csd.ai.nlp.libsvm.ex.SVMTrainer;
import ca.uwo.csd.ai.nlp.mallet.libsvm.SVMClassifierTrainer;
import cc.mallet.classify.Classification;
import cc.mallet.classify.Classifier;
import cc.mallet.classify.ClassifierTrainer;
import cc.mallet.classify.MaxEnt;
import cc.mallet.classify.MaxEntTrainer;
import cc.mallet.classify.NaiveBayesTrainer;
import cc.mallet.types.InstanceList;
import cc.mallet.types.Label;

public class ModelComparator {
	public static void main(String[] args) {
		// estimateBayes();
		//estimateMaxent();
		estimateSVM();
	}
	

	public static void estimateMaxent() {
		KomenDataset data = KomenClassification.buildData("work_data/tr_sara_komen.txt", "work_data/tr_non_sara_komen.txt", 0.9);
		InstanceList instances = KomenClassification.buildInstance(data);
		ClassifierTrainer<MaxEnt> trainer = new MaxEntTrainer();
		Classifier c = trainer.train(instances);


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
			Classification cf = c.classify(k.content);
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
		
		
		SaraCommentAPI.saveClassifier(c, "maxent_classifier.data");
		
	}

	public static void estimateBayes() {
		KomenDataset data = KomenClassification.buildData("work_data/tr_sara_komen.txt", "work_data/tr_non_sara_komen.txt", 0.9);
		InstanceList instances = KomenClassification.buildInstance(data);
		Classifier c = new NaiveBayesTrainer().train(instances);

		// Test the model

		int false_pos = 0;
		int false_neg = 0;
		int true_pos = 0;
		int true_neg = 0;

		List<String> falseNegList = new ArrayList<>();
		List<String> falsePosList = new ArrayList<>();

		for (int i = 0; i < data.test.size(); i++) {
			Komen k = data.test.get(i);
			String res = c.classify(k.content).getLabeling().getBestLabel().toString();

			if (k.label == Komen.SARA && res == Komen.NORMAL) {
				falsePosList.add(k.content);
			}
			if (k.label == Komen.NORMAL && res == Komen.SARA) {
				falseNegList.add(k.content);
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
		SaraCommentAPI.showResult(true_pos, false_neg, false_pos, true_neg);
	}
	


	public static void estimateSVM() {
		KomenDataset data = KomenClassification.buildData("work_data/tr_sara_komen.txt", "work_data/tr_non_sara_komen.txt", 0.9);
		InstanceList instances = KomenClassification.buildInstance(data);
		ClassifierTrainer trainer = new SVMClassifierTrainer(new LinearKernel());
        Classifier c = trainer.train(instances);
        //System.out.println("Accuracy: " + classifier.getAccuracy(testingInstanceList));

		// Test the model

		int false_pos = 0;
		int false_neg = 0;
		int true_pos = 0;
		int true_neg = 0;

		List<String> falseNegList = new ArrayList<>();
		List<String> falsePosList = new ArrayList<>();

		for (int i = 0; i < data.test.size(); i++) {
			Komen k = data.test.get(i);
			String res = c.classify(k.content).getLabeling().getBestLabel().toString();

			if (k.label == Komen.SARA && res == Komen.NORMAL) {
				falsePosList.add(k.content);
			}
			if (k.label == Komen.NORMAL && res == Komen.SARA) {
				falseNegList.add(k.content);
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
		SaraCommentAPI.showResult(true_pos, false_neg, false_pos, true_neg);
	}
	


}

package id.co.babe.sara.classify;

import id.co.babe.sara.filter.DataReader;
import id.co.babe.sara.filter.model.Komen;
import id.co.babe.sara.filter.model.KomenDataset;
import id.co.babe.sara.util.ConfigParams;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import cc.mallet.classify.Classification;
import cc.mallet.classify.Classifier;
import cc.mallet.types.InstanceList;
import cc.mallet.types.Label;
import cc.mallet.types.Labeling;

public class SaraCommentAPI {
	public static double NORMAL_THRES = 0.9;
	public static double SARA_THRES = 0.6;
	
	public static void load() {
		ConfigParams.load();
		NORMAL_THRES = ConfigParams.normal_thres;
		SARA_THRES = ConfigParams.sara_thres;
		
	}


	public static KomenDataset buildData(String sara_file, String normal_file,
			double train_percent) {
		KomenDataset data = new KomenDataset();

		data.updateData(DataReader.readSaraKomens(sara_file), train_percent);
		data.updateData(DataReader.readNormalKomens(normal_file), train_percent);

		System.out.println("Train data: "+ data.train_pos + " -- " + data.train_neg);
		System.out.println("Test data: " + data.test_pos + " -- " + data.test_neg);
		System.out.println("\n\n-------------------");

		return data;
	}
	
	
	public static KomenDataset buildData(String sara_file, String normal_file,
			double train_sara_percent, double train_normal_percent) {
		KomenDataset data = new KomenDataset();

		data.updateData(DataReader.readSaraKomens(sara_file), train_sara_percent);
		data.updateData(DataReader.readNormalKomens(normal_file), train_normal_percent);

		System.out.println("Train data: "+ data.train_pos + " -- " + data.train_neg);
		System.out.println("Test data: " + data.test_pos + " -- " + data.test_neg);
		System.out.println("\n\n-------------------");

		return data;
	}


	/**
	 * Load classifier from file
	 * @param classifier_path
	 * @return
	 */
	public static Classifier loadClassifier(String classifier_path) {
		Classifier classifier = null;

		try {
			classifier = KomenClassification.loadClassifier(new File(classifier_path));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return classifier;
	}
	
	

	public static void saveClassifier(Classifier classifier, String classifier_path) {
		try {
			KomenClassification.saveClassifier(classifier, new File(classifier_path));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Classify comment by classifier
	 * @param classifier
	 * @param input
	 * @return
	 */
	public static String classify(Classifier classifier, String input) {
		Classification cf = classifier.classify(input);
		String res = cf.getLabeling().getBestLabel().toString();
		return res;
	}
	
	
	/**
	 * Classify comment by classifier
	 * @param classifier
	 * @param input
	 * @return
	 */
	public static Labeling classifyLabel(Classifier classifier, String input) {
		Classification cf = classifier.classify(input);
		Labeling res = cf.getLabeling();
		return res;
	}
	
	public static Map<String, Double> classifyMap(Classifier c, String input) {
		Map<String, Double> result = new HashMap<String, Double>();
		
		Classification cf = c.classify(input);
		Labeling res = cf.getLabeling();
		
		Label first = res.getLabelAtRank(0);
		Double fScore = res.value(first);
		Label second = res.getLabelAtRank(1);
		Double sScore = res.value(second);
		
		result.put(first.toString(), fScore);
		result.put(second.toString(), sScore);
		
		
		
		return result;
	}
	
	/**
	 * Classify comment to sara / normal / probable
	 * 
	 * @param c - classifier
	 * @param input - comment to classify
	 * @param normal_threshold - Confident threshold to make sure a comment is normal
	 * @param sara_threshold - Confident threshold to make sure a comment is sara
	 * @return
	 */
	public static String classifyConfident(Classifier c, String input, double normal_threshold, double sara_threshold) {
		
		
		Classification cf = c.classify(input);
		Labeling res = cf.getLabeling();
		
		
		String best_label = res.getBestLabel().toString();
		double best_score = res.getBestValue();
		
		String result = Komen.PROBABLE;
		if((best_label.equals(Komen.NORMAL) && best_score >= normal_threshold)
				|| (best_label.equals(Komen.SARA) && best_score >= sara_threshold)) {
			result = best_label;
		}
		
		return result;
	}
	
	/**
	 * Classify comment to sara / normal / undefined with default threshold
	 * 
	 * @param c - classifier
	 * @param input - comment to classify
	 * @return
	 */
	public static String classifyConfident(Classifier c, String input) {
		
		
		String result = classifyConfident(c, input, NORMAL_THRES, SARA_THRES);
		
		return result;
	}
	

	

	public static Classifier trainClassifier(InstanceList trainingInstances) {
		return KomenClassification.trainClassifier(trainingInstances);
	}

	public static InstanceList buildInstance(String bad_file, String good_file,
			double train_percent) {
		return KomenClassification.buildInstance(bad_file, good_file, train_percent);
	}

	/**
	 * Build classifier from good_data and bad_data then save to classifier_file
	 * @param bad_file
	 * @param good_file
	 * @param train_percent
	 * @param classifier_file
	 * @return
	 */
	public static Classifier buildClassifier(String bad_file, String good_file,
			double train_percent, String classifier_file) {
		return KomenClassification.buildClassifier(bad_file, good_file, train_percent, classifier_file);
	}
	
	
	public static Classifier buildClassifier(KomenDataset dataset, String classifier_file) {
		return KomenClassification.buildClassifier(dataset, classifier_file);
	}


	public static void loadAndEstimate(String bad_file, String good_file,
			double train_percent, String classifier_file) {
		
		KomenClassification.loadAndEstimate(bad_file, good_file, train_percent, classifier_file);
	}

	public static void estimateClassifier(Classifier c, KomenDataset data) {
		KomenClassification.estimateClassifier(c, data);
	}

	/**
	 * Calculate and show precision and recall result
	 * @param true_pos
	 * @param false_neg
	 * @param false_pos
	 * @param true_neg
	 */
	public static void showResult(double true_pos, double false_neg, double false_pos, double true_neg) {
		System.out.println();
		System.out.println("true_pos: " + true_pos + " -- false_neg: " + false_neg);
		System.out.println("false_pos: " + false_pos + " -- true_neg: " + true_neg);

		System.out.println();
		System.out.println("true_pos: " + true_pos + " -- true_pos + false_pos: "
				+ (false_pos + true_pos));
		System.out.println("true_pos: " + true_pos + " -- false_neg + true_pos: "
				+ (false_neg + true_pos));

		double precision = true_pos * 1.0 / (true_pos + false_pos);
		double recall = true_pos * 1.0 / (false_neg + true_pos);
		System.out.println("Precision: " + precision + " -- Recall: " + recall);
		double f_score = 2 * precision * recall / (precision + recall);
		System.out.println("F-Score: " + f_score);
		
		
		
		System.out.println();
		System.out.println("true_neg: " + true_neg + " -- true_neg + false_neg: "
				+ (true_neg + false_neg));
		System.out.println("true_neg: " + true_neg + " -- false_neg + true_pos: "
				+ (true_neg + false_pos));
		
		double n_precision = true_neg * 1.0 / (true_neg + false_neg);
		double n_recall = true_neg * 1.0 / (true_neg + false_pos);
		
		System.out.println("Neg_Precision: " + n_precision + " -- Ne_Recall: " + n_recall);
		double n_f_score = 2 * n_precision * n_recall / (n_precision + n_recall);
		System.out.println("Neg_F-score: " + n_f_score);
		
		System.out.println("\n--------------------");
	}

}

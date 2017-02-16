package id.co.babe.classify;

import id.co.babe.filter.DataReader;
import id.co.babe.filter.model.KomenDataset;

import java.io.File;

import cc.mallet.classify.Classification;
import cc.mallet.classify.Classifier;
import cc.mallet.types.InstanceList;

public class SaraCommentAPI {


	public static KomenDataset buildData(String good_file, String bad_file,
			double train_percent) {
		KomenDataset data = new KomenDataset();

		data.updateData(DataReader.readSpamKomens(good_file), train_percent);
		data.updateData(DataReader.readNormalKomens(bad_file), train_percent);

		System.out.println("Train data: ");
		System.out.println(data.train_pos + " -- " + data.train_neg);

		System.out.println("\n\nTest data: ");
		System.out.println(data.test_pos + " -- " + data.test_neg);
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

	public static void loadAndEstimate(String bad_file, String good_file,
			double train_percent, String classifier_file) {
		
		KomenClassification.loadAndEstimate(bad_file, good_file, train_percent, classifier_file);
	}

	public static void estimateClassifier(Classifier c, KomenDataset data) {
		KomenClassification.estimateClassifier(c, data);
	}


}

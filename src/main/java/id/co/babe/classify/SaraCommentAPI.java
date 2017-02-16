package id.co.babe.classify;

import id.co.babe.filter.DataReader;
import id.co.babe.filter.model.KomenDataset;

import java.io.File;
import java.io.IOException;

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


	public static Classifier loadClassifier(File serializedFile) {
		Classifier classifier = null;

		try {
			classifier = KomenClassification.loadClassifier(serializedFile);
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		
		return classifier;
	}

	public static void saveClassifier(Classifier classifier, File serializedFile) {
		try {
			KomenClassification.saveClassifier(classifier, serializedFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Classifier trainClassifier(InstanceList trainingInstances) {
		return KomenClassification.trainClassifier(trainingInstances);
	}

	public static InstanceList buildInstance(String bad_file, String good_file,
			double train_percent) {
		return KomenClassification.buildInstance(bad_file, good_file, train_percent);
	}

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

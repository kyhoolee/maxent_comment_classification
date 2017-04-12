package id.co.babe.sara.classify;

import id.co.babe.sara.filter.DataReader;
import id.co.babe.sara.filter.model.Komen;
import id.co.babe.sara.filter.model.KomenDataset;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import cc.mallet.classify.Classifier;
import cc.mallet.classify.ClassifierTrainer;
import cc.mallet.classify.MaxEnt;
import cc.mallet.classify.MaxEntTrainer;
import cc.mallet.pipe.CharSequence2TokenSequence;
import cc.mallet.pipe.FeatureSequence2FeatureVector;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.Target2Label;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.pipe.TokenSequenceLowercase;
import cc.mallet.pipe.TokenSequenceNGrams;
import cc.mallet.pipe.iterator.ArrayIterator;
import cc.mallet.types.InstanceList;

public class KomenClassification {


	public static KomenDataset buildData(String good_file, String bad_file,
			double train_percent) {
		KomenDataset data = new KomenDataset();

		data.updateData(DataReader.readSaraKomens(good_file), train_percent);
		data.updateData(DataReader.readNormalKomens(bad_file), train_percent);

		System.out.println("Train data: ");
		System.out.println(data.train_pos + " -- " + data.train_neg);

		System.out.println("\n\nTest data: ");
		System.out.println(data.test_pos + " -- " + data.test_neg);
		System.out.println("\n\n-------------------");

		return data;
	}


	public static Classifier loadClassifier(File serializedFile)
			throws FileNotFoundException, IOException, ClassNotFoundException {
		Classifier classifier;

		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
				serializedFile));
		classifier = (Classifier) ois.readObject();
		ois.close();

		return classifier;
	}

	public static void saveClassifier(Classifier classifier, File serializedFile)
			throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(
				serializedFile));
		oos.writeObject(classifier);
		oos.close();
	}

	public static Classifier trainClassifier(InstanceList trainingInstances) {
		ClassifierTrainer<MaxEnt> trainer = new MaxEntTrainer();
		return trainer.train(trainingInstances);
	}

	public static InstanceList buildInstance(String bad_file, String good_file,
			double train_percent) {
		KomenDataset data = buildData(bad_file, good_file, train_percent);
		return buildInstance(data);
	}

	public static InstanceList buildInstance(KomenDataset data) {
		List<String> badTrain = new ArrayList<>();
		List<String> goodTrain = new ArrayList<>();
		for (int i = 0; i < data.train.size(); i++) {
			Komen k = data.train.get(i);
			String train_content = k.content;//NorvigSpellCorrector.correctSentence(k.content, NorvigSpellCorrector.dict); 
					//
			if (k.label == Komen.NORMAL) {
				goodTrain.add(train_content);
			}
			if (k.label == Komen.SARA) {
				badTrain.add(train_content);
			}
		}
		InstanceList instances = new InstanceList(new SerialPipes(new Pipe[] {
				new Target2Label(), 
				new CharSequence2TokenSequence(),
				new TokenSequenceLowercase(),
				//new IndoTokenCorrector(),
				new IndoTokenRemoveStopwords(),
				//new TokenSequenceNGrams(new int[]{1,2}),
				new TokenSequence2FeatureSequence(),
				new FeatureSequence2FeatureVector()
		}));
		
		
		instances.addThruPipe(new ArrayIterator(badTrain, Komen.SARA));
		instances.addThruPipe(new ArrayIterator(goodTrain, Komen.NORMAL));
		return instances;
	}

	public static Classifier buildClassifier(String bad_file, String good_file,
			double train_percent, String classifier_file) {
		InstanceList instances = buildInstance(bad_file, good_file,
				train_percent);
		Classifier classifier = trainClassifier(instances);
		try {
			saveClassifier(classifier, new File(classifier_file));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return classifier;
	}
	
	
	public static Classifier buildClassifier(KomenDataset dataset, String classifier_file) {
		InstanceList instances = buildInstance(dataset);
		Classifier classifier = trainClassifier(instances);
		try {
			saveClassifier(classifier, new File(classifier_file));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return classifier;
	}

	public static void loadAndEstimate(String bad_file, String good_file,
			double train_percent, String classifier_file) {
		
		KomenDataset data = buildData(bad_file, good_file, train_percent);
		try {
			Classifier c = loadClassifier(new File(classifier_file));
			estimateClassifier(c, data);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void estimateClassifier(Classifier c, KomenDataset data) {
		int false_pos = 0;
		int false_neg = 0;
		int true_pos = 0;
		int true_neg = 0;

		for (int i = 0; i < data.test.size(); i++) {
			Komen k = data.test.get(i);
			String test_content = k.content;
			String res = c.classify(test_content).getLabeling().getBestLabel().toString();

			if (k.label.equals(Komen.NORMAL)) {
				if (res.equals(Komen.NORMAL)) {
					true_neg++;
				} else {
					false_neg++;
				}
			} else {
				if (res.equals(Komen.SARA)) {
					true_pos++;
				} else {
					false_pos++;
				}
			}
		}

		SaraCommentAPI.showResult(true_pos, false_neg, false_pos, true_neg);
	}


}

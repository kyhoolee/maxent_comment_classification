package id.co.babe.classify;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cc.mallet.classify.Classification;
import cc.mallet.classify.Classifier;
import cc.mallet.classify.ClassifierTrainer;
import cc.mallet.classify.MaxEntTrainer;
import cc.mallet.classify.NaiveBayesTrainer;
import cc.mallet.pipe.CharSequence2TokenSequence;
import cc.mallet.pipe.FeatureCountPipe;
import cc.mallet.pipe.FeatureSequence2FeatureVector;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.Target2Label;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.pipe.TokenSequenceLowercase;
import cc.mallet.pipe.TokenSequenceRemoveStopwords;
import cc.mallet.pipe.iterator.ArrayIterator;
import cc.mallet.types.FeatureVector;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import cc.mallet.types.LabelAlphabet;
import id.co.babe.classifier.bayes.BayesClassifier;
import id.co.babe.filter.DataReader;
import id.co.babe.filter.RuleFilter;
import id.co.babe.filter.TextfileIO;
import id.co.babe.filter.model.Komen;
import id.co.babe.filter.model.KomenDataset;
import id.co.babe.util.Util;

public class KomenClassification {
	public static void main(String[] args) {

		//estimateBayes();
		estimateMaxent();
	}

	public static final String ROOT = "";
			//"/home/mainspring/tutorial/learn/text-classifier/data_komen/";

	private static KomenDataset buildData() {
		KomenDataset data = new KomenDataset();

		data.updateData(DataReader.readSpamKomens(ROOT + "revised_bad_komen.txt"), 0.75);
		data.updateData(DataReader.readNormalKomens(ROOT + "revised_good_komen.txt"), 0.8);

		System.out.println("Train data: ");
		System.out.println(data.train_pos + " -- " + data.train_neg);

		System.out.println("\n\nTest data: ");
		System.out.println(data.test_pos + " -- " + data.test_neg);
		System.out.println("\n\n-------------------");

		return data;
	}

	public static String repaceSpeCharacter(String input) {
		String alphaAndDigits = input.replaceAll("[^a-zA-Z0-9]+"," ");
		return alphaAndDigits;
	}
	
	
	public Classifier loadClassifier(File serializedFile)
	        throws FileNotFoundException, IOException, ClassNotFoundException {

	        // The standard way to save classifiers and Mallet data                                            
	        //  for repeated use is through Java serialization.                                                
	        // Here we load a serialized classifier from a file.                                               

	        Classifier classifier;

	        ObjectInputStream ois =
	            new ObjectInputStream (new FileInputStream(serializedFile));
	        classifier = (Classifier) ois.readObject();
	        ois.close();

	        return classifier;
	    }
	
	public void saveClassifier(Classifier classifier, File serializedFile)
	        throws IOException {

	        // The standard method for saving classifiers in                                                   
	        //  Mallet is through Java serialization. Here we                                                  
	        //  write the classifier object to the specified file.                                             

	        ObjectOutputStream oos =
	            new ObjectOutputStream(new FileOutputStream(serializedFile));
	        oos.writeObject (classifier);
	        oos.close();
	    }
	
	public Classifier trainClassifier(InstanceList trainingInstances) {

        // Here we use a maximum entropy (ie polytomous logistic regression)                               
        //  classifier. Mallet includes a wide variety of classification                                   
        //  algorithms, see the JavaDoc API for details.                                                   

        ClassifierTrainer trainer = new MaxEntTrainer();
        return trainer.train(trainingInstances);
    }
	
	public static void estimateSVM() {
		KomenDataset data = buildData();

		List<String> badTrain = new ArrayList<>();
		List<String> goodTrain = new ArrayList<>();

		// Train the probability with naive-bayes
		for (int i = 0; i < data.train.size(); i++) {
			Komen k = data.train.get(i);
			String train_content = repaceSpeCharacter(k.content);
			if (k.label == Komen.NORMAL) {
				goodTrain.add(train_content);
			}

			if (k.label == Komen.SPAM) {
				badTrain.add(train_content);
			}
		}

		InstanceList instances = new InstanceList(
				new SerialPipes(
						new Pipe[] { 
								new Target2Label(),
								new CharSequence2TokenSequence(), 
								
								new TokenSequenceLowercase(), 
								
								new IndoTokenRemoveStopwords(),
								
								new TokenSequence2FeatureSequence(), 
								
								new FeatureSequence2FeatureVector()
							
				}));

		instances.addThruPipe(new ArrayIterator(badTrain, Komen.SPAM));
		instances.addThruPipe(new ArrayIterator(goodTrain, Komen.NORMAL));
		
		
		
		Classifier c = new MaxEntTrainer().train(instances);

		Classification cf = c.classify("nelson mandela never eats lions");
		System.out.println(cf.getLabeling().getBestLabel() == ((LabelAlphabet) instances.getTargetAlphabet())
				.lookupLabel("africa"));

		// Test the model

		int false_pos = 0;
		int false_neg = 0;
		int true_pos = 0;
		int true_neg = 0;

		List<String> falseNegList = new ArrayList<>();
		List<String> falsePosList = new ArrayList<>();

		for (int i = 0; i < data.test.size(); i++) {
			Komen k = data.test.get(i);
			String test_content = repaceSpeCharacter(k.content);
			String res = c.classify(test_content).getLabeling().getBestLabel().toString();
			

			if (k.label == Komen.SPAM && res == Komen.NORMAL) {
				falsePosList.add(k.content);
			}

			if (k.label == Komen.NORMAL && res == Komen.SPAM) {
				falseNegList.add(k.content);
			}

			if (k.label == Komen.NORMAL) {
				if (res == Komen.NORMAL) {
					true_neg++;
				} else {
					false_neg++;
				}
			} else {
				if (res == Komen.SPAM) {
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
	
	public static void estimateMaxent() {
		KomenDataset data = buildData();

		List<String> badTrain = new ArrayList<>();
		List<String> goodTrain = new ArrayList<>();

		// Train the probability with naive-bayes
		for (int i = 0; i < data.train.size(); i++) {
			Komen k = data.train.get(i);
			String train_content = repaceSpeCharacter(k.content);
			if (k.label == Komen.NORMAL) {
				goodTrain.add(train_content);
			}

			if (k.label == Komen.SPAM) {
				badTrain.add(train_content);
			}
		}

		InstanceList instances = new InstanceList(
				new SerialPipes(
						new Pipe[] { 
								new Target2Label(),
								new CharSequence2TokenSequence(), 
								
								new TokenSequenceLowercase(), 
								
								new IndoTokenRemoveStopwords(),
								
								new TokenSequence2FeatureSequence(), 
								
								new FeatureSequence2FeatureVector()
							
				}));

		instances.addThruPipe(new ArrayIterator(badTrain, Komen.SPAM));
		instances.addThruPipe(new ArrayIterator(goodTrain, Komen.NORMAL));
		
		
		
		Classifier c = new MaxEntTrainer().train(instances);

		Classification cf = c.classify("nelson mandela never eats lions");
		System.out.println(cf.getLabeling().getBestLabel() == ((LabelAlphabet) instances.getTargetAlphabet())
				.lookupLabel("africa"));

		// Test the model

		int false_pos = 0;
		int false_neg = 0;
		int true_pos = 0;
		int true_neg = 0;

		List<String> falseNegList = new ArrayList<>();
		List<String> falsePosList = new ArrayList<>();

		for (int i = 0; i < data.test.size(); i++) {
			Komen k = data.test.get(i);
			String test_content = repaceSpeCharacter(k.content);
			String res = c.classify(test_content).getLabeling().getBestLabel().toString();
			

			if (k.label == Komen.SPAM && res == Komen.NORMAL) {
				falsePosList.add(k.content);
			}

			if (k.label == Komen.NORMAL && res == Komen.SPAM) {
				falseNegList.add(k.content);
			}

			if (k.label == Komen.NORMAL) {
				if (res == Komen.NORMAL) {
					true_neg++;
				} else {
					false_neg++;
				}
			} else {
				if (res == Komen.SPAM) {
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

	public static void estimateBayes() {
		KomenDataset data = buildData();

		List<String> badTrain = new ArrayList<>();
		List<String> goodTrain = new ArrayList<>();

		// Train the probability with naive-bayes
		for (int i = 0; i < data.train.size(); i++) {
			Komen k = data.train.get(i);

			if (k.label == Komen.NORMAL) {
				goodTrain.add(k.content);
			}

			if (k.label == Komen.SPAM) {
				badTrain.add(k.content);
			}
		}

		InstanceList instances = new InstanceList(new SerialPipes(new Pipe[] { new Target2Label(),
				new CharSequence2TokenSequence(), new TokenSequenceLowercase(), new IndoTokenRemoveStopwords(),
				new TokenSequence2FeatureSequence(), new FeatureSequence2FeatureVector() }));

		instances.addThruPipe(new ArrayIterator(badTrain, Komen.SPAM));
		instances.addThruPipe(new ArrayIterator(goodTrain, Komen.NORMAL));
		Classifier c = new NaiveBayesTrainer().train(instances);

		

		// Test the model

		int false_pos = 0;
		int false_neg = 0;
		int true_pos = 0;
		int true_neg = 0;

		List<String> falseNegList = new ArrayList<>();
		List<String> falsePosList = new ArrayList<>();

		for (int i = 0; i < data.train.size(); i++) {
			Komen k = data.train.get(i);
			String res = c.classify(k.content).getLabeling().getBestLabel().toString();
			

			if (k.label == Komen.SPAM && res == Komen.NORMAL) {
				falsePosList.add(k.content);
			}

			if (k.label == Komen.NORMAL && res == Komen.SPAM) {
				falseNegList.add(k.content);
			}

			if (k.label == Komen.NORMAL) {
				if (res == Komen.NORMAL) {
					true_neg++;
				} else {
					false_neg++;
				}
			} else {
				if (res == Komen.SPAM) {
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

	public static void estimate() {
		KomenDataset data = buildData();

		// Train the probability with naive-bayes
		BayesClassifier<String, String> bayes = new BayesClassifier<String, String>();
		for (int i = 0; i < data.train.size(); i++) {
			Komen k = data.train.get(i);
			bayes.learn(k.label, WordTokenizer.tokenize(k.content));// Arrays.asList(k.content.toLowerCase().split("\\s")));
		}

		// Test the model

		int false_pos = 0;
		int false_neg = 0;
		int true_pos = 0;
		int true_neg = 0;

		List<String> falseNegList = new ArrayList<>();
		List<String> falsePosList = new ArrayList<>();

		for (int i = 0; i < data.test.size(); i++) {
			Komen k = data.test.get(i);
			String res = bayes.classify(WordTokenizer.tokenize(k.content)).getCategory();// Arrays.asList(k.content.toLowerCase().split("\\s"))).getCategory();

			// k.content = Util.filter(k.content);
			// if(res == Komen.NORMAL) {
			// res = RuleFilter.ruleSpam(k.content);
			// } else { //if(res == Komen.SPAM)
			// res = RuleFilter.ruleNormal(k.content);
			// }

			if (k.label == Komen.SPAM && res == Komen.NORMAL) {
				falsePosList.add(k.content);// RuleFilter.printRule(k.content).toLowerCase());//
			}

			if (k.label == Komen.NORMAL && res == Komen.SPAM) {
				falseNegList.add(k.content);// RuleFilter.printRule(k.content));
											// //k.content);//
			}

			if (k.label == Komen.NORMAL) {
				if (res == Komen.NORMAL) {
					true_neg++;
				} else {
					false_neg++;
				}
			} else {
				if (res == Komen.SPAM) {
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

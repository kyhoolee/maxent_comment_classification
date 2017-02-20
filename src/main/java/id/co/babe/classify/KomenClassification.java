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
import java.util.Map;

import cc.mallet.classify.Classification;
import cc.mallet.classify.Classifier;
import cc.mallet.classify.ClassifierTrainer;
import cc.mallet.classify.MaxEnt;
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
import cc.mallet.types.Label;
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

		// estimateBayes();
		// estimateMaxent();
		
		//Classifier c = buildClassifier("revised_bad_komen.txt", "revised_good_komen.txt", 0.8, "maxent_classifier.data");
		//KomenDataset data = buildData();
		//estimateClassifier(c, data);
		//loadAndEstimate("revised_bad_komen.txt", "revised_good_komen.txt", 0.8, "maxent_classifier.data");
		checkClassifier();
		//System.out.println(replaceSpeCharacter("Dan kalo lu kalah HOK,,,lu bakalan balik k cina jualan BAKPAO....HAHAHA"));
	}
	
	public static void checkClassifier() {
		Classifier classifier;

		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(new FileInputStream(new File("maxent_classifier.data")));
			classifier = (Classifier) ois.readObject();
			ois.close();
			
			
			
			String[] test = {
					"Bego ini orang kenapa Ahok bilang begitu..? Karena sebelumnya si Novel ini sering ucapkan kalimat Kafir berulang-ulang kepada orang yg bukan Muslim.kamu lompat aja dari monas biar mampus sekalian.munafik.",
					"Jujur yah kalo ngisep peler aceh itu rasanya nikmat,  Rasanya jantan banget rasanya pengen dipaksa paksa ma tuh cowok aceh  Dari hati yg terdalam saya merasa siap diapain aja,  Dipakein rok ok, bando ok, kutang emak2 hayo, kemben juga masuk  Welcome cowok aceh  Welcome in my ash.",
					"Dan kalo lu kalah HOK,,,lu bakalan balik k cina jualan BAKPAO....HAHAHA",
					"Yaskar roz org ini dengan bangganya sebut kata kafir pakai hp buatan kafir...  Binggungkan lu pada..",
					"Ada kok yg suka Ahok  Sipit & Non Muslim  Hanya orang bodoh banget yg suka Ahok.",
					"Ini contoh anak bangsa yg buruk.... Smoga bj habibie cepat mati...",
					"KIH DIKASIH HATI MINTA JANTUNG...KIH GEMBLUUUUNG",
					"Faktanya kelakuan iblis jauh lebih baik drpd kelakuan muhammad yg hobinya ngentot sembarangan, pantesan aja ninggalnya krn penyakit raja singa",
					"Ya kmu bapaknya anjing. Kata2mu lebih lotor dri najis babi. Iblis berbentuk manusia ya kmu ini",
					"POLISI ADA APA YAH TIDAK TANGKAP TERDAKWA SI AHOK KAFIR",
					"Congor si joki aja di boikot biar gk bacot bikin mulut tmbh mecotot",
					"Yg penting fitsa hats. Apa yg diomongkan si bibib fitsa hats g perlu di dengar selain fitsa hatsnya hehehehe",
					"Bajingan si fahri..mulut dower...anjing najis. Cuih..."
			};
			
			for(int i = 0 ; i < test.length ; i ++) {
				Classification cf = classifier.classify(test[i]);
				Label label = cf.getLabeling().getBestLabel();
				String res = cf.getLabeling().getBestLabel().toString();
				double score = cf.getLabeling().value(label);
				System.out.println(res + "  " + score + " -- " + test[i]);
				Map<String, Double> result = SaraCommentAPI.classifyMap(classifier, test[i]);
				System.out.println("  " + Komen.NORMAL + ":"  + result.get(Komen.NORMAL) + "    --   " + Komen.SPAM + ":" + result.get(Komen.SPAM));
			}
			
			String[] test1 = {
					"Ini bru fisi n misi...kreeeeen",
					"terimakasih bp presiden bapak presiden sungguh merakyat semoga presiden yg lain nanti juga begitu bisa pro rakyat",
					"Bukan kota bung yang ngapung .tai banyak pada ngapung.hhuuuaaak ciuh",
					"Ini fanatik sama pujaan kebablasan... di fb boanyak yg ginian tapi ga dibuat dalam bentuk.buku...jadi mereka.sek aman...menjelek2kan orang lain..",
					"Hanya kekotoran hatilah yg berharap Ahok di penjara. Muslim cerdas rahmatan pasti dukung Ahok????????",
					"Setuju mrk benci karna di hasut untuk membenci.. mrk membenci krn perbedaan agama.. secara gk langsng mrk menistakan agama mrk krn menunjukan rasa benci itu.",
					"Bayarnya nanti pake hadiah rumah sby yg di kuningan aja daripada diksh sm sby yg sdh banyak makan uang rakyat uang yg bukan haknya kan lumayan ngurangin hutangnya indonesia",
					"Kami cuma bisa foa semoga pok ahok jsfi gubnur",
					"Orang yang pengen dimanja, enak enakan. Tapi gak peduli ama yang lain. Itulah Ato Widiansyah",
					"Pasti ada yg berani bayar lebih",
					"S7 bro. Jd intinya msa pmrinthn skrg rkyat hrs tnduk kpda pmrinth,gk bisa br-opini. Dkit2 dbilg makar,apalah..mdh2n cpt slse rezim pmrnthn skrg.",
					"Hahahahaha....komen....dengan.hati...kotor...itu...bro....di...baca...dlu...",
					"Hahaha btul pa ahok,, dua pa ahok bukan satu atau tiga...",
					"salut buat TNI., sy dukung penuh sikap TNI., krn sy sbg wrg negara indonesia jg tersinggung negara kt di lecehkan spt itu...bravo TNI",
					"Heee...hebat ni org,coba bilang jg yg tersandung hukum smw g usah di tahan bro..",
					"Busyeet sampe ke bawa mimpi loe si ahok? Di forum yg ngak ada sangkut pautnya pun kesebut namanya TOP banget... CINTA Ya loe sama ahok",
					"Ini bukan hilang, tapi menghilangkan diri, anak tdk tahu terimakasih terhadap orang tua.",
					"Benny jumalin onta sengir gudig, ehh manusia",
					"Hahaha sekolah g y...bljar bca lgi ya",
					"Hajar pak kami rakya indonesia siap",
			};
			
			System.out.println();
			for(int i = 0 ; i < test1.length ; i ++) {
				Classification cf = classifier.classify(test1[i]);
				Label label = cf.getLabeling().getBestLabel();
				String res = cf.getLabeling().getBestLabel().toString();
				double score = cf.getLabeling().value(label);
				System.out.println(res + "  " + score + " -- " + test1[i]);
				Map<String, Double> result = SaraCommentAPI.classifyMap(classifier, test1[i]);
				System.out.println("  " + Komen.NORMAL + ":"  + result.get(Komen.NORMAL) + "    --   " + Komen.SPAM + ":" + result.get(Komen.SPAM));
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		

		
	}

	public static final String ROOT = "";

	// "/home/mainspring/tutorial/learn/text-classifier/data_komen/";

	private static KomenDataset buildData() {
		KomenDataset data = new KomenDataset();

		data.updateData(
				DataReader.readSpamKomens(ROOT + "revised_17_182_sara_komen.txt"), 0.6);//revised_ken_sara_comments.txt
		data.updateData(
				DataReader.readNormalKomens(ROOT + "revised_17_182_non_sara_komen.txt"),
				0.6);

		System.out.println("Train data: ");
		System.out.println(data.train_pos + " -- " + data.train_neg);

		System.out.println("\n\nTest data: ");
		System.out.println(data.test_pos + " -- " + data.test_neg);
		System.out.println("\n\n-------------------");

		return data;
	}

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

	public static String replaceSpeCharacter(String input) {
		String alphaAndDigits = input.replaceAll("[^a-zA-Z0-9]+", " ");
		return alphaAndDigits;
	}

	public static Classifier loadClassifier(File serializedFile)
			throws FileNotFoundException, IOException, ClassNotFoundException {

		// The standard way to save classifiers and Mallet data
		// for repeated use is through Java serialization.
		// Here we load a serialized classifier from a file.

		Classifier classifier;

		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
				serializedFile));
		classifier = (Classifier) ois.readObject();
		ois.close();

		return classifier;
	}

	public static void saveClassifier(Classifier classifier, File serializedFile)
			throws IOException {

		// The standard method for saving classifiers in
		// Mallet is through Java serialization. Here we
		// write the classifier object to the specified file.

		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(
				serializedFile));
		//System.out.println(classifier);
		oos.writeObject(classifier);
		oos.close();
	}

	public static Classifier trainClassifier(InstanceList trainingInstances) {

		// Here we use a maximum entropy (ie polytomous logistic regression)
		// classifier. Mallet includes a wide variety of classification
		// algorithms, see the JavaDoc API for details.

		ClassifierTrainer<MaxEnt> trainer = new MaxEntTrainer();
		return trainer.train(trainingInstances);
	}

	public static InstanceList buildInstance(String bad_file, String good_file,
			double train_percent) {
		KomenDataset data = buildData(bad_file, good_file, train_percent);

		List<String> badTrain = new ArrayList<>();
		List<String> goodTrain = new ArrayList<>();

		// Train the probability with naive-bayes
		for (int i = 0; i < data.train.size(); i++) {
			Komen k = data.train.get(i);
			String train_content = k.content;// repaceSpeCharacter(k.content);
			if (k.label == Komen.NORMAL) {
				goodTrain.add(train_content);
			}

			if (k.label == Komen.SPAM) {
				badTrain.add(train_content);
			}
		}

		InstanceList instances = new InstanceList(new SerialPipes(new Pipe[] {
				new Target2Label(), new CharSequence2TokenSequence(),

				new TokenSequenceLowercase(),

				new IndoTokenRemoveStopwords(),

				new TokenSequence2FeatureSequence(),

				new FeatureSequence2FeatureVector()

		}));

		instances.addThruPipe(new ArrayIterator(badTrain, Komen.SPAM));
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
		// Test the model

		int false_pos = 0;
		int false_neg = 0;
		int true_pos = 0;
		int true_neg = 0;

		List<String> falseNegList = new ArrayList<>();
		List<String> falsePosList = new ArrayList<>();

		for (int i = 0; i < data.test.size(); i++) {
			Komen k = data.test.get(i);
			String test_content = k.content;// repaceSpeCharacter(k.content);
			String res = c.classify(test_content).getLabeling().getBestLabel().toString();
			//System.out.println(k.label + "-" + res + "  " + test_content);
			if (k.label.equals(Komen.SPAM) && res.equals(Komen.NORMAL)) {
				falsePosList.add(k.content);
			}

			if (k.label.equals(Komen.NORMAL) && res.equals(Komen.SPAM)) {
				falseNegList.add(k.content);
			}

			if (k.label.equals(Komen.NORMAL)) {
				if (res.equals(Komen.NORMAL)) {
					true_neg++;
				} else {
					false_neg++;
				}
			} else {
				if (res.equals(Komen.SPAM)) {
					true_pos++;
				} else {
					false_pos++;
				}
			}

		}

		System.out.println("\n\n");
		System.out.println("False_pos: " + false_pos + " -- Total_pos: "
				+ (false_pos + true_pos));
		System.out.println("False_neg: " + false_neg + " -- Total_neg: "
				+ (false_neg + true_neg));

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
			String train_content = k.content;// repaceSpeCharacter(k.content);
			if (k.label == Komen.NORMAL) {
				goodTrain.add(train_content);
			}

			if (k.label == Komen.SPAM) {
				badTrain.add(train_content);
			}
		}

		InstanceList instances = new InstanceList(new SerialPipes(new Pipe[] {
				new Target2Label(), new CharSequence2TokenSequence(),

				new TokenSequenceLowercase(),

				new IndoTokenRemoveStopwords(),

				new TokenSequence2FeatureSequence(),

				new FeatureSequence2FeatureVector()

		}));

		instances.addThruPipe(new ArrayIterator(badTrain, Komen.SPAM));
		instances.addThruPipe(new ArrayIterator(goodTrain, Komen.NORMAL));

		ClassifierTrainer<MaxEnt> trainer = new MaxEntTrainer();
		Classifier c = trainer.train(instances);

		Classification cf = c
				.classify("Ada kok yg suka Ahok  Sipit & Non Muslim  Hanya orang bodoh banget yg suka Ahok.");
		System.out
				.println(cf.getLabeling().getBestLabel() == ((LabelAlphabet) instances
						.getTargetAlphabet()).lookupLabel("0"));

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
			cf = c.classify(k.content);
			Label label = cf.getLabeling().getBestLabel();
			String res = cf.getLabeling().getBestLabel().toString();
			double score = cf.getLabeling().value(label);
			if(score > 0.75) {
				highTrust.add(k.content);
				
				if (k.label.equals(Komen.SPAM) && res.equals(Komen.NORMAL)) {
					highFalsePosList.add(k.content);
				}

				if (k.label.equals(Komen.NORMAL) && res.equals(Komen.SPAM)) {
					highFalseNegList.add(k.content);
				}
				
				
				if (k.label.equals(Komen.NORMAL)) {
					if (res.equals(Komen.NORMAL)) {
						high_true_neg++;
					} else {
						high_false_neg++;
					}
				} else {
					if (res.equals(Komen.SPAM)) {
						high_true_pos++;
					} else {
						high_false_pos++;
					}
				}
			}
			
			if(res.equals(Komen.NORMAL) && score < 0.65)
				res = Komen.SPAM;

			if (k.label.equals(Komen.SPAM) && res.equals(Komen.NORMAL)) {
				falsePosList.add(k.content);
			}

			if (k.label.equals(Komen.NORMAL) && res.equals(Komen.SPAM)) {
				falseNegList.add(k.content);
			}

			if (k.label.equals(Komen.NORMAL)) {
				if (res.equals(Komen.NORMAL)) {
					true_neg++;
				} else {
					false_neg++;
				}
			} else {
				if (res.equals(Komen.SPAM)) {
					true_pos++;
				} else {
					false_pos++;
				}
			}

		}

		System.out.println("\n\n");
		System.out.println("False_pos: " + false_pos + " -- Total_pos: "
				+ (false_pos + true_pos));
		System.out.println("False_neg: " + false_neg + " -- Total_neg: "
				+ (false_neg + true_neg));

		TextfileIO.writeFile(ROOT + "false_negative.txt", falseNegList);
		TextfileIO.writeFile(ROOT + "false_positive.txt", falsePosList);

		double precision = true_pos * 1.0 / (true_pos + false_pos);
		double recall = true_pos * 1.0 / (false_neg + true_pos);
		System.out.println("Precision: " + precision + " -- Recall: " + recall);

		double f_score = 2 * precision * recall / (precision + recall);
		System.out.println("F-Score: " + f_score);
		
		System.out.println("\n\n");
		System.out.println("high_False_pos: " + high_false_pos + " -- high_Total_pos: "
				+ (high_false_pos + high_true_pos));
		System.out.println("high_False_neg: " + high_false_neg + " -- high_Total_neg: "
				+ (high_false_neg + high_true_neg));
		
		precision = high_true_pos * 1.0 / (high_true_pos + high_false_pos);
		recall = high_true_pos * 1.0 / (high_false_neg + high_true_pos);
		System.out.println("Precision: " + precision + " -- Recall: " + recall);

		f_score = 2 * precision * recall / (precision + recall);
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

		InstanceList instances = new InstanceList(new SerialPipes(new Pipe[] {
				new Target2Label(), new CharSequence2TokenSequence(),
				new TokenSequenceLowercase(), new IndoTokenRemoveStopwords(),
				new TokenSequence2FeatureSequence(),
				new FeatureSequence2FeatureVector() }));

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

		for (int i = 0; i < data.test.size(); i++) {
			Komen k = data.test.get(i);
			String res = c.classify(k.content).getLabeling().getBestLabel()
					.toString();

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
		System.out.println("False_pos: " + false_pos + " -- Total_pos: "
				+ (false_pos + true_pos));
		System.out.println("False_neg: " + false_neg + " -- Total_neg: "
				+ (false_neg + true_neg));

		TextfileIO.writeFile(ROOT + "false_negative.txt", falseNegList);
		TextfileIO.writeFile(ROOT + "false_positive.txt", falsePosList);

		double precision = true_pos * 1.0 / (true_pos + false_pos);
		double recall = true_pos * 1.0 / (false_neg + true_pos);
		System.out.println("Precision: " + precision + " -- Recall: " + recall);

		double f_score = 2 * precision * recall / (precision + recall);
		System.out.println("F-Score: " + f_score);
	}


}

package id.co.babe.classify;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import cc.mallet.classify.Classification;
import cc.mallet.classify.Classifier;
import cc.mallet.classify.NaiveBayesTrainer;
import cc.mallet.pipe.CharSequence2TokenSequence;
import cc.mallet.pipe.FeatureSequence2FeatureVector;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.Target2Label;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.pipe.iterator.ArrayIterator;
import cc.mallet.types.InstanceList;
import cc.mallet.types.LabelAlphabet;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.SentenceUtils;
import edu.stanford.nlp.process.DocumentPreprocessor;
import id.co.babe.filter.TextfileIO;
import id.co.babe.util.Util;

public class WordTokenizer {
	
	public static HashSet<String> stop_words = null;
	public static final String STOPWORD_PATH = "stop_words.txt";
	
	public static List<String> getStopWords() {
		List<String> data = TextfileIO.readFile(Thread.currentThread().getContextClassLoader().getResourceAsStream(STOPWORD_PATH));
		return data;
	}
	
	public static HashSet<String> stopWords() {
		if(stop_words == null) {
			List<String> data = TextfileIO.readFile(Thread.currentThread().getContextClassLoader().getResourceAsStream(STOPWORD_PATH));
			stop_words = new HashSet<>();
			stop_words.addAll(data);
		}
		
		return stop_words;
	}
	
	
	
	public static List<String> tokenize(String input) {
		HashSet<String> stopWords = stopWords();
		List<String> result = new ArrayList<>();
		String alphaOnly = filter(input);
		String[] tokens = alphaOnly.toLowerCase().split("\\s");
		for(String t : tokens) {
			if( !stopWords.contains(t)) {
				result.add(t);
			}
		}
			
		return result;
	}
	
	public static String filter(String input) {
		String alphaOnly = input.replaceAll("[^a-zA-Z]+"," ");
		
		return alphaOnly;
	}
	
	public static void main(String[] args) {
		
	}
	
	public void testStringTrained ()
	{
		String[] africaTraining = new String[] {
				"on the plains of africa the lions roar",
				"in swahili ngoma means to dance",
				"nelson mandela became president of south africa",
		"the saraha dessert is expanding"};
		String[] asiaTraining = new String[] {
				"panda bears eat bamboo",
				"china's one child policy has resulted in a surplus of boys",
		"tigers live in the jungle"};

		InstanceList instances =
			new InstanceList (
					new SerialPipes (new Pipe[] {
							new Target2Label (),
							new CharSequence2TokenSequence (),
							new TokenSequence2FeatureSequence (),
							new FeatureSequence2FeatureVector ()}));

		instances.addThruPipe (new ArrayIterator (africaTraining, "africa"));
		instances.addThruPipe (new ArrayIterator (asiaTraining, "asia"));
		Classifier c = new NaiveBayesTrainer ().train (instances);

		Classification cf = c.classify ("nelson mandela never eats lions");
		System.out.println(cf.getLabeling().getBestLabel()
				== ((LabelAlphabet)instances.getTargetAlphabet()).lookupLabel("africa"));
	}
	
	public static void testTokenize() {
		
		String[] data = {
				"Tdk berbobot coi beritanya..malu ama dunia internasional..gitu aja kok jadi viral..dasar	",
				"Novel itu cuma bisa bikin orang islam malu najis guwa orang seperti novel itu memang babi berkedok jubah	",
				"Gara gara fatwa mui semua jadi kacau, org yg lg bekerja utk pembangunan jakarta diganggu krn fatwa yg dibuat tanpa pemikiran yg dalam oleh mui.	",
				"Iri yg pasti... Dah kandangin aja kelamaaan	",
				"Om dusta om, nanti berdusta dan provokasi jg akan masuk surga. Hahaha	",
				"Akhir Pebruari 2017, demo demo selesai, paling ada Demo masak	",
				"Ada disurga buatan fpi, om dusta om	",
				"Indonesia Indonesia,,yg begitu dijadikan viral. Giliran yg baik2, gak diapa2in	",
				"ach lo emang sapi cina,,jdi begundalnya ahok hok	",
				"Om dusta om, novel gantiin pak ahok masuk bui, rizieq akan nyusul pula krn menghina agama lain. Hahaha	",
				"Om dusta om, bawa agama utk tujuan haram, skrg senjata mkn tuan. Hahaha	",
				"YTH  Habibku, kita Tidur...pakai AC buatan kafir. Kita berangkat/pulang kerja naik kendaraan buatan Kafir. Berkomunikasi dgn hp buatan kafir.Dan sekarang baru diketahui Habibku yg terhormat juga  mencari nafkah di tempat kafir. Maka sdh selayaknya kita menyayangi mereka jgn pernah mengkafir2 mereka.	",
				"Berisik nih kristen nya bodo. Udh tau di bohongin tuhan itu 1 bukan 3 ya ehh tetep aja bego. Apa ga ngerasa ketipu? Wahai domba tersesat	",
				"Insyaallah gw yakin ahok di Penjara, krn ini doa rakyat kecil yg dizalimi, para ulama & umat Muslim	",
				"Wkwkwkwk mampus... Natal bisa berujung kematian	",
				"Sakit hari karena pernah dipenjara 7 bulan ....dasar mantan penjahat ga ada yg baik dari perbuatannya	",
				"Oh pernah kerja di fitsa hats jg ya tuh onta..wkwkwkwk	",
				"Habib otaknya kok kotor,terus yg ngadih gelar tu siapa ya?nama mukimin aja di tambahi novel,luuuccuuuuuuuuu....	",
				"Jelas sekali  orang ini   gaptek... Jadinya dia  tidak tahu kalau  apa yg dia ucapkan bisa di telusuri ..... Kalau ternyata  dia bohong ... Maka  tamatlah  kesaksian dia.... Tamatlah  FPI....	",
				"Kalau hakim tidak dalam tekanan ato pro slah satu pihak dan pakai hati nurani krm hra pertanggung jawabkan ke Alloh sebagai wakil nya di dunia.	",
				"Mklum org arab kgak fasih ngmg p....payah jd fayah...pizza jd fitza...hhhh	",
				"Mklum org arab kgak fasih ngmg P....payah jd fayah...pizza jd fitza...hhhh	",
				"Segera terapi psikologis bisa diobatin kok. Mumpung belum gila akut seperti babib jijik	"
		};
		
		for(int i = 0 ; i < data.length ; i ++) {
			List<String> result = tokenize(data[i]);
			System.out.println(Util.stringList(result));
			System.out.println("\n\n");
		}
	}
	
	public static void stanford_nlp() {
		String paragraph = "Muslim membela kafir...muslim yang mana itu....Munafik	";
		Reader reader = new StringReader(paragraph);
		DocumentPreprocessor dp = new DocumentPreprocessor(reader);
		List<String> sentenceList = new ArrayList<String>();

		for (List<HasWord> sentence : dp) {
		   String sentenceString = SentenceUtils.listToString(sentence);
		   for(HasWord word: sentence) {
			   System.out.println(word);
		   }
		   sentenceList.add(sentenceString);
		}

		for (String sentence : sentenceList) {
		   System.out.println(sentence);
		}
	}

}

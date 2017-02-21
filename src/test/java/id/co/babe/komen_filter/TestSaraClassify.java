package id.co.babe.komen_filter;

import id.co.babe.sara.classify.SaraCommentAPI;
import id.co.babe.sara.filter.model.Komen;

import java.util.Map;

import cc.mallet.classify.Classification;
import cc.mallet.classify.Classifier;
import cc.mallet.types.Label;

public class TestSaraClassify {
	public static void checkClassifier() {
		Classifier classifier;

		try {
			classifier = SaraCommentAPI.loadClassifier("maxent_classifier.data");
			
			
			
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
				System.out.println("  " + Komen.NORMAL + ":"  + result.get(Komen.NORMAL) + "    --   " + Komen.SARA + ":" + result.get(Komen.SARA));
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
				System.out.println("  " + Komen.NORMAL + ":"  + result.get(Komen.NORMAL) + "    --   " + Komen.SARA + ":" + result.get(Komen.SARA));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		

		
	}
}

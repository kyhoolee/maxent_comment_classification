package id.co.babe.sara.filter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;


import id.co.babe.sara.classify.nlp.NlpRuleFilter;
import id.co.babe.sara.filter.model.Komen;
import id.co.babe.sara.filter.model.KomenDataset;

public class DataReader {
	
	public static final String SOURCE = "/home/mainspring/tutorial/learn/text-classifier/data/label_komen";
	
    private static final char DEFAULT_SEPARATOR = ',';
    private static final char DEFAULT_QUOTE = '"';
    
    public static void main(String[] args) {
    	//String komen = "good_komen.txt";
    	//String revised = "false_negative_revised.txt";
    	//filterKomen(komen, revised);
    	//filterKomen("revised_sara_comments.txt");
    	//filterKomen("202_sara.txt");
    	//filterKomenTarget("work_data/sara_komen.txt", "work_data/212_sara_komen.txt");
    	//separateData("work_data/train_sara_komen.txt", "work_data/test_sara_komen.txt", 0.8);
    	//separateData("work_data/train_non_sara_komen.txt", "work_data/test_non_sara_komen.txt", 0.8);
    	
    	//ruleFilter("work_data/tr_non_sara_komen.txt", "work_data/false_tr_non_sara_komen.txt", "work_data/true_tr_non_sara_komen.txt");
    	//ruleFilter("work_data/test_non_sara_komen.txt", "work_data/false_test_non_sara_komen.txt", "work_data/true_test_non_sara_komen.txt");
    	//filterKomenTarget("work_data/242_data/sara.txt", "work_data/242_data/review_sara.txt");
    	//filterKomenTarget("work_data/242_data/non.txt", "work_data/242_data/review_non.txt");
//    	filterWord("/home/mainspring/tutorial/learn/text-classifier/spell_correction/data/done.csv",
//    			"/home/mainspring/tutorial/learn/text-classifier/spell_correction/data/undone.csv",
//    			"/home/mainspring/tutorial/learn/text-classifier/spell_correction/data/review_undone.csv");
    	readRootDict("nlp_data/indo_dict/comment_dict.csv", "nlp_data/indo_dict/root_dict.csv");
    	
    }
    
    public static void readStopDict(String path, String stop_path) {
    	List<String> lines = TextfileIO.readFile(path);
    	List<String> stops = new ArrayList<>();
    	for(String line : lines) {
    		List<String> tokens = parseLine(line);
//    		String r = "";
//    		for(String t : tokens) {
//    			r = r + " -- " + t;
//    		}
//    		System.out.println(r);
    		String is_stop = tokens.get(3);
    		if(is_stop.equals("0"))
    			stops.add(tokens.get(0));
    		
    	}
    	
    	TextfileIO.writeFile(stop_path, stops);
    }
    
    public static void readRootDict(String path, String root_path) {
    	List<String> lines = TextfileIO.readFile(path);
    	List<String> rootList = new ArrayList<>();
    	for(String line : lines) {
    		List<String> tokens = parseLine(line);
//    		String r = "";
//    		for(String t : tokens) {
//    			r = r + " -- " + t;
//    		}
//    		System.out.println(r);
    		String is_stop = tokens.get(3);
    		if(!is_stop.equals("0")) {
    			//root.add(tokens.get(0));
    			String word = tokens.get(0);
    			String correct = tokens.get(3);
    			String root = tokens.get(2);
    			if(root!=null && !root.isEmpty()) {
    				rootList.add(word+","+root);
    			} else if(correct != null && !correct.isEmpty()) {
    				rootList.add(word+","+correct);
    			} else {
    				rootList.add(word+","+word);
    			}
    		}
    		
    	}
    	
    	TextfileIO.writeFile(root_path, rootList);
    }
    
    public static Set<String> wordSet(List<String> data) {
    	Set<String> result = new HashSet<>();
    	
    	for(String line : data) {
    		
    		String word = line.split(",")[0];
    		result.add(word);
    	}
    	
    	return result;
    	
    }
    
    public static Map<String, String> wordMap(List<String> data) {
    	Map<String, String> result = new HashMap<>();
    	
    	for(String line : data) {
    		
    		String word = line.split(",")[0];
    		result.put(word, line);
    	}
    	
    	return result;
    	
    }
    
    
    
    public static void filterWord(String done_file, String undone_file, String review_undone) {
    	List<String> dones = TextfileIO.readFile(done_file);
    	List<String> undones = TextfileIO.readFile(undone_file);
    	
    	Set<String> done_set = wordSet(dones);
    	System.out.println(done_set.size());
    	Map<String, String> undone_map = wordMap(undones);
    	System.out.println(undone_map.size());
    	List<String> result = new ArrayList<>();
    	for(String w : undone_map.keySet()) {
    		
    		if(!done_set.contains(w)) {
    			result.add(undone_map.get(w));
    		}
    	}
    	
    	System.out.println(result.size());
    	
    	TextfileIO.writeFile(review_undone, result);
    	
    }
    
    public static void ruleFilter(String inputFile, String outputSara, String outputNon) {
    	List<String> data = TextfileIO.readFile(inputFile);
    	
    	List<String> sara = new ArrayList<String>();
    	List<String> non = new ArrayList<String>();
    	for(String k : data) {
    		if(NlpRuleFilter.ruleInference(k).equals(Komen.SARA)) {
    			sara.add(k);
    		} else {
    			non.add(k);
    		}
    	}
    	
    	TextfileIO.writeFile(outputSara, sara);
    	TextfileIO.writeFile(outputNon, non);
    } 
    
    
    public static void separateData(String source, String target, double keep_percent) {
    	List<String> data = TextfileIO.readFile(source);
    	
    	List<String> remain = new ArrayList<>();
    	List<String> separate = new ArrayList<>();
    	
    	for(String line : data) {
    		double rand = Math.random();
			
			if(keep_percent < rand) {
				remain.add(line);
			} else {
				separate.add(line);
			}
    	}
    	
    	TextfileIO.writeFile(source, remain);
    	TextfileIO.writeFile(target, separate);
    }
    
    
    public static void checkKomen() {
    	List<Komen> komens = tokenFilter(readKomensCsv(SOURCE + "/label_komen2.csv"));
    	int spam = 0;
    	for(int i = 0 ; i < komens.size() ; i ++) {
    		Komen k = komens.get(i);
    		RuleFilter.regexMatch(k.content);
    		if(k.label == Komen.SARA) {
    			//System.out.println(k.content);
    			
    			spam ++;
    		}
    		
    	}
    	
    	System.out.println("Total: " + komens.size() + " -- Spam: " + spam + " -- Normal: " + (komens.size() - spam));
    	
    	
    	
    }
    
    
    public static void filterKomen(String komen_file, String revised_file) {
    	List<String> komen = TextfileIO.readFile(komen_file);
    	List<String> revised = TextfileIO.readFile(revised_file);
    	
    	HashSet<String> set_komen = new HashSet<>();
    	HashSet<String> set_revised = new HashSet<>();
    	
    	set_komen.addAll(komen);
    	set_revised.addAll(revised);
    	
    	System.out.println(komen.size());
    	System.out.println(set_komen.size() + " -- " + set_revised.size());
    	
    	for(String r : set_revised) {
    		set_komen.remove(r);
    	}
    	
    	System.out.println(set_komen.size());
    	
    	List<String> result = new ArrayList<String>();
    	result.addAll(set_komen);
    	
    	TextfileIO.writeFile("revised_" + komen_file, result);
    }
    
    public static void filterKomen(String komen_file) {
    	List<String> komen = TextfileIO.readFile(komen_file);
    	
    	HashSet<String> set_komen = new HashSet<>();
    	
    	set_komen.addAll(komen);
    	
    	System.out.println(komen.size());
    	System.out.println(set_komen.size());
    	
    	
    	System.out.println(set_komen.size());
    	
    	List<String> result = new ArrayList<String>();
    	result.addAll(set_komen);
    	
    	TextfileIO.writeFile("revised_" + komen_file, result);
    }
    
    
    public static void filterKomenTarget(String komen_file, String target_file) {
    	List<String> komen = TextfileIO.readFile(komen_file);
    	
    	HashSet<String> set_komen = new HashSet<>();
    	
    	set_komen.addAll(komen);
    	
    	System.out.println(komen.size());
    	System.out.println(set_komen.size());
    	
    	
    	System.out.println(set_komen.size());
    	
    	List<String> result = new ArrayList<String>();
    	result.addAll(set_komen);
    	
    	TextfileIO.writeFile(target_file, result);
    }
    
    public static KomenDataset buildSample(KomenDataset dataset, String name, double train_prob) {
    	String filePath = SOURCE + "/" + name;
    	return buildData(dataset, tokenFilter(readKomensCsv(filePath)), train_prob);
    }
    
    public static KomenDataset buildData(KomenDataset dataset, List<Komen> data, double train_prob) {
    	
    	dataset.updateData(data, train_prob);
    	
    	return dataset;
    }
    
    public static List<Komen> tokenFilter(List<Komen> data) {
    	List<Komen> result = new ArrayList<>();
    	
    	for(int i = 0 ; i < data.size() ; i ++) {
    		Komen komen = data.get(i);
    		
    		//komen.content = komen.content.replaceAll("[.,]"," ").replace("\\n", " ");
    				//replaceAll("[^\\w\\s]","");
    		
    		result.add(komen);
    	}
    	
    	return result;
    }
    
    public static List<Komen> readSaraKomens(String filePath) {
    	return readKomens(filePath, Komen.SARA);
    }
    
    public static List<Komen> readNormalKomens(String filePath) {
    	return readKomens(filePath, Komen.NORMAL);
    }
    
    
    public static List<Komen> readKomens(String filePath, String label) {
    	List<Komen> result = new ArrayList<>();
    	
    	List<String> data = TextfileIO.readFile(filePath);
    	for(int i = 0 ; i < data.size() ; i ++) {
    		String content = data.get(i);
    		if(content != null && !content.isEmpty()) {
	    		Komen k = new Komen(content, label);
	    		result.add(k);
    		}
    	}
    	
    	return result;
    }

    public static List<Komen> readKomensCsv(String filePath) {
    	List<Komen> result = new ArrayList<Komen>();
    	
    	try {
	
	        Scanner scanner = new Scanner(new File(filePath));
	        int count = 0;
	        while (scanner.hasNext()) {
	            List<String> line = parseLine(scanner.nextLine());
	            
	            if(line.size() >= 2) {
	            	try {
	            		Komen komen = new Komen(line.get(0), line.get(1));
	            		result.add(komen);
	            	} catch (Exception e) {
	            		
	            	}
	            }
	            count ++;
	        }
	        System.out.println(count);
	        scanner.close();
        
    	} catch (Exception e) {
    		
    	}
        
        return result;

    }

    public static List<String> parseLine(String cvsLine) {
        return parseLine(cvsLine, DEFAULT_SEPARATOR, DEFAULT_QUOTE);
    }

    public static List<String> parseLine(String cvsLine, char separators) {
        return parseLine(cvsLine, separators, DEFAULT_QUOTE);
    }

    public static List<String> parseLine(String cvsLine, char separators, char customQuote) {
        List<String> result = new ArrayList<>();
        //if empty, return!
        if (cvsLine == null || cvsLine.isEmpty()) {
            return result;
        }
        if (customQuote == ' ') {
            customQuote = DEFAULT_QUOTE;
        }
        if (separators == ' ') {
            separators = DEFAULT_SEPARATOR;
        }
        StringBuffer curVal = new StringBuffer();
        boolean inQuotes = false;
        boolean startCollectChar = false;
        boolean doubleQuotesInColumn = false;

        char[] chars = cvsLine.toCharArray();
        for (char ch : chars) {
            if (inQuotes) {
                startCollectChar = true;
                if (ch == customQuote) {
                    inQuotes = false;
                    doubleQuotesInColumn = false;
                } else {
                    //Fixed : allow "" in custom quote enclosed
                    if (ch == '\"') {
                        if (!doubleQuotesInColumn) {
                            curVal.append(ch);
                            doubleQuotesInColumn = true;
                        }
                    } else {
                        curVal.append(ch);
                    }
                }
            } else {
                if (ch == customQuote) {
                    inQuotes = true;
                    //Fixed : allow "" in empty quote enclosed
                    if (chars[0] != '"' && customQuote == '\"') {
                        curVal.append('"');
                    }
                    //double quotes in column will hit this!
                    if (startCollectChar) {
                        curVal.append('"');
                    }
                } else if (ch == separators) {
                    result.add(curVal.toString());
                    curVal = new StringBuffer();
                    startCollectChar = false;
                } else if (ch == '\r') {
                    //ignore LF characters
                    continue;
                } else if (ch == '\n') {
                    //the end, break!
                    break;
                } else {
                    curVal.append(ch);
                }
            }
        }
        result.add(curVal.toString());
        return result;
    }

}

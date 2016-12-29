package id.co.babe.filter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import id.co.babe.filter.model.Komen;
import id.co.babe.filter.model.KomenDataset;

public class DataReader {
	
	public static final String SOURCE = "/home/mainspring/tutorial/learn/text-classifier/data/label_komen";
	
    private static final char DEFAULT_SEPARATOR = ',';
    private static final char DEFAULT_QUOTE = '"';
    
    public static void main(String[] args) {
    	
    	List<Komen> komens = tokenFilter(readKomensCsv(SOURCE + "/label_komen2.csv"));
    	int spam = 0;
    	for(int i = 0 ; i < komens.size() ; i ++) {
    		Komen k = komens.get(i);
    		RuleFilter.regexMatch(k.content);
    		if(k.label == Komen.SPAM) {
    			//System.out.println(k.content);
    			
    			spam ++;
    		}
    		
    	}
    	
    	System.out.println("Total: " + komens.size() + " -- Spam: " + spam + " -- Normal: " + (komens.size() - spam));
    	
    	
    	
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
    
    public static List<Komen> readSpamKomens(String filePath) {
    	return readKomens(filePath, Komen.SPAM);
    }
    
    public static List<Komen> readNormalKomens(String filePath) {
    	return readKomens(filePath, Komen.NORMAL);
    }
    
    
    public static List<Komen> readKomens(String filePath, String label) {
    	List<Komen> result = new ArrayList<>();
    	
    	List<String> data = TextfileIO.readFile(filePath);
    	for(int i = 0 ; i < data.size() ; i ++) {
    		String content = data.get(i);
    		//content = content.replace("\",\"", " ");
    		Komen k = new Komen(content, label);
    		
    		result.add(k);
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

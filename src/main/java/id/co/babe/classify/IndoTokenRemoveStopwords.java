package id.co.babe.classify;

import cc.mallet.pipe.TokenSequenceRemoveStopwords;

public class IndoTokenRemoveStopwords extends TokenSequenceRemoveStopwords {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public IndoTokenRemoveStopwords() {
		super(false);
		
		super.addStopWords(WordTokenizer.getStopWords().toArray(new String[0]));
		
	}

}

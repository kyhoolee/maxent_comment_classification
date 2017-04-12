package id.co.babe.sara.classify;

import id.co.babe.sara.classify.nlp.NlpRuleFilter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import cc.mallet.pipe.Pipe;
import cc.mallet.types.Instance;
import cc.mallet.types.Token;
import cc.mallet.types.TokenSequence;

public class TokenSequence2Gram extends Pipe implements Serializable
{
	int [] gramSizes = null;
    
	public TokenSequence2Gram (int [] sizes)
	{
		this.gramSizes = sizes;
	}
	
	public Instance pipe (Instance carrier)
	{
		String newTerm = null;
		TokenSequence tmpTS = new TokenSequence();
		TokenSequence ts = (TokenSequence) carrier.getData();

		for (int i = 0; i < ts.size(); i++) {
			Token t = ts.get(i);
			for(int j = 0; j < gramSizes.length; j++) {
				int len = gramSizes[j];
				if (len <= 0 || len > (i+1)) continue;
				if (len == 1) { tmpTS.add(t); continue; }
				newTerm = new String(t.getText());
				//for(int k = 1; k < len; k++)
				int k = len -1;
				//if(NlpRuleFilter.checkDict(newTerm)) {
				
					newTerm = ts.get(i-k).getText() + "_" + newTerm;
					tmpTS.add(newTerm);
				//}
			}
		}

		carrier.setData(tmpTS);

		return carrier;
	}

	// Serialization 
	
	private static final long serialVersionUID = 1;
	private static final int CURRENT_SERIAL_VERSION = 0;
	
	private void writeObject (ObjectOutputStream out) throws IOException {
		out.writeInt (CURRENT_SERIAL_VERSION);
		out.writeInt (gramSizes.length);
		for (int i = 0; i < gramSizes.length; i++)
			out.writeInt (gramSizes[i]);
	}
	
	private void readObject (ObjectInputStream in) throws IOException, ClassNotFoundException {
		int version = in.readInt ();
		int size = in.readInt();
		gramSizes = new int[size];
		for (int i = 0; i < size; i++)
			gramSizes[i] = in.readInt();
	}

}
package id.co.babe.sara.filter;

import id.co.babe.sara.filter.model.Komen;
import id.co.babe.sara.util.Util;

public class CommentFilter {

	public static String ruleInference(String content) {
		String res = Komen.NORMAL;

		content = Util.filter(content);
		if (res == Komen.NORMAL) {
			res = RuleFilter.ruleSpam(content);
		} 
		
		if(res == Komen.SARA) {
			res = RuleFilter.ruleNormal(content);
		}

		return res;
	}
}

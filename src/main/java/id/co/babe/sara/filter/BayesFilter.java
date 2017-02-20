package id.co.babe.sara.filter;

import id.co.babe.sara.classifier.bayes.BayesClassifier;
import id.co.babe.sara.store.FileDB;
import id.co.babe.sara.store.SqlDB;

public class BayesFilter extends BayesClassifier<String, String> {
	public static final String ROOT = "/home/mainspring/tutorial/learn/text-classifier/data/";
	public BayesFilter() {
		super();
	}
	
	public void saveModel() {
		FileDB.saveCategoryCount(this.totalCategoryCount, ROOT + "model_category_count.csv");
		FileDB.saveFeatureCount(this.totalFeatureCount, ROOT + "model_feature_count.csv");
		FileDB.saveCategoryFeatureCount(this.featureCountPerCategory, ROOT + "model_category_feature_count.csv");
		
	}
	
	public void loadModel() {
		this.reset();
		this.featureCountPerCategory = FileDB.loadCategoryFeatureCount(ROOT + "model_category_feature_count.csv");
		this.totalCategoryCount = FileDB.loadCategoryCount(ROOT + "model_category_count.csv");
		this.totalFeatureCount = FileDB.loadFeatureCount(ROOT + "model_feature_count.csv");
	}
	

}

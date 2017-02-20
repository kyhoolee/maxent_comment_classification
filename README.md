## Synopsis

This is Sara comment classification project. The main purpose of this project is to classify Sara comments. The machine learning using to classify these comments from normal comments is Maximum-Entropy.

## Code Example
Three main API

- Build and save classifier

Build classifier from good_data and bad_data then save to classifier_file
@param bad_file
@param good_file
@param train_percent
@param classifier_file

public static Classifier buildClassifier(String bad_file, String good_file,double train_percent, String classifier_file)

- Load classifier 
Load classifier object from classifier_file

Load classifier from file
@param classifier_path
@return Classifier

public static Classifier loadClassifier(String classifier_path)

- Classify comment

Classify comment by classifier
@param classifier
@param input
@return 0 - Sara and 1 - Normal

public static String classify(Classifier classifier, String input) 

- Classify comment with confident threshold

Classify comment to sara / normal / undefined
@param c - classifier
@param input - comment to classify
@param normal_threshold - Confident threshold to make sure a comment is normal
@param sara_threshold - Confident threshold to make sure a comment is sara
@return

public static String classifyConfident(Classifier c, String input, double normal_threshold, double sara_threshold) 

- Classify comment with default confident threshold

Classify comment to sara / normal / undefined with default threshold
@param c - classifier
@param input - comment to classify
@return

public static String classifyConfident(Classifier c, String input)

## Motivation

Famous model for text classification is Naive-Bayes, Maximum Entropy, SVM.
The main problem to solve with classification is Overfiting and Underfiting.
## Tests

Sample code and evaluation in id.co.babe.classify.KomenClassification

## License

A short snippet describing the license (MIT, Apache, etc.)

package id.co.babe.store;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.io.ICsvListReader;
import org.supercsv.io.ICsvMapWriter;
import org.supercsv.prefs.CsvPreference;

public class FileDB {

	private static CellProcessor[] categoryFeatureCount() {
		final CellProcessor[] processors = new CellProcessor[] { new Optional(), // category
				new Optional(), // feature
				new Optional(), // count

		};

		return processors;
	}

	private static CellProcessor[] categoryCount() {
		final CellProcessor[] processors = new CellProcessor[] { new Optional(), // category
				new Optional(), // count

		};

		return processors;
	}

	private static CellProcessor[] featureCount() {
		final CellProcessor[] processors = new CellProcessor[] { new Optional(), // feature
				new Optional(), // count

		};

		return processors;
	}

	private static void readWithCsvListReader(String filePath) throws Exception {

		ICsvListReader listReader = null;
		try {
			listReader = new CsvListReader(new FileReader(filePath), CsvPreference.STANDARD_PREFERENCE);
			listReader.getHeader(true);
			final CellProcessor[] processors = categoryFeatureCount();

			List<Object> categoryFeature;
			while ((categoryFeature = listReader.read(processors)) != null) {
				System.out.println(String.format("lineNo=%s, rowNo=%s, category=%s, feature=%s, count=%s",
						listReader.getLineNumber(), listReader.getRowNumber(), categoryFeature.get(0),
						categoryFeature.get(1), categoryFeature.get(2)));
			}

		} finally {
			if (listReader != null) {
				listReader.close();
			}
		}
	}

	public static void main(String[] args) {
		try {
			readWithCsvListReader(
					"/home/mainspring/tutorial/learn/text-classifier/data/model_category_feature_count.csv");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Dictionary<String, Dictionary<String, Integer>> loadCategoryFeatureCount(String filePath) {
		Hashtable<String, Dictionary<String, Integer>> result = new Hashtable<>();

		ICsvListReader listReader = null;
		try {
			listReader = new CsvListReader(new FileReader(filePath), CsvPreference.STANDARD_PREFERENCE);
			listReader.getHeader(true);
			final CellProcessor[] processors = categoryFeatureCount();

			List<Object> categoryFeature;
			while ((categoryFeature = listReader.read(processors)) != null) {
//				System.out.println(String.format("lineNo=%s, rowNo=%s, category=%s, feature=%s, count=%s",
//						listReader.getLineNumber(), listReader.getRowNumber(), categoryFeature.get(0),
//						categoryFeature.get(1), categoryFeature.get(2)));
				try {
					String category = categoryFeature.get(0).toString();
					String feature = categoryFeature.get(1).toString();
					int count = Integer.parseInt(categoryFeature.get(2).toString());
					// System.out.print(feature + " :: " + count);
					if (category != null && feature != null) {
						if (result.contains(category)) {
							Dictionary<String, Integer> catFeaCount = result.get(category);
							if (catFeaCount != null) {
								catFeaCount.put(feature, count);
							} else {
								catFeaCount = new Hashtable<>();
								catFeaCount.put(feature, count);
							}
							result.put(category, catFeaCount);
						} else {
							Dictionary<String, Integer> catFeaCount = new Hashtable<>();
							catFeaCount.put(feature, count);
							result.put(category, catFeaCount);
						}
					}
				} catch (Exception ex) {
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (listReader != null) {
				try {
					listReader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return result;
	}

	public static void saveCategoryFeatureCount(Dictionary<String, Dictionary<String, Integer>> catFeaCount,
			String filePath) {

		ICsvMapWriter mapWriter = null;
		try {
			mapWriter = new CsvMapWriter(new FileWriter(filePath), CsvPreference.STANDARD_PREFERENCE);
			final CellProcessor[] processors = categoryFeatureCount();
			final String[] header = new String[] { "category", "feature", "count" };
			mapWriter.writeHeader(header);

			int i = 0;
			for (String category : ((Hashtable<String, Dictionary<String, Integer>>) catFeaCount).keySet()) {
				Dictionary<String, Integer> feaCount = catFeaCount.get(category);

				if (feaCount != null) {
					for (String feature : ((Hashtable<String, Integer>) feaCount).keySet()) {
						// insertCategoryFeatureCount(category, feature,
						// feaCount.get(feature));
						int count = feaCount.get(feature);

						final Map<String, Object> line = new HashMap<String, Object>();
						line.put(header[0], category);
						line.put(header[1], feature);
						line.put(header[2], count);
						mapWriter.write(line, header, processors);
						i++;
						if (i % 100 == 0) {
							System.out.println(
									i + " -- Category-feature-count: " + category + " :: " + feature + " :: " + count);
						}
					}
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (mapWriter != null) {
				try {
					mapWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static Dictionary<String, Integer> loadCategoryCount(String filePath) {
		Dictionary<String, Integer> result = new Hashtable<>();

		ICsvListReader listReader = null;
		try {
			listReader = new CsvListReader(new FileReader(filePath), CsvPreference.STANDARD_PREFERENCE);
			listReader.getHeader(true);
			final CellProcessor[] processors = categoryCount();

			List<Object> categoryCount;
			while ((categoryCount = listReader.read(processors)) != null) {
				System.out
						.println(String.format("lineNo=%s, rowNo=%s, category=%s, count=%s", listReader.getLineNumber(),
								listReader.getRowNumber(), categoryCount.get(0), categoryCount.get(1)));
				try {
					String category = categoryCount.get(0).toString();
					int count = Integer.parseInt(categoryCount.get(1).toString());
					// System.out.print(category + " :: " + count);
					result.put(category, count);
				} catch (Exception ex) {
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (listReader != null) {
				try {
					listReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return result;
	}

	public static void saveCategoryCount(Dictionary<String, Integer> categoryCount, String filePath) {
		ICsvMapWriter mapWriter = null;
		try {

			mapWriter = new CsvMapWriter(new FileWriter(filePath), CsvPreference.STANDARD_PREFERENCE);
			final CellProcessor[] processors = categoryCount();
			final String[] header = new String[] { "category", "count" };
			mapWriter.writeHeader(header);

			int i = 0;
			for (String category : ((Hashtable<String, Integer>) categoryCount).keySet()) {

				int count = categoryCount.get(category);
				final Map<String, Object> line = new HashMap<String, Object>();
				line.put(header[0], category);
				line.put(header[1], count);
				mapWriter.write(line, header, processors);

				i++;
				if (i % 100 == 0) {
					System.out.println(i + " category-count: " + category + " :: " + count);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (mapWriter != null) {
				try {
					mapWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static Dictionary<String, Integer> loadFeatureCount(String filePath) {
		Dictionary<String, Integer> result = new Hashtable<>();

		ICsvListReader listReader = null;
		try {
			listReader = new CsvListReader(new FileReader(filePath), CsvPreference.STANDARD_PREFERENCE);
			listReader.getHeader(true);
			final CellProcessor[] processors = featureCount();
			
			List<Object> featureCount;
			while ((featureCount = listReader.read(processors)) != null) {
//				System.out.println(String.format("lineNo=%s, rowNo=%s, feature=%s, count=%s",
//						listReader.getLineNumber(), listReader.getRowNumber(), featureCount.get(0),
//						featureCount.get(1)));
				try {
					String feature = featureCount.get(0).toString();
					int count = Integer.parseInt(featureCount.get(1).toString());
					// System.out.print(category + " :: " + count);
					result.put(feature, count);
				} catch (Exception ex) {}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (listReader != null) {
				try {
					listReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return result;
	}

	public static void saveFeatureCount(Dictionary<String, Integer> featureCount, String filePath) {
		ICsvMapWriter mapWriter = null;
		try {

			mapWriter = new CsvMapWriter(new FileWriter(filePath), CsvPreference.STANDARD_PREFERENCE);
			final CellProcessor[] processors = featureCount();
			final String[] header = new String[] { "feature", "count" };
			mapWriter.writeHeader(header);

			int i = 0;
			for (String feature : ((Hashtable<String, Integer>) featureCount).keySet()) {

				int count = featureCount.get(feature);
				final Map<String, Object> line = new HashMap<String, Object>();
				line.put(header[0], feature);
				line.put(header[1], count);
				mapWriter.write(line, header, processors);
				i++;
				if (i % 100 == 0) {
					System.out.println(i + " Feature-Count: " + feature + " :: " + count);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (mapWriter != null) {
				try {
					mapWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}

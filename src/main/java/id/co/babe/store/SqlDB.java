package id.co.babe.store;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;


import id.co.babe.util.ConfigParams;

public class SqlDB {
	
	public static Dictionary<String, Dictionary<String, Integer>> loadCategoryFeatureCount() {
    	Hashtable<String, Dictionary<String, Integer>> result = new Hashtable<>();
    	
    	try {
            Connection connection = getConnection();
            Statement stmt = connection.createStatement();
            String select = "SELECT category, feature, count FROM category_feature_count";
            ResultSet rs = stmt.executeQuery(select);
            while (rs.next()) {
            	String category = rs.getString("category");
            	String feature = rs.getString("feature");
            	int count = rs.getInt("count");
                //System.out.print(feature + " :: " + count);
                
                if(result.contains(category)) {
                	Dictionary<String, Integer> catFeaCount = result.get(category);
                	if(catFeaCount != null) {
                		catFeaCount.put(feature, count);
                	} else {
                		catFeaCount = new Hashtable<>();
                		catFeaCount.put(feature, count);
                	}
                	result.put(category, catFeaCount);
                } {
                	Dictionary<String, Integer> catFeaCount = new Hashtable<>();
                	catFeaCount.put(feature, count);
                	result.put(category, catFeaCount);
                }
                
            } 
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    	
    	return result;
    }
	
	public static void saveCategoryFeatureCount(Dictionary<String, Dictionary<String, Integer>> catFeaCount) {
		try {
			Connection connection = getConnection();
            String query = " insert into category_feature_count (category, feature, count) values (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            
            int i = 0;
            for(String category : ((Hashtable<String, Dictionary<String,Integer>>) catFeaCount).keySet()) {
            	Dictionary<String, Integer> feaCount = catFeaCount.get(category);
            	
            	if(feaCount != null) {
	            	for(String feature : ((Hashtable<String, Integer>)feaCount).keySet()) {
		                // insertCategoryFeatureCount(category, feature, feaCount.get(feature));
	            		int count = feaCount.get(feature);
	            		statement.setString(1, category);
	                    statement.setString(2, feature);
	                    statement.setInt(3, count);
	                    statement.addBatch();
	                    i++;
	                    if(i % 100 == 0) {
	                    	System.out.println(i + " -- Category-feature-count: " + category + " :: " + feature + " :: " + count);
	                    }
	            	}
            	}
            	
            	
            }
            
            statement.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public static void insertCategoryFeatureCount(String category, String feature, int count) {
		try {
            Connection connection = getConnection();
            String query = " insert into category_feature_count (category, feature, count) values (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            
            statement.setString(1, category);
            statement.setString(2, feature);
            statement.setInt(3, count);
            statement.execute();

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
	
	public static Dictionary<String, Integer> loadCategoryCount() {
    	Dictionary<String, Integer> result = new Hashtable<>();
    	
    	try {
            Connection connection = getConnection();
            Statement stmt = connection.createStatement();
            String select = "SELECT category, count FROM category_count";
            ResultSet rs = stmt.executeQuery(select);
            while (rs.next()) {
            	String category = rs.getString("category");
            	int count = rs.getInt("count");
                //System.out.print(category + " :: " + count);
                result.put(category, count);
            } 
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    	
    	return result;
    }
	
	public static void saveCategoryCount(Dictionary<String, Integer> categoryCount) {
		
		try {
			Connection connection = getConnection();
            String query = " insert into category_count (category, count) values (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            
            int i = 0;
            for(String category : ((Hashtable<String, Integer>) categoryCount).keySet()) {
            	//System.out.println();
            	//insertCategoryCount(category, categoryCount.get(category));
            	int count = categoryCount.get(category);
            	statement.setString(1, category);
                statement.setInt(2, count);
                statement.addBatch();
                
                i++;
                if( i % 100 == 0) {
                	System.out.println(i + " category-count: " + category + " :: " + count);
                }
            }
            statement.executeBatch();
            
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public static void insertCategoryCount(String category, int count) {
		try {
            Connection connection = getConnection();
            String query = " insert into category_count (category, count) values (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            
            statement.setString(1, category);
            statement.setInt(2, count);
            statement.execute();

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
	
	public static Dictionary<String, Integer> loadFeatureCount() {
    	Dictionary<String, Integer> result = new Hashtable<>();
    	
    	try {
            Connection connection = getConnection();
            Statement stmt = connection.createStatement();
            String select = "SELECT feature, count FROM feature_count";
            ResultSet rs = stmt.executeQuery(select);
            while (rs.next()) {
            	String feature = rs.getString("feature");
            	int count = rs.getInt("count");
                System.out.print(feature + " :: " + count);
                result.put(feature, count);
            } 
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    	
    	return result;
    }
	
	public static void saveFeatureCount(Dictionary<String, Integer> featureCount) {
		try {
			
			Connection connection = getConnection();
            String query = " insert into feature_count (feature, count) values (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            
            int i = 0;
            for(String feature : ((Hashtable<String, Integer>) featureCount).keySet()) {
            	//System.out.println(feature + " -- " + featureCount.get(feature));
            	//insertFeatureCount(feature, featureCount.get(feature));
            	
                int count = featureCount.get(feature);
                statement.setString(1, feature);
                statement.setInt(2, count);
                
                statement.addBatch();
                i ++;
                if(i % 100 == 0) {
                	System.out.println(i + " Feature-Count: " + feature + " :: " + count);
                }
            }
            
            statement.executeBatch();
            
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public static void insertFeatureCount(String feature, int count) {
		try {
            Connection connection = getConnection();
            String query = " insert into feature_count (feature, count) values (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            
            statement.setString(1, feature);
            statement.setInt(2, count);
            statement.execute();

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
	
 
    public static Set<String> getBlackSet() {
    	Set<String> result = new HashSet<>();
    	
        try {
            Connection connection = getConnection();
            Statement stmt = connection.createStatement();
            String select = "SELECT word FROM black_word";
            ResultSet rs = stmt.executeQuery(select);
            while (rs.next()) {
            	String word = rs.getString("word");
                System.out.print(word);
                result.add(word);
            } 
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    	
    	return result;
    }
    
    public static void setBlackSet(Set<String> wordSet) {
    	try {
            Connection connection = getConnection();
            String query = " insert into black_word (word, category) values (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            
            int i = 0;
            for(String word : wordSet) {
                statement.setString(1, word);
                statement.setInt(2, 0);
                statement.addBatch();
                i++;
                if(i % 1000 == 0 || i == wordSet.size()) {
                    statement.executeBatch(); // Execute every 1000 items.
                }
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setBlack(String word) {
    	try {
            Connection connection = getConnection();
            String query = " insert into black_word (word, category) values (?, ?)";
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setString(1, word);
            preparedStmt.setInt(2, 0);
            preparedStmt.execute();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void deleteBlack(String word) {
    	try {
            Connection connection = getConnection();
            String query = " delete from black_word where word='" + word + "'";
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.execute();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	
    public static void createSchema() throws SQLException {
        Connection connection = getConnection();
        Statement stmt = connection.createStatement();
    	
        stmt.executeUpdate("DROP TABLE IF EXISTS category_feature_count");
        stmt.executeUpdate("create table category_feature_count "
        		+ "(id int auto_increment, category varchar(255), feature varchar(255), count int, "
        		+ "primary key (id) );"); //, unique key category_feature (category, feature));");
        
        stmt.executeUpdate("DROP TABLE IF EXISTS feature_count");
        stmt.executeUpdate("create table feature_count "
        		+ "(id int auto_increment, feature varchar(255), count int, "
        		+ "primary key (id) );"); //, unique key (feature));");
        
        stmt.executeUpdate("DROP TABLE IF EXISTS category_count");
        stmt.executeUpdate("create table category_count "
        		+ "(id int auto_increment, category varchar(255), count int, "
        		+ "primary key (id));");//, unique key (category));");
        
        
        /*
        CREATE TABLE black_word (
        	id INT AUTO_INCREMENT,
        	word VARCHAR(255) ,
        	category INT ,
        	PRIMARY KEY (id),
        	UNIQUE KEY (word)
        ) ;
        */
        stmt.executeUpdate("DROP TABLE IF EXISTS black_word");
        stmt.executeUpdate("create table black_word "
        		+ "(id int auto_increment, word varchar(255), category int, "
        		+ "primary key (id), "
        		+ "unique key (word));");
        
        
        
        connection.close();
    }

    public static Connection getConnection() throws SQLException {
    	ConfigParams.load();
        String host = ConfigParams.sql_host;
        String port = ConfigParams.sql_port;
        String database = ConfigParams.sql_database;
        String username = ConfigParams.sql_username;
        String password = ConfigParams.sql_password;
        String dbUrl = "jdbc:mysql://" + host + ":" + port + "/" +database;
        return DriverManager.getConnection(dbUrl, username, password);
    }

    
    public static void main(String[] args) {
    	try {
			createSchema();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	//ConfigParams.load();
    }
}

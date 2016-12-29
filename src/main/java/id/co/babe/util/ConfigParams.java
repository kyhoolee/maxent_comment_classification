package id.co.babe.util;

import java.util.Properties;


public class ConfigParams {
	public static final String CONFIG_PATH = "config.properties";
	
	public static String sql_host = "localhost_";
    public static String sql_port = "3306_";
    public static String sql_database = "komen_filter_";
    public static String sql_username = "root_";
    public static String sql_password = "maingames_";
	
	private static Properties properties = null;
	
	public static void load() {
		if(properties == null) {
			try {
				properties = new Properties();
				properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(CONFIG_PATH));
				
				sql_host = properties.getProperty("sql_host");
				sql_port = properties.getProperty("sql_port");
				sql_database = properties.getProperty("sql_database");
				sql_username = properties.getProperty("sql_username");
				sql_password = properties.getProperty("sql_password");
				
				System.out.println(sql_host + " " + sql_port + " " + sql_database + " " + sql_username + " " + sql_password);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}

}
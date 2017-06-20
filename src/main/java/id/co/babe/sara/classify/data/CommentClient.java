package id.co.babe.sara.classify.data;

import id.co.babe.sara.filter.DataReader;
import id.co.babe.sara.filter.TextfileIO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

public class CommentClient {
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL =  "jdbc:mysql://10.2.15.2:3306/babe";

	// Database credentials
	static final String USER = "babe";
	static final String PASS = "!!babe!!";
			
	
	/*
1 = Active
0 = Review
-1 = SPAM Comment
-2 = SARA Comment
-3 = Keyword Filtered
-4 = Banned User
	 */
	
	public static final int normal = 1;
	public static final int review = 0;
	public static final int spam = -1;
	public static final int sara = -2;
	public static final int word = -3;
	public static final int user = -4;
	
	public static void main(String[] args) {
		writeData();
		//getNewCommentsByDay(sara, 0, 0);
		//summary();
		
		//System.out.println(countUpdateCommentByDay(sara, sara, 1, 0));
	}
	
	public static void writeData() {
		List<String> data = getNewCommentsByDate(spam, "2017-05-11", "2017-05-01");
		TextfileIO.writeFile("work_data/db_data/spam.11.5.txt", data); // 13358
	}
	
	public static void summary() {
		
		normalSummary();
		System.out.println();
		reviewSummary();
		System.out.println();
		saraSummary();
	}
	
	public static void normalSummary() {
		System.out.println("Total normal: " + countComment(normal));
		System.out.println("origin: " + countNonupdateComment(normal));
		System.out.println("sara: " + countUpdateComment(normal, sara));
		System.out.println("---------------------------------------");
	}
	
	public static void reviewSummary() {
		System.out.println("Total review: " + countComment(review));
		System.out.println("non-update: " + countNonupdateComment(review));
		System.out.println("normal: " + countUpdateComment(review, normal));
		System.out.println("sara: " + countUpdateComment(review, sara));
		System.out.println("----------------------------------------");
	}
	
	
	
	public static void saraSummary() {
		System.out.println("Total sara: " + countComment(sara));
		System.out.println("non-update: " + countNonupdateComment(sara));
		System.out.println("normal: " + countUpdateComment(sara, normal));
		System.out.println("----------------------------------------");
	}
	
	

	

	
	public static int countComment(int origin) {
		String query = "select count(*) as total from tbl_comment_changelog where original_sts = " + origin + " order by id desc ; ";
		return countSql(query);
		
	}
	
	public static int countCommentByDay(int origin, int previousDay, int range) {
		String query = "select count(*) as total from tbl_comment_changelog where original_sts = " + origin 
				+ " and date(created) <= curdate() - " + previousDay
				+ " and date(created) >= curdate() - " + (previousDay + range)
				+ " order by id desc ; ";
		return countSql(query);
		
	}
	
	public static int countCommentByDay(int origin, int previousDay) {
		String query = "select count(*) as total from tbl_comment_changelog where original_sts = " + origin 
				+ " and date(created) <= curdate() - " + previousDay
				+ " order by id desc ; ";
		return countSql(query);
		
	}
	
	
	public static int countNewCommentByDay(int update) {
		String query = "select count(*) as total from tbl_comment_changelog where new_sts = " + update + " order by id desc ; ";
		return countSql(query);
	}
	
	
	
	public static int countNonupdateCommentByDay(int origin, int previousDay) {
		String query = "select count(*) as total from tbl_comment_changelog where " 
				+ " original_sts = " + origin + " and new_sts is null " 
				+ " and date(created) <= curdate() - " + previousDay
				+ " order by id desc ; ";
		return countSql(query);
		
	}
	
	public static int countUpdateCommentByDay(int origin, int update, int previousDay) {
		String query = "select count(*) as total from tbl_comment_changelog where original_sts = " + origin + " and new_sts = " + update 
				+ " and date(created) <= curdate() - " + previousDay
				+ " order by id desc ; ";
		return countSql(query);
		
	}
	
	public static int countCommentsByDay(int type, int previousDay) {
		String query = "select count(*) as total from tbl_comment_changelog where new_sts = " + type 
				+ " and date(created) <= curdate() - " + previousDay
				+ " order by id desc ; ";
		return countSql(query);
		
	}
	
	
	public static int countNonupdateCommentByDay(int origin, int previousDay, int range) {
		String query = "select count(*) as total from tbl_comment_changelog where " 
				+ " original_sts = " + origin + " and new_sts is null " 
				+ " and date(created) <= curdate() - " + previousDay
				+ " and date(created) >= curdate() - " + (previousDay + range)
				+ " order by id desc ; ";
		return countSql(query);
		
	}
	
	public static int countUpdateCommentByDay(int origin, int update, int previousDay, int range) {
		String query = "select count(*) as total from tbl_comment_changelog where original_sts = " + origin + " and new_sts = " + update 
				+ " and date(created) <= curdate() - " + previousDay
				+ " and date(created) >= curdate() - " + (previousDay + range)
				+ " order by id desc ; ";
		return countSql(query);
		
	}
	
	public static int countCommentsByDay(int type, int previousDay, int range) {
		String query = "select count(*) as total from tbl_comment_changelog where new_sts = " + type 
				+ " and date(created) <= curdate() - " + previousDay
				+ " and date(created) >= curdate() - " + (previousDay + range)
				+ " order by id desc ; ";
		return countSql(query);
		
	}
	
	
	
	public static int countNonupdateComment(int origin) {
		String query = "select count(*) as total from tbl_comment_changelog where original_sts = " + origin + " and new_sts is null order by id desc ; ";
		return countSql(query);
		
	}
	
	public static int countNewComment(int update) {
		String query = "select count(*) as total from tbl_comment_changelog where new_sts = " + update + " order by id desc ; ";
		return countSql(query);
	}
	
	public static int countUpdateComment(int origin, int update) {
		String query = "select count(*) as total from tbl_comment_changelog where original_sts = " + origin + " and new_sts = " + update + " order by id desc ; ";
		return countSql(query);
		
	}
	
	public static int countComments(int type) {
		String query = "select count(*) as total from tbl_comment_changelog where new_sts = " + type + " order by id desc ; ";
		return countSql(query);
		
	}
	
	public static int countSql(String query) {
		int result = 0;
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			String sql;
			sql = query;
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				int total = rs.getInt("total");
				result = total;
			}
			
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return result;
	}
	
	
	
	

	public static List<String> getCommentsByDay(int origin, int previousDay) {
	      
		String sql = "select *, from_base64(content) ct " 
				+ "from tbl_comment_changelog inner join sasha_article_comment on "
				+ " tbl_comment_changelog.id = sasha_article_comment.id "
				+ " where original_sts = " + origin 
				+ " and date(tbl_comment_changelog.created) <= curdate() - " + previousDay
				+ " order by tbl_comment_changelog.id desc ; ";
		
		List<String> result = getComments(sql);
		return result;
	}
	
	public static List<String> getNonupdateCommentsByDay(int origin, int previousDay) {
	      
		String sql = "select *, from_base64(content) ct " 
				+ "from tbl_comment_changelog inner join sasha_article_comment on "
				+ " tbl_comment_changelog.id = sasha_article_comment.id "
				+ " where original_sts = " + origin 
				+ " and date(tbl_comment_changelog.created) <= curdate() - " + previousDay
				+ " and new_sts is null "
				+ " order by tbl_comment_changelog.id desc ; ";
		
		List<String> result = getComments(sql);
		return result;
	}
	
	public static List<String> getUpdateCommentsByDay(int origin, int update, int previousDay) {
	      
		String sql = "select *, from_base64(content) ct " 
				+ "from tbl_comment_changelog inner join sasha_article_comment on "
				+ " tbl_comment_changelog.id = sasha_article_comment.id "
				+ " where original_sts = " + origin + " and new_sts = " + update
				+ " and date(tbl_comment_changelog.created) <= curdate() - " + previousDay
				+ " order by tbl_comment_changelog.id desc ; ";
		
		List<String> result = getComments(sql);
		return result;
	}
	
	public static List<String> getNewCommentsByDay(int update, int previousDay) {
	      
		String sql = "select *, from_base64(content) ct " 
				+ "from tbl_comment_changelog inner join sasha_article_comment on "
				+ " tbl_comment_changelog.id = sasha_article_comment.id "
				+ " where new_sts = " + update
				+ " and date(tbl_comment_changelog.created) <= curdate() - " + previousDay
				+ " order by tbl_comment_changelog.id desc ; ";
		
		List<String> result = getComments(sql);
		return result;
	}
	
	
	
	
	
	
	public static List<String> getCommentsByDay(int origin, int previousDay, int range) {
	      
		String sql = "select *, from_base64(content) ct " 
				+ "from tbl_comment_changelog inner join sasha_article_comment on "
				+ " tbl_comment_changelog.id = sasha_article_comment.id "
				+ " where original_sts = " + origin 
				+ " and date(tbl_comment_changelog.created) <= curdate() - " + previousDay
				+ " and date(tbl_comment_changelog.created) > curdate() - " + (previousDay + range)
				+ " order by tbl_comment_changelog.id desc ; ";
		
		List<String> result = getComments(sql);
		return result;
	}
	
	public static List<String> getNonupdateCommentsByDay(int origin, int previousDay, int range) {
	      
		String sql = "select *, from_base64(content) ct " 
				+ "from tbl_comment_changelog inner join sasha_article_comment on "
				+ " tbl_comment_changelog.id = sasha_article_comment.id "
				+ " where original_sts = " + origin 
				+ " and date(tbl_comment_changelog.created) <= curdate() - " + previousDay
				+ " and date(tbl_comment_changelog.created) > curdate() - " + (previousDay + range)
				+ " and new_sts is null "
				+ " order by tbl_comment_changelog.id desc ; ";
		
		List<String> result = getComments(sql);
		return result;
	}
	
	public static List<String> getUpdateCommentsByDay(int origin, int update, int previousDay, int range) {
	      
		String sql = "select *, from_base64(content) ct " 
				+ "from tbl_comment_changelog inner join sasha_article_comment on "
				+ " tbl_comment_changelog.id = sasha_article_comment.id "
				+ " where original_sts = " + origin + " and new_sts = " + update
				+ " and date(tbl_comment_changelog.created) <= curdate() - " + previousDay
				+ " and date(tbl_comment_changelog.created) > curdate() - " + (previousDay + range)
				+ " order by tbl_comment_changelog.id desc ; ";
		
		List<String> result = getComments(sql);
		return result;
	}
	
	public static List<String> getUpdateCommentsByDate(int origin, int update, String bigDate, String smallDate) {
	      
		String sql = "select *, from_base64(content) ct " 
				+ "from tbl_comment_changelog inner join sasha_article_comment on "
				+ " tbl_comment_changelog.id = sasha_article_comment.id "
				+ " where original_sts = " + origin + " and new_sts = " + update
				+ " and date(tbl_comment_changelog.created) <= '" + bigDate + "' "
				+ " and date(tbl_comment_changelog.created) > '" + smallDate + "' "
				+ " order by tbl_comment_changelog.id desc ; ";
		
		List<String> result = getComments(sql);
		return result;
	}
	
	public static List<String> getNewCommentsByDay(int update, int previousDay, int range) {
	      
		String sql = "select *, from_base64(content) ct " 
				+ "from tbl_comment_changelog inner join sasha_article_comment on "
				+ " tbl_comment_changelog.id = sasha_article_comment.id "
				+ " where new_sts = " + update
				+ " and date(tbl_comment_changelog.created) <= curdate() - " + previousDay
				+ " and date(tbl_comment_changelog.created) > curdate() - " + (previousDay + range)
				+ " order by tbl_comment_changelog.id desc ; ";
		
		List<String> result = getComments(sql);
		return result;
	}
	
	
	
	public static List<String> getNewCommentsByDate(int update, String bigDate, String smallDate) {
	      
		String sql = "select *, from_base64(content) ct " 
				+ "from tbl_comment_changelog inner join sasha_article_comment on "
				+ " tbl_comment_changelog.id = sasha_article_comment.id "
				+ " where new_sts = " + update
				+ " and date(tbl_comment_changelog.created) <= '" + bigDate + "' "
				+ " and date(tbl_comment_changelog.created) > '" + smallDate + "' "
				+ " order by tbl_comment_changelog.id desc ; ";
		
		List<String> result = getComments(sql);
		return result;
	}
	
	
	
	
	
	
	public static List<String> getComments(int origin) {
	      
		String sql = "select *, from_base64(content) ct " 
				+ "from tbl_comment_changelog inner join sasha_article_comment on "
				+ " tbl_comment_changelog.id = sasha_article_comment.id "
				+ " where original_sts = " + origin + " order by tbl_comment_changelog.id desc ; ";
		
		List<String> result = getComments(sql);
		return result;
	}
	
	public static List<String> getNonupdateComments(int origin) {
	      
		String sql = "select *, from_base64(content) ct " 
				+ "from tbl_comment_changelog inner join sasha_article_comment on "
				+ " tbl_comment_changelog.id = sasha_article_comment.id "
				+ " where original_sts = " + origin + " and new_sts is null "
				+ " order by tbl_comment_changelog.id desc ; ";
		
		List<String> result = getComments(sql);
		return result;
	}
	
	public static List<String> getUpdateComments(int origin, int update) {
	      
		String sql = "select *, from_base64(content) ct " 
				+ "from tbl_comment_changelog inner join sasha_article_comment on "
				+ " tbl_comment_changelog.id = sasha_article_comment.id "
				+ " where original_sts = " + origin + " and new_sts = " + update + " order by tbl_comment_changelog.id desc ; ";
		
		List<String> result = getComments(sql);
		return result;
	}
	
	public static List<String> getNewComments(int update) {
	      
		String sql = "select *, from_base64(content) ct " 
				+ "from tbl_comment_changelog inner join sasha_article_comment on "
				+ " tbl_comment_changelog.id = sasha_article_comment.id "
				+ " where new_sts = " + update + " order by tbl_comment_changelog.id desc ; ";
		
		List<String> result = getComments(sql);
		return result;
	}
	
	public static List<String> getComments(String sql) {
		
		List<String> result = new ArrayList<String>();
		
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				String content = rs.getString("ct");
				Timestamp created = rs.getTimestamp("tbl_comment_changelog.created");

				if(content != null && !content.isEmpty()) {
					content = content.replace("\n", "\t").replace("\r", ""); // created.toString() + " : " + 
					result.add(content);
					System.out.println(created + " -- " + content);
				}
			}
			
			System.out.println("total: " + result.size());
			
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		
		return result;
	}
}

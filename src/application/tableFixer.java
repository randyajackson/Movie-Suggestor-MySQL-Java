package application;

import java.sql.*;
import java.util.*;
import java.lang.Math;

public class tableFixer {
	   // JDBC driver name and database URL
	   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	   static final String DB_URL = "jdbc:mysql://localhost:3306?zeroDateTimeBehavior=convertToNull";

	   //  Database credentials
	   static final String USER = "root";
	   static final String PASS = "nine0four";
	   
	   
	   public static void main(String[] args) {
	   Connection conn = null;
	   Statement stmt = null;
	   ResultSet rs = null;
	   
	   try{
		 //STEP 2: Register JDBC driver
		      Class.forName("com.mysql.jdbc.Driver");

		      //STEP 3: Open a connection
		      conn = DriverManager.getConnection(DB_URL, USER, PASS);
		      
		      //STEP 4: Execute a query

		      
		      stmt = conn.createStatement();
		      String sql = null;
		      
		      sql = "use Final;";
		      //________________________________________________________________________Create Movie Table
		      stmt.executeUpdate(sql);
		      
		     /** sql = "CREATE TABLE IF NOT EXISTS MoviesEXPORT   " +
		    		  "(iterator_id INT, " +
	                    "genres VARCHAR(1000), " +
	                    "keywords VARCHAR(1000), " +
	                    "original_title VARCHAR(1000), " +
	                    "popularity FLOAT, " +
	                    "vote_average FLOAT, " +
	                    "vote_count FLOAT);";
	      
		      stmt.executeUpdate(sql);
		      
		      sql = "LOAD DATA LOCAL INFILE 'C:/Users/Randy/Desktop/database/Final Project/moviesUSE.csv' INTO TABLE Movies CHARACTER SET latin1 FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\\r\\n';"; 
		      
		      stmt.executeUpdate(sql);**/
		      
		    //________________________________________________________________________Variables for queries
		      int totalTuples = 0;
		      int iterator_id = 0;
		      String keywords = null;
		      String genres = null;
		      String original_title = null;
		      float popularity = 0;
		      float vote_average = 0;
		      float vote_count = 0;
		      
		      

		      int totalKeywords = 0;
		      int totalGenres = 0;

		      //_______________________________________________________________________Query for info about searched movie
		   	  sql = "select count(*) from Movies;";
		      rs = stmt.executeQuery(sql);
	  		   while (rs.next())
	  		   {
	  			   totalTuples = rs.getInt("count(*)");
	  		   } 
	  		   
			      sql = "CREATE TABLE IF NOT EXISTS Movies2   " +
			    		  "(iterator_id INT, " +
		                    "genres VARCHAR(1000), " +
		                    "keywords VARCHAR(1000), " +
		                    "original_title VARCHAR(1000), " +
		                    "popularity FLOAT, " +
		                    "vote_average FLOAT, " +
		                    "vote_count FLOAT);";
		      stmt.executeUpdate(sql);
		      
			for(int i = 1; i < totalTuples; i++)
	  		  {
				System.out.println(i);
				
		   	  sql = "select iterator_id, genres, keywords, original_title, popularity, vote_average, vote_count from Movies where iterator_id ='" + i + "' ;";
		      rs = stmt.executeQuery(sql);
		   		   
		   		   while (rs.next())
		   		   {
		   			   iterator_id = rs.getInt("iterator_id");
		   			   genres = rs.getString("genres");
		   			   keywords = rs.getString("keywords");
		   		       original_title = rs.getString("original_title");
		   		       popularity = rs.getFloat("popularity");
		   		       vote_average = rs.getFloat("vote_average");
		   		       vote_count = rs.getFloat("vote_count");
		   		   }  
		   		       
		   		       
		   		      
		   	  		   if(keywords != null && genres != null)
		   	  		   {
		   	  			String[] keywordsArray = keywords.split(",");
		   	   			String[] genreArray = genres.split(",");
		   	   			
		   	   				if(keywordsArray.length > 12)
		   	   				{
		   	   				int entry = 0;
		   	   				
		   	   				if(keywordsArray.length >= genreArray.length)
		   	   				entry = keywordsArray.length;
		   	   				else
		   	   				entry = genreArray.length;
		   	   				
		   	   				int keylength = keywordsArray.length;
		   	   				int genlength = genreArray.length;
		   	   				
		   	   				for(int p = 0; p < entry; p++)
		   	   				{
		   	   					
		   	   					if(p >= keylength)
		   	   					{
		   	   						
		   	   						sql = "INSERT INTO Movies2(iterator_id, genres, keywords, original_title, popularity, vote_average, vote_count) VALUES (?, ?, ?, ?, ?, ?, ?)";
		   	   						PreparedStatement pstmt = conn.prepareStatement(sql);
		   	   						pstmt.setInt(1, iterator_id);  
		   	   						pstmt.setString(2, genreArray[p]);
		   	   						pstmt.setString(3, "empty");
		   	   						pstmt.setString(4, original_title);
		   	   						pstmt.setFloat(5, popularity);
		   	   						pstmt.setFloat(6, vote_average);
		   	   						pstmt.setFloat(7, vote_count);
		   	   						
		   	   						int test = pstmt.executeUpdate();	
		   	   					}
		   	   					else if(p >= genlength)
		   	   					{
		   	   						sql = "INSERT INTO Movies2(iterator_id, genres, keywords, original_title, popularity, vote_average, vote_count) VALUES (?, ?, ?, ?, ?, ?, ?)";
		   	   						PreparedStatement pstmt = conn.prepareStatement(sql);
		   	   						pstmt.setInt(1, iterator_id);  
		   	   						pstmt.setString(2, "empty");
		   	   						pstmt.setString(3, keywordsArray[p]);
		   	   						pstmt.setString(4, original_title);
		   	   						pstmt.setFloat(5, popularity);
		   	   						pstmt.setFloat(6, vote_average);
		   	   						pstmt.setFloat(7, vote_count);
		   	   						
		   	   						int test = pstmt.executeUpdate();
		   	   						
		   	   					}
		   	   					else
		   	   					{
		   	   						sql = "INSERT INTO Movies2(iterator_id, genres, keywords, original_title, popularity, vote_average, vote_count) VALUES (?, ?, ?, ?, ?, ?, ?)";
		   	   						PreparedStatement pstmt = conn.prepareStatement(sql);
		   	   						pstmt.setInt(1, i);  
		   	   						pstmt.setString(2, genreArray[p]);
		   	   						pstmt.setString(3, keywordsArray[p]);
		   	   						pstmt.setString(4, original_title);
		   	   						pstmt.setFloat(5, popularity);
		   	   						pstmt.setFloat(6, vote_average);
		   	   						pstmt.setFloat(7, vote_count);
		   	   						
		   	   						int test = pstmt.executeUpdate();	
		   	   						
		   	   					}
		   	   					
		   	   				}
		   	   				
		   	   				}
		   	  		   }
		   		   }
		   		   
		   		   //System.out.println(iterator_id + " " + genres + " " + keywords + " " + original_title + " " + popularity + " "+ vote_average + " " + vote_count);
	  		  
	   }
	   catch(SQLException se){
		      //Handle errors for JDBC
		      se.printStackTrace();
		   }catch(Exception e){
		      //Handle errors for Class.forName
		      e.printStackTrace();
		   }finally{
		      //finally block used to close resources
		      try{
		         if(stmt!=null)
		            conn.close();
		      }catch(SQLException se){
		      }// do nothing
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		   }//end try
	   
}}


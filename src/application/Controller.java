package application;
import java.net.URL;
import java.sql.*;
import java.util.*;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.*;


public class Controller implements Initializable
{
	// JDBC driver name and database URL
	   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	   static final String DB_URL = "jdbc:mysql://localhost:3306?zeroDateTimeBehavior=convertToNull";

	   //  Database credentials
	   static final String USER = "root";
	   static final String PASS = "nine0four";
	   
	   static String backPass;
	   static String movieList[];
	   static String[] suggestions;
	   static Boolean suggestPass = true;
	   static String output;
	   
    @FXML
    private TextField search;

    @FXML
    private TextArea results;
    
    @FXML
    private void handleAction(ActionEvent e) 
    {
    	if(suggestPass)
    	{
    	results.clear();
    	
        String text = search.getText();
        suggestions = suggest(text);
        
        if(suggestions.length != 0)
        output = "Which result for " + text + ".\n";
        else
        output = "No results for " + text + ".\n";
        
        for(int i = 0; i< suggestions.length; i++)
        {
        	output += (i+1) + ": " + suggestions[i] + "\n";
        }
        
        new Thread(() -> results.insertText(0, output)).start();
        
        if(suggestions.length == 0)
        suggestPass = true;
        else
        suggestPass = false;
        
        search.clear();
    	}
    	else
    	{
    	
        String text = search.getText();
        
        new Thread(() -> results.insertText(0, search(suggestions[Integer.parseInt(text) - 1]))).start();
        
        suggestPass = true;
        
        results.clear();
        search.clear();
    	}
    }
    
    public String[] suggest(String search)
    {
       Connection conn = null;
  	   Statement stmt = null;
  	   ResultSet rs = null;	
  	   
  	   try 
  	   {
   		 //STEP 2: Register JDBC driver
		      Class.forName("com.mysql.jdbc.Driver");

		      //STEP 3: Open a connection
		      conn = DriverManager.getConnection(DB_URL, USER, PASS);
		      
		      //STEP 4: Execute a query

		      
		      stmt = conn.createStatement();
		      
		      String sql = "create database if not exists Final;";
		      
		      stmt.executeUpdate(sql);
		      
		      sql = "use Final;";
		      //________________________________________________________________________Create Movie Table
		      stmt.executeUpdate(sql);
		      
		      sql = "CREATE TABLE IF NOT EXISTS movies" +
		    		  "(iterator_id INT, " +
	                    "genres VARCHAR(1000), " +
	                    "keywords VARCHAR(1000), " +
	                    "original_title VARCHAR(1000), " +
	                    "popularity FLOAT, " +
	                    "vote_average FLOAT, " +
	                    "vote_count FLOAT)";
		      stmt.executeUpdate(sql);
	      
	      //_______________________________________________________________________________Load Data into Movie Table
	      int resultTotal;
	 	  sql = "select count(*) COUNT from movies;";
  			   
					   rs = stmt.executeQuery(sql);
					   rs.next();
					   resultTotal = rs.getInt("COUNT");
		  
	      if(resultTotal < 1)		   
		  {sql = "LOAD DATA LOCAL INFILE 'movies5.csv' INTO TABLE Movies CHARACTER SET latin1 FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\\n' IGNORE 1 LINES;"; 

	      stmt.executeUpdate(sql);}
	      
 	      sql = "select count(distinct original_title) COUNT from movies where original_title LIKE '%" + search + "%'  ;";
	   			   
				   rs = stmt.executeQuery(sql);
				   rs.next();
				   resultTotal = rs.getInt("COUNT");
				   movieList = new String[resultTotal];
				   int i = 0;
						   
 	   	  sql = "select distinct original_title from movies where original_title LIKE '%" + search + "%' order by original_title ASC;"; 
 	      rs = stmt.executeQuery(sql);
 	   		   
 	   		   while (rs.next())
 	   		   {
 	   			   movieList[i] = rs.getString("original_title");
 	   			   i++;
 	   		   }
	      
	      
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
  	   
  	   return movieList;
    	
    }
    
    public String search(String search)
    {
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
 		      
 		      String sql = "create database if not exists Final;";
 		      
 		      stmt.executeUpdate(sql);
 		      
 		      sql = "use Final;";
 		      //________________________________________________________________________Create Movie Table
 		      stmt.executeUpdate(sql);
 		      
 		      sql = "CREATE TABLE IF NOT EXISTS movies" +
 		    		  "(iterator_id INT, " +
 	                    "genres VARCHAR(1000), " +
 	                    "keywords VARCHAR(1000), " +
 	                    "original_title VARCHAR(1000), " +
 	                    "popularity FLOAT, " +
 	                    "vote_average FLOAT, " +
 	                    "vote_count FLOAT)";
 		      stmt.executeUpdate(sql);
 	      
 	      //_______________________________________________________________________________Load Data into Movie Table
 	      

 		      int resultTotal;
 		 	  sql = "select count(*) COUNT from movies;";
 	  			   
 						   rs = stmt.executeQuery(sql);
 						   rs.next();
 						   resultTotal = rs.getInt("COUNT");
 			  
 		      if(resultTotal < 1)		   
 			  {sql = "LOAD DATA LOCAL INFILE 'movies5.csv' INTO TABLE Movies CHARACTER SET latin1 FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\\n' IGNORE 1 LINES;"; 
 			  stmt.executeUpdate(sql);}
 	      
 	      
 	      //________________________________________________________________________Variables for queries
 	      
 	      int iterator_id = 0;
 	      String keywords = null;
 	      String genres = null;
 	      String original_title = null;
 	      
 	      String inputTitle = search;
 	      
 	      List<String> rowValues = new ArrayList<String>();
 	      String[] keywordsArray;
 	      String[] genreArray;
 	      int totalKeywords = 0;
 	      int totalGenres = 0;
 	      
 	      //_______________________________________________________________________Query for info about searched movie
 	      
 	   	  sql = "select iterator_id from movies where original_title ='" + inputTitle + "' ;"; // get id
 	      rs = stmt.executeQuery(sql);
 	   		   
 	   		   while (rs.next())
 	   		   {
 	   			   iterator_id = rs.getInt("iterator_id");
 	   		   }
 	   	  
   		   	   
   		 	 sql = "select genres from movies where original_title ='" + inputTitle + "' ;"; // get genres
   			  rs = stmt.executeQuery(sql);
   			  
   	  		   	   while (rs.next())
   	  		       {
   	  		   		   genres = rs.getString("genres");
   	  		   		   if( !(genres.equals("empty")) )
   	  		   			   rowValues.add(genres);   
   	  		       }
   		   	   
   	  		   	   genreArray = rowValues.toArray(new String[rowValues.size()]);
   	  		   	   rowValues.clear();
   	  		   	   
   		   	  
   		   	   //______________________________________________________________________Total keywords and genres from mainMovie 
   		   	   
   		   	   
   		   	   //_______________________________________________________________________Creat mainMovie title object and insert

   		   	   title mainMovie = new title();
   		   	   mainMovie.setID(iterator_id);
   		   	   mainMovie.setGenreArray(genreArray);

   		   	   //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   		   	   //END obtaining of main search
   		   	   //BEGIN obtaining suggestions
   		   	   //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   		   	   
   		   	   String[] mainKeywordsArray = mainMovie.getKeywords();
   		   	   String[] mainGenresArray = mainMovie.getGenres();
   		   	   
   		   	   resultTotal = 0; // number of results that match the first genre
   		   	   
   		   	   //keywordsArray for temp
   		   	   //genreArray for temp
   		   	   
   		   	   //______________________________________________Query titles with genre #1 from main
   		   	   
   		   	   //______________________________________________Getting count for Creating Array of title objects
   		      List<Integer> idArray = new ArrayList<Integer>();
   		   	   
   		   	   sql = "select iterator_id from movies where genres='" + mainGenresArray[0] + "' AND iterator_id != " + mainMovie.getIteratorID() + ";";
   		   	   rs = stmt.executeQuery(sql);
   		   	   
   		   	   while( rs.next() )
   		   	   {idArray.add( rs.getInt("iterator_id") );}
    
   		   	   //______________________________________________Creating array of title objects for holding results
   		   	   
   		   	  title[] suggestedTitles = new title[idArray.size()];
   		   	   int titleCounter = 0;
   		   	   
   		    
   		  for(int i = 0; i < idArray.size() ; i++)
   		  {
   			  			  
   	  		  suggestedTitles[i] = new title();
   	  		  suggestedTitles[i].setID(idArray.get(i));
   	  		  

   			  
   		  }
   	   		   
   	   		   titleCounter = 0; // resetting titleCounter to 0 to count matching genres
   	   		   
   	   		   float totalVotes = 0;
 	  	   	   float totalRatingAverage = 0;
 	  	   	   
   	   		   //_______________________________________________________________________________________Counting number of matching Genres
   	   		   for(; titleCounter < suggestedTitles.length - 1; titleCounter++)
   	   		   {	   
   	   			   	   sql = "select count(*) COUNT from movies a inner join movies b on a.genres = b.genres where (a.iterator_id = " + mainMovie.getIteratorID() + " and b.iterator_id = " + suggestedTitles[titleCounter].getIteratorID() + ") and (a.genres != 'empty' and b.genres != 'empty');";
   	   			   
 	   				   rs = stmt.executeQuery(sql);
 	   				   rs.next();
 	   				   resultTotal = rs.getInt("COUNT");
 	   				   suggestedTitles[titleCounter].addToGenre(resultTotal); 
 	   				   
 	  	   			   sql = "select count(*) COUNT from movies a inner join movies b on a.keywords = b.keywords where (a.iterator_id = " + mainMovie.getIteratorID() + " and b.iterator_id = " + suggestedTitles[titleCounter].getIteratorID() + ") and (a.genres != 'empty' and b.genres != 'empty');";
 	  	   			   
 		   				   rs = stmt.executeQuery(sql);
 		   				   rs.next();
 		   				   resultTotal = rs.getInt("COUNT");
 		   				   suggestedTitles[titleCounter].addToKeyword(resultTotal);
 		   				
 		   				   suggestedTitles[titleCounter].computeEuclidean();
 		   				   
 		   	    		  sql = "select vote_count, vote_average from movies where iterator_id =" + suggestedTitles[titleCounter].getIteratorID() + " ;";
 		  	  	          rs = stmt.executeQuery(sql);
 		  	  	   		   
 		  	  	   		   while (rs.next())
 		  	  	   		   {
 		  	  	   			   totalVotes += rs.getFloat("vote_count");
 		  	  	   			   totalRatingAverage += rs.getFloat("vote_average");
 		  	  	   		   }
   	   			   
   	   		   }
   	   		   
   	   		   titleCounter = 0; // resetting titleCounter to 0 to count matching keywords
   	   		   
   	   		  

   	   		   //___________________________________________________________________________________Eliminate nulls from the array of suggestions
   	   		   //title[] finalSuggestion = condense(suggestedTitles);
   	   		   title[] finalSuggestion = suggestedTitles;

   	   		   //_________________________________________________________________________________Sorting highest to lowest Euclidean Values
   	   		   sortEuclidean(finalSuggestion);
   	   		   
   	   		   //_________________________________________________________________________________Narrowing to top 20 suggestions
   	   		   title[] twentySuggestions = new title[20];
   	   		   
   	   		   for(;titleCounter < twentySuggestions.length; titleCounter++)
   	   		   {
   	   			twentySuggestions[titleCounter] = finalSuggestion[titleCounter];
   	   		   }
   	   		   
   	   		   //_________________________________________________________________________________Getting names of top 20
   	   		   
   	 	   	  for(int i = 0; i < 20; i++)
   	 	   	  {
   	   		  sql = "select original_title from movies where iterator_id =" + twentySuggestions[i].getIteratorID() + " ;";
   	 	      rs = stmt.executeQuery(sql);
   	 	   		   
   	 	   		   while (rs.next())
   	 	   		   {
   	 	   			   original_title = rs.getString("original_title");
   	 	   		   }
   	 	   		   
   	 	   		   twentySuggestions[i].setMovieTitle(original_title); 
   	 	   	  }
   	 	   	  
   	 	   	  //___________________________________________________________________________________Computing Popularity Metric
   	 	   	  
   	 	   	  //(# votes / all votes) x AVG Rating + (1 - (# votes / all votes) ) x AVG of ALL Ratings   	  
   	 	   	  float indVotes = 0;
   	 	   	  float indAvg = 0;
   	 	   	  float popularity = 0;
   	 	   	  
   	 	   	  
   	 	   	  for(int i = 0; i < 20; i++)
   	 	   	  {
   	   		  sql = "select vote_count, vote_average from movies where iterator_id =" + twentySuggestions[i].getIteratorID() + " ;";
   	 	      rs = stmt.executeQuery(sql);
   	 	   		   
   	 	   		   while (rs.next())
   	 	   		   {
   	 	   			   indVotes = rs.getFloat("vote_count");
   	 	   			   indAvg = rs.getFloat("vote_average");
   	 	   		   }
   	 	   	  
   	 	   		   popularity = ( (indVotes / totalVotes) * indAvg ) + ( (1 - (indVotes / totalVotes)) * totalRatingAverage );
   	 	   		   twentySuggestions[i].setTotalPopularity(popularity);
   	 	   	  }
   	 	   	  
   	 	   	   sortPopularity(twentySuggestions);
   	 	   	  
   	   		   backPass = "Top 5 Movie Suggestions For: " + inputTitle + "\n";
   	   		   
   	   		   
   	   		   String[] splitTitle = inputTitle.split("\\s+");

   	   		   int x = 5;
   	   		   int count = 1;
   	   		   
   	   		   
   	   		   for(int i = 0; i < x; i++)
   	   		   {  
   	   			   if( twentySuggestions[i].getMovieTitle().toLowerCase().contains(splitTitle[0].toLowerCase() ) )
   	   				   x++;
   	   			   else
   	   				   {backPass += count + ": "+ twentySuggestions[i].getMovieTitle() + "\n"; count++;}
   	   		   }
   	   		   
   	   		   System.out.println(backPass);
   	   		   return backPass;
   	  	   	   
    		
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
	return backPass;
    }
    
    static title[] condense(title[] arr)
	   {
		    ArrayList<title> list = new ArrayList<title>();
		    for (title s : arr){
		        if (s != null)
		            list.add(s);
		    }
		    arr = list.toArray(new title[list.size()]);
		    return arr;
		}
	   
	   static void sortPopularity(title[] array)
	     {
	          boolean noChange = true; // stop when a pass causes no change
	          for(int i = array.length; i > 0; i--)
	          {
	               noChange = true;
	               for(int j = 1; j < i; j++)
	               {
	            	   
	                    if(array[j].getTotalPopularity() > array[j - 1].getTotalPopularity())
	                    {
	                         swap(array, j, j - 1);
	                         noChange = false;
	                    }
	               } 
	               if (noChange)
	                    return; // sorted--no need to continue
	          }
	     }
	   
	   
	   static void sortEuclidean(title[] array)
	     {
	          boolean noChange = true; // stop when a pass causes no change
	          for(int i = array.length; i > 0; i--)
	          {
	               noChange = true;
	               for(int j = 1; j < i; j++)
	               {
	            	   
	                    if(array[j].getEuclidean() > array[j - 1].getEuclidean())
	                    {
	                         swap(array, j, j - 1);
	                         noChange = false;
	                    }
	               } 
	               if (noChange)
	                    return; // sorted--no need to continue
	          }
	     }

	     static void swap(Object[] array, int index1, int index2)
	     {
	          Object temp = array[index1];
	          array[index1] = array[index2];
	          array[index2] = temp;
	     }
	     
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	
	
}

//Title Object
class title
{
	   int iterator_id;
	   String[] keywords;
	   String[] genres;
	   
	   int genreCount;
	   int keywordCount;
	   
	   int mainTotalGenre;
	   int mainTotalKeyword;
	   
	   double totalEuclidean;
	   float totalPopularity;
	   
	   String movieTitle;
	   
	   public title()
	   {}
	   
	   public title(int iterator_id, String[] keywords, String[] genres, int mainTotalGenre, int mainTotalKeyword)
	   {
		   this.iterator_id = iterator_id;
		   this.keywords = keywords;
		   this.genres = genres;
		   this.mainTotalGenre = mainTotalGenre;
		   this.mainTotalKeyword = mainTotalKeyword;
	   }
	   
	   public void addEntry(int iterator_id, String[] keywords, String[] genres, int mainTotalGenre, int mainTotalKeyword)
	   {
		   this.iterator_id = iterator_id;
		   this.keywords = keywords;
		   this.genres = genres;
		   this.mainTotalGenre = mainTotalGenre;
		   this.mainTotalKeyword = mainTotalKeyword;
	   }
	
	   public void setTotalPopularity(float x)
	   {
		  totalPopularity = x;   
	   }
	   
	   public void setGenreArray(String[] x)
	   {
		   genres = x;
		   
	   }
	   
	   public float getTotalPopularity()
	   {
		   return totalPopularity;
	   }
	   
	   public void setMovieTitle(String movieTitle)
	   {
		  this.movieTitle = movieTitle; 
	   }
	   
	   public String getMovieTitle()
	   {
		   return movieTitle;
	   }
	   
	   public void computeEuclidean()
	   {
		   totalEuclidean = Math.sqrt(Math.pow( (mainTotalGenre - genreCount) , 2) + Math.pow( (mainTotalKeyword - keywordCount), 2));
		   
	   }
	   
	   public void setEuclidean(double x)
	   {
		   totalEuclidean = x;
	   }
	   
	   public double getEuclidean()
	   {
		   return totalEuclidean;
	   }
	   //COMPUTE POPULARITY
	   public int getGenreCount()
	   {
		  return genreCount; 
	   }
	   
	   public int getKeywordCount()
	   {
		  return keywordCount;
	   }
	   
	   public int getIteratorID()
	   {
		   return iterator_id;
	   }
	   public String[] getKeywords()
	   {
		   return keywords;
	   }
	   
	   public String[] getGenres()
	   {
		   return genres;
	   }
	   
	   public void addToGenre(int x)
	   {
		   genreCount = x;
	   }
	   
	   public void addToKeyword(int x)
	   {
		   keywordCount = x;
	   }
	   
	   public void setID(int x)
	   {
		   iterator_id = x;
	   }
	   
}


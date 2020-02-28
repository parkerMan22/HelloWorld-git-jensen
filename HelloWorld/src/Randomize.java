import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Randomize")
public class Randomize extends HttpServlet {
   private static final long serialVersionUID = 1L;

   public Randomize() {
      super();
   }

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      search(response);
   }

   void search(HttpServletResponse response) throws IOException {
	  String finalAdj = "ADJ";
	  String finalNoun = "NOUN";
	  String finalVerb = "VERB";
      response.setContentType("text/html");
      PrintWriter out = response.getWriter();
      String title = "Generated Idea!";
      String docType = "<!doctype html public \"-//w3c//dtd html 4.0 " + //
            "transitional//en\">\n"; //
      
      Connection connection = null;
      PreparedStatement preparedStatement = null;
      try {
         DBConnection.getDBConnection(getServletContext());
         connection = DBConnection.connection;

         //Get Random Noun from Noun Database
   	  	 Random random = new Random();
   	  	 int randomInteger = random.nextInt(59) + 1;
         String selectSQL = "SELECT * FROM Nouns";
         preparedStatement = connection.prepareStatement(selectSQL);
         ResultSet rs = preparedStatement.executeQuery();
         while (rs.next()) {//Loop through every row in table to get correct noun
            int id = rs.getInt("id");
            String noun = rs.getString("noun").trim();
            if (id == randomInteger) {
            	finalNoun = noun;
            }   
         }
         
         //Get Random Verb from Verb Database
   	  	 randomInteger = random.nextInt(59) + 1;
         selectSQL = "SELECT * FROM Verbs";
         preparedStatement = connection.prepareStatement(selectSQL);
         rs = preparedStatement.executeQuery();
         while (rs.next()) {//Loop through every row in table to get correct verb
            int id = rs.getInt("id");
            String verb = rs.getString("verb").trim();
            if (id == randomInteger) {
            	finalVerb = verb;
            }   
         }
         
         //Get Random Adjective from Adjective Database
   	  	 randomInteger = random.nextInt(64) + 1;
         selectSQL = "SELECT * FROM Adjective";
         preparedStatement = connection.prepareStatement(selectSQL);
         rs = preparedStatement.executeQuery();
         while (rs.next()) {//Loop through every row in table to get correct Adjective
            int id = rs.getInt("id");
            String adj = rs.getString("adjective").trim();
            if (id == randomInteger) {
            	finalAdj = adj;
            }   
         }
         
         //Output answer with HTML formatting
         String randomIdea = String.format("A(n) <h1>%s %s that can %s\n</h1>", finalAdj, finalNoun, finalVerb);
         out.println(docType + //
           	  "<html>"+
       	      "<head>"+
       	      "<style>"+
       	      "header {"+
       	          "background-color:#FFC78F;"+
       	          "color:#529CB3;"+
       	          "text-align:center;"+
       	          "padding:5px;"+	 
       	      "}"+
       	      "nav {"+
       	          "line-height:30px;"+
       	          "background-color:#8FE5FF;"+
       	          "height:300px;"+
       	          "width:100px;"+
       	          "float:left;"+
       	          "padding:5px;"+	      
       	      "}"+
       	      "section {"+
       	          "width:350px;"+
       	          "float:left;"+
       	          "padding:10px;"+	 	 
       	      "}"+
       	      "footer {"+
       	          "background-color:#FFC78F;"+
       	          "color:#529CB3;"+
       	          "clear:both;"+
       	          "text-align:center;"+
       	          "padding:5px;"+	 	 
       	      "}"+
       	      "</style>"+
       	      "</head>"+
       	
       	      "<body style='background-color:#8FE5FF;'>"+
       	      "<header>"+
       	      "<h1> Random Idea Generator </h1>"+
       	      "</header>"+

       	      "<section>"+
       	      	"<h1>" + randomIdea + "</h1>"+
       	      	"<form action='Randomize' method='POST'>"+
       	      		"<input type='submit' value='Generate Rondomized Idea' />"+
       	      	"</form>"+
       	      "</section>"+
       	      "<footer>"+
       	      "Copyright"+
       	      "</footer>"+
       	      "</body>"+
       	      "</html>");

         rs.close();
         preparedStatement.close();
         connection.close();
      } catch (SQLException se) {
         se.printStackTrace();
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         try {
            if (preparedStatement != null)
               preparedStatement.close();
         } catch (SQLException se2) {
         }
         try {
            if (connection != null)
               connection.close();
         } catch (SQLException se) {
            se.printStackTrace();
         }
      }
   }

   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      doGet(request, response);
   }

}

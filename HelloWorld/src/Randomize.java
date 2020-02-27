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
	  Random random = new Random();
	  int randomInteger = random.nextInt(59) + 1;
	  String finalAdj = "ADJ";
	  String finalNoun = "NOUN";
	  String finalVerb = "VERB";
	  System.out.println("randomInteger:" + randomInteger);
      response.setContentType("text/html");
      PrintWriter out = response.getWriter();
      String title = "Generated Idea!";
      String docType = "<!doctype html public \"-//w3c//dtd html 4.0 " + //
            "transitional//en\">\n"; //
      out.println(docType + //
            "<html>\n" + //
            "<head><title>" + title + "</title></head>\n" + //
            "<body bgcolor=\"#f0f0f0\">\n" + //
            "<h1 align=\"center\">" + title + "</h1>\n");

      Connection connection = null;
      PreparedStatement preparedStatement = null;
      try {
         DBConnection.getDBConnection(getServletContext());
         connection = DBConnection.connection;

         //Change selectSQL to different noun table
         String selectSQL = "SELECT * FROM Nouns";
         preparedStatement = connection.prepareStatement(selectSQL);

         ResultSet rs = preparedStatement.executeQuery();

         while (rs.next()) {//Loop through every row in table to get variables for noun
            int id = rs.getInt("id");
            String noun = rs.getString("noun").trim();
            if (id == randomInteger) {
            	finalNoun = noun;
            }   
         }
         
   	  	 randomInteger = random.nextInt(59) + 1;
         //Change selectSQL to different noun table
         selectSQL = "SELECT * FROM Verbs";
         preparedStatement = connection.prepareStatement(selectSQL);
         rs = preparedStatement.executeQuery();
         while (rs.next()) {//Loop through every row in table to get variables for noun
            int id = rs.getInt("id");
            String verb = rs.getString("verb").trim();
            if (id == randomInteger) {
            	finalVerb = verb;
            }   
         }
         
   	  	 randomInteger = random.nextInt(64) + 1;
         //Change selectSQL to different noun table
         selectSQL = "SELECT * FROM Adjective";
         preparedStatement = connection.prepareStatement(selectSQL);
         rs = preparedStatement.executeQuery();
         while (rs.next()) {//Loop through every row in table to get variables for noun
            int id = rs.getInt("id");
            String adj = rs.getString("adjective").trim();
            if (id == randomInteger) {
            	finalAdj = adj;
            }   
         }
         
         out.printf("%s %s that can %s\n", finalAdj, finalNoun, finalVerb);
         out.println("<a href=/HelloWorld/simpleFormSearch.html>\n\nRandomize</a> <br>");
         out.println("</body></html>");
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

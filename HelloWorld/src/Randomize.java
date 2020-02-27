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

         //Change selectSQL to different table types
         String selectSQL = "SELECT * FROM Nouns";
         preparedStatement = connection.prepareStatement(selectSQL);

         ResultSet rs = preparedStatement.executeQuery();

         while (rs.next()) {//Loop through every row in table to get variables
            int id = rs.getInt("id");
            String noun = rs.getString("noun").trim();

            //use variables
            //out.println("ID: " + id + ", ");
            //out.println("Noun: " + noun + ", ");
            
            if (id == randomInteger) {
                //out.println("COREECT ID: " + id + ", ");
                //out.println("Noun: " + noun + ", ");
            	out.printf("A ADJ %s that can VERB\n", noun);
            }
         }
         out.println("<a href=/HelloWorld/simpleFormSearch.html>Randomize</a> <br>");
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

package com.Accio;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet("/Search")
public class Search extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //getting keyword from frontend
        String keyword = request.getParameter("keyword");
        //setting up connection to database
        Connection connection = DatabaseConnection.getConnection();
        try {
            //store the query of user
            PreparedStatement preparedStatement = connection.prepareStatement("Insert into history values(?, ?);");
            preparedStatement.setString(1, keyword);
            preparedStatement.setString(2, "http://localhost:8080/SearchEngine/Search?keyword="+keyword);
            preparedStatement.executeUpdate();

            ResultSet resultSet = connection.createStatement().executeQuery("select pagetitle, pagelink, (length(lower(pageText))-length(replace(lower(pageText), '" + keyword.toLowerCase() + "', '')))/length('" + keyword.toLowerCase() + "') as countoccurence from pages order by countoccurence desc limit 30;");
            ArrayList<SearchResult> results = new ArrayList<>();

            //transferring values from resultset to results arraylist
            while (resultSet.next()) {
                SearchResult searchResult = new SearchResult();
                searchResult.setTitle(resultSet.getString("pageTitle"));
                searchResult.setLink(resultSet.getString("pageLink"));
                results.add(searchResult);
            }
            //displaying  results arraylist in console
            for (SearchResult result : results) {
                System.out.println(result.getTitle() + "\n" + result.getLink() + "\n");
            }
            request.setAttribute("results", results);
            request.getRequestDispatcher("search.jsp").forward(request, response);
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
        }
        catch (SQLException | ServletException sqlException) {
            sqlException.printStackTrace();
        }
    }
}

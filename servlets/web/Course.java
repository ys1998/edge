package servlets.web;
import servlets.common.DbHelper;
import servlets.common.Config;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Course
 */
@WebServlet("/Course")
public class Course extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Course() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	    response.setContentType("text/html");
	    PrintWriter out = response.getWriter();
/*	    Enumeration<String> params = request.getParameterNames(); 
	    while(params.hasMoreElements()){
	     String paramName = params.nextElement();
	     System.out.println("Parameter Name - "+paramName+", Value - "+request.getParameter(paramName));
	    }*/
	    
	    request.getRequestDispatcher("/course.jsp").include(request, response); 
	    //out.println("");

	    out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

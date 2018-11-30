package servlets.web;
import servlets.common.DbHelper;
import servlets.common.Config;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Servlet implementation class CheckAllAssigned
 */
@WebServlet("/CheckAllAssigned")
public class CheckAllAssigned extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckAllAssigned() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String test_id = request.getParameter("test_id");
		String course_id = request.getParameter("course_id");
		String semester = request.getParameter("semester");
		Integer year = Integer.valueOf(request.getParameter("year"));
		
		String query = "select count(*) "
				+ "from ans "
				+ "where course_id = ? and semester=? and year=? and test_id=? and grader is null";
		
		List<List<Object>> res = DbHelper.executeQueryList(query, 
				new DbHelper.ParamType[] {DbHelper.ParamType.STRING, DbHelper.ParamType.STRING, 
						DbHelper.ParamType.INT, DbHelper.ParamType.STRING}, 
				new Object[] {course_id, semester, year, test_id});
		
		ObjectMapper mapper = new ObjectMapper();
    	ObjectNode node = mapper.createObjectNode();
		
    	System.out.println(res);
    	
		if (Math.toIntExact((Long)res.get(0).get(0))==0) {
			node.put("go", "true");
		}
		else {
			node.put("go", "false");
		}
		response.getWriter().print(node.toString());
		//response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

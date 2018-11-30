package servlets.web;
import servlets.common.DbHelper;
import servlets.common.Config;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Servlet implementation class CourseMeta
 */
@WebServlet("/CourseMeta")
public class CourseMeta extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CourseMeta() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		HttpSession session = request.getSession();
		if(session.getAttribute("id") == null) { //not logged in
			response.getWriter().print(DbHelper.errorJson("Not logged in").toString());
			return;
		}
		
		String userid = (String) session.getAttribute("id");
		String course_id = request.getParameter("course_id");
		String semester = request.getParameter("semester");
		Integer year = Integer.valueOf(request.getParameter("year"));
		
		System.out.println(course_id + semester + year.toString());

		
		String querynumstud = "select count(*) from takes where course_id = ? and semester = ? and year = ?";
		String jsonnumstud = DbHelper.executeQueryJson(querynumstud, 
				new DbHelper.ParamType[] {DbHelper.ParamType.STRING,
						DbHelper.ParamType.STRING,
						DbHelper.ParamType.INT}, 
				new Object[] {course_id, semester, year});
		
		String querynumta = "select count(*) from TA where course_id = ? and semester = ? and year = ?";
		String jsonnumta = DbHelper.executeQueryJson(querynumta, 
				new DbHelper.ParamType[] {DbHelper.ParamType.STRING,
						DbHelper.ParamType.STRING,
						DbHelper.ParamType.INT}, 
				new Object[] {course_id, semester, year});

		String querytas = 
				"select rollnumber,name from TA natural join student where course_id=? and semester=? and year=?"
				;
		String jsontas = DbHelper.executeQueryJson(querytas, 
				new DbHelper.ParamType[] {DbHelper.ParamType.STRING,
						DbHelper.ParamType.STRING,
						DbHelper.ParamType.INT}, 
				new Object[] {course_id, semester, year});
		
		String querytests = 
				"select test_id, name from test where course_id=? and semester=? and year=?"
				;
		String jsontests = DbHelper.executeQueryJson(querytests, 
				new DbHelper.ParamType[] {DbHelper.ParamType.STRING,
						DbHelper.ParamType.STRING,
						DbHelper.ParamType.INT}, 
				new Object[] {course_id, semester, year});
		
		ObjectMapper mapper = new ObjectMapper();
    	ObjectNode node = mapper.createObjectNode();
    	node.put("Students", jsonnumstud);
    	node.put("TAs", jsonnumta);
    	node.put("AllTAs", jsontas);
    	node.put("AllTests", jsontests);
		
		response.getWriter().print(node.toString());
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

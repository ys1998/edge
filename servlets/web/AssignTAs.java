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
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Servlet implementation class AssignTAs
 */
@WebServlet("/AssignTAs")
public class AssignTAs extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AssignTAs() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		if(session.getAttribute("id") == null) { //not logged in
			response.getWriter().print(DbHelper.errorJson("Not logged in").toString());
			return;
		}
		String userid = (String)session.getAttribute("id");
		
		
		String semester = request.getParameter("semester");
		Integer year = Integer.valueOf(request.getParameter("year"));
	    String course_id = request.getParameter("course_id");
	    String test_id = request.getParameter("test_id");
	    String ta = request.getParameter("ta_roll");
	    Integer index = Integer.parseInt(request.getParameter("index"));
	    
	    String instructorCheckQuery = "select * "
				+ "from teaches "
				+ "where uid=? and course_id = ? and semester=? and year=? "
				;
		
		List<List<Object>> res = DbHelper.executeQueryList(instructorCheckQuery, 
				new DbHelper.ParamType[] {DbHelper.ParamType.STRING, DbHelper.ParamType.STRING, 
						DbHelper.ParamType.STRING, DbHelper.ParamType.INT}, 
				new Object[] {userid, course_id, semester, year});
		
		if(res.isEmpty()) {
			response.getWriter().print(DbHelper.errorJson("Instruction section pair does not exist"));
			return;
		}
		
	    if (ta=="") {
			ObjectMapper mapper = new ObjectMapper();
	    	ObjectNode node = mapper.createObjectNode();
	    	node.put("status", "false");
	    	response.getWriter().print(node.toString());
	    	return;
	    	//node.put("message", "TA roll number not null!!");
	    }
	    
	    String query = "UPDATE ans set grader = (select uid from student where rollnumber=?) where course_id = ? and semester = ? and year = ? and test_id = ? and index = ?";
	    
	    System.out.println("ta is gonna be assigned);" + index.toString());
	    
	    String rett = DbHelper.executeUpdateJson(
	    		query, 
	    		new DbHelper.ParamType[] {
	    				DbHelper.ParamType.STRING,
	    				DbHelper.ParamType.STRING,
	    				DbHelper.ParamType.STRING, 
	    				DbHelper.ParamType.INT,
	    				DbHelper.ParamType.STRING,
	    				DbHelper.ParamType.INT,
	    		}, 
	    		new Object[] {ta, course_id, semester, year, test_id, index}
	    );
	    System.out.println(rett);
		ObjectMapper mapper = new ObjectMapper();
    	ObjectNode node = mapper.createObjectNode();
    	node.put("status", "true");
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

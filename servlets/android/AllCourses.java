package servlets.android;

import servlets.common.DbHelper;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Servlet implementation class AllCourses
 */
@WebServlet("/AllCourses")
public class AllCourses extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AllCourses() {
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
		
		String semester = (String) session.getAttribute("semester");
		String year = Integer.toString((int)session.getAttribute("year"));
		String mode = request.getParameter("mode");
		String rollno = request.getParameter("rollno");
		if(mode.equals("student")) { // Query for student
			String query1 = 
					"select course_id,semester,year "
					+ "from takes "
					+ "where year = ? and semester = ? and rollnumber = ?";
			List<List<Object>> res1 = DbHelper.executeQueryList(query1, 
					new DbHelper.ParamType[] {DbHelper.ParamType.STRING,
							DbHelper.ParamType.STRING,
							DbHelper.ParamType.STRING,
							DbHelper.ParamType.STRING}, 
					new Object[] { year,semester, rollno});
			
			String query2;
			List<List<Object>> res2 = null;
			
			if(semester.equals("Fall")) {
				query2 =  "select course_id, semester, year "
						+ "from takes "
						+ "where (year < ? or (year = ? and semester = 'Spring')) and rollnumber = ?";
				res2 = DbHelper.executeQueryList(query2, 
						new DbHelper.ParamType[] {DbHelper.ParamType.STRING,
								DbHelper.ParamType.STRING,
								DbHelper.ParamType.STRING,
								DbHelper.ParamType.STRING}, 
						new Object[] { year, year, rollno});
			} else if(semester.equals("Spring")){
				query2 =  "select course_id,semester,year "
						+ "from takes "
						+ "where year < ? and rollnumber = ?";
				res2 = DbHelper.executeQueryList(query2, 
						new DbHelper.ParamType[] {DbHelper.ParamType.STRING,
								DbHelper.ParamType.STRING,
								DbHelper.ParamType.STRING}, 
						new Object[] { year, rollno});
			}

			ObjectNode node = new ObjectMapper().createObjectNode();   	
	    	node.put("status", true);
	    	ArrayNode temp = new ObjectMapper().createArrayNode();
	    	for(int i=0; i<res1.size(); ++i) {
	    		ArrayNode inner_temp = new ObjectMapper().createArrayNode();
	    		inner_temp.add((String)res1.get(i).get(0));
	    		inner_temp.add((String)res1.get(i).get(1));
	    		inner_temp.add((String)res1.get(i).get(2));
	    		temp.add(inner_temp);	    		
	    	}
	    	node.putArray("present_courses").addAll(temp);
	    	
	    	ArrayNode temp2 = new ObjectMapper().createArrayNode();
	    	for(int i=0; i<res2.size(); ++i) {
	    		ArrayNode inner_temp = new ObjectMapper().createArrayNode();
	    		inner_temp.add((String)res2.get(i).get(0));
	    		inner_temp.add((String)res2.get(i).get(1));
	    		inner_temp.add((String)res2.get(i).get(2));
	    		temp.add(inner_temp);	    		
	    	}
	    	node.putArray("past_courses").addAll(temp2);
	    	response.getWriter().print(node.toString());
		}
		else { // Query for TA
			String query = 
					"select course_id,semester,year "
					+ "from TA "
					+ "where year = ? and semester = ? and rollnumber = ?"
					;
			List<List<Object>> res = DbHelper.executeQueryList(query, 
				new DbHelper.ParamType[] {DbHelper.ParamType.STRING,
						DbHelper.ParamType.STRING,
						DbHelper.ParamType.STRING,
						DbHelper.ParamType.STRING}, 
				new Object[] { year,semester, rollno});
			
			ObjectNode node = new ObjectMapper().createObjectNode();   	
	    	node.put("status", true);
			ArrayNode temp = new ObjectMapper().createArrayNode();
	    	for(int i=0; i<res.size(); ++i) {
	    		ArrayNode inner_temp = new ObjectMapper().createArrayNode();
	    		inner_temp.add((String)res.get(i).get(0));
	    		inner_temp.add((String)res.get(i).get(1));
	    		inner_temp.add((String)res.get(i).get(2));
	    		temp.add(inner_temp);	    		
	    	}
	    	node.putArray("present_courses").addAll(temp);
	    	response.getWriter().print(node.toString());
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}



import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class AddTA
 */
@WebServlet("/AddTA")
public class AddTA extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddTA() {
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
		String rollnumber = request.getParameter("rollnumber");
		String course_id = request.getParameter("course_id");
		String semester = request.getParameter("semester");
		String year = request.getParameter("year");
		
		String instructorCheckQuery = "select * "
				+ "from teaches "
				+ "where uid=? and course_id = ? and semester=? and year=? "
				;
		
		List<List<Object>> res = DbHelper.executeQueryList(instructorCheckQuery, 
				new DbHelper.ParamType[] {DbHelper.ParamType.STRING, DbHelper.ParamType.STRING, 
						DbHelper.ParamType.STRING, DbHelper.ParamType.STRING}, 
				new String[] {userid, course_id, semester, year});
		
		if(res.isEmpty()) {
			response.getWriter().print(DbHelper.errorJson("Instruction section pair does not exist"));
			return;
		}
		
		String newMsgQuery = "insert into TA (rollnumber, course_id, semester, year) "
				+ "values (?, ?, ?, ?)";
		String json = DbHelper.executeUpdateJson(newMsgQuery, 
				new DbHelper.ParamType[] {DbHelper.ParamType.STRING, 
						DbHelper.ParamType.STRING,
						DbHelper.ParamType.STRING,
						DbHelper.ParamType.STRING},
				new Object[] {rollnumber, course_id, semester, year});
		response.getWriter().print(json);
		response.getWriter().print(json);
		
		
	
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

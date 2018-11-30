package servlets.android;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import servlets.common.DbHelper;

/**
 * Servlet implementation class AllExams
 */
@WebServlet("/AllExams")
public class AllExams extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AllExams() {
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
		String semester = (String) session.getAttribute("semester");
		int year = (int)session.getAttribute("year");
		String mode = request.getParameter("mode");
		String course_id = request.getParameter("course_id");
		String rollno = request.getParameter("rollno");

		if(mode.equals("student")) { // Query for student
			String query = 
					"select name,test_id,test_date "
					+ "from appears natural join test "
					+ "where semester = ? and year = ? and course_id = ? and rollnumber = ? "
					+ "order by test_date desc";
			String json = DbHelper.executeQueryJson(query, 
					new DbHelper.ParamType[] {DbHelper.ParamType.STRING,
							DbHelper.ParamType.INT,
							DbHelper.ParamType.STRING,
							DbHelper.ParamType.STRING}, 
					new Object[] {semester, year, course_id, rollno});
			response.getWriter().print(json);
		}
		else { // Query for TA
			String query = 
					"select distinct name,test_id,test_date "
					+ "from ans natural join test "
					+ "where grader = ? and semester = ? and year = ? and course_id = ? "
					+ "order by test_date desc";
			String json = DbHelper.executeQueryJson(query, 
					new DbHelper.ParamType[] {DbHelper.ParamType.STRING,
							DbHelper.ParamType.STRING,
							DbHelper.ParamType.INT,
							DbHelper.ParamType.STRING}, 
					new Object[] {userid, semester, year,course_id});
			response.getWriter().print(json);
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

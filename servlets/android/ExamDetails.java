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
 * Servlet implementation class ExamDetails
 */
@WebServlet("/ExamDetails")
public class ExamDetails extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ExamDetails() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		
		// mode, rollno, test_id, course_id, sem, year
		HttpSession session = request.getSession();
		if(session.getAttribute("id") == null) { //not logged in
			response.getWriter().print(DbHelper.errorJson("Not logged in").toString());
			return;
		}

		String semester = (String) session.getAttribute("semester");
		int year = (int)session.getAttribute("year");
		String mode = request.getParameter("mode");
		String course_id = request.getParameter("course_id");
		String rollno = request.getParameter("rollno");
		String test_id = request.getParameter("test_id");
		String uid = (String)session.getAttribute("id");
		
		if(mode.equals("student")) { // Query for student
			String query = 
					"select index,marks_obt,grader,m_marks "
					+ "from ans natural join question "
					+ "where semester = ? and year = ? and course_id = ? and rollnumber = ? and test_id = ? "
					+ "order by index asc";
			String json = DbHelper.executeQueryJson(query, 
					new DbHelper.ParamType[] {DbHelper.ParamType.STRING,
							DbHelper.ParamType.INT,
							DbHelper.ParamType.STRING,
							DbHelper.ParamType.STRING,
							DbHelper.ParamType.STRING}, 
					new Object[] {semester, year, course_id, rollno, test_id});
			response.getWriter().print(json);
		}
		else { // Query for TA later wrote 
			String query = 
					"select index, m_marks, count(*) as total, count(marks_obt) as graded "
					+ "from question natural join ans "
					+ "where semester = ? and year = ? and course_id = ? and test_id = ? and grader = ? "
					+ "group by (index, m_marks)";
			String json = DbHelper.executeQueryJson(query, 
					new DbHelper.ParamType[] {DbHelper.ParamType.STRING,
							DbHelper.ParamType.INT,
							DbHelper.ParamType.STRING,
							DbHelper.ParamType.STRING,
							DbHelper.ParamType.STRING}, 
					new Object[] {semester, year, course_id, test_id, uid});
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

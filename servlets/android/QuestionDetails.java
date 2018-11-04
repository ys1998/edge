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
 * Servlet implementation class QuestionDetails
 */
@WebServlet("/QuestionDetails")
public class QuestionDetails extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QuestionDetails() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    // rollno, course_id, sem, year, test_id, index
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		if(session.getAttribute("id") == null) { //not logged in
			response.getWriter().print(DbHelper.errorJson("Not logged in").toString());
			return;
		}
		
		String semester = (String) session.getAttribute("semester");
		String year = Integer.toString((int)session.getAttribute("year"));
		String course_id = request.getParameter("course_id");
		String rollno = request.getParameter("rollno");
		String test_id = request.getParameter("test_id");
		String index_temp = request.getParameter("index");
		int index = Integer.parseInt(index_temp);
		
		// This servlet is student only 
		
		String query = 
				"select stud_ans,comments,marks_obt "
				+ "from ans "
				+ "where semester = ? and year = ? and course_id = ? and rollno = ? and test_id = ? and index = ?"
				;
		String json = DbHelper.executeQueryJson(query, 
				new DbHelper.ParamType[] {DbHelper.ParamType.STRING,
						DbHelper.ParamType.STRING,
						DbHelper.ParamType.STRING,
						DbHelper.ParamType.STRING,
						DbHelper.ParamType.INT}, 
				new Object[] {semester, year, course_id, rollno, test_id,index});
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

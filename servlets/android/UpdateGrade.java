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
 * Servlet implementation class UpdateGrade
 */
@WebServlet("/UpdateGrade")
public class UpdateGrade extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateGrade() {
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
		
		String userid = (String) session.getAttribute("id");
		String semester = (String) session.getAttribute("semester");
		int year = (Integer) session.getAttribute("year");
		String course_id = request.getParameter("course_id");
		String test_id = request.getParameter("test_id");
		String rollno = request.getParameter("rollno");
		int index = Integer.parseInt(request.getParameter("index"));
		String comments = request.getParameter("comments");
		double marks_obt = Double.parseDouble(request.getParameter("marks_obt"));
		String query = 
				" update ans set (comments, marks_obt) = (?, ?) "
				+ "where course_id = ? and semester = ? and year = ? and test_id = ? and index = ? and rollnumber = ? and grader = ? "
				;
		
		String json = DbHelper.executeUpdateJson(query, 
				new DbHelper.ParamType[] {DbHelper.ParamType.STRING,
						DbHelper.ParamType.DOUBLE,
						DbHelper.ParamType.STRING,
						DbHelper.ParamType.STRING,
						DbHelper.ParamType.INT,
						DbHelper.ParamType.STRING,
						DbHelper.ParamType.INT,
						DbHelper.ParamType.STRING,
						DbHelper.ParamType.STRING,
						}, 
				new Object[] {comments,marks_obt,course_id,semester,year, test_id,index,rollno,userid});
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

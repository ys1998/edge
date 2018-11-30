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
 * Servlet implementation class GetAllStudent
 */
@WebServlet("/GetAllStudents")
public class GetAllStudent extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetAllStudent() {
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
		int index = Integer.parseInt(request.getParameter("index"));
		
		String query = 
				" select rollnumber, marks_obt "
				+ " from  ans"
				+ " where grader = ? and semester = ? and  year = ? and course_id = ? and index = ? and test_id = ? "
				;
		
		String json = DbHelper.executeQueryJson(query, 
				new DbHelper.ParamType[] {DbHelper.ParamType.STRING,
						DbHelper.ParamType.STRING,
						DbHelper.ParamType.INT,
						DbHelper.ParamType.STRING,
						DbHelper.ParamType.INT,
						DbHelper.ParamType.STRING,
						}, 
				new Object[] {userid,semester,year, course_id, index,test_id});
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

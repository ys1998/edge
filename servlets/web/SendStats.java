package servlets.web;
import servlets.common.DbHelper;
import servlets.common.Config;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SendStats
 */
@WebServlet("/SendStats")
public class SendStats extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SendStats() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		String test_id = request.getParameter("test_id");
		String course_id = request.getParameter("course_id");
		String semester = request.getParameter("semester");
		Integer year = Integer.valueOf(request.getParameter("year"));
		
		String query = 
				" with myreln as ((select rollnumber, coalesce(sum(marks_obt), 0)   as sum_marks"
			  + " from ans  "
			  + " where semester = ? and year = ? and course_id = ? and test_id = ? "
			  + " group by rollnumber)"
			  + " UNION "
			  + " ((select rollnumber,-1 from takes where semester = ? and year = ? and course_id = ?) EXCEPT (select rollnumber,-1 from appears where semester = ? and year = ? and course_id = ? and test_id = ?)))"
			  + " select rollnumber, name, sum_marks from myreln natural join student order by sum_marks desc";
		
		 String json = DbHelper.executeQueryJson(query, 
					new DbHelper.ParamType[] {
							DbHelper.ParamType.STRING, DbHelper.ParamType.INT, 
							DbHelper.ParamType.STRING, DbHelper.ParamType.STRING,
							DbHelper.ParamType.STRING, DbHelper.ParamType.INT,
							DbHelper.ParamType.STRING, DbHelper.ParamType.STRING,
							DbHelper.ParamType.INT, DbHelper.ParamType.STRING,
							DbHelper.ParamType.STRING,
					}, 
					new Object[] {semester, year, course_id, test_id, semester, year, course_id, semester, year, course_id, test_id});
		
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

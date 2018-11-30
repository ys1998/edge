package servlets.android;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import servlets.common.DbHelper;
import servlets.common.Config;

/**
 * Servlet implementation class Stat
 */
@WebServlet("/Stats")
public class Stats extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Stats() {
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
		String rollno;
		
		List<List<Object>> res = DbHelper.executeQueryList("select rollnumber from student where uid = ?",
				new DbHelper.ParamType[] {DbHelper.ParamType.STRING}, new String[] {userid});
		rollno = (String) res.get(0).get(0);
		
		String query = 
				"   with R(test_date, roll, marks) as (select test_date, rollnumber, sum(marks_obt) "
				+ " from ans natural join test "
				+ " where semester = ? and year = ? and course_id = ? "
				+ " group by (rollnumber, test_date)), "
				+ " R2(roll, test_date, t_rank) as (select roll, test_date, rank() over (partition by test_date order by marks desc) as t_rank "
				+ " from R "
				+ " order by t_rank),"
				+ " R3(test_date, cnt) as (select test_date, count(*) from R2 group by test_date) "
				+ " select test_date, t_rank, cnt "
				+ " from R2 natural join R3  "
				+ " where roll = ? "
				;
		
		String json = DbHelper.executeQueryJson(query, 
				new DbHelper.ParamType[] {DbHelper.ParamType.STRING,
						DbHelper.ParamType.INT,
						DbHelper.ParamType.STRING,
						DbHelper.ParamType.STRING,
						}, 
				new Object[] {semester, year, course_id,rollno});
		response.getWriter().print(json);
		return;
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

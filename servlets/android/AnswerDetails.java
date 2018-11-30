package servlets.android;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import servlets.common.DbHelper;

// STUDENT ONLY SERVLET

/**
 * Servlet implementation class AnswerDetails
 */
@WebServlet("/AnswerDetails")
public class AnswerDetails extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AnswerDetails() {
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
		int year = (int) session.getAttribute("year");
		String course_id = request.getParameter("course_id");
		String test_id = request.getParameter("test_id");
		int index = Integer.parseInt(request.getParameter("index"));
		String mode = request.getParameter("mode");
		
		if(mode.equals("student")) {
		
			List<List<Object>> res = DbHelper.executeQueryList("select rollnumber from student where uid = ?",
					new DbHelper.ParamType[] {DbHelper.ParamType.STRING}, new String[] {userid});
			String rollno = res.get(0).get(0).toString();
			if(rollno != null) {
				String query = 
						"select model_ans,marks_obt,m_marks,comments "
						+ "from ans natural join question "
						+ "where semester = ? and year = ? and course_id = ? and test_id = ? and rollnumber = ? and index = ? "
						;
				
				String json = DbHelper.executeQueryJson(query, 
						new DbHelper.ParamType[] {DbHelper.ParamType.STRING,
								DbHelper.ParamType.INT,
								DbHelper.ParamType.STRING,
								DbHelper.ParamType.STRING,
								DbHelper.ParamType.STRING,
								DbHelper.ParamType.INT}, 
						new Object[] {semester,year,course_id,test_id,rollno,index});
				
				// stud_ans stores the location of the exact answer, get is by parsing from json
				response.getWriter().print(json);
			}else {
				response.getWriter().print(DbHelper.errorJson("Invalid access!"));
			}
		}else {
			// TA
			String rollno = request.getParameter("rollno");
			String query = 
					"select model_ans,marks_obt,m_marks,comments "
					+ "from ans natural join question "
					+ "where semester = ? and year = ? and course_id = ? and test_id = ? "
					+ "and rollnumber = ? and index = ? and grader = ?";
			
			String json = DbHelper.executeQueryJson(query, 
					new DbHelper.ParamType[] {DbHelper.ParamType.STRING,
							DbHelper.ParamType.INT,
							DbHelper.ParamType.STRING,
							DbHelper.ParamType.STRING,
							DbHelper.ParamType.STRING,
							DbHelper.ParamType.INT,
							DbHelper.ParamType.STRING}, 
					new Object[] {semester,year,course_id,test_id,rollno,index, userid});
			
			// stud_ans stores the location of the exact answer, get is by parsing from json
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

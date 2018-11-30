package servlets.android;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import servlets.common.DbHelper;
/**
 * Servlet implementation class GetAnswer
 */
@WebServlet("/GetAnswer")
public class GetAnswer extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetAnswer() {
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
		String rollno;
		
		if(mode.equals("student")) {
			// get rollnumber and perform THE check
			List<List<Object>> res = DbHelper.executeQueryList("select rollnumber from student where uid = ?",
					new DbHelper.ParamType[] {DbHelper.ParamType.STRING}, new String[] {userid});
			rollno = (String) res.get(0).get(0);
			if(rollno != null) {
				// check is now complete
				String query = 
						"select stud_ans "
						+ "from ans "
						+ "where semester = ? and year = ? and course_id = ? and test_id = ? and rollnumber = ? and index = ? ";
				res = DbHelper.executeQueryList(query, 
						new DbHelper.ParamType[] {DbHelper.ParamType.STRING,
								DbHelper.ParamType.INT,
								DbHelper.ParamType.STRING,
								DbHelper.ParamType.STRING,
								DbHelper.ParamType.STRING,
								DbHelper.ParamType.INT}, 
						new Object[] {semester, year, course_id, test_id, rollno, index});
				
				// stud_ans stores the location of the exact answer
				String location = (String) res.get(0).get(0);
				if(location != null) {
					// File is located at the location retrieve it and send it
					File pdfFile = new File(location);
					response.setContentType("application/pdf");
					response.addHeader("Content-Disposition", "attachment; filename=" + pdfFile.getName());
					response.setContentLength((int) pdfFile.length());
		
					FileInputStream fileInputStream = new FileInputStream(pdfFile);
					OutputStream responseOutputStream = response.getOutputStream();
					int bytes;
					while ((bytes = fileInputStream.read()) != -1) {
						responseOutputStream.write(bytes);
					}
					fileInputStream.close();
				} else {
					response.getWriter().print(DbHelper.errorJson("Answer not found"));
				}
			}
		}else { 
			// its a TA
			rollno = request.getParameter("rollno");
			String query = 
				"select stud_ans "
				+ "from ans "
				+ "where grader = ? and semester = ? and year = ? and course_id = ? and test_id = ? and rollnumber = ? and index = ? "
				;
			
			List<List<Object>> res1 = DbHelper.executeQueryList(query, 
					new DbHelper.ParamType[] {DbHelper.ParamType.STRING,
							DbHelper.ParamType.STRING,
							DbHelper.ParamType.INT,
							DbHelper.ParamType.STRING,
							DbHelper.ParamType.STRING,
							DbHelper.ParamType.STRING,
							DbHelper.ParamType.INT}, 
					new Object[] {userid, semester, year, course_id, test_id, rollno, index});
			
			// stud_ans stores the location of the exact answer, get is by parsing from json
			String location = (String) res1.get(0).get(0);
			if(location != null) {
				// File is located at the location retrieve it and send it
				File pdfFile = new File(location);
				response.setContentType("application/pdf");
				response.addHeader("Content-Disposition", "attachment; filename=" + pdfFile.getName());
				response.setContentLength((int) pdfFile.length());
	
				FileInputStream fileInputStream = new FileInputStream(pdfFile);
				OutputStream responseOutputStream = response.getOutputStream();
				int bytes;
				while ((bytes = fileInputStream.read()) != -1) {
					responseOutputStream.write(bytes);
				}
				fileInputStream.close();
			} else {
				response.getWriter().print(DbHelper.errorJson("Answer not found"));
			}
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

package servlets.web;
import servlets.common.DbHelper;
import servlets.common.Config;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.math.BigDecimal;


import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class SubmitTest
 */
@WebServlet("/SubmitTest")
@MultipartConfig
public class SubmitTest extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SubmitTest() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		return;
}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		if(session.getAttribute("id") == null) { //not logged in
			response.getWriter().print(DbHelper.errorJson("Not logged in").toString());
			return;
		}
		String userid = (String)session.getAttribute("id");
		Enumeration<String> params = request.getParameterNames(); 
		while(params.hasMoreElements()){
		 String paramName = params.nextElement();
		 System.out.println("Parameter Name - "+paramName+", Value - "+request.getParameter(paramName));
		}
		String course_id = request.getParameter("course_id");
		String semester = request.getParameter("semester");
		Integer year = Integer.valueOf(request.getParameter("year"));
		System.out.println(userid+course_id+semester+year.toString());
		String instructorCheckQuery = "select * "
				+ "from teaches "
				+ "where uid=? and course_id = ? and semester=? and year=? "
				;
		
		List<List<Object>> res = DbHelper.executeQueryList(instructorCheckQuery, 
				new DbHelper.ParamType[] {DbHelper.ParamType.STRING, DbHelper.ParamType.STRING, 
						DbHelper.ParamType.STRING, DbHelper.ParamType.INT}, 
				new Object[] {userid, course_id, semester, year});
		
		if(res.isEmpty()) {
			response.getWriter().print(DbHelper.errorJson("Instruction section pair does not exist"));
			return;
		}
		int num_ques = Integer.parseInt( request.getParameter("num_ques") );
		String name = request.getParameter("name");
		String test_id = request.getParameter("test_id");
		String test_date = request.getParameter("test_date");
		String m_marks[] = new String[num_ques];
		String q_text[] = new String[num_ques];
		String space[] = new String[num_ques];
		String model_ans[] = new String[num_ques];
		System.out.println(test_date);

		for(int i = 0; i < num_ques; i++) {
			int j = i+1;
			m_marks[i] = request.getParameter("MaxMarks" + j);
			q_text[i] = request.getParameter("Textarea" + j);
			space[i] = request.getParameter("Pages" + j);
			model_ans[i] = request.getParameter("TextAnswerarea" + j);
			
		}
		String insertTest = "insert into test (course_id, semester, year, test_id, name, num_ques, test_date) "
				+ "values (?, ?, ?, ?, ?, ?, ?)";
		String insertTestJson = DbHelper.executeUpdateJson(insertTest, 
				new DbHelper.ParamType[] {
						DbHelper.ParamType.STRING, 
						DbHelper.ParamType.STRING,
						DbHelper.ParamType.INT,
						DbHelper.ParamType.STRING,
						DbHelper.ParamType.STRING,
						DbHelper.ParamType.INT,
						DbHelper.ParamType.DATE
		},
				new Object[] {course_id, semester, year, test_id , name , num_ques , test_date}
		);
		System.out.println(insertTestJson);
		for(int i = 0; i < num_ques; i++) {
			BigDecimal mmarks =  new BigDecimal(m_marks[i]);
			
			String questionQuery = "insert into question (course_id, semester, year, test_id, index, m_marks, q_text, space,model_ans) "
					+ "values (?, ?, ?, ?, ?, ?, ?, ?,?)";
			String questionJson = DbHelper.executeUpdateJson(questionQuery, 
					new DbHelper.ParamType[] {
							DbHelper.ParamType.STRING, 
							DbHelper.ParamType.STRING,
							DbHelper.ParamType.INT,
							DbHelper.ParamType.STRING,
							DbHelper.ParamType.INT,
							DbHelper.ParamType.NUMERIC,
							DbHelper.ParamType.STRING,
							DbHelper.ParamType.INT,
							DbHelper.ParamType.STRING,
							
			},
					new Object[] {course_id, semester, year, test_id, i+1 ,mmarks
							, q_text[i],Integer.parseInt(space[i]),model_ans[i] }
			);
			System.out.println(questionJson);
		}
		
		
		response.getWriter().print(DbHelper.okJson().toString());
		
	}

}

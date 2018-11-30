package servlets.web;
import servlets.common.DbHelper;
import servlets.common.Config;
import java.io.FileInputStream;
import java.io.FileWriter;  
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;


/**
 * Servlet implementation class QuestionPaper
 */
@WebServlet("/QuestionPaper")
public class QuestionPaper extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QuestionPaper() {
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
		String userid = (String)session.getAttribute("id");
		String test_id = request.getParameter("test_id");
		String course_id = request.getParameter("course_id");
		String semester = request.getParameter("semester");
		Integer year = Integer.valueOf(request.getParameter("year"));
		
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
		
		String instructorNameQuery = "select name "
				+ "from instructor "
				+ "where uid=?"
				;
		
		List<List<Object>> resname = DbHelper.executeQueryList(instructorNameQuery, 
				new DbHelper.ParamType[] {DbHelper.ParamType.STRING}, 
				new String[] {userid});
		
		
		
		String numQuesQuery = "select num_ques , name "
				+ "from test "
				+ "where test_id=? and course_id = ? and semester=? and year=? "
				;
		
		List<List<Object>> res2 = DbHelper.executeQueryList(numQuesQuery, 
				new DbHelper.ParamType[] {DbHelper.ParamType.STRING, DbHelper.ParamType.STRING, 
						DbHelper.ParamType.STRING, DbHelper.ParamType.INT}, 
				new Object[] {test_id, course_id, semester, year});
		if(res2.isEmpty()) {
			response.getWriter().print(DbHelper.errorJson("Given TestID does not exist"));
			return;
		}
		int numQues = (int)res2.get(0).get(0);
		String testname = (String)res2.get(0).get(1);
		String instName = (String)resname.get(0).get(0);
		
		FileWriter fw = new FileWriter("temp.tex");    
        fw.write("\\documentclass{article}\n" + 
        		"\\usepackage{graphicx}\n" + 
        		"\n" + 
        		"\\begin{document}\n" + 
        		"\n" + 
        		"\\title{" + testname +  "}\n" + 
        		"\\author{ " + instName  +" }\n" + 
        		"\n" + 
        		"\\maketitle"
        		+ "\\newpage \n");  
        
        
		for(int i = 1; i<=numQues; i++) {
			String QuesQuery = "select q_text, m_marks, space "
					+ "from question "
					+ "where index=? and test_id=? and course_id = ? and semester=? and year=? "
					;
			List<List<Object>> resIter = DbHelper.executeQueryList(QuesQuery, 
					new DbHelper.ParamType[] {DbHelper.ParamType.INT, DbHelper.ParamType.STRING, DbHelper.ParamType.STRING, 
							DbHelper.ParamType.STRING, DbHelper.ParamType.INT}, 
					new Object[] {i, test_id, course_id, semester, year});
			
			String q_text = (String)resIter.get(0).get(0);
			BigDecimal mmarks = (BigDecimal) resIter.get(0).get(1);
			int space = (int)resIter.get(0).get(2);

			
			fw.write("\n");
			
			fw.write("Question " + Integer.toString(i));
			fw.write(" \\\\ ");
			fw.write("\n");

			fw.write("Maximum Marks " + mmarks.toString());
			fw.write(" \\\\ ");
			fw.write("\n");
			fw.write(q_text);
			fw.write("\n");
			for(int l = 0; l < space; l++) {
				fw.write( " \\phantom{blabla}   \\newpage ");
			}
			
			
			
		}
		fw.write("\n");
		fw.write("\\end{document}\n" + 
				"");
        fw.close();    

        List<String> command = new ArrayList<String>(); 
        command.add("pdflatex");
        command.add("temp.tex");
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);
        Process p = pb.start();
        try {
			p.waitFor();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        OutputStream out = response.getOutputStream();
        FileInputStream in = new FileInputStream("temp.pdf");
        byte[] buffer = new byte[4096];
        int length;
        while ((length = in.read(buffer)) > 0){
            out.write(buffer, 0, length);
        }
        in.close();
        out.flush();
		
		
		
		
//		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

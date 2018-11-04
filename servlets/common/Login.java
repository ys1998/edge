package servlets.common;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    // Function for computing hashed password
    String customHash(String textToHash) {
    	int p = 31;
        int m = 1000000009;
        Long hash_value = 0L;
        Long p_pow = 1L;
        for (int i=0; i<textToHash.length(); ++i) {
            hash_value = (hash_value + (textToHash.charAt(i) - 'a' + 1) * p_pow) % m;
            p_pow = (p_pow * p) % m;
        }
        return Long.toString(hash_value);
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String uid = request.getParameter("uid");
		String pwd = request.getParameter("pwd");
		List<List<Object>> res = DbHelper.executeQueryList("select uid, salt, hash from users where uid = ?", 
				new DbHelper.ParamType[] {DbHelper.ParamType.STRING},
				new String[] {uid});
		if(res.size() == 0) {
			response.getWriter().print(DbHelper.errorJson("Invalid username"));
			return;
		} else {
			String salt = (String)res.get(0).get(1);
			// find hash
			String hash_pwd = customHash(pwd + salt);
			if(hash_pwd.equals((String)res.get(0).get(2))){
				// match! collect metadata
				
				HttpSession session = request.getSession();
				session.setAttribute("id", uid);
				int month = Calendar.getInstance().get(Calendar.MONTH);
				if(month < 7)
					session.setAttribute("semester", "Spring");
				else
					session.setAttribute("semester", "Fall");
				session.setAttribute("year", Calendar.getInstance().get(Calendar.YEAR));
				
				List<List<Object>> res1 = DbHelper.executeQueryList("select name from instructor where uid = ?", 
						new DbHelper.ParamType[] {DbHelper.ParamType.STRING},
						new String[] {uid});
				if(res1.size() > 0) {
					// instructor
					ObjectNode node = new ObjectMapper().createObjectNode();  	
			    	node.put("status", true);
			    	node.put("type", "instructor");
			    	node.put("name", (String)res1.get(0).get(0));
			    	response.getWriter().print(node.toString());
			    	return;
				} else {
					// student
					List<List<Object>> res2 = DbHelper.executeQueryList("select name, rollnumber from student where uid = ?", 
							new DbHelper.ParamType[] {DbHelper.ParamType.STRING},
							new String[] {uid});
					ObjectNode node = new ObjectMapper().createObjectNode();   	
			    	node.put("status", true);
			    	node.put("type", "student");
			    	node.put("name", (String)res2.get(0).get(0));
			    	node.put("rollno", (String)res2.get(0).get(1));
			    	response.getWriter().print(node.toString());
			    	return;
				}
			}else {
				// incorrect pwd !
				response.getWriter().print(DbHelper.errorJson("Invalid password"));
				return;
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

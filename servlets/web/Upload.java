package servlets.web;
import servlets.common.DbHelper;
import servlets.common.Config;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.utils.PageRange;
import com.itextpdf.kernel.utils.PdfSplitter;


/**
 * Servlet implementation class Upload
 */
@WebServlet("/Upload")
@MultipartConfig
public class Upload extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Upload() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    private void fillans(String path, String course_id, String semester, Integer year, String test_id) throws IOException {
    File answer_folder = new File(path) ;

	if(answer_folder.exists()) {
		System.out.println("Directory? "+answer_folder.isDirectory());
	}
	else {
		System.out.println("Not a Folder !!");
	}

	List<Integer> pageNumbers = new ArrayList<Integer>(); // build this using database 
	try (java.sql.Connection conn = DriverManager.getConnection(Config.url, Config.user, "")){	
		conn.setAutoCommit(false);		  
		try(PreparedStatement stmt = conn.prepareStatement(" select index,space "
	       		+ " from question  "
	       		+ " where course_id = ? and semester = ? and year = ? and test_id = ? "
	       		+ " order by index ")){

			stmt.setString(1,course_id);
			stmt.setString(2, semester);
			stmt.setInt(3, year);
			stmt.setString(4,test_id);
			ResultSet rs1 = stmt.executeQuery();
			int total = 2;
			while(rs1.next()){
				System.out.println(" the number being added to list is : " + total);
				pageNumbers.add(total);
				total += Integer.parseInt(rs1.getString(2)); 
			}
			//pageNumbers.remove(pageNumbers.size()-1);
		}
		catch(Exception e1) {
			e1.printStackTrace();
			conn.rollback();
		}
	}catch(Exception e1) {
		e1.printStackTrace();
	}

	List<String> updates = new ArrayList<String>();
	
	for(final File fileEntry : answer_folder.listFiles()) {
		if(fileEntry.isDirectory()) continue;//System.out.println("Expected File, Got directory");
		String name = fileEntry.getName();
		// make a folder and add answers there
		String file_path = fileEntry.getAbsolutePath();
		String folder_path = file_path.substring(0, file_path.length()-4);
		String rollnumber = name.substring(0, name.length()-4);
		File new_folder = new File(folder_path);
		System.out.println("Folder path   :   " +folder_path);
		boolean created = new_folder.mkdir(); // remove the .pdf from last.
		if(created) {
			System.out.println("Directory Created");
		}
		else {
			System.out.println("Directory Creation Failed");
		}
		// you need to add /ansx.pdf to output file
		System.out.println("Opening path   :   " +file_path);
	    PdfDocument pdfDoc = new PdfDocument(new PdfReader(file_path));
	    List<PdfDocument> splitDocuments = new PdfSplitter(pdfDoc) {
	    	int partNumber = 0;
	    	@Override
	    	protected PdfWriter getNextPdfWriter(PageRange documentPageRange) {
	    		try {
	    			System.out.println("Adding to updates  " +folder_path + "ans" + String.valueOf(partNumber) + ".pdf"+"/"+rollnumber);
	    			if(partNumber!=0) {
	    			updates.add(folder_path + "/ans" + String.valueOf(partNumber) + ".pdf"+":"+rollnumber + ":" + partNumber);
	    			System.out.println(updates.size());
	    			}
	    			return new PdfWriter(folder_path + "/ans" + String.valueOf(partNumber++) + ".pdf");
	    		} catch (FileNotFoundException e) {
	    			throw new RuntimeException();
	    		}
	    	}
	    }.splitByPageNumbers(pageNumbers);

	    for (PdfDocument doc : splitDocuments)
	    	doc.close();
	    pdfDoc.close();
		// Remove this file from answer_folder
	 
	    if(fileEntry.delete()){
            System.out.println("File deleted from Project root directory");
        }else System.out.println("File doesn't exists in root directory");
    
     
	    System.out.println("Out of function!!");
	   
	}
	String query = "insert into ans values (?,?,?,?,?,?,?)";// where course_id = ? and semester = ? and year = ? and test_id = ? and index = ? and rollnumber = ? ";
    try (Connection conn = DriverManager.getConnection(Config.url, Config.user, Config.password))
    {	System.out.println("Processing queries now!!");
    	conn.setAutoCommit(false);
    	try(PreparedStatement pstmt = conn.prepareStatement(query)) {
    		for (int i = 0; i < updates.size(); i++) {
    			String[] arr = updates.get(i).split(":");
    			//String rollnumber = arr[arr.length-1].substring(0, arr[arr.length-1].length()-4);
    			pstmt.setString( 1, course_id );
    			pstmt.setString( 2, semester );
    			pstmt.setInt( 3, year );
    			pstmt.setString( 4, test_id);
    			pstmt.setString( 7, arr[0]);
    			pstmt.setInt(5, Integer.parseInt(arr[2]));
    			pstmt.setString( 6, arr[1]);
    			pstmt.addBatch();
    			System.out.println("Arguments for updating answer : " + arr[0] + arr[1]+arr[2]);
    		}

    		int[] count = pstmt.executeBatch();
    		conn.commit();
    	}
    	catch(Exception ex){
    		conn.rollback();
    		throw ex;
    	}
    	finally{
    		conn.setAutoCommit(true);
    	}
    } catch (Exception e) {
    	e.printStackTrace();
    }
    
    }
    
    
    private void copyInputStreamToFile( InputStream in, File file ) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*
    private Boolean checkrollcourse(String roll, String Course, String Sem, String year) {
    	try (Connection conn = DriverManager.getConnection(Config.url, Config.user, Config.password))
        {
            conn.setAutoCommit(false);
            try(PreparedStatement stmt = conn.prepareStatement(query)) {
            	setParams(stmt, paramTypes, params);
                ResultSet rs = stmt.executeQuery();
                json = resultSetToJson(rs);
                conn.commit();
            }
            catch(Exception ex)
            {
                conn.rollback();
                throw ex;
            }
            finally{
                conn.setAutoCommit(true);
            }
        } catch (Exception e) {
            return errorJson(e.getMessage()).toString();
        }
    	return true;
    }
    */
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String semester = request.getParameter("semester");
		Integer year = Integer.valueOf(request.getParameter("year"));
	    String course_id = request.getParameter("course_id");
	    String test_id = request.getParameter("test_id");
		
	    List<List<Object>> uploaded = DbHelper.executeQueryList(
	    		"select ansuploaded from test where course_id = ? and semester = ? and year = ? and test_id = ? ", 
	    		new DbHelper.ParamType[] {
	    				DbHelper.ParamType.STRING, DbHelper.ParamType.STRING, 
	    				DbHelper.ParamType.INT, DbHelper.ParamType.STRING
				}, 
	    		new Object[] {course_id, semester, year, test_id}
	    );
		System.out.println(uploaded.get(0).get(0));
	    if ((boolean) uploaded.get(0).get(0)) {
			ObjectMapper mapper = new ObjectMapper();
	    	ObjectNode node = mapper.createObjectNode();
	    	node.put("status", false);
	    	node.put("message", "alreade uploaded bitch");
			
	    	//response.getWriter().print(node.toString());
			/*response.sendRedirect("AssignGraders?course_id=" + course_id
					+ "&semester=" + semester
					+ "&year=" + year.toString()
					+ "&test_id=" + test_id
					+ "&upload=" + "false");*/
	    	/*response.sendRedirect("Course?course_id=" + course_id
					+ "&semester=" + semester
					+ "&year=" + year.toString());*/
	    	response.getWriter().write("FAILED");
	    	return;	 	
	    }
		
		
	    Part filePart = request.getPart("file"); // Retrieves <input type="file" name="file">
	    String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
	    InputStream fileContent = filePart.getInputStream();
	    
	    //File uploads = new File("../../eclipse-workspace/data");
	    File zipfile = new File(fileName);
	    //System.out.println(file.toString());

	    zipfile.createNewFile();

	    copyInputStreamToFile(fileContent, zipfile);
	    
	    List<String> command = new ArrayList<String>(); 
	    command.add("mkdir");
        command.add("-p");
	    command.add("Database/eclipse-workspace/data/"+year.toString()+"/"+semester+"/"+course_id+"/"+test_id);
        //command.add(";");
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);
        Process p = pb.start();
        try {
			p.waitFor();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    List<String> command1 = new ArrayList<String>();         
	    command1.add("unzip");
	    command1.add("-j");
	    command1.add(zipfile.toString());
	    command1.add("-d");
	    command1.add("Database/eclipse-workspace/data/"+year.toString()+"/"+semester+"/"+course_id+"/"+test_id);
        ProcessBuilder pb1 = new ProcessBuilder(command1);
        pb1.redirectErrorStream(true);
        Process p1 = pb1.start();
        try {
			p1.waitFor();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        
        
        
        /*
	    Process p = Runtime.getRuntime().exec(""
	    		+ "mkdir -p " + "Database/eclipse-workspace/data/"+year+"/"+semester+"/"+course_id+"/"+test_id + " ; "
	    				+ " "
	    				+ "unzip -j " + zipfile.toString() + " -d Database/eclipse-workspace/data/"+year+"/"+semester+"/"+course_id+"/"+test_id);
	    try {
	    	System.out.println("Waiting for batch file ...");
			p.waitFor();
			System.out.println("completed ...");
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		*/
	    Runtime.getRuntime().exec("rm " + zipfile.toString());
	    
	  /*  System.out.println(""
	    		+ "mkdir -p " + "Database/eclipse-workspace/data/"+year+"/"+semester+"/"+course_id+"/"+test_id + " ; "
				+ " "
				+ "unzip -j " + zipfile.toString() + " -d Database/eclipse-workspace/data/"+year+"/"+semester+"/"+course_id+"/"+test_id);
	    */
	    //Runtime.getRuntime().exec("")
	    // Files.copy(fileContent, file.toPath());
	    
	    String Dir = zipfile.toString();
	    int val = (int)(zipfile.toString().length()-4);
	    String mystr = "Database/eclipse-workspace/data/" +year.toString()+"/"+semester+"/"+course_id+"/"+test_id ;//+ "/" + zipfile.toString().substring(0, val);
	    System.out.println(mystr);
	    
	    File folder = new File((mystr));
	    
	    File[] listOfFiles = folder.listFiles();
	    System.out.println(listOfFiles);
	  // Iterating array of files for printing name of all files present in the directory.
	    //for (int i = 0; i < listOfFiles.length; i++) {
	    //    System.out.println("File Name: " +listOfFiles[i]);
	    //}
	    
		
		System.out.println(listOfFiles);
	    String query = "INSERT into appears values(?,?,?,?,?)";
	    try (Connection conn = DriverManager.getConnection(Config.url, Config.user, Config.password))
        {
            conn.setAutoCommit(false);
            try(PreparedStatement pstmt = conn.prepareStatement(query)) {
            	for (int i = 0; i < listOfFiles.length; i++) {
            		
            		String[] arr = listOfFiles[i].toString().split("/");
            		String rollnumber = arr[arr.length-1];//.substring(0, arr[arr.length-1].length()-4);
            		//if (! checkrollcourse(rollnumber, course_id, year, semester)) {
            		//	continue;
            		//}
            		pstmt.setString( 1, course_id );
                	pstmt.setString( 2, semester );
                	pstmt.setInt( 3, year );
                	pstmt.setString( 4, test_id);
                	
                	pstmt.setString( 5, arr[arr.length-1].substring(0, arr[arr.length-1].length()-4));
                	pstmt.addBatch();
                	System.out.println(arr[arr.length-1]);
            	}
            	
                
            	int[] count = pstmt.executeBatch();
            	conn.commit();
            }
            catch(Exception ex)
            {
                conn.rollback();
                throw ex;
            }
            finally{
                conn.setAutoCommit(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

	    
	    fillans(folder.toString(), course_id, semester, year, test_id);
	    

	    
	    DbHelper.executeUpdateJson(
	    		"update test set ansuploaded = true where course_id = ? and semester = ? and year = ? and test_id = ? ", 
	    		new DbHelper.ParamType[] {
	    				DbHelper.ParamType.STRING, DbHelper.ParamType.STRING, 
	    				DbHelper.ParamType.INT, DbHelper.ParamType.STRING
				}, 
	    		new Object[] {course_id, semester, year, test_id}
	    );
	    
	    response.getWriter().write("File " + folder.toString() + " successfully uploaded");
	    
		/*response.sendRedirect("AssignGraders?course_id=" + course_id
				+ "&semester=" + semester
				+ "&year=" + year.toString()
				+ "&test_id=" + test_id
				+ "&upload=" + "true");  */

	    
	    
	}

}

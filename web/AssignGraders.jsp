<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
  <script src="https://code.jquery.com/jquery-1.11.1.min.js"></script>
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.min.css">
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/latex.js@0.11.1/dist/latex.min.js"></script>
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>

  <link rel="stylesheet" type="text/css" href="MakeTest.css">

<script>
fakeparam = <% out.print(request.getParameter("upload")); %>;
console.log(fakeparam);
//console.log((fakeparam).type());
/*if (!fakeparam){
	alert("upload failed, already uploaded");
}*/

</script>

<script src="https://code.jquery.com/jquery-1.11.1.min.js"></script>

<script type="text/javascript">
function loader(){
		  
	  course_id = "<% out.print(request.getParameter("course_id")); %>";
	  semester = "<% out.print(request.getParameter("semester")); %>";
	  year = "<% out.print(request.getParameter("year")); %>";
	  
	  xhttp = new XMLHttpRequest();
	  xhttp.onreadystatechange = function() {
	    if (this.readyState == 4 && this.status == 200) {
	      var myObj = JSON.parse(this.responseText);

	      var tas = JSON.parse(myObj.AllTAs).data;
		  output = "Select each option to assign grader corresponding to the question: <br>";
		  $('.TASelector').append($('<option>', {
			    //value: ,
			    text: ""
			}));
		  for (x in tas){
			  /*console.log(tas[x].rollnumber);
			  output += tas[x].rollnumber;
			  output += " <br>";*/
			  $('.TASelector').append($('<option>', {
				    value: tas[x].rollnumber,
				    text: tas[x].rollnumber
				}));
		  }
		  document.getElementById('tamsg').innerHTML = output;
	    }
	  };
	  xhttp.open("GET", "CourseMeta" + "?course_id=" + course_id + "&semester=" + semester + "&year=" + year, true);
	  xhttp.send();
	}

function submitFunct(i1){
	course_id = "<% out.print(request.getParameter("course_id")); %>";
	semester = "<% out.print(request.getParameter("semester")); %>";
	year = "<% out.print(request.getParameter("year")); %>";
	test_id = "<% out.print(request.getParameter("test_id")); %>";
	
	console.log(i1);
	var x = document.getElementById("Question"+i1).value;
	xhttp = new XMLHttpRequest();
	  xhttp.onreadystatechange = function() {
	    if (this.readyState == 4 && this.status == 200) {
	      //console.log(this.responseText);
	    	var myObj = JSON.parse(this.responseText);
	      // Check Suvvess
	      console.log(myObj.status);
	      if (myObj.status == "false"){
	    	  alert("Grader should not be null");
	      }
	    }
	  };
	  xhttp.open("GET", "AssignTAs" + "?course_id=" + course_id + "&semester=" + semester + "&test_id=" + test_id + "&index=" + i1  + "&ta_roll=" + x  + "&year=" + year, true);
	  xhttp.send();

}
loader();
</script>

</head>
<body>
<div>
<div id='tamsg' class="col-md-7 col-md-offset-2" style="color:lightblue;" align="center"></div><br><br>
<div class="col-md-7 col-md-offset-2">
<%
int loopIndex;

for (loopIndex = 1; loopIndex <= Integer.parseInt((String) request.getAttribute("num_ques")); loopIndex++) {
      out.println(
    		  "Question "
        + loopIndex +
        "&nbsp&nbsp&nbsp  " +
        "<select class=\"TASelector form-control\" onchange=\"submitFunct(" + loopIndex + ")\" id=\"Question"+ loopIndex + "\" >  </select>" + 
        "<br>"
        );
    }
%>
<br>
<input type="button" class="btn btn-info" value="Submit and Course Home" onclick="relocate_home()">

</div>


</div>
<div>

<script>
function relocate_home()
{
	function temp_func(){
		course_id = "<% out.print(request.getParameter("course_id")); %>";
		semester = "<% out.print(request.getParameter("semester")); %>";
		year = "<% out.print(request.getParameter("year")); %>";
		test_id = "<% out.print(request.getParameter("test_id")); %>";

		xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = function() {
	  	if (this.readyState == 4 && this.status == 200) {
	  		var myobj = JSON.parse(this.responseText);
	  		
	  		if (myobj.go=="false"){
	  			alert("Please fill all graders before submitting");
	  			return 1;
	  		}
	  		else {
	  			location.href = "/EDGE/Course?course_id=" + course_id + "&semester=" + semester + "&year=" + year;
	  		}
	  	}
	  	
		};
		xhttp.open("GET", "CheckAllAssigned" + "?course_id=" + course_id + "&semester=" + semester + "&test_id=" + test_id + "&year=" + year, true);
		xhttp.send();
	}
	out = temp_func();
	console.log(out);
} 
</script>



</div>

</body>
</html>
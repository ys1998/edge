<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>EDGE</title>
<script src="https://code.jquery.com/jquery-1.11.1.min.js"></script>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta http-equiv="content-type" content="text/html; charset=UTF-8">
  <meta http-equiv="content-language" content="en">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.min.css">
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/latex.js@0.11.1/dist/latex.min.js"></script>
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>

  <link rel="stylesheet" type="text/css" href="MakeTest.css">
<script>

$(document).ready(function () {
	// body...
  FillMeta('meta', 'TAs', 'Tests');
})

function FillMeta(id, id2, id3) {
	  // body...
	  
	  
	  course_id = "<% out.print(request.getParameter("course_id")); %>";
	  semester = "<% out.print(request.getParameter("semester")); %>";
	  year = "<% out.print(request.getParameter("year")); %>";
	  
	  xhttp = new XMLHttpRequest();
	  xhttp.onreadystatechange = function() {
	    if (this.readyState == 4 && this.status == 200) {
	      var myObj = JSON.parse(this.responseText);
	      console.log(myObj);
		  output = "";
		 
		  var studs = JSON.parse(myObj.Students);
		  output += "The number of students registed = " +studs.data[0].count;

		  output += "<br>";
		  
		  var studs = JSON.parse(myObj.TAs);
		  output += "The number of teaching assitants registed = " + studs.data[0].count; 
		  //for (x in myObj.TAs.data) {output+= myObj.TAs.data[x].count; console.log(myObj.TAs.data[x].count);}
		  
		  document.getElementById(id).innerHTML = output;
		  
		  var tas = JSON.parse(myObj.AllTAs).data;
		  if (tas.length==0){
			  output = "Uhh Oh, no teaching assistants. You can add a TA from here"
		  }
		  else{
		  output = "Here are the list of TAs in the course: <br>";
		  output += "<ul>";
		  for (x in tas){
			  output += "<li>";
			  console.log(tas[x].rollnumber);
			  output += tas[x].rollnumber;
			  output += "     ";
			  output += tas[x].name;
			//output += " <br>";
			  output += "</li>";
		  }
		  output += "</ul>";
		  }
		  document.getElementById(id2).innerHTML = output;
		  
		  var tests = JSON.parse(myObj.AllTests).data;
		  output = "Here are the list of Tests in the course: <br>";
		  output += "<ul class=\"list-group\"  >";
		  
		  /*<li class="list-group-item">First item</li>
		  <li class="list-group-item">Second item</li>
		  <li class="list-group-item">Third item</li>*/
		
		  for (x in tests){
			  //output += "<li >";
			  output += "<li class=\"list-group-item list-group-item-dark\" style=\"color:blue\">";
			  output += tests[x].test_id;
			  output += "    ";
			  output += tests[x].name;
			  output += "    ";
			  output += "<a href=\"UploadAnswers?course_id=" + course_id + "&semester=" + semester + "&year=" + year +  "&test_id=" + tests[x].test_id +"\"> Click to upload answers <\/a>";
			  output += "    ";
			  output += "<a href=\"QuestionPaper?course_id=" + course_id + "&semester=" + semester + "&year=" + year + "&test_id=" + tests[x].test_id +"\"> Click to download the paper <\/a>";
			  output += "    ";
			  output += "<a href=\"TestMarks?course_id=" + course_id + "&semester=" + semester + "&year=" + year + "&test_id=" + tests[x].test_id +"\"> Click to check standings <\/a>";
			  //output += " <br>";
			  output += "</li>";
		  }
		  output += "</ul>";
		  
		  document.getElementById(id3).innerHTML = output;
	    }
	  };
	 

	  
	  //console.log(course_id);
	  xhttp.open("GET", "CourseMeta" + "?course_id=" + course_id + "&semester=" + semester + "&year=" + year, true);
	  xhttp.send();
}


function mynewfunc(){
    var mycourse_id = "<% out.print(request.getParameter("course_id")); %>";
    var mysemester = "<% out.print(request.getParameter("semester")); %>";
    var myyear = "<% out.print(request.getParameter("year")); %>";
    var element1 = document.getElementById("TA_Roll"); 

	console.log('wtf');
		xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = function() {
			if (this.readyState == 4 && this.status == 200) {
				console.log(JSON.parse(this.responseText));
				element1.value="";
				FillMeta('meta', 'TAs', 'Tests');
				
			}
		};
		xhttp.open("GET", "AddTA?course_id="+mycourse_id+"&semester="+mysemester+"&year="+myyear+"&rollnumber="+element1.value);
	xhttp.send();	
}
</script>
</head>
<body>


<script>
function mylogout(){
	xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
	  if (this.readyState == 4 && this.status == 200) {
	    //console.log(this.responseText);
	  	var myObj = JSON.parse(this.responseText);
	    // Check Suvvess
	    console.log(myObj.status);
	    if (myObj.status){
	  	  //alert("Grader should not be null");
	  	  console.log(myObj.status);
	  	  window.location.href = '/EDGE/LoginPage';
	    }
	    else {
	    	console.log(myObj.status);
	    }
	  }
	};
	xhttp.open("POST", "Logout", true);
	xhttp.send();
	
}
</script>
<input type="button" class="btn btn-info" value="Logout" style="float:right" onclick="mylogout()">

<div class="container-fluid">
    <div class="row justify-content-md-center whitecolor">
        <div class="col-md-auto">
          <h1 class="cover-heading text-center">
          Welcome to webpage of course <% out.print(request.getParameter("course_id")); %>. (<% out.print(request.getParameter("semester")); %>, <% out.print(request.getParameter("year")); %>)
          </h1>
        </div>
    </div>
    <div class="container">
    
<div id="meta" class="col-md-4 col-md-offset-4" style="color:lightblue;">

</div>
<br>
<br>

<hr align="center" width="75%">

<div id="TAs" class="col-md-4 col-md-offset-2" style="color:lightblue;">

</div>
<div id="addTA" class="col-md-4" style="color:lightblue;">
<div class="form-group">
	<div class="col-md-8">
	<input placeholder="Enter Roll Number of TA" id="TA_Roll" class="form-control">
	</div>
	<div class="col-md-4">
	<button onclick="mynewfunc()" id='addTAbutt' class="btn btn-sm btn-info">
		ADD TA
	</button>
		</div>
	</div>

</div>
<br>
<br><br><br>
<hr align="center" width="75%">

<div id="Tests"  class="col-md-7 col-md-offset-2" style="color:lightblue;" align="center">

</div>
<hr align="center" width="75%">

<div id="MakeTest" class="col-md-8 col-md-offset-2" style="color:blue;">
<button type="button" class="btn btn-outline-success btn-block" 
onclick="location.href='MakeTest?course_id=<% out.print(request.getParameter("course_id")); %>&semester=<% out.print(request.getParameter("semester")); %>&year=<% out.print(request.getParameter("year")); %>'"> 

 New Test
</button>
</div>
<br><br>

<hr align="center" width="75%">

</div>

</div>
</body>
</html>

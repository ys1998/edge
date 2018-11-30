<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Test Marks</title>

  <script src="https://code.jquery.com/jquery-1.11.1.min.js"></script>
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.min.css">
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/latex.js@0.11.1/dist/latex.min.js"></script>
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
  
  <link rel="stylesheet" type="text/css" href="MakeTest.css">

<script>


course_id = "<% out.print(request.getParameter("course_id")); %>";
semester = "<% out.print(request.getParameter("semester")); %>";
year = "<% out.print(request.getParameter("year")); %>";
test_id = "<% out.print(request.getParameter("test_id")); %>";

xhttp = new XMLHttpRequest();
xhttp.onreadystatechange = function() {
  if (this.readyState == 4 && this.status == 200) {
 	output = "";
 	output += "<table class=\"table table-dark\"> ";
 	output += "<thead> "
 	output += "<tr> ";
 	output += "		<th> index </th>";
 	output += "		<th> rollnumber </th>";
 	output += "		<th> name </th>";
 	output += "		<th> marks </th>";
 	output += "</tr> ";

 	var jsonobj = JSON.parse(this.responseText).data;
 	counter = 1;
 	console.log(jsonobj);
 	output += "<tbody> ";
 	for (x in jsonobj){
 		//console.log(jsonobj[x]);
 		//console.log('aaaaaaaaa');
 	 	output += "<tr> ";
 	 	output += "		<th> " + counter + " </th>";
 	 	output += "		<th> " + jsonobj[x].rollnumber + " </th>";
 	 	output += "		<th> " + jsonobj[x].name + "</th>";
 	 	if (jsonobj[x].sum_marks == -1){
 	 	 	output += "		<th style=\"color:red;\"> ABSENT </th>";
 	 	}
 	 	else {
 	 	 	output += "		<th> " + jsonobj[x].sum_marks + "</th>"; 	 		
 	 	}

 	 	output += "</tr> ";		
 	}
 	output += "</tbody> ";
 	counter+=1;
 	  document.getElementById('tab').innerHTML = output;
  }

};

//console.log(course_id);
xhttp.open("GET", "SendStats" + "?course_id=" + course_id + "&semester=" + semester + "&year=" + year + "&test_id=" + test_id, true);
xhttp.send();

</script>

</head>
<body>
<div class="col-md-7 col-md-offset-2">
<div id="tab">
	
</div>
</div>

</body>
</html>
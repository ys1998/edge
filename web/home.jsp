<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>

<html>

<head>
<meta charset="UTF-8">
<title>EDGE</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.min.css">
<script src="https://code.jquery.com/jquery-1.11.1.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>
<link rel="stylesheet" type="text/css" href="home.css">
<script src="home.js"></script>


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

<br> <br> <br> <br> <br>
<div class="container">
    
    <h2 class="whitecolor">Welcome Professor <% out.print(session.getAttribute("name")); %> <h2>
    <div class="row">
        <div id="current">
        </div>
    </div>
</div>

<hr align="center" width="75%">

<div class="container">
    
    <p class="whitecolor"> Past Semesters <p>
    <div class="row">
        <div id="past">
        </div>
    </div>
</div>

</body>

</html>
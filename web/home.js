$(document).ready(function () {
	// body...
  getCourses("current", getSem(), getYear());
  console.log(getSem(), getYear());
  getPastSems("past");
  
})

function getYear() {
  d = new Date();
  return d.getFullYear();
}

function getSem() {
	 d = new Date();
	  if (d.getMonth() <= 6)
		  return "Spring";
	  else
		  return "Fall";
}

function getCourses(id, sem, year) {
  // body...
  xhttp = new XMLHttpRequest();
  xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
      var myObj = JSON.parse(this.responseText);
      console.log(myObj);
      myObj = myObj.data;
      console.log(myObj);
      length = myObj.length;
      output = "";
      factor = 12/length;
      //console.log(factor);
      for (x in myObj){
        //console.log("<div class=\"col-sm-" + factor + "\" onclick=\"linkcourse(" + myObj[x].course_id + ");\" >");
    	output += "<div class=\"col-sm-" + factor + "\">";
        output += "  <a href=\"Course?course_id=" + myObj[x].course_id + "&semester=" + sem + "&year=" + year +  "\" class=\"btn btn-sq btn-primary\"> ";
        output += "    <i class=\"fa fa-user fa-5x\"</i>"
        output += "    <br/> ";
        output += "   " + myObj[x].course_id + " <br> ";
        output += "  </a> ";
        output += "</div> ";
        console.log("  <a href=\"Course?course_id=" + myObj[x].course_id + "&semester=" + sem + "&year=" + year +  "\" class=\"btn btn-sq btn-primary\"> ");
      }
      document.getElementById(id).innerHTML = output;
    }
  };
 
  xhttp.open("GET", "GetSemCourse" + "?semester=" + sem + "&year=" + year, true);
  xhttp.send();
}

function getPastSems(id) {
  // body...
  xhttp = new XMLHttpRequest();
  xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
      var myObj = JSON.parse(this.responseText);
      myObj = myObj.data;
      length = myObj.length;
      output = "";
      factor = 12/length;
      
      for (x in myObj){
        output += "<div class=\"col-sm-" + factor + "\"> ";
        output += "  <a class=\"btn btn-sq btn-primary\" onclick=\"getCourses(\'" + id + "\', \'" + myObj[x].semester + "\', \'" + myObj[x].year + "\')\"> ";
        output += "    <i class=\"fa fa-user fa-5x\"></i> ";
        output += "      " + myObj[x].semester + " <br> <br> " + myObj[x].year + " <br> ";
        output += "  </a> ";
        output += "</div> ";
        //console.log(myObj[x]);
        console.log("  <a class=\"btn btn-sq btn-primary\" onclick=\"getCourses(id, \"" + myObj[x].semester + "\", \"" + myObj[x].year + "\")\"> ");
      }
      document.getElementById(id).innerHTML = output;
    }
  };
  xhttp.open("GET", "GetPastSemList", true);
  xhttp.send();
}

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta charset="UTF-8">
  <meta http-equiv="content-type" content="text/html; charset=UTF-8">
  <meta http-equiv="content-language" content="en">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  
  <script src="https://code.jquery.com/jquery-1.11.1.min.js"></script>
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.min.css">
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/latex.js@0.11.1/dist/latex.min.js"></script>
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>

  <link rel="stylesheet" type="text/css" href="MakeTest.css">

    <title>EDGE</title>
  <script>
    function loader() {
      // console.log("YOOOO");
      // $('#latexArea1').hide();
      // $('#HidingButton1').hide();
    	AddForm("AddForm0");
    }
    function LatexGen(caller) {
      console.log(caller)
      var key = caller.substring(6);
      var textAreakey = ""
      var text = document.getElementById("Textarea"+key).value;
      var generator = new latexjs.HtmlGenerator({ hyphenate: false })

      generator = latexjs.parse(text, { generator: generator })
      $("#latexArea"+key).empty();
      document.getElementById("latexArea"+key).appendChild(generator.stylesAndScripts("https://cdn.jsdelivr.net/npm/latex.js@0.11.1/dist/"))
      document.getElementById("latexArea"+key).appendChild(generator.domFragment());

      $('#latexArea'+key).show();
      $('#HidingButton'+key).show();       
    }
    function HideLatex(caller) {
      var key = caller.substring(12)
      $('#latexArea'+key).empty();
      $('#latexArea'+key).hide();
      $('#HidingButton'+key).hide();
    }
    
    function LatexAnswerGen(caller) {
      console.log(caller)
      var key = caller.substring(12);
      var textAreakey = ""
      var text = document.getElementById("TextAnswerarea"+key).value;
      var generator = new latexjs.HtmlGenerator({ hyphenate: false })

      generator = latexjs.parse(text, { generator: generator })
      console.log(this)
      $("#latexAnswerArea"+key).empty();
      document.getElementById("latexAnswerArea"+key).appendChild(generator.stylesAndScripts("https://cdn.jsdelivr.net/npm/latex.js@0.11.1/dist/"))
      document.getElementById("latexAnswerArea"+key).appendChild(generator.domFragment());

      $('#latexAnswerArea'+key).show();
      $('#HidingAnswerButton'+key).show();       
    }
    
    function checkidexists(){
   
  	  course_id = "<% out.print(request.getParameter("course_id")); %>";
	  semester = "<% out.print(request.getParameter("semester")); %>";
	  year = "<% out.print(request.getParameter("year")); %>";

    	var test_id = document.getElementById('testID').value;
    	xhttp = new XMLHttpRequest();
    	  xhttp.onreadystatechange = function() {
    	    if (this.readyState == 4 && this.status == 200) {
    	    	console.log((this.responseText));
    	    	if (JSON.parse(this.responseText).count != 0){
    	    		console.log('aaaa');
    	    		 document.getElementById('msgtestid').innerHTML = "test id exists";
    	    	}
    	    	else{
    	    		console.log('aaaaaaa');
   	    		 document.getElementById('msgtestid').innerHTML = "";    	    		
    	    	}
    	    }
    	  };
    	  xhttp.open("GET", "CheckTestId" + "?course_id=" + course_id + "&semester=" + semester + "&year=" + year + "&test_id=" + test_id, true);
    	  xhttp.send();  
    }
    
    var city = "";

	function lookForCityChange()
	{
		console.log('ca;;ed');
	    var newCity = document.getElementById('testID').value;
	    if (newCity != city && newCity != "") {
	        city = newCity;
	        checkidexists();     // do whatever you need to do
	    }
	}
	
	setInterval(lookForCityChange, 100);

	
	function HideAnswerLatex(caller) {
      var key = caller.substring(18)
      $('#latexAnswerArea'+key).empty();
      $('#latexAnswerArea'+key).hide();
      $('#HidingAnswerButton'+key).hide();
    }

    function AddForm(caller) {
      var key = caller.substring(7);
      var nxt = parseInt(key) + 1;
      var item = document.getElementById(caller);
      var parent = item.parentNode;
      
      parent.removeChild(item);
      
      var f = document.createElement("div");
      f.setAttribute('id',"form"+nxt.toString());
      // f.setAttribute('class',"col-md-8")
      f.innerHTML = "Question Number " + nxt.toString() + "<br>";
      
      var d0 = document.createElement("div");
      d0.setAttribute('class',"col-md-4");

      var l0 = document.createElement("label");
      l0.setAttribute('for',"MaxMarks" + nxt.toString());
      l0.innerHTML = "Maximum Marks:  "
      d0.appendChild(l0);
      
      var t0 = document.createElement("input");
      t0.setAttribute('id',"MaxMarks" + nxt.toString());
      t0.setAttribute('type' , "Number");
      t0.setAttribute('class' , "form-control");
      d0.appendChild(t0);
      f.appendChild(d0);

      var d1 = document.createElement("div");
      d1.setAttribute('class',"col-md-8");

      var l3 = document.createElement("label");
      l3.setAttribute('for',"Pages" + nxt.toString());
      l3.innerHTML = "Pages Alloted :  "
      d1.appendChild(l3);
      
      var t3 = document.createElement("input");
      t3.setAttribute('class' , "form-control");
      t3.setAttribute('id',"Pages" + nxt.toString());
      t3.setAttribute('type' , "Number");
      d1.appendChild(t3);
      f.appendChild(d1);

      var d2 = document.createElement("div");
      d2.setAttribute('class',"col-md-6");


      var l1 = document.createElement("label");
      l1.setAttribute('for',"Textarea" + nxt.toString());
      l1.innerHTML = "Question Text:  "
      d2.appendChild(l1);
      
      var t1 = document.createElement("textarea");
      t1.setAttribute('class' , "form-control");
      t1.setAttribute('id',"Textarea" + nxt.toString());
      t1.setAttribute('rows',"4");
      t1.setAttribute('style',"max-width: 100%");
      d2.appendChild(t1);

      var s = document.createElement("input");
      s.setAttribute('id',"submit" + nxt.toString() );
      s.setAttribute('value',"Render");
      s.setAttribute('class',"btn btn-sm btn-info");
      s.setAttribute('type',"button");
      s.setAttribute('onclick',"return LatexGen(this.id)");
      d2.appendChild(s);

      var t = document.createElement("div");
      t.setAttribute('id', "latexArea"+ nxt.toString());
      d2.appendChild(t);

      var s2 = document.createElement("input");
      s2.setAttribute('id',"HidingButton" + nxt.toString() );
      s2.setAttribute('value',"Hide");
      s2.setAttribute('class',"btn btn-sm btn-warning");
      s2.setAttribute('type',"button");
      s2.setAttribute('onclick',"return HideLatex(this.id)");
      d2.appendChild(s2);

      f.appendChild(d2);


      var d3 = document.createElement("div");
      d3.setAttribute('class',"col-md-6");

      var l2 = document.createElement("label");
      l2.setAttribute('for',"TextAnswerarea" + nxt.toString());
      l2.innerHTML = "Answer Text:  "
      d3.appendChild(l2);
      
      var t2 = document.createElement("textarea");
      t2.setAttribute('id',"TextAnswerarea" + nxt.toString());
      t2.setAttribute('class' , "form-control");
      t2.setAttribute('rows',"4");
      t2.setAttribute('style',"max-width: 100%");
      d3.appendChild(t2);

      var s21 = document.createElement("input");
      s21.setAttribute('id',"submitAnswer" + nxt.toString() );
      s21.setAttribute('value',"Render");
      s21.setAttribute('class',"btn btn-sm btn-info");
      s21.setAttribute('type',"button");
      s21.setAttribute('onclick',"return LatexAnswerGen(this.id)");
      d3.appendChild(s21);

      var t21 = document.createElement("div");
      t21.setAttribute('id', "latexAnswerArea"+ nxt.toString());
      d3.appendChild(t21);

      var s22 = document.createElement("input");
      s22.setAttribute('id',"HidingAnswerButton" + nxt.toString() );
      s22.setAttribute('value',"Hide");
      s22.setAttribute('class',"btn btn-sm btn-warning");
      s22.setAttribute('type',"button");
      s22.setAttribute('onclick',"return HideAnswerLatex(this.id)");
      d3.appendChild(s22);

      f.appendChild(d3);

      var k = document.createElement("div");
      k.setAttribute('class',"form-group");
      k.innerHTML = "&nbsp";
      
      f.appendChild(k);



      parent.appendChild(f);
      var s3 = document.createElement("input");
      s3.setAttribute('id',"AddForm" + nxt.toString() );
      s3.setAttribute('class',"btn btn-lg btn-success btn-arrow-right");
      s3.setAttribute('value',"+");
      s3.setAttribute('type',"button");
      s3.setAttribute('onclick',"AddForm(this.id)");
      parent.appendChild(s3);

      $('#latexArea' + nxt.toString()).hide();
      $('#latexAnswerArea' + nxt.toString()).hide();
      $('#HidingButton' + nxt.toString()).hide();
      $('#HidingAnswerButton' + nxt.toString()).hide();
    }

    function ClearTest(argument) {
      console.log(document.getElementById("formAccumulater").lastChild.id);
      var str;
      str = document.getElementById("formAccumulater").lastChild.id;
      console.log(str);
      str = str.substring(7);
      console.log(str);
      var numQues = parseInt(str);
      document.getElementById("formAccumulater").removeChild(document.getElementById("formAccumulater").lastChild)
      var i;
      for (i = 1; i <= numQues; i++) {
        document.getElementById("formAccumulater").removeChild(document.getElementById("formAccumulater").lastChild)
      }

      var s3 = document.createElement("input");
      s3.setAttribute('id',"AddForm0");
      s3.setAttribute('value',"+");
      s3.setAttribute('class',"btn btn-lg btn-success btn-arrow-right");
      s3.setAttribute('type',"button");
      s3.setAttribute('onclick',"AddForm(this.id)");
      document.getElementById("formAccumulater").appendChild(s3);
      return; 
    }

    function SubmitTest() {
      console.log(document.getElementById("formAccumulater").lastChild.id);
      var str;
      str = document.getElementById("formAccumulater").lastChild.id;
      str = str.substring(7);
      var numQues = parseInt(str);

      var formData = new FormData();
      formData.append("course_id", "<% out.print(request.getParameter("course_id")); %>"); 
      formData.append("semester", "<% out.print(request.getParameter("semester")); %>");
      formData.append("year", "<% out.print(request.getParameter("year")); %>");

      formData.append("num_ques" ,numQues);
      if(document.getElementById("testName").value === "")
      {	alert("Incomplete Form");
      return;
      }
      if(document.getElementById("testID").value === "")
      {	alert("Incomplete Form");
      return;
      }
      if(document.getElementById("testDate").value === "")
      {	alert("Incomplete Form");
      return;
      }
      
      formData.append("name" , document.getElementById("testName").value )
      formData.append("test_id" , document.getElementById("testID").value )
      formData.append("test_date" , document.getElementById("testDate").value )
      
      var i;
      for (i = 1; i <= numQues; i++) {
    	  if(document.getElementById("MaxMarks"+i).value === "")
          {	alert("Incomplete Form");
          return;
          }
    	  if(parseInt(document.getElementById("MaxMarks"+i).value , 10) >= 1000)
          {	alert("Max marks for a given question must be less than 1000");
          return;
          }
    	  
    	  if(document.getElementById("Textarea"+i).value === "")
          {	alert("Incomplete Form");
          return;
          }if(document.getElementById("TextAnswerarea"+i).value === "")
          {	alert("Incomplete Form");
          return;
          }if(document.getElementById("Pages"+i).value === "")
          {
        	  alert("Incomplete Form");
          return;
          }
          if(parseInt(document.getElementById("Pages"+i).value , 10) < 1)
          {	
        	  alert("NonZero page allocation necesary");
          return;
          }
        formData.append("MaxMarks"+i , document.getElementById("MaxMarks"+i).value );
        formData.append("Textarea"+i , document.getElementById("Textarea"+i).value );
        formData.append("TextAnswerarea"+i , document.getElementById("TextAnswerarea"+i).value );
        formData.append("Pages"+i , document.getElementById("Pages"+i).value );
      }

      var request = new XMLHttpRequest();
      request.onreadystatechange = function(){
        if (this.readyState == 4 && this.status == 200){
          var tb = JSON.parse(this.responseText);
          console.log(tb)
          console.log(tb.status == true);
          if(tb.status == true){
        	  var mycourse_id = "<% out.print(request.getParameter("course_id")); %>";
              var mysemester = "<% out.print(request.getParameter("semester")); %>";
              var myyear = "<% out.print(request.getParameter("year")); %>";
            	window.location.href="Course?course_id="+mycourse_id+"&semester="+mysemester+"&year="+myyear;
        	  
          }
          else{
        	  alert("Test creation failed due to " + tb.message);
        	  return;
          }
     }
      }
      request.open("POST", "SubmitTest",true);
      request.send(formData);
      return;
    }

  </script>
</head>
<body onload="loader()">
  <div class="container-fluid">
    <div class="row justify-content-md-center whitecolor">
        <div class="col-md-auto">
          <h1 class="cover-heading text-center">Making A Test</h1>
        </div>
    </div>
  </div>

    <div class="container" id = "formAccumulater">
      <div class="row justify-content-md-center">
          <div class="col col-lg-3">
        Test ID : <input type="text" class="form-control" id="testID"> 
        <p id="msgtestid"></p>
          </div>
          <div class="col-md-5">
        Name Of Test : <input type="text" class="form-control" id="testName"> 
        </div>
        <div class="col col-lg-3">
        Test Date : <input type="date" class="form-control" id="testDate">
          </div>
      </div>
      <br>
      
      <input class="btn btn-lg btn-success" type="button" value="Submit" id="SubmitTest" onclick="SubmitTest()">
      <input class="btn btn-lg btn-danger" type="button" value="Clear" id="ClearTest" onclick="ClearTest()" style="float: right;">
      
      <br>
      <br>
      <br>
      <input class = "btn btn-lg btn-success btn-arrow-right" type="button" value="+" id="AddForm0" onclick="AddForm(this.id)">

    </div>

  </div>
</body>

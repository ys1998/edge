$(document).ready(function() {
    var today = new Date().toDateString();
	$('.today').html(today);
})

function Login() {
	// body...
	var uid = document.getElementById("userid").value;
	var pwd = document.getElementById("password").value;
	var url = "Login?uid="+uid+"&pwd="+pwd;
	console.log(url);
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
	    if (this.readyState == 4 && this.status == 200) {
	    	var tb = JSON.parse(this.responseText);
	        console.log(tb);
	        var typ = tb["type"];
	        if(tb["status"] && typ == "instructor")
	        {
	        	console.log("Going to Home");
	        document.location.href="Home";
//			window.location.replace("Home");
	        }
	        else{
	        	document.getElementById("ErrorCode").innerHTML = tb["message"];
	        }
	    }
	    else {
	         console.log('failed' + this.readyState + "   "+ this.status);
	      }
	  };
	 xhttp.open("GET", url, true);
	 xhttp.send();
	}
<!DOCTYPE html>
<html lang="en">
    <head>
        <title>HTML5 drag'n'drop file upload with Servlet</title>
          <script src="https://code.jquery.com/jquery-1.11.1.min.js"></script>
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.min.css">
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/latex.js@0.11.1/dist/latex.min.js"></script>
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
  
  <link rel="stylesheet" type="text/css" href="MakeTest.css">
  
        <script>
            window.onload = function() {
                var dropbox = document.getElementById("dropbox");
                dropbox.addEventListener("dragenter", noop, false);
                dropbox.addEventListener("dragexit", noop, false);
                dropbox.addEventListener("dragover", noop, false);
                dropbox.addEventListener("drop", dropUpload, false);
            }

            function noop(event) {
                event.stopPropagation();
                event.preventDefault();
            }

            function dropUpload(event) {
                noop(event);
                var files = event.dataTransfer.files;

                for (var i = 0; i < files.length; i++) {
                    upload(files[i]);
                }
            }

            function upload(file) {
                document.getElementById("status").innerHTML = "Uploading " + file.name;

                var formData = new FormData();
                formData.append("file", file);

                var xhr = new XMLHttpRequest();
                xhr.upload.addEventListener("progress", uploadProgress, false);
                xhr.addEventListener("load", uploadComplete, false);
                xhr.open("POST", "Upload?course_id=<% out.print(request.getParameter("course_id"));%>&year=<% out.print(request.getParameter("year"));%>&semester=<% out.print(request.getParameter("semester"));%>&test_id=<% out.print(request.getParameter("test_id"));%>", true); // If async=false, then you'll miss progress bar support.
                xhr.send(formData);
            }

            function uploadProgress(event) {
                // Note: doesn't work with async=false.
                var progress = Math.round(event.loaded / event.total * 100);
                document.getElementById("status").innerHTML = "Progress " + progress + "%";
            }

            function uploadComplete(event) {
                //document.getElementById("status").innerHTML = event.target.responseText;
                var msg = event.target.responseText;
                if (msg == "FAILED"){
                	document.location.href = "/EDGE/Home";
                }
                else {
                	document.location.href = "/EDGE/AssignGraders?course_id=<% out.print(request.getParameter("course_id"));%>&year=<% out.print(request.getParameter("year"));%>&semester=<% out.print(request.getParameter("semester"));%>&test_id=<% out.print(request.getParameter("test_id"));%>";
                }
            }
        </script>
        <style>
            #dropbox {
                /*width: 300px;
                height: 200px;*/
                border: 1px solid gray;
                border-radius: 1px;
                padding: 5px;
                color: gray;
                display: block;
    			margin-left: auto;
   	 			margin-right: auto;
   	 			width: 30%;
   	 			height: 10%;
            }
        </style>
    </head>
    <body>
    <div id="randoms"  class="col-md-7 col-md-offset-2" style="color:lightblue;" align="center">
    	<div>
  <h1 >Upload Your File Here!</h1>
  <h2 >Instructions to upload</h1>
  <p>1.) The zip should contain scanned pdfs of all students appeared for the examination</p>
  <p>2.) You are allowed to submit only once, so make sure to submit all files correctly</p>
  <p>3.) Once submitted you'll be redirected to the grader assignment page, where you can select a grader for every question</p>
  
</div>
  <br>
</div>
        <div><img src="https://www.cse.iitb.ac.in/~namanjain/dnd.png" class="img-thumbnail" id="dropbox" alt="drag & drop"> <p style="text-align:center">Drag and Drop Here</p></div>
        <div id="status"></div>
    </body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>EDGE</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.0/js/bootstrap.min.js">
    <script src="https://code.jquery.com/jquery-1.11.1.min.js"></script>
    <link rel="stylesheet" type="text/css" href="login.css">
    <script src="login.js"></script>
</head>
<body>

<br> <br> <br> <br> <br>
<div class="container">
    <div class="row">
        <div class='col-md-3'></div>
        <div class="col-md-6">
            <div class="login-box well">
                    
                        <legend>Sign In</legend>
                        <div class="form-group">
                            <input value='' id="userid" placeholder="uid" type="text" class="form-control" />
                        </div>
                        <div class="form-group">
                            <input id="password" value='' placeholder="Password" type="password" class="form-control" />
                        </div>
                        <div class="form-group">
                            <input type="submit" class="btn btn-default btn-login-submit btn-block m-t-md" value="Login" onclick="Login()" />
                        </div>
                        <div id = "ErrorCode">
                        </div>
                
            </div>
        </div>
        <div class='col-md-3'></div>
    </div>
</html>

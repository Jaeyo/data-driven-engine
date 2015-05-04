<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>data driven engine</title>

<!--  script src="http://code.jquery.com/jquery-1.11.2.min.js"></script -->

<!--  
<script src="http://jsplumbtoolkit.com/js/dom.jsPlumb-1.7.5-min.js"></script>
-->

<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.0/jquery.min.js"></script>
<script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.9.2/jquery-ui.min.js"></script>
<script src="http://cdnjs.cloudflare.com/ajax/libs/jsPlumb/1.4.1/jquery.jsPlumb-1.4.1-all-min.js"></script>

<link href="http://seogi1004.github.io/jui/lib/jui/jui.min.css" rel="stylesheet">
<script src="http://seogi1004.github.io/jui/lib/jui/jui.js"></script>

<link href="http://designmodo.github.io/Flat-UI/dist/css/flat-ui.min.css" rel="stylesheet">
<script src="http://designmodo.github.io/Flat-UI/dist/js/flat-ui.min.js"></script>

<style>
html, body, .container {
	height: 100%;
	width: 100%;
	line-height: 1;
}

* {
	margin : 0;
	padding : 0;
}

small {
	font-size: smaller !important;
	line-height: 1 !important;
	margin: 0 !important;
	padding: 0 !important;
}

hr {
	margin-top: 0.3em;
	margin-bottom: 0.3em;
}

.operation {
	font-size: medium !important;
}

.component-menu-container {
	width: 100%;
	height: 4em;
	left: 0;
	top: 0;
	position: absolute;
	background-color: rgb(194, 194, 194);
}

.component-container {
	top: 4em;
	position: absolute;
}

.component {
	margin : 0;
	background-color : rgb(244, 244, 244);
	color : black;
	border-radius : 0.3em;
	padding : 1em;
	float : left;
	position : absolute;
}
</style>

</head>
<body>

<script src="/resource/js/js_cols.min.js"></script>
<script src="/resource/js/home.js"></script>
<script type="text/javascript">
var addFileReaderBtn = new ComponentBtn('FileReader');
var addSimpleDeliverBtn = new ComponentBtn('SimpleDeliver');
var addConsolePrinter = new ComponentBtn('ConsolePrinter');
</script>

<div class="container">
	<div class="component-menu-container">
		<a href="#" class="btn btn-block btn-lg btn-primary" onclick="addFileReaderBtn.addComponent();">add FileReader</a>
		<a href="#" class="btn btn-block btn-lg btn-primary" onclick="addSimpleDeliverBtn.addComponent();">add SimpleDeliver</a>
		<a href="#" class="btn btn-block btn-lg btn-primary" onclick="addConsolePrinter.addComponent();">add ConsolePrinter</a>
	</div>
	<div id="componentContainerDiv" class="component-container">
	</div>
</div>

</body>
</html>
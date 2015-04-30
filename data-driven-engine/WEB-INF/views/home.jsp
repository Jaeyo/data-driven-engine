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

<div class="container">
	<div class="component-menu-container">
		<a id="addComponentBtn" href="#" class="btn btn-block btn-lg btn-primary">add component</a>
		<a id="addFilterBtn" href="#" class="btn btn-block btn-lg btn-primary">add filter</a>
		<a id="testBtn" href="#" class="btn btn-block btn-lg btn-primary">test button</a>
	</div>
	<div id="componentContainerDiv" class="component-container">
	</div>
</div>


<script type="text/javascript">
var controller;
var view;
var data;
var util;

var firstInstance;

function Controller() {
	this.addComponent = function(componentName){
		controller.ajaxCall('/DataFlow/AddComponent/'+componentName, 'post', {}, function(response){
			console.log(response); //TODO IMME
		});
	} //addComponent
	
	this.addConnection = function(sourceId, targetId){
		controller.ajaxCall('/DataFlow/AddConnection/'+sourceId+'/'+targetId , 'post', {}, function(response){
			console.log(response); //TODO IMME
		});
	} //addConnection
	
	this.refreshMap = function(){
		controller.ajaxCall('/DataFlow/Map', 'get', {}, function(response){
			console.log(response); //TODO IMME
		});
	} //refreshMap
	
	this.ajaxCall = function(url, type, data, onSuccess){
		$.ajax({
			url:url,
			type:type,
			dataType:'json',
			data:data,
			success:onSuccess,
			error:function(e){
				alert("Error\n" + e.responseText);
				console.error(e.statusText);
			}
		});
	} //ajaxCall
}; //Controller
controller = new Controller();

function View() {
	this.componentContainerDiv = $("#componentContainerDiv");
	this.addComponentBtn = $("#addComponentBtn");
	this.addFilterBtn = $("#addFilterBtn");
	this.testBtn = $("#testBtn");
	
	this.jsPlumbInstance;
	
	this.addComponent = function(componentName, x, y, type, name, id){
		var componentHtml = 
			'<div class="component">' + 
				'<h6>' + componentName + '</h6>' + 
				'<hr />' +
				'<small>type : ' + type + '</small><br />' +
				'<small>name : ' + name + '</small><br />' +
				'<small>id : ' + id + '</small>' +
				'<hr />' +
				'<a href="#" class="operation">start</a><br />' +
				'<a href="#" class="operation">stop</a><br />' +
				'<a href="#" class="operation">configuration</a><br />' +
			'</div>';
		var componentDOM = $(componentHtml);
		componentDOM.css({ left : x, top : y });
		view.jsPlumbInstance.draggable(componentDOM, { grid : [20, 20] });
		componentDOM.hide();
		view.componentContainerDiv.append(componentDOM);
		componentDOM.toggle("bounce", {}, 500);
	} //addComponent
	
	this.addComponent = function(x, y){
		var componentHtml = 
			'<div class="component">' + 
				'<h6>FileReader</h6>' + 
				'<hr />' +
				'<small>type : processor</small><br />' +
				'<small>name : FileReader</small><br />' +
				'<small>id : wowoiei3i2398389w</small>' +
				'<hr />' +
				'<a href="#" class="operation">start</a><br />' +
				'<a href="#" class="operation">stop</a><br />' +
				'<a href="#" class="operation">configuration</a><br />' +
			'</div>';
		var componentDOM = $(componentHtml);
		componentDOM.css({ left : x, top : y });
		view.jsPlumbInstance.draggable(componentDOM, { grid : [20, 20] });
		componentDOM.hide();
		view.componentContainerDiv.append(componentDOM);
		componentDOM.toggle("bounce", {}, 500);
	} //addComponent
	
	this.initComponentMenu = function(){
		view.addComponentBtn.click(function(){
			view.addComponent(0, 0);
		});
		view.addFilterBtn.click(function(){
			view.addComponent(0, 0);
		});
		view.testBtn.click(function(){
			//TODO IMME
		});
	} //initComponentMenu
	
	this.setDraggable = function(target){
		view.jsPlumbInstance.draggable(target, { grid : [20, 20] });
	} //setDraggable
}; //UI
view = new View();

function Data() {
}; //Data
data = new Data();

function Util() {
	this.rgb = function(r, g, b){
		return '#' + componentToHex(r) + componentToHex(g) + componentToHex(b);
	} //rgb
}; //Util
util = new Util;


jsPlumb.ready(function() {
	view.jsPlumbInstance = jsPlumb.getInstance({
		ConnectionOverlays: [
			[ "Arrow", { location: 1 } ],
			[ "Label", {
				location: 0.1,
				id: "label",
				cssClass: "aLabel"
			}]
		]
	});
	view.initComponentMenu();
});

jui.ready([ "ui.accordion" ], function(accordion) {
    accordion_1 = accordion(".accordion", {
        event: {
            open: function(index, e) {
                $(this.root).find("i").attr("class", "icon-arrow1");
                $(e.target).find("i").attr("class", "icon-arrow3");
            }
        },
        index: 1
    });
});
</script>
</body>
</html>
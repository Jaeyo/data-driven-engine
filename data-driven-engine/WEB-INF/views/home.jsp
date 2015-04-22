<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>data driven engine</title>

<script src="http://code.jquery.com/jquery-1.11.2.min.js"></script>
<script src="http://code.interactjs.io/interact-1.2.4.min.js"></script>

<style>
.component-menu {
	width : 10%;
	height : 10%;
	min-height : 6.5em;
	margin : 0px;
	background-color : #29e;
	color : white;
	border-radius : 0.75em;
	padding : 4%;
	float : left;
}
.dragable{
	-webkit-transform : translate(0px, 0px);
	transform : translate(0px, 0px);
}

.dragable-not-drop{
	-webkit-transform : translate(0px, 0px);
	transform : translate(0px, 0px);
}
</style>

</head>
<body>

<div class="component-menu dragable">add component</div>
<div class="component-menu dragable-not-drop">add filter</div>

<script type="text/javascript">
var controller;
var ui;
var data;

function Controller() {
	
}; //Controller
controller = new Controller();

function UI() {
	this.dragMoveListener = function(event){
		var target = event.target;
		x = (parseFloat(target.getAttribute('data-x')) || 0) + event.dx,
		y = (parseFloat(target.getAttribute('data-y')) || 0) + event.dy;
		
		target.style.webkitTransform = 
		target.style.transform = 
			'translate(' + x + 'px, ' + y + 'px)';
		
		target.setAttribute('data-x', x);
		target.setAttribute('data-y', y);
	} //dragMoveListener
	
	this.moveToStartPointListener = function(event){
		var target = event.target;
		target.style.webkitTransform = 
		target.style.transform = 
			'translate(0px, 0px)';
		
		target.setAttribute('data-x', 0);
		target.setAttribute('data-y', 0);
	} //moveToStartPointListener
	
	this.applyDragDrop = function(querySelectorWord){
		interact(querySelectorWord).draggable({
			inertia : true,
			restrict : {
				restriction : 'parent',
				endOnly : true,
				elementRect : { top : 0, left : 0, bottom : 1, right : 1}
			},
			onmove : ui.dragMoveListener
		});
	} //applyDragDrop
	
	this.applyDragNotDrop = function(querySelectorWord){
		interact(querySelectorWord).draggable({
			inertia : true,
			restrict : {
				restriction : 'parent',
				endOnly : true,
				elementRect : { top : 0, left : 0, bottom : 1, right : 1}
			},
			onmove : ui.dragMoveListener,
			onend : ui.moveToStartPointListener
		});
	} //appliDragNotDrop
}; //UI
ui = new UI();

function Data() {
	
}; //Data
data = new Data();

//INIT ----
ui.applyDragDrop('.dragable');
ui.applyDragNotDrop('.dragable-not-drop');

</script>
</body>
</html>
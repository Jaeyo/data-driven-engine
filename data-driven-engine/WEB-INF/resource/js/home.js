JsPlumbWrapper = function(){
	this.instance = jsPlumb.getInstance({
		ConnectionOverlays: [
		                     [ "Arrow", { location: 1 } ],
		                     [ "Label", {
		                    	 location: 0.1,
		                    	 id: "label",
		                    	 cssClass: "aLabel"
		                     }]
		]
	});
} //INIT
JsPlumbWrapper.prototype = {
	draggable: function(target){
		this.instance.draggable(target, { 
			grid: [20, 20],
			stop: function(e){
				var x = e.offsetX;
				var y = e.offsetY;
				var uuid = $(this).attr("id");
				controller.updateComponent(uuid, x, y);
			}
		});
	}, //setDraggable
	connect: function(sourceId, targetId){
		this.instance.connect({
			source: sourceId,
			target: targetId
		});
	}
} //JsPlumbWrapper
var jsPlumbWrapper = new JsPlumbWrapper();


ServerAdapter = function(){
} //INIT
ServerAdapter.prototype = {
	ajaxCall: function(url, type, data, onSuccess){
		$.ajax({
			url: url,
			type: type,
			dataType: 'json',
			data: data,
			success: onSuccess,
			error: function(e){
				alert("ERROR\n" + e.responseText);
				console.log(e.statusText);
			}
		});
	}, //ajaxCall
	addComponent: function(type, name, callback){
		this.ajaxCall('/DataFlow/AddComponent/' + type, 'post', {name: name}, function(response){
			callback(response);
		});
	}, //addComponent
	updateComponent: function(uuid, x, y){
		this.ajaxCall('/DataFlow/UpdateComponent/' + uuid, 'put', {x: x, y: y}, function(response){
			//do nothing
		});
	}, //updateComponent
	addConnection: function(sourceId, targetId, onSuccess){
		this.ajaxCall('/DataFlow/AddConnection/' + sourceId + '/' + targetId, 'post', {}, onSuccess);
	}, //addConnection
	getMap: function(){
		this.ajaxCall('/DataFlow/Map', 'get', {}, function(response){
			console.log(response);
		});
	}
} //ServerAdapter
var serverAdapter = new ServerAdapter();



Model = function(){
	this.componentMap = [];
	this.source;
	this.target;
} //INIT
Model.prototype = {
	addComponent: function(component){
		this.componentMap[component.getUUID()] = component;
	}, //addComponent
	setSource: function(uuid){ this.source = uuid; },
	getSource: function(){ return this.source; },
	setTarget: function(uuid){ this.target = uuid; },
	getTarget: function(){ return this.target; }
} //Model



View = function(){
} //INIT
View.prototype = {
	addComponent: function(component){
		var x = component.getX();
		var y = component.getY();
		var componentDom = component.getDom();
		componentDom.css({ left : x, top : y });
		jsPlumbWrapper.draggable(componentDom);
		$("#componentContainerDiv").append(componentDom);
	} //addComponent
} //View



Controller = function(){
	this.model = new Model();
	this.view = new View();
} //INIT
Controller.prototype = {
	clearComponents: function(){
		//TODO
	}, //clearComponents
	addComponent: function(component){
		this.model.addComponent(component);
		this.view.addComponent(component);
	}, //addComponent
	updateComponent: function(uuid, x, y){
		serverAdapter.updateComponent(uuid, x, y);
	}, //updateComponent
	setSource: function(uuid){
		this.model.setSource(uuid);
		this.connectSourceToTarget();
	}, //setSource
	setTarget: function(uuid){
		this.model.setTarget(uuid);
		this.connectSourceToTarget();
	}, //setTarget
	connectSourceToTarget: function(){
		if(this.model.getSource() == null || this.model.getTarget() == null)
			return;
		var source = this.model.getSource();
		var target = this.model.getTarget();
		this.model.setSource(null);
		this.model.setTarget(null);
		serverAdapter.addConnection(source, target, function(response){
			if(response.success != 1){
				alert("Error\n" + response.msg);
				return;
			} //if
			jsPlumbWrapper.connect(source, target);
		});
	} //connectSourceToTarget
} //Controller
var controller  = new Controller();



ComponentBtn = function(type){
	this.type = type;
} //ComponentBtn
ComponentBtn.prototype = {
	addComponent: function(){
		var name = 'input component name';
		serverAdapter.addComponent(this.type, name, function(response){
			if(response.success != 1){
				alert("Error\n" + JSON.stringify(response));
				return;
			} //if
			var component = new Component(
					response.component.name, 
					response.component.type, 
					response.component.uuid, 
					response.component.x, 
					response.component.y);
			controller.addComponent(component);
		});
	} //addComponent
} //ComponentBtn




ComponentModel = function(name, type, uuid, x, y){
	this.name = name;
	this.type = type;
	this.uuid = uuid;
	this.x = x;
	this.y = y;
} //INIT
ComponentModel.prototype = {
	getName: function(){ return this.name; },
	getType : function(){ return this.type; },
	getUUID: function(){ return this.uuid; },
	getX: function(){ return this.x; },
	getY: function(){ return this.y; }
} //ComponentModel



ComponentView = function(name, type, uuid){
	var componentHtml = 
			'<div class="component" id="' + uuid + '">' + 
				'<h6>' + name + '</h6>' + 
				'<hr />' +
				'<small>type : ' + type + '</small><br />' +
				'<small>uuid : ' + uuid + '</small>' +
				'<hr />' +
				'<a href="#" class="operation">start</a><br />' +
				'<a href="#" class="operation">stop</a><br />' +
				'<a href="#" class="operation" onclick="controller.setSource(\'' + uuid + '\');">set source</a><br />' +
				'<a href="#" class="operation" onclick="controller.setTarget(\'' + uuid + '\');">set target</a><br />' +
				'<a href="#" class="operation">configuration</a><br />' +
			'</div>';
		this.dom = $(componentHtml);
} //INIT
ComponentView.prototype = {
	getDom: function(){
		return this.dom;
	} //getDom
} //ComponentView



Component = function(name, type, uuid, x, y){
	this.model = new ComponentModel(name, type, uuid, x, y);
	this.view = new ComponentView(name, type, uuid);
} //INIT
Component.prototype = {
	connect: function(targetComponent){
		sourceUUID = this.getUUID();
		targetUUID = targetComponent.getUUID();
		serverAdapter.connect(sourceUUID, targetUUID, function(response){
			if(response.success == 0){
				alert('Error\n' + JSON.stringify(response));
				return;
			} //if
			
			jsPlumbWrapper.connect(sourceUUID, targetUUID);
		});
	}, //addConnection
	getDom: function(){
		return this.view.getDom();
	}, //getDom
	getUUID: function(){
		return this.model.getUUID();
	}, //getUUID
	getX: function(){ return this.model.x; },
	getY: function(){ return this.model.y; }
} //Component
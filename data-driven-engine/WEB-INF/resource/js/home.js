//js_cols.require('js_cols.HashMap');

//-------------------------------------------------------------------------------------

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
}; //INIT
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
	}, //draggable
	connect: function(sourceId, targetId){
		this.instance.connect({
			source: sourceId,
			target: targetId
		});
	}
}; //JsPlumbWrapper
var jsPlumbWrapper = new JsPlumbWrapper();

//-------------------------------------------------------------------------------------

ServerAdapter = function(){
}; //INIT
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
}; //ServerAdapter
var serverAdapter = new ServerAdapter();

//-------------------------------------------------------------------------------------

Model = function(){
	this.componentMap = [];
}; //INIT
Model.prototype = {
	addComponent: function(component){
		this.componentMap[component.getUUID()] = component;
	}, //addComponent
	getInputableComponents: function(){
		var retArr = [];
		for(var uuid in this.componentMap){
			var component = this.componentMap[uuid];
			if(component.isInputable())
				retArr.push(component);
		} //for uuid
		return retArr;
	}, //getInputableComponents
	getOutputableComponents: function(){
		var retArr = [];
		for(var uuid in this.componentMap){
			var component = this.componentMap[uuid];
			if(component.isOutputable())
				retArr.push(component);
		} //for uuid
		return retArr;
	}, //getOutputableComponents
	getComponent: function(uuid){
		return this.componentMap[uuid];
	} //getComponent
}; //Model

//-------------------------------------------------------------------------------------

View = function(){
}; //INIT
View.prototype = {
	addComponent: function(component){
		var x = component.getX();
		var y = component.getY();
		var componentDom = component.getDom();
		componentDom.css({ left : x, top : y });
		jsPlumbWrapper.draggable(componentDom);
		$("#componentContainerDiv").append(componentDom);
	} //addComponent
}; //View

//-------------------------------------------------------------------------------------

Controller = function(){
	this.model = new Model();
	this.view = new View();
}; //INIT
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
	viewConnectTargetList: function(uuid){
		var sourceComponent = this.model.getComponent(uuid);
		var components = this.model.getInputableComponents();
		sourceComponent.viewConnectTargetList(components);
	}, //viewConnectTargetList
	connect: function(sourceUUID, targetUUID){
		jsPlumbWrapper.connect(sourceUUID, targetUUID);
	} //connect
}; //Controller
var controller  = new Controller();

//-------------------------------------------------------------------------------------

ComponentBtn = function(type){
	this.type = type;
}; //ComponentBtn
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
					response.component.y,
					response.component.inputable,
					response.component.outputable);
			controller.addComponent(component);
		});
	} //addComponent
}; //ComponentBtn

//-------------------------------------------------------------------------------------

ComponentModel = function(name, type, uuid, x, y, inputable, outputable){
	this.name = name;
	this.type = type;
	this.uuid = uuid;
	this.x = x;
	this.y = y;
	this.inputable = inputable;
	this.outputable = outputable;
}; //INIT
ComponentModel.prototype = {
	getName: function(){ return this.name; },
	getType : function(){ return this.type; },
	getUUID: function(){ return this.uuid; },
	getX: function(){ return this.x; },
	getY: function(){ return this.y; },
	isInputable: function(){ return this.inputable; },
	isOutputable: function(){ return this.outputable; }
}; //ComponentModel

//-------------------------------------------------------------------------------------

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
				'<a href="#" class="operation" onclick="controller.viewConnectTargetList(\'' + uuid + '\')">connect</a><br />' +
				'<div class="connect-target-list" />' +
				'<a href="#" class="operation">configuration</a><br />' +
			'</div>';
		this.dom = $(componentHtml);
}; //INIT
ComponentView.prototype = {
	getDom: function(){
		return this.dom;
	}, //getDom
	viewConnectTargetList: function(myUUID, inputableComponents){
		var connectTargetListHtml = "";
		for(var i=0; i<inputableComponents.length; i++){
			connectTargetListHtml +=
				'<a href="#" onclick="controller.connect(\'' + myUUID + '\', \'' + inputableComponents[i].getUUID() + '\')">' + inputableComponents[i].getName() + '</a><br />';
		} //for i
		$('#' + myUUID).find(".connect-target-list")[0].innerHTML = connectTargetListHtml;
	} //viewConnectTargetList
}; //ComponentView

//-------------------------------------------------------------------------------------

Component = function(name, type, uuid, x, y, inputable, outputable){
	this.model = new ComponentModel(name, type, uuid, x, y, inputable, outputable);
	this.view = new ComponentView(name, type, uuid);
}; //INIT
Component.prototype = {
	connect: function(targetComponent){
		var sourceUUID = this.getUUID();
		var targetUUID = targetComponent.getUUID();
		serverAdapter.connect(sourceUUID, targetUUID, function(response){
			if(response.success === 0){
				alert('Error\n' + JSON.stringify(response));
				return;
			} //if
			
			jsPlumbWrapper.connect(sourceUUID, targetUUID);
		});
	}, //addConnection
	viewConnectTargetList: function(inputableComponents){
		var myUUID = this.getUUID();
		inputableComponents = inputableComponents.filter(function(component){ 
			return component.getUUID() != myUUID;
		});
		this.view.viewConnectTargetList(myUUID, inputableComponents);
	}, //viewConnectTargetList
	getDom: function(){
		return this.view.getDom();
	}, //getDom
	getUUID: function(){ return this.model.getUUID(); },
	getName: function(){ return this.model.getName(); },
	getX: function(){ return this.model.getX(); },
	getY: function(){ return this.model.getY(); },
	isInputable: function(){ return this.model.isInputable(); },
	isOutputable: function(){ return this.model.isOutputable(); }
}; //Component

//-------------------------------------------------------------------------------------

ComponentConfigModel = function(){
	this.configMap = new js_cols.HashMap();
};
ComponentConfigModel.prototype = {
	set: function(key, value){
		this.configMap.insert(key, value);
	}, //set
	get: function(key){
		return this.configMap.get(key);
	}, //get
	getConfig: function(){
		return this.configMap;
	} //getConfig
};

//-------------------------------------------------------------------------------------

ComponentConfigView = function(){
};
ComponentConfigView.prototype = {
	showDialog: function(configMap){
		var html = '<div class="component-config-dialog">';
		configMap.forEach(function(value, key, map){
			html += '<label>' + key + '</label>';
			html += '<input type="text" value="' + value + '" />';
		});
		html += '</div>';
		var dialog = null;
		dialog = $(html).dialog({
			autoOpen: false,
			height: 300,
			width: 350,
			modal: true,
			buttons: {
				"OK": function(){ console.log("ok"); },
				"Cancel": function(){
					console.log("cancel");
					dialog.dialog("close");
				}
			} //buttons
		});
		dialog.dialog("open");
	} //showDialog
};

//-------------------------------------------------------------------------------------

ComponentConfig = function(){
	this.model = new ComponentConfigModel();
	this.view = new ComponentConfigView();
}; //INIT
ComponentConfig.prototype = {
	showDialog: function(){
		var configMap = this.model.getConfig();
		this.view.showDialog(configMap);
	} //showDialog
};
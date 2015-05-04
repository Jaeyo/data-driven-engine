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
				//var x = e.offsetX;
				//var y = e.offsetY;
				var x = target.css('left').replace('px', '');
				var y = target.css('top').replace('px', '');
				var uuid = $(this).attr('id');
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
	getMap: function(onSuccess){
		this.ajaxCall('/DataFlow/Map', 'get', {}, onSuccess);
	}
}; //ServerAdapter
var serverAdapter = new ServerAdapter();

//-------------------------------------------------------------------------------------

Model = function(){
	this.componentMap = new js_cols.HashMap();
}; //INIT
Model.prototype = {
	addComponent: function(component){
		this.componentMap.insert(component.getUUID(), component);
	}, //addComponent
	getInputableComponents: function(){
		var inputableComponents = new js_cols.LinkedList();
		this.componentMap.forEach(function(component, uuid, map){
			if(component.isInputable())
				inputableComponents.addLast(component);
		});
		return inputableComponents;
	}, //getInputableComponents
	getOutputableComponents: function(){
		var outputableComponents = new js_cols.LinkedList();
		this.componentMap.forEach(function(component, uuid, map){
			if(component.isOUtputable())
				outputableComponents.addLast(component);
		});
		return outputableComponents;
	}, //getOutputableComponents
	getComponent: function(uuid){
		return this.componentMap.get(uuid);
	}, //getComponent
	removeAllUUIDs: function(){
		var uuids = new js_cols.LinkedList();
		this.componentMap.forEach(function(component, uuid, map){
			uuids.addLast(uuid);
		});
		this.componentMap.clear();
		return uuids;
	} //removeAll
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
	}, //addComponent
	removeComponents: function(uuids){
		uuids.forEach(function(uuid, key, list){
			$("#" + uuid).remove();
		});
	} //removeCompoents
}; //View

//-------------------------------------------------------------------------------------

Controller = function(){
	this.model = new Model();
	this.view = new View();
}; //INIT
Controller.prototype = {
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
	}, //connect
	clearMap: function(){
		var uuids = this.model.removeAllUUIDs();
		this.view.removeComponents(uuids);
	}, //clearMap
	refreshMap: function(){
		this.clearMap();
		serverAdapter.getMap(function(response){
			if(response.success != 1){
				console.log('error');
				console.log(response);
				return;
			} //if
			var componentsJson = response.map.components;
			var linesJson = response.map.lines;
			
			for(var i=0; i<componentsJson.length; i++){
				var component = new Component(
						componentsJson[i].name,
						componentsJson[i].type,
						componentsJson[i].uuid,
						componentsJson[i].x,
						componentsJson[i].y,
						componentsJson[i].inputable,
						componentsJson[i].outputable);
				controller.addComponent(component);
			} //for i
			//TODO handle linesJson
		});
	} //refreshMap
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
		inputableComponents.forEach(function(component, key, list){
			connectTargetListHtml +=
				'<a href="#" onclick="controller.connect(\'' + myUUID + '\', \'' + component.getUUID() + '\')">' + component.getName() + '</a><br />';
		});
		$('#' + myUUID).find(".connect-target-list")[0].innerHTML = connectTargetListHtml;
	} //viewConnectTargetList
}; //ComponentView

//-------------------------------------------------------------------------------------

Component = function(name, type, uuid, x, y, inputable, outputable){
	this.model = new ComponentModel(name, type, uuid, x, y, inputable, outputable);
	this.view = new ComponentView(name, type, uuid);
	this.config = new ComponentConfig();
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
		inputableComponents = inputableComponents.filter(function(component, key, list){ 
			return component.getUUID() != myUUID;
		});
		this.view.viewConnectTargetList(myUUID, inputableComponents);
	}, //viewConnectTargetList
	showConfig: function(){
		this.config.showDialog();
	}, //showConfig
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
}; //ComponentConfig

//-------------------------------------------------------------------------------------

ConnectionConfigModel = function(){
	this.connectionMap = new js_cols.HashMap();
}; //ConnectionConfigModel
ConnectionConfigModel.prototype = {
	set: function(key, value){
		this.connectionMap.set(key, value);
	}, //set
	get: function(key){
		return this.connectionMap.get(key);
	}, //get
	getConnectionMap: function(){
		return this.connectionMap;
	} //getConnectionMap
}; //ConnectionConfigModel

//-------------------------------------------------------------------------------------

ConnectionConfigView = function(){
	//TODO IMME
}; //ConnectionConfigView
ConnectionConfigView.prototype = {
	//TODO IMME
}; //COnnectionConfigView

//-------------------------------------------------------------------------------------

ConnectionConfig = function(){
	this.model = new ConnectionConfigModel();
	this.view = new ConnectionConfigView();
} //INIT
ConnectionConfig.prototype = {
	//TODO IMME
}; //ConnectionConfig

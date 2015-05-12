var format = function(str, binding){
	for(var key in binding){
		var regEx = new RegExp("\\{" + key + "\\}", "gm");
		str = str.replace(regEx, binding[key]);
	} //for key
	return str;
}; //format

JsPlumbWrapper = function(){
	this.instance = jsPlumb.getInstance({
		Endpoint: ["Dot", {radius: 2}],
		HoverPaintStyle: {strokeStyle: "#1e8151", lineWidth: 2},
		ConnectionOverlays: [
		    [ "Arrow", { 
		    	location: 1,
		    	id: "arrow",
		    	length: 14,
		    	foldback: 0.8
		    } ]
		],
		Container: "componentContainerDiv"
	});
	
	this.instance.bind("click", function(info, originalEvent){
		jsPlumbWrapper.detach(info.sourceId, info.targetId);
	});
	
	this.instance.bind("connection", function(info, originalEvent){
		serverAdapter.addConnection(info.sourceId, info.targetId, function(response){
			if(response.success != 1)
				jsPlumbWrapper.detach(info.sourceId, info.targetId);
		}); 
	});
	
	this.instance.bind("connectionDetached", function(info, originalEvent){
		serverAdapter.removeConnection(info.sourceId, info.targetId);
	});
}; //INIT
JsPlumbWrapper.prototype = {
	draggable: function(target){
		var uuid = target.attr('id');
		this.instance.draggable(target, { 
			grid: [20, 20],
			stop: function(e){
				var x = target.css('left').replace('px', '');
				var y = target.css('top').replace('px', '');
				controller.updateComponent(uuid, x, y);
			}
		});
	
		var component = controller.getComponent(uuid);
		if(component.isInputable() == true){
			this.instance.makeTarget(target, {
				dropOptions: { hoverClass: "dragHover" },
				anchor: "Continuous",
				allowLoopback: false
			});
		} //if
		if(component.isOutputable() == true){
			this.instance.makeSource(target, {
				filter: ".end-point",
				anchor: "Continuous",
				connector: ["StateMachine", { curviness: 20 }],
				connectorStyle: {strokeStyle: "#5c96bc", lineWidth: 2, outlineColor: "transparent", outlineWidth: 4},
				maxConnections: 1
			});
		} else{
			target.find(".end-point").hide();
		} //if
	}, //draggable
	connect: function(sourceId, targetId){
		this.instance.connect({
			source: sourceId,
			target: targetId
		});
	}, //connect
	detach: function(sourceId, targetId){
		this.instance.detach({
			source: sourceId,
			target: targetId
		})
	} //detach
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
				console.log('error');
				console.log(e);
			}
		});
	}, //ajaxCall
	addComponent: function(type, name, callback){
		this.ajaxCall('/DataFlow/Component/' + type, 'post', {name: name}, function(response){
			callback(response);
		});
	}, //addComponent
	updateComponent: function(uuid, x, y){
		this.ajaxCall('/DataFlow/Component/' + uuid, 'put', {x: x, y: y}, function(response){
			//do nothing
		});
	}, //updateComponent
	startComponent: function(uuid){
		this.ajaxCall('/DataFlow/StartComponent/' + uuid, 'put', {}, function(response){
			if(response.success != 1){
				console.log('error');
				console.log(response);
				return;
			} //if
		});
	}, //startComponent
	stopComponent: function(uuid){
		this.ajaxCall('/DataFlow/StopComponent/' + uuid, 'put', {}, function(response){
			if(response.success != 1){
				console.log('error');
				console.log(response);
				return;
			} //if
		});
	}, //stopComponent
	addConnection: function(sourceId, targetId){
		this.ajaxCall('/DataFlow/Connection/' + sourceId + '/' + targetId, 'post', {}, function(response){
			if(response.success != 1){
				console.log('error');
				console.log(response);
				return;
			} //if
		});
	}, //addConnection
	addConnection: function(sourceId, targetId, onSuccess){
		this.ajaxCall('/DataFlow/Connection/' + sourceId + '/' + targetId, 'post', {}, onSuccess);
	}, //addConnection
	removeConnection: function(sourceId, targetId){
		this.ajaxCall('/DataFlow/Connection/' + sourceId + '/' + targetId, 'delete', {}, function(response){
			if(response.success != 1){
				console.log('error');
				console.log(response);
//				controller.refreshMap();
				return;
			} //if
		});
	}, //removeConnection
	getMap: function(onSuccess){
		this.ajaxCall('/DataFlow/Map', 'get', {}, onSuccess);
	}, //getMap
	rename: function(uuid, name){
		this.ajaxCall('/DataFlow/Component/' + uuid, 'put', {name: name}, function(response){
			//do nothing
		});
	}, //rename
	getConfig: function(uuid, onSuccess){
		var config = new js_cols.HashMap();
		this.ajaxCall('/DataFlow/Config/' + uuid, 'get', {}, onSuccess);
	}, //getConfig
	setConfig: function(uuid, configMap){
		var config = {};
		configMap.forEach(function(value, key, map){
			config[key] = value;
		});
		this.ajaxCall('/DataFlow/Config/' + uuid, 'put', config, function(response){
			if(response.success != 1){
				console.log('error');
				console.log(response);
				controller.refreshMap();
				return;
			} //if
		});
	} //setConfig
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
			if(component.isOutputable())
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
		$("#componentContainerDiv").append(componentDom);
		jsPlumbWrapper.draggable(componentDom);
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
	getComponent: function(uuid){
		return this.model.getComponent(uuid);
	}, //getComponent
	updateComponent: function(uuid, x, y){
		serverAdapter.updateComponent(uuid, x, y);
	}, //updateComponent
	startComponent: function(uuid){
		serverAdapter.startComponent(uuid);
	}, //startComponent
	stopComponent: function(uuid){
		serverAdapter.stopComponent(uuid);
	}, //stopComponent
	clearMap: function(){
		var uuids = this.model.removeAllUUIDs();
		this.view.removeComponents(uuids);
	}, //clearMap
	showRenameDialog: function(uuid){
		var component = controller.getComponent(uuid);
		var renameTitleDom= $("#renameTitleTmpl").tmpl({name: component.getName(), uuid: uuid});
		var renameTitleHtml = renameTitleDom.html();
		component.getDom().find(".component-title-area")[0].innerHTML = renameTitleHtml;
	}, //showRenameDialog
	rename: function(uuid){
		var component = controller.getComponent(uuid);
		var name = component.getDom().find("input").val();
		serverAdapter.rename(uuid, name);
		controller.refreshMap();
	},
	showConfigDialog: function(uuid){
		this.model.getComponent(uuid).showConfigDialog();
	}, //showConfigDialog
	connect: function(sourceUUID, targetUUID){
		this.model.getComponent(sourceUUID).connect(targetUUID);
	}, //connect
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
			
			for(var i=0; i<linesJson.length; i++){
				var sourceUUID = linesJson[i].source;
				var targetUUID = linesJson[i].target;
				controller.connect(sourceUUID, targetUUID);
			} //for i
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
	this.dom = $("#componentTmpl").tmpl({uuid: uuid, type: type, name: name});
}; //INIT
ComponentView.prototype = {
	getDom: function(){
		return this.dom;
	}, //getDom
	showRenameDialog: function(myUUID, oldName){
		new ComponentRenameDialog().show(myUUID, oldName);
	}, //showRenameDialog
	showConfigDialog: function(myUUID){
		new ComponentConfigDialog().show(myUUID);
	} //showConfigDialog
}; //ComponentView

//-------------------------------------------------------------------------------------

Component = function(name, type, uuid, x, y, inputable, outputable){
	this.model = new ComponentModel(name, type, uuid, x, y, inputable, outputable);
	this.view = new ComponentView(name, type, uuid);
}; //INIT
Component.prototype = {
	connect: function(targetUUID){
		jsPlumbWrapper.connect(this.getUUID(), targetUUID);
	}, //connect
	showRenameDialog: function(){
		this.view.showRenameDialog(this.model.getUUID(), this.model.getName());
	}, //showRenameDialog
	showConfigDialog: function(){
		this.view.showConfigDialog(this.model.getUUID());
	}, //showConfigDialog
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

ComponentRenameDialog = function(){};
ComponentRenameDialog.prototype = {
	show: function(uuid, oldName){
		var html = 
			'<div class="component-rename-dialog">'+
			'<label>name</label><br />'+
			'<input id="renameInput" type="text" style="width:300px;" value="'+oldName+'" />'+
			'</div>';
		var dialog = null;
		dialog = $(html).dialog({
			autoOpen: false,
			height: 300,
			width: 350,
			modal: true,
			buttons: {
				"OK": function(){
					var newName = $("#renameInput").val();
					serverAdapter.rename(uuid, newName);
					controller.refreshMap();
					dialog.dialog("destroy").remove();
				},
				"Cancel": function(){
					dialog.dialog("destroy").remove();
				}
			} //buttons
		});
		dialog.dialog("open");
	} //show
};

//-------------------------------------------------------------------------------------

ComponentConfigDialog = function(){ };
ComponentConfigDialog.prototype = {
	show: function(uuid){
		var parseConfigMapFunc = this.parseConfigMap;
		var makeHtmlFunc = this.makeHtml;
		var makeDialogFunc = this.makeDialog;
		serverAdapter.getConfig(uuid, function(response){
			var configMap = parseConfigMapFunc(response);
			var html = makeHtmlFunc(configMap);
			var dialog = makeDialogFunc(uuid, html);
			dialog.dialog("open");
		});
	}, //show
	parseConfigMap: function(response){
		var configMap = new js_cols.HashMap();
		if(response.success != 1){
			console.log('error');
			console.log(response);
			return null;
		} //if
		
		for(var key in response.config){
			if(response.config.hasOwnProperty(key) == false)
				continue;
			configMap.insert(key, response.config[key]);
		} //for key
		return configMap;
	}, //parseConfigMap
	makeHtml: function(configMap){
		var html = '<div class="component-config-dialog">';
		configMap.forEach(function(value, key, map){
			html += '<label class="component-config-key">'+key+'</label><br />';
			html += '<input class="component-config-value" type="text" style="width:300px;" value="'+value+'" />';
		});
		html += '</div>';
		return html;
	}, //makeHtml
	makeDialog: function(uuid, html){
		var dialog = null;
		dialog = $(html).dialog({
			autoOpen: false,
			height: 300,
			width: 350,
			modal: true,
			buttons: {
				"OK": function(){
					var newConfig = new js_cols.HashMap();
					var keys = $(".component-config-key");
					var values = $(".component-config-value");
					for(var i=0; i<keys.length; i++){
						var key = $(keys[i]).text();
						var value = $(values[i]).val();
						newConfig.insert(key, value);
					} //for i
					serverAdapter.setConfig(uuid, newConfig);
					dialog.dialog("destroy").remove();
				},
				"Cancel": function(){
					dialog.dialog("destroy").remove();
				}
			} //buttons
		});
		return dialog;
	} //makeDialog
};
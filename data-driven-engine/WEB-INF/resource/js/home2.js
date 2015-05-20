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
		Container: "componentContainer"
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
				serverAdapter.updateComponent(uuid, x, y);
			}
		});
	
		var component = controller.model.getComponent(uuid);
		if(component.model.inputable == true){
			this.instance.makeTarget(target, {
				dropOptions: { hoverClass: "dragHover" },
				anchor: "Continuous",
				allowLoopback: false
			});
		} //if
		if(component.model.outputable == true){
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
				handleError(e);
			}
		});
	}, //ajaxCall
	addNewComponent: function(type, name, callback){
		this.ajaxCall('/DataFlow/Component/' + type, 'post', {name: name}, callback);
	}, //addNewComponent
	updateComponent: function(uuid, x, y){
		this.ajaxCall('/DataFlow/Component/' + uuid, 'put', {x: x, y: y}, function(response){
			if(response.success != 1)
				handleError(response);
			//do nothing
		});
	}, //updateComponent
	startComponent: function(uuid){
		this.ajaxCall('/DataFlow/StartComponent/' + uuid, 'put', {}, function(response){
			if(response.success != 1){
				handleError(response);
				return;
			} //if
		});
	}, //startComponent
	stopComponent: function(uuid){
		this.ajaxCall('/DataFlow/StopComponent/' + uuid, 'put', {}, function(response){
			if(response.success != 1){
				handleError(response);
				return;
			} //if
		});
	}, //stopComponent
	removeComponent: function(uuid, onSuccess){
		this.ajaxCall('/DataFlow/RemoveComponent/' + uuid, 'delete', {}, onSuccess);
	}, //removeComponent
	addConnection: function(sourceId, targetId, onSuccess){
		this.ajaxCall('/DataFlow/Connection/' + sourceId + '/' + targetId, 'post', {}, onSuccess);
	}, //addConnection
	removeConnection: function(sourceId, targetId){
		this.ajaxCall('/DataFlow/Connection/' + sourceId + '/' + targetId, 'delete', {}, function(response){
			if(response.success != 1){
				handleError(response);
				return;
			} //if
		});
	}, //removeConnection
	getMap: function(onSuccess){
		this.ajaxCall('/DataFlow/Map', 'get', {}, onSuccess);
	}, //getMap
	rename: function(uuid, name){
		this.ajaxCall('/DataFlow/Component/' + uuid, 'put', {name: name}, function(resp){
			if(resp.success != 1){ handleError(resp); return; }
			controller.refreshMap();
		});
	}, //rename
	getConfig: function(uuid, onConfig){
		this.ajaxCall('/DataFlow/Config/' + uuid, 'get', {}, function(resp){
			var configMap = new js_cols.HashMap();
			for(var key in resp.config){
				if(resp.config.hasOwnProperty(key) == false)
					continue;
				configMap.insert(key, resp.config[key]);
			} //for key
			onConfig(configMap);
		});
	}, //getConfig
	setConfig: function(uuid, configMap){
		var config = {};
		configMap.forEach(function(value, key, map){
			config[key] = value;
		});
		this.ajaxCall('/DataFlow/Config/' + uuid, 'put', config, function(resp){
			if(response.success != 1){ handleError(resp); return; }
			controller.refreshMap();
		});
	} //setConfig
}; //ServerAdapter
var serverAdapter = new ServerAdapter();

//-------------------------------------------------------------------------------------

Model = function(){
	this.cpnts = new js_cols.HashMap();
}; //INIT
Model.prototype = {
	insertComponent: function(cpnt){
		this.cpnts.insert(cpnt.model.uuid, cpnt);
	}, //insertComponent
	getComponent: function(uuid){
		return this.cpnts.get(uuid);
	} //getComponent
}; //Model

View = function(){
}; //INIT
View.prototype = {
	showComponent: function(cpnt){
		var cpntDom = cpnt.view.dom;
		cpntDom.css({ left: cpnt.model.x, top: cpnt.model.y });
		$(".component-container").append(cpntDom);
		jsPlumbWrapper.draggable(cpntDom);
	}, //showComponent
	viewConfig: function(configArr, uuid){
		$("#menuContainerBottom").empty();
		$("#configTmpl").tmpl({configArr: configArr, uuid: uuid}).appendTo($("#menuContainerBottom"));
		var focusDom = $("#menuContainerBottom").find("input");
		if(focusDom.length > 0)
			focusDom.focus();
	}, //viewConfi
	viewRenameConfig: function(uuid, name){
		$("#menuContainerBottom").empty();
		$("#renameTmpl").tmpl({uuid: uuid, name: name}).appendTo($("#menuContainerBottom"));
		var focusDom = $("#menuContainerBottom").find("input");
		if(focusDom.length > 0)
			focusDom.focus();
	} //viweRenameConfig
}; //View

Controller = function(){
	this.model = new Model();
	this.view = new View();
}; //INIT
Controller.prototype = {
	addNewComponent: function(cpntType){
		var cpntName = 'component name';
		serverAdapter.addNewComponent(cpntType, cpntName, function(resp){
			if(resp.success != 1){ handleError(resp); return; } 
			
			var cpnt = new Component(
				resp.component.name,
				resp.component.type,
				resp.component.uuid,
				resp.component.x,
				resp.component.y,
				resp.component.inputable,
				resp.component.outputable,
				resp.component.started);
			cpnt.model.x = '15em';
			
			controller.model.insertComponent(cpnt);
			controller.view.showComponent(cpnt);
		});
	}, //addNewComponent
	removeComponent: function(uuid){
		var cpnt = this.model.getComponent(uuid);
		if(cpnt.model.started == true){
			$("#btnRemoveComponent").notify("started component cannot be removed", "error");
			return;
		} //if
		
		serverAdapter.removeComponent(uuid, function(resp){
			if(resp.success != 1){
				handleError(resp);
				$("#btnRemoveComponent").notify(resp.msg, "error");
				return;
			} //if
			controller.refreshMap();
		});
	}, //removeComponent
	startComponent: function(uuid){
		serverAdapter.startComponent(uuid);
		this.refreshMap();
	}, //startComponent
	stopComponent: function(uuid){
		serverAdapter.stopComponent(uuid);
		this.refreshMap();
	}, //stopComponent
	refreshMap: function(){
		$(".component-container").empty();
		this.model = new Model();
		this.view = new View();
		
		serverAdapter.getMap(function(resp){
			if(resp.success != 1){ handleError(resp); return; }
			
			var cpntsJson = resp.map.components;
			var linesJson = resp.map.lines;
			
			for(var i=0; i<cpntsJson.length; i++){
				var cpnt = new Component(
					cpntsJson[i].name,
					cpntsJson[i].type,
					cpntsJson[i].uuid,
					cpntsJson[i].x,
					cpntsJson[i].y,
					cpntsJson[i].inputable,
					cpntsJson[i].outputable,
					cpntsJson[i].started);
				controller.model.insertComponent(cpnt);
				controller.view.showComponent(cpnt);
			} //for i
			
			for(var i=0; i<linesJson.length; i++){
				var sourceUUID = linesJson[i].source;
				var targetUUID = linesJson[i].target;
				jsPlumbWrapper.connect(sourceUUID, targetUUID);
			} //for i
			
		});
	}, //refreshMap
	viewConfig: function(uuid){
		serverAdapter.getConfig(uuid, function(configMap){
			var configArr = [];
			configMap.forEach(function(value, key, map){
				var configObj = {};
				configObj.key = key;
				configObj.value = value;
				configArr.push(configObj);
			});
			
			controller.view.viewConfig(configArr, uuid);
		});
	}, //viewConfig
	setConfig: function(uuid){
		var configKeys = $("#menuContainerBottom").find("label");
		var configValues = $("#menuContainerBottom").find("input");
		var configMap = new js_cols.HashMap();
		for(var i=0; i<configKeys.length; i++){
			var key = configKeys[i].innerHTML;
			var value = configValues[i].value;
			configMap.insert(key, value);
		} //for i
		serverAdapter.setConfig(uuid, configMap);
	}, //setConfig
	viewRenameConfig: function(uuid){
		var name = this.model.getComponent(uuid).model.name;
		this.view.viewRenameConfig(uuid, name);
	}, //viewRenameConfig
	rename: function(uuid){
		var newName = $("#menuContainerBottom").find("input").val();
		serverAdapter.rename(uuid, newName);
	}, //rename
	toggleComponentOutput: function(uuid){
		this.model.getComponent(uuid).toggleOutput();
	} //viewOutputOfComponent
}; //Controller
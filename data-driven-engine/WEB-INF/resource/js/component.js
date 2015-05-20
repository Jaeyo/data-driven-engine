ComponentModel = function(name, type, uuid, x, y, inputable, outputable, started){
	this.name = name;
	this.type = type;
	this.uuid = uuid;
	this.x = x;
	this.y = y;
	this.inputable = inputable;
	this.outputable = outputable;
	this.started = started;
	this.isOutputOpened = false;
}; //INIT
ComponentModel.prototype = {
}; //ComponentModel



ComponentView = function(name, type, uuid, started){
	this.dom = $("#componentTmpl").tmpl({name: name, type: type, uuid: uuid});
	if(started){
		this.dom.find(".component-running-signal").css("background-color", "green");
	} else{
		this.dom.find(".component-running-signal").css("background-color", "red");
	} //if
}; //INIT
ComponentView.prototype = {
	toggleOutput: function(isOpened){
		if(isOpened){
			this.dom.find(".component-output-area").find(".component-output").remove();
		} else{
			var textareaDom = $("componentOutputTmpl").tmpl({});
			this.dom.find(".component-output-area").append(textareaDom);
		} //if
	}, //viewOutput
	appendIntoOutput: function(msg){
		var textareaDom = this.dom.find(".component-output");
		textareaDom.val(msg + "\n" + textareaDom.val());
	} //appendIntoOutput
}; //ComponentView



Component = function(name, type, uuid, x, y, inputable, outputable, started){
	this.model = new ComponentModel(name, type, uuid, x, y, inputable, outputable, started);
	this.view = new ComponentView(name, type, uuid, started);
}; //INIT
Component.prototype = {
	toggleOutput: function(){
		if(this.model.isOutputOpened == true){
			this.sock.close();
			this.view.toggleOutput(true);
		} else{
			this.model.isOutputOpened = true;
			this.view.toggleOutput(false);
	
			this.sock = new WebSocket();
			var viewObj = this.view;
			this.sock.listenComponentOutput(this.model.uuid, function(msg){
				viewObj.appendIntoOutput(msg);
			});
		} //if
	}, //viewOutput
}; //Component
ComponentModel = function(name, type, uuid, x, y, inputable, outputable, started){
	this.name = name;
	this.type = type;
	this.uuid = uuid;
	this.x = x;
	this.y = y;
	this.inputable = inputable;
	this.outputable = outputable;
	this.started = started;
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
}; //ComponentView



Component = function(name, type, uuid, x, y, inputable, outputable, started){
	this.model = new ComponentModel(name, type, uuid, x, y, inputable, outputable, started);
	this.view = new ComponentView(name, type, uuid, started);
}; //INIT
Component.prototype = {
		
}; //Component
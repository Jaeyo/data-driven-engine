var format = function(str, binding){
	for(var key in binding){
		var regEx = new RegExp("\\{" + key + "\\}", "gm");
		str = str.replace(regEx, binding[key]);
	} //for key
	return str;
}; //format

var handleError = function(err){
	console.log('error');
	console.log(err);
}; //handleError
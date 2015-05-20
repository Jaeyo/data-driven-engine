WebSocket = function(){
}; //INIT
WebSocket.prototype = {
	listenComponentOutput: function(uuid, onMsg){
		this.sock = new SockJS("/ComponentOutput");
		this.sock.onmessage = onMsg;
		this.sock.onclose = function(){
			console.log("websocket ComponentOutput with uuid " + uuid + " has closed");
		};
		this.sock.send(uuid);
	}, //listenComponentOutput
	close: function(){
		this.sock.close();
	} //close
}; //WebSocket
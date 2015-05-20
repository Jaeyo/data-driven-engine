WebSocketAdapter = function(){
}; //INIT
WebSocketAdapter.prototype = {
	listenComponentOutput: function(uuid, onMsg){
		this.sock = new SockJS("/ComponentOutput", {});
		this.sock.onmessage = onMsg;
		this.sock.onopen = function(){
			console.log("websocket with uuid " + uuid + " opened");
			this.send(uuid);
		} //open
		this.sock.onclose = function(){
			console.log("websocket ComponentOutput with uuid " + uuid + " has closed");
		};
	}, //listenComponentOutput
	close: function(){
		this.sock.close();
	} //close
}; //WebSocket
var SL_Socket = function(){
	this.timer = null;
	this.url = null;
	this.connectCycle = 30000;
	this.tempSocket = null;
	this.socket = null;
	this.msgListener = null;
};

SL_Socket.CONNECT_CMD = "connect";
SL_Socket.DISCONNECT_CMD = "disconnect";

//设定url
SL_Socket.prototype.setUrl = function(_url){
	this.url = _url;
};
//设定重连周期
SL_Socket.prototype.setReconnectCycle = function(cycle){
	this.connectCycle = cycle*1000;
};
//设定消息监听函数,函数携带一个参数，SL_Socket作为函数的this，参数是消息字符串
SL_Socket.prototype.setMsgListener = function(listener){
	this.msgListener = listener;
};
//启动服务
SL_Socket.prototype.start = function(){
	if(!window.WebSocket) {alert("not support websocket.");return;}

	this.stop();
	this.timer = window.setInterval(this._checkConnect.bind(this),this.connectCycle);
	this.connect();
};
//停止服务
SL_Socket.prototype.stop = function(){
	window.clearInterval(this.timer);
	this.disconnect();
};
//检查连接函数
SL_Socket.prototype._checkConnect = function(){
	if(this.isConnect()){
		return;
	}
	this.disconnect();
	this.connect();
};
//触发连接
SL_Socket.prototype.connect = function(){
	if(this.isConnect()){ //判断是否在连接中，如果连接中，直接返回
		return false;
	}
	this.tempSocket = new WebSocket(this.url);
	this.tempSocket.onopen = function(){
		this.socket = this.tempSocket;
		if(this.msgListener){
			this.msgListener.bind(this)(SL_Socket.CONNECT_CMD);
		}
	}.bind(this);
	this.tempSocket.onclose = function(){
		this.disconnect();
		if(this.msgListener){
			this.msgListener.bind(this)(SL_Socket.DISCONNECT_CMD);
		}
	}.bind(this);
	this.tempSocket.onmessage = function(message){
		if(this.msgListener){
			this.msgListener.bind(this)(message.data);
		}
	}.bind(this);
};
//断开当前的连接
SL_Socket.prototype.disconnect = function(){
	if(this.tempSocket){
		this.tempSocket.close();
		this.tempSocket = null;
	}
	if(this.socket){
		this.socket.close();
		this.socket = null;
		return true;
	}
	return false;
};
//是否连接中
SL_Socket.prototype.isConnect = function(){
	return this.socket != null;
};
//发送消息
SL_Socket.prototype.send = function(infoStr){
	if(this.isConnect()){
		this.socket.send(infoStr);
		return true;
	}
	return false;
};
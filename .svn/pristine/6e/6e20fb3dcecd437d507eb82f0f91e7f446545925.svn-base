package sl.server.web;

import java.util.HashMap;
import java.util.UUID;

import org.json.JSONObject;

import server.xzy.websocket.XZYWebSocketServer.XZYWebSocket;

public class WebSocketManager {
	public static final Object WEBSOCKET_UID_FLAG = new Object();
	private static WebSocketManager singleInstance = new WebSocketManager();
	private HashMap<String,XZYWebSocket> socketMapping = new HashMap<String,XZYWebSocket>();
	
	public static WebSocketManager getSingleInstance(){
		return singleInstance;
	}
	
	public void addSocket(XZYWebSocket conn){
		String uid = UUID.randomUUID().toString();
		conn.setInfo(WebSocketManager.WEBSOCKET_UID_FLAG, uid);
		synchronized(socketMapping){
			socketMapping.put(uid, conn);
		}
	}
	public void removeSocket(XZYWebSocket conn){
		String uid = (String)conn.getInfo(WEBSOCKET_UID_FLAG);
		synchronized(socketMapping){
			socketMapping.remove(uid);
		}
	}
	
	public XZYWebSocket getSocket(String socketUid){
		synchronized(socketMapping){
			return socketMapping.get(socketUid);
		}
	}
	
	public boolean sendMessage(String socketUid,JSONObject info){
		XZYWebSocket conn = this.getSocket(socketUid);
		if(conn == null){
			return false;
		}
		conn.sendMessage(info.toString());
		return true;
	}
}

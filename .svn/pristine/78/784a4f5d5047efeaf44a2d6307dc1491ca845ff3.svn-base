package sl.server.web;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;
import javax.persistence.criteria.CriteriaBuilder.In;

import org.json.JSONObject;

import com.xzy.base_c.InfoContainer;

import server.xzy.socket.IReleaser;
import server.xzy.socket.XZYSocket;
import server.xzy.websocket.XZYWebSocketServer.XZYWebSocket;
import sl.util.QrCodeUtil;
import sun.misc.BASE64Encoder;

public class SLWebSocketReleaser implements IReleaser {
    @Override
    public void init(XZYSocket socketServer) throws Exception {

    }

    @Override
    public void execute(InfoContainer cmdInfo) {
        XZYWebSocket conn = (XZYWebSocket) cmdInfo
            .getInfo(XZYSocket.SOCKET_FLAG);

        Object cmd = cmdInfo.getInfo(XZYSocket.CMD_FLAG);
        Object data = cmdInfo.getInfo(XZYSocket.DATA_FLAG);
        System.out.println("data ========================" + data);
        System.out.println("------------------------ " + cmd);

        if (cmd == XZYSocket.SOCKET_CONNECT_CMD) {
            try {
                WebSocketManager.getSingleInstance().addSocket(conn);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (cmd == XZYSocket.SOCKET_DISCONNECT_CMD) {
            WebSocketManager.getSingleInstance().removeSocket(conn);
        } else {
            try {
                if (data != null) {
                	if(Integer.valueOf(data.toString()) == 0x01){
                		//生成二维码请求
                		String uid = conn.getInfo(WebSocketManager.WEBSOCKET_UID_FLAG).toString();
                        JSONObject json = new JSONObject();
                        BufferedImage img = QrCodeUtil.createQrCode(
                            "http://192.168.1.215:8080/login.go?method=loginByQCode&uid="
                                    + uid,
                            128, "png", null);

                        System.out.println(
                            "http://192.168.1.215:8080/login.go?method=loginByQCode&uid="
                                    + uid);
                        ByteArrayOutputStream out = new ByteArrayOutputStream(
                                1024 * 64);
                        ImageIO.write(img, "png", out);
                        BASE64Encoder encoder = new BASE64Encoder();
                        json.put("qrcode", "data:image/png;base64,"
                                + encoder.encode(out.toByteArray()));
                        json.put("msg", "login_qr_code");
                        WebSocketManager.getSingleInstance().sendMessage(
                            conn.getInfo(WebSocketManager.WEBSOCKET_UID_FLAG)
                                .toString(),
                            json);
                	}
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

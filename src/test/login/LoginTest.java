package test.login;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import com.xzy.base.Util;
import com.xzy.base.server.event.DefaultEventCenterServer;
import com.xzy.base.server.log.LogRecordServer;
import com.xzy.base_c.XZYStart;
import com.xzy.base_i.IEvent;
import com.xzy.base_i.IEventListener;
import com.xzy.base_i.IEventResponse;

import server.xzy.ui.IJSListener;
import server.xzy.ui.WebFrameServer;

public class LoginTest {
    private static boolean server = false;

    public static synchronized void init() {
        LogRecordServer.getSingleInstance().startServer();
        if (!server) {
            server = true;
            XZYStart.main(new String[0]);
        }
    }

    private String verifyCode = null;

    @Test
    public void testLoginByAccountPwd() {
        LoginTest.init();

        DefaultEventCenterServer.getSingleInstance()
            .registEventListener("verify_create", new IEventListener() {

                @Override
                public boolean hasInteresting(IEvent event) {
                    // TODO Auto-generated method stub
                    return true;
                }

                @Override
                public IEventResponse dispose(IEvent event) {
                    try {
                        verifyCode = (String) event.getEventPara().get("code");
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    return null;
                }

            });

        Util.sleep(1000);
        final WebFrameServer server = new WebFrameServer();
        server.setServerName("WebFrameTest");
        server.addPara("listener", new IJSListener() {
            @Override
            public Object js2Java(Object[] argv) {
                if (argv[0].equals("onload")) {
                    JSONObject infos = new JSONObject();
                    try {
                        infos.put("account", "moyer22");
                        infos.put("pwd", "123");
                        infos.put("verify", verifyCode);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    server.executeJavaScript("window.loginByAccount", infos);
                } else if (argv[0].equals("ondestroy")) {
                    server.stopServer();
                } else if (argv[0].equals("testresult")) {

                }
                return null;
            }
        });
        server.addPara("url",
            "D:\\EntersSellsSaves\\SL_Manager\\src\\test\\login\\loginByAccount.html");
        server.startServer();

        Util.sleep(100000);
    }

    @Test
    public void testLoginByToken() {

    }
}

package sl.service.impl;

import java.awt.image.BufferedImage;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.xzy.base.server.db.RedisServer;
import com.xzy.base.server.event.DefaultEventCenterServer;
import com.xzy.base_c.BasicEvent;

import sl.dao.TLoginDao;
import sl.dao.TUserDao;
import sl.dao.impl.LoginDaoImpl;
import sl.dao.impl.UserDaoImpl;
import sl.domain.User;
import sl.service.TLoginService;
import sl.util.SlLoginConfig;
import sl.util.VerificationCodeUtil;
import web.xzy.base.HttpInfo;

/**
 * 
 * @author moyer
 * @date 2018��1��18��
 */
public class LoginServiceImpl implements TLoginService {

    private TLoginDao loginDao = new LoginDaoImpl();
    private TUserDao  userDao  = new UserDaoImpl();

    @Override
    public User validationName(String name) {
        return loginDao.validationName(name);
    }

    @Override
    public User loginByAccount(String name, String password) throws Exception {
        return loginDao.loginByAccount(name, password);
    }

    @Override
    public String loginByToken(String token) throws Exception {
        String userInfoStr = RedisServer.getSingleInstance().getValue(token);
        if (StringUtils.isBlank(userInfoStr)) {
            // ����
            return null;
        }
        // У����û���Ϣ����mysql���Ƿ�һ��
        JSONObject userObj = new JSONObject(userInfoStr);
        long userId = userObj.getLong(SlLoginConfig.REDIES_USERID);
        String upwd = userObj.getString(SlLoginConfig.REDIES_UPASSWORD);

        String user = RedisServer.getSingleInstance()
            .getValue(String.valueOf(userId));
        if (!StringUtils.isBlank(user)) {
            // ͨ���û���Ϣ��ȡredis�е��û���Ϣ�� ���Աȣ��Ƿ����
            if (!upwd.equals(user)) {
                // ���벻ƥ��
                return null;
            }
        }

        // ����tokne������ʱ��
        RedisServer.getSingleInstance().expire(token,
            SlLoginConfig.REDIS_TOKEN_OUERDUE);
        return userInfoStr;
    }

    @Override
    public BufferedImage genCaptcha(HttpServletRequest request,
            HttpServletResponse response) {
        // ����һ��У����
        String captcha = VerificationCodeUtil.genCaptcha(4);
        // �����ɵ���֤�����redis�У��������ù���ʱ��
        String sessionId = request.getSession().getId();
        System.out.println(captcha + "  " + sessionId);
        try {
            DefaultEventCenterServer.getSingleInstance()
                .notify(new BasicEvent(this, "verify_create", new JSONObject()
                    .put("code", captcha).put("session", sessionId)));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RedisServer.getSingleInstance().setValue(
            sessionId + SlLoginConfig.CAPTCHA, captcha.toUpperCase(),
            SlLoginConfig.REDIS_CODE_OUERDUE);

        // ��У����תΪͼ�񷵻�
        BufferedImage image = VerificationCodeUtil.genCaptchaImg(captcha);
        return image;
    }

    @Override
    public String exitLogin(String tokenkey) throws Exception {
        // �����token����Ϊ���ڣ�����û�����������token���ǿ��Լ���ʹ��
        RedisServer.getSingleInstance().removeKey(tokenkey);
        return null;
    }

    @Override
    public String byToken(HttpInfo httpInfo, HttpServletRequest request) {
        // ��url��ȡtoken
        String checkToken = null;

        checkToken = httpInfo.getString(SlLoginConfig.TT_TOKEN);
        if (StringUtils.isAllBlank(checkToken)) {
            // 1:ȡcookie�е�token
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(SlLoginConfig.TT_TOKEN)) {
                    checkToken = cookie.getValue().toString();
                }
            }
        }
        return checkToken;
    }
}

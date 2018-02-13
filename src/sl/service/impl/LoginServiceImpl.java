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
 * @date 2018年1月18日
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
            // 过期
            return null;
        }
        // 校验此用户信息，与mysql中是否一致
        JSONObject userObj = new JSONObject(userInfoStr);
        long userId = userObj.getLong(SlLoginConfig.REDIES_USERID);
        String upwd = userObj.getString(SlLoginConfig.REDIES_UPASSWORD);

        String user = RedisServer.getSingleInstance()
            .getValue(String.valueOf(userId));
        if (!StringUtils.isBlank(user)) {
            // 通过用户信息获取redis中的用户信息， 并对比，是否过期
            if (!upwd.equals(user)) {
                // 密码不匹配
                return null;
            }
        }

        // 更新tokne的生命时长
        RedisServer.getSingleInstance().expire(token,
            SlLoginConfig.REDIS_TOKEN_OUERDUE);
        return userInfoStr;
    }

    @Override
    public BufferedImage genCaptcha(HttpServletRequest request,
            HttpServletResponse response) {
        // 生成一个校验码
        String captcha = VerificationCodeUtil.genCaptcha(4);
        // 将生成的验证码存入redis中，并且设置过期时间
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

        // 把校验码转为图像返回
        BufferedImage image = VerificationCodeUtil.genCaptchaImg(captcha);
        return image;
    }

    @Override
    public String exitLogin(String tokenkey) throws Exception {
        // 将这个token设置为过期，如果用户还有其他的token还是可以继续使用
        RedisServer.getSingleInstance().removeKey(tokenkey);
        return null;
    }

    @Override
    public String byToken(HttpInfo httpInfo, HttpServletRequest request) {
        // 从url获取token
        String checkToken = null;

        checkToken = httpInfo.getString(SlLoginConfig.TT_TOKEN);
        if (StringUtils.isAllBlank(checkToken)) {
            // 1:取cookie中的token
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

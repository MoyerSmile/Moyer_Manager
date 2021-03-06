package sl.action;

import java.awt.image.BufferedImage;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import com.xzy.base.server.db.RedisServer;

import io.jsonwebtoken.Claims;
import sl.domain.User;
import sl.server.web.WebSocketManager;
import sl.service.TLoginService;
import sl.service.impl.LoginServiceImpl;
import sl.util.JsonReader;
import sl.util.MD5Util;
import sl.util.SlLoginConfig;
import sl.util.TokenUtil;
import web.xzy.base.HttpAction;
import web.xzy.base.HttpInfo;

public class LoginAction extends HttpAction {

    private TLoginService loginService = new LoginServiceImpl();

    /**
     * 创建登录验证码
     * 
     * @param httpInfo
     * @param request
     * @param response
     * @return
     */
    public void createLoginVerify(HttpInfo httpInfo, HttpServletRequest request,
            HttpServletResponse response) {
        try {
            // 把校验码转为图像
            BufferedImage image = loginService.genCaptcha(request, response);
            response.setContentType("image/jpeg");
            // 输出图像
            ServletOutputStream outStream = response.getOutputStream();
            ImageIO.write(image, "jpeg", outStream);
            outStream.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 使用账号密码登录
     * 
     * @param httpInfo
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public String loginByAccount(HttpInfo httpInfo, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        JSONObject result = new JSONObject();
        try {
            JSONObject json = JsonReader.receivePost(request);
            String userName = json.get(SlLoginConfig.USERNAME).toString();
            String password = json.get(SlLoginConfig.PASSWORD).toString();
            String code = json.get(SlLoginConfig.CODE).toString();
            // 是否需要踢掉已经登录的用户
            if (SlLoginConfig.LOGNI_ONLINE) {

            }

            String checkToken = loginService.byToken(httpInfo, request);
            if (checkToken != null) {
                RedisServer.getSingleInstance().removeKey(checkToken);
            }
            // 1判断验证码是否输入正确
            String sessionId = request.getSession().getId();
            String captcha = RedisServer.getSingleInstance()
                .getValue(sessionId + SlLoginConfig.CAPTCHA);
            if (captcha == null || !captcha.equals(code.toUpperCase())) {
                result.put("result", SlLoginConfig.FAILURE);
                result.put("cause", "验证码输入错误");
                this.writeJson(httpInfo, request, response, result);
                return null;
            }

            // 2判断用户名是否正确
            User user = loginService.validationName(userName);
            if (user == null) {
                result.put("result", SlLoginConfig.FAILURE);
                result.put("cause", "用户名不存在");
                this.writeJson(httpInfo, request, response, result);
                return null;
            }

            // 3判断用户名与密码(md5加密)是否匹配
            User user1 = loginService.loginByAccount(userName,
                MD5Util.getMD5(password));
            if (user1 == null) {
                result.put("result", SlLoginConfig.FAILURE);
                result.put("cause", "用户名与密码不匹配");
                this.writeJson(httpInfo, request, response, result);
                return null;
            }

            // 4生成token
            Map<String, Object> claims = new HashMap<>();
            claims.put("iss", SlLoginConfig.JWT_ISS);// 发行人
            String token = TokenUtil.createJWT(sessionId, user1.getName(),
                claims, SlLoginConfig.JWT_OUERDUE);

            // token保存到redis中，过期时间为s
            JSONObject userObj = new JSONObject();
            userObj.put(SlLoginConfig.REDIES_USERID, user.getId());
            userObj.put(SlLoginConfig.REDIES_USERNAME, user.getName());
            userObj.put(SlLoginConfig.REDIES_UPASSWORD, user.getPassword());
            RedisServer.getSingleInstance().setValue(token, userObj.toString(),
                SlLoginConfig.REDIS_TOKEN_OUERDUE);

            // 将用户信息也同步到token
            RedisServer.getSingleInstance()
                .setValue(String.valueOf(user.getId()), user.getPassword());

            // 将token保存到cookis中
            Cookie cookie = new Cookie(SlLoginConfig.TT_TOKEN, token);
            cookie.setHttpOnly(true);
            cookie.setMaxAge(365 * 24 * 60 * 60);
            response.addCookie(cookie);

            result.put("result", SlLoginConfig.SUCCESSFUL);
            result.put(SlLoginConfig.REDIES_USERNAME,
                URLDecoder.decode(user1.getName(), "utf-8"));
            result.put("cause", "登录成功");
            this.writeJson(httpInfo, request, response, result);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", SlLoginConfig.FAILURE);
            result.put("cause", "连接超时，请稍后再试");
            this.writeJson(httpInfo, request, response, result);
        }
        return null;
    }

    /**
     * 使用token登录，验证token的合法和有效性
     * 
     * @param httpInfo
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public String loginByToken(HttpInfo httpInfo, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        JSONObject result = new JSONObject();
        try {
            String checkToken = loginService.byToken(httpInfo, request);
            if (StringUtils.isBlank(checkToken)) {
                result.put("result", SlLoginConfig.FAILURE);
                result.put("cause", "没有token，请登录");
                this.writeJson(httpInfo, request, response, result);
                return null;
            }

            try {
                Claims claims = TokenUtil.parseJWT(checkToken);// 验证token是否被更改
            } catch (Exception e) {
                result.put("result", SlLoginConfig.FAILURE);
                result.put("cause", "token过期，请重新登录");
                this.writeJson(httpInfo, request, response, result);
                return null;
            }

            // 查询redis是否存在token,并检查有效性
            String userInfoStr = loginService.loginByToken(checkToken);
            if (StringUtils.isBlank(userInfoStr)) {
                result.put("result", SlLoginConfig.FAILURE);
                result.put("cause", "token过期，请重新登录");
                this.writeJson(httpInfo, request, response, result);
                return null;
            }
            JSONObject userObj = new JSONObject(userInfoStr);
            String userName = userObj.getString(SlLoginConfig.REDIES_USERNAME);
            result.put("result", SlLoginConfig.SUCCESSFUL);
            result.put(SlLoginConfig.REDIES_USERNAME,
                URLDecoder.decode(userName, "utf-8"));
            result.put("cause", "使用token登录成功");

            Cookie cookie = new Cookie(SlLoginConfig.TT_TOKEN, checkToken);
            cookie.setHttpOnly(true);
            cookie.setMaxAge(365 * 24 * 60 * 60);
            response.addCookie(cookie);

            this.writeJson(httpInfo, request, response, result);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", SlLoginConfig.FAILURE);
            result.put("cause", "连接超时，请稍后再试");
            this.writeJson(httpInfo, request, response, result);
        }
        return null;
    }

    /**
     * 使用二维码登录
     * 
     * @param httpInfo
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public String loginByQCode(HttpInfo httpInfo, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        try {
            String uid = httpInfo.getString("uid");
            String checkToken = loginService.byToken(httpInfo, request);
            if (uid == null || checkToken == null) {
                return null;
            }
            System.out.println(checkToken + "  " + uid);

            // 校验token是否有效
            String userInfoStr = loginService.loginByToken(checkToken);
            if (userInfoStr == null) {
                return null;
            }
            JSONObject userObj = new JSONObject(userInfoStr);
            long userId = userObj.getLong(SlLoginConfig.REDIES_USERID);
            String userName = userObj.getString(SlLoginConfig.REDIES_USERNAME);
            String pwd = userObj.getString(SlLoginConfig.REDIES_UPASSWORD);

            Map<String, Object> claims = new HashMap<>();
            claims.put("iss", SlLoginConfig.JWT_ISS);// 发行人
            String token = TokenUtil.createJWT("1", "userName", claims,
                SlLoginConfig.JWT_OUERDUE);

            userObj = new JSONObject();
            userObj.put(SlLoginConfig.REDIES_USERID, userId);
            userObj.put(SlLoginConfig.REDIES_USERNAME, userName);
            userObj.put(SlLoginConfig.REDIES_UPASSWORD, pwd);
            RedisServer.getSingleInstance().setValue(token, userObj.toString(),
                SlLoginConfig.REDIS_TOKEN_OUERDUE);

            JSONObject tokenObj = new JSONObject();
            tokenObj.put("msg", "login_token");
            tokenObj.put("token", token);
            WebSocketManager.getSingleInstance().sendMessage(uid, tokenObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 退出登录
     * 
     * @param httpInfo
     * @param request
     * @param response
     * @return
     */
    public String exitLogin(HttpInfo httpInfo, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        JSONObject result = new JSONObject();
        try {
            // 1:取cookie，或则url中的token
            String checkToken = loginService.byToken(httpInfo, request);
            loginService.exitLogin(checkToken);
            result.put("result", SlLoginConfig.SUCCESSFUL);
            result.put("cause", "退出成功");
            this.writeJson(httpInfo, request, response, result);
        } catch (Exception e) {
            result.put("result", SlLoginConfig.FAILURE);
            result.put("cause", "服务器连接超时，稍后再试");
            this.writeJson(httpInfo, request, response, result);
        }
        return null;
    }
}

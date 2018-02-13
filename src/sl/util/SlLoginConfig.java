package sl.util;

public class SlLoginConfig {
    public static final String  USERNAME            = "userName";                // 前台传入的用户名
    public static final String  PASSWORD            = "password";                // 前台传入的密码
    public static final String  CODE                = "code";                    // 前台传入的验证码
    public static final String  TT_TOKEN            = "TT_TOKEN";                // 前台拿到token的变量名

    public static final int     SUCCESSFUL          = 1;                         // 返回成功结果的标致
    public static final int     FAILURE             = -1;                        // 返回结果为失败的标致

    public static final int     REDIS_TOKEN_OUERDUE = 8 * 24 * 60 * 60;          // redis存放token过期时间,秒（8天）
    public static final int     REDIS_CODE_OUERDUE  = 120;                       // redis中验证码过期时长,秒
    public static final String  CAPTCHA             = "captcha";                 // 验证码存入redis中作为key的固定字符
    public static final String  REDIES_USERID       = "userId";                  // 用户id存入redis中的key
    public static final String  REDIES_USERNAME     = "userName";                // 用户名存入redis中的key
    public static final String  REDIES_UPASSWORD    = "password";                // 用户名存入redis中的key

    public static final String  JWT_ISS             = "slmoyer";                 // JWT发行人定义
    public static final long    JWT_OUERDUE         = 365 * 24 * 60 * 60 * 1000; // token的过期时间，毫秒

    public static final Boolean LOGNI_ONLINE        = false;                     // 是不是只允许一个用户登录？true=是
}

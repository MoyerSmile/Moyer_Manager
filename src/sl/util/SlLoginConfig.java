package sl.util;

public class SlLoginConfig {
    public static final String  USERNAME            = "userName";                // ǰ̨������û���
    public static final String  PASSWORD            = "password";                // ǰ̨���������
    public static final String  CODE                = "code";                    // ǰ̨�������֤��
    public static final String  TT_TOKEN            = "TT_TOKEN";                // ǰ̨�õ�token�ı�����

    public static final int     SUCCESSFUL          = 1;                         // ���سɹ�����ı���
    public static final int     FAILURE             = -1;                        // ���ؽ��Ϊʧ�ܵı���

    public static final int     REDIS_TOKEN_OUERDUE = 8 * 24 * 60 * 60;          // redis���token����ʱ��,�루8�죩
    public static final int     REDIS_CODE_OUERDUE  = 120;                       // redis����֤�����ʱ��,��
    public static final String  CAPTCHA             = "captcha";                 // ��֤�����redis����Ϊkey�Ĺ̶��ַ�
    public static final String  REDIES_USERID       = "userId";                  // �û�id����redis�е�key
    public static final String  REDIES_USERNAME     = "userName";                // �û�������redis�е�key
    public static final String  REDIES_UPASSWORD    = "password";                // �û�������redis�е�key

    public static final String  JWT_ISS             = "slmoyer";                 // JWT�����˶���
    public static final long    JWT_OUERDUE         = 365 * 24 * 60 * 60 * 1000; // token�Ĺ���ʱ�䣬����

    public static final Boolean LOGNI_ONLINE        = false;                     // �ǲ���ֻ����һ���û���¼��true=��
}
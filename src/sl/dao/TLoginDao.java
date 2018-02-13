package sl.dao;

import sl.domain.User;

/**
 * 
 * @author moyer
 * @date 2018年1月18日
 */
public interface TLoginDao {

    /**
     * 验证用户输入的用户名是否错误
     * 
     * @param name
     * @return
     */
    public User validationName(String name);

    /**
     * 用户名密码登录
     * 
     * @param name
     * @param password
     * @return
     * @throws Exception
     */
    public User loginByAccount(String name, String password) throws Exception;

}

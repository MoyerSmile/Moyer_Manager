package sl.service;

import java.util.List;

import sl.domain.User;

/**
 * 用户管理接口
 * 
 * @author moyer
 * @date 2018年1月31日
 */
public interface TUserService {
    /**
     * 修改，新增
     * 
     * @param user
     */
    public void saveUser(User user) throws Exception;

    /**
     * 修改
     * 
     * @param user
     */
    public void updateUser(User user) throws Exception;

    /**
     * 删除用户
     * 
     * @param userId
     */
    public void delectUser(long userId) throws Exception;

    /**
     * 查询用户多个
     * 
     * @param userName
     * @param phone
     * @param address
     * @return
     */
    public List<User> findUsers(String userName, String phone, String address)
            throws Exception;

    /**
     * 查询单个user
     * 
     * @param userId
     * @return
     */
    public User findUser(long userId) throws Exception;
}

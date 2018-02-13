package sl.dao.impl;

import java.util.List;

import org.hibernate.Session;

import sl.dao.TLoginDao;
import sl.domain.User;
import sl.util.HibernateUtil;

/**
 * 
 * @author moyer
 * @date 2018Äê1ÔÂ19ÈÕ
 */
public class LoginDaoImpl extends GenericDAOImpl implements TLoginDao {

    @Override
    public User validationName(String name) {
        Session session = HibernateUtil.getSession();
        List<User> list = session.createQuery("FROM User WHERE account=?")
            .setString(0, name)
            .list();
        HibernateUtil.closeSession();
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public User loginByAccount(String name, String password) throws Exception {
        Session session = HibernateUtil.getSession();
        List<User> list = session
            .createQuery("FROM User WHERE account=? AND password=?")
            .setString(0, name)
            .setString(1, password)
            .list();
        HibernateUtil.closeSession();
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

}

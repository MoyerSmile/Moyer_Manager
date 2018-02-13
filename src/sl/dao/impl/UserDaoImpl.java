package sl.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

import com.xzy.base.server.db.RedisServer;

import sl.dao.TUserDao;
import sl.domain.User;
import sl.util.HibernateUtil;
import sl.util.SlLoginConfig;

public class UserDaoImpl extends GenericDAOImpl implements TUserDao {

    @Override
    public void saveUser(User user) throws Exception {
        Session session = HibernateUtil.getSession();
        session.save(user);
        HibernateUtil.closeSession();
        // 更新redis中的数据
        RedisServer.getSingleInstance().setValue(String.valueOf(user.getId()),
            user.getPassword());
    }

    @Override
    public void updateUser(User user) throws Exception {
        Session session = HibernateUtil.getSession();
        Transaction tx = session.beginTransaction();
        // UPDATE User SET name= :name,password= :password WHERE id= :id
        StringBuffer hql = new StringBuffer();
        hql.append("UPDATE User SET name= :name");
        if (!StringUtils.isAllBlank(user.getAccount())) {
            hql.append(",account= :account");
        }
        if (!StringUtils.isAllBlank(user.getPassword())) {
            hql.append(",password= :password");
        }
        if (!StringUtils.isAllBlank(user.getTelephone())) {
            hql.append(",telephone= :telephone");
        }
        if (!StringUtils.isAllBlank(user.getAddress())) {
            hql.append(",address= :address");
        }

        hql.append(" WHERE id= :id");
        Query query = session.createQuery(hql.toString());
        query.setString("name", user.getName());
        if (!StringUtils.isAllBlank(user.getAccount())) {
            query.setString("account", user.getAccount());
        }
        if (!StringUtils.isAllBlank(user.getPassword())) {
            query.setString("password", user.getPassword());

            // 更新redis中的数据
            RedisServer.getSingleInstance()
                .setValue(String.valueOf(user.getId()), user.getPassword());
        }
        if (!StringUtils.isAllBlank(user.getTelephone())) {
            query.setString("telephone", user.getTelephone());
        }
        if (!StringUtils.isAllBlank(user.getAddress())) {
            query.setString("address", user.getAddress());
        }
        query.setLong("id", user.getId());
        query.executeUpdate();
        tx.commit();
        HibernateUtil.closeSession();
    }

    @Override
    public void delectUser(long userId) throws Exception {
        Session session = HibernateUtil.getSession();
        User u = session.load(User.class, userId);
        Transaction tx = session.beginTransaction();
        session.delete(u);
        tx.commit();
        HibernateUtil.closeSession();
        // 更新redis中的数据
        RedisServer.getSingleInstance().setValue(String.valueOf(u.getId()),
            "delete", SlLoginConfig.REDIS_TOKEN_OUERDUE);
    }

    @Override
    public List<User> findUsers(String userName, String phone, String address)
            throws Exception {
        Session session = HibernateUtil.getSession();
        String hql = "FROM User WHERE 1=1";
        List<Object> params = new ArrayList<>();
        if (!StringUtils.isAllBlank(userName)) {
            hql += " AND name LIKE ?";
            params.add("%" + userName + "%");
        }
        if (!StringUtils.isAllBlank(phone)) {
            hql += " AND telephone LIKE ?";
            params.add("%" + phone + "%");
        }
        if (!StringUtils.isAllBlank(address)) {
            hql += " AND address LIKE ?";
            params.add("%" + address + "%");
        }
        Type[] types = this.getTypes(params.toArray());
        Query query = session.createQuery(hql);
        query.setParameters(params.toArray(), types);
        List<User> list = query.list();
        HibernateUtil.closeSession();
        return list;
    }

    @Override
    public User findUser(long userId) throws Exception {
        Session session = HibernateUtil.getSession();
        String hql = "FROM User WHERE id=?";
        List<User> list = session.createQuery(hql).setLong(0, userId).list();
        HibernateUtil.closeSession();
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    private Type[] getTypes(Object[] params) {
        Type[] types = new Type[params.length];
        for (int i = 0; i < params.length; i++) {
            Object param = params[i];
            Type type = null;
            if (param instanceof String)
                type = StandardBasicTypes.STRING;
            else if (param instanceof Integer)
                type = StandardBasicTypes.INTEGER;
            else if (param instanceof Float)
                type = StandardBasicTypes.FLOAT;
            else if (param instanceof Double)
                type = StandardBasicTypes.DOUBLE;
            else if (param instanceof Boolean)
                type = StandardBasicTypes.BOOLEAN;
            else if (param instanceof Date)
                type = StandardBasicTypes.TIMESTAMP;
            else if (param instanceof Long)
                type = StandardBasicTypes.LONG;
            types[i] = type;
        }
        return types;
    }
}

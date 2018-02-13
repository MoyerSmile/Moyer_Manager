package sl.dao;

import java.io.Serializable;
import java.util.List;

/**
 * 底层使用封装类
 * 
 * @author moyer
 * @date 2018年1月21日
 */
public interface GenericDAO {
    public void save(Object obj);

    public void update(Object obj);

    public void delete(Object obj);

    public Object load(Class<?> cls, Serializable id);

    public <E> E querySingleResult(String hql);

    public <E> E querySingleResult(String hql, Object[] params);

    public int excute(String hql);

    public int excute(String hql, Object[] params);

    public <E> void batchSave(List<E> list);

    public <E> List<E> query(String hql);

    public <E> List<E> query(String hql, int start, int limit);

    public <E> List<E> query(String hql, Object[] params);

    public <E> List<E> query(String hql, Object[] params, int start, int limit);

    public List<?> getAll(Class<?> dataName);

    public List<?> getAll(Class<?> dataName, String orderBy);

    public List<?> getAll(Class<?> dataName, String condition, String orderBy);

    public <E> List<E> sqlQuery(String sql, List<Object> param, int start,
            int limit);

    public <E> E querySqlSingleResult(String sql, Object[] param);

    public List<?> getAllbyId(Class<?> dataName, long id);

}

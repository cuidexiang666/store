package cn.itcast.store.user.dao.impl;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import cn.itcast.store.user.dao.IUserDao;
import cn.itcast.store.user.domain.User;
import cn.itcast.store.utils.C3P0Utils;

public class UserDaoImpl implements IUserDao {

    /**
     * 用户登录(用户名、密码、激活状态)
     */
    @Override
    public User login(User user) {
        User existUser = null;
        // 1.获得QueryRunner核心对象
        QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
        // 2.编写SQL语句
        String sql = "select * from user where username=? and password=? and state=?";
        // 3.设置实际参数
        Object[] params = {user.getUsername(),user.getPassword(),1};
        // 4.执行查询操作
        try {
            existUser = qr.query(sql, new BeanHandler<>(User.class), params);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return existUser;
    }

    /**
     * 根据激活码查询用户信息
     */
    @Override
    public User findUserByCode(String code) {
        User existUser = null;
        // 1.获得QueryRunner核心对象
        QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
        // 2.编写SQL语句
        String sql = "select * from user where code=?";
        // 3.设置实际参数
        Object[] params = { code };
        // 4.执行查询操作
        try {
            existUser = qr.query(sql, new BeanHandler<>(User.class), params);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return existUser;
    }

    /**
     * 根据uid修改用户信息的通用方法
     */
    @Override
    public int modifyUserByUid(User exsitUser) {
        int rows = 0;
        // 1.获得QueryRunner核心对象
        QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
        // 2.编写SQL语句
        String sql = "update user set password=?,name=?,email=?,telephone=?,birthday=?,sex=?,state=?,code=? where uid=?";
        // 3.设置实际参数
        Object[] params = { exsitUser.getPassword(), exsitUser.getName(), exsitUser.getEmail(),
                exsitUser.getTelephone(), exsitUser.getBirthday(), exsitUser.getSex(), exsitUser.getState(),
                exsitUser.getCode(), exsitUser.getUid() };
        // 4.执行插入操作
        try {
            rows = qr.update(sql, params);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return rows;
    }

    /**
     * 用户注册方法
     */
    @Override
    public int register(User user) {
        int rows = 0;
        // 1.获得QueryRunner核心对象
        QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
        // 2.编写SQL语句
        String sql = "insert into user values(?,?,?,?,?,?,?,?,?,?)";
        // 3.设置实际参数
        Object[] params = { user.getUid(), user.getUsername(), user.getPassword(), user.getName(),
                user.getEmail(), user.getTelephone(), user.getBirthday(), user.getSex(), user.getState(),
                user.getCode() };
        // 4.执行插入操作
        try {
            rows = qr.update(sql, params);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return rows;
    }

}

package cn.itcast.store.user.dao;

import cn.itcast.store.user.domain.User;

public interface IUserDao {

    int register(User user);

    User findUserByCode(String code);

    int modifyUserByUid(User exsitUser);

    User login(User user);

}

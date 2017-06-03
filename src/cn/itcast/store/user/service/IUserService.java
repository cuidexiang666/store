package cn.itcast.store.user.service;

import cn.itcast.store.user.domain.User;

public interface IUserService {

    int register(User user);

    User findUserByCode(String code);

    int modifyUserByUid(User exsitUser);

    User login(User user);

}

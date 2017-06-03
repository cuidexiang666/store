package cn.itcast.store.user.service.impl;

import cn.itcast.store.user.dao.IUserDao;
import cn.itcast.store.user.dao.impl.UserDaoImpl;
import cn.itcast.store.user.domain.User;
import cn.itcast.store.user.service.IUserService;

public class UserServiceImpl implements IUserService {

    private IUserDao dao = new UserDaoImpl();
    
    @Override
    public int register(User user) {
        return dao.register(user);
    }

    @Override
    public User findUserByCode(String code) {
        return dao.findUserByCode(code);
    }

    @Override
    public int modifyUserByUid(User exsitUser) {
        return dao.modifyUserByUid(exsitUser);
    }

    @Override
    public User login(User user) {
        return dao.login(user);
    }

}

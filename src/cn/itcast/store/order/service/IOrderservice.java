package cn.itcast.store.order.service;

import cn.itcast.store.order.domain.Order;
import cn.itcast.store.user.domain.User;
import cn.itcast.store.utils.PageBean;

public interface IOrderservice {

	void save(Order order);

	PageBean<Order> searchOrdersByUidForPage(User exsitUser, PageBean<Order> pageBean);

	Order searchOrderInfoByOid(String oid);

	int modifyOrderByOid(Order order);

}

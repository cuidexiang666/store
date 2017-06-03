package cn.itcast.store.order.dao;

import java.util.ArrayList;
import java.util.List;

import cn.itcast.store.order.domain.Order;
import cn.itcast.store.order.domain.OrderItem;
import cn.itcast.store.user.domain.User;
import cn.itcast.store.utils.PageBean;

public interface IOrderDao {

	void save(Order order);

	void save(OrderItem orderItem);

	int getTotalRecord(User exsitUser);

	List<Order> searchOrdersByUidForPage(User exsitUser, PageBean<Order> pageBean);

	Order searchOrderInfoByOid(String oid);

	int modifyOrderByOid(Order order);


}

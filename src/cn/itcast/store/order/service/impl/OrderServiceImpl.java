package cn.itcast.store.order.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cn.itcast.store.order.dao.IOrderDao;
import cn.itcast.store.order.dao.impl.OrderDaoImpl;
import cn.itcast.store.order.domain.Order;
import cn.itcast.store.order.domain.OrderItem;
import cn.itcast.store.order.service.IOrderservice;
import cn.itcast.store.user.domain.User;
import cn.itcast.store.utils.C3P0Utils;
import cn.itcast.store.utils.PageBean;

public class OrderServiceImpl implements IOrderservice {

	private IOrderDao dao = new OrderDaoImpl();
	

	/**
	 * 修改订单
	 */
	@Override
	public int modifyOrderByOid(Order order) {
		
		return dao.modifyOrderByOid(order);
	}

	/**
	 * 查询单个订单信息
	 */
	@Override
	public Order searchOrderInfoByOid(String oid) {
		return dao.searchOrderInfoByOid(oid);
	}
	
	/**
	 * 分页查询所有订单信息的方法
	 */
	@Override
	public PageBean<Order> searchOrdersByUidForPage(User exsitUser, PageBean<Order> pageBean) {
		//获得每页显示的个数
		int pageSize = 3;
		pageBean.setPageSize(pageSize);
		//获得总记录数
		int totalRecord = dao.getTotalRecord(exsitUser);
		pageBean.setTotalRecord(totalRecord);
		//分页查询某个用户下面所有订单信息
		List<Order> orders = new ArrayList<>();
		orders = dao.searchOrdersByUidForPage(exsitUser,pageBean);
		pageBean.setResult(orders);
		
		return pageBean;
	}
	
	
	@Override
	public void save(Order order) {
		
		Connection conn = C3P0Utils.getConnnection();
		//手动开启事务
		try {
			conn.setAutoCommit(false);
			//调用dao层方法
			dao.save(order);
			//获得所有订单项
			ArrayList<OrderItem> list = order.getList();
			for (OrderItem orderItem : list) {
				dao.save(orderItem);
			}
			//提交事务
			conn.commit();
		} catch (Exception e) {
			//回滚事务
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}

}

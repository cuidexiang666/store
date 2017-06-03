package cn.itcast.store.order.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.store.order.dao.IOrderDao;
import cn.itcast.store.order.domain.Order;
import cn.itcast.store.order.domain.OrderItem;
import cn.itcast.store.product.domain.Product;
import cn.itcast.store.user.domain.User;
import cn.itcast.store.utils.C3P0Utils;
import cn.itcast.store.utils.PageBean;

public class OrderDaoImpl implements IOrderDao {


	/**
	 * 修改订单信息(通用的修改方法)
	 */
	@Override
	public int modifyOrderByOid(Order order) {
		int rows = 0;
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "update orders set state=?, address=?, name=?, telephone=? where oid=?";
		Object[] params = {order.getState(),order.getAddress(),order.getName(),order.getTelephone(),order.getOid()};
		try {
			rows = qr.update(sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rows;
	}
	/**
	 * 查询单个订单详情
	 */
	@Override
	public Order searchOrderInfoByOid(String oid) {
		Order order = null;
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select * from orders where oid = ?";
		try {
			order = qr.query(sql, new BeanHandler<>(Order.class), oid);
			//编写SQL语句
			String sql1 = "select * from orderitem o,product p where o.pid=p.pid and o.oid=?";
			//创建订单项集合
			ArrayList<OrderItem> orderItems = new ArrayList<>();
			List<Map<String,Object>> list = qr.query(sql1, new MapListHandler(), oid);
			//遍历
			for (Map<String, Object> map : list) {
				OrderItem orderItem = new OrderItem();
				BeanUtils.populate(orderItem, map);
				Product product = new Product();
				BeanUtils.populate(product, map);
				orderItem.setProduct(product);
				orderItems.add(orderItem);
			}
			order.setList(orderItems);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return order;
	}
	/**
	 * 查询总记录数
	 */
	@Override
	public int getTotalRecord(User exsitUser) {
		Long totalRecord = 0l;
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select count(*) from orders where uid=?";
		try {
			totalRecord = (Long) qr.query(sql, new ScalarHandler(), exsitUser.getUid());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return totalRecord.intValue();
	}

	/**
	 * 分页查询某个用户下面的订单
	 */
	@Override
	public List<Order> searchOrdersByUidForPage(User exsitUser, PageBean<Order> pageBean) {
		List<Order> orders = null;
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select * from orders where uid = ? limit ?,?";
		Object[] params = {exsitUser.getUid(),pageBean.getStartIndex(),pageBean.getPageSize()};
		try {
			//执行查询操作
			orders = qr.query(sql, new BeanListHandler<>(Order.class), params);
			//遍历得到单个订单
			for (Order order : orders) {
				String sql1 = "select * from orderitem o,product p where o.pid=p.pid and o.oid = ?";
				//查询所有订单项和商品信息
				List<Map<String,Object>> list = qr.query(sql1, new MapListHandler(), order.getOid());
				//创建订单项集合对象
				ArrayList<OrderItem> orderItems = new ArrayList<>();
				for (Map<String,Object> map : list) {
					//使用beanUtils封装product对象
					Product product = new Product();
					BeanUtils.populate(product, map);
					//封装orderitem对象
					OrderItem orderItem = new OrderItem();
					BeanUtils.populate(orderItem, map);
					//将product封装到orderitem中
					orderItem.setProduct(product);
					//将orderitem封装集合中
					orderItems.add(orderItem);
				}
				//将orderitems封装到order中
				order.setList(orderItems);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return orders;
	}

	/**
	 * 保存订单的方法
	 */
	@Override
	public void save(Order order) {

		QueryRunner qr = new QueryRunner();
		String sql = "insert into orders values(?,?,?,?,?,?,?,?)";
		Object[] params = {order.getOid(),order.getOrdertime(),order.getTotal(),order.getState(),order.getAddress(),order.getName(),order.getTelephone(),order.getUser().getUid()};
		try {
			qr.update(C3P0Utils.getConnnection(), sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 保存订单项的方法
	 */
	@Override
	public void save(OrderItem orderItem) {

		QueryRunner qr = new QueryRunner();
		String sql = "insert into orderitem values(?,?,?,?,?)";
		Object[] param = {orderItem.getItemid(),orderItem.getCount(),orderItem.getSubtotal(),orderItem.getProduct().getPid(),orderItem.getOrder().getOid()};
		try {
			qr.update(C3P0Utils.getConnnection(), sql, param);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

package cn.itcast.store.product.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.store.product.dao.IProductDao;
import cn.itcast.store.product.domain.Category;
import cn.itcast.store.product.domain.Product;
import cn.itcast.store.utils.C3P0Utils;
import cn.itcast.store.utils.PageBean;

public class ProductDaoImpl implements IProductDao {

	/**
	 * 添加分类信息
	 */
	@Override
	public int addCategory(Category category) {
		int rows = 0;
		//获取QueryRunner核心对象
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		//编写SQL语句
		String sql = "insert into category values(?,?)";
		Object[] params = {category.getCid(),category.getCname()};
		try {
			rows = qr.update(sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rows;
	}
	/**
	 * 查询商品分类
	 */
	@Override
	public List<Category> findAllCategory() {
		List<Category> categorys = null;
		//获取QueryRunner核心对象
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		//编写SQL语句
		String sql = "select * from category";
		try {
			categorys = qr.query(sql, new BeanListHandler<>(Category.class));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return categorys;
	}

	/**
	 * 获得总记录数
	 */
	@Override
	public int getTotalRecord(String cid) {
		Long totalRecord = 0L;
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select count(*) from product where cid = ? and pflag = ?";
		try {
			totalRecord = (Long) qr.query(sql, new ScalarHandler(), cid,0);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return totalRecord.intValue();
	}

	/**
	 * 根据商品分类查询商品
	 */
	@Override
	public List<Product> findAllProductByCid(String cid, PageBean<Product> pageBean) {
		List<Product> products = null;
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select * from product where cid = ? and pflag = ? limit ?,?";
		Object[] params = {cid,0,pageBean.getStartIndex(),pageBean.getPageSize()};
		try {
			products = qr.query(sql, new BeanListHandler<>(Product.class), params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return products;
	}

	/**
	 * 根据pid查询所有商品
	 */
	@Override
	public Product findAllProductByPid(String pid) {
		Product product = null;
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select * from product where pid = ?";
		try {
			product = qr.query(sql, new BeanHandler<>(Product.class), pid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return product;
	}

	/**
	 * 查询热门商品
	 */
	@Override
	public List<Product> findHotProduct(String is_hot) {
		List<Product> products = null;
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select * from product where is_hot=? order by pdate desc limit 9";
		try {
			products = qr.query(sql, new BeanListHandler<>(Product.class), is_hot);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return products;
	}
	/**
	 * 查询最新商品
	 */
	@Override
	public List<Product> findNewProduct() {
		List<Product> products = null;
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select * from product order by pdate desc limit 9";
		try {
			products = qr.query(sql, new BeanListHandler<>(Product.class));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return products;
	}
	@Override
	public int delCategory(String cid) {
		int rows = 0;
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "delete from category where cid = ?";
		try {
			rows = qr.update(sql, cid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rows;
	}
	@Override
	public Category findCategoryBycid(String cid) {
		Category category = null;
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select * from category where cid = ?";
		try {
			category = qr.query(sql, new BeanHandler<>(Category.class), cid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return category;
	}
	/**
	 * 修改分类信息
	 */
	@Override
	public int modifyCategory(Category category) {
		int rows = 0;
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "update category set cname=? where cid=? ";
		Object[] params = {category.getCname(),category.getCid()};
		try {
			rows = qr.update(sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rows;
	}

}

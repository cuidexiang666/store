package cn.itcast.store.product.service.impl;

import java.util.List;

import cn.itcast.store.product.dao.IProductDao;
import cn.itcast.store.product.dao.impl.ProductDaoImpl;
import cn.itcast.store.product.domain.Category;
import cn.itcast.store.product.domain.Product;
import cn.itcast.store.product.service.IProductService;
import cn.itcast.store.utils.PageBean;

public class ProductServiceImpl implements IProductService {
	private IProductDao dao = new ProductDaoImpl();

	/**
	 * 添加分类信息
	 */
	@Override
	public int addCategory(Category category) {
		return dao.addCategory(category);
	}
	/**
	 * 查询所有分类信息
	 */
	@Override
	public List<Category> findAllCategory() {
		return dao.findAllCategory();
	}

	/**
	 * 根据商品分类查询
	 * @return 
	 */
	@Override
	public PageBean<Product> findAllProductByCid(String cid, PageBean<Product> pageBean) {
		//每页显示的个数
		int pageSize = 12;
		pageBean.setPageSize(pageSize);
		//总记录数
		int totalRecord = dao.getTotalRecord(cid);
		pageBean.setTotalRecord(totalRecord);
		
		List<Product> products = dao.findAllProductByCid(cid,pageBean);
		pageBean.setResult(products);
		return pageBean;
	}

	@Override
	public Product findAllProductByPid(String pid) {
		
		return dao.findAllProductByPid(pid);
	}

	/**
	 * 查热门商品
	 */
	@Override
	public List<Product> findHotProduct(String is_hot) {
		
		return dao.findHotProduct(is_hot);
	}
	/**
	 * 查最新商品
	 */
	@Override
	public List<Product> findNewProduct() {
		
		return dao.findNewProduct();
	}
	@Override
	public int delCategory(String cid) {
		return dao.delCategory(cid);
	}
	@Override
	public Category findCategoryBycid(String cid) {
		return dao.findCategoryBycid(cid);
	}
	@Override
	public int modifyCategory(Category category) {
		return dao.modifyCategory(category);
	}

}

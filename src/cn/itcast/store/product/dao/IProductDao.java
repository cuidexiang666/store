package cn.itcast.store.product.dao;

import java.util.List;

import cn.itcast.store.product.domain.Category;
import cn.itcast.store.product.domain.Product;
import cn.itcast.store.utils.PageBean;

public interface IProductDao {

	List<Category> findAllCategory();

	int getTotalRecord(String cid);

	List<Product> findAllProductByCid(String cid, PageBean<Product> pageBean);

	Product findAllProductByPid(String pid);

	List<Product> findHotProduct(String is_hot);

	List<Product> findNewProduct();

	int addCategory(Category category);

	int delCategory(String cid);

	Category findCategoryBycid(String cid);

	int modifyCategory(Category category);


}

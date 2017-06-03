package cn.itcast.store.product.service;

import java.util.List;

import cn.itcast.store.product.domain.Category;
import cn.itcast.store.product.domain.Product;
import cn.itcast.store.utils.PageBean;

public interface IProductService {

	List<Category> findAllCategory();

	PageBean<Product> findAllProductByCid(String cid, PageBean<Product> pageBean);

	Product findAllProductByPid(String pid);

	List<Product> findHotProduct(String is_hot);
	List<Product> findNewProduct();

	int addCategory(Category category);

	int delCategory(String cid);

	Category findCategoryBycid(String cid);

	int modifyCategory(Category category);

}

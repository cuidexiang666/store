package cn.itcast.store.product.domain;

import java.io.Serializable;

public class CartItem implements Serializable{

	//商品对象（从商品详情页面有用户传递过来）
	private Product product;
	
	//商品数量
	private int count;
	
	//小计
	private double subtotal;

	public double getSubtotal() {
		return subtotal = this.count*this.product.getShop_price();
	}

	/*public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}*/
	
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public CartItem() {
		super();
	}
	
	
}

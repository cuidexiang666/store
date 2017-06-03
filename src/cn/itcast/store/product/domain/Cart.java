package cn.itcast.store.product.domain;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class Cart implements Serializable{

	//购物项
	private LinkedHashMap<String, CartItem> map = new LinkedHashMap<>();
	
	//总金额【影响总金额 ：添加一个商品，删除一个商品，清空购物车】
	private double total = 0.0;
	
	//添加一个商品【一种是添加，一种是追加】
	public void addCartItem(CartItem cartItem) {
		//获得pid
		String pid = cartItem.getProduct().getPid();
		//获得旧cartItem
		CartItem oldCartItem = map.get(pid);
		if (oldCartItem==null) {
			//说明没有购物项，是添加
			map.put(pid, cartItem);
		}else {
			//说明有购物项，是追加
			oldCartItem.setCount(oldCartItem.getCount()+cartItem.getCount());
		}
		//无论是添加还是追加，总金额都是小计相加
		total+=cartItem.getSubtotal();
	}
	
	
	//删除一个商品

	public void removeCartItemByPid(String pid) {
		//根据pid删除购物项
		CartItem cartItem = map.remove(pid);
		//重新计算总金额
		total-=cartItem.getSubtotal();
	}
	
	//清空购物车
	public void clearItem() {
		map.clear();
		total=0.0;
	}
	
	
	

	public LinkedHashMap<String, CartItem> getMap() {
		return map;
	}

	public void setMap(LinkedHashMap<String, CartItem> map) {
		this.map = map;
	}

	public double getTotal() {
		return total;
	}
	
	public void setTotal(double total) {
		this.total = total;
	}
	
	public Cart() {
		super();
	}
}

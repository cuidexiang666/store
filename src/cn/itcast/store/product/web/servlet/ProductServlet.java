package cn.itcast.store.product.web.servlet;

import cn.itcast.store.product.domain.Cart;
import cn.itcast.store.product.domain.CartItem;
import cn.itcast.store.product.domain.Category;
import cn.itcast.store.product.domain.Product;
import cn.itcast.store.product.service.IProductService;
import cn.itcast.store.product.service.impl.ProductServiceImpl;
import cn.itcast.store.utils.BaseServlet;
import cn.itcast.store.utils.JedisUtils;
import cn.itcast.store.utils.PageBean;
import cn.itcast.store.utils.UUIDUtils;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.catalina.connector.Request;
import org.apache.commons.beanutils.BeanUtils;

import com.google.gson.Gson;

public class ProductServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
	
	private IProductService service = new ProductServiceImpl();
	
	public String modifyCategory(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Category category = new Category();
		try {
			BeanUtils.populate(category, request.getParameterMap());
			int rows = service.modifyCategory(category);
			if (rows>0) {
				response.getWriter().write("true");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
		
	}
	/**
	 * 根据cid查询分类信息
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findCategoryBycid(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String cid = request.getParameter("cid");
		Category category = service.findCategoryBycid(cid);
		request.setAttribute("category", category);
		Gson gson = new Gson();
		String json = gson.toJson(category);
		response.getWriter().write(json);
		return null;
	}
	/**
	 * 删除分类信息
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String delCategory(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String cid = request.getParameter("cid");
		int rows = service.delCategory(cid);
		if (rows>0) {
			response.getWriter().write("true");
		}
		
		return null;
	}
	/**
	 * 添加分类信息
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String addCategory(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String cname = request.getParameter("cname");
		String cid = UUIDUtils.getUUID();
		Category category = new Category();
		category.setCid(cid);
		category.setCname(cname);
		int rows = service.addCategory(category);
		if (rows>0) {
			//说明添加成功
			response.getWriter().write("true");
		}
		return null;
	}
	/**
	 * 获得购物车对象
	 * @return
	 */
	private Cart getCart(HttpServletRequest request) {

		HttpSession session = request.getSession();
		Object obj = session.getAttribute("cart");
		if (obj==null) {
			//说明没有购物车对象
			Cart cart = new Cart();
			return cart;
		}else{
			return (Cart) obj;
		}
	}
	/**
	 * 清空购物车
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String clearItem(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//获得购物车对象
		Cart cart = this.getCart(request);
		//清空购物项
		cart.clearItem();
		//将清空后的放回session中
		request.getSession().setAttribute("cart", cart);
		//重定向到cart.jsp页面
		response.sendRedirect(request.getContextPath()+"/cart.jsp");
		
		return null;
	}
	/**
	 * 删除购物项
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String deleteCartItemByPid(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//获取请求参数pid
		String pid = request.getParameter("pid");
		//获得购物车对象
		Cart cart = this.getCart(request);
		//删除购物项
		cart.removeCartItemByPid(pid);
		//将删除后的放回session中
		request.getSession().setAttribute("cart", cart);
		//重定向到cart.jsp页面
		response.sendRedirect(request.getContextPath()+"/cart.jsp");
		return null;
	}
	/**
	 * 添加购物项
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String addCartItem(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//获取请求参数
		String pid = request.getParameter("pid");
		int count = Integer.parseInt(request.getParameter("count"));
		//调用service层方法
		Product product = service.findAllProductByPid(pid);
		//将商品信息保存到购物项
		CartItem cartItem = new CartItem();
		cartItem.setProduct(product);
		cartItem.setCount(count);
		//获得购物车对象
		Cart cart = this.getCart(request);
		//添加商品
		cart.addCartItem(cartItem);
		//将商品信息保存到域对象
		request.getSession().setAttribute("cart", cart);
		//重定向到cart.jsp页面
		response.sendRedirect(request.getContextPath()+"/cart.jsp");
		return null;
	}
	
	/**
	 * 查询热门商品
	 */
	public String findHotProduct(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//获取请求参数
		String is_hot = request.getParameter("is_hot");
		//调用service层方法
		List<Product> products = service.findHotProduct(is_hot);
		Gson gson = new Gson();
		String json = gson.toJson(products);
		//将结果响应到客户端
		response.getWriter().write(json);
		
		return null;
		
	}
	/**
	 * 查询最新商品
	 */
	public String findNewProduct(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//调用service层方法
		List<Product> products = service.findNewProduct();
		Gson gson = new Gson();
		String json = gson.toJson(products);
		//将结果响应到客户端
		response.getWriter().write(json);
		
		return null;
		
	}
	/**
	 * 根据pid查询单个商品信息
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findAllProductByPid(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//获取请求参数
		String pid = request.getParameter("pid");
		//调用service层方法
		IProductService service = new ProductServiceImpl();
		Product product = service.findAllProductByPid(pid);
		//将结果保存到域对象
		request.setAttribute("product", product);
		
		return "product_info.jsp" ;
		
	}
	
	/**
	 * 根据商品分类分页查询商品
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findAllProductByCid(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		int pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
		PageBean<Product> pageBean = new PageBean<>();
		//将请求参数封装到pageBean中
		pageBean.setPageNumber(pageNumber);
		String cid = request.getParameter("cid");
		try {
			IProductService service = new ProductServiceImpl();
			pageBean = service.findAllProductByCid(cid,pageBean);
			request.setAttribute("pageBean", pageBean);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "product_list.jsp";
		
	}
	/**
	 * 异步方式实现（redis缓存）
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findAllCategorys(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//获取jedis核心对象
		Jedis jedis = JedisUtils.getJedis();
		//获得redis数据库中的数据
		String json = jedis.get("category");
		try {
			if (json==null) {
				//说明缓存中没有
				List<Category> categorys = service.findAllCategory();
				Gson gson = new Gson();
				String ct = gson.toJson(categorys);
				//将其保存到jedis数据库中
				jedis.set("categorys", ct);
				response.getWriter().write(ct);
			}else {
				//缓存中有	
				response.getWriter().write(json);
			}
		} finally {
			JedisUtils.close(jedis);
		}
		
		return null;
		
	}
	/**
	 * 异步方式
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findAllCategory(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		//直接调用service层方法
		List<Category> categorys = service.findAllCategory();
		Gson gson = new Gson();
		String json = gson.toJson(categorys);
		response.getWriter().write(json);
		//转发到首页
		return null;

	}
	/**
	 * 同步方式
	 */
/*	public String findAllCategory(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		//直接调用service层方法
		List<Category> categorys = service.findAllCategory();
		//将结果保存到域对象
		request.setAttribute("categorys", categorys);
		//转发到首页
		return "index.jsp";
		
	}
*/}

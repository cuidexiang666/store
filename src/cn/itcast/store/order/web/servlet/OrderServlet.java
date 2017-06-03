package cn.itcast.store.order.web.servlet;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;

import cn.itcast.store.order.domain.Order;
import cn.itcast.store.order.domain.OrderItem;
import cn.itcast.store.order.service.IOrderservice;
import cn.itcast.store.order.service.impl.OrderServiceImpl;
import cn.itcast.store.product.domain.Cart;
import cn.itcast.store.product.domain.CartItem;
import cn.itcast.store.user.domain.User;
import cn.itcast.store.utils.BaseServlet;
import cn.itcast.store.utils.PageBean;
import cn.itcast.store.utils.PaymentUtil;
import cn.itcast.store.utils.UUIDUtils;

public class OrderServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
	private IOrderservice service = new OrderServiceImpl();

	/**
	 * 支付成功之后修改订单状态
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String payOrderSuccess(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//获取返回的所有参数
		String p1_MerId = request.getParameter("p1_MerId");
		String r0_Cmd = request.getParameter("r0_Cmd");
		String r1_Code = request.getParameter("r1_Code");
		String r2_TrxId = request.getParameter("r2_TrxId");
		String r3_Amt = request.getParameter("r3_Amt");
		String r4_Cur = request.getParameter("r4_Cur");
		String r5_Pid = request.getParameter("r5_Pid");
		String r6_Order = request.getParameter("r6_Order");
		String r7_Uid = request.getParameter("r7_Uid");
		String r8_MP = request.getParameter("r8_MP");
		String r9_BType = request.getParameter("r9_BType");
		String rb_BankId = request.getParameter("rb_BankId");
		String ro_BankOrderId = request.getParameter("ro_BankOrderId");
		String rp_PayDate = request.getParameter("rp_PayDate");
		String rq_CardNo = request.getParameter("rq_CardNo");
		String ru_Trxtime = request.getParameter("ru_Trxtime");
		String hmac = request.getParameter("hmac");

		//校验数据是否正确
		boolean flag = PaymentUtil.verifyCallback(hmac, p1_MerId, r0_Cmd, r1_Code, r2_TrxId, r3_Amt, r4_Cur, r5_Pid, r6_Order, r7_Uid, r8_MP, r9_BType, "69cl522AV6q613Ii4W6u8K6XuW8vM1N6bFgyv769220IuYe9u37N4y7rI4Pl");
		if(flag){
			//数据正确,修改订单状态
			try{
				Order order = service.searchOrderInfoByOid(r6_Order);
				order.setState(1);
				service.modifyOrderByOid(order);
				request.setAttribute("msg","订单付款成功,订单号为:"+r6_Order+"///付款金额为:"+r3_Amt);
			}catch(Exception e){
				throw new RuntimeException(e);
			}
			
		}else{
			throw new RuntimeException("数据遭篡改");
		}


		return "/msg.jsp";
	}
	/**
	 * 在线支付
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String payForOrderByOid(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//获得请求参数
		String oid = request.getParameter("oid");
		//根据oid查询订单信息
		Order order = service.searchOrderInfoByOid(oid);
		try {
			BeanUtils.populate(order, request.getParameterMap());
			//调用service层方法
			int rows = service.modifyOrderByOid(order);
			//判断
			if (rows>0) {
				//在线支付
				String p0_Cmd = "Buy";
                String p1_MerId = "10001126856";
                String p2_Order = order.getOid();
                String p3_Amt = "0.01";// 测试用1分钱，真正开发中用order.getTotal();
                String p4_Cur = "CNY";
                String p5_Pid = "";
                String p6_Pcat = "";
                String p7_Pdesc = "";
                // 商户接收支付成功数据的地址
                String p8_Url = "http://localhost:8080" + request.getContextPath()
                        + "/OrderServlet?method=payOrderSuccess";
                String p9_SAF = "0";
                String pa_MP = "";
                String pd_FrpId = request.getParameter("pd_FrpId");
                String pr_NeedResponse = "1";
                String hmac = PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt, p4_Cur, p5_Pid,
                        p6_Pcat, p7_Pdesc, p8_Url, p9_SAF, pa_MP, pd_FrpId, pr_NeedResponse,
                        "69cl522AV6q613Ii4W6u8K6XuW8vM1N6bFgyv769220IuYe9u37N4y7rI4Pl");

                StringBuffer buffer = new StringBuffer("https://www.yeepay.com/app-merchant-proxy/node?");
                buffer.append("p0_Cmd=" + p0_Cmd);
                buffer.append("&p1_MerId=" + p1_MerId);
                buffer.append("&p2_Order=" + p2_Order);
                buffer.append("&p3_Amt=" + p3_Amt);
                buffer.append("&p4_Cur=" + p4_Cur);
                buffer.append("&p5_Pid=" + p5_Pid);
                buffer.append("&p6_Pcat=" + p6_Pcat);
                buffer.append("&p7_Pdesc=" + p7_Pdesc);
                buffer.append("&p8_Url=" + p8_Url);
                buffer.append("&p9_SAF=" + p9_SAF);
                buffer.append("&pa_MP=" + pa_MP);
                buffer.append("&pd_FrpId=" + pd_FrpId);
                buffer.append("&pr_NeedResponse=" + pr_NeedResponse);
                buffer.append("&hmac=" + hmac);

                response.sendRedirect(buffer.toString());
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return null;
		
	}
	/**
	 * 查询单个订单详情
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String searchOrderInfoByOid(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			
		//获得请求参数oid
		String oid = request.getParameter("oid");
		//调用service层方法
		Order order = service.searchOrderInfoByOid(oid);
		//将查询的结果保存到域对象
		request.setAttribute("order", order);
		//转发
		return "order_info.jsp";
		
	}
	/**
	 * 获得购物车对象
	 * @param request
	 * @return
	 */
	private Cart getCart(HttpServletRequest request) {
        //1.获得session对象
        HttpSession session = request.getSession();
        //2.从session获得购物车对象
        Object obj = session.getAttribute("cart");
        //3.判断
        if(obj!=null){
            //说明session中有购物车对象
            return (Cart) obj;
        }else{
            //说明session中没有购物车对象，创建一个
            Cart cart = new Cart();
            return cart;
        }
    }
	
	/**
	 * 根据uid查询所有订单
	 */
	public String searchOrdersByUidForPage(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//获得请求参数pageNumber
		int pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
		//封装到pageBean中
		PageBean<Order> pageBean = new PageBean<>();
		pageBean.setPageNumber(pageNumber);
		//从session中获得用户数据
		User exsitUser = (User) request.getSession().getAttribute("exsitUser");
		//判断
		if (exsitUser==null) {
			//说明用户未登录
			request.setAttribute("msg", "<h3>您还未登录或登录超时，请返回重新登录。。。</h3>");
			return "msg.jsp";
		}
		//说明已经登录
		//调用service层查询订单信息的方法
		pageBean = service.searchOrdersByUidForPage(exsitUser,pageBean);
		//将查询出来的结果保存到域对象
		request.setAttribute("pageBean", pageBean);
		//转发到order_list.jsp
		return "order_list.jsp";
	}
	/**
	 * 生成订单
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String makeOrder(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		//从session中获得用户登录状态
		User exsitUser = (User) request.getSession().getAttribute("exsitUser");
		//判断
		if (exsitUser==null) {
			//说明未登录，不生成订单，给出提示信息
			request.setAttribute("msg", "<h3 style='color:red'>哥们，您还未登录，无法进行生成订单操作，请先登录……</h3>");
			return "msg.jsp";
		}else {
			//说明已经登录
			//从session中获得购物车对象
			Cart cart = this.getCart(request);
			//判断
			if (cart==null&&cart.getMap().size()==0) {
				//说明没有订单信息，不能生成订单
				request.setAttribute("msg", "<h3 style='color:red'>哥们，您还未购物，无法进行生成订单操作，请先去买东西吧……</h3>");
				//转发
				return "msg.jsp";
			}else {
				//说明有购物车信息，可以生成订单
				//创建订单对象
				Order order = new Order();
				//根据购物车生成订单对象
				order.setOid(UUIDUtils.getUUID());
				order.setOrdertime(new Date());
				order.setTotal(cart.getTotal());
				order.setUser(exsitUser);
				//创建一个订单集合对象
				ArrayList<OrderItem> list = new ArrayList<>();
				//将订单项封装进list集合中
				for (String key : cart.getMap().keySet()) {
					//获得购物项
					CartItem cartItem = cart.getMap().get(key);
					//将购物项中的数据封装到订单项中
					OrderItem orderItem = new OrderItem();
					orderItem.setCount(cartItem.getCount());
					orderItem.setItemid(UUIDUtils.getUUID());
					orderItem.setOrder(order);
					orderItem.setSubtotal(cartItem.getSubtotal());
					orderItem.setProduct(cartItem.getProduct());
					list.add(orderItem);
				}
				order.setList(list);
				//调用service层方法，生成订单
				service.save(order);
				//将数据保存到域对象
				request.setAttribute("order", order);
				
			}
		}
		return "order_info.jsp";

	}
}

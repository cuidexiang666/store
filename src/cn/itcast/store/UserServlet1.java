package cn.itcast.store;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.store.utils.BaseServlet;

public class UserServlet1 extends BaseServlet {
    
    /**
     * 一旦用户点击用户注册功能超链接，就会进入到当前的servlet(UserServlet1),会找service(ServletRequest request,ServletResposne response)
     * 但是发现当前类中没有这个方法，去他父类中找！
     * @param request
     * @param response
     */

    
    /**
     * 用户进行某个操作之后需要进行页面的跳转(请求转发和重定向) 
     * 请求转发：服务器内部路径【将服务器内部的路径写成一个字符串作为方法的返回值】
     * 重定向：不太确定
     * 
     * @param request
     * @param response
     */
    
    
    // 定义一些方法完成用户需要进行的操作
    public void register(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("用户注册");
    }

    public void active(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("用户激活");
    }

    /**
     * 如果给了返回值，说明是请求转发，否则是重定向
     * @param request
     * @param response
     * @return
     */
    public String login(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("用户登录");
        return "a.jsp";
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("用户退出");
    }

}
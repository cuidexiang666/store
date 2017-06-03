package cn.itcast.store.user.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

import cn.itcast.store.user.domain.User;
import cn.itcast.store.user.service.IUserService;
import cn.itcast.store.user.service.impl.UserServiceImpl;
import cn.itcast.store.user.utils.MailUtils;
import cn.itcast.store.utils.BaseServlet;
import cn.itcast.store.utils.UUIDUtils;

public class UserServlet extends BaseServlet {

    private static final long serialVersionUID = 1L;

    private IUserService service = new UserServiceImpl();

    /**
     * 用户注销
     * 
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String logout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //从session中移除当前的用户信息
        request.getSession().removeAttribute("exsitUser");
        return "index.jsp";
    }

    /**
     * 用户登录
     * 
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String login(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // 1.创建javabean
            User user = new User();
            // 2.封装数据到javabean
            BeanUtils.populate(user, request.getParameterMap());
            // 3.调用service层登录方法(查询)
            User exsitUser = service.login(user);
            // 4.判断
            if (exsitUser != null) {
                // 说明登录成功，跳转到首页
                request.getSession().setAttribute("exsitUser", exsitUser);
                // 重定向到首页
                response.sendRedirect(request.getContextPath() + "/index.jsp");
            } else {
                // 登录失败，在登录页面给出提示信息
                request.setAttribute("msg", "用户名或密码错误或还未激活");
                return "login.jsp";
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * 用户激活
     * 
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String active(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 1.获得激活码
        String code = request.getParameter("code");
        // 2.非空判断
        if (code != null) {
            // 3.根据激活码查询用户信息
            User exsitUser = service.findUserByCode(code);
            // 4.非空判断
            if (exsitUser != null) {
                // 5.说明未激活，修改激活状态、删除激活码
                exsitUser.setCode("");
                exsitUser.setState(1);
                // 6.修改数据中的数据
                int rows = service.modifyUserByUid(exsitUser);
                // 7.判断
                if (rows > 0) {
                    // 8.页面跳转(重定向到登录页面)
                    response.sendRedirect(request.getContextPath() + "/login.jsp");
                }
            } else {
                // 说明已经激活,给出提示信息
                request.setAttribute("msg", "<h3 style='color:red'>哥们，您已经激活过了，不要重复激活……</h3>");
                // 转发到提示信息页面
                return "msg.jsp";
            }
        }
        return null;
    }

    /**
     * 用户注册功能 register：由页面method参数的值而来
     * 
     * @param request
     * @param response
     * @return 进行页面跳转，如果不为空是请求转发；否则是重定向
     * @throws ServletException
     * @throws IOException
     */
    public String register(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // 1.创建一个javabean
            User user = new User();
            // 2.将获得到请求参数封装到javabean
            BeanUtils.populate(user, request.getParameterMap());
            // 3.单独封装uid 、code、state
            user.setUid(UUIDUtils.getUUID());
            user.setCode(UUIDUtils.getUUID64());
            user.setState(0);// 刚开始注册，肯定是未激活状态
            // 4.调用service层注册方法(插入操作)
            int rows = service.register(user);
            if (rows > 0) {
                // 5.说明注册成功，发送一个封激活邮件
                MailUtils.sendMail(user.getEmail(), user.getCode());
                // 6.设置提示信息
                request.setAttribute("msg", "<h3 style='color:blue'>恭喜您，注册成功，请前往邮箱激活……</h3>");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 6.给出提示信息页面
        return "msg.jsp";
    }

}
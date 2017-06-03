package cn.itcast.store.utils;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EncodingFilter implements Filter {

    public void destroy() {

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // 动态代理模式实现
        // 1.获得被代理对象(已存在，ServletRequest)
        final HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        resp.setContentType("text/html;charset=utf-8");
        // 2.创建代理对象
        HttpServletRequest myreq = (HttpServletRequest) Proxy.newProxyInstance(
                req.getClass().getClassLoader(), req.getClass().getInterfaces(), new InvocationHandler() {

                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        // 3.对指定方法进行增强(getParameter)
                        if ("getParameter".equals(method.getName())) {
                            // 4.只对GET请求进行处理
                            if ("GET".equalsIgnoreCase(req.getMethod())) {
                                // 5.执行被代理对象里面的方法(getParameter)
                                String parameter = (String) method.invoke(req, args);

                                // 非空判断
                                if (parameter != null) {
                                    // 6.对参数进行编码处理
                                    parameter = new String(parameter.getBytes("iso8859-1"), "utf-8");
                                }
                                return parameter;
                            } else if ("POST".equalsIgnoreCase(req.getMethod())) {
                                req.setCharacterEncoding("utf-8");
                            }
                            // 7.如果是其它方式，就执行原有的方法
                            return method.invoke(req, args);
                        }
                        // 8.如果是其它方法被执行，就执行对应的方法
                        return method.invoke(req, args);
                    }
                });

        // 9.放行(让程序执行目标类的方法)
        chain.doFilter(myreq, resp);

    }

    public void init(FilterConfig fConfig) throws ServletException {

    }

}
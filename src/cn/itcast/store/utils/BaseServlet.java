package cn.itcast.store.utils;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * 因为当前类必须有处理请求的能力，所以必须是一个servlet
 * @author Never Say Never
 * @date 2017年5月4日	
 * @version V1.0
 */
public class BaseServlet extends HttpServlet {
    
    /**
     * 以后我们写的一些功能的servlet，只要继承当前类(BaseServlet)，那么就不用重复去写反射这一块代码！
     */
    
    @Override
    public void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String methodName = request.getParameter("method");
        
        try {
            //1.字节码对象（当前类的实例对象.getClass）【注意，此时的this代表的是UserServlet1的实例对象】
            Class clazz = this.getClass();
            
            //非空判断
            if(methodName!=null){
                //2.获得method对象
                Method method = clazz.getMethod(methodName, HttpServletRequest.class,HttpServletResponse.class);
                //3.执行这个方法
                String path = (String) method.invoke(this, request,response);
                //4.非空判断
                if(path!=null){
                    //说明是请求转发
                    request.getRequestDispatcher(path).forward(request, response);
                    //不让后面的代码继续执行
                    return ;
                }
            }else{
                System.out.println("哥们，请传递参数method……");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
            //NoSuchMethodException: 说明给定的参数值与servlet写的名称不一致！
        }
    }

}

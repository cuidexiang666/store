package cn.itcast.store;
import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserServlet0 extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String methodName = request.getParameter("method");
        
        /*if("register".equals(methodName)){
            register(request, response);
        }else if("active".equals(methodName)){
            active(request, response);
        }else if("login".equals(methodName)){
            login(request, response);
        }else if("logout".equals(methodName)){
            logout(request, response);
        }*/
        
        /**
         * 代码写到此处，发现有点美中不足！如果某个模块下面有很多的子功能，那么我这个servlet中就需要进行很多的判断，这样写的代码不够优美！
         * 我们想到了，这个问题不就是获得方法名并调用这个方法吗？那么可以使用之前学习过的反射来优化这块的代码 
         */
        
        try {
            //1.字节码对象（当前类的实例对象.getClass）
            Class clazz = this.getClass();
            
            //非空判断
            if(methodName!=null){
                //2.获得method对象
                Method method = clazz.getMethod(methodName, HttpServletRequest.class,HttpServletResponse.class);
                //3.执行这个方法
                method.invoke(this, request,response);
            }else{
                System.out.println("哥们，请传递参数method……");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
            //NoSuchMethodException: 说明给定的参数值与servlet写的名称不一致！
        }
        
        /**
         * 程序写到此处，我们发现，如果再开发一个模块，那么上面的那部分代码又得写一次，这样会导致上面那一部分代码的重用性特别差
         * 联想到之前学习的过的技术，使用继承可以提高代码的重用性！我们可以将当前的servlet或者其它模块的servlet都去继承一个类，将上面那部分代码写到父类中去
         * 但是，这个父类必须有能力接收用户请求！而servlet有这个能力！这样就好办了，让这个父类去继承servlet！
         */
        
    }
    
    //定义一些方法完成用户需要进行的操作
    public void register(HttpServletRequest request, HttpServletResponse response){
        System.out.println("用户注册");
    }
    public void active(HttpServletRequest request, HttpServletResponse response){
        System.out.println("用户激活");
    }
    public void login(HttpServletRequest request, HttpServletResponse response){
        System.out.println("用户登录");
    }
    public void logout(HttpServletRequest request, HttpServletResponse response){
        System.out.println("用户退出");
    }
    
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

}
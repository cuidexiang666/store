<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Insert title here</title>
	</head>
	<body>
		<!-- 
			问题：如果每一个小的功能都写一个servlet，那么整个项目会有很多的servlet，这样操作会造成这个项目先得特别臃肿 
			解决：我们想到了，将一个功能模块写成一个servlet,然后在这个请求的servlet后面跟上一个参数(参数名随便给，参数值写成具体功能的方法名称)
		-->
		<h3>用户模块</h3>
		<%-- <a href="${pageContext.request.contextPath }/RegisterServlet">用户注册</a>
		<a href="${pageContext.request.contextPath }/ActiveServlet">用户激活</a>
		<a href="${pageContext.request.contextPath }/LoginServlet">用户登录</a>
		<a href="${pageContext.request.contextPath }/LogoutServlet">用户退出</a> --%>
		
		<%-- <a href="${pageContext.request.contextPath }/UserServlet0">用户注册</a>
		<a href="${pageContext.request.contextPath }/UserServlet0?method=active">用户激活</a>
		<a href="${pageContext.request.contextPath }/UserServlet0?method=login">用户登录</a>
		<a href="${pageContext.request.contextPath }/UserServlet0?method=logout1">用户退出</a> --%>
		
		<a href="${pageContext.request.contextPath }/UserServlet1">用户注册</a>
		<a href="${pageContext.request.contextPath }/UserServlet1?method=active">用户激活</a>
		<a href="${pageContext.request.contextPath }/UserServlet1?method=login">用户登录</a>
		<a href="${pageContext.request.contextPath }/UserServlet1?method=logout1">用户退出</a>
		
		<h3>商品模块</h3>
		<a href="${pageContext.request.contextPath }/AddProductServlet">添加商品</a>
		<a href="${pageContext.request.contextPath }/FindProductServlet">查询商品</a>
		<a href="${pageContext.request.contextPath }/ModifyProductServlet">修改商品</a>
		<a href="${pageContext.request.contextPath }/DeleteProductServlet">删除商品</a><br/>
		……
		……
		……
	</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script type="text/javascript">
	//页面加载事件
	$(function(){
		//alert($("#categoryname li").size());
		//判断
		if($("#categoryname li").size()==0){
			//同步方式
			//location.href = "${pageContext.request.contextPath}/ProductServlet?method=findAllCategory";
			
			//异步方式
			$.post("${pageContext.request.contextPath}/ProductServlet",{"method":"findAllCategory"},function(data){
				//alert(data);
				if(data!=""){
					$(data).each(function(i,m){
						//alert(m.cname);
						var temp = "<li><a href='${pageContext.request.contextPath}/ProductServlet?method=findAllProductByCid&pageNumber=1&cid="+m.cid+"'>"+m.cname+"</a></li>";
						$("#categoryname").append(temp);
					});
				}
			},"json");
		}
	});
</script>
<!--
         	描述：菜单栏
         -->
<div class="container-fluid">
	<div class="col-md-4">
		<img src="img/logo2.png" />
	</div>
	<div class="col-md-5">
		<img src="${pageContext.request.contextPath}/img/header.png" />
	</div>
	<div class="col-md-3" style="padding-top:20px">
		<ol class="list-inline">
			<c:choose>
				<c:when test="${not empty exsitUser }">
					<li><span style="color:red">欢迎,${exsitUser.username}</span></li>
					<li><a href="${pageContext.request.contextPath}/cart.jsp">购物车</a></li>
					<li><a href="${pageContext.request.contextPath}/OrderServlet?method=searchOrdersByUidForPage&pageNumber=1">我的订单</a></li>
					<li><a href="${pageContext.request.contextPath}/UserServlet?method=logout">注销</a></li>
				</c:when>
				<c:otherwise>
					<li><a href="${pageContext.request.contextPath}/login.jsp">登录</a></li>
					<li><a href="${pageContext.request.contextPath}/register.jsp">注册</a></li>
					<li><a href="${pageContext.request.contextPath}/cart.jsp">购物车</a></li>
				</c:otherwise>
			</c:choose>
		</ol>
	</div>
</div>
<!--
         	描述：导航条
         -->
<div class="container-fluid">
	<nav class="navbar navbar-inverse">
		<div class="container-fluid">
			<!-- Brand and toggle get grouped for better mobile display -->
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
					<span class="sr-only">Toggle navigation</span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="${pageContext.request.contextPath }">首页</a>
			</div>

			<!-- Collect the nav links, forms, and other content for toggling -->
			<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
				<ul class="nav navbar-nav" id = categoryname>
				
				<c:if test="${not empty categorys }">
					<c:forEach items="${categorys }" var="category">
						<li><a href="#">${category.cname }</a></li>
					</c:forEach>
				</c:if>
				
					<!-- <li class="active"><a href="#">手机数码<span class="sr-only">(current)</span></a></li>
					<li><a href="#">电脑办公</a></li>
					<li><a href="#">电脑办公</a></li>
					<li><a href="#">电脑办公</a></li> -->
				</ul> 
				<form class="navbar-form navbar-right" role="search">
					<div class="form-group">
						<input type="text" class="form-control" placeholder="Search">
					</div>
					<button type="submit" class="btn btn-default">Submit</button>
				</form>

			</div>
			<!-- /.navbar-collapse -->
		</div>
		<!-- /.container-fluid -->
	</nav>
</div>
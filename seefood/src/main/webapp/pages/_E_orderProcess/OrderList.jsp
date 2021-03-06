<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<c:if test="${empty bean}">
	<c:redirect url="/pages/login/login.jsp" />
</c:if>
<jsp:useBean id="orderBeans" class="_D_shopping.model.OrderDAO"
	scope="page" />
<title>seefood-食府</title>
<link rel="shortcut icon"
	href="../map0331/img/%E4%B8%AD%E5%BC%8F%E9%A4%90%E5%BB%B3.png">
<link rel="stylesheet" href="<c:url value="/css/style3.css" />"
	type="text/css">
<!-- <link rel='stylesheet prefetch' -->
<!-- 	href='//ajax.googleapis.com/ajax/libs/jqueryui/1.11.2/themes/smoothness/jquery-ui.css'> -->
<script src="https://use.fontawesome.com/63852fa2ad.js"></script>
<link rel="stylesheet" href="<c:url value="/css/list.css" />" type="text/css">
<meta name="viewport" content="initial-scale=1.0, user-scalable=no">
</head>
<body>
	<c:set var="funcName" value="ORD" scope="session" />



	<div id="overlay"></div>
	<span id="menu"><i class="fa fa-navicon"></i></span>
	<header>
	<h1>
		<span>${bean.memberNicknName}</span><br>
		<br>的訂購紀錄
	</h1>
	</header>
	<jsp:include page="/indexleft.jsp" />
	<section>
	<div class="container">
		<article ><article style="
    height: 600px;">
		
<div id="notebooks" ng-app="notebooks" ng-controller="NotebookListCtrl" style="overflow:auto;">
<input type="text" id="query" ng-model="query" readonly="readonly" value="點選訂單可看詳細內容" style="text-align: center;">
<c:forEach var="anOrderBean" varStatus="stat" items="${orderBeans.allOrders}">
						<c:if test="${anOrderBean.memberNicknName==bean.memberNicknName}">
  <ul  class="listul">
    <a href='<c:url value='orderDetail.do?memberId=${bean.memberNicknName}&orderNo=${anOrderBean.orderNo}' />'><li class="listli" ng-repeat="notebook in notebooks | filter:query | orderBy: orderList">
      訂單編號:  ${anOrderBean.orderNo} <br/>
      訂購日期: ${anOrderBean.orderDate}<br/>
      <div class="right top">總花費紅利:<fmt:formatNumber value="${anOrderBean.total}" pattern="#,###,###" />點
	  </div>
    </li></a>
  </ul>
  
  </c:if>
  </c:forEach>
  
</div>

		

		</article></article>
	</div>
	<jsp:include page="../goTop/goTop.jsp" />
	</section>
	<script
		src='//cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js'></script>
	<script
		src='//ajax.googleapis.com/ajax/libs/jqueryui/1.11.2/jquery-ui.min.js'></script>
		
	<script>
		$(function() {

			// Window Dimentions
			var w = $(window).width(), h = $(window).height();
			//$('header').width(w);
			$('header').height(h);
			// Window Dimentions Resize
			$(window).resize(function() {
				var w = $(window).width(), h = $(window).height();
				//$('header').width(w);
				$('header').height(h);
			});

			//
			$('#menu').click(function() {
				$(this).toggleClass('on');
				$('#overlay').toggleClass('on');
				$('html').toggleClass('on');
				$('menu').toggleClass('on');
				$('header').toggleClass('on');
				$('section').toggleClass('on');
				if ($('#menu > i').hasClass('fa-navicon')) {
					$('#menu > i').addClass('fa-arrow-left');
					$('#menu > i').removeClass('fa-navicon');
				} else {
					$('#menu > i').addClass('fa-navicon');
					$('#menu > i').removeClass('fa-arrow-left');
				}
			});
			$('#overlay').click(function() {
				$(this).removeClass('on');
				$('#menu').removeClass('on');
				$('html').removeClass('on');
				$('menu').removeClass('on');
				$('header').removeClass('on');
				$('section').removeClass('on');
				$('#menu > i').addClass('fa-navicon');
				$('#menu > i').removeClass('fa-arrow-left');
			});

		});
		//# sourceURL=pen.js
	</script>

</body>
</html>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
   String cp = request.getContextPath();
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>spring</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<link rel="stylesheet" href="<%=cp%>/resource/css/style.css" type="text/css">
<link rel="stylesheet" href="<%=cp%>/resource/css/main.css" type="text/css">
<link rel="stylesheet" href="<%=cp%>/resource/css/photo.css" type="text/css">

<style type="text/css">
.imgLayout{
	width: 210px;
	height: 250px;
	padding: 10px 5px 10px;
	margin: 5px 5px;
	border: 1px solid #DAD9FF;
}
.subject {
     width:180px;
     height:25px;
     line-height:25px;
     margin:5px auto;
     border-top: 1px solid #DAD9FF;
     display: inline-block;
     white-space:nowrap;
     overflow:hidden;
     text-overflow:ellipsis;
     cursor: pointer;
     color: red;
}

#photomainuser tr{
	margin: 50px auto;
}

</style>
<script src="http://code.jquery.com/jquery-3.5.1.min.js"></script>
<script type="text/javascript">
function photoarticle(photoNum) {
	var url="${photoarticleUrl}&photoNum="+photoNum;
	location.href=url;
}

</script>
</head>
<body>
<div class="header">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</div>
<div class="photoselect">
	<table class="photomainuser" style="text-align: center; margin: 100px 30px 10px 250px; float: left;">
		<tr align="left"  class="photomember">
			<td>
				<button type="button" value="nam"style="width: 100%;" onclick="javascript:location.href='<%=cp%>/photo/photoMain.do'" class="btn1"> 전체보기  </button>
			</td>
		</tr>
		<tr height="30px;"></tr>
		

		<tr align="left" class="photomember">
			<td>
				<button type="button" value="nam"style="width: 100%;" onclick="javascript:location.href='<%=cp%>/photo/photoMain.do?nam=nam'"class="btn1"> 남상현</button>
			</td>
		</tr>
		<tr height="30px;"></tr>
		<tr align="left" class="photomember">
			<td>
				<button type="button" value="nam"style="width: 100%;" onclick="javascript:location.href='<%=cp%>/photo/photoMain.do?nam=kim'"class="btn1"> 김종완</button>
			</td>
		</tr>
		<tr height="30px;"></tr>
		<tr align="left" class="photomember">
			<td>
				<button type="button" value="nam"style="width: 100%;" onclick="javascript:location.href='<%=cp%>/photo/photoMain.do?nam=lee'"class="btn1"> 이다혜</button>
			</td>
		</tr>
		<tr height="30px;"></tr>
		<tr align="left" class="photomember">
			<td>
				<button type="button" value="nam"style="width: 100%;" onclick="javascript:location.href='<%=cp%>/photo/photoMain.do?nam=jun'"class="btn1"> 전미주</button>
			</td>
		</tr>
		<tr height="30px;"></tr>
		<tr align="left" class="photomember">
			<td>
				<button type="button" value="nam"style="width: 100%;" onclick="javascript:location.href='<%=cp%>/photo/photoMain.do?nam=jung'"class="btn1"> 정혜분</button>
			</td>
		</tr>
		
		
		<tr height="30px;"></tr>
		<tr height="30px;"></tr>
		<tr height="30">
			<td align="center">
			          <button type="button" class="btn" onclick="javascript:location.href='<%=cp%>/photo/created.do';" style="width: 100%">사진올리기</button>
			</td>
		</tr>
	</table>
</div>

	<table style="width: 60%; margin: 100px 10px 100px 10px; border-spacing: 0px;" class="photozone">
	<c:forEach var="dto" items="${photoMain}" varStatus="status">
                 <c:if test="${status.index==0}">
                       <tr>
                 </c:if>
                 <c:if test="${status.index!=0 && status.index%3==0}">
                        <c:out value="</tr><tr>" escapeXml="false"/>
                 </c:if>
                 
			     <td align="center">
			        <div class="imgLayout">
			             <img src="<%=cp%>/uploads/photo/${dto.imageFilename}" width="200" height="200" border="0">
			             <span class="subject" onclick="javascript:photoarticle('${dto.photoNum}');" >
			                   ${dto.subject}
			             </span>
			         </div>
			     </td>
	</c:forEach>
		
		<c:set var="n" value="${photoMain.size()}"/>
		<c:if test="${n>0&&n%3!=0}">
		        <c:forEach var="i" begin="${n%3+1}" end="3" step="1">
			         <td>
			             <div class="imgLayout">&nbsp;</div>
			         </td>
		        </c:forEach>
		</c:if>
		<c:if test="${n!=0 }">
		       <c:out value="</tr>" escapeXml="false"/>
		</c:if>
		<tr style="width: 100%; margin: 30px auto;">
			<td style="text-align: center;" colspan="3">
				${dataCount==0?"등록된 게시물이 없습니다.":paging}
			</td>
		</tr>
			</table>           
			
			
<div class="footer">
    <jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
</div>


</body>
</html>
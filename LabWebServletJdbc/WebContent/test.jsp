<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

<%@ page import="model.*" %>
<%
CustomerService service1 = new CustomerService();
CustomerBean bean = service1.login("Alex", "A");

ProductService service2 = new ProductService();
java.util.List<ProductBean> beans = service2.select(null);
%>
<h1><%=bean%></h1>
<h1><%=beans%></h1>

<%@ page import="javax.naming.*" %>
<%@ page import="javax.sql.DataSource" %>
<%@ page import="java.sql.*" %>
<%
Context ctx = new InitialContext();
DataSource dataSource = (DataSource) ctx.lookup("java:comp/env/jdbc/xxx");
Connection conn = dataSource.getConnection();
Statement stmt = conn.createStatement();
ResultSet rset = stmt.executeQuery("select * from dept");
while(rset.next()) {
	String col1 = rset.getString(1);
	String col2 = rset.getString(2);
	out.println("<h3>"+col1+":"+col2+"</h3>");
}
rset.close();
stmt.close();
conn.close();
%>



</body>
</html>
package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import model.CustomerBean;
import model.CustomerDAO;

public class CustomerDAOJdbc implements CustomerDAO {
//	private static final String URL = "jdbc:sqlserver://localhost:1433;database=java";
//	private static final String USERNAME = "sa";
//	private static final String PASSWORD = "P@ssw0rd";
	
	private DataSource dataSource = null;
	public CustomerDAOJdbc() {
		try {
			Context ctx = new InitialContext();
			dataSource = (DataSource) ctx.lookup("java:comp/env/jdbc/xxx");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		CustomerDAO dao = new CustomerDAOJdbc();
		CustomerBean bean = dao.select("Alex");
		System.out.println(bean);
		
		dao.update("EEE".getBytes(),
				"ellen@iii.org.tw", new java.util.Date(), "Ellen");
	}
	private static final String SELECT = "select * from customer where custid=?";
	@Override
	public CustomerBean select(String custid) {
		CustomerBean result = null;

		ResultSet rset = null;
		try(//Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			Connection conn = dataSource.getConnection();
			PreparedStatement stmt = conn.prepareStatement(SELECT);) {
			stmt.setString(1, custid);
			rset = stmt.executeQuery();
			if(rset.next()) {
				result = new CustomerBean();
				result.setCustid(rset.getString("custid"));
				result.setPassword(rset.getBytes("password"));
				result.setEmail(rset.getString("email"));
				result.setBirth(rset.getDate("birth"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rset!=null) {
				try {
					rset.close();
				} catch (SQLException e) {
					e.printStackTrace();
				} 
			}	
		}
		return result;
	}
	private static final String UPDATE =
			"update customer set password=?, email=?, birth=? where custid=?";
	@Override
	public boolean update(byte[] password,
				String email, java.util.Date birth, String custid) {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = dataSource.getConnection();
			stmt = conn.prepareStatement(UPDATE);

			stmt.setBytes(1, password);
			stmt.setString(2, email);
			if(birth!=null) {
				long time = birth.getTime();
				stmt.setDate(3, new java.sql.Date(time));
			} else {
				stmt.setDate(3, null);
			}
			stmt.setString(4, custid);
			int i = stmt.executeUpdate();
			if(i==1) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (stmt!=null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				} 
			}
			if (conn!=null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				} 
			}
		}
		return false;
	}
}

package model;

import java.util.Arrays;

import model.dao.CustomerDAOJdbc;

public class CustomerService {
	private CustomerDAO customerDao = new CustomerDAOJdbc();
	public boolean changePassword(String username, String oldPassword, String newPassword) {
		CustomerBean bean = this.login(username, oldPassword);
		if(bean!=null) {
			if(newPassword!=null && newPassword.length()!=0) {
				byte[] temp = newPassword.getBytes();
				return customerDao.update(
						temp, bean.getEmail(), bean.getBirth(), username);
			}
		}
		return false;
	}
	public CustomerBean login(String username, String password) {
		CustomerBean bean = customerDao.select(username);
		if(bean!=null) {
			if(password!=null && password.trim().length()!=0) {
				byte[] pass = bean.getPassword();	//
				byte[] temp = password.getBytes();	//
				if(Arrays.equals(pass, temp)) {
					return bean;
				}
			}
		}
		return null;
	}
	public static void main(String[] args) {
		CustomerService service = new CustomerService();
		CustomerBean bean = service.login("Alex", "A");
		System.out.println(bean);
		
		service.changePassword("Ellen", "EEE", "E");
	}
}

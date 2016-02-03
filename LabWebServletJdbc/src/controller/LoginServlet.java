package controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.CustomerBean;
import model.CustomerService;

@WebServlet(
		urlPatterns={"/secure/login.controller"}
)
public class LoginServlet extends HttpServlet {
	private CustomerService customerService = new CustomerService();
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
//接收資料
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
//驗證資料
		Map<String, String> error = new HashMap<String, String>();
		request.setAttribute("error", error);

		if(username==null || username.trim().length()==0) {
			error.put("username", "Please enter ID to login");
		}
		if(password==null || password.trim().length()==0) {
			error.put("password", "Please enter PWD to login");
		}
		
		if(error!=null && !error.isEmpty()){
			request.getRequestDispatcher(
					"/secure/login.jsp").forward(request, response);
			return;
		}
//呼叫model
		CustomerBean bean = customerService.login(username, password);
		
//根據model執行結果顯示view
		if(bean==null) {
			error.put("password", "Login failed, please try again");
			request.getRequestDispatcher(
					"/secure/login.jsp").forward(request, response);
		} else {
			HttpSession session = request.getSession();
			session.setAttribute("user", bean);

			String path = request.getContextPath();
			response.sendRedirect(path+"/index.jsp");
		}
	}
	@Override
	protected void doPost(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		this.doGet(req, resp);
	}
}

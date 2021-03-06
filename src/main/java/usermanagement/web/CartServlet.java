package usermanagement.web;

import java.io.IOException;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import usermanagement.dao.CartDao;
import usermanagement.dao.UserDao;
import usermanagement.model.Item;
import usermanagement.model.User;

/**
 * Servlet implementation class UserServlet
 * Allows client side services to access backend Java methods
 */
@WebServlet("/")
public class CartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CartDao cartDao;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CartServlet() {
    	this.cartDao = new CartDao();
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getServletPath();
		switch (action) {
    		case "/cart":
    			addToCart(request, response);
    			break;
		}
    		
    
    }
    
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getServletPath();
		switch(action) {
			case "/cart":
				showCartPage(request, response);
				break;
			case "/add":
				addTotal(request, response);
				break;
			case "/search":
				showQueryPage(request, response);
				break;
			case"/checkout":
				showCheckoutPage(request, response);
				break;
		}
			
	}
	

	
	private void showCartPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String query = request.getParameter("q");
		String add = request.getParameter("id");
		System.out.println("Query: " + query);
		System.out.println("Id: "+add );
		List<Item> listItem;
		if ( query != null ) {
			if ( !query.equals("") ) {
				listItem = cartDao.selectQueryItems(query);
			} else {
				listItem = cartDao.selectAllItems();
			}
		} else {
			listItem = cartDao.selectAllItems();
		}
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("cart.jsp");
		request.setAttribute("listItem", listItem);
		dispatcher.forward(request, response);
	}
	
	private void showCheckoutPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Show checkout page");
		RequestDispatcher dispatcher = request.getRequestDispatcher("checkout.jsp");
		List<Item> checkOutItem = cartDao.getCheckoutItems();
		System.out.println(checkOutItem.size());
		request.setAttribute("checkOutItem", checkOutItem);
		dispatcher.forward(request, response);
	}
	
	private void showQueryPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String query = request.getParameter("q");
		List<Item> listItem= cartDao.selectQueryItems(query);
		RequestDispatcher dispatcher = request.getRequestDispatcher("cart.jsp");
		request.setAttribute("listItem", listItem);
		dispatcher.forward(request, response);
	}
	
	
	
	private void addTotal(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("cart.jsp");
		List<Item> listItem = cartDao.selectAllItems();
		request.setAttribute("listItem", listItem);
		dispatcher.forward(request, response);
	}
	
	private void addToCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("cart.jsp");
		System.out.println(request);
		String query = request.getParameter("id");
		int id = Integer.valueOf(query);
		cartDao.addItemToCart(id);
		
		List<Item> listItem = cartDao.selectAllItems();
		request.setAttribute("listItem", listItem);
		dispatcher.forward(request, response);
	}

}

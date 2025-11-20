package entities;
import java.util.ArrayList;
import java.util.List;

public class Customer extends User {
	   private String address;
	    private ShoppingCart shoppingCart;
	    private List<Order> orders = new ArrayList<>();

	    public Customer(int userId, String username, String email, String address) {
	        super(userId, username, email);
	        this.address = address;
	        this.shoppingCart = new ShoppingCart();
	    }

	    public String getAddress() {
	        return address;
	    }

	    public ShoppingCart getShoppingCart() {
	        return shoppingCart;
	    }

	    public List<Order> getOrders() {
	        return orders;
	    }

	    public void addOrder(Order order) {
	        orders.add(order);
	    }

	    @Override
	    public String toString() {
	        return "User ID: " + userId + ", Username: " + username + ", Email: " + email + ", Address: " + address;
	    }
}

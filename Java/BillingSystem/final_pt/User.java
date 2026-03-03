package final_pt;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class User 
{
	 private String username;
	    private String password;
	    private String fullName;
	    private double balance;
	    private List<Transaction> transactions;
	    
	    public User(String username, String password, String fullName) {
	        this.username = username;
	        this.password = password;
	        this.fullName = fullName;
	        this.balance = 1000.0; // Initial balance
	        this.transactions = new ArrayList<>();
	        addTransaction("Initial Deposit", 1000.0);
	    }
	    
	    public String getUsername() { return username; }
	    public String getPassword() { return password; }
	    public String getFullName() { return fullName; }
	    public double getBalance() { return balance; }
	    public List<Transaction> getTransactions() { return transactions; }
	    
	    public void addTransaction(String description, double amount) {
	        transactions.add(new Transaction(description, amount));
	        balance += amount;
	    }
	    
	    public boolean canPay(double amount) {
	        return balance >= amount;
	    }
	}

	class Transaction {
	    private LocalDateTime dateTime;
	    private String description;
	    private double amount;
	    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	    
	    public Transaction(String description, double amount) {
	        this.dateTime = LocalDateTime.now();
	        this.description = description;
	        this.amount = amount;
	    }
	    
	    public LocalDateTime getDateTime() { return dateTime; }
	    public String getDescription() { return description; }
	    public double getAmount() { return amount; }
	    
	    @Override
	    public String toString() {
	        return String.format("%s | %s | %s$%.2f",
	            dateTime.format(formatter),
	            description,
	            amount >= 0 ? "+" : "-",
	            Math.abs(amount));
	    }
}

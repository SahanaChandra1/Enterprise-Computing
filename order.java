//Sahana Chandra
//cnt4717-Fall 2021
//project 1
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Order {
	private int currentNumItems = 0;
	private double orderSubtotal = 0;
	private double orderTotal = 0;
	private int totalItems = 0;
	private int maxNumItems = -1;
	private String filename = "transactions.txt";
	private ArrayList<String> items = new ArrayList<>(); //all confirmed items
	private StringBuilder viewOrder = new StringBuilder();
	private StringBuilder finishOrder = new StringBuilder();

	File file = new File(filename);
	String[] itemInfo = new String[6];

	BigDecimal num  = new BigDecimal("58.987");


	public String getFinishOrder() {
		return this.finishOrder.toString();
	}


	public void setFinishOrder(String date, String time) {
		this.setOrderTotal();
		this.finishOrder.append("Date: " + date + " " + time);
		this.finishOrder.append(System.getProperty("line.separator"));
		this.finishOrder.append(System.getProperty("line.separator"));
		this.finishOrder.append("Number of line items: " + this.getTotalItems());
		this.finishOrder.append(System.getProperty("line.separator"));
		this.finishOrder.append(System.getProperty("line.separator"));
		this.finishOrder.append("Item# /ID / Price / Qty / Disc %/ Subtotal");
		this.finishOrder.append(System.getProperty("line.separator"));
		this.finishOrder.append(System.getProperty("line.separator"));
		this.finishOrder.append(this.getViewOrder());
		this.finishOrder.append(System.getProperty("line.separator"));
		this.finishOrder.append(System.getProperty("line.separator"));
		this.finishOrder.append("Order subtotal:   $" + new DecimalFormat("#0.00").format(this.getOrderTotal()));
		this.finishOrder.append(System.getProperty("line.separator"));
		this.finishOrder.append(System.getProperty("line.separator"));
		this.finishOrder.append("Tax rate:     6%");
		this.finishOrder.append(System.getProperty("line.separator"));
		this.finishOrder.append(System.getProperty("line.separator"));
		this.finishOrder.append("Order total:      $" + new DecimalFormat("#0.00").format(this.getOrderSubtotal()));
		this.finishOrder.append(System.getProperty("line.separator"));
		this.finishOrder.append(System.getProperty("line.separator"));
		this.finishOrder.append("Thanks for shopping at NileDot!");


	}

	public String getViewOrder() {
		return this.viewOrder.toString();
	}

	public void addToViewOrder(String order) {
		viewOrder.append(this.getTotalItems() + ". " + order);
		viewOrder.append(System.getProperty("line.separator"));
	}

	public String[] getItemInfo() {
		return itemInfo;
	}

	public void setItemInfo(String itemID, String title, String price, String quantityOfItem, String discountPercentage, String totalDiscount) {
		itemInfo[0] = itemID;
		itemInfo[1] = title;
		itemInfo[2] = price;
		itemInfo[3] = quantityOfItem;
		itemInfo[4] = discountPercentage;
		itemInfo[5] = totalDiscount;
	}

	public double getTotalDiscount(int quantity, double ItemPrice) {

		if(quantity >= 1 && quantity <= 4 )
			return (quantity * ItemPrice); //0% discount
		if(quantity >= 5 && quantity <= 9)
			return .10 * (quantity * ItemPrice); //10% discount
		if(quantity >= 10 && quantity <= 14)
			return .15 * (quantity * ItemPrice); //15% discount
		if(quantity >= 15)
			return .20 * (quantity * ItemPrice); //20% discount

		return 0.0;
	}

	public int getDiscountPercentage(int quantity) {
		if(quantity >= 1 && quantity <= 4 )
			return 0; //0% discount
		if(quantity >= 5 && quantity <= 9)
			return 10;//10% discount
		if(quantity >= 10 && quantity <= 14)
			return 15; //15% discount
		if(quantity >= 15)
			return 20; //20% discount
		return 0;
	}

	public String viewOrder() {
		return filename;

	}

	public void prepareTransaction() {
		String lineItem = new String();
		for(int i = 0; i< this.itemInfo.length; i++){
			lineItem += this.itemInfo[i] + ", ";
		}
		items.add(lineItem);
	}

	public void printTransactions() throws IOException {
		Calendar calendar= Calendar.getInstance();
		Date date = calendar.getTime();
		SimpleDateFormat permutation = new SimpleDateFormat("yyMMddyyHHmm");
		SimpleDateFormat time = new SimpleDateFormat("hh:mm:ss a z");
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");

		//set finsihOrder string builder for dialog box
		this.setFinishOrder(dateFormat.format(date), time.format(date));

		//create file if none exist
		if(file.exists() == false) {
			file.createNewFile();
		}

		//append only if file exist
		PrintWriter outputStream = new PrintWriter(new FileWriter(filename, true));

		//write to file
		for(int i = 0; i< this.items.size(); i++){
			outputStream.append(permutation.format(date) + ", ");
			String lineItem = this.items.get(i);
			outputStream.append(lineItem);
			outputStream.append(dateFormat.format(date) + ", ");
			outputStream.append(time.format(date));
			outputStream.println();
		}

		outputStream.flush();
		outputStream.close();
	}


	public int getCurrentNumItems() {
		return currentNumItems;
	}
	public void setCurrentNumItems(int currentNumItems) {
		this.currentNumItems = this.currentNumItems + currentNumItems;
	}
	public double getOrderSubtotal() {
		return orderSubtotal;
	}
	public void setOrderSubtotal(int quantity, double ItemPrice) {
		this.orderSubtotal = this.orderSubtotal + this.getTotalDiscount(quantity, ItemPrice);
	}
	public int getTotalItems() {
		return totalItems;
	}
	public void setTotalItems(int totalItems) {
		this.totalItems = totalItems;
	}
	public int getMaxNumItems() {
		return maxNumItems;
	}
	public void setMaxNumItems(int maxNumItems) {
		this.maxNumItems = maxNumItems;
	}
	public double getOrderTotal() {
		return orderTotal;
	}

	public void setOrderTotal() {
		this.orderTotal = this.orderSubtotal + (.06 * this.orderSubtotal);
	}



}

//Sahana Chandra
//cnt4717-Fall 2021
//project 1
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.swing.*;
import java.util.*;

public class NileDot extends JFrame {
	private String inventoryFile = "inventory.txt";
	private ArrayList<Item> inventory;
	private Order order = new Order();

	// Text Fields
	private JTextField jtfNumItems = new JTextField();
	private JTextField jtfItemID = new JTextField();
	private JTextField jtfQuantity = new JTextField();
	private JTextField jtfItemInfo = new JTextField();
	private JTextField jtfTotalItems = new JTextField();

	// Buttons
	private JButton jbtProcessItem = new JButton("Process Item #1");// need to update item #


	private JButton jbtConfirmItem = new JButton("Confirm Item #1");// need to update item #
	private JButton jbtViewOrder = new JButton("View Order");
	private JButton jbtFinishOrder = new JButton("Finish Order");
	private JButton jbtNewOrder = new JButton("New Order");
	private JButton jbtExit = new JButton("Exit");

	// Jlabels
	JLabel jlbSubtotal = new JLabel("Order Subtotal for 0 item(s):");
	JLabel jlbItemID = new JLabel("Enter Item ID for Item #1:");
	JLabel jlbQuantity = new JLabel("Enter Quantitiy for Item #1:");
	JLabel jlbItemInfo = new JLabel("Item #1 Info:");


	public NileDot() throws FileNotFoundException
	{
		this.getInventoryFromFile();

		JPanel p1 = new JPanel(new GridLayout(5,2));
		p1.add(new JLabel("Enter number of items in this order:"));
		p1.add(jtfNumItems);
		p1.add(jlbItemID);
		p1.add(jtfItemID);
		p1.add(jlbQuantity);
		p1.add(jtfQuantity);
		p1.add(jlbItemInfo);
		p1.add(jtfItemInfo);
		p1.add(jlbSubtotal);
		p1.add(jtfTotalItems);




		JPanel p2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		p2.add(jbtProcessItem);
		p2.add(jbtConfirmItem);
		p2.add(jbtViewOrder);
		p2.add(jbtFinishOrder);
		p2.add(jbtNewOrder);
		p2.add(jbtExit);

		//deactivate
		this.jbtConfirmItem.setEnabled(false);
		this.jbtViewOrder.setEnabled(false);
		this.jbtFinishOrder.setEnabled(false);
		this.jtfTotalItems.setEnabled(false);
		this.jtfItemInfo.setEnabled(false);


		add(p1, BorderLayout.NORTH);
		add(p2, BorderLayout.SOUTH);



		jbtProcessItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int numOfItemsInOrder = Integer.parseInt(jtfNumItems.getText());
				int ItemID = Integer.parseInt(jtfItemID.getText());
				int quantityOfItem = Integer.parseInt(jtfQuantity.getText());


				if(order.getMaxNumItems() == -1 && numOfItemsInOrder > 0) {
					order.setMaxNumItems(numOfItemsInOrder);
					jtfNumItems.setEnabled(false);
				}
				int ItemIndex = linearSearch(ItemID);
				if(ItemIndex != -1)
				{
					Item foundItem = inventory.get(ItemIndex);
					order.setItemInfo(foundItem.getItemID() + "", foundItem.getInfo(), foundItem.getPrice() + "", quantityOfItem + "", order.getDiscountPercentage(quantityOfItem) + "", order.getTotalDiscount(quantityOfItem, foundItem.getPrice()) + "");
					String ItemInfo = foundItem.getItemID() + foundItem.getInfo() +  " $" + foundItem.getPrice() + " " + quantityOfItem + " " + order.getDiscountPercentage(quantityOfItem) + "% " + order.getTotalDiscount(quantityOfItem, foundItem.getPrice()); //need to get discound
					jtfItemInfo.setText(ItemInfo);
					jbtConfirmItem.setEnabled(true);
					jbtProcessItem.setEnabled(false);
					order.setOrderSubtotal(quantityOfItem, foundItem.getPrice());
					jtfItemInfo.setEnabled(false);
					jtfTotalItems.setEnabled(false);
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Item ID " + ItemID + " not in file.");
				}
			}

		});

		jbtConfirmItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e)
			{
				int numOfItemsInOrder = Integer.parseInt(jtfNumItems.getText());
				int ItemID = Integer.parseInt(jtfItemID.getText());
				int quantityOfItem = Integer.parseInt(jtfQuantity.getText());

				if(numOfItemsInOrder > order.getMaxNumItems())
					System.out.println("went over qantity");

				order.setCurrentNumItems(quantityOfItem);
				order.setTotalItems(order.getTotalItems() + 1);

				JOptionPane.showMessageDialog(null, "Item #" + order.getTotalItems() + " accepted");
				order.prepareTransaction();
				order.addToViewOrder(jtfItemInfo.getText());


				jbtProcessItem.setEnabled(true);
				jbtViewOrder.setEnabled(true);
				jbtFinishOrder.setEnabled(true);
				jbtConfirmItem.setEnabled(false);
				jtfNumItems.setEnabled(false);

				jbtProcessItem.setText("Process Item #" + (order.getTotalItems() + 1));
				jbtConfirmItem.setText("Confirm Item #" + (order.getTotalItems() + 1));

				jtfItemID.setText("");
				jtfQuantity.setText("");
				jtfTotalItems.setText("$" +  new DecimalFormat("#0.00").format(order.getOrderSubtotal()));

				jlbSubtotal.setText("Order subtotal for " + order.getCurrentNumItems() + " item(s)");
				jlbItemID.setText("Enter Item ID for Item #" + (order.getTotalItems() + 1) + ":");
				jlbQuantity.setText("Enter quantity for Item #" + (order.getTotalItems() + 1) + ":");
				if(order.getCurrentNumItems() < order.getMaxNumItems())
				jlbItemInfo.setText("Item #" + (order.getTotalItems() + 1) + " info:");

				if(order.getCurrentNumItems() >= order.getMaxNumItems()) {
					jlbItemID.setVisible(false);
					jlbQuantity.setVisible(false);
					jbtProcessItem.setEnabled(false);
					jbtConfirmItem.setEnabled(false);
				}
			}


		});


		jbtViewOrder.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, order.getViewOrder());
			}
		});



		jbtFinishOrder.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				try {
					order.printTransactions();
					JOptionPane.showMessageDialog(null, order.getFinishOrder());

				} catch (IOException e1) {
					e1.printStackTrace();
				}
				NileDot.super.dispose();
			}
		});


		jbtNewOrder.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {


				NileDot.super.dispose();
				try {
					NileDot.main(null);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		});


		jbtExit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				NileDot.super.dispose();
			}
		});

	}


	public int linearSearch(int ItemID) {
		for(int i = 0; i < this.inventory.size(); i++) {
			Item currentItem = inventory.get(i);
			if(currentItem.getItemID() == ItemID)
				return i;
		}
		return -1;
	}

	public void getInventoryFromFile() throws FileNotFoundException {
		this.inventory = new ArrayList<Item>();
		File file = new File("inventory.txt");
		Scanner textFile = new Scanner(file);


		while (textFile.hasNextLine()) {
			String Item = textFile.nextLine();
			String[] ItemInfo = Item.split(",");

			Item currentItem = new Item();
			currentItem.setItemID(Integer.parseInt(ItemInfo[0]));

			currentItem.setInfo(ItemInfo[1]);

			currentItem.setPrice(Double.parseDouble(ItemInfo[2]));

			inventory.add(currentItem);
		}

		textFile.close();
		for (int i = 0; i < inventory.size(); i++) {
			Item current = inventory.get(i);
			System.out.println(current.getItemID() + ", " + current.getInfo() + ", " + current.getPrice());
		}
	}


	public ArrayList<Item> getInventory() {
		return inventory;
	}

	public void setInventory(ArrayList<Item> inventory) {
		this.inventory = inventory;
	}


	public static void main(String[] args) throws FileNotFoundException {
		NileDot frame = new NileDot();
		frame.pack();
		frame.setInfo("Item Store");
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}
}

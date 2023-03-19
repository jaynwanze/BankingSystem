package ca3;

import java.awt.*;

import java.awt.event.*;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.*;

import java.time.Year;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import java.util.Map.Entry;

import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;


public class BankGUI extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	int index = 0;

	// GUI-related variables
	JPanel panelBankInterface, panelButtons, menuPanel, panelShowCustomerInfo, panelNavigation, panelNavButtons;
	JLabel labelName, labelBankInfo, labelCustNavigation;
	Button btnSubmit, btnFinish, btnClear;

	JTextArea taShowInfo;

	JButton btnFirstCustRecord, btnLastCustRecord, btnNextCustRecord, btnPrevCustRecord;
	LinkedList<Customer> customerList = new LinkedList<Customer>();
	LinkedHashMap<String, ArrayList<CustomerAccount>> map = new LinkedHashMap<String, ArrayList<CustomerAccount>>();
	LinkedHashMap<String, ArrayList<AccountTransaction>> map2 = new LinkedHashMap<String, ArrayList<AccountTransaction>>();

	int nextCust = 0;
	boolean notClicked = true;
	private static final DecimalFormat df = new DecimalFormat("0.00");
	//*************************************
    //BankGUI Class
	//*************************************
	public BankGUI() {

		//frame title
		super("Bank Records Information System");
		// welcome message
		JOptionPane.showMessageDialog(null, "Welcome to the Banking Information System");
		JPasswordField adminPass = new JPasswordField();
		// creating an administrator user/password
		String msg = "Please enter new password of system administrator.";
		boolean inputValid = false;
		while (!inputValid) {
			int userInput = JOptionPane.showOptionDialog(getContentPane(), new Object[] { msg, adminPass }, "Confirm",
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

			if (userInput == JOptionPane.CANCEL_OPTION) {
				System.exit(0);
			}
			if (userInput == JOptionPane.CLOSED_OPTION) {
				System.exit(0);
			}
			if (!adminPass.getText().isBlank() && adminPass.getText().length() >= 8) {
				inputValid = true;
			} else {
				JOptionPane.showMessageDialog(null, "Password must be at least 8 characters long.");

			}
		}
		Administrator a = new Administrator(adminPass.getText());

		// creating panels and setting border layouts
		this.setLayout(new BorderLayout()); // set layout to borderLayout

		panelBankInterface = new JPanel(new BorderLayout());
		panelButtons = new JPanel();
		this.add(panelBankInterface, "Center"); // adding panel interface to the frame
		this.add(panelButtons, "South"); // adding panelButtons to the frame

		panelBankInterface.setBorder(new TitledBorder(new BevelBorder(BevelBorder.RAISED)));

		menuPanel = new JPanel(new BorderLayout());
		menuPanel.setBorder(new EmptyBorder(2, 2, 2, 2));

		panelShowCustomerInfo = new JPanel(new BorderLayout());
		panelShowCustomerInfo.setBorder(new EmptyBorder(2, 2, 2, 2));

		panelNavigation = new JPanel(new GridLayout(2, 1));
		panelNavigation.setBorder(new EmptyBorder(2, 2, 2, 2));

		panelBankInterface.add(menuPanel, "North");
		panelBankInterface.add(panelShowCustomerInfo, "Center");
		panelBankInterface.add(panelNavigation, "East");

		// creating customer menu
		JMenu customer = new JMenu("Customer");
		customer.setMnemonic(KeyEvent.VK_C);

		JMenu customerDetails = new JMenu("Customer Details");
		JMenu customerAccount = new JMenu("Customer Account");
		// creating customer details menu items
		JMenuItem createCustomerDetails = new JMenuItem("Create");
		JMenu modifyCustomerDetails = new JMenu("Modify");
		JMenuItem modifyCustomerPassword = new JMenuItem("Modify Password");
		JMenuItem deleteCustomerDetails = new JMenuItem("Delete");
		customerDetails.add(createCustomerDetails);
		customerDetails.add(modifyCustomerDetails);
		modifyCustomerDetails.add(modifyCustomerPassword);
		customerDetails.add(deleteCustomerDetails);

		// creating customer records menu items
		JMenuItem createCustomerAccount = new JMenuItem("Create");
		JMenu modifyCustomerAccount = new JMenu("Modify");
		JMenuItem modifyCustomerAccountBal = new JMenuItem("Modify Account Balance");
		JMenuItem deleteCustomerAccount = new JMenuItem("Delete");
		customerAccount.add(createCustomerAccount);
		customerAccount.add(modifyCustomerAccount);
		modifyCustomerAccount.add(modifyCustomerAccountBal);
		customerAccount.add(deleteCustomerAccount);

		customer.add(customerDetails);
		customer.add(customerAccount);

		// creating navigation menu
		JMenu navigation = new JMenu("Navigation");
		JMenuItem customerRecords = new JMenuItem("Display Customer Records");
		JMenuItem customerSpecificAccInfo = new JMenuItem("Display Specific Customer Account Information");

		navigation.add(customerRecords);
		navigation.add(customerSpecificAccInfo);

		// creating navigation menu
		JMenu transaction = new JMenu("Transaction");
		JMenuItem withdrawal = new JMenuItem("Withdrawal");
		JMenuItem lodgement = new JMenuItem("Lodgement");
		transaction.add(withdrawal);
		transaction.add(lodgement);

		// creating navigation menu
		JMenu eStatement = new JMenu("E-Statement");
		JMenuItem displayEStatement = new JMenuItem("Display Specific Customer E-Statement");
		eStatement.add(displayEStatement);

		// creating menu bar
		JMenuBar menuBar = new JMenuBar();

		menuBar.add(customer);
		menuBar.add(navigation);
		menuBar.add(transaction);
		menuBar.add(eStatement);

		menuPanel.add(menuBar);

		labelBankInfo = new JLabel("Banking Information");
		taShowInfo = new JTextArea();
		taShowInfo.setEditable(false);

		panelShowCustomerInfo.add(labelBankInfo, "North");
		panelShowCustomerInfo.add(new JScrollPane(taShowInfo), "Center");

		panelNavButtons = new JPanel(new GridLayout(0, 1));

		labelCustNavigation = new JLabel("Customer Records Navigation");

		btnFirstCustRecord = new JButton("First Customer Record");
		btnLastCustRecord = new JButton("Last Customer Record");
		btnNextCustRecord = new JButton("Next Customer Record");
		btnPrevCustRecord = new JButton("Previous Customer Record");

		panelNavButtons.add(labelCustNavigation);

		panelNavButtons.add(btnFirstCustRecord);
		panelNavButtons.add(btnLastCustRecord);
		panelNavButtons.add(btnNextCustRecord);
		panelNavButtons.add(btnPrevCustRecord);

		panelNavigation.add(panelNavButtons);

		panelNavigation.setVisible(false);
		btnFinish = new Button("Finish");
		btnClear = new Button("Clear");
		panelButtons.add(btnFinish);
		panelButtons.add(btnClear);
		
		//*************************************
        //Actionlistener to create customer details
		//*************************************
		createCustomerDetails.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Customer c = null;
				JTextField ppsNum = new JTextField();
				JTextField firstName = new JTextField();
				JTextField secondName = new JTextField();
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				JTextField dateOfBirth = new JTextField();
			
				String msg1 = "Please enter your PPS number. (Must be 8 digits)";
				String msg2 = "Please enter your first name.";
				String msg3 = "Please enter your second name.";
				String msg4 = "Please enter your date of birth (Format - DD/MM/YYYY).";
				boolean inputValid = false;
				boolean dateValid = false;
				while (!inputValid) {
					String msg = "";

					int userInput = JOptionPane.showOptionDialog(getContentPane(),
							new Object[] { msg1, ppsNum, msg2, firstName, msg3, secondName, msg4, dateOfBirth },
							"Customer Creation", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null,
							null);

					if (userInput == JOptionPane.CANCEL_OPTION || userInput == JOptionPane.CLOSED_OPTION) {
						JOptionPane.showMessageDialog(null, "Creation of customer was cancelled by user!");

						setDefaultCloseOperation(JOptionPane.ABORT);
					}

					try {
						sdf.parse(dateOfBirth.getText());
						dateValid = true;

						if (dateOfBirth.getText().length() != 10) {
							dateValid = false;
						}
						if (dateOfBirth.getText().length() == 10
								&& !(Character.isDigit(dateOfBirth.getText().charAt(0))
										&& !(Character.isDigit(dateOfBirth.getText().charAt(1))))
								&& dateOfBirth.getText().charAt(2) != ('/')
								&& !(Character.isDigit(dateOfBirth.getText().charAt(3))
										&& !(Character.isDigit(dateOfBirth.getText().charAt(4))))
								&& dateOfBirth.getText().charAt(5) != ('/')
								&& !(Character.isDigit(dateOfBirth.getText().charAt(6))
										&& !(Character.isDigit(dateOfBirth.getText().charAt(7))))
								&& !(Character.isDigit(dateOfBirth.getText().charAt(8))
										&& !(Character.isDigit(dateOfBirth.getText().charAt(9))))) {
							dateValid = false;
						}

					} catch (ParseException p) {
						dateValid = false;
					}

					if (!dateValid) {
						inputValid = false;

					}
					if (ppsNum.getText().length() == 8 && !(ppsNum.getText().isBlank())
							&& firstName.getText().matches("^[\\p{L} .'-]+$") && !(firstName.getText().isBlank())
							&& secondName.getText().matches("^[\\p{L} .'-]+$") && !(secondName.getText().isBlank())
							&& dateValid == true && dateOfBirth.getText().length() == 10) {

						if (Integer.parseInt(dateOfBirth.getText().substring(0, 2)) > 00
								&& Integer.parseInt(dateOfBirth.getText().substring(0, 2)) <= 31
								&& Integer.parseInt(dateOfBirth.getText().substring(3, 5)) > 00
								&& Integer.parseInt(dateOfBirth.getText().substring(3, 5)) <= 12 && Integer
										.parseInt(dateOfBirth.getText().substring(6, 10)) <= Year.now().getValue() - 18)

						{
							inputValid = true;
							if (userInput == JOptionPane.OK_OPTION) {

								c = new Customer(ppsNum.getText(), firstName.getText(), secondName.getText(),
										dateOfBirth.getText());
								customerList.add(c);
								taShowInfo.setText("");

								JOptionPane.showMessageDialog(null,
										"  A unique Customer ID and password will be generated for you.\n"
												+ "             Please take note and keep your password safe!.\n" +

												"Customer has been created and details have been added to system!"
												+ "\n*************************************************************\n"
												+ "                    Customer ID: " + c.getCustomerID() + "\n"
												+ "                          Password: " + c.getPassword()
												+ "\n*************************************************************\n\n");

								JOptionPane.showMessageDialog(null,
										"Please create a Customer Account which will be linked to this Customer ID.\n");

								taShowInfo.append("Newly Created Customer Details: " + "\n" + c.toString() + "\n\n");

							}

							else {
								JOptionPane.showMessageDialog(null, "Creation of customer was cancelled by user!");
								setDefaultCloseOperation(JOptionPane.ABORT);
							}
						} else {
							msg += "Please enter valid date\n";

							if (Integer.parseInt(dateOfBirth.getText().substring(0, 2)) > 31
									&& Integer.parseInt(dateOfBirth.getText().substring(0, 2)) < 01) {
								msg += "Invalid day - Please enter valid day\n";
							}
							if (Integer.parseInt(dateOfBirth.getText().substring(3, 5)) > 12
									&& Integer.parseInt(dateOfBirth.getText().substring(3, 5)) < 01) {
								msg += "Invalid month - Please enter valid month\n";
							}
							if (Integer.parseInt(dateOfBirth.getText().substring(6, 10)) > Year.now().getValue() - 18) {
								int n = Year.now().getValue() - 18;
								msg += "Invalid year - Must be under year " + n + " (Must be over 18 years of age).\n";
							}
							JOptionPane.showMessageDialog(null, msg);
						}

					}

					else {
						msg += "Error invalid input\n";
						// if the parse fails, the input is not valid
						inputValid = false;
						if (ppsNum.getText().length() != 8 || ppsNum.getText().isBlank()) {

							msg += "Please enter 8 digit PPS Number!\n";
						}
						if (!(firstName.getText().matches("^[\\p{L} .'-]+$")) && firstName.getText().isBlank()) {
							msg += "Please enter valid first name,\n";
						}
						if (!(secondName.getText().matches("^[\\p{L} .'-]+$")) && secondName.getText().isBlank()) {
							msg += "Please enter valid second name.\n";
						}

						if (dateValid == false) {
							msg += "Please enter valid date\n";

						}
						if (dateValid == true) {

							if (Integer.parseInt(dateOfBirth.getText().substring(0, 2)) > 31
									&& Integer.parseInt(dateOfBirth.getText().substring(0, 2)) < 1) {
								msg += "Invalid day (DD) - Please enter valid day\n";
							}
							if (Integer.parseInt(dateOfBirth.getText().substring(3, 5)) > 12
									&& Integer.parseInt(dateOfBirth.getText().substring(3, 5)) < 1) {
								msg += "Invalid month (MM) - Please enter valid month\n";
							}
							if (Integer.parseInt(dateOfBirth.getText().substring(6, 10)) > Year.now().getValue() - 18) {
								int n = Year.now().getValue() - 18;
								msg += "Invalid year (YYYY) - Must be under year " + n
										+ " (Must be over 18 years of age).\n";
							}

						}

						JOptionPane.showMessageDialog(null, msg);
					}

				}
				;

			}
		});
		//*****************************************
        //Actionlistener to modify customer details
		//******************************************

		modifyCustomerPassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean found = false;

				if (customerList.isEmpty() == true) {
					JOptionPane.showMessageDialog(null, "List is Empty!");

				} else {
					JTextField custID = new JTextField();
					JPasswordField pass = new JPasswordField();

					String msg1 = "Please enter unique customer ID number.";
					String msg2 = "Please enter current customer password in order to update password.";

					int userInput = JOptionPane.showOptionDialog(getContentPane(),
							new Object[] { msg1, custID, msg2, pass }, "Confirm", JOptionPane.OK_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, null, null);
					if (userInput == JOptionPane.OK_OPTION) {

						String s = custID.getText();
						String p = pass.getText();

						for (Customer cust : customerList) {

							if (s.equals(cust.getCustomerID()) && p.equals(cust.getPassword())) {
								found = true;
								JPasswordField newPass = new JPasswordField();

								String msg = "Please enter new password.";

								boolean inputValid = false;
								while (!inputValid) {
									userInput = JOptionPane.showOptionDialog(getContentPane(),
											new Object[] { msg, newPass }, "Confirm", JOptionPane.OK_CANCEL_OPTION,
											JOptionPane.QUESTION_MESSAGE, null, null, null);
									String newP = newPass.getText();
									if (userInput == JOptionPane.CANCEL_OPTION
											&& userInput == JOptionPane.CLOSED_OPTION) {
										JOptionPane.showMessageDialog(null,
												"Operation to update password was cancelled by user!");

										setDefaultCloseOperation(JOptionPane.ABORT);

									}
									if (userInput == JOptionPane.OK_OPTION) {
										if (!newP.isBlank() && (newP.length() >= 8)) {
											inputValid = true;
											cust.setPassword(newP);

											JOptionPane.showMessageDialog(null,
													"Password has been sucessfully updated!");

										} else {
											JOptionPane.showMessageDialog(null,
													"Password must be at least 8 characters long.");

										}

									}

								}
							}

						}

						if (found == false) {
							JOptionPane.showMessageDialog(null,
									"Invalid or incorrect Customer ID/Password was entered!");
						}

					} else {
						JOptionPane.showMessageDialog(null, "Operation to update password was cancelled by user!");
					}
				}
			}
		});
		//***********************************************
		// action listener to remove customer from system
		//***********************************************
		deleteCustomerDetails.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (customerList.isEmpty() == true) {
					JOptionPane.showMessageDialog(null, "List is Empty!");

				} else {
					JTextField custID = new JTextField();

					String msg1 = "Please enter unique customer ID number of customer to be removed from the system.";

					int userInput = JOptionPane.showOptionDialog(getContentPane(), new Object[] { msg1, custID },
							"Confirm", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
					if (userInput == JOptionPane.OK_OPTION) {// validate input

						String s = custID.getText();

						boolean removalComplete = false;
						boolean custAccRemoved = false;
						boolean custAccPresent = false;

						for (Customer cust : customerList) {
							if (s.equals(cust.getCustomerID()) ) {
							if ( map.get(s) == null || map.get(s).isEmpty() == true ) {
								taShowInfo.setText("");
								JOptionPane.showMessageDialog(null, "Customer was sucessfully removed by user!");
								customerList.remove(cust);
								
								removalComplete = true;
								custAccRemoved = true;

								
							}
							else if ((s.equals(cust.getCustomerID()) && map.get(s).isEmpty() == false)) {

								custAccPresent = true;

							}
							{
								
							}
						}
						}

						if (removalComplete == false && custAccRemoved == false && custAccPresent == false) {
							JOptionPane.showMessageDialog(null,
									"Removal unsuccesful - Invalid entry/Customer ID could not be found!");
						} else if (removalComplete == false && custAccRemoved == false && custAccPresent == true) {
							JOptionPane.showMessageDialog(null,
									"Removal unsucessful - There is a customer account is still associated with this user !");

						}

					} else {
						JOptionPane.showMessageDialog(null, "Removal was cancelled by user!");

					}

				}

			}

		});
		//******************************************
        //Actionlistener to create customer account
		//******************************************

		createCustomerAccount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				CustomerAccount ca;
				JTextField custID = new JTextField();

				String msg1 = "Please enter unique customer ID number in order for a linked customer account to be created and added to the system.";

				int userInput = JOptionPane.showOptionDialog(getContentPane(), new Object[] { msg1, custID, },
						"Confirm", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
				if (userInput == JOptionPane.OK_OPTION) {
					boolean creationComplete = false;

					String s = custID.getText();

					for (Customer cust : customerList) {
						if (s.equals(cust.getCustomerID())) {

							double bal = Double.parseDouble(df.format(0.00));

							ca = new CustomerAccount(bal);

							if (map.get(s) == null) {
								map.put(s, new ArrayList<CustomerAccount>());
							}
							map.get(s).add(ca);
							taShowInfo.setText("");

							JOptionPane.showMessageDialog(null,
									"                            A Customer Account has now been created for you.\n"
											+ "This contains an unique account number which has been generated for you and your balance of zero.\n"
											+ "                  Lodge money into your account through the transactions menu!");

							creationComplete = true;
							taShowInfo.append("Newly Created Customer Account Details for Customer ID: " + s + "\n"
									+ ca.toString() + "\n\n");

						}

					}

					if (creationComplete == false) {
						JOptionPane.showMessageDialog(null,
								"Customer creation unsuccesful - Invalid entry/Customer ID could not be found!");
					}

				} else {
					JOptionPane.showMessageDialog(null, "Customer Account creation was cancelled by user!");

				}

			}
		});
		//***************************************************
        //Actionlistener to modify customer account details
		//***************************************************

		modifyCustomerAccountBal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String msg = "";

				if (customerList.isEmpty() == true) {
					JOptionPane.showMessageDialog(null, "List is Empty!");
				} else if (customerList.isEmpty() == false) {
					JTextField custID = new JTextField();
					JPasswordField pass = new JPasswordField();

					String msg1 = "Please enter unique customer ID number of specific customer account balance to be modified.";
					String msg2 = "Please enter password of system administrator.";

					int userInput = JOptionPane.showOptionDialog(getContentPane(),
							new Object[] { msg1, custID, msg2, pass }, "Confirm", JOptionPane.OK_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, null, null);
					if (userInput == JOptionPane.OK_OPTION) {
						boolean found = false;

						String s = custID.getText();
						String p = pass.getText();

						for (Customer cust : customerList) {

							if (s.equals(cust.getCustomerID()) && p.equals(a.getPassword())) {

								found = true;

								if (map.containsKey(cust.getCustomerID()) == false) {

									JOptionPane.showMessageDialog(null,
											"There is no customer account linked to this customer!");

								} else {

									JTextField accNum = new JTextField();

									String msg5 = "Please enter unique customer account number linked to your customer ID to continue.";

									userInput = JOptionPane.showOptionDialog(getContentPane(),
											new Object[] { msg5, accNum }, "Confirm", JOptionPane.OK_CANCEL_OPTION,
											JOptionPane.QUESTION_MESSAGE, null, null, null);
									String accNo = accNum.getText();
									if (userInput == JOptionPane.OK_OPTION) {// validate input
										boolean found2 = false;
										for (Entry<String, ArrayList<CustomerAccount>> entry : map.entrySet()) {
											String key = entry.getKey();
											ArrayList<CustomerAccount> value = entry.getValue();
											for (int i = 0; i < value.size(); i++) {
												if (s.equals(key) && value.get(i).getAccountNumber().equals(accNo)) {
													found2 = true;
													JFormattedTextField amount = new JFormattedTextField(df);
													boolean inputValid = false;

													while (!inputValid) {

														String msg3 = "The account balance is : "
																+ value.get(i).getAccountBalance();
														String msg4 = "\nEnter amount you would like to modify/update account balance to? (Two decimals places)";

														userInput = JOptionPane.showOptionDialog(getContentPane(),
																new Object[] { msg3, msg4, amount }, "Confirm",
																JOptionPane.OK_CANCEL_OPTION,
																JOptionPane.QUESTION_MESSAGE, null, null, null);
														if (userInput == JOptionPane.CANCEL_OPTION
																|| userInput == JOptionPane.CLOSED_OPTION) {

															setDefaultCloseOperation(JOptionPane.ABORT);
															JOptionPane.showMessageDialog(null,
																	"Customer account balance modification was cancelled by user!");

														}
														try {
															double number = Double.parseDouble(amount.getText());
															inputValid = true;
														} catch (NumberFormatException c) {
															// if the parse fails, the input is not valid
															inputValid = false;
															JOptionPane.showMessageDialog(null,
																	"Error invalid input - Please enter number! (Two decimal places)");

														}

													}
													;
													if (userInput == JOptionPane.OK_OPTION) {// validate input
														double am = Double.parseDouble(amount.getText());

														value.get(i).setAccountBalance(am);

														taShowInfo.setText("");
														JOptionPane.showMessageDialog(null,
																"Customer account balance modification was sucessful - New balance will now be displayed!");

														msg += "Customer Account balance for Customer ID - "
																+ cust.getCustomerID() + "\n" + "Account Number - "
																+ value.get(i).getAccountNumber() + "\nNew Balance: "
																+ value.get(i).getAccountBalance() + " \n";

													} else {
														JOptionPane.showMessageDialog(null,
																"Customer account balance modification was cancelled by user!");

													}
												}
											}
										}
										if (found2 == false) {
											JOptionPane.showMessageDialog(null, "Incorrect account number!");

										}

									} else {
										JOptionPane.showMessageDialog(null,
												"Customer account balance modification was cancelled by user!");

									}
								}
							}

						}
						if (found == false) {
							JOptionPane.showMessageDialog(null,
									"Invalid or incorrect Customer ID/Password was entered!");
						}
					} else {
						JOptionPane.showMessageDialog(null,
								"Customer account balance modification was cancelled by user!");

					}
				}
				taShowInfo.append(msg);

			}

		});
		
		//******************************************
        //Actionlistener to delete customer account
		//******************************************
		deleteCustomerAccount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (map.isEmpty() == true) {
					JOptionPane.showMessageDialog(null, "List is Empty!");

				} else {
					JTextField custId = new JTextField();
					JTextField accNum = new JTextField();

					String msg = "Please enter unique customer ID number of the customer account to be removed from the system.";
					String msg1 = "Please enter unique customer account number of the customer account to be removed from the system.";

					int userInput = JOptionPane.showOptionDialog(getContentPane(),
							new Object[] { msg, custId, msg1, accNum }, "Confirm", JOptionPane.OK_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, null, null);
					if (userInput == JOptionPane.OK_OPTION) {// validate input

						String s = accNum.getText();
						String c = custId.getText();

						boolean removalComplete = false;
						boolean balanceZero = false;
						boolean balanceGreaterZero = false;
						boolean custIdExists = false;
						boolean accNumFound = false;

						for (String id : map.keySet()) {
							if (id.equals(c)) {
								custIdExists = true;
								for (int i = 0; i < map.get(id).size(); i++) {

									if (map.get(id).get(i).getAccountNumber().equals(s)) {
										accNumFound = true;
										if (map.get(id).get(i).getAccountBalance() == 0) {

											map.get(id).remove(map.get(id).get(i));
											JOptionPane.showMessageDialog(null,
													"Customer Account was sucessfully removed by user!");
											removalComplete = true;
											balanceZero = true;
											accNumFound = true;

										}

									}

									else if ((map.get(id).get(i).getAccountNumber().equals(s)
											&& !(map.get(id).get(i).getAccountBalance() == 0))) {

										balanceGreaterZero = true;
										accNumFound = true;

									}

								}
							}

						}

						if (custIdExists == false) {
							JOptionPane.showMessageDialog(null,
									"Removal unsuccesful - Invalid entry/Customer ID could not be found!");
						}
						if (custIdExists == true && accNumFound == false) {
							JOptionPane.showMessageDialog(null,
									"Removal unsuccesful - Invalid entry/Account Number could not be found!");
						} else if (removalComplete == false && balanceZero == false && balanceGreaterZero == true
								&& custIdExists == true && accNumFound == true) {
							JOptionPane.showMessageDialog(null,
									"Removal unsucessful - Customer Account has a balance which is greater than zero!");
						}

					} else {
						JOptionPane.showMessageDialog(null, "Removal was cancelled by user!");

					}

				}

			}

		});

		// if display customer records menu is selected
		customerRecords.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				panelNavigation.setVisible(true);

			}
		});
		
		//*************************************
        //Actionlistener to display specific customer account info
		//*************************************

		customerSpecificAccInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String msg = "";

				if (customerList.isEmpty() == true) {
					JOptionPane.showMessageDialog(null, "List is Empty!");
				} else if (customerList.isEmpty() == false) {
					JTextField custID = new JTextField();
					JPasswordField pass = new JPasswordField();

					String msg1 = "Please enter unique customer ID number of specific customer to be displayed from the system.";
					String msg2 = "Please enter password of the specified customer or password of system administrator.";

					int userInput = JOptionPane.showOptionDialog(getContentPane(),
							new Object[] { msg1, custID, msg2, pass }, "Confirm", JOptionPane.OK_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, null, null);
					if (userInput == JOptionPane.OK_OPTION) {
						boolean found = false;

						String s = custID.getText();
						String p = pass.getText();

						for (Customer cust : customerList) {
							if ((s.equals(cust.getCustomerID()))) {
								if (((p.equals(cust.getPassword())) || (p.equals(a.getPassword())))) {

									found = true;

									if (map.containsKey(cust.getCustomerID()) == false) {
										taShowInfo.setText("");
										String first = cust.toString();

										msg += "Specified Customer Record:  " + "\n" + first
												+ "\n*There is no Customer Account Linked to this Customer*\n";

									} else {

										taShowInfo.setText("");
										String first = cust.toString();
										ArrayList<CustomerAccount> value = map.get(cust.getCustomerID());

										/*
										 * Alternative Map.Entry<String, ArrayList<CustomerAccount>> entry =
										 * map.entrySet().iterator().next(); ArrayList<CustomerAccount> value =
										 * entry.getValue();
										 */

										msg += "Specified Customer Record:  " + "\n" + first + "\n" + value.toString()
												.substring(1, value.toString().length() - 1).replaceAll(", ", "")
												+ "\n\n";

									}
								}
							}
						}
						if (found == false) {
							JOptionPane.showMessageDialog(null,
									"Invalid or incorrect Customer ID/Password was entered!");
						}
					} else {
						JOptionPane.showMessageDialog(null,
								"Specific customer record display action was cancelled by user!");

					}
				}
				taShowInfo.append(msg);

			}

		});

		//*************************************
	    //Actionlistener to create withdrawal transaction
		//*************************************

		withdrawal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AccountTransaction at = null;
				String msg = "";
				String transType = "Withdrawal";
				if (customerList.isEmpty() == true) {
					JOptionPane.showMessageDialog(null, "List is Empty!");
				} else if (customerList.isEmpty() == false) {
					JTextField custID = new JTextField();
					JPasswordField pass = new JPasswordField();

					String msg1 = "Please enter unique customer ID number for specific customer withdrawal transaction to be created.";
					String msg2 = "Please enter password customer user password.";

					int userInput = JOptionPane.showOptionDialog(getContentPane(),
							new Object[] { msg1, custID, msg2, pass }, "Confirm", JOptionPane.OK_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, null, null);
					if (userInput == JOptionPane.OK_OPTION) {
						boolean found = false;
						for (Customer cust : customerList) {
							String s = custID.getText();
							String p = pass.getText();
							if (s.equals(cust.getCustomerID()) && p.equals(cust.getPassword())) {
								found = true;

								if ((map.containsKey(cust.getCustomerID()) == true)) {

									JTextField accNum = new JTextField();

									String msg5 = "Please enter unique customer account number linked to your customer ID to continue.";

									userInput = JOptionPane.showOptionDialog(getContentPane(),
											new Object[] { msg5, accNum }, "Confirm", JOptionPane.OK_CANCEL_OPTION,
											JOptionPane.QUESTION_MESSAGE, null, null, null);
									String accNo = accNum.getText();

									if (userInput == JOptionPane.OK_OPTION) {// validate input
										boolean found2 = false;
										for (Entry<String, ArrayList<CustomerAccount>> entry : map.entrySet()) {
											String key = entry.getKey();
											ArrayList<CustomerAccount> value = entry.getValue();
											for (int i = 0; i < value.size(); i++) {
												if (s.equals(key) && value.get(i).getAccountNumber().equals(accNo)) {
													found2 = true;
													JFormattedTextField amount = new JFormattedTextField(df);
													boolean inputValid = false;
													JOptionPane.showMessageDialog(null,
															"Bank charges are a standard €15 for any transaction performed on an account!");

													while (!inputValid) {

														String msg3 = "Your account balance is : "
																+ value.get(i).getAccountBalance();
														String msg4 = "\nEnter amount you would like to withdraw? (Two decimals places)";

														userInput = JOptionPane.showOptionDialog(getContentPane(),
																new Object[] { msg3, msg4, amount }, "Confirm",
																JOptionPane.OK_CANCEL_OPTION,
																JOptionPane.QUESTION_MESSAGE, null, null, null);
														if (userInput == JOptionPane.CANCEL_OPTION
																|| userInput == JOptionPane.CLOSED_OPTION) {

															setDefaultCloseOperation(JOptionPane.ABORT);
															JOptionPane.showMessageDialog(null,
																	"Withdrawal was cancelled by user!");

														}
														try {
															double number = Double.parseDouble(amount.getText());
															inputValid = true;
														} catch (NumberFormatException c) {
															// if the parse fails, the input is not valid
															inputValid = false;
															JOptionPane.showMessageDialog(null,
																	"Error invalid input - Please enter number! (Two decimal places)");

														}

													}
													;

													if (userInput == JOptionPane.OK_OPTION) {
														double am = Double.parseDouble(amount.getText());
														double withdrawalAmount = am + 15.00;
														double result = value.get(i).withdrawal(withdrawalAmount);

														if (result == -1) {
															taShowInfo.setText("");
															JOptionPane.showMessageDialog(null,
																	"Withdrawal unsucessful - This transaction would take your into negative balance therefore it was cancelled!");
														} else {

															taShowInfo.setText("");
															JOptionPane.showMessageDialog(null,
																	"Withdrawal sucessful - Your new customer account balance will be displayed!");
															java.util.Date date = new java.util.Date();

															at = new AccountTransaction(date.toString(), transType,
																	withdrawalAmount, value.get(i).getAccountBalance());
															String aN = value.get(i).getAccountNumber();
															if (map2.get(aN) == null) {
																map2.put(aN, new ArrayList<AccountTransaction>());
															}

															map2.get(aN).add(at);
															msg += "Customer Account balance for Customer ID - "
																	+ cust.getCustomerID() + "\n" + "Account Number - "
																	+ value.get(i).getAccountNumber()
																	+ "\nNew Balance: "
																	+ value.get(i).getAccountBalance() + " \n";

														}

													} else {
														JOptionPane.showMessageDialog(null,
																"Withdrawal was cancelled by user!");

													}

												}
											}
										}
										if (found2 == false) {
											JOptionPane.showMessageDialog(null, "Incorrect account number!");

										}

									} else {
										JOptionPane.showMessageDialog(null, "Withdrawal was cancelled by user!");

									}
								} else {
									JOptionPane.showMessageDialog(null,
											"Customer user does not have a customer account - Please create one to complete a withdrawal!");

								}
							}

						}
						if (found == false) {
							JOptionPane.showMessageDialog(null,
									"Invalid or incorrect Customer ID/Password was entered!");

						}

					} else {
						JOptionPane.showMessageDialog(null, "Withdrawal was cancelled by user!");

					}
				}
				taShowInfo.append(msg);

			}
		});
		//*************************************
	    //Actionlistener to create lodgement transaction
		//*************************************
		lodgement.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AccountTransaction at = null;
				String msg = "";
				String transType = "Lodgement";
				if (customerList.isEmpty() == true) {
					JOptionPane.showMessageDialog(null, "List is Empty!");
				} else if (customerList.isEmpty() == false) {
					JTextField custID = new JTextField();
					JPasswordField pass = new JPasswordField();

					String msg1 = "Please enter unique customer ID number for specific customer lodgement transaction to be created.";
					String msg2 = "Please enter password customer user password.";

					int userInput = JOptionPane.showOptionDialog(getContentPane(),
							new Object[] { msg1, custID, msg2, pass }, "Confirm", JOptionPane.OK_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, null, null);
					if (userInput == JOptionPane.OK_OPTION) {// validate input
						boolean found = false;

						for (Customer cust : customerList) {
							String s = custID.getText();
							String p = pass.getText();
							if (s.equals(cust.getCustomerID()) && p.equals(cust.getPassword())) {
								found = true;

								if ((map.containsKey(cust.getCustomerID()) == true)) {

									JTextField accNum = new JTextField();

									String msg5 = "Please enter unique customer account number linked to your customer ID to continue.";

									userInput = JOptionPane.showOptionDialog(getContentPane(),
											new Object[] { msg5, accNum }, "Confirm", JOptionPane.OK_CANCEL_OPTION,
											JOptionPane.QUESTION_MESSAGE, null, null, null);
									String accNo = accNum.getText();

									if (userInput == JOptionPane.OK_OPTION) {// validate input
										boolean found2 = false;

										for (Entry<String, ArrayList<CustomerAccount>> entry : map.entrySet()) {
											String key = entry.getKey();
											ArrayList<CustomerAccount> value = entry.getValue();
											for (int i = 0; i < value.size(); i++) {
												if (s.equals(key) && value.get(i).getAccountNumber().equals(accNo)) {
													found2 = true;
													// InputVerifier verifier = new MyNumericVerifier();

													JFormattedTextField amount = new JFormattedTextField(df);// validate
																												// amount

													boolean inputvalid = false;
													JOptionPane.showMessageDialog(null,
															"Bank charges are a standard €15 for any transaction performed on an account!");

													while (!inputvalid) {

														String msg3 = "Your account balance is : "
																+ value.get(i).getAccountBalance();
														String msg4 = "\nEnter amount you would like to lodge? (Two decimals places)";

														userInput = JOptionPane.showOptionDialog(getContentPane(),
																new Object[] { msg3, msg4, amount }, "Confirm",
																JOptionPane.OK_CANCEL_OPTION,
																JOptionPane.QUESTION_MESSAGE, null, null, null);
														if (userInput == JOptionPane.CANCEL_OPTION
																|| userInput == JOptionPane.CLOSED_OPTION) {

															setDefaultCloseOperation(JOptionPane.ABORT);
															JOptionPane.showMessageDialog(null,
																	"Lodegment was cancelled by user!");

														}
														try {
															double number = Double.parseDouble(amount.getText());
															inputvalid = true;
														} catch (NumberFormatException c) {
															// if the parse fails, the input is not valid
															inputvalid = false;
															JOptionPane.showMessageDialog(null,
																	"Error invalid input - Please enter number! (Two decimal places)");

														}

													}
													;

													if (userInput == JOptionPane.OK_OPTION) {
														double am = Double.parseDouble((amount.getText()));

														double lodgementAmount = am - 15.00;
														double result = value.get(i).lodgement(lodgementAmount);

														if (result == -1) {
															taShowInfo.setText("");
															JOptionPane.showMessageDialog(null,
																	"Lodgement unsucessful - This transaction would take your into negative balance therefore it was cancelled!");
														} else {

															taShowInfo.setText("");
															JOptionPane.showMessageDialog(null,
																	"Lodgement sucessful - Your new customer account balance will be displayed!");
															java.util.Date date = new java.util.Date();
															at = new AccountTransaction(date.toString(), transType,
																	lodgementAmount, value.get(i).getAccountBalance());
															String aN = value.get(i).getAccountNumber();
															if (map2.get(aN) == null) {
																map2.put(aN, new ArrayList<AccountTransaction>());
															}

															map2.get(aN).add(at);
															msg += "Customer Account balance for Customer ID - "
																	+ cust.getCustomerID() + "\n" + "Account Number - "
																	+ value.get(i).getAccountNumber()
																	+ "\nNew Balance: "
																	+ value.get(i).getAccountBalance() + " \n";

														}

													} else {
														JOptionPane.showMessageDialog(null,
																"Lodgement was cancelled by user!");

													}

												}
											}
										}
										if (found2 == false) {
											JOptionPane.showMessageDialog(null, "Incorrect account number!");

										}

									} else {
										JOptionPane.showMessageDialog(null, "Lodgement was cancelled by user!");

									}
								} else {
									JOptionPane.showMessageDialog(null,
											"Customer user does not have a customer account - Please create one to complete a Lodgement!");

								}
							}
						}
						if (found == false) {
							JOptionPane.showMessageDialog(null,
									"Invalid or incorrect Customer ID/Password was entered!");

						}

					} else {
						JOptionPane.showMessageDialog(null, "Lodgement was cancelled by user!");

					}
				}
				taShowInfo.append(msg);

			}
			
		});
		
		//*************************************
	    //Actionlistener to display e-statement
		//*************************************
		displayEStatement.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AccountTransaction at = null;
				String msg = "";
				if (customerList.isEmpty() == true) {
					JOptionPane.showMessageDialog(null, "List is Empty!");
				} else if (customerList.isEmpty() == false) {
					JTextField custID = new JTextField();
					JPasswordField pass = new JPasswordField();

					String msg1 = "Please enter unique customer ID number for specific customer account E statement to be displayed.";
					String msg2 = "Please enter password of the specified customer or password of system administrator.";

					int userInput = JOptionPane.showOptionDialog(getContentPane(),
							new Object[] { msg1, custID, msg2, pass }, "Confirm", JOptionPane.OK_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, null, null);
					if (userInput == JOptionPane.OK_OPTION) {
						boolean found = false;

						String s = custID.getText();
						String p = pass.getText();

						for (Customer cust : customerList) {
							if ((s.equals(cust.getCustomerID()))) {
								if (((p.equals(cust.getPassword())) || (p.equals(a.getPassword())))) {
									found = true;

									if ((map.containsKey(cust.getCustomerID()) == true)) {

										JTextField accNum = new JTextField();

										String msg5 = "Please enter unique customer account number linked to your customer ID to continue.";

										userInput = JOptionPane.showOptionDialog(getContentPane(),
												new Object[] { msg5, accNum }, "Confirm", JOptionPane.OK_CANCEL_OPTION,
												JOptionPane.QUESTION_MESSAGE, null, null, null);
										String accNo = accNum.getText();

										if (userInput == JOptionPane.OK_OPTION) {// validate input
											boolean found2 = false;

											for (Entry<String, ArrayList<CustomerAccount>> entry : map.entrySet()) {
												String key = entry.getKey();
												ArrayList<CustomerAccount> value = entry.getValue();
												for (int i = 0; i < value.size(); i++) {
													if (s.equals(key)
															&& value.get(i).getAccountNumber().equals(accNo)) {
														found2 = true;
														String aN = value.get(i).getAccountNumber();
														if ((map2.containsKey(aN) == true)) {

															taShowInfo.setText("");
															JOptionPane.showMessageDialog(null,
																	"Now Displaying Customer Account E-Statement!");

//
															
															for (Entry<String, ArrayList<AccountTransaction>> entry2 : map2
																	.entrySet()) {
																String key2 = entry2.getKey();
																ArrayList<AccountTransaction> value2 = entry2
																		.getValue();

																if (key2.equals(aN)) {
																	msg += "Customer Account Balance from "
																			+ value2.get(0).getTransactionDate()
																			+ " (Beginning Period) to Present for Customer ID - "
																			+ cust.getCustomerID() + "\n"
																			+ "Account Number - " + aN
																			+ "\nCustomer Account Balance (Beginning Period): "
																			+ 0 + "\n\n"
																			+ "List of Account Transactions:\n"
																			+ value2.toString()
																					.substring(1,
																							value2.toString().length()
																									- 1)
																					.replaceAll(", ", "")
																			+ "Current Balance: "
																			+ value.get(i).getAccountBalance() + "\n";

																}
															}
														} else {
															JOptionPane.showMessageDialog(null,
																	"Customer user does not have any account transactions - Therefore cannot display an E-Statement!");

														}
													}

												}

											}
											if (found2 == false) {
												JOptionPane.showMessageDialog(null,
														"Operation unsuccesful - Invalid entry/Account number could not be found!");
											}

										} else {
											JOptionPane.showMessageDialog(null, "Lodgement was cancelled by user!");

										}
									} else {
										JOptionPane.showMessageDialog(null,
												"Customer user does not have a customer account - Therefore cannot display an E-Statement!");

									}
								}

							}
						}
						if (found == false) {
							JOptionPane.showMessageDialog(null,
									"Invalid or incorrect Customer ID/Password was entered!");

						}

					} else {
						JOptionPane.showMessageDialog(null, "Action to display E-statement was cancelled by user!");

					}
				}
				taShowInfo.append(msg);

			}
		});
		//*************************************
	    //Actionlistener to clear text area
		//*************************************
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				taShowInfo.setText("");

			}
		});
		
		//*************************************
	    //Actionlistener to end the program
		//*************************************
		btnFinish.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		Dimension center = Toolkit.getDefaultToolkit().getScreenSize();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setSize(1000, 400);
		this.setLocation(center.width / 2 - this.getSize().width / 2, center.height / 2 - this.getSize().height / 2);

		btnFirstCustRecord.addActionListener(this);
		btnLastCustRecord.addActionListener(this);
		btnNextCustRecord.addActionListener(this);
		btnPrevCustRecord.addActionListener(this);

	}
	
	//*************************************
    //Actionlistener to navigate through customer records
	//*************************************
	@Override
	public void actionPerformed(ActionEvent e) {
		String msg = "";
		ArrayList<Customer> list = new ArrayList<Customer>(customerList);

		if (e.getSource() == btnFirstCustRecord) {
			if (customerList.isEmpty() == true) {
				JOptionPane.showMessageDialog(null, "List is Empty!");
			} else if (customerList.isEmpty() == false) {
				if (map.containsKey(customerList.getFirst().getCustomerID()) == false) {
					taShowInfo.setText("");
					String first = customerList.getFirst().toString();

					msg += "First Customer Record:  " + "\n" + first
							+ "\n*There is no Customer Account Linked to this Customer*\n";
					nextCust = 0;
					notClicked = false;
				} else {
					taShowInfo.setText("");
					String first = customerList.getFirst().toString();
					ArrayList<CustomerAccount> value = map.entrySet().stream().findFirst().get().getValue();

					/*
					 * Alternative Map.Entry<String, ArrayList<CustomerAccount>> entry =
					 * map.entrySet().iterator().next(); ArrayList<CustomerAccount> value =
					 * entry.getValue();
					 */

					msg += "First Customer Record:  " + "\n" + first + "\n"
							+ value.toString().substring(1, value.toString().length() - 1).replaceAll(", ", "")
							+ "\n\n";
					nextCust = 0;
					notClicked = false;
				}
			}

		}
		if (e.getSource() == btnLastCustRecord) {
			if (customerList.isEmpty() == true) {
				JOptionPane.showMessageDialog(null, "List is Empty!");

			} else if (customerList.isEmpty() == false) {
				if (map.containsKey(customerList.getLast().getCustomerID()) == false) {
					taShowInfo.setText("");
					String last = customerList.getLast().toString();

					msg += "Last Customer Record:  " + "\n" + last + "\n"
							+ "*There is no Customer Account Linked to this Customer*\n";
					nextCust = list.size() - 1;
					notClicked = false;

				} else {
					taShowInfo.setText("");
					String last = customerList.getLast().toString();
					ArrayList<CustomerAccount> value = map.entrySet().stream().reduce((one, two) -> two).get()
							.getValue();

					msg += "Last Customer Record:  " + "\n" + last + "\n"
							+ value.toString().substring(1, value.toString().length() - 1).replaceAll(", ", "")
							+ "\n\n";
					nextCust = list.size() - 1;
					notClicked = false;
				}
			}
		}

		if (e.getSource() == btnNextCustRecord) {
			if (customerList.isEmpty() == true) {
				JOptionPane.showMessageDialog(null, "List is Empty!");
			} else if (customerList.isEmpty() == false) {

				if (notClicked == true) {
					JOptionPane.showMessageDialog(null,
							"Please begin navigation by selecting either the first or last customer record!");

				} else {

					if ((nextCust + 1) < list.size()) {
						taShowInfo.setText("");
						Customer next = list.get(nextCust + 1);
						msg += "Next Customer Record:\n" + next.toString() + "\n"
								+ "*There is no Customer Account Linked to this Customer*\n";
						nextCust++;
					}

					else {
						JOptionPane.showMessageDialog(null, "End of list!");

					}

				}

			} else {
				JOptionPane.showMessageDialog(null, " End of list!");

			}
		}

		if (e.getSource() == btnPrevCustRecord) {
			if (customerList.isEmpty() == true) {
				JOptionPane.showMessageDialog(null, "List is Empty!");
			} else if (customerList.isEmpty() == false) {
				if (notClicked == true) {

					JOptionPane.showMessageDialog(null,
							"Please begin navigation by selecting either the first or last customer record!");

				}

				else {
					if ((nextCust - 1) >= 0) {
						taShowInfo.setText("");
						Customer next = list.get(nextCust - 1);
						msg += "Previous Customer Record:\n" + next.toString() + "\n"
								+ "*There is no Customer Account Linked to this Customer*\n";
						nextCust--;
					} else {
						JOptionPane.showMessageDialog(null, "Start of list!");

					}

				}
			}
		}

		taShowInfo.append(msg);
	}

	//*************************************
	//Run main program
	//*************************************

	public static void main(String[] args) {
		BankGUI ghj = new BankGUI();
	}

}
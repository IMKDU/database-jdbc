package com.assignment.wk12;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * =========================
 * wk12  
 * CSE3010 Database
 * =========================
 * 
 * -----------
 * Description
 * -----------
 * 
 * This is the skeleton code for the wk12 assignment.
 * 
 */
public class wk12 implements ActionListener {

    private String url;
    private String user;
    private String password;
	
	private JFrame frame;
	private JPanel panel;
	private JLabel idLabel;
	private JLabel pwdLabel;
	private JTextField idInput;
	private JPasswordField pwdInput;
	private JButton loginButton;
	private JButton getTablesButton;
	private JLabel statusLabel;
	private JTextField statusMessageLabel;
	
	private Connection conn;
	
	public wk12() {
		
	}
	
	/*
	 * Task 1.
	 * Implement this method to properly initialize
	 * GUI components.
	 */
	public void task1() {
		url = "jdbc:postgresql://localhost/postgres";
		user = "postgres";
		password = "1234";

		// fill in here
		frame = new JFrame("Week12 Assignment");
		panel = new JPanel(new GridBagLayout());

		idLabel = new JLabel("ID");
		pwdLabel = new JLabel("Password");
		statusLabel = new JLabel("Status");

		idInput = new JTextField(20);
		pwdInput = new JPasswordField(20);
		statusMessageLabel = new JTextField(30);
		statusMessageLabel.setEditable(false);

		loginButton = new JButton("Login");
		getTablesButton = new JButton("Tables");

		loginButton.addActionListener(this);
		getTablesButton.addActionListener(this);
		// end
	}
	
	/*
	 * Task 2.
	 * Implement this method to properly display
	 * GUI components.
	 */
	public void task2() {
		// fill in here
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// Row 0: ID Label and Input
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.0;
		panel.add(idLabel, gbc);

		gbc.gridx = 1;
		gbc.weightx = 1.0;
		panel.add(idInput, gbc);

		// Row 1: Password Label and Input
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0.0;
		panel.add(pwdLabel, gbc);

		gbc.gridx = 1;
		gbc.weightx = 1.0;
		panel.add(pwdInput, gbc);

		// Row 2: Login and Tables Buttons
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 0.5;
		panel.add(loginButton, gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.5;
		panel.add(getTablesButton, gbc);

		// Row 3: Status Label
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 2;
		gbc.weightx = 1.0;
		panel.add(statusLabel, gbc);

		// Row 4: Status Message
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth = 2;
		panel.add(statusMessageLabel, gbc);

		frame.add(panel);
		frame.setSize(450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		// end
	}
	
	/*
	 * Task 3.
	 * Implement this method to connect to the database
	 * with the input user ID and password.
	 */
	public Connection task3() {
		String statusMessageString = "Connected";
		// fill in here
		user = idInput.getText();
		password = new String(pwdInput.getPassword());

		Connection conn = this.connect();
		if (conn != null) {
			statusMessageLabel.setText(statusMessageString);
		} else {
			statusMessageLabel.setText("Connection Failed");
		}
		// end
		return conn;
	}
	
	/*
	 * Task 4.
	 * Implement this method to retrieve the list of tables
	 * and print out on Console.
	 */
	public void task4() {
		System.out.println("=====");
		System.out.println("Task4");
		System.out.println("=====");
		String statusMessageString = "Available Tables are printed out on Console";
		try {
			// fill in here
			java.sql.DatabaseMetaData metaData = conn.getMetaData();
			java.sql.ResultSet tables = metaData.getTables(null, null, "%", new String[]{"TABLE"});

			while (tables.next()) {
				String tableName = tables.getString("TABLE_NAME");
				System.out.println(tableName);
			}

			tables.close();
			statusMessageLabel.setText(statusMessageString);
			// end
		} catch (NullPointerException ne) {
			ne.printStackTrace();
			statusMessageLabel.setText("NullPointerException: Did you Login?");
		} catch (SQLException se) {
			se.printStackTrace();
			statusMessageLabel.setText("SQLException: " + se.getMessage());
		}
		System.out.println("");
		System.out.println("");
	}
    
    /**
     * Connect to the PostgreSQL database
     *
     * @return a Connection object
     */
    public Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }
    
    /**
     * Disconnect from the PostgreSQL database
     *
     */
    public void disconnect(Connection conn) {
        try {
        	conn.close();
            System.out.println("Connection is closed successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    @Override
	public void actionPerformed(ActionEvent e) {
    	if (e.getSource() == loginButton) {
    		conn = task3();
    	} else if (e.getSource() == getTablesButton) {
    		task4();
    	}
	}
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        wk12 sub = new wk12();
        sub.task1();
        sub.task2();

    }

	
	
}

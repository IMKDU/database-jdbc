package com.assignment.wk12;

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
		frame = new JFrame("Database Login");
		panel = new JPanel();

		idLabel = new JLabel("User ID:");
		pwdLabel = new JLabel("Password:");
		statusLabel = new JLabel("Status:");

		idInput = new JTextField(15);
		pwdInput = new JPasswordField(15);
		statusMessageLabel = new JTextField(30);
		statusMessageLabel.setEditable(false);

		loginButton = new JButton("Login");
		getTablesButton = new JButton("Get Tables");

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
		panel.add(idLabel);
		panel.add(idInput);
		panel.add(pwdLabel);
		panel.add(pwdInput);
		panel.add(loginButton);
		panel.add(getTablesButton);
		panel.add(statusLabel);
		panel.add(statusMessageLabel);

		frame.add(panel);
		frame.setSize(400, 250);
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

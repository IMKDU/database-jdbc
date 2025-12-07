package com.assignment.wk14b;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;


/**
 * =========================
 * wk14b Assignment
 * CSE3010 Database
 * =========================
 * 
 * -----------
 * Description
 * -----------
 * 
 * This is the skeleton code for the wk14b assignment.
 * 
 */
public class wk14b implements ActionListener {
	
	private String url;
    private String user;
    private String password;
	private Connection conn;
	
	private JFrame frame;
	private JPanel panel;
	private JLabel idLabel;
	private JLabel pwdLabel;
	private JTextField idInput;
	private JPasswordField pwdInput;
	private JButton loginButton;
	
	private JTextArea check_area;
	private JComboBox<String> check_box;

	private JTextField courseIdInput;
	private JTextField sectionIdInput;
	private JTextField semesterInput;
	private JTextField yearInput;
	private JButton registerButton;

	String[] queries = {"SELECT * FROM course",
			"SELECT * FROM section", "SELECT * FROM prereq"};
	
	/*
	 * Replace url, user and password, if preferred.
	 */
	public wk14b() {
		url = "jdbc:postgresql://localhost/postgres";
		user = "postgres";
		password = "1234";

		frame = new JFrame();
		panel = new JPanel();
		idLabel = new JLabel("ID");
		pwdLabel = new JLabel("Password");
		idInput = new JTextField(user);
		pwdInput = new JPasswordField(password);
		loginButton = new JButton("Login");
		
		panel.setLayout(null);
		
		// Specify location of Components
		idLabel.setBounds(20, 10, 60, 30);
		pwdLabel.setBounds(20, 50, 60, 30);
		idInput.setBounds(100, 10, 80, 30);
		pwdInput.setBounds(100, 50, 80, 30);
		loginButton.setBounds(200, 25,  80, 35);
		
		// Add an ActionListener to the Login Button
		loginButton.addActionListener(this);
		
		// Add Components to Panel
		panel.add(idLabel);
		panel.add(pwdLabel);
		panel.add(idInput);
		panel.add(pwdInput);
		panel.add(loginButton);
		// Add Panel to Frame
		frame.add(panel);
		
		frame.setTitle("Week11Lab JDBC Lab 4");					// name on the top of the frame
		frame.setSize(320, 130);								// size of the frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	// call System.exit() on closing
		frame.setVisible(true);									// display the frame on the screen
	}
	
	/*
	 * This method pops up Course Info window
	 * replacing Login window.
	 * Course Info window must be shown after 
	 * the user successfully log in (and the 
	 * connection with the database server is
	 * successfully established).
	 * 
	 */
	private void courseInfo() {
		check_area = new JTextArea();
		check_box = new JComboBox<String>();

		frame.setVisible(false);

		frame = new JFrame();
		panel = new JPanel();

		panel.setFont(new Font(null, 1, 12));
		panel.setBorder(new TitledBorder("Inquiry"));
		panel.setBounds(380, 80, 490, 280);
		panel.setLayout(null);

		check_box.addItem("Course");
		check_box.addItem("Section");
		check_box.addItem("Prereq");

		// Create registration fields
		JLabel courseIdLabel = new JLabel("CourseID");
		JLabel sectionIdLabel = new JLabel("SectionID");
		JLabel semesterLabel = new JLabel("Semester");
		JLabel yearLabel = new JLabel("Year");

		courseIdInput = new JTextField();
		sectionIdInput = new JTextField();
		semesterInput = new JTextField();
		yearInput = new JTextField();
		registerButton = new JButton("Register");

		check_area.setBorder(new LineBorder(Color.gray, 2));
		check_area.setEditable(false);

		JScrollPane scroll = new JScrollPane();
		scroll.setViewportView(check_area);

		// Position components
		check_box.setBounds(20, 40, 70, 30);

		courseIdLabel.setBounds(110, 30, 60, 20);
		courseIdInput.setBounds(110, 50, 70, 25);

		sectionIdLabel.setBounds(190, 30, 60, 20);
		sectionIdInput.setBounds(190, 50, 70, 25);

		semesterLabel.setBounds(270, 30, 60, 20);
		semesterInput.setBounds(270, 50, 70, 25);

		yearLabel.setBounds(350, 30, 40, 20);
		yearInput.setBounds(350, 50, 50, 25);

		registerButton.setBounds(410, 45, 80, 30);

		scroll.setBounds(10, 80, 460, 270);

		check_box.addActionListener(this);
		registerButton.addActionListener(this);

		panel.add(check_box);
		panel.add(courseIdLabel);
		panel.add(courseIdInput);
		panel.add(sectionIdLabel);
		panel.add(sectionIdInput);
		panel.add(semesterLabel);
		panel.add(semesterInput);
		panel.add(yearLabel);
		panel.add(yearInput);
		panel.add(registerButton);
		panel.add(scroll);

		frame.add(panel);

		frame.setTitle("Course Info");
		frame.setSize(500, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	/*
	 * Modify this method to pop up Course Info window
	 * replacing the Login window. 
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == loginButton) {
			// Get the text entered in JTextField and JPasswordField
			user = idInput.getText();
			password = new String(pwdInput.getPassword());

			conn = this.connect();
			if (conn != null) {
				courseInfo();
			}
		} else if (e.getSource() == check_box) {
			try {
				showTable();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		} else if (e.getSource() == registerButton) {
			try {
				registerCourse();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}
	
	private String getQuery(String tn) {
		String retQuery = null;
		if (tn.equalsIgnoreCase("course")) {
			retQuery = queries[0];
		} else if (tn.equalsIgnoreCase("section")) {
			retQuery = queries[1];
		} else if (tn.equalsIgnoreCase("prereq")) {
			retQuery = queries[2];
		}
		return retQuery;
	}
	
	/*
	 * Implement this method.
	 * The table content must be displayed on
	 * the Course Info window's JTextArea.
	 * 
	 * First row of the displayed table content
	 * must be the name of columns separated by
	 * a tab ('\t').
	 * 
	 * Second row of the displayed table content
	 * must be dash (-) as many times as the sum
	 * of length of column name string times 3.
	 * (# of dash = length of all column names * 3)
	 * 
	 * Then, each row of the table should be 
	 * displayed line by line.
	 * 
	 * The display should be as follow:
	 * <Column names separated by a tab>
	 * ----------------------------------- 
	 * <The 1st row of the chosen table>
	 * <The 2nd row of the chosen table>
	 * ...
	 * <The last row of the chosen table>
	 *  
	 */
	private void showTable() throws SQLException {
		
		String displayMsg = "";
		PreparedStatement pstmt = conn.prepareStatement(
				getQuery((String) check_box.getSelectedItem()));
		ResultSet result = pstmt.executeQuery();
		ResultSetMetaData rsmd = result.getMetaData();
		int numCols = rsmd.getColumnCount();
		int tableCharWidth = 0;
		for (int i = 1; i<= numCols; i++) {
			//System.out.print(rsmd.getColumnName(i) + "\t");
			displayMsg += rsmd.getColumnName(i) + "\t";
			tableCharWidth += rsmd.getColumnName(i).length();
		}
		//System.out.println();
		displayMsg += "\n";
		for (int i = 1; i <= tableCharWidth * 3; i++) {
			//System.out.print("-");
			displayMsg += "-";
		}
		//System.out.println();
		displayMsg += "\n";
		while (result.next()) {
			for (int i = 1; i <= numCols; i++) {
				//System.out.print(result.getString(i) + "\t");
				displayMsg += result.getString(i) + "\t";
			}
			//System.out.println();
			displayMsg += "\n";
		}

		check_area.setText(displayMsg);

	}

	/*
	 * Task 1: Implement exception handling for invalid course registration
	 *
	 * This method checks if the course section exists in the database
	 * before registering the student. If the course section does not exist,
	 * it shows an error message.
	 */
	private void registerCourse() throws SQLException {
		String courseId = courseIdInput.getText().trim();
		String sectionId = sectionIdInput.getText().trim();
		String semester = semesterInput.getText().trim();
		String year = yearInput.getText().trim();

		// Check if the section exists in the database
		String checkQuery = "SELECT * FROM section WHERE course_id = ? AND sec_id = ? AND semester = ? AND year = ?";
		PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
		checkStmt.setString(1, courseId);
		checkStmt.setString(2, sectionId);
		checkStmt.setString(3, semester);
		checkStmt.setInt(4, Integer.parseInt(year));

		ResultSet rs = checkStmt.executeQuery();

		if (!rs.next()) {
			// Course section does not exist - show error message
			String errorMsg = user + ": You cannot register in the course " + courseId +
					" section " + sectionId + " for the " + semester +
					" semester in " + year + " because such a course is not offered!";
			JOptionPane.showMessageDialog(frame, errorMsg, "Message", JOptionPane.INFORMATION_MESSAGE);
		} else {
			// Course section exists - proceed with registration
			// Note: We don't need to handle SQLException for duplicate registration as per requirements
			String insertQuery = "INSERT INTO takes (ID, course_id, sec_id, semester, year) VALUES (?, ?, ?, ?, ?)";
			PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
			insertStmt.setString(1, user);  // student ID
			insertStmt.setString(2, courseId);
			insertStmt.setString(3, sectionId);
			insertStmt.setString(4, semester);
			insertStmt.setInt(5, Integer.parseInt(year));

			insertStmt.executeUpdate();
			insertStmt.close();

			JOptionPane.showMessageDialog(frame, "Successfully registered for the course!", "Success", JOptionPane.INFORMATION_MESSAGE);
		}

		rs.close();
		checkStmt.close();
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
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new wk14b();
        
    }

}

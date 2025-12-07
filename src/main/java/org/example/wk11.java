package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Scanner;

public class wk11 {

    // fill in here
    private final String url = "jdbc:postgresql://localhost:5432/postgres";
    private final String user = "postgres";
    private final String password = "202203";
    // end

    /**
     * Connect to the PostgreSQL database
     * @return a Connection object
     */
    public Connection task1() {
        System.out.println("=====");
        System.out.println("Task1");
        System.out.println("=====");

        Connection conn = null;
        try {
            Properties props = new Properties();
            // fill in here
            props.setProperty("user", user);
            props.setProperty("password", password);
            // end

            conn = DriverManager.getConnection(url, props);
            System.out.println("Connected to the PostgreSQL server successfully.");

            // ⭐ Task1에서 연결 테스트 후 바로 종료
            conn.close();
            System.out.println("Connection is closed successfully.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("");
        System.out.println("");

        return conn;  // null 또는 닫힌 연결 반환
    }

    /**
     * Task2~4를 위한 새로운 연결 (출력 없음)
     */
    private Connection getConnection() {
        Connection conn = null;
        try {
            Properties props = new Properties();
            props.setProperty("user", user);
            props.setProperty("password", password);
            conn = DriverManager.getConnection(url, props);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void disconnect(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Connection is closed successfully.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void task2(Connection conn) {
        System.out.println("=====");
        System.out.println("Task2");
        System.out.println("=====");

        try {
            Statement st = null;
            String sql = null;
            // fill in here
            st = conn.createStatement();
            sql = "SELECT id, name, dept_name, salary " +
                    "FROM instructor " +
                    "WHERE salary = (SELECT MAX(salary) FROM instructor)";
            // end

            ResultSet rs = st.executeQuery(sql);
            System.out.println("info of the instructor who is receiving the highest salary:");
            System.out.println(" ID " + "\t"
                    + "  name   " + "\t"
                    + "dept_name" + "\t"
                    + "salary" + "\t");
            System.out.println("----" + "\t"
                    + "--------" + "\t"
                    + "---------" + "\t"
                    + "------");
            displayInstructor(rs);
            rs.close();
            st.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("");
        System.out.println("");
    }

    public void task3(Connection conn) {
        System.out.println("=====");
        System.out.println("Task3");
        System.out.println("=====");

        try {
            Statement st;
            String sql;
            ResultSet rs = null;
            st = conn.createStatement();
            // fill in here
            sql = "SELECT id, name, dept_name, salary " +
                    "FROM instructor " +
                    "WHERE dept_name = (" +
                    "    SELECT dept_name " +
                    "    FROM instructor " +
                    "    WHERE salary = (SELECT MAX(salary) FROM instructor)" +
                    ")";
            rs = st.executeQuery(sql);
            // end

            System.out.println("info of instructors who are in the same department as the instructor receiving the highest salary:");
            System.out.println(" ID " + "\t"
                    + "  name   " + "\t"
                    + "dept_name" + "\t"
                    + "salary" + "\t");
            System.out.println("----" + "\t"
                    + "--------" + "\t"
                    + "---------" + "\t"
                    + "------");
            displayInstructor(rs);
            rs.close();
            st.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("");
        System.out.println("");
    }

    public void task4(Connection conn) {
        System.out.println("=====");
        System.out.println("Task4");
        System.out.println("=====");

        String deptName = readDeptNameFromStdin();

        try {
            PreparedStatement pstmt = null;
            ResultSet rs;
            String SQL = "SELECT id, name, dept_name, salary "
                    + "FROM instructor "
                    + "WHERE dept_name = ?";
            // fill in here
            pstmt = conn.prepareStatement(SQL);
            pstmt.setString(1, deptName);
            // end

            rs = pstmt.executeQuery();
            System.out.println("info of instructors in the department chosen by the user:");
            System.out.println(" ID " + "\t"
                    + "  name   " + "\t"
                    + "dept_name" + "\t"
                    + "salary" + "\t");
            System.out.println("----" + "\t"
                    + "--------" + "\t"
                    + "---------" + "\t"
                    + "------");
            displayInstructor(rs);
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("");
        System.out.println("");
    }

    private String readDeptNameFromStdin() {
        String deptName;
        Scanner in = new Scanner(System.in);
        System.out.println("Enter the department name you want to get info of instructors from:");
        deptName = in.nextLine();
        System.out.println("The department name entered by the user is: " + deptName);
        in.close();
        return deptName;
    }

    private void displayInstructor(ResultSet rs) throws SQLException {
        while (rs.next()) {
            System.out.println(rs.getString(1) + "\t"
                    + rs.getString(2) + "\t"
                    + rs.getString("dept_name") + "\t\t"
                    + rs.getBigDecimal("salary"));
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        wk11 sol = new wk11();

        // Task1: 연결 테스트 (연결 후 바로 종료)
        sol.task1();

        // Task2~4: 새로운 연결 사용
        Connection conn = sol.getConnection();
        sol.task2(conn);
        sol.task3(conn);
        sol.task4(conn);
        sol.disconnect(conn);
    }
}
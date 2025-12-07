package com.postgresqltutorial;

import java.sql.*;
import java.util.Properties;

/**
 * JDBC 실습 1 - App.java
 * Statement와 PreparedStatement 사용법 학습
 */
public class App {

    // 데이터베이스 연결 정보
    private final String url = "jdbc:postgresql://localhost:5432/postgres";
    private final String user = "postgres";
    private final String password = "202203";

    /**
     * PostgreSQL 데이터베이스에 연결
     */
    public Connection connect() {
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");

            Properties props = new Properties();
            props.setProperty("user", user);
            props.setProperty("password", password);

            conn = DriverManager.getConnection(url, props);
            System.out.println("Connected to the PostgreSQL server successfully.");

        } catch (ClassNotFoundException e) {
            System.err.println("PostgreSQL JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * 연결 종료
     */
    public void disconnect(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Statement 예제 1: 교수 수 세기
     */
    public void statementQueryEx1(Connection conn) {
        System.out.println("========================");
        System.out.println("statementQueryEx1");
        System.out.println("========================");

        if (conn == null) {
            System.err.println("Connection is null!");
            return;
        }

        try {
            Statement st = conn.createStatement();
            String sql = "SELECT count(*) FROM instructor";
            ResultSet rs = st.executeQuery(sql);

            rs.next();
            int count = rs.getInt(1);
            System.out.println("number of instructor=" + count);

            rs.close();
            st.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("");
        System.out.println("");
    }

    /**
     * Statement 예제 2: 모든 교수 정보 출력
     */
    public void statementQueryEx2(Connection conn) {
        System.out.println("========================");
        System.out.println("statementQueryEx2");
        System.out.println("========================");

        if (conn == null) {
            System.err.println("Connection is null!");
            return;
        }

        try {
            Statement st = conn.createStatement();
            String sql = "SELECT * FROM instructor";
            ResultSet rs = st.executeQuery(sql);

            displayInstructor(rs);

            rs.close();
            st.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("");
        System.out.println("");
    }

    /**
     * PreparedStatement 예제: ID=22222인 교수 찾기
     */
    public void prepareStatementQueryEx1(Connection conn) {
        System.out.println("========================");
        System.out.println("prepareStatementQueryEx1");
        System.out.println("========================");

        if (conn == null) {
            System.err.println("Connection is null!");
            return;
        }

        String SQL = "SELECT id, name, dept_name, salary "
                + "FROM instructor "
                + "WHERE id = ?";

        try {
            PreparedStatement pstmt = conn.prepareStatement(SQL);
            pstmt.setString(1, "22222");
            ResultSet rs = pstmt.executeQuery();

            displayInstructor(rs);

            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("");
        System.out.println("");
    }

    /**
     * 교수 정보 출력 (이미지와 동일한 형식)
     */
    private void displayInstructor(ResultSet rs) throws SQLException {
        while (rs.next()) {
            System.out.println(
                    rs.getString(1) + "\t" +           // ID
                            rs.getString(2) + "\t" +           // name
                            rs.getString("dept_name") + " " +  // dept_name
                            rs.getBigDecimal("salary")         // salary
            );
        }
    }

    /**
     * Main 메소드
     */
    public static void main(String[] args) {
        App app = new App();
        Connection conn = app.connect();

        if (conn == null) {
            return;
        }

        app.statementQueryEx1(conn);
        app.statementQueryEx2(conn);
        app.prepareStatementQueryEx1(conn);

        app.disconnect(conn);
    }
}
package crud;

import conn.DBConnection;
import java.sql.*;

public class CRUDEmployee {

    private void title(String text) {
        System.out.println();
        System.out.println("===== " + text + " =====");
    }

    // CREATE TABLE (여러 번 실행해도 OK)
    public void createTable() {
        String sql =
                "CREATE TABLE IF NOT EXISTS Employee (" +
                        "  EmpNo INT AUTO_INCREMENT PRIMARY KEY," +
                        "  EmpName VARCHAR(30) NOT NULL," +
                        "  Dept VARCHAR(20) NOT NULL," +
                        "  HireDate DATE NOT NULL," +
                        "  Salary INT NOT NULL," +
                        "  CONSTRAINT uq_emp_empname UNIQUE (EmpName)," +
                        "  CONSTRAINT chk_emp_salary CHECK (Salary >= 2000000)" +
                        ")";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("TRUNCATE TABLE Employee");

            title("CREATE + TRUNCATE");
            System.out.println("Employee 테이블 준비 완료 (데이터 초기화됨).");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // INSERT (중복이면 무시)
    public int insert(String empName, String dept, String hireDate, int salary) {
        String sql = "INSERT IGNORE INTO Employee (EmpName, Dept, HireDate, Salary) VALUES (?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, empName);
            ps.setString(2, dept);
            ps.setString(3, hireDate);  // "YYYY-MM-DD"
            ps.setInt(4, salary);
            int rows = ps.executeUpdate();
            title("INSERT");
            if (rows == 1) {
                System.out.printf("추가됨: %s | %s | %s | %,d%n", empName, dept, hireDate, salary);
            } else {
                System.out.printf("중복-무시됨(추가 0건): %s%n", empName);
            }
            return rows;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // SELECT: 특정 부서 (사번/이름/급여)
    public void selectByDept(String dept) {
        String sql = "SELECT EmpNo, EmpName, Salary FROM Employee WHERE Dept = ?";
        title("SELECT - 부서별 ( " + dept + " )");
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dept);
            try (ResultSet rs = ps.executeQuery()) {
                int n = 0;
                System.out.println("EmpNo | EmpName | Salary");
                while (rs.next()) {
                    n++;
                    System.out.printf("%d | %s | %,d%n",
                            rs.getInt("EmpNo"),
                            rs.getString("EmpName"),
                            rs.getInt("Salary"));
                }
                if (n == 0) System.out.println("(0건)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // SELECT: 최소 급여 이상 (이름/부서)
    public void selectByMinSalary(int minSalary) {
        String sql = "SELECT EmpName, Dept FROM Employee WHERE Salary >= ?";
        title("SELECT - 급여 ≥ " + String.format("%,d", minSalary));
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, minSalary);
            try (ResultSet rs = ps.executeQuery()) {
                int n = 0;
                System.out.println("EmpName | Dept");
                while (rs.next()) {
                    n++;
                    System.out.printf("%s | %s%n",
                            rs.getString("EmpName"),
                            rs.getString("Dept"));
                }
                if (n == 0) System.out.println("(0건)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // UPDATE: 이름으로 급여 변경
    public int updateSalaryByName(String empName, int newSalary) {
        String sql = "UPDATE Employee SET Salary = ? WHERE EmpName = ?";
        title("UPDATE - 급여변경 (" + empName + " → " + String.format("%,d", newSalary) + ")");
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newSalary);
            ps.setString(2, empName);
            int rows = ps.executeUpdate();
            System.out.println("영향받은 행: " + rows);
            return rows;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // DELETE: 사번으로 삭제
    public int deleteByEmpNo(int empNo) {
        String sql = "DELETE FROM Employee WHERE EmpNo = ?";
        title("DELETE - EmpNo = " + empNo);
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, empNo);
            int rows = ps.executeUpdate();
            System.out.println("영향받은 행: " + rows);
            return rows;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}

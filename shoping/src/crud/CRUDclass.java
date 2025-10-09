package crud;

import dbcon.DBConnection;

import java.sql.*;

public class CRUDclass {


    public void createTable() {

        String dropSale = "DROP TABLE Sale";
        String dropshopMember = "DROP TABLE ShopMember";

        String createShopMember = "CREATE TABLE ShopMember (" +
                "  CustNo INT PRIMARY KEY AUTO_INCREMENT," +
                "  CustName VARCHAR(30) NOT NULL," +
                "  Phone VARCHAR(13) UNIQUE," +
                "  Address VARCHAR(50)," +
                "  JoinDate DATE NOT NULL," +
                "  Grade VARCHAR(1) NOT NULL CHECK(Grade IN('A','B','C','D'))," +
                "  City VARCHAR(2)" +
                ")";

        String createSale = "CREATE TABLE Sale (" +
                "  SaleNo INT PRIMARY KEY AUTO_INCREMENT," +
                "  CustNo INT NOT NULL," +
                "  PCost INT," +
                "  Amount INT," +
                "  Price INT," +
                "  PCode VARCHAR(3)," +
                "  CONSTRAINT fk_sale_member FOREIGN KEY (CustNo) REFERENCES ShopMember(CustNo)" +
                ")";

        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement()) {

            stmt.execute(dropSale);
            stmt.execute(dropshopMember);

            stmt.execute(createShopMember);
            stmt.execute(createSale);

            System.out.println("Drop, Create 작업완료 ");
        } catch (SQLException e) {
            System.out.println("에러임" + e.getMessage());
        }
    }


    public void insertMem() {
        String sql = "INSERT INTO ShopMember " +
                "(CustName, Phone, Address, JoinDate, Grade, City) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {


            ps.setString(1, "홍길동");
            ps.setString(2, "010-1234-5678");
            ps.setString(3, "서울시 강남구");
            ps.setDate(4, Date.valueOf("2020-01-01"));
            ps.setString(5, "A");
            ps.setString(6, "01");
            ps.addBatch();

            ps.setString(1, "이순신");
            ps.setString(2, "010-2222-3333");
            ps.setString(3, "부산시 해운대구");
            ps.setDate(4, Date.valueOf("2021-03-15"));
            ps.setString(5, "B");
            ps.setString(6, "02");
            ps.addBatch();

            ps.setString(1, "강감찬");
            ps.setString(2, "010-7777-8888");
            ps.setString(3, "대구시 달서구");
            ps.setDate(4, Date.valueOf("2019-05-20"));
            ps.setString(5, "C");
            ps.setString(6, "03");
            ps.addBatch();

            ps.setString(1, "을지문덕");
            ps.setString(2, "010-2222-4555");
            ps.setString(3, "광주시 중앙구");
            ps.setDate(4, Date.valueOf("2023-03-30"));
            ps.setString(5, "D");
            ps.setString(6, "04");
            ps.addBatch();

            ps.executeBatch();

        } catch (SQLException e) {
            System.out.println("회원등록오류" + e.getMessage());
        }
    }

    // (2-2-1) A등급 회원 조회
    public void queryAgrade() {
        String sql = "SELECT CustName, Phone, JoinDate FROM ShopMember WHERE Grade = 'A'";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            System.out.println("A등급회원");
            while (rs.next()) {
                System.out.printf("- %s | %s | %s%n",
                        rs.getString("CustName"),
                        rs.getString("Phone"),
                        rs.getString("JoinDate"));
            }

        } catch (SQLException e) {
            System.out.println("A등급 조회 오류" + e.getMessage());
        }
    }

    // (2-2-2) 2020년 이후 가입 회원 조회
    public void queryJoin2020() {
        String sql = "SELECT * FROM ShopMember WHERE YEAR(JoinDate) > 2020";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("2020년 이후 가입자");
            while (rs.next()) {
                System.out.printf("- #%d %s | %s | %s | %s | %s%n",
                        rs.getInt("CustNo"),
                        rs.getString("CustName"),
                        rs.getString("Phone"),
                        rs.getString("JoinDate"),
                        rs.getString("Grade"),
                        rs.getString("City")
                );
            }
        } catch (SQLException e) {
            System.out.println("가입자 조회오류" + e.getMessage());
        }
    }

    //(2-3) 판매 등록
    public void insertSale() {
        String sql = "INSERT INTO Sale " +
                "(CustNo, PCost, Amount, Price, PCode) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, 1);
            ps.setInt(2, 1000);
            ps.setInt(3, 10);
            ps.setInt(4, 10000);
            ps.setString(5, "P01");
            ps.addBatch();
            ps.setInt(1, 2);
            ps.setInt(2, 2000);
            ps.setInt(3, 5);
            ps.setInt(4, 10000);
            ps.setString(5, "P02");
            ps.addBatch();
            ps.setInt(1, 3);
            ps.setInt(2, 1500);
            ps.setInt(3, 7);
            ps.setInt(4, 10500);
            ps.setString(5, "P03");
            ps.addBatch();
            ps.setInt(1, 2);
            ps.setInt(2, 1500);
            ps.setInt(3, 7);
            ps.setInt(4, 10500);
            ps.setString(5, "P03");
            ps.addBatch();
            ps.setInt(1, 4);
            ps.setInt(2, 1600);
            ps.setInt(3, 6);
            ps.setInt(4, 20500);
            ps.setString(5, "P04");
            ps.addBatch();

            ps.executeBatch();

            System.out.println("판매등록완료");

        } catch (SQLException e) {
            System.out.println("판매등록오류" + e.getMessage());
        }
    }

    //(2-4) 판매 조회(총 구매금액 상위2명)
    public void queryTop() {
        String sql = "SELECT m.CustNo, m.CustName, SUM(s.Price) AS TotalPrice " +
                "FROM Sale s " +
                "JOIN ShopMember m ON m.CustNo = s.CustNo " +
                "GROUP BY m.CustNo, m.CustName " +
                "ORDER BY TotalPrice DESC " +
                "LIMIT 2";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            System.out.println("판매상위2명");
            while (rs.next()) {
                System.out.printf("-#%d %s | 총액: %,d%n",
                        rs.getInt("CustNo"),
                        rs.getString("CusTName"),
                        rs.getInt("TotalPrice")
                );

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // (2-5-1) 이순신 등급 수정 후 확인 조회
    public void updateleeA() {
        String updateSql = "UPDATE ShopMember SET Grade = 'A' WHERE CustName = '이순신'";
        String selectSql = "SELECT CustName, Phone, JoinDate FROM ShopMember WHERE CustName = '이순신'";

        try (Connection con = DBConnection.getConnection()) {
            // 1) UPDATE
            try (PreparedStatement ps = con.prepareStatement(updateSql)) {
                int rows = ps.executeUpdate();
                System.out.println("[이순신 등급 수정] 영향 행: " + rows);
            }

            // 2) SELECT (수정 결과 확인)
            try (PreparedStatement ps = con.prepareStatement(selectSql);
                 ResultSet rs = ps.executeQuery()) {

                System.out.println("\n[A등급 회원 (이순신)]");
                while (rs.next()) {
                    System.out.printf("- %s | %s | %s%n",
                            rs.getString("CustName"),
                            rs.getString("Phone"),
                            rs.getDate("JoinDate"));
                }
            }
        } catch (SQLException e) {
            System.out.println("등급 수정 오류: " + e.getMessage());
        }
    }

    public void deleteCust3() {
        String delSale = "DELETE FROM Sale WHERE CustNo = 3";
        String delMem  = "DELETE FROM ShopMember WHERE CustNo = 3";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps1 = con.prepareStatement(delSale);
             PreparedStatement ps2 = con.prepareStatement(delMem)) {

            int a = ps1.executeUpdate();
            int b = ps2.executeUpdate();
            System.out.println("[CustNo=3 삭제] Sale:" + a + " | ShopMember:" + b);
        } catch (SQLException e) {
            System.out.println("삭제 오류: " + e.getMessage());
        }

    }
}

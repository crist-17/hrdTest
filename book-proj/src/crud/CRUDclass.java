package crud;

import ko.pci.conn.DBConnection;

import java.sql.*;


public class CRUDclass {
    public void q1() {

        // (1) 2020년 이상 출판도서
        System.out.printf("%n[q1] 2020년 이상 출판 도서%n");
        String sql = "select * from book where pubyear >= 2020";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            int n = 0;
            while (rs.next()) {
                n++;
                System.out.printf("- %-6d | %-20s | %,8d | %4d%n",
                        rs.getInt("bookid"),
                        rs.getString("title"),
                        rs.getInt("price"),
                        rs.getInt("pubyear")
                );
            }
            if (n == 0) System.out.printf("(결과없음)%n");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(rs, ps, conn);
        }
    }
        // (2) 특정 회원 대출 목록
        public void q2 (String name){
            System.out.printf("%n[Q2] 회원 대출 목록: %s%n", name);
            String sql =
                    "SELECT b.BookID, b.Title, r.RentalID, r.RentDate, r.ReturnDate " +
                            "FROM Rental r " +
                            "JOIN Member m ON r.MemberID = m.MemberID " +
                            "JOIN Book   b ON r.BookID   = b.BookID " +
                            "WHERE m.Name = ? " +
                            "ORDER BY r.RentDate DESC";
            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                conn = DBConnection.getConnection();
                ps = conn.prepareStatement(sql);
                ps.setString(1, name);
                rs = ps.executeQuery();
                int n = 0;
                while (rs.next()) {
                    n++;
                    System.out.printf("- BookID=%-6d | %-20s | Rent=%tF | Return=%s | RID=%d%n",
                            rs.getInt("BookID"),
                            rs.getString("Title"),
                            rs.getDate("RentDate"),
                            (rs.getDate("ReturnDate") == null ? "NULL" : rs.getDate("ReturnDate").toString()),
                            rs.getInt("RentalID"));
                }
                if (n == 0) System.out.printf("(결과 없음)%n");
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                DBConnection.close(rs, ps, conn);
            }
        }

        // (3) 미반납 도서
        public void q3() {
            System.out.printf("%n[Q3] 미반납 도서%n");
            String sql =
                    "SELECT b.BookID, b.Title, r.RentalID, r.RentDate, m.Name " +
                            "FROM Rental r " +
                            "JOIN Member m ON r.MemberID = m.MemberID " +
                            "JOIN Book   b ON r.BookID   = b.BookID " +
                            "WHERE r.ReturnDate IS NULL";
            Connection conn = null; PreparedStatement ps = null; ResultSet rs = null;
            try {
                conn = DBConnection.getConnection();
                ps = conn.prepareStatement(sql);
                rs = ps.executeQuery();
                int n = 0;
                while (rs.next()) {
                    n++;
                    System.out.printf("- %-6d | %-20s | 대출일=%tF | 회원=%s | RID=%d%n",
                            rs.getInt("BookID"),
                            rs.getString("Title"),
                            rs.getDate("RentDate"),
                            rs.getString("Name"),
                            rs.getInt("RentalID"));
                }
                if (n == 0) System.out.printf("(결과 없음)%n");
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                DBConnection.close(rs, ps, conn);
            }
        }

        // (4) 도서별 대출 횟수
        public void q4() {
            System.out.printf("%n[Q4] 도서별 대출 횟수%n");
            String sql =
                    "SELECT b.BookID, b.Title, COUNT(*) AS cnt " +
                            "FROM Rental r JOIN Book b ON r.BookID = b.BookID " +
                            "GROUP BY b.BookID, b.Title " +
                            "ORDER BY cnt DESC, b.Title";
            Connection conn = null; PreparedStatement ps = null; ResultSet rs = null;
            try {
                conn = DBConnection.getConnection();
                ps = conn.prepareStatement(sql);
                rs = ps.executeQuery();
                int n = 0;
                while (rs.next()) {
                    n++;
                    System.out.printf("- %-6d | %-20s | 대출횟수=%d%n",
                            rs.getInt("BookID"),
                            rs.getString("Title"),
                            rs.getInt("cnt"));
                }
                if (n == 0) System.out.printf("(결과 없음)%n");
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                DBConnection.close(rs, ps, conn);
            }
        }

        // (5) 최고가 도서 1권
        public void q5() {
            System.out.printf("%n[Q5] 최고가 도서 1권%n");
            String sql = "SELECT * FROM Book ORDER BY Price DESC LIMIT 1";
            Connection conn = null; PreparedStatement ps = null; ResultSet rs = null;
            try {
                conn = DBConnection.getConnection();
                ps = conn.prepareStatement(sql);
                rs = ps.executeQuery();
                if (rs.next()) {
                    System.out.printf("- %-6d | %-20s | %,8d | %4d%n",
                            rs.getInt("BookID"),
                            rs.getString("Title"),
                            rs.getInt("Price"),
                            rs.getInt("PubYear"));
                } else {
                    System.out.printf("(결과 없음)%n");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                DBConnection.close(rs, ps, conn);
            }
        }
    }



package test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcMySqlExample {

	// Change these according to your DB
	private static final String URL =
			"jdbc:mysql://localhost:3306/espocrm?useSSL=false&serverTimezone=UTC";
	private static final String USER = "espocrm_user";
	private static final String PASS = "espocrm_pass";

	public static void main(String[] args) {
		String query = "SELECT * FROM account where name=?";

		try (
				// 1. Get Connection
				Connection conn = DriverManager.getConnection(URL, USER, PASS);

				// 2. Create PreparedStatement
				PreparedStatement ps = conn.prepareStatement(query)
				) {
			// 3. Set parameters
			ps.setString(1, "Test");

			// 4. Execute query
			try (ResultSet rs = ps.executeQuery()) {

				// 5. Process result set
				while (rs.next()) {
					String id = rs.getString("id");
					String name = rs.getString("name");
					String website = rs.getString("website");

					System.out.println(id + " | " + name + " | " + website);
				}
			}
			
			
			//Update Query Example
//			conn.setAutoCommit(false);
//			String updateQuery = "update account set website=? where name=?";
//			String updateQueryName = "Test";
//			String updateQueryWebSite = "www.microsoft.com";
//
//			PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
//			updateStmt.setString(1, updateQueryWebSite);
//			updateStmt.setString(2, updateQueryName);
//			
//			int affectedResults = updateStmt.executeUpdate();
//			System.out.println("Updated Records "+affectedResults);
//			conn.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

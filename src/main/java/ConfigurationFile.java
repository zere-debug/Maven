import javax.sql.RowSet;
import java.sql.*;

@Configuration
@ComponentScan

public class ConfigurationFile {
    public class Dao {

        @Bean
        public void saveUser(String name) throws SQLException {
            String sql = "INSERT INTO \"User\" (name) VALUES (?)";
            try (Connection conn = connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, name);
                pstmt.executeUpdate();
            }
        }

        @Bean
        private Connection connect() {
            RowSet DatabaseConfig;
            return DriverManager.getConnection(DatabaseConfig.getUrl(), DatabaseConfig.getUser(), DatabaseConfig.getPassword());
        }

        @Bean
        public void deleteUserById(int userId) throws SQLException {
            String sql = "DELETE FROM \"User\" WHERE id = ?";
            try (Connection conn = connect()) {
                conn.setAutoCommit(false);
                Savepoint savepoint = conn.setSavepoint();
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, userId);
                    pstmt.executeUpdate();
                    conn.commit();
                } catch (SQLException e) {
                    conn.rollback(savepoint);
                    throw e;
                }
            }
        }

    }
}

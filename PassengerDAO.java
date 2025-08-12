import java.sql.*;

public class PassengerDAO {
    public int addPassenger(String name, String email) throws SQLException {
        String sql = "INSERT INTO passengers (name, email) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                return keys.getInt(1);
            } else {
                throw new SQLException("Passenger ID not generated");
            }
        }
    }
}

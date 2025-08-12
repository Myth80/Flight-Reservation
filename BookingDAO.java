import java.sql.*;

public class BookingDAO {
    public boolean bookTicket(int flightId, int passengerId) throws SQLException {
        FlightDAO flightDAO = new FlightDAO();
        if (flightDAO.reduceSeat(flightId)) {
            String sql = "INSERT INTO bookings (flight_id, passenger_id) VALUES (?, ?)";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, flightId);
                stmt.setInt(2, passengerId);
                stmt.executeUpdate();
                return true;
            }
        }
        return false;
    }
}

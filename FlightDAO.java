import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FlightDAO {
    public void addFlight(String flightNumber, String origin, String destination, Timestamp departure, Timestamp arrival, int seats) throws SQLException {
        String sql = "INSERT INTO flights (flight_number, origin, destination, departure_time, arrival_time, seats_available) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, flightNumber);
            stmt.setString(2, origin);
            stmt.setString(3, destination);
            stmt.setTimestamp(4, departure);
            stmt.setTimestamp(5, arrival);
            stmt.setInt(6, seats);
            stmt.executeUpdate();
        }
    }

    public List<String> listFlights() throws SQLException {
        String sql = "SELECT flight_id, flight_number, origin, destination, departure_time, arrival_time, seats_available FROM flights";
        List<String> flights = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String flight = String.format("ID: %d, Number: %s, %s -> %s, Departure: %s, Arrival: %s, Seats Available: %d",
                        rs.getInt("flight_id"),
                        rs.getString("flight_number"),
                        rs.getString("origin"),
                        rs.getString("destination"),
                        rs.getTimestamp("departure_time").toString(),
                        rs.getTimestamp("arrival_time").toString(),
                        rs.getInt("seats_available"));
                flights.add(flight);
            }
        }
        return flights;
    }

    public boolean reduceSeat(int flightId) throws SQLException {
        String checkSeatsSql = "SELECT seats_available FROM flights WHERE flight_id = ?";
        String updateSeatsSql = "UPDATE flights SET seats_available = seats_available - 1 WHERE flight_id = ? AND seats_available > 0";
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSeatsSql)) {
                checkStmt.setInt(1, flightId);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt("seats_available") > 0) {
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateSeatsSql)) {
                        updateStmt.setInt(1, flightId);
                        int updated = updateStmt.executeUpdate();
                        if (updated == 1) {
                            conn.commit();
                            return true;
                        }
                    }
                }
            }
            conn.rollback();
            return false;
        }
    }
}

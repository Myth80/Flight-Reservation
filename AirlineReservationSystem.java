import java.sql.Timestamp;
import java.util.List;
import java.util.Scanner;

public class AirlineReservationSystem {
    public static void main(String[] args) {
        FlightDAO flightDAO = new FlightDAO();
        PassengerDAO passengerDAO = new PassengerDAO();
        BookingDAO bookingDAO = new BookingDAO();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Airline Reservation System ---");
            System.out.println("1. Add Flight");
            System.out.println("2. List Flights");
            System.out.println("3. Book Ticket");
            System.out.println("4. Exit");
            System.out.print("Choose option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  

            try {
                switch (choice) {
                    case 1:
                        System.out.print("Flight Number: ");
                        String fNum = scanner.nextLine();
                        System.out.print("Origin: ");
                        String origin = scanner.nextLine();
                        System.out.print("Destination: ");
                        String dest = scanner.nextLine();
                        System.out.print("Departure Time (yyyy-MM-dd HH:mm): ");
                        String dep = scanner.nextLine();
                        System.out.print("Arrival Time (yyyy-MM-dd HH:mm): ");
                        String arr = scanner.nextLine();
                        System.out.print("Seats Available: ");
                        int seats = scanner.nextInt();
                        scanner.nextLine();

                        Timestamp departure = Timestamp.valueOf(dep + ":00");
                        Timestamp arrival = Timestamp.valueOf(arr + ":00");

                        flightDAO.addFlight(fNum, origin, dest, departure, arrival, seats);
                        System.out.println("Flight added successfully.");
                        break;

                    case 2:
                        List<String> flights = flightDAO.listFlights();
                        for (String flight : flights) {
                            System.out.println(flight);
                        }
                        break;

                    case 3:
                        System.out.print("Passenger Name: ");
                        String name = scanner.nextLine();
                        System.out.print("Passenger Email: ");
                        String email = scanner.nextLine();

                        int passengerId = passengerDAO.addPassenger(name, email);

                        System.out.print("Flight ID to book: ");
                        int flightId = scanner.nextInt();
                        scanner.nextLine();

                        boolean booked = bookingDAO.bookTicket(flightId, passengerId);
                        if (booked) {
                            System.out.println("Ticket booked successfully!");
                        } else {
                            System.out.println("Failed to book ticket: No seats available.");
                        }
                        break;

                    case 4:
                        System.out.println("Exiting system.");
                        System.exit(0);

                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

    }
}

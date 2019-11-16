package com.hrs.dal.test;

import com.hrs.configs.Configuration;
import com.hrs.dal.Gateway;
import com.hrs.exceptions.AirlineReservationSystemException;
import com.hrs.exceptions.InvalidPasswordException;
import com.hrs.exceptions.InvalidUserNameException;
import com.hrs.test.Tester;
import com.hrs.view.models.*;
import com.hrs.view.util.FieldValue;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static com.hrs.test.Tester.*;
import static com.hrs.test.Tester.STATUS_CANCELED;


public class APIservice implements ServiceModule {

    private Connection connection;

    public APIservice() {
        try {
            connection = Gateway.getDBConnection();
        } catch (SQLException e) {

        }
    }

    @Override
    public Set<Flight> getAllFlightsByAirline(String airlineName, LocalDate localDate) throws AirlineReservationSystemException {

        airlineName = "'" + airlineName + "'";
        String dt = "'" + localDate.toString() + "'";
        Set<Flight> flights = new LinkedHashSet<>();
        String query = "select flight_info.flight_info_id,airline_flight_info.airline_flight_id,airline_info.airline_id, " +
                "source_name, destination_name, flight_status_info," +
                "flight_source_date,flight_dest_date,flight_max_capacity,flight_current_capacity,fare,airline_name," +
                "airline_flight_name, flight_fly_time, flight_land_time\n" +
                "from flight_info, airline_info, airline_flight_info, flight_status\n" +
                "where flight_info.airline_flight_id = airline_flight_info.airline_flight_id and\n" +
                "airline_flight_info.airline_id = airline_info.airline_id and\n" +
                "flight_status.airline_flight_id = airline_flight_info.airline_flight_id and\n" +
                "airline_info.airline_name = " + airlineName + " and\n" +
                "flight_source_date > " + dt;
        try {
            Statement statement = this.connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            int rowcount = 0;
            if (rs.last()) {
                rowcount = rs.getRow();
                rs.beforeFirst();
            }
            if (rowcount == 0) {
                throw new IllegalArgumentException("Airline Name Not Found");
            }

            while (rs.next()) {

                Integer flightID = Integer.parseInt(rs.getString("flight_info.flight_info_id"));
                String flightCode = Integer.toString(rs.getString("airline_flight_name").hashCode());
                LocalDate sourceDate = LocalDate.parse(rs.getString("flight_source_date"));
                Source source = new Source(rs.getString("source_name"), sourceDate, rs.getString("flight_fly_time"));
                LocalDate destinationDate = LocalDate.parse(rs.getString("flight_dest_date"));
                Destination destination = new Destination(rs.getString("destination_name"), destinationDate, rs.getString("flight_land_time"));
                Integer capacity = Integer.parseInt(rs.getString("flight_max_capacity"));
                Integer availableSeat = capacity - Integer.parseInt(rs.getString("flight_current_capacity"));
                Float fare = Float.parseFloat(rs.getString("fare"));
                String status = rs.getString("flight_status_info");
                Airline airLine = new Airline(Integer.parseInt(rs.getString("airline_flight_info.airline_flight_id")), rs.getString("airline_name"));
                Airplane airplane = new Airplane(Integer.parseInt(rs.getString("airline_info.airline_id")), rs.getString("airline_flight_name"));

                Flight flight = new Flight(flightID, flightCode, source, destination, availableSeat, status, airLine, airplane, fare);
                System.out.println(flight);
                flights.add(flight);
            }
        } catch (SQLException e) {

        }

        return flights;
    }

    @Override
    public Set<Flight> getAllFlightsForReservation() {
        /*
         * @this query will show you all the current available flights, before you showed it to customer screen, make
         * sure check the given date.
         * */
        Set<Flight> flights = new LinkedHashSet<>();
        String current = "'" + LocalDate.now().toString() + "'";

        String query = "select flight_info.flight_info_id, airline_flight_info.airline_flight_id,airline_info.airline_id, \n" +
                "source_name, destination_name, flight_status_info,flight_source_date, \n" +
                "flight_dest_date,flight_max_capacity,flight_current_capacity,fare,airline_name, \n" +
                "airline_flight_name, flight_fly_time, flight_land_time\n" +
                "from flight_info, airline_info, airline_flight_info, flight_status\n" +
                "where flight_info.airline_flight_id = airline_flight_info.airline_flight_id and\n" +
                "airline_flight_info.airline_id = airline_info.airline_id and\n" +
                "flight_status.airline_flight_id = airline_flight_info.airline_flight_id and\n" +
                "flight_max_capacity > flight_current_capacity and\n" +
                "flight_status_info = 'On Time' and\n" +
                "flight_source_date > " + current;

        try {
            Statement statement = this.connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            int rowcount = 0;
            if (rs.last()) {
                rowcount = rs.getRow();
                rs.beforeFirst();
            }
            if (rowcount == 0) {
                throw new IllegalArgumentException("No Result Found");
            }

            while (rs.next()) {

                Integer flightID = Integer.parseInt(rs.getString("flight_info.flight_info_id"));
                String flightCode = Integer.toString(rs.getString("airline_flight_name").hashCode());
                LocalDate sourceDate = LocalDate.parse(rs.getString("flight_source_date"));
                Source source = new Source(rs.getString("source_name"), sourceDate, rs.getString("flight_fly_time"));
                LocalDate destinationDate = LocalDate.parse(rs.getString("flight_dest_date"));
                Destination destination = new Destination(rs.getString("destination_name"), destinationDate, rs.getString("flight_land_time"));
                Integer capacity = Integer.parseInt(rs.getString("flight_max_capacity"));
                Integer availableSeat = capacity - Integer.parseInt(rs.getString("flight_current_capacity"));
                Float fare = Float.parseFloat(rs.getString("fare"));
                String status = rs.getString("flight_status_info");
                Airline airLine = new Airline(Integer.parseInt(rs.getString("airline_flight_info.airline_flight_id")), rs.getString("airline_name"));
                Airplane airplane = new Airplane(Integer.parseInt(rs.getString("airline_info.airline_id")), rs.getString("airline_flight_name"));

                Flight flight = new Flight(flightID, flightCode, source, destination, availableSeat, status, airLine, airplane, fare);
                System.out.println(flight);
                flights.add(flight);
            }
        } catch (SQLException e) {

        }

        return flights;
    }

    @Override
    public Set<Reservation> getAllReservationsByCustomerId(Integer customerId) {

        Set<Reservation> reservations = new LinkedHashSet<>();

        String query = "select flight_info.flight_info_id, airline_flight_info.airline_flight_id,airline_info.airline_id,\n" +
                "source_name, destination_name, flight_status_info,flight_source_date, \n" +
                "flight_dest_date,flight_max_capacity,flight_current_capacity,fare,airline_name,\n" +
                "airline_flight_name, flight_fly_time, flight_land_time, res_status, reservation_by, reservation_date\n" +
                "from flight_info, airline_info, airline_flight_info, flight_status, customer_info, reservation_status, reservation_info\n" +
                "where flight_info.airline_flight_id = airline_flight_info.airline_flight_id and\n" +
                "airline_flight_info.airline_id = airline_info.airline_id and\n" +
                "flight_status.airline_flight_id = airline_flight_info.airline_flight_id and\n" +
                "customer_info.customer_id = " + Integer.toString(customerId) + " and\n " +
                "customer_info.customer_id = reservation_info.customer_id and\n" +
                "reservation_info.reservation_id = reservation_status.reservation_id and\n" +
                "reservation_info.reservation_id = flight_info.reservation_id";
        try {
            Statement statement = this.connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            int rowcount = 0;
            if (rs.last()) {
                rowcount = rs.getRow();
                rs.beforeFirst();
            }
            if (rowcount == 0) {
                throw new IllegalArgumentException("Id Not Found");
            }
            while (rs.next()) {
                Integer flightID = Integer.parseInt(rs.getString("flight_info.flight_info_id"));
                String flightCode = Integer.toString(rs.getString("airline_flight_name").hashCode());
                LocalDate sourceDate = LocalDate.parse(rs.getString("flight_source_date"));
                Source source = new Source(rs.getString("source_name"), sourceDate, rs.getString("flight_fly_time"));
                LocalDate destinationDate = LocalDate.parse(rs.getString("flight_dest_date"));
                Destination destination = new Destination(rs.getString("destination_name"), destinationDate, rs.getString("flight_land_time"));
                Integer capacity = Integer.parseInt(rs.getString("flight_max_capacity"));
                Integer availableSeat = capacity - Integer.parseInt(rs.getString("flight_current_capacity"));
                Float fare = Float.parseFloat(rs.getString("fare"));
                String status = rs.getString("flight_status_info");
                Airline airLine = new Airline(Integer.parseInt(rs.getString("airline_flight_info.airline_flight_id")), rs.getString("airline_name"));
                Airplane airplane = new Airplane(Integer.parseInt(rs.getString("airline_info.airline_id")), rs.getString("airline_flight_name"));
                Flight flight = new Flight(flightID, flightCode, source, destination, availableSeat, status, airLine, airplane, fare);

                Reservation reservation = new Reservation(flight, LocalDate.parse(rs.getString("reservation_date")), rs.getString("res_status"), Integer.parseInt(rs.getString("reservation_by")));
                System.out.println(reservation);
                reservations.add(reservation);
            }
        } catch (SQLException e) {

        }

        return reservations;
    }

    @Override
    public Customer getCustomerByLogin(String username, String password) throws InvalidUserNameException, InvalidPasswordException {
        Set<Reservation> reservations = new LinkedHashSet<>();
        Set<Flight> flights = new LinkedHashSet<>();
        Customer customer = new Customer();
        username = "'" + username + "'";
        password = "'" + password + "'";
        String query = "select customer_info.customer_id, customer_first_name, customer_last_name, customer_email, cust_username, cust_password\n" +
                "from customer_info, customer_login\n" +
                "where customer_info.customer_id = customer_login.customer_id\n" +
                "and cust_username = " + username + " and cust_password = " + password;
        Integer id = null;
        String fname = "";
        String lname = "";
        Login login = new Login();
        try {
            Statement statement = this.connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            int rowcount = 0;
            if (rs.last()) {
                rowcount = rs.getRow();
                rs.beforeFirst();
            }
            if (rowcount == 0) {
                throw new IllegalArgumentException("Customer Not Found");
            }
            while (rs.next()) {
                id = Integer.parseInt(rs.getString("customer_info.customer_id"));
                fname = rs.getString("customer_first_name");
                lname = rs.getString("customer_last_name");

                login = new Login(rs.getString("cust_username"), rs.getString("cust_password"));
                reservations = getAllReservationsByCustomerId(id);
            }


            String query2 = "select flight_info.flight_info_id, airline_flight_info.airline_flight_id,airline_info.airline_id,\n" +
                    "source_name, destination_name, flight_status_info,flight_source_date, \n" +
                    "flight_dest_date,flight_max_capacity,flight_current_capacity,fare,airline_name,\n" +
                    "airline_flight_name, flight_fly_time, flight_land_time, res_status, reservation_by, reservation_date\n" +
                    "from flight_info, airline_info, airline_flight_info, flight_status, customer_info, reservation_status, reservation_info\n" +
                    "where flight_info.airline_flight_id = airline_flight_info.airline_flight_id and\n" +
                    "airline_flight_info.airline_id = airline_info.airline_id and\n" +
                    "flight_status.airline_flight_id = airline_flight_info.airline_flight_id and\n" +
                    "customer_info.customer_id = " + id + " and\n " +
                    "customer_info.customer_id = reservation_info.customer_id and\n" +
                    "reservation_info.reservation_id = reservation_status.reservation_id and\n" +
                    "reservation_info.reservation_id = flight_info.reservation_id";
            ResultSet rs2 = statement.executeQuery(query2);
            while (rs2.next()) {
                Integer flightID = Integer.parseInt(rs.getString("flight_info.flight_info_id"));
                String flightCode = Integer.toString(rs.getString("airline_flight_name").hashCode());
                LocalDate sourceDate = LocalDate.parse(rs.getString("flight_source_date"));
                Source source = new Source(rs.getString("source_name"), sourceDate, rs.getString("flight_fly_time"));
                LocalDate destinationDate = LocalDate.parse(rs.getString("flight_dest_date"));
                Destination destination = new Destination(rs.getString("destination_name"), destinationDate, rs.getString("flight_land_time"));
                Integer capacity = Integer.parseInt(rs.getString("flight_max_capacity"));
                Integer availableSeat = capacity - Integer.parseInt(rs.getString("flight_current_capacity"));
                Float fare = Float.parseFloat(rs.getString("fare"));
                String status = rs.getString("flight_status_info");
                Airline airLine = new Airline(Integer.parseInt(rs.getString("airline_flight_info.airline_flight_id")), rs.getString("airline_name"));
                Airplane airplane = new Airplane(Integer.parseInt(rs.getString("airline_info.airline_id")), rs.getString("airline_flight_name"));
                Flight flight = new Flight(flightID, flightCode, source, destination, availableSeat, status, airLine, airplane, fare);
                flights.add(flight);
            }
            customer = new Customer(id, fname, lname, login, reservations, flights);
            System.out.println(customer);
        } catch (SQLException e) {

        }

        return customer;
    }

    @Override
    public Admin getGlobalAdminByLogin(String username, String password) throws InvalidUserNameException, InvalidPasswordException {
        username = "'" + username + "'";
        password = "'" + password + "'";

        String query = "select airline_admin_fname, airline_admin_lname, admin_username, admin_password\n" +
                "from airline_admin, airline_admin_login\n" +
                "where airline_admin.airline_admin_id = airline_admin_login.airline_admin_id and\n" +
                "admin_username = " + username + " and admin_password = " + password;
        Admin admin = new Admin();
        try {
            Statement statement = this.connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            int rowcount = 0;
            if (rs.last()) {
                rowcount = rs.getRow();
                rs.beforeFirst();
            }
            if (rowcount == 0) {
                throw new IllegalArgumentException("Admin Not Found");
            }

            while (rs.next()) {
                admin = new Admin(rs.getString("airline_admin_fname"), rs.getString("admin_username"), new Login(rs.getString("admin_username"), rs.getString("admin_password")));
            }
            System.out.println(admin);

        } catch (SQLException e) {

        }

        return admin;
    }

    @Override
    public Admin getAirlineAdminByLogin(String airline, String username, String password) throws InvalidUserNameException, InvalidPasswordException {

        username = "'" + username + "'";
        password = "'" + password + "'";
        airline = "'" + airline + "'";

        String query = "select airline_admin_fname, airline_admin_lname, admin_username, admin_password, airline_name\n" +
                "from airline_admin, airline_admin_login, airline_info\n" +
                "where airline_admin.airline_admin_id = airline_admin_login.airline_admin_id and\n" +
                "admin_username = " + username + " and admin_password = " + password + " and \n" +
                "airline_info.airline_id = airline_admin.airline_id\n" +
                "and airline_name = " + airline;

        Admin admin = new Admin();
        try {
            Statement statement = this.connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            int rowcount = 0;
            if (rs.last()) {
                rowcount = rs.getRow();
                rs.beforeFirst();
            }
            if (rowcount == 0) {
                throw new IllegalArgumentException("Admin Not Found");
            }

            while (rs.next()) {
                admin = new Admin(rs.getString("airline_admin_fname"), rs.getString("admin_username"), new Login(rs.getString("admin_username"), rs.getString("admin_password")));
            }
            System.out.println(admin);

        } catch (SQLException e) {

        }

        return admin;
    }

    @Override
    public Set<Reservation> getGlobalReservationsMadeUsingSearchEngine() {
        Set<Reservation> reservations = new LinkedHashSet<>();

        String query = "select flight_info.flight_info_id, airline_flight_info.airline_flight_id,airline_info.airline_id,\n" +
                "source_name, destination_name, flight_status_info,flight_source_date, \n" +
                "flight_dest_date,flight_max_capacity,flight_current_capacity,fare,airline_name,\n" +
                "airline_flight_name, flight_fly_time, flight_land_time, res_status, reservation_by, reservation_date\n" +
                "from flight_info, airline_info, airline_flight_info, flight_status, customer_info, reservation_status, reservation_info\n" +
                "where flight_info.airline_flight_id = airline_flight_info.airline_flight_id and\n" +
                "airline_flight_info.airline_id = airline_info.airline_id and\n" +
                "flight_status.airline_flight_id = airline_flight_info.airline_flight_id and\n" +
                "customer_info.customer_id = and\n " +
                "customer_info.customer_id = reservation_info.customer_id and\n" +
                "reservation_info.reservation_id = reservation_status.reservation_id and\n" +
                "reservation_info.reservation_id = flight_info.reservation_id";
        try {
            Statement statement = this.connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            int rowcount = 0;
            if (rs.last()) {
                rowcount = rs.getRow();
                rs.beforeFirst();
            }
            if (rowcount == 0) {
                throw new IllegalArgumentException("Id Not Found");
            }
            while (rs.next()) {
                Integer flightID = Integer.parseInt(rs.getString("flight_info.flight_info_id"));
                String flightCode = Integer.toString(rs.getString("airline_flight_name").hashCode());
                LocalDate sourceDate = LocalDate.parse(rs.getString("flight_source_date"));
                Source source = new Source(rs.getString("source_name"), sourceDate, rs.getString("flight_fly_time"));
                LocalDate destinationDate = LocalDate.parse(rs.getString("flight_dest_date"));
                Destination destination = new Destination(rs.getString("destination_name"), destinationDate, rs.getString("flight_land_time"));
                Integer capacity = Integer.parseInt(rs.getString("flight_max_capacity"));
                Integer availableSeat = capacity - Integer.parseInt(rs.getString("flight_current_capacity"));
                Float fare = Float.parseFloat(rs.getString("fare"));
                String status = rs.getString("flight_status_info");
                Airline airLine = new Airline(Integer.parseInt(rs.getString("airline_flight_info.airline_flight_id")), rs.getString("airline_name"));
                Airplane airplane = new Airplane(Integer.parseInt(rs.getString("airline_info.airline_id")), rs.getString("airline_flight_name"));
                Flight flight = new Flight(flightID, flightCode, source, destination, availableSeat, status, airLine, airplane, fare);

                Reservation reservation = new Reservation(flight, LocalDate.parse(rs.getString("reservation_date")), rs.getString("res_status"), Integer.parseInt(rs.getString("reservation_by")));
                System.out.println(reservation);
                reservations.add(reservation);
            }
        } catch (SQLException e) {

        }

        return reservations;
    }

    @Override
    public Set<Airplane> getAllAirPlaneByAirLine(String airlineName) {
        Set<Airplane> airplanes = new LinkedHashSet<>();

        return airplanes;
    }

    @Override
    public Set<Airport> getAllAirports() {
        Set<Airport> airports = new LinkedHashSet<>();
        return airports;
    }

    @Override
    public Set<Flight> getAllFlightsByAirlineForReservation(String airlineName) {
        return Tester.testFlights();
    }

    @Override
    public boolean insertNewCustomer(String firstName, String lastName, String email, String password)
//            throws IllegalArgumentException, InvalidEmailException
    {
        try {

        } catch (Exception ex) {

        }
        return true;
    }

    @Override
    public boolean cancelReservation(Integer customerId, Integer reservationId) {
        return true;
    }

    @Override
    public boolean cancelFlight(Integer flightId) {
        return true;
    }

    @Override
    public boolean makeReservation(Integer flightIdPk, String username, String password)
            throws InvalidUserNameException, InvalidPasswordException {
        return true;
    }

    @Override
    public boolean makeReservation(Integer flightIdPk, Integer customerId) {
        return true;
    }

    @Override
    public boolean makeReservationBySE(Integer flightIdPk, String username, String password)
            throws InvalidUserNameException, InvalidPasswordException {
        return true;
    }

    @Override
    public boolean makeReservationBySE(Integer flightIdPk, Integer customerId) {
        return true;
    }

    @Override
    public boolean insertFlightByAirline(Flight flight) {
        return true;
    }

    @Override
    public Set<Flight> getAllFlightsByAirport(String airportName) {
        Set<Flight> flights = new LinkedHashSet<>();
        return flights;
    }

}

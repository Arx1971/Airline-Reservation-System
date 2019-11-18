package com.hrs.dal.test;

import com.hrs.configs.Configuration;
import com.hrs.dal.Gateway;
import com.hrs.test.Tester;
import com.hrs.view.models.*;
import com.hrs.view.util.FieldValue;

import java.sql.*;
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
    public Set<Flight> getAllFlightsByAirline(String airlineName, LocalDate localDate) {

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
    public Customer getCustomerByLogin(String username, String password) {
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
    public Admin getGlobalAdminByLogin(String username, String password) {
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
    public Admin getAirlineAdminByLogin(String airline, String username, String password) {

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
                "reservation_info.reservation_id = reservation_status.reservation_id and\n" +
                "reservation_info.reservation_id = flight_info.reservation_id and\n" +
                "reservation_by = 0";
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
        airlineName = "'" + airlineName + "'";
        String query = "select airline_flight_id, airline_flight_name\n" +
                "from airline_flight_info, airline_info\n" +
                "where airline_info.airline_id = airline_flight_info.airline_id\n" +
                "and airline_name = " + airlineName;
        try {
            Statement statement = this.connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            int rowcount = 0;
            if (rs.last()) {
                rowcount = rs.getRow();
                rs.beforeFirst();
            }
            if (rowcount == 0) {
                throw new IllegalArgumentException("Airline Name has No Airplane");
            }
            while (rs.next()) {
                Airplane airplane = new Airplane(Integer.parseInt(rs.getString("airline_flight_id")), rs.getString("airline_flight_name"));
                airplanes.add(airplane);
                //System.out.println(rs.getString("airline_flight_id") + " " + rs.getString("airline_flight_name"));
            }

        } catch (SQLException e) {

        }
        System.out.println(airplanes);
        return airplanes;
    }

    @Override
    public Set<Airport> getAllAirports() {
        Set<Airport> airports = new LinkedHashSet<>();
        String query = "select airport_id, airport_name\n" +
                "from airport_info";
        try {
            Statement statement = this.connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            int rowcount = 0;
            if (rs.last()) {
                rowcount = rs.getRow();
                rs.beforeFirst();
            }
            if (rowcount == 0) {
                throw new IllegalArgumentException("No Airport Found");
            }
            while (rs.next()) {
                Airport airport = new Airport(Integer.parseInt(rs.getString("airport_id")), rs.getString("airport_name"));
                airports.add(airport);
                System.out.println(rs.getString("airport_id") + " " + rs.getString("airport_name"));
            }
        } catch (SQLException e) {

        }

        return airports;
    }

    @Override
    public Set<Flight> getAllFlightsByAirlineForReservation(String airlineName) {
        airlineName = "'" + airlineName + "'";
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
                "flight_source_date > " + current + "and airline_name = " + airlineName;

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
    public boolean insertNewCustomer(String firstName, String lastName, String email, String password) {
        insert_customer_info(firstName, lastName, email, password);
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
    public boolean makeReservation(Integer flightIdPk, String username, String password) {
        return true;
    }

    @Override
    public boolean makeReservation(Integer flightIdPk, Integer customerId) {
        return true;
    }

    @Override
    public boolean makeReservationBySE(Integer flightIdPk, String username, String password) {
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
        airportName = "'" + airportName + "'";
        Set<Flight> flights = new LinkedHashSet<>();

        String query = "select flight_info.flight_info_id, airline_flight_info.airline_flight_id,airline_info.airline_id, \n" +
                "source_name, destination_name, flight_status_info,flight_source_date,\n" +
                "flight_dest_date,flight_max_capacity,flight_current_capacity,fare,airline_name, \n" +
                "airline_flight_name, flight_fly_time, flight_land_time\n" +
                "from flight_info, airline_info, airline_flight_info, flight_status\n" +
                "where flight_info.airline_flight_id = airline_flight_info.airline_flight_id and\n" +
                "airline_flight_info.airline_id = airline_info.airline_id and\n" +
                "flight_status.airline_flight_id = airline_flight_info.airline_flight_id and\n" +
                "(source_name = " + airportName + " or destination_name = " + airportName + ")";

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

    public Set<Reservation> getAllReservationsMadeUsingSearchEngineAndAirlineGui(String airlineName) {
        return null;
    }

    void insertAirlineAdmin(String firstname, String lastname, Integer airline_ID) {

        String firstname_1 = "'" + firstname + "'";
        String lastname_1 = "'" + lastname + "'";

        String query = "insert into airline_admin(airline_id, airline_admin_fname, airline_admin_lname) values ( " + airline_ID + "," + firstname_1 + "," + lastname_1 + ")";
        try {
            PreparedStatement ps = connection.prepareStatement(query,
                    Statement.RETURN_GENERATED_KEYS);

            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();
            int generatedKey = 0;
            if (rs.next()) {
                generatedKey = rs.getInt(1);
            }

            insertAdminLogin(generatedKey, firstname + lastname, "12345");

        } catch (SQLException e) {
            throw new IllegalArgumentException("AirLine Admin Exist");
        }

    }

    private void insertAdminLogin(Integer admin_ID, String username, String password) {

        String username_ = "'" + username + "'";
        String password_ = "'" + password + "'";

        String query = "insert into airline_admin_login(airline_admin_id, admin_username, admin_password) values ( " + admin_ID + "," + username_ + "," + password_ + ")";
        try {
            PreparedStatement ps = connection.prepareStatement(query,
                    Statement.RETURN_GENERATED_KEYS);

            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();
            int generatedKey = 0;
            if (rs.next()) {
                generatedKey = rs.getInt(1);
            }

        } catch (SQLException e) {
            throw new IllegalArgumentException("Admin ID Error");
        }
    }

    private void insertAirline_flight_info(String flight_name, Integer airline_id, float fare, int flight_max_capacity, int flight_current_capacity) {

        String flight_name_ = "'" + flight_name + "'";

        String query = "insert into airline_flight_info(airline_flight_name, airline_id, fare, flight_max_capacity, flight_current_capacity)\n" +
                "\tvalue ( " + flight_name_ + "," + airline_id + "," + fare + "," + flight_max_capacity + "," + flight_current_capacity + ")";
        try {
            PreparedStatement ps = connection.prepareStatement(query,
                    Statement.RETURN_GENERATED_KEYS);

            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();
            int generatedKey = 0;
            if (rs.next()) {
                generatedKey = rs.getInt(1);
            }

        } catch (SQLException e) {
            throw new IllegalArgumentException("");
        }
    }

    void insert_airline_info(String airline_name) {

        String airline_name_ = "'" + airline_name + "'";

        String query = "insert into airline_info(airline_name) values ( " + airline_name_ + ")";
        try {
            PreparedStatement ps = connection.prepareStatement(query,
                    Statement.RETURN_GENERATED_KEYS);

            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();
            int generatedKey = 0;
            if (rs.next()) {
                generatedKey = rs.getInt(1);
            }

        } catch (SQLException e) {
            throw new IllegalArgumentException("");
        }

    }

    private void insert_airport_info(String airport_name) {

        String airport_name_ = "'" + airport_name + "'";

        String query = "insert into airport_info(airport_name) values ( " + airport_name_ + ")";
        try {
            PreparedStatement ps = connection.prepareStatement(query,
                    Statement.RETURN_GENERATED_KEYS);

            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();
            int generatedKey = 0;
            if (rs.next()) {
                generatedKey = rs.getInt(1);
            }

        } catch (SQLException e) {
            throw new IllegalArgumentException("");
        }

    }

    private void insert_arrival_info(Integer airport_id, Integer airline_flight_id, Integer flight_status_id) {
    }

    private void insert_available_flight_id(Integer airline_flight_id, LocalDate localDate) {
    }

    private void insert_customer_info(String firstname, String lastname, String email, String password) {
        String firstname_1 = "'" + firstname + "'";
        String lastname_1 = "'" + lastname + "'";
        String email_1 = "'" + email + "'";
        String query = "insert into customer_info(customer_first_name, customer_last_name, customer_email) values ( " + firstname_1 + "," + lastname_1 + "," + email_1 + ")";
        try {
            PreparedStatement ps = connection.prepareStatement(query,
                    Statement.RETURN_GENERATED_KEYS);

            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();
            int generatedKey = 0;
            if (rs.next()) {
                generatedKey = rs.getInt(1);
            }
            insert_customer_login(email, password, generatedKey);
        } catch (SQLException e) {
            throw new IllegalArgumentException(" name is already Taken");
        }

    }

    private void insert_customer_login(String username, String password, Integer customer_id) {

        username = "'" + username + "'";
        password = "'" + password + "'";
        String query = "insert into customer_login(cust_username, cust_password, customer_id) values( " + username + "," + password + "," + customer_id + ")";
        try {
            PreparedStatement ps = connection.prepareStatement(query,
                    Statement.RETURN_GENERATED_KEYS);
            ps.execute();
        } catch (SQLException e) {
            throw new IllegalArgumentException("Err");
        }
    }

    private void insert_departures_info(Integer airport_id, Integer airline_flight_id, Integer flight_status_id) {
        String query = "insert into departures_info(airport_id, airline_flight_id, " +
                "flight_status_id) values( " + airport_id + "," + airline_flight_id + "," + flight_status_id
                + ")";
        try {
            PreparedStatement ps = connection.prepareStatement(query,
                    Statement.RETURN_GENERATED_KEYS);
            ps.execute();
        } catch (SQLException e) {
            throw new IllegalArgumentException("Err");
        }
    }

    private void insert_destination_info(Integer airport_id) {
        String query = "insert into destination_info(airport_id) values (2);" + airport_id + ")";
        try {
            PreparedStatement ps = connection.prepareStatement(query,
                    Statement.RETURN_GENERATED_KEYS);
            ps.execute();
        } catch (SQLException e) {
            throw new IllegalArgumentException("Err");
        }
    }

    private void insert_source_info(Integer airport_id) {
        String query = "insert into source_info(airport_id) values (2);" + airport_id + ")";
        try {
            PreparedStatement ps = connection.prepareStatement(query,
                    Statement.RETURN_GENERATED_KEYS);
            ps.execute();
        } catch (SQLException e) {
            throw new IllegalArgumentException("Err");
        }
    }

    private void insert_flight_info(Integer reservation_id, Integer airline_flight_id, LocalDate sourceDate, LocalDate destination_date, String fly_time, String land_time, String source_name, String destination_name) {

    }

    private void insert_flight_status_info(Integer airline_flight_id, String flight_status) {
        
    }

    private void insert_reservation_info(Integer customer_id, String reservation_by, LocalDate localDate) {

    }

    private void insert_reservation_status(String res_status) {

    }

}

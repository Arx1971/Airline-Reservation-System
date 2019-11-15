package com.hrs.dal.test;

import com.hrs.dal.Gateway;
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
    public Set<Flight> getAllFlightsByAirline(String airlineName, LocalDate localDate) {

        airlineName = "'" + airlineName + "'";
        String dt = "'" + localDate.toString() + "'";
        Set<Flight> flights = new LinkedHashSet<>();
        String query = "select flight_info.flight_info_id,airline_flight_info.airline_flight_id,airline_info.airline_id, source_name, destination_name, flight_status_info," +
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
        Set<Flight> flights = new LinkedHashSet<>();

        String query = "select flight_info.flight_info_id, airline_flight_info.airline_flight_id,airline_info.airline_id, \n" +
                "source_name, destination_name, flight_status_info,flight_source_date, \n" +
                "flight_dest_date,flight_max_capacity,flight_current_capacity,fare,airline_name, \n" +
                "airline_flight_name, flight_fly_time, flight_land_time\n" +
                "from flight_info, airline_info, airline_flight_info, flight_status\n" +
                "where flight_info.airline_flight_id = airline_flight_info.airline_flight_id and\n" +
                "airline_flight_info.airline_id = airline_info.airline_id and\n" +
                "flight_status.airline_flight_id = airline_flight_info.airline_flight_id and\n" +
                "flight_max_capacity > flight_current_capacity and\n" +
                "flight_status_info = 'On Time'";

        try {
            Statement statement = this.connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
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

        return reservations;
    }

    @Override
    public Customer getCustomerByLogin(String username, String password) throws InvalidUserNameException, InvalidPasswordException {
        return Tester.testCustomer();
    }

    @Override
    public Admin getGlobalAdminByLogin(String username, String password) throws InvalidUserNameException, InvalidPasswordException {
        return Tester.admin();
    }

    @Override
    public Admin getAirlineAdminByLogin(String airline, String username, String password) throws InvalidUserNameException, InvalidPasswordException {
        return new Admin("", "");
    }

    @Override
    public Set<Reservation> getGlobalReservationsMadeUsingSearchEngine() {
        Set<Reservation> reservations = new LinkedHashSet<>();
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

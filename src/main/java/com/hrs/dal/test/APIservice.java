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
        return Tester.testFlights();
    }

    @Override
    public Set<Flight> getAllFlightsForReservation() {
        return Tester.testFlights();
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
        reservations.add(new Reservation(testCustomer(), testFlight1(), LocalDate.now(), STATUS_ACTIVE(), 0));
        reservations.add(new Reservation(testCustomer(), testFlight2(), LocalDate.now(), STATUS_CANCELED(), 0));
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

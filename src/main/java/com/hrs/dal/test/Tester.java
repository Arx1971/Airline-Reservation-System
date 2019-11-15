package com.hrs.dal.test;

import com.hrs.dal.Gateway;

import java.sql.*;
import java.time.LocalDate;

public class Tester {

    public static void main(String args[]) throws SQLException {
        LocalDate localDate = LocalDate.of(2017, 11, 6);
        APIservice as = new APIservice();
        System.out.println("Flight By Airline And Date: \n");
        as.getAllFlightsByAirline("American Airlines", localDate);
        System.out.println("Flight For Reservation: \n");
        as.getAllFlightsForReservation();
        System.out.println("Reservation By Customer ID: ");
        as.getAllReservationsByCustomerId(2);
    }

}

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
        System.out.println("LOGIN INFORMATION BY CUSTOMER: USERNAME AND PASSWORD");
        as.getCustomerByLogin("megatron@email.com", "12345");
        as.getGlobalAdminByLogin("delta1234", "12345");
        as.getAirlineAdminByLogin("Delta", "Jetblue1234", "12345");
        System.out.println("getGlobalReservationsMadeUsingSearchEngine");
        as.getGlobalReservationsMadeUsingSearchEngine();
        System.out.println("getAllAirPlaneByAirLine");
        as.getAllAirPlaneByAirLine("American Airlines");
        System.out.println("AIRPORTS NAME");
        as.getAllAirports();
        System.out.println("getAllFlightsByAirlineForReservation");
        as.getAllFlightsByAirlineForReservation("American Airlines");
        //as.insertNewCustomer("Bubmbble", "Bee", "BumbleBee@email.com", "12345");
    }

}

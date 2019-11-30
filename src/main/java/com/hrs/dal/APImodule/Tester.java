package com.hrs.dal.APImodule;

import java.sql.*;
import java.time.LocalDate;

public class Tester {

    public static void main(String args[]) throws SQLException {
        LocalDate localDate = LocalDate.of(2017, 11, 6);
        APIservice as = new APIservice();
        /*System.out.println("Flight By Airline And Date: \n");
        as.getAllFlightsByAirline("American Airlines", localDate);
        System.out.println("Flight For Reservation: \n");
        System.out.println("Reservation By Customer ID: ");
        as.getAllReservationsByCustomerId(3);
        System.out.println("LOGIN INFORMATION BY CUSTOMER: USERNAME AND PASSWORD");
        as.getCustomerByLogin("megatron@email.com", "12345");
        as.getGlobalAdminByLogin("delta1234", "12345");
        as.getAirlineAdminByLogin("Delta", "Jetblue1234", "12345");
        System.out.println("getGlobalReservationsMadeUsingSearchEngine");

        System.out.println("getAllAirPlaneByAirLine");
        as.getAllAirPlaneByAirLine("American Airlines");
        System.out.println("AIRPORTS NAME");
        as.getAllAirports();
        System.out.println("getAllFlightsByAirlineForReservation");
        as.getAllFlightsByAirlineForReservation("American Airlines");
        //as.insertNewCustomer("Bubmbble", "Bee", "BumbleBee@email.com", "12345"); // tested
        System.out.println("getAllFlightsByAirport");
        as.getAllFlightsByAirport("LA");
        as.insertAirlineAdmin("Vector", "Prime", 1); // tested
        as.insert_airline_info("S");
        as.insert_flight_info(1, 2, LocalDate.of(2018, 12, 31), LocalDate.of(2018, 12, 31), "12:00:00", "02:00:00", "NY", "BOSTON");
*/
        //as.cancelReservation(1, 1);
        //as.getGlobalReservationsMadeUsingSearchEngine();
        //as.getAllFlightsByAirline("American Airlines", localDate);
        //as.getAllFlightsForReservation("str");
        //as.getCustomerByLogin("megatron@email.com", "12345");
        //as.getGlobalAdminByLogin("delta1234", "12345");
        //as.getAirlineAdminByLogin("Delta", "delta1234", "12345");
        //as.getAllAirPlaneByAirLine("American Airlines");
        //as.getAllAirports();
        //as.getAllFlightsByAirlineForReservation("American Airlines");
        //as.getAllReservationsMadeUsingSearchEngineAndAirlineGui("American Airlines");
        //as.cancelFlight(2);
        as.makeReservation(1, "megatron@email.com", "12345");
    }

}

package com.hrs.dal.test;

import com.hrs.dal.Gateway;
import com.hrs.exceptions.AirlineReservationSystemException;
import com.hrs.exceptions.InvalidPasswordException;
import com.hrs.exceptions.InvalidUserNameException;

import java.sql.*;
import java.time.LocalDate;

public class Tester {

    public static void main(String args[]) throws SQLException, AirlineReservationSystemException {
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
    }

}

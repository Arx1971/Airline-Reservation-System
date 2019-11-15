package com.hrs.dal.test;

import com.hrs.dal.Gateway;

import java.sql.*;
import java.time.LocalDate;

public class Tester {

    public static void main(String args[]) throws SQLException {
        LocalDate ldObj = LocalDate.of(2017, 11, 6);
        APIservice as = new APIservice();
        as.getAllFlightsByAirline("American Airlines", ldObj);
        as.getAllFlightsForReservation();
    }

}

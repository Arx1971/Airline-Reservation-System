insert into customer_info(customer_first_name, customer_last_name, customer_email) values ('Megatron', 'live', 'megatron@email.com');
insert into customer_info(customer_first_name, customer_last_name, customer_email) values ('Optimus', 'Prime', 'optimus@email.com');
insert into customer_info(customer_first_name, customer_last_name, customer_email) values ('Iron', 'Hide', 'iron@email.com');

insert into airline_info(airline_name) values ('Delta');
insert into airline_info(airline_name) values ('American Airlines');
insert into airline_info(airline_name) values ('Jet Blue');

insert into airport_info(airport_name) values ('JFK');
insert into airport_info(airport_name) values ('LA');
insert into airport_info(airport_name) values ('MI');
insert into airport_info(airport_name) values ('BOSTON');
insert into airport_info(airport_name) values ('NEWARK');
insert into airport_info(airport_name) values ('GEORGIA');
insert into airport_info(airport_name) values ('ATLANTA');
insert into airport_info(airport_name) values ('NY');


insert into source_info(airport_id) values (1);
insert into source_info(airport_id) values (2);
insert into source_info(airport_id) values (1);

insert into destination_info(airport_id) values (2);
insert into destination_info(airport_id) values (3);
insert into destination_info(airport_id) values (3);

insert into customer_login(cust_username, cust_password, customer_id) values('megatron@email.com', '12345', 1);
insert into customer_login(cust_username, cust_password, customer_id) values('optimus@email.com', '12345', 2);
insert into customer_login(cust_username, cust_password, customer_id) values('iron@email.com', '12345', 3);

insert into airline_flight_info(airline_flight_name, airline_id, fare, flight_max_capacity, flight_current_capacity)
	value ('Boeing 707', 1,	35.0,	60, 0);
insert into airline_flight_info(airline_flight_name, airline_id, fare, flight_max_capacity, flight_current_capacity)
	value ('Boeing 77X', 2,	50.0,	40, 0);
insert into airline_flight_info(airline_flight_name, airline_id, fare, flight_max_capacity, flight_current_capacity)
	value ('Boeing 37E', 2,	65.0,	50, 0);
insert into airline_flight_info(airline_flight_name, airline_id, fare, flight_max_capacity, flight_current_capacity)
	value ('Boeing 505', 2,	75.0,	70, 0);
insert into airline_flight_info(airline_flight_name, airline_id, fare, flight_max_capacity, flight_current_capacity)
	value ('Boeing 77X', 3,	35.0,	30, 0);
insert into airline_flight_info(airline_flight_name, airline_id, fare, flight_max_capacity, flight_current_capacity)
	value ('Boeing 707', 1,	55.0,	25, 0);

insert into airline_admin(airline_id, airline_admin_fname, airline_admin_lname) values (2, 'Hamid', 'UR');
insert into airline_admin(airline_id, airline_admin_fname, airline_admin_lname) values (3, 'Syeed', 'Ah');
insert into airline_admin(airline_id, airline_admin_fname, airline_admin_lname) values (1, 'Rahin', 'arx');

insert into airline_admin_login(airline_admin_id, admin_username, admin_password) values (2,'america1234', 12345);
insert into airline_admin_login(airline_admin_id, admin_username, admin_password) values (3,'jetblue1234', 12345);
insert into airline_admin_login(airline_admin_id, admin_username, admin_password) values (1,'delta1234', 12345);

insert into available_flight(airline_flight_id, available_date) values(4, '2019-12-10');
insert into available_flight(airline_flight_id, available_date) values(6, '2019-12-16');
insert into available_flight(airline_flight_id, available_date) values(3, '2019-12-14');
insert into available_flight(airline_flight_id, available_date) values(1, '2019-12-10');
insert into available_flight(airline_flight_id, available_date) values(2, '2019-12-16');
insert into available_flight(airline_flight_id, available_date) values(5, '2019-12-14');

insert into flight_status(airline_flight_id,flight_status_info) values (5, 'On Time');
insert into flight_status(airline_flight_id,flight_status_info) values (4, 'Cancelled');
insert into flight_status(airline_flight_id,flight_status_info) values (3, 'On Time');
insert into flight_status(airline_flight_id,flight_status_info) values (6, 'On Time');
insert into flight_status(airline_flight_id,flight_status_info) values (2, 'Cancelled');
insert into flight_status(airline_flight_id,flight_status_info) values (1, 'On Time');

insert into reservation_info(customer_id, reservation_by, reservation_date) values(1, '0', '2011-12-03');
insert into reservation_info(customer_id, reservation_by, reservation_date) values(2, '1', '2018-11-03');
insert into reservation_info(customer_id, reservation_by, reservation_date) values(3, '0', '2019-01-03');
insert into reservation_info(customer_id, reservation_by, reservation_date) values(2, '1', '2017-10-03');
insert into reservation_info(customer_id, reservation_by, reservation_date) values(3, '1', '2019-10-03');
insert into reservation_info(customer_id, reservation_by, reservation_date) values(1, '1', '2015-01-03');
insert into reservation_info(customer_id, reservation_by, reservation_date) values(2, '0', '2012-01-03');
insert into reservation_info(customer_id, reservation_by, reservation_date) values(3, '1', '2012-01-03');
insert into reservation_info(customer_id, reservation_by, reservation_date) values(1, '0', '2019-01-03');
insert into reservation_info(customer_id, reservation_by, reservation_date) values(3, '0', '2017-01-03');

insert into arrival_info(airport_id, airline_flight_id, flight_status_id) values(3,5,1);
insert into arrival_info(airport_id, airline_flight_id, flight_status_id) values(2,3,3);
insert into arrival_info(airport_id, airline_flight_id, flight_status_id) values(1,2,5);

insert into departures_info(airport_id, airline_flight_id, flight_status_id) values(2,4,2);
insert into departures_info(airport_id, airline_flight_id, flight_status_id) values(3,6,4);
insert into departures_info(airport_id, airline_flight_id, flight_status_id) values(1,1,6);

insert into flight_info(reservation_id,airline_flight_id,flight_source_date,flight_dest_date,flight_fly_time,flight_land_time,source_name,destination_name)
	values(4,6,'2012-01-03','2012-01-08','08:00:00','12:00:00', 'BOSTON' , 'LA');
insert into flight_info(reservation_id,airline_flight_id,flight_source_date,flight_dest_date,flight_fly_time,flight_land_time,source_name,destination_name)
	values(3,1,'2018-12-04','2018-12-08','03:00:00', '05:00:00','LA' , 'TEXAS');
insert into flight_info(reservation_id,airline_flight_id,flight_source_date,flight_dest_date,flight_fly_time,flight_land_time,source_name,destination_name)
	values(1,2,'2019-09-12','2019-09-18','04:00:00', '11:00:00','JFK' , 'NEWARK');
insert into flight_info(reservation_id,airline_flight_id,flight_source_date,flight_dest_date,flight_fly_time,flight_land_time,source_name,destination_name)
	values(8,3,'2017-12-16','2017-12-08','06:00:00','11:00:00','NEWARK' , 'LGA');
insert into flight_info(reservation_id,airline_flight_id,flight_source_date,flight_dest_date,flight_fly_time,flight_land_time,source_name,destination_name)
	values(9,5,'2019-11-10','2019-11-18','10:00:00', '13:00:00','GEORGIA' , 'BOSTON');
insert into flight_info(reservation_id,airline_flight_id,flight_source_date,flight_dest_date,flight_fly_time,flight_land_time,source_name,destination_name)
	values(4,6,'2015-01-03','2015-01-08','12:00:00', '19:00:00','LA' , 'MI');
insert into flight_info(reservation_id,airline_flight_id,flight_source_date,flight_dest_date,flight_fly_time,flight_land_time,source_name,destination_name)
	values(2,5,'2014-02-01','2014-02-08','23:00:00', '01:00:00','ATLANTA' , 'LA');
insert into flight_info(reservation_id,airline_flight_id,flight_source_date,flight_dest_date,flight_fly_time,flight_land_time,source_name,destination_name)
	values(9,3,'2019-12-04','2019-12-08','22:00:00', '04:00:00','NY' , 'ATLANTA');
insert into flight_info(reservation_id,airline_flight_id,flight_source_date,flight_dest_date,flight_fly_time,flight_land_time,source_name,destination_name)
	values(1,4,'2018-01-06','2018-01-08','08:00:00', '10:00:00','BOSTON' , 'NY');

INSERT INTO reservation_status(reservation_id, res_status) VALUES (1, 'ACTIVE');
INSERT INTO reservation_status(reservation_id, res_status) VALUES (4, 'ACTIVE');
INSERT INTO reservation_status(reservation_id, res_status) VALUES (5, 'ACTIVE');
INSERT INTO reservation_status(reservation_id, res_status) VALUES (6, 'ACTIVE');
INSERT INTO reservation_status(reservation_id, res_status) VALUES (7, 'ACTIVE');
INSERT INTO reservation_status(reservation_id, res_status) VALUES (8, 'CANCELLED');
INSERT INTO reservation_status(reservation_id, res_status) VALUES (10, 'CANCELLED');
INSERT INTO reservation_status(reservation_id, res_status) VALUES (9, 'CANCELLED');
INSERT INTO reservation_status(reservation_id, res_status) VALUES (2, 'CANCELLED');
INSERT INTO reservation_status(reservation_id, res_status) VALUES (3, 'CANCELLED');
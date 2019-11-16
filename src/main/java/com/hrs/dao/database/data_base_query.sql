# 1
select airline_name, airline_flight_name, customer_first_name, airport_name in
(select airport_name
from  airport_info, flight_info, source_info, airline_flight_info, airline_info,  customer_info, reservation_info
where flight_info.source_id = source_info.source_id and 
airport_info.airport_id = source_info.airport_id and
customer_info.customer_id = reservation_info.customer_id and
reservation_info.reservation_id = flight_info.reservation_id and
flight_info.airline_flight_id = airline_flight_info.airline_flight_id and
airline_flight_info.airline_id = airline_info.airline_id
),
airport_name in
(select airport_name
from airport_info, flight_info, destination_info, airline_flight_info, airline_info,  customer_info, reservation_info
where flight_info.destination_id = destination_info.destination_id and 
airport_info.airport_id = destination_info.airport_id and
flight_info.airline_flight_id = airline_flight_info.airline_flight_id and
customer_info.customer_id = reservation_info.customer_id and
reservation_info.reservation_id = flight_info.reservation_id and
flight_info.airline_flight_id = airline_flight_info.airline_flight_id and
airline_flight_info.airline_id = airline_info.airline_id
)

from airline_info, airline_flight_info, customer_info, flight_info, reservation_info, airport_info
where customer_info.customer_id = reservation_info.customer_id and
reservation_info.reservation_id = flight_info.reservation_id and
flight_info.airline_flight_id = airline_flight_info.airline_flight_id and
airline_flight_info.airline_id = airline_info.airline_id


select flight_info.flight_info_id, source_name, destination_name,flight_time, flight_status_info,flight_source_date,flight_dest_date,flight_max_capacity,flight_current_capacity,fare,airline_name,airline_flight_name
from flight_info, airline_info, airline_flight_info, flight_status
where flight_info.airline_flight_id = airline_flight_info.airline_flight_id and
airline_flight_info.airline_id = airline_info.airline_id and
flight_status.airline_flight_id = airline_flight_info.airline_flight_id and
airline_info.airline_name = 'Delta' and
flight_source_date > '2017-01-01'

select flight_info.flight_info_id, airline_flight_info.airline_flight_id,airline_info.airline_id,
source_name, destination_name, flight_status_info,flight_source_date,
flight_dest_date,flight_max_capacity,flight_current_capacity,fare,airline_name,
airline_flight_name, flight_fly_time, flight_land_time
from flight_info, airline_info, airline_flight_info, flight_status
where flight_info.airline_flight_id = airline_flight_info.airline_flight_id and
airline_flight_info.airline_id = airline_info.airline_id and
flight_status.airline_flight_id = airline_flight_info.airline_flight_id and
flight_max_capacity > flight_current_capacity and
flight_status_info = 'On Time' and
flight_source_date > '2019-11-15'


select flight_info.flight_info_id, airline_flight_info.airline_flight_id,airline_info.airline_id,
source_name, destination_name, flight_status_info,flight_source_date,
flight_dest_date,flight_max_capacity,flight_current_capacity,fare,airline_name,
airline_flight_name, flight_fly_time, flight_land_time, res_status, reservation_by, reservation_date
from flight_info, airline_info, airline_flight_info, flight_status, customer_info, reservation_status, reservation_info
where flight_info.airline_flight_id = airline_flight_info.airline_flight_id and
airline_flight_info.airline_id = airline_info.airline_id and
flight_status.airline_flight_id = airline_flight_info.airline_flight_id and
customer_info.customer_id = 1 and
customer_info.customer_id = reservation_info.customer_id and
reservation_info.reservation_id = reservation_status.reservation_id and
reservation_info.reservation_id = flight_info.reservation_id

select customer_info.customer_id, customer_first_name, customer_last_name, customer_email, cust_username, cust_password
from customer_info, customer_login
where customer_info.customer_id = customer_login.customer_id
and cust_username = 'megatron@email.com' and cust_password = '12345'

select flight_info.flight_info_id, airline_flight_info.airline_flight_id,airline_info.airline_id,
source_name, destination_name, flight_status_info,flight_source_date,
flight_dest_date,flight_max_capacity,flight_current_capacity,fare,airline_name,
airline_flight_name, flight_fly_time, flight_land_time, res_status, reservation_by, reservation_date
from flight_info, airline_info, airline_flight_info, flight_status, customer_info, reservation_status, reservation_info
where flight_info.airline_flight_id = airline_flight_info.airline_flight_id and
airline_flight_info.airline_id = airline_info.airline_id and
flight_status.airline_flight_id = airline_flight_info.airline_flight_id and
reservation_info.reservation_id = reservation_status.reservation_id and
reservation_info.reservation_id = flight_info.reservation_id and
reservation_by = 0
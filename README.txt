-- TITLE AND PURPOSE OF APPLICATION --

Title: C195 Software II Scheduling Application

Purpose: Provide a way to add customers and appointments to the company scheduling database. All appointments are checked for scheduling conflicts and multiple reports are available based on user selected criteria.

-- AUTHOR, CONTACT INFORMATION, APPLICATION VERSION, DATE --

Author: Joshua Vinson

Email: JVINS34@WGU.EDU

APPLICATION VERSION: 1.0

Date: 02/13/2022

-- IDE VERSION, JDK VERSION, JAVAFX VERSION --

IDE: IntelliJ IDEA 2021.3 (Community Edition)

JDK: Java SE 17.0.1

JavaFX: JavaFX-SDK-17.0.1

-- DIRECTIONS FOR PROGRAM --

1. Enter a correct user name and password on the login screen. Select Login to login or Exit to close the application.

2. On the MAIN MENU you may
	A) Create a new customer by clicking the ADD NEW CUSTOMER button.
	B) Modify an existing customer by selecting a customer from the customer list and clicking MODIFY CUSTOMER.
	C) Delete a customer from the database by selecting a customer from the customer list and clicking DELETE CUSTOMER. A confirmation box will ask if you are sure you want to delete the selected customer and will proceed with your confirmation.
	D) Add a new appointment for a customer by selecting a customer from the list and clicking ADD APPOINTMENT.
	E) Modify an exisiting customer appointment by selecting an appointment from the appointment list and clickiing MODIFY APPOINTMENT.
	F) Delete an appointment from the database by selecting an appointment from the appointment list and clicking DELETE APPOINTMENT. A confirmation box will ask if you are sure you want to delete the selected appointment and will proceed with your confirmation.
	G) Sort the APPOINTMENTS table by ALL, by CURRENT MONTH, or by CURRENT WEEK.
	H) Check various reports by clicking the REPORTS button.

3. When ADDING a CUSTOMER, you must enter a Name, an Address, a Country, a State/Province, a Postal Code, and a Phone Number. A unique Customer ID will be generated for you. Click SAVE to add the Customer to the database or CANCEL to exit the Customer screen.

4. When MODIFYING a CUSTOMER, you may change any of the fields except for the Customer ID as this is unique and must stay the same. Click SAVE to update the customer in the database or CANCEL to exit the Customer menu with no changes.

5. When ADDING an APPOINTMENT you must enter your User ID, a Contact ID, a Title, a Type, a Description, a Start Date, a Start Time, an End Date, an End Time, and a Location. The Customer ID will be populated for you.
	A) The description must be less than 50 characters.
	B) The start date and time must be in the future and come before the end date and time.
	C) Customer scheduling conflicts will be checked for you. You cannot save a scheduling conflict.
6. When MODIFYING an APPOINTMENT you may change any of the fields except for the Appointment ID as this is unique and must stay the same. Click SAVE to update the appointment in the database or CANCEL to exit the Appointment menu with no changes.

7. The REPORTS menu allows you to check the total number of DIVISIONS the company currently services per Country, the total number of Appointments by MONTH and TYPE, and the the Appointments assigned to each Contact. Use the Combo Boxes above each table to filter your selection.

-- EXTRA REPORT IN A3F --
The report shows a list of all divisions that the company currently operates in and is filtered by country. You can see the total divisions by country and the tableview shows the name of the division and when it was added to the companies service area.

-- MYSQL CONNECTOR DRIVER VERSION --

Driver Version: mysql-connector-java-8.0.25

-- END OF README --

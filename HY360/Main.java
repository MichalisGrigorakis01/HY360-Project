import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.sql.*;
import java.util.Date;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static Connection con;

    private static java.util.Date calculateEstimatedDate(java.util.Date startDate, int duration) {
        // Προσθήκη του αριθμού των ημερών στην αρχική ημερομηνία
        return new java.util.Date(startDate.getTime() + duration * 24L * 3600L * 1000L);
    }
    static void ReturnRentedVehicle(String RegistrationNumber) throws SQLException, ParseException {
        long ExtraPayment=0;
//DATE NOW
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date currentDate = new java.util.Date();
        String dateNow = formatter.format(currentDate);
//DATE NOW
        String estimatedDateString="0000-01-18 20:49:01";

//DATE ENOIKIASEIS + DURATION
        String selectQuery = "SELECT duration, Date FROM rental WHERE RegistrationNumber = ?";

        PreparedStatement preparedStatement = con.prepareStatement(selectQuery);
        preparedStatement.setString(1,RegistrationNumber);
        ResultSet resultSet = preparedStatement.executeQuery();


        // Επεξεργασία των αποτελεσμάτων
        if (resultSet.next()) {
            int duration = resultSet.getInt("duration");
            java.util.Date Date = resultSet.getTimestamp("Date");
            java.util.Date estimatedDate = calculateEstimatedDate(Date, duration);
            estimatedDateString = formatter.format(estimatedDate);

            // Εκτύπωση του αποτελέσματος
            System.out.println("Εκτιμώμενη ημερομηνία: " + estimatedDateString);
            System.out.println("Ημερομηνια οντως παραδωσης"+dateNow);
        } else {
            System.out.println("Δεν βρέθηκε εγγραφή με το συγκεκριμένο Register number.");
        }
//DATE ENOIKIASEIS + DURATION
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        java.util.Date dateNow1 = dateFormat.parse(dateNow);
        Date estimatedDate1 = dateFormat.parse(estimatedDateString);

        long differenceInMillis = dateNow1.getTime() - estimatedDate1.getTime() ;
        long hours = differenceInMillis / (60 * 60 * 1000);


        String deleteQuery = "UPDATE rental SET returned=1 WHERE RegistrationNumber = ?";
        String UpdateAvailabilityQuery = "UPDATE vehicle SET Availability=? WHERE RegistrationNumber = ?";
        PreparedStatement preparedStatement1 = con.prepareStatement(UpdateAvailabilityQuery);
        preparedStatement1.setBoolean(1, true);
        preparedStatement1.setString(2, RegistrationNumber);
        PreparedStatement preparedStatement2 = con.prepareStatement(deleteQuery);
        preparedStatement2.setString(1, RegistrationNumber);

        int rowsAffected1= preparedStatement1.executeUpdate();
        int rowsAffected = preparedStatement2.executeUpdate();


        if (rowsAffected > 0 && rowsAffected1 > 0 ) {
            if(hours>0){
                ExtraPayment = 40*hours;
                System.out.println();
                System.out.println("καθυστερησατε:"+hours+" θα χρειαστει να πληρωσετε εξτρα 40*"+hours+"ωρες για καθε ωρα αργοπορειας παραδωσης.Συνολικο εξτρα κοστος ="+ExtraPayment);
            }else{
                System.out.println("Επιτυχής διαγραφή ενοικίασης.Εγκυρη ωρα επιστροφης!");

            }
        } else {
            System.out.println("Δεν βρέθηκε εγγραφή για διαγραφή.");
        }
    }

    static void RegisterVehicle(String brand,String model,String color,int autonomy,String RegistrationNumber , String VehicleType,int Passengers,int InsuranceCost,int DailyRental,boolean Availability) throws SQLException {
        String query = "INSERT INTO vehicle (brand,model,color,autonomy,RegistrationNumber,VehicleType,Passengers,InsuranceCost,DailyRental,Availability) VALUES(?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = con.prepareStatement(query);
        preparedStatement.setString(1,brand);
        preparedStatement.setString(2,model);
        preparedStatement.setString(3,color);
        preparedStatement.setInt(4,autonomy);
        preparedStatement.setString(5,RegistrationNumber);
        preparedStatement.setString(6,VehicleType);
        preparedStatement.setInt(7,Passengers);
        preparedStatement.setInt(8,InsuranceCost);
        preparedStatement.setInt(9,DailyRental);
        preparedStatement.setBoolean(10,Availability);

        int rowsAffected = preparedStatement.executeUpdate();
        System.out.println(rowsAffected);

        if(rowsAffected>0){
            System.out.println("epityxhs eggrafh oxhmatos");
        }else{
            System.out.println("apotuxia eggrafhs oxhmatos");
        }
    }
    static void RegisterCustomer(String Name,String Address,String Date,String LicenseNumber,String CreditCard_Id) throws SQLException {
        String query = "INSERT INTO customer (Name,Address,Date,LicenseNumber,CreditCard_Id) VALUES(?,?,?,?,?)";
        PreparedStatement preparedStatement = con.prepareStatement(query);
        preparedStatement.setString(1,Name);
        preparedStatement.setString(2,Address);
        preparedStatement.setString(3,Date);
        preparedStatement.setString(4,LicenseNumber);
        preparedStatement.setString(5,CreditCard_Id);

        int rowsAffected = preparedStatement.executeUpdate();

        if(rowsAffected>0){
            System.out.println("epityxhs eggrafh");
        }else{
            System.out.println("apotuxia eggrafhs");
        }
    }
    public static void Report_Vehicle_Damage(String RegistrationNumber)throws SQLException{
        String query = "SELECT Rental_Id FROM rental WHERE RegistrationNumber=?";
        PreparedStatement stm = con.prepareStatement(query);
        stm.setString(1,RegistrationNumber);
        ResultSet res = stm.executeQuery();
        if(!res.next()){
            System.out.println("Vehicle not rented");
            return;
        }
        int Rental_Id=res.getInt("Rental_Id");
        Repair_Vehicle(RegistrationNumber,"Repair");
        String Type_from_vehicle = "SELECT VehicleType FROM vehicle WHERE RegistrationNumber=?";
        stm = con.prepareStatement(Type_from_vehicle);
        stm.setString(1,RegistrationNumber);
        res=stm.executeQuery();
        res.next();
        String VehicleType=res.getString("VehicleType");
        query="SELECT RegistrationNumber FROM vehicle WHERE VehicleType=? AND Availability=1";
        stm = con.prepareStatement(query);
        stm.setString(1,VehicleType);
        res=stm.executeQuery();
        if(!res.next()){
            System.out.println("No available vehicle for replacement,refunding");
            query="UPDATE rental SET returned=1 WHERE Rental_Id=?";
            stm = con.prepareStatement(query);
            stm.setInt(1,Rental_Id);
            stm.executeUpdate();
            return;
        }
        String new_reg=res.getString("RegistrationNumber");
        query="UPDATE rental SET RegistrationNumber=? WHERE Rental_Id=?";
        stm=con.prepareStatement(query);
        stm.setString(1,new_reg);
        stm.setInt(2,Rental_Id);
        stm.executeUpdate();

        query="UPDATE vehicle SET Availability=? WHERE RegistrationNumber=?";
        stm=con.prepareStatement(query);
        stm.setInt(1,0);
        stm.setString(2,new_reg);
        stm.executeUpdate();
    }
    public static void ReportAccident(String RegistrationNumber) throws SQLException{
        Statement stm = con.createStatement();
        String vehicle_from_rental = "SELECT * FROM rental";
        ResultSet res = stm.executeQuery(vehicle_from_rental);

        while(res.next()){
            if(RegistrationNumber.equals(res.getString("RegistrationNumber"))){
                if(res.getInt("InsurancePaid")==1){
                    Report_Vehicle_Damage(RegistrationNumber);
                }else{
                    String Update_Payment = "UPDATE rental SET Payment=? WHERE RegistrationNumber = ?";
                    PreparedStatement preparedStatement1 = con.prepareStatement(Update_Payment);
                    preparedStatement1.setInt(1,res.getInt("Payment") * 3);
                    preparedStatement1.setString(2,res.getString("RegistrationNumber"));
                    int rowsAffected1 = preparedStatement1.executeUpdate();
                    Report_Vehicle_Damage(RegistrationNumber);
                }
            }
        }
    }
    public static void Repair_Vehicle(String RegistrationNumber,String Fix) throws SQLException {
        String vehicle_from_vehicles = "UPDATE vehicle SET Availability=0,RepairDays=? WHERE RegistrationNumber=?";
        PreparedStatement stm = con.prepareStatement(vehicle_from_vehicles);
        int RepairDays=0;
        if(Fix.equals("Maintenance")){
            RepairDays=1;
        }else if(Fix.equals("Repair")){
            RepairDays=3;
        }
        stm.setInt(1,RepairDays);
        stm.setString(2,RegistrationNumber);
        int rows=stm.executeUpdate();
        if(rows==0){
            System.out.println("Vehicle does not exist");
        }
    }

    public static void Search_Available_Vehicles() throws SQLException {
        String TypeOfVehicle;
        String brand;
        String RegistrationNumber;
        String model;
        Statement stm = con.createStatement();
        String query = "SELECT * FROM vehicle WHERE Availability=1 ORDER BY VehicleType";
        ResultSet res = stm.executeQuery(query);

        while(res.next()) {
            brand = res.getString("brand");
            TypeOfVehicle = res.getString("VehicleType");
            RegistrationNumber=res.getString("RegistrationNumber");
            model=res.getString("model");
            System.out.println("VEHICLE TYPE:"+TypeOfVehicle + " CAR:" + brand+" "+model+" REGISTRATION NUMBER: "+RegistrationNumber);
            }
        }


    public static void Rent(String RegistrationNumber,String CreditCard_Id,int Duration,int InsurancePaid) throws SQLException{
        String query="SELECT model,VehicleType,InsuranceCost,DailyRental,Rented FROM vehicle WHERE RegistrationNumber=? AND Availability=1";
        PreparedStatement stmt= con.prepareStatement(query);
        stmt.setString(1,RegistrationNumber);
        ResultSet res=stmt.executeQuery();
        if (!res.next() ) {
            System.out.println("Vehicle not available");
            return;
        }
        String model=res.getString("model");
        String type=res.getString("VehicleType");
        int insurance=res.getInt("InsuranceCost");
        int daily=res.getInt("DailyRental");
        int age;
        if(type.equals("BICYCLE") ||type.equals("SCOOTER") ){
            age=16*365;
        }else{
            age=18*365;
        }
        int counter=res.getInt("Rented");
        query="SELECT Id_User,LicenseNumber FROM customer WHERE CreditCard_Id=? AND DATEDIFF(CURDATE(),Date)>?";
        stmt= con.prepareStatement(query);
        stmt.setString(1,CreditCard_Id);
        stmt.setInt(2,age);
        res=stmt.executeQuery();
        if (!res.next() ) {
            System.out.println("valid user does not exist");
            return;
        }
        int Id_User=res.getInt("Id_User");
        String licence=res.getString("LicenseNumber");
        if(age==18*365 && licence.equals("")){
            System.out.println("Cant rent without licence");
            return;
        }

        query="INSERT INTO rental(Id_User,Date,Duration,Payment,RegistrationNumber,VehicleType,InsurancePaid) VALUES(?,NOW(),?,?,?,?,?)";
        stmt= con.prepareStatement(query);
        stmt.setInt(1,Id_User);
        stmt.setInt(2,Duration);
        int final_amount;
        if(InsurancePaid==1){
            final_amount=insurance+Duration*daily;
        }else{
            final_amount=Duration*daily;
        }
        stmt.setInt(3,final_amount);
        stmt.setString(4,RegistrationNumber);
        stmt.setString(5,type);
        stmt.setInt(6,InsurancePaid);
        stmt.executeUpdate();
        query="UPDATE vehicle SET Availability=0,rented=? WHERE RegistrationNumber=?";
        stmt= con.prepareStatement(query);
        stmt.setInt(1,++counter);
        stmt.setString(2,RegistrationNumber);
        stmt.executeUpdate();
    }

    public static void minmaxaverage() throws SQLException {
        ArrayList<Integer> SUV = new ArrayList<Integer>();
        ArrayList<Integer> CABRIO = new ArrayList<Integer>();
        ArrayList<Integer> SMART = new ArrayList<Integer>();
        ArrayList<Integer> MOTORCYCLE = new ArrayList<Integer>();
        ArrayList<Integer> BICYCLE = new ArrayList<Integer>();
        ArrayList<Integer> SCOOTER = new ArrayList<Integer>();
        String query="SELECT * FROM Rental WHERE VehicleType=?";
        PreparedStatement stmt=con.prepareStatement(query);
        stmt.setString(1,"SUV");
        ResultSet res=stmt.executeQuery();
        while(res.next()){
            SUV.add(res.getInt("Duration"));
        }
        int min,max;
        double average;
        if(!SUV.isEmpty()){
             min= Collections.min(SUV);
             max= Collections.max(SUV);
             average = SUV.stream().mapToInt(val -> val).average().orElse(0.0);
            System.out.println("SUV MIN:"+min+" SUV MAX:"+max+" SUV AVERAGE:"+average);
        }


        query="SELECT * FROM Rental WHERE VehicleType=?";
        stmt=con.prepareStatement(query);
        stmt.setString(1,"CABRIO");
         res=stmt.executeQuery();
        while(res.next()){
            CABRIO.add(res.getInt("Duration"));
        }
        if(!CABRIO.isEmpty()){
            min= Collections.min(CABRIO);
            max= Collections.max(CABRIO);
            average = CABRIO.stream().mapToInt(val -> val).average().orElse(0.0);
            System.out.println("CABRIO MIN:"+min+" CABRIO MAX:"+max+" CABRIO AVERAGE:"+average);
        }


        query="SELECT * FROM Rental WHERE VehicleType=?";
        stmt=con.prepareStatement(query);
        stmt.setString(1,"SMART");
        res=stmt.executeQuery();
        while(res.next()){
            SMART.add(res.getInt("Duration"));
        }
        if(!SMART.isEmpty()){
            min= Collections.min(SMART);
            max= Collections.max(SMART);
            average = SMART.stream().mapToInt(val -> val).average().orElse(0.0);
            System.out.println("SMART MIN:"+min+" SMART MAX:"+max+" SMART AVERAGE:"+average);
        }


        query="SELECT * FROM Rental WHERE VehicleType=?";
        stmt=con.prepareStatement(query);
        stmt.setString(1,"MOTORCYCLE");
        res=stmt.executeQuery();
        while(res.next()){
            MOTORCYCLE.add(res.getInt("Duration"));
        }
        if(!MOTORCYCLE.isEmpty()){
            min= Collections.min(MOTORCYCLE);
            max= Collections.max(MOTORCYCLE);
            average = MOTORCYCLE.stream().mapToInt(val -> val).average().orElse(0.0);
            System.out.println("MOTORCYCLE MIN:"+min+" MOTORCYCLE MAX:"+max+" MOTORCYCLE AVERAGE:"+average);
        }


        query="SELECT * FROM Rental WHERE VehicleType=?";
        stmt=con.prepareStatement(query);
        stmt.setString(1,"BICYCLE");
        res=stmt.executeQuery();
        while(res.next()){
            BICYCLE.add(res.getInt("Duration"));
        }
        if(!BICYCLE.isEmpty()){
            min= Collections.min(BICYCLE);
            max= Collections.max(BICYCLE);
            average = BICYCLE.stream().mapToInt(val -> val).average().orElse(0.0);
            System.out.println("BICYCLE MIN:"+min+" BICYCLE MAX:"+max+" BICYCLE AVERAGE:"+average);
        }


        query="SELECT * FROM Rental WHERE VehicleType=?";
        stmt=con.prepareStatement(query);
        stmt.setString(1,"SCOOTER");
        res=stmt.executeQuery();
        while(res.next()){
            SCOOTER.add(res.getInt("Duration"));
        }
        if(!SCOOTER.isEmpty()){
            min= Collections.min(SCOOTER);
            max= Collections.max(SCOOTER);
            average = SCOOTER.stream().mapToInt(val -> val).average().orElse(0.0);
            System.out.println("SCOOTER MIN:"+min+" SCOOTER MAX:"+max+" SCOOTER AVERAGE:"+average);
        }

    }

    public static void monies() throws SQLException {
        String query="SELECT SUM(Payment) FROM Rental WHERE VehicleType='SUV'";
        Statement stmt=con.createStatement();
        ResultSet res=stmt.executeQuery(query);
        res.next();
        int SUV_TOTAL=res.getInt("SUM(Payment)");

         query="SELECT SUM(Payment) FROM Rental WHERE VehicleType='CABRIO'";
         stmt=con.createStatement();
         res=stmt.executeQuery(query);
        res.next();
        int CABRIO_TOTAL=res.getInt("SUM(Payment)");


         query="SELECT SUM(Payment) FROM Rental WHERE VehicleType='SMART'";
         stmt=con.createStatement();
         res=stmt.executeQuery(query);
        res.next();
        int SMART_TOTAL=res.getInt("SUM(Payment)");


         query="SELECT SUM(Payment) FROM Rental WHERE VehicleType='MOTORCYCLE'";
         stmt=con.createStatement();
         res=stmt.executeQuery(query);
        res.next();
        int MOTORCYCLE_TOTAL=res.getInt("SUM(Payment)");


         query="SELECT SUM(Payment) FROM Rental WHERE VehicleType='BICYCLE'";
         stmt=con.createStatement();
         res=stmt.executeQuery(query);
        res.next();
        int BICYCLE_TOTAL=res.getInt("SUM(Payment)");

        query="SELECT SUM(Payment) FROM Rental WHERE VehicleType='SCOOTER'";
        stmt=con.createStatement();
        res=stmt.executeQuery(query);
        res.next();
        int SCOOTER_TOTAL=res.getInt("SUM(Payment)");


        System.out.println("SUV SUM:"+SUV_TOTAL+" CABRIO SUM:"+CABRIO_TOTAL+" SMART SUM:"+SMART_TOTAL+" MOTORCYCLE SUM:"+MOTORCYCLE_TOTAL+" BICYCLE SUM:"+BICYCLE_TOTAL+" SCOOTER SUM:"+SCOOTER_TOTAL);
    }

    public static void repair_cost() throws SQLException {
        String query="SELECT SUM(RepairDays) FROM Vehicle";
        Statement stmt=con.createStatement();
        ResultSet res=stmt.executeQuery(query);
        res.next();
        int no=res.getInt("SUM(RepairDays)");
        System.out.println("Total cost of repair/maintenance:"+no*30);
    }

    public static void popular() throws SQLException{
        String query="SELECT brand,model,Rented FROM Vehicle WHERE VehicleType='SUV' HAVING Rented=MAX(Rented)";
        Statement stmt=con.createStatement();
        ResultSet res=stmt.executeQuery(query);
        res.next();
        String no=res.getString("BRAND")+" "+res.getString("model");
        System.out.println("MOST POPULAR SUV:"+no);

        query="SELECT brand,model,Rented FROM Vehicle WHERE VehicleType='CABRIO' HAVING Rented=MAX(Rented)";
        stmt=con.createStatement();
        res=stmt.executeQuery(query);
        res.next();
        no=res.getString("BRAND")+" "+res.getString("model");
        System.out.println("MOST POPULAR CABRIO:"+no);

        query="SELECT brand,model,Rented FROM Vehicle WHERE VehicleType='SMART' HAVING Rented=MAX(Rented)";
        stmt=con.createStatement();
        res=stmt.executeQuery(query);
        res.next();
        no=res.getString("BRAND")+" "+res.getString("model");
        System.out.println("MOST POPULAR SMART:"+no);

        query="SELECT brand,model,Rented FROM Vehicle WHERE VehicleType='MOTORCYCLE' HAVING Rented=MAX(Rented)";
        stmt=con.createStatement();
        res=stmt.executeQuery(query);
        res.next();
        no=res.getString("BRAND")+" "+res.getString("model");
        System.out.println("MOST POPULAR MOTORCYCLE:"+no);

        query="SELECT brand,model,Rented FROM Vehicle WHERE VehicleType='BICYCLE' HAVING Rented=MAX(Rented)";
        stmt=con.createStatement();
        res=stmt.executeQuery(query);
        res.next();
        no=res.getString("BRAND")+" "+res.getString("model");
        System.out.println("MOST POPULAR BICYCLE:"+no);

        query="SELECT brand,model,Rented FROM Vehicle WHERE VehicleType='SCOOTER' HAVING Rented=MAX(Rented)";
        stmt=con.createStatement();
        res=stmt.executeQuery(query);
        res.next();
        no=res.getString("BRAND")+" "+res.getString("model");
        System.out.println("MOST POPULAR SCOOTER:"+no);


    }

    public static void main(String[] args) throws SQLException, ParseException {
        String q="";
        Scanner scanner = new Scanner(System.in);
        String url = "jdbc:mysql://localhost";
        String databaseName = "360";
        int port = 3306;
        String username = "root";
        String password = "";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        con = DriverManager.getConnection(url + ":" + port + "/" + databaseName + "?characterEncoding=UTF-8", username, password);
        System.out.println("What would you like to do?");
        while(!q.equals("quit")){
            System.out.println("Available commands: \n register : signup user \n vehicle : make new vehicle available \n available : show available vehicles \n rent : rent car \n return : return vehicle \n damaged : replace damaged car" +
                    "\n repair : return a vehicle to be repaired or maintenanced \n quit: exit the program \n popular: check the most popular cars by type \n minmaxaverage: check rent stats about different types \n repaircost: chekc the repair cost of vehicles" +
                    "\n monies: check the monies by type");
            q="quit";
            q=scanner.nextLine();
            if(q.equals("register")){
                System.out.println("Enter your name:");
                String name=scanner.nextLine();
                System.out.println("Enter your address:");
                String address=scanner.nextLine();
                System.out.println("Enter your date of birth (YYYY-MM-DD format)");
                String date=scanner.nextLine();
                System.out.println("Enter your license number(if not available press enter):");
                String licence=scanner.nextLine();
                System.out.println("Enter your credit card number:");
                String creditcard=scanner.nextLine();
                RegisterCustomer(name,address,date,licence,creditcard);
                System.out.println("Congratulations! Anything else you would like us to help with?");
            }else if(q.equals("vehicle")){
                System.out.println("Enter the vehicles brand:");
                String brand=scanner.nextLine();
                System.out.println("Enter the vehicles model:");
                String model=scanner.nextLine();
                System.out.println("Enter the vehicles colour:");
                String color=scanner.nextLine();
                System.out.println("Enter the number of passengers:");
                int passengers= scanner.nextInt();
                scanner.nextLine();
                System.out.println("Enter the cost of insurance:");
                int insurance= scanner.nextInt();
                scanner.nextLine();
                System.out.println("How long does the vehicle last?:");
                int autonomy= scanner.nextInt();
                scanner.nextLine();
                String registrationnumber;
                int dailyrental;
                System.out.println("Enter the Vehicles type(SUV,SMART,CABRIO,MOTORCYCLE,BICYCLE,SCOOTER):");
                String VehicleType=scanner.nextLine();
                while(true){
                    if(VehicleType.equals("SUV")){
                        System.out.println("Enter the vehicles registration number:");
                        registrationnumber=scanner.nextLine();
                        dailyrental=200;
                        break;

                    }else if(VehicleType.equals("SMART")){
                        System.out.println("Enter the vehicles registration number:");
                        registrationnumber=scanner.nextLine();
                        dailyrental=100;
                        break;
                    }else if(VehicleType.equals("CABRIO")){
                        System.out.println("Enter the vehicles registration number:");
                        registrationnumber=scanner.nextLine();
                        dailyrental=300;
                        break;
                    }else if(VehicleType.equals("MOTORCYCLE")){
                        System.out.println("Enter the vehicles registration number:");
                        registrationnumber=scanner.nextLine();
                        dailyrental=150;
                        break;
                    }else if(VehicleType.equals("BICYCLE")){
                        UUID uniqueKey = UUID.randomUUID();
                        registrationnumber=uniqueKey.toString();
                        dailyrental=50;
                        break;
                    }else if(VehicleType.equals("SCOOTER")){
                        UUID uniqueKey = UUID.randomUUID();
                        registrationnumber=uniqueKey.toString();
                        dailyrental=25;
                        break;
                    }else{
                        System.out.println("Enter valid category.");
                        VehicleType=scanner.nextLine();
                    }
                }

                RegisterVehicle(brand,model,color,autonomy,registrationnumber ,VehicleType,passengers,insurance,dailyrental,true);
                System.out.println("Congratulations! Anything else you would like us to help with?");
            }else if(q.equals("available")){
                Search_Available_Vehicles();
            }else if(q.equals("rent")){
                System.out.println("Enter the RegistrationNumber of the car you want to order");
                String registrationnumber=scanner.nextLine();
                System.out.println("Enter your credit card id:");
                String credit_card=scanner.nextLine();
                System.out.println("For how long would you like to rent it?:");
                int duration=scanner.nextInt();
                scanner.nextLine();
                System.out.println("Will you pay the insurance(1,0)?:");
                int insurance=scanner.nextInt();
                scanner.nextLine();
                Rent(registrationnumber,credit_card,duration,insurance);
            }else if(q.equals("return")){
                System.out.println("Enter the RegistrationNumber of the car you want to return:");
                String reg_number=scanner.nextLine();
                ReturnRentedVehicle(reg_number);
            }else if(q.equals("damaged")){
                System.out.println("Which rented vehicle is damaged?:");
                String reg_number=scanner.nextLine();
                ReportAccident(reg_number);
            }else if(q.equals("repair")){
                System.out.println("Enter the RegistrationNumber of the car you want to send to the car shop:");
                String reg_number=scanner.nextLine();
                System.out.println("Maintenance or repairs?(1,0):");
                int repair=scanner.nextInt();
                scanner.nextLine();
                if(repair==1){
                    Repair_Vehicle(reg_number,"Maintenance");
                }else{
                    Repair_Vehicle(reg_number,"Repair");
                }
            }else if(q.equals("popular")){
                popular();
            }else if(q.equals("minmaxaverage")){
                minmaxaverage();
            }else if(q.equals("repaircost")){
                repair_cost();
            }else if(q.equals("monies")){
                monies();
            }
        }
    }
}
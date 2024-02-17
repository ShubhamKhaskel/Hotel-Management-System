import java.sql.*;
import java.util.*;

public class HotelReservationSystem {

    private static final String url="jdbc:mysql://localhost:3307/hotel_db";
    private static final String username="root";
    private static final String password="root";

    public static void main(String[] args) throws Exception
    {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); //loading as the jdbc driver forname loads all the drivers
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            while (true) {
                System.out.println();
                System.out.println("HOTEL MANAGEMENT SYSTEM");
                System.out.println("1: Reserve a room");
                System.out.println("2: View a Reservation");
                System.out.println("3: Get room number");
                System.out.println("4: Update reservations");
                System.out.println("5: Delete Reservation");
                System.out.println("0: Exit");
                System.out.println("Choose an option:");
                Scanner sc = new Scanner(System.in);
                int choice = sc.nextInt();

                switch (choice) {
                    case 1:
                        reserveRoom(connection);
                        break;
                    case 2:
                        viewReservations(connection);
                        break;
                    case 3:
                        getRoomNumber(connection);
                        break;
                    case 4:
                        updateReservation(connection);
                        break;
                    case 5:
                        deleteReservation(connection);
                        break;
                    case 0:
                        exit();
                        break;

                }


            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void reserveRoom(Connection connection)
    {
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter guest name");
        String guestName=sc.next();
        System.out.println("Enter room number");
        int roomNumber=sc.nextInt();
        System.out.println("Enter contact number");
        String contactNumber=sc.next();

        String sql="insert into reservations(guest_name,room_number,contact_number)"+
                "values('"+guestName+"','"+roomNumber+"','"+contactNumber+"')";

        try(Statement statement=connection.createStatement()) //java teh sql run korle gele statement interface lage
        {
            int affectedRows=statement.executeUpdate(sql);

            if(affectedRows>0)
            {
                System.out.println("Reservation successful");
            }
            else
            {
                System.out.println("Reservation unsuccessful!!!!");
            }
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }



    }

    public static void viewReservations(Connection connection)
    {
        Scanner sc=new Scanner(System.in);

        String sql="select reservation_id,guest_name,room_number,contact_number,"+
                "reservation_date from reservations";

        try {
            Statement statement=connection.createStatement();
            ResultSet resultSet=statement.executeQuery(sql);

            System.out.println("Current Reservations:");
            System.out.println("+----------------+------------+-------------+------------+-------------------------");
            System.out.println("+ Reservation ID + Guest      + Room Number + Contact No + Date      ");
            System.out.println("+----------------+------------+-------------+------------+-------------------------");

            while(resultSet.next())
            {
                int r_id=resultSet.getInt("reservation_id");
                String name=resultSet.getString("guest_name");
                int roomNumber=resultSet.getInt("room_number");
                String cont=resultSet.getString("contact_number");
                String resdate=resultSet.getTimestamp("reservation_date").toString();

                System.out.println("     "+r_id+"            "+name+"       "+roomNumber+"          "+cont+"       "+resdate);
                System.out.println("+----------------+------------+-------------+------------+--------------------");

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    private static void getRoomNumber(Connection connection)
    {
        Scanner sc=new Scanner(System.in);
        try
        {
            System.out.println("Enter reservation id:-");
            int id=sc.nextInt();
            System.out.println("Enter Guest Name:-");
            String name=sc.next();


            String sql="select room_number from hotel_db.reservations"+"where reservation_id="+id
                    +" and guest_name='"+name+"'";

           // System.out.println("hellloo");

            try(Statement statement=connection.createStatement();
                ResultSet resultSet=statement.executeQuery(sql)){

                System.out.println("helooooooooooooo");

                if(resultSet.next())
                {
                    int rn=resultSet.getInt("room_number");
                    System.out.println("Room number of "+name+" is "+rn);
                }
                else
                {
                    System.out.println("Room number not found!!!!");
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


        }
        catch(Exception e)
        {

        }



    }

    private static  void updateReservation(Connection connection)
    {
        try
        {
            Scanner sc=new Scanner(System.in);
            System.out.println("Enter reservation ID to update");
            int id=sc.nextInt();

            if(!reservationExists(connection,id))
            {
                System.out.println("Reservation not found!!!");
                return;
            }
            else
            {
                System.out.println("Enter new guest name");
                String name=sc.nextLine();
                System.out.println("Enter new room number");
                int no=sc.nextInt();
                System.out.println("Enter new contact number");
                String cno=sc.next();

                String sql="update reservations set guest_name ='"+name+"',room_number="
                        +no+",contact_number='"+cno+"'where reservation_id="+id;

                try(Statement statement=connection.createStatement()) //java teh sql run korle gele statement interface lage
                {
                    int affectedRows=statement.executeUpdate(sql);

                    if(affectedRows>0)
                    {
                        System.out.println("Reservation updated successful");
                    }
                    else
                    {
                        System.out.println("unsuccessful!!!!");
                    }
                }
                catch(Exception e)
                {
                    System.out.println(e.getMessage());
                }
            }
        }
        catch(Exception e)
        {

        }
    }

    private static boolean reservationExists(Connection connection,int r) {

        try
        {
        String sql="select reservation_id from reservations where reservation_id="+r;


        try {
            Statement statement=connection.createStatement();
            ResultSet resultSet=statement.executeQuery(sql);

            return resultSet.next(); //return boolean if set has something

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteReservation(Connection connection)
    {
        try
        {
            Scanner sc=new Scanner(System.in);
            System.out.println("Enter reservation ID to delete:");
            int id=sc.nextInt();

            if(!reservationExists(connection,id))
            {
                System.out.println("Reservation not found!!!");
                return;
            }

            String sql="delete from reservations where reservation_id="+id;

            try(Statement statement=connection.createStatement()) //java teh sql run korle gele statement interface lage
            {
                int affectedRows=statement.executeUpdate(sql);

                if(affectedRows>0)
                {
                    System.out.println("Reservation deleted successfully");
                }
                else
                {
                    System.out.println("unsuccessful!!!!");
                }
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
            }
        }
        catch (Exception e)
        {

        }
    }

    public static void exit()
    {
        System.out.println("Exiting system");
        int i=5;
        while(i!=0)
        {
            System.out.print(".");
            try {
                Thread.sleep(450);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            i--;
        }
        System.out.println();
        System.out.println("THANK YOU!!!");
    }


}



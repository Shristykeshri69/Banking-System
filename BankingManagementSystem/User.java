package BankingManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {
    private Connection con;
    private Scanner sc;
    public User(Connection con,Scanner sc){
        this.con=con;
        this.sc=sc;
    }

    public void register(){
        Scanner sc=new Scanner(System.in);
        System.out.print("Full name");
        String full_name=sc.nextLine();
        System.out.print("Email");
        String email=sc.nextLine();
        System.out.println("password");
        String password=sc.nextLine();
        if(User_exist(email)){
            System.out.println("user already exists for this email address!!");

        }
        String register_query="INSERT INTO USER(full_name,email,password) VALUES(?,?,?)";
        try{
            PreparedStatement preparedStatement=con.prepareStatement(register_query);
            preparedStatement.setString(1,full_name);
            preparedStatement.setString(2,email);
            preparedStatement.setString(3,password);
            int rowsaffected=preparedStatement.executeUpdate();
            if(rowsaffected>0){
                System.out.println("registration successfully done");
            }else {
                System.out.println("Registration failed");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

   public String login(){
        System.out.println("email");
        String email=sc.nextLine();
        System.out.println("password");
        String password=sc.nextLine();
        String loginquery="SELECT * FROM USER WHERE email = ? and password =?";
       try
        {
            PreparedStatement preparedStatement=con.prepareStatement(loginquery);
            preparedStatement.setString(1,email);
            preparedStatement.setString(2,password);
            ResultSet resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                return email;

            }
            else{
                return null;
            }

       }
       catch(SQLException e){
           e.printStackTrace();
       }
       return null;
   }


   public boolean User_exist(String email){
        String query="select * from user WHERE email=?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}

package BankingManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Account_Manager {
    private Connection con;
    private Scanner sc;
      public Account_Manager(Connection con,Scanner sc){
         this.con=con;
         this.sc=sc;
     }


    public void credit_money(Long account_number)throws  SQLException{
        System.out.println("Enter amount:-");
        Scanner sc=new Scanner(System.in);
        double amount=sc.nextDouble();
        System.out.println("Enter security_pin");
        String securitypin=sc.nextLine();

        try{
            con.setAutoCommit(false);
            if(account_number!=0){
                PreparedStatement preparedStatement=con.prepareStatement("Select * from account_number=? and security_number=?");
                preparedStatement.setLong(1,account_number);
                preparedStatement.setString(2,securitypin);
                ResultSet resultSet=preparedStatement.executeQuery();
                if(resultSet.next()){

                        String credit_query="Update accounts set balance=balance+? where account_number=?";
                        PreparedStatement preparedStatement1=con.prepareStatement(credit_query);
                        preparedStatement1.setDouble(1,amount);
                        preparedStatement1.setLong(2,account_number);
                        int rowsaffected = preparedStatement1.executeUpdate();
                        if(rowsaffected >0){
                            System.out.println("Rs."+amount+"credited successfully..");
                            con.commit();
                            con.setAutoCommit(true);
                            return;

                        }else{
                            System.out.println("Transaction failed!");
                            con.rollback();
                            con.setAutoCommit(true);
                        }


                    }
                }
                 else{
                    System.out.println("Invalid pin!!!...");
                }




        }catch(SQLException e){
            e.printStackTrace();
        }
        ///
        con.setAutoCommit(true);
    }


    public void debit_money(Long account_number)throws  SQLException{
        System.out.println("Enter amount:-");
        Scanner sc=new Scanner(System.in);
        double amount=sc.nextDouble();
        System.out.println("Enter security_pin");
        String security_pin=sc.nextLine();

        try{
            con.setAutoCommit(true);
            if(account_number!=0){
                PreparedStatement preparedStatement=con.prepareStatement("Select * from account_number=? and security_number=?");
                preparedStatement.setLong(1,account_number);
                preparedStatement.setString(2,security_pin);
                ResultSet resultSet=preparedStatement.executeQuery();
                if(resultSet.next()){
                    double current_balance=resultSet.getDouble("balance");

                    if(amount<=current_balance){
                        String debit_query="Update accounts set balance=balance-? where account_number=?";
                        PreparedStatement preparedStatement1=con.prepareStatement(debit_query);
                        preparedStatement1.setDouble(1,amount);
                        preparedStatement1.setLong(2,account_number);
                        int rowsaffected = preparedStatement1.executeUpdate();
                        if(rowsaffected >0){
                            System.out.println("Rs."+amount+"debited successfully..");
                            con.commit();
                            con.setAutoCommit(true);

                        }else{
                            System.out.println("Transaction failed!");
                            con.rollback();
                            con.setAutoCommit(true);
                        }


                    }else{
                        System.out.println("Insufficient balance!!");
                    }
                }else{
                    System.out.println("Invalid pin!!!...");
                }


            }

        }catch(SQLException e){
            e.printStackTrace();
        }

        con.setAutoCommit(true);
    }


    public void transfer_money(long sender_account_number) throws SQLException {
        System.out.println("enter receiver account number:--");
        Scanner sc=new Scanner(System.in);
        long receiver_account_number=sc.nextLong();
        System.out.println("Enter amount:-");
        double amount=sc.nextDouble();
        System.out.println("Enter Security pin:-");
        String security_pin=sc.nextLine();
        try{
            con.setAutoCommit(false);
            if(sender_account_number!=0 && receiver_account_number!=0){
                PreparedStatement preparedStatement=con.prepareStatement("select * from accounts where account_number=? and security_pin=?");
                preparedStatement.setLong(1,sender_account_number);
                preparedStatement.setString(2,security_pin);
                ResultSet resultSet=preparedStatement.executeQuery();

                if(resultSet.next()){
                   double current_balance=resultSet.getDouble("balance");
                   if(amount<=current_balance){

                       // set debit and credit query......
                       String debit_query="UPDATE Accounts set balance=balance- ? where account_number=?";
                       String credit_query="UPDATE Accounts set balance=balance+ ? where account_number=?";


                       // create PreparedStatement for credit and debit query.....
                       PreparedStatement creditpreparedStatement=con.prepareStatement(credit_query);
                       PreparedStatement debitpreparedStatement=con.prepareStatement(debit_query);

                       //  set values for debit and credit preparedstatement........
                       creditpreparedStatement.setDouble(1,amount);
                       creditpreparedStatement.setLong(2,receiver_account_number);

                       debitpreparedStatement.setDouble(1,amount);
                       debitpreparedStatement.setLong(2,sender_account_number);

                       int rowsaffected1 =debitpreparedStatement.executeUpdate();
                       int rowsaffected2 =creditpreparedStatement.executeUpdate();

                       if(rowsaffected1>0 && rowsaffected2>0){
                           System.out.println("Transaction successfully done");
                           System.out.println("Rs."+amount+"Transaction successfully");
                           con.commit();
                           con.setAutoCommit(true);
                           return;
                       }
                       else{
                           System.out.println("Transaction failed");
                           con.rollback();
                           con.setAutoCommit(true);
                       }
                   }
                   else
                   {
                       System.out.println("Insufficient balance");
                   }

                }else{
                    System.out.println("Invalid pin....");
                }
            }else{
                System.out.println("Invalid account_number");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        con.setAutoCommit(true);

    }

    public void checkbalance(long account_number){
          System.out.println("Enter security pin");
          Scanner sc=new Scanner(System.in);
          String security_pin=sc.nextLine();

          try{
              PreparedStatement preparedStatement=con.prepareStatement("select balance from accounts where account_number=? and security_pin=?");
              preparedStatement.setLong(1,account_number);
              preparedStatement.setString(2,security_pin);
              ResultSet resultSet=preparedStatement.executeQuery();
              if(resultSet.next()){
                  double balance=resultSet.getDouble("balance");
                  System.out.println("Balance"+balance);
              }
              else{
                  System.out.println("Invalid Pin");
              }
          }catch (SQLException e){
              e.printStackTrace();
          }

      }
}

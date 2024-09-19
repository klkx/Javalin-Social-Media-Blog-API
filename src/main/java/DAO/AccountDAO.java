package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;


public class AccountDAO{

    public Account createAccount(Account account){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "INSERT INTO Account (username, password) VALUES (?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            preparedStatement.executeUpdate();
            ResultSet primaryKeyResultSet = preparedStatement.getGeneratedKeys();
            if(primaryKeyResultSet.next()){
                int generated_account_id = (int) primaryKeyResultSet.getLong(1);
                return new Account(generated_account_id, account.getUsername(), account.getPassword());
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return null;
    }

    public Account getAccount_byID(int id){
        Connection connection = ConnectionUtil.getConnection();
        ResultSet resultSet = null;
        System.out.println("In AccountDAO.getAccount: ");
        try{
            System.out.println("In AccountDAO.getAccount try block: ");
            // use account_id to look for user if there is account id data otherwise use username to look for account
            if(id >= 0){
                System.out.println("getAccountDAO: provided id is provided.");

                String sql = "SELECT * FROM Account WHERE account_id = ?;";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, id);
                resultSet = preparedStatement.executeQuery();

                System.out.println("getAccountDAO: provided id is >=0: the resultSet is ");
                System.out.println(resultSet.toString());
            }else{
                System.out.println("AccountDAO.getAccount_byID: cannot find the user by id due to id<0 ;");
            }

            if(resultSet.next()){
                System.out.println("To return the existing account record from the database by id.");
                System.out.println(resultSet.toString());

                return new Account((int)resultSet.getLong("account_id"), 
                    resultSet.getString("username"),
                    resultSet.getString("password"));
            }
        }catch(SQLException e){
            System.out.println("In AccountDAO.getAccount SQLException block: ");
            System.out.println(e.getMessage());
        }

        return null;
    }
    
    public Account getAccount_byUserName(String username){
        Connection connection = ConnectionUtil.getConnection();
        ResultSet resultSet = null;
        System.out.println("In AccountDAO.getAccount: ");
        try{
            System.out.println("In AccountDAO.getAccount try block: ");
            // use account_id to look for user if there is account id data otherwise use username to look for account
            if (username != null) {
                System.out.println("getAccountDAO.getuser_byUsername: username is provided.");

                String sql = "SELECT * FROM Account WHERE username = ?;";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, username);
                resultSet = preparedStatement.executeQuery();
            }else{
                System.out.println("getAccountDAO.getuser_byUsername: Cannot find the login via username;");
            }

            if(resultSet.next()){
                System.out.println("To return the existing account record from the database.");
                System.out.println(resultSet.toString());

                return new Account((int)resultSet.getLong("account_id"), 
                    resultSet.getString("username"),
                    resultSet.getString("password"));
            }

        }catch(SQLException e){
            System.out.println("In AccountDAO.getAccount SQLException block: ");
            System.out.println(e.getMessage());
        }

        return null;
    }
    

}
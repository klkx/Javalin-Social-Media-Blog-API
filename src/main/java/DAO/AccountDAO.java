package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public Account getAccount(Account account){
        Connection connection = ConnectionUtil.getConnection();
        ResultSet resultSet = null;

        try{
            // use account_id to look for user if there is account id data otherwise use username to look for account
            if(account.getAccount_id() > -1){
                String sql = "SELECT * FROM Account WHERE account_id = ?;";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, account.getAccount_id());
                resultSet = preparedStatement.executeQuery();
            }else if (account.getUsername() != null) {
                String sql = "SELECT * FROM Account WHERE username = ?;";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, account.getUsername());
                resultSet = preparedStatement.executeQuery();
            }
            if(resultSet.next()){
                return new Account((int)resultSet.getLong("account_id"), 
                resultSet.getString("username"),
                resultSet.getString("password"));
            }

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return null;
    }
    
}
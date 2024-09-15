package Service;

import DAO.AccountDAO;
import Model.Account;
import java.sql.*;

public class AccountService {
    
    AccountDAO accountDAO;

    public AccountService(){
        this.accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    public Account createAccount(Account account){
        // the username is not blank, the password is at least 4 characters long 
        
        String username = account.getUsername();
        String password = account.getPassword();

        if(username.isEmpty() || username == null || password.isEmpty() || password ==null|| password.length() <4){
            return null;
        }
        
        // an Account with that username does not already exist. 
        Account existingAccount = accountDAO.getAccount(account);
        if(existingAccount != null){ //not null means duplicated username or user id
            return null;
        }

        return this.accountDAO.createAccount(account);
    }
}

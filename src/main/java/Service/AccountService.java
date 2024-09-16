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

    public Account loginAccount(Account account){    
        String username = account.getUsername();
        String password = account.getPassword();
        System.out.println(account.getAccount_id());
        System.out.println("Login: provided account username:");
        System.out.println(username);
        System.out.println("Login: provided account pass:");
        System.out.println(password);
        if(username.isEmpty() || username == null || password.isEmpty() || password ==null){
            return null;
        }
        Account existingAccount = accountDAO.getAccount(account);
        if(existingAccount != null){ 
            System.out.println("Login: got the existing account by id or usernmae.");
            if(existingAccount.getUsername().equals(username) && existingAccount.getPassword().equals(password)){
                return existingAccount;
            }else{
                System.out.println("Login: account username and pass are not matched.");    
                System.out.println(existingAccount.getUsername());
                System.out.println(username);
                System.out.println(existingAccount.getPassword());
                System.out.println(password);
            }
        }else{
            System.out.println("LoginService: provided account is not in the databse.");
        }
        return null;
    }
}

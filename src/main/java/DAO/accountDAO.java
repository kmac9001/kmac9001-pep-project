package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;


public class accountDAO {

    //insert account into table
    public Account insertAccount(Account a){
        Connection c = ConnectionUtil.getConnection();

        try{
            //check to see if the user already has an account
            String s0 = "SELECT username FROM Account WHERE username = ?;";

            PreparedStatement ps = c.prepareStatement(s0);
            ps.setString(1, a.getUsername());

            ResultSet uname = ps.executeQuery();
            if(uname.next()){
                return null;
            }
            
            //make prepared statement
            String s = "INSERT INTO Account (username, password) VALUES (?,?);";

            PreparedStatement p = c.prepareStatement(s, Statement.RETURN_GENERATED_KEYS);

            //set values
            p.setString(1, a.getUsername());
            p.setString(2, a.getPassword());

            
            //execute
            p.executeUpdate();
            ResultSet r = p.getGeneratedKeys();
            if(r.next()){
                int id = (int) r.getLong(1);

                return new Account(id, a.getUsername(), a.getPassword());
                
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Account checkAccount(Account a){
        Connection c = ConnectionUtil.getConnection();

        try{
            String s = "SELECT * FROM Account WHERE username = ? AND password = ?;";
            PreparedStatement p = c.prepareStatement(s);
            p.setString(1, a.getUsername());
            p.setString(2, a.getPassword());
            
            ResultSet r = p.executeQuery();
            if(!r.next()){
                return null;
            }
            else{
                Account acc = new Account(r.getInt("account_id"),r.getString("username"), r.getString("password"));
                return acc;
            }
             
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Account getAccountByID(int id){
        Connection c = ConnectionUtil.getConnection();

        try{

            String q = "SELECT * FROM Account WHERE account_id = ?;";
            PreparedStatement p = c.prepareStatement(q);
            p.setInt(1, id);

            ResultSet a = p.executeQuery();
            if(!a.next()){
                return null;
            }
            else{
                Account acc = new Account(a.getInt("account_id"),a.getString("username"),a.getString("password"));
                return acc;
            }

        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    

}

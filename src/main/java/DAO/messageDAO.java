package DAO;

import Model.Message;
import Util.ConnectionUtil;
import java.sql.*;
import java.util.ArrayList;

public class messageDAO {

    public Message createMessage(Message m){
        Connection c = ConnectionUtil.getConnection();

        try{
            //ensure message is valid
            if(m.getMessage_text().length() > 0 && m.getMessage_text().length() < 256){
                //message valid, insert into table
                String s = "INSERT INTO Message (posted_by, message_text, time_posted_epoch) VALUES (?,?,?);";
                PreparedStatement p = c.prepareStatement(s, Statement.RETURN_GENERATED_KEYS);

                //assign message details
                p.setInt(1, m.getPosted_by());
                p.setString(2, m.getMessage_text());
                p.setLong(3, m.getTime_posted_epoch());

                //create message in database
                p.executeUpdate();
                //get automatically generated key from successful message insertion. if the key exists, insertion was successful
                ResultSet r = p.getGeneratedKeys();
                if(r.next()){
                    int id = (int) r.getLong(1);

                    return new Message(id, m.getPosted_by(), m.getMessage_text(), m.getTime_posted_epoch());
                }


            }
            else{
                return null;
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public ArrayList<Message> allMessages(){
        Connection c = ConnectionUtil.getConnection();
        ArrayList<Message> mlist = new ArrayList<Message>();

        try {

            String q = "SELECT * FROM Message;";
            PreparedStatement p = c.prepareStatement(q);
            ResultSet r = p.executeQuery();
            while(r.next()){
                Message nm = new Message(r.getInt("message_id"), r.getInt("posted_by"), r.getString("message_text"), r.getLong("time_posted_epoch"));
                mlist.add(nm);
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return mlist;
    }

    public Message messageByID(int id){
        Connection c = ConnectionUtil.getConnection();
        try {
            
            String q = "SELECT * FROM Message WHERE message_id = ?;";
            PreparedStatement p = c.prepareStatement(q);
            p.setInt(1, id);
            ResultSet r = p.executeQuery();
            if(r.next()){
                Message rm = new Message(r.getInt("message_id"), r.getInt("posted_by"), r.getString("message_text"), r.getLong("time_posted_epoch"));
                return rm;
            }


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Message deleteMessageByID(int id){
        Connection c = ConnectionUtil.getConnection();
        try {
            
            String q = "SELECT * FROM Message WHERE message_id = ?;";
            PreparedStatement p = c.prepareStatement(q);
            p.setInt(1, id);
            ResultSet r = p.executeQuery();
            if(r.next()){
                Message rm = new Message(r.getInt("message_id"), r.getInt("posted_by"), r.getString("message_text"), r.getLong("time_posted_epoch"));
                String dc = "DELETE FROM Message WHERE message_id = ?;";
                PreparedStatement p2 = c.prepareStatement(dc);
                p2.setInt(1, id);
                p2.executeUpdate();
                return rm;
            }


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Message updateMessage(int id, String newText){
        Connection c = ConnectionUtil.getConnection();
        try {
            
            String q = "SELECT * FROM Message WHERE message_id = ?;";
            PreparedStatement p = c.prepareStatement(q);
            p.setInt(1, id);
            ResultSet r = p.executeQuery();
            if(r.next()){
                String nt = "UPDATE Message SET message_text = ? WHERE message_id = ?;";
                PreparedStatement p2 = c.prepareStatement(nt);
                p2.setString(1, newText);
                p2.setInt(2, id);
                p2.executeUpdate();

                String q2 = "SELECT * FROM Message WHERE message_id = ?;";
                PreparedStatement p3 = c.prepareStatement(q2);
                p3.setInt(1, id);
                ResultSet r2 = p3.executeQuery();
                

                if(r2.next()){
                    Message rm = new Message(r2.getInt("message_id"), r2.getInt("posted_by"), r2.getString("message_text"), r2.getLong("time_posted_epoch"));
                    return rm;
                }
            }


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public ArrayList<Message> accountMessages(int id){
        Connection c = ConnectionUtil.getConnection();
        ArrayList<Message> mlist = new ArrayList<Message>();

        try {

            String q = "SELECT * FROM Message WHERE posted_by = ?;";
            PreparedStatement p = c.prepareStatement(q);
            p.setInt(1, id);
            ResultSet r = p.executeQuery();
            while(r.next()){
                Message nm = new Message(r.getInt("message_id"), r.getInt("posted_by"), r.getString("message_text"), r.getLong("time_posted_epoch"));
                mlist.add(nm);
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return mlist;
    }
    
}

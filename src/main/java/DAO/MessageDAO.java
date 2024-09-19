package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;


public class MessageDAO{

    public Message createMessage(Message message){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            preparedStatement.executeUpdate();
            ResultSet primaryKeyResultSet = preparedStatement.getGeneratedKeys();
            if(primaryKeyResultSet.next()){
                int generated_id = (int) primaryKeyResultSet.getLong(1);
                return new Message(generated_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return null;
    }

    public ArrayList<Message> getALLMessages(){
        Connection connection = ConnectionUtil.getConnection();
        ResultSet resultSet = null;
        try{
            String sql = "SELECT * FROM message;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            ArrayList<Message> Messages = new ArrayList<>();
            while(resultSet.next()){
                Messages.add(
                    new Message(
                        resultSet.getInt("message_id"), 
                        resultSet.getInt("posted_by"), 
                        resultSet.getString("message_text"), 
                        resultSet.getLong("time_posted_epoch"))
                );
            }
            return Messages;
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return new ArrayList<>();
    }


    public ArrayList<Message> getALLMessages_byUserID(int userId){
        Connection connection = ConnectionUtil.getConnection();
        ResultSet resultSet = null;
        try{
            String sql = "SELECT * FROM message WHERE posted_by = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            resultSet = preparedStatement.executeQuery();
            ArrayList<Message> aUserMessages = new ArrayList<>();
            while(resultSet.next()){
                aUserMessages.add(
                    new Message(
                        resultSet.getInt("message_id"), 
                        resultSet.getInt("posted_by"), 
                        resultSet.getString("message_text"), 
                        resultSet.getLong("time_posted_epoch"))
                );
            }
            return aUserMessages;
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return new ArrayList<>();
    }

    public Message getMessage_byID(int id){
        Connection connection = ConnectionUtil.getConnection();
        ResultSet resultSet = null;
        try{
            String sql = "SELECT * FROM message WHERE message_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return new Message(
                    resultSet.getInt("message_id"), 
                    resultSet.getInt("posted_by"), 
                    resultSet.getString("message_text"), 
                    resultSet.getLong("time_posted_epoch"));

            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    public Message deleteMessage_byID(int id){ //return deleted message otherwise null
        Connection connection = ConnectionUtil.getConnection();
        try{
            Message deletedMessage = getMessage_byID(id);
            if(deletedMessage != null){
                //execute the delete
                String delete_sql = "DELETE FROM message WHERE message_id = ?;";
                PreparedStatement delete_preparedStatement = connection.prepareStatement(delete_sql);
                delete_preparedStatement.setInt(1, id);
                delete_preparedStatement.executeUpdate();
                return deletedMessage;
            }else{
                return null;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
  
    public Message updateMessage_byID(int id, String newMessage){ //return updated message otherwise null
        Connection connection = ConnectionUtil.getConnection();
        try{
            Message theTargetMessage = getMessage_byID(id);
            if(theTargetMessage != null){
                String sql = "UPDATE message SET message_text = ? WHERE message_id = ?;";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, newMessage);
                preparedStatement.setInt(2, id);
                preparedStatement.executeUpdate();
                return getMessage_byID(id);
            }else{
                return null;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
  
}
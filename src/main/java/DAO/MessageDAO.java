package DAO;
import Model.Message;
import Util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {


    public Message createMessage(Message message){
        
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "INSERT INTO message(posted_by, message_text, time_posted_epoch) VALUES(? , ? , ?) ";
            PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, message.getPosted_by());
            stmt.setString(2, message.getMessage_text());
            stmt.setLong(3, message.getTime_posted_epoch());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();

            if(rs.next()) {
                message.setMessage_id(rs.getInt("message_id"));
                System.out.print(message);
                return message;
            }

        }catch(SQLException e){
            System.out.print(e.getMessage());
        }
        return null;
    }

    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try{
            String sql = "Select * FROM message";
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Message message = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

    public Message getMessageById(int id){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "SELECT * FROM message where message_id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Message message = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );
                return message;
            }

        }catch(SQLException e){
            System.out.print(e.getMessage());
        }
        return null;
    }
    
    
    public Boolean deleteMessageById(int id){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "DELETE FROM message where message_id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            
            return rowsAffected > 0;
            

        }catch(SQLException e){
            System.out.print(e.getMessage());
        }
        return false;
    }

    public boolean updateMessageById(int messageId, String text){
        try {
            Connection connection = ConnectionUtil.getConnection();
            String sql = "UPDATE message set message_text = ? WHERE message_id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, text);
            stmt.setInt(2, messageId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }
        return false;
    }

    public List<Message> getAllMessagesByUser(int userId){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> userMessages = new ArrayList<>();
        try {
            String sql = "SELECT * from message where posted_by = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                Message message = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );
                userMessages.add(message);
            }
        } catch (SQLException e) {
            // TODO: handle exception
            System.out.print(e.getMessage());
        }
        return userMessages;
    }

}

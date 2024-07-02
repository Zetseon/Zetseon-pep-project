package DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {
    
    public Account getAccountByUsername(String username){
        Connection connection = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM Account WHERE username = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Account(
                    rs.getInt("account_id"),
                    rs.getString("username"),
                    rs.getString("password")
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Account getUserByAccountId(int id){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM Account WHERE account_id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Account(
                    rs.getInt("account_id"),
                    rs.getString("username"),
                    rs.getString("password")
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
        
    }

    public Account createAccount(Account account){
        Connection connection = ConnectionUtil.getConnection();
        String sql = "INSERT INTO account(username, password) VALUES(?, ?)";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, account.getUsername());
            stmt.setString(2, account.getPassword());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if(rs.next()){
                account.setAccount_id(rs.getInt("account_id"));
                return account;
            }
        } catch (SQLException e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }
        
        return null;
    }
    
}

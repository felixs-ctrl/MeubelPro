package meubelpro.dao;

import java.sql.*;
import meubelpro.config.DatabaseConnection;
import meubelpro.model.Admin;
import meubelpro.util.PasswordUtil;

public class AdminDAO {

    public Admin login(String username, String password) throws SQLException {
        String sql = "SELECT id, username, nama_lengkap, email FROM admin WHERE username = ? AND password = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, PasswordUtil.sha256(password));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Admin(rs.getInt("id"), rs.getString("username"),
                            rs.getString("nama_lengkap"), rs.getString("email"));
                }
            }
        }
        return null;
    }
}


package meubelpro.dao;

import java.sql.SQLException;
import meubelpro.config.HibernateUtil;
import meubelpro.model.Admin;
import meubelpro.util.PasswordUtil;
import org.hibernate.Session;

public class AdminDAO {

    public Admin login(String username, String password) throws SQLException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            String passwordHash = PasswordUtil.sha256(password);

            Admin admin = session.createQuery(
                    "FROM Admin a WHERE a.username = :username "
                    + "AND a.password = :password",
                    Admin.class)
                    .setParameter("username", username)
                    .setParameter("password", passwordHash)
                    .uniqueResult();

            return admin;

        } catch (RuntimeException e) {
            throw new SQLException("Gagal melakukan login admin.", e);
        }
    }
}

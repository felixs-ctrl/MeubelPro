package meubelpro.dao;

import java.sql.SQLException;

import meubelpro.model.Admin;
import meubelpro.util.PasswordUtil;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.springframework.stereotype.Repository;

@Repository
public class AdminDAO {

    private final SessionFactory sessionFactory;

    public AdminDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Admin login(String username, String password) throws SQLException {

        try (Session session = sessionFactory.openSession()) {

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

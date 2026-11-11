package meubelpro.config;

import meubelpro.model.Produk;
import meubelpro.model.BahanBaku;
import meubelpro.model.Admin;
import meubelpro.model.RencanaProduksi;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static final SessionFactory SESSION_FACTORY =
            buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            return new Configuration()
                    .configure("hibernate.cfg.xml")
                    .addAnnotatedClass(Produk.class)
                    .addAnnotatedClass(BahanBaku.class)
                    .addAnnotatedClass(Admin.class)
                    .addAnnotatedClass(RencanaProduksi.class)
                    .buildSessionFactory();

        } catch (Throwable ex) {
            System.err.println("GAGAL MEMBUAT SESSION FACTORY!");
            ex.printStackTrace();
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return SESSION_FACTORY;
    }

    public static void shutdown() {
        if (SESSION_FACTORY != null) {
            SESSION_FACTORY.close();
        }
    }
}

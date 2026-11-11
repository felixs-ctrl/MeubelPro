
package meubelpro;

import meubelpro.config.HibernateUtil;
import org.hibernate.Session;

public class HibernateTest {

    public static void main(String[] args) {

        try {
            System.out.println("Memulai pengujian Hibernate...");

            try (Session session = HibernateUtil.getSessionFactory().openSession()) {

                System.out.println("Koneksi Hibernate berhasil!");

                Long produk = session.createQuery(
                        "SELECT COUNT(p.id) FROM Produk p",
                        Long.class
                ).getSingleResult();

                Long bahan = session.createQuery(
                        "SELECT COUNT(b.id) FROM BahanBaku b",
                        Long.class
                ).getSingleResult();

                Long admin = session.createQuery(
                        "SELECT COUNT(a.id) FROM Admin a",
                        Long.class
                ).getSingleResult();

                Long produksi = session.createQuery(
                        "SELECT COUNT(r.id) FROM RencanaProduksi r",
                        Long.class
                ).getSingleResult();

                System.out.println("Jumlah produk: " + produk);
                System.out.println("Jumlah bahan baku: " + bahan);
                System.out.println("Jumlah admin: " + admin);
                System.out.println("Jumlah rencana produksi: " + produksi);

            }

        } catch (Exception e) {
            System.out.println("Pengujian Hibernate gagal!");
            e.printStackTrace();

        } finally {
            HibernateUtil.shutdown();
        }
    }
}

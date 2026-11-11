
package meubelpro.dao;

import java.sql.SQLException;
import java.util.List;
import meubelpro.config.HibernateUtil;
import meubelpro.model.Produk;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ProdukDAO {

    public List<Produk> getAll() throws SQLException {
        return search(null);
    }

    public List<Produk> search(String keyword) throws SQLException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            String like = "%" + (keyword == null ? "" : keyword.trim()) + "%";

            return session.createQuery(
                    "FROM Produk p WHERE p.kode LIKE :keyword "
                    + "OR p.nama LIKE :keyword "
                    + "OR p.jenis LIKE :keyword ORDER BY p.kode",
                    Produk.class)
                    .setParameter("keyword", like)
                    .getResultList();

        } catch (RuntimeException e) {
            throw new SQLException("Gagal mencari data produk.", e);
        }
    }

    public void insert(Produk p) throws SQLException {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.persist(p);

            transaction.commit();

        } catch (RuntimeException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new SQLException("Gagal menambahkan produk.", e);
        }
    }

    public void update(Produk p) throws SQLException {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.merge(p);

            transaction.commit();

        } catch (RuntimeException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new SQLException("Gagal mengubah produk.", e);
        }
    }

    public void delete(int id) throws SQLException {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Produk produk = session.get(Produk.class, id);

            if (produk != null) {
                session.remove(produk);
            }

            transaction.commit();

        } catch (RuntimeException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new SQLException("Gagal menghapus produk.", e);
        }
    }

    public int count() throws SQLException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Long total = session.createQuery(
                    "SELECT COUNT(p.id) FROM Produk p",
                    Long.class)
                    .getSingleResult();

            return total.intValue();

        } catch (RuntimeException e) {
            throw new SQLException("Gagal menghitung jumlah produk.", e);
        }
    }
}

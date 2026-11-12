package meubelpro.dao;

import java.sql.SQLException;
import java.util.List;

import meubelpro.model.BahanBaku;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import org.springframework.stereotype.Repository;

@Repository
public class BahanBakuDAO {

    private final SessionFactory sessionFactory;

    public BahanBakuDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<BahanBaku> getAll() throws SQLException {
        try (Session session = sessionFactory.openSession()) {

            return session.createQuery(
                    "FROM BahanBaku b ORDER BY b.jenis, b.nama",
                    BahanBaku.class)
                    .getResultList();

        } catch (RuntimeException e) {
            throw new SQLException("Gagal mengambil data bahan baku.", e);
        }
    }

    public void insert(BahanBaku b) throws SQLException {
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            session.persist(b);

            transaction.commit();

        } catch (RuntimeException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new SQLException("Gagal menambahkan bahan baku.", e);
        }
    }

    public void updateStok(int id, int stokBaru) throws SQLException {
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            BahanBaku bahan = session.get(BahanBaku.class, id);

            if (bahan != null) {
                bahan.setStok(stokBaru);
            }

            transaction.commit();

        } catch (RuntimeException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new SQLException("Gagal mengubah stok bahan baku.", e);
        }
    }

    public void delete(int id) throws SQLException {
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            BahanBaku bahan = session.get(BahanBaku.class, id);

            if (bahan != null) {
                session.remove(bahan);
            }

            transaction.commit();

        } catch (RuntimeException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new SQLException("Gagal menghapus bahan baku.", e);
        }
    }

    public int count() throws SQLException {
        try (Session session = sessionFactory.openSession()) {

            Long total = session.createQuery(
                    "SELECT COUNT(b.id) FROM BahanBaku b",
                    Long.class)
                    .getSingleResult();

            return total.intValue();

        } catch (RuntimeException e) {
            throw new SQLException("Gagal menghitung jumlah bahan baku.", e);
        }
    }
}

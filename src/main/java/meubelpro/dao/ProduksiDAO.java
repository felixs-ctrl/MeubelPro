package meubelpro.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import meubelpro.model.RencanaProduksi;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import org.springframework.stereotype.Repository;

@Repository
public class ProduksiDAO {

    private final SessionFactory sessionFactory;

    public ProduksiDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<RencanaProduksi> getJadwal(String status) throws SQLException {
        try (Session session = sessionFactory.openSession()) {

            String hql = "SELECT r, p.kode, p.nama "
                    + "FROM RencanaProduksi r "
                    + "JOIN Produk p ON p.id = r.produkId ";

            if (status != null && !status.isEmpty()) {
                hql += "WHERE r.status = :status ";
            }

            hql += "ORDER BY r.tanggalMulai, r.id";

            var query = session.createQuery(hql, Object[].class);

            if (status != null && !status.isEmpty()) {
                query.setParameter("status", status);
            }

            List<Object[]> results = query.getResultList();
            List<RencanaProduksi> list = new ArrayList<>();

            for (Object[] row : results) {
                RencanaProduksi r = (RencanaProduksi) row[0];
                r.setKodeProduk((String) row[1]);
                r.setNamaProduk((String) row[2]);
                list.add(r);
            }

            return list;

        } catch (RuntimeException e) {
            throw new SQLException("Gagal mengambil jadwal produksi.", e);
        }
    }

    public void insert(RencanaProduksi r) throws SQLException {
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            r.setStatus(RencanaProduksi.DIRENCANAKAN);
            r.setJumlahHasil(0);

            session.persist(r);

            transaction.commit();

        } catch (RuntimeException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new SQLException("Gagal menambahkan rencana produksi.", e);
        }
    }

    public void updateStatus(int id, String status, int jumlahHasil)
            throws SQLException {

        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            RencanaProduksi r = session.get(RencanaProduksi.class, id);

            if (r != null) {
                r.setStatus(status);
                r.setJumlahHasil(
                        RencanaProduksi.SELESAI.equals(status) ? jumlahHasil : 0
                );
            }

            transaction.commit();

        } catch (RuntimeException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new SQLException("Gagal mengubah status produksi.", e);
        }
    }

    public void delete(int id) throws SQLException {
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            RencanaProduksi r = session.get(RencanaProduksi.class, id);

            if (r != null) {
                session.remove(r);
            }

            transaction.commit();

        } catch (RuntimeException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new SQLException("Gagal menghapus rencana produksi.", e);
        }
    }

    public int countByStatus(String status) throws SQLException {
        try (Session session = sessionFactory.openSession()) {

            String hql = "SELECT COUNT(r.id) FROM RencanaProduksi r";

            if (status != null) {
                hql += " WHERE r.status = :status";
            }

            var query = session.createQuery(hql, Long.class);

            if (status != null) {
                query.setParameter("status", status);
            }

            return query.getSingleResult().intValue();

        } catch (RuntimeException e) {
            throw new SQLException("Gagal menghitung rencana produksi.", e);
        }
    }

    public int[] getTotalUnit() throws SQLException {
        try (Session session = sessionFactory.openSession()) {

            Object[] result = session.createQuery(
                    "SELECT COALESCE(SUM(r.jumlah), 0), "
                    + "COALESCE(SUM(r.jumlahHasil), 0) "
                    + "FROM RencanaProduksi r",
                    Object[].class)
                    .getSingleResult();

            Number totalRencana = (Number) result[0];
            Number totalHasil = (Number) result[1];

            return new int[]{
                totalRencana.intValue(),
                totalHasil.intValue()
            };

        } catch (RuntimeException e) {
            throw new SQLException("Gagal menghitung total unit produksi.", e);
        }
    }
}

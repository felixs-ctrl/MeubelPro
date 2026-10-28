package meubelpro.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import meubelpro.config.DatabaseConnection;
import meubelpro.model.Produk;

/** DAO CRUD untuk tabel produk. Semua method melempar SQLException agar ditangani oleh View. */
public class ProdukDAO {

    private Produk map(ResultSet rs) throws SQLException {
        return new Produk(rs.getInt("id"), rs.getString("kode"), rs.getString("nama"),
                rs.getString("jenis"), rs.getString("deskripsi"));
    }

    /** Ambil semua produk. */
    public List<Produk> getAll() throws SQLException {
        return search(null);
    }

    /** Cari produk berdasarkan kode / nama / jenis. keyword null/kosong = semua data. */
    public List<Produk> search(String keyword) throws SQLException {
        String sql = "SELECT * FROM produk WHERE kode LIKE ? OR nama LIKE ? OR jenis LIKE ? ORDER BY kode";
        String like = "%" + (keyword == null ? "" : keyword.trim()) + "%";
        List<Produk> list = new ArrayList<>();
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, like); ps.setString(2, like); ps.setString(3, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    public void insert(Produk p) throws SQLException {
        String sql = "INSERT INTO produk (kode, nama, jenis, deskripsi) VALUES (?,?,?,?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.getKode());
            ps.setString(2, p.getNama());
            ps.setString(3, p.getJenis());
            ps.setString(4, p.getDeskripsi());
            ps.executeUpdate();
        }
    }

    public void update(Produk p) throws SQLException {
        String sql = "UPDATE produk SET kode=?, nama=?, jenis=?, deskripsi=? WHERE id=?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.getKode());
            ps.setString(2, p.getNama());
            ps.setString(3, p.getJenis());
            ps.setString(4, p.getDeskripsi());
            ps.setInt(5, p.getId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM produk WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public int count() throws SQLException {
        try (Connection c = DatabaseConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM produk")) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }
}

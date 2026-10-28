package meubelpro.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import meubelpro.config.DatabaseConnection;
import meubelpro.model.BahanBaku;

/** DAO untuk tabel bahan_baku (tambah data, lihat stok, tambah/kurangi stok, hapus). */
public class BahanBakuDAO {

    public List<BahanBaku> getAll() throws SQLException {
        List<BahanBaku> list = new ArrayList<>();
        String sql = "SELECT * FROM bahan_baku ORDER BY jenis, nama";
        try (Connection c = DatabaseConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new BahanBaku(rs.getInt("id"), rs.getString("kode"), rs.getString("nama"),
                        rs.getString("jenis"), rs.getString("satuan"), rs.getInt("stok")));
            }
        }
        return list;
    }

    public void insert(BahanBaku b) throws SQLException {
        String sql = "INSERT INTO bahan_baku (kode, nama, jenis, satuan, stok) VALUES (?,?,?,?,?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, b.getKode());
            ps.setString(2, b.getNama());
            ps.setString(3, b.getJenis());
            ps.setString(4, b.getSatuan());
            ps.setInt(5, b.getStok()); // Diubah ke setInt
            ps.executeUpdate();
        }
    }

    /** Mengubah stok menjadi nilai baru. */
    public void updateStok(int id, int stokBaru) throws SQLException { // Parameter stokBaru diubah ke int
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("UPDATE bahan_baku SET stok=? WHERE id=?")) {
            ps.setInt(1, stokBaru); // Diubah ke setInt
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM bahan_baku WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public int count() throws SQLException {
        try (Connection c = DatabaseConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM bahan_baku")) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }
}
package meubelpro.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import meubelpro.config.DatabaseConnection;
import meubelpro.model.RencanaProduksi;

/** DAO untuk tabel rencana_produksi (rencana, jadwal, update status, laporan). */
public class ProduksiDAO {

    private static final String SELECT_JOIN =
            "SELECT r.*, p.kode AS kode_produk, p.nama AS nama_produk "
          + "FROM rencana_produksi r JOIN produk p ON p.id = r.produk_id ";

    private RencanaProduksi map(ResultSet rs) throws SQLException {
        RencanaProduksi r = new RencanaProduksi();
        r.setId(rs.getInt("id"));
        r.setProdukId(rs.getInt("produk_id"));
        r.setKodeProduk(rs.getString("kode_produk"));
        r.setNamaProduk(rs.getString("nama_produk"));
        r.setJumlah(rs.getInt("jumlah"));
        r.setTanggalMulai(rs.getDate("tanggal_mulai").toLocalDate());
        r.setTanggalSelesai(rs.getDate("tanggal_selesai").toLocalDate());
        r.setStatus(rs.getString("status"));
        r.setJumlahHasil(rs.getInt("jumlah_hasil"));
        return r;
    }

    /** Jadwal produksi, diurutkan dari tanggal mulai terdekat. status null = semua status. */
    public List<RencanaProduksi> getJadwal(String status) throws SQLException {
        boolean filter = status != null && !status.isEmpty();
        String sql = SELECT_JOIN + (filter ? "WHERE r.status = ? " : "") + "ORDER BY r.tanggal_mulai, r.id";
        List<RencanaProduksi> list = new ArrayList<>();
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            if (filter) ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    public void insert(RencanaProduksi r) throws SQLException {
        String sql = "INSERT INTO rencana_produksi (produk_id, jumlah, tanggal_mulai, tanggal_selesai, status) "
                   + "VALUES (?,?,?,?,?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, r.getProdukId());
            ps.setInt(2, r.getJumlah());
            ps.setDate(3, Date.valueOf(r.getTanggalMulai()));
            ps.setDate(4, Date.valueOf(r.getTanggalSelesai()));
            ps.setString(5, RencanaProduksi.DIRENCANAKAN);
            ps.executeUpdate();
        }
    }

    /** Update status; jumlahHasil hanya relevan saat status "Selesai". */
    public void updateStatus(int id, String status, int jumlahHasil) throws SQLException {
        String sql = "UPDATE rencana_produksi SET status=?, jumlah_hasil=? WHERE id=?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, RencanaProduksi.SELESAI.equals(status) ? jumlahHasil : 0);
            ps.setInt(3, id);
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM rencana_produksi WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    /** Jumlah rencana pada status tertentu (null = semua). */
    public int countByStatus(String status) throws SQLException {
        String sql = "SELECT COUNT(*) FROM rencana_produksi" + (status == null ? "" : " WHERE status = ?");
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            if (status != null) ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    /** Total unit yang direncanakan (index 0) dan total unit hasil (index 1). */
    public int[] getTotalUnit() throws SQLException {
        String sql = "SELECT COALESCE(SUM(jumlah),0), COALESCE(SUM(jumlah_hasil),0) FROM rencana_produksi";
        try (Connection c = DatabaseConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            rs.next();
            return new int[]{rs.getInt(1), rs.getInt(2)};
        }
    }
}

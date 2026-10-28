package meubelpro.model;

import java.time.LocalDate;

/** Model data rencana produksi (sudah termasuk info produk hasil JOIN). */
public class RencanaProduksi {
    public static final String DIRENCANAKAN = "Direncanakan";
    public static final String DIKERJAKAN   = "Sedang Dikerjakan";
    public static final String SELESAI      = "Selesai";
    public static final String[] DAFTAR_STATUS = {DIRENCANAKAN, DIKERJAKAN, SELESAI};

    private int id;
    private int produkId;
    private String kodeProduk;
    private String namaProduk;
    private int jumlah;
    private LocalDate tanggalMulai;
    private LocalDate tanggalSelesai;
    private String status;
    private int jumlahHasil;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getProdukId() { return produkId; }
    public void setProdukId(int produkId) { this.produkId = produkId; }
    public String getKodeProduk() { return kodeProduk; }
    public void setKodeProduk(String kodeProduk) { this.kodeProduk = kodeProduk; }
    public String getNamaProduk() { return namaProduk; }
    public void setNamaProduk(String namaProduk) { this.namaProduk = namaProduk; }
    public int getJumlah() { return jumlah; }
    public void setJumlah(int jumlah) { this.jumlah = jumlah; }
    public LocalDate getTanggalMulai() { return tanggalMulai; }
    public void setTanggalMulai(LocalDate tanggalMulai) { this.tanggalMulai = tanggalMulai; }
    public LocalDate getTanggalSelesai() { return tanggalSelesai; }
    public void setTanggalSelesai(LocalDate tanggalSelesai) { this.tanggalSelesai = tanggalSelesai; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public int getJumlahHasil() { return jumlahHasil; }
    public void setJumlahHasil(int jumlahHasil) { this.jumlahHasil = jumlahHasil; }
}

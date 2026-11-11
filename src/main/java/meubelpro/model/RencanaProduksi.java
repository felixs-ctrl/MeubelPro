
package meubelpro.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Transient;
import java.time.LocalDate;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "rencana_produksi")
public class RencanaProduksi {

    public static final String DIRENCANAKAN = "Direncanakan";
    public static final String DIKERJAKAN = "Sedang Dikerjakan";
    public static final String SELESAI = "Selesai";

    public static final String[] DAFTAR_STATUS = {
        DIRENCANAKAN, DIKERJAKAN, SELESAI
    };

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "produk_id")
    private int produkId;

    @Transient
    private String kodeProduk;

    @Transient
    private String namaProduk;

    @Column(name = "jumlah")
    private int jumlah;

    @Column(name = "tanggal_mulai")
    private LocalDate tanggalMulai;

    @Column(name = "tanggal_selesai")
    private LocalDate tanggalSelesai;

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "status",
            columnDefinition = "ENUM('Direncanakan','Sedang Dikerjakan','Selesai')")
    private String status;

    @Column(name = "jumlah_hasil")
    private int jumlahHasil;

    public RencanaProduksi() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProdukId() {
        return produkId;
    }

    public void setProdukId(int produkId) {
        this.produkId = produkId;
    }

    public String getKodeProduk() {
        return kodeProduk;
    }

    public void setKodeProduk(String kodeProduk) {
        this.kodeProduk = kodeProduk;
    }

    public String getNamaProduk() {
        return namaProduk;
    }

    public void setNamaProduk(String namaProduk) {
        this.namaProduk = namaProduk;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public LocalDate getTanggalMulai() {
        return tanggalMulai;
    }

    public void setTanggalMulai(LocalDate tanggalMulai) {
        this.tanggalMulai = tanggalMulai;
    }

    public LocalDate getTanggalSelesai() {
        return tanggalSelesai;
    }

    public void setTanggalSelesai(LocalDate tanggalSelesai) {
        this.tanggalSelesai = tanggalSelesai;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getJumlahHasil() {
        return jumlahHasil;
    }

    public void setJumlahHasil(int jumlahHasil) {
        this.jumlahHasil = jumlahHasil;
    }
}

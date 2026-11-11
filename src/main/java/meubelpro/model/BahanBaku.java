
package meubelpro.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "bahan_baku")
public class BahanBaku {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "kode")
    private String kode;

    @Column(name = "nama")
    private String nama;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "jenis", columnDefinition = "ENUM('Kayu','Paku','Baut','Lem','Cat','Lainnya')")
    private String jenis;

    @Column(name = "satuan")
    private String satuan;

    @Column(name = "stok")
    private int stok;

    public BahanBaku() {
    }

    public BahanBaku(int id, String kode, String nama,
                     String jenis, String satuan, int stok) {
        this.id = id;
        this.kode = kode;
        this.nama = nama;
        this.jenis = jenis;
        this.satuan = satuan;
        this.stok = stok;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }
}

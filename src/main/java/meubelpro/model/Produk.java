
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
@Table(name = "produk")
public class Produk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "kode")
    private String kode;

    @Column(name = "nama")
    private String nama;

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "jenis", columnDefinition = "ENUM('Meja','Kursi','Lemari','Rak')")
    private String jenis;

    @Column(name = "deskripsi")
    private String deskripsi;

    public Produk() {
    }

    public Produk(int id, String kode, String nama,
                  String jenis, String deskripsi) {
        this.id = id;
        this.kode = kode;
        this.nama = nama;
        this.jenis = jenis;
        this.deskripsi = deskripsi;
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

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    @Override
    public String toString() {
        return kode + " - " + nama;
    }
}

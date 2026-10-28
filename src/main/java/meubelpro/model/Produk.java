package meubelpro.model;

/** Model data produk mebel. */
public class Produk {
    private int id;
    private String kode;
    private String nama;
    private String jenis;      // Meja / Kursi / Lemari / Rak
    private String deskripsi;

    public Produk() { }
    public Produk(int id, String kode, String nama, String jenis, String deskripsi) {
        this.id = id; this.kode = kode; this.nama = nama; this.jenis = jenis; this.deskripsi = deskripsi;
    }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getKode() { return kode; }
    public void setKode(String kode) { this.kode = kode; }
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    public String getJenis() { return jenis; }
    public void setJenis(String jenis) { this.jenis = jenis; }
    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }

    /** Dipakai oleh JComboBox agar tampil "KODE - Nama". */
    @Override public String toString() { return kode + " - " + nama; }
}

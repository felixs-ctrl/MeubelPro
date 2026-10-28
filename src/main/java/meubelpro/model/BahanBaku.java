package meubelpro.model;

/** Model data bahan baku. */
public class BahanBaku {
    private int id;
    private String kode;
    private String nama;
    private String jenis;   // Kayu / Paku / Baut / Lem / Cat / Lainnya
    private String satuan;
    private int stok;       // Diubah dari double ke int

    public BahanBaku() { }
    
    public BahanBaku(int id, String kode, String nama, String jenis, String satuan, int stok) {
        this.id = id; 
        this.kode = kode; 
        this.nama = nama; 
        this.jenis = jenis; 
        this.satuan = satuan; 
        this.stok = stok;
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getKode() { return kode; }
    public void setKode(String kode) { this.kode = kode; }
    
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    
    public String getJenis() { return jenis; }
    public void setJenis(String jenis) { this.jenis = jenis; }
    
    public String getSatuan() { return satuan; }
    public void setSatuan(String satuan) { this.satuan = satuan; }
    
    public int getStok() { return stok; }
    public void setStok(int stok) { this.stok = stok; }
}
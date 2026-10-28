# MeubelPro - Sistem Pengelolaan & Perencanaan Produksi Mebel

Java Swing + MySQL (JDBC), arsitektur Model - DAO - View.

## Struktur
- database/meubelpro.sql        -> skrip database + data awal
- pom.xml                       -> Maven (driver MySQL otomatis diunduh)
- src/main/java/meubelpro/
  - Main.java                   -> titik masuk
  - config/DatabaseConnection   -> koneksi JDBC
  - util/PasswordUtil           -> hash SHA-256
  - model/                      -> Admin, Produk, BahanBaku, RencanaProduksi
  - dao/                        -> AdminDAO, ProdukDAO, BahanBakuDAO, ProduksiDAO
  - view/                       -> Beranda, Login, Profile, Dashboard, panel-panel

## Cara menjalankan
1. Jalankan MySQL (XAMPP/Service), lalu import database/meubelpro.sql.
2. Sesuaikan USER/PASSWORD di config/DatabaseConnection.java.
3. Buka folder project di Apache NetBeans (File > Open Project), Run (F6).
4. Login: admin / admin123

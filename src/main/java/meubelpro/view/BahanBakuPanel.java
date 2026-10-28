package meubelpro.view;

import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import meubelpro.dao.BahanBakuDAO;
import meubelpro.model.BahanBaku;

/** Kelola Bahan Baku: lihat stok, tambah bahan, ubah stok, hapus. */
public class BahanBakuPanel extends RefreshablePanel {

    private static final String[] JENIS = {"Kayu", "Paku", "Baut", "Lem", "Cat", "Lainnya"};
    private static final int BATAS_MENIPIS = 10;   // stok <= nilai ini ditandai "Menipis" (tipe diubah ke int)

    private final BahanBakuDAO dao = new BahanBakuDAO();
    private final DefaultTableModel model =
            UiUtil.readOnlyModel("No", "Kode", "Nama Bahan", "Jenis", "Stok", "Satuan", "Keterangan");
    private final JTable table = UiUtil.table(model);
    private List<BahanBaku> data = new ArrayList<>();

    public BahanBakuPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setBackground(UiUtil.BG);

        add(UiUtil.title("Kelola Bahan Baku"), BorderLayout.NORTH);
        table.getColumnModel().getColumn(0).setMaxWidth(50);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bawah = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        bawah.setOpaque(false);
        JButton btnTambah = UiUtil.button("Tambah Bahan Baku");
        JButton btnStok = UiUtil.button("Ubah Stok (+/-)");
        JButton btnHapus = UiUtil.button("Hapus");
        JButton btnRefresh = UiUtil.button("Refresh");
        bawah.add(btnTambah);
        bawah.add(btnStok);
        bawah.add(btnHapus);
        bawah.add(btnRefresh);
        add(bawah, BorderLayout.SOUTH);

        btnTambah.addActionListener(e -> tambah());
        btnStok.addActionListener(e -> ubahStok());
        btnHapus.addActionListener(e -> hapus());
        btnRefresh.addActionListener(e -> refresh());
    }

    @Override
    public void refresh() {
        try {
            data = dao.getAll();
            model.setRowCount(0);
            int no = 1;
            for (BahanBaku b : data) {
                // Stok langsung ditampilkan sebagai bilangan bulat tanpa format desimal (.00)
                model.addRow(new Object[]{no++, b.getKode(), b.getNama(), b.getJenis(),
                        b.getStok(), b.getSatuan(),
                        b.getStok() <= BATAS_MENIPIS ? "Menipis" : "Aman"});
            }
        } catch (SQLException ex) {
            UiUtil.sqlError(this, ex);
        }
    }

    private BahanBaku getSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            UiUtil.warn(this, "Pilih salah satu bahan baku pada tabel terlebih dahulu.");
            return null;
        }
        return data.get(row);
    }

    private void tambah() {
        JTextField txtKode = new JTextField(20);
        JTextField txtNama = new JTextField(20);
        JComboBox<String> cmbJenis = new JComboBox<>(JENIS);
        JTextField txtSatuan = new JTextField(20);
        JTextField txtStok = new JTextField("0", 20);

        JPanel form = UiUtil.formPanel(
                new String[]{"Kode", "Nama Bahan", "Jenis", "Satuan (kg/pcs/liter/m3)", "Stok Awal"},
                new JComponent[]{txtKode, txtNama, cmbJenis, txtSatuan, txtStok});

        if (JOptionPane.showConfirmDialog(this, form, "Tambah Bahan Baku",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) != JOptionPane.OK_OPTION) return;

        String kode = txtKode.getText().trim();
        String nama = txtNama.getText().trim();
        String satuan = txtSatuan.getText().trim();
        if (kode.isEmpty() || nama.isEmpty() || satuan.isEmpty()) {
            UiUtil.warn(this, "Kode, Nama, dan Satuan wajib diisi.");
            return;
        }
        int stok; // Diubah ke int
        try {
            stok = Integer.parseInt(txtStok.getText().trim()); // Menggunakan Integer.parseInt
            if (stok < 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            UiUtil.warn(this, "Stok harus berupa angka bulat >= 0.");
            return;
        }
        try {
            dao.insert(new BahanBaku(0, kode, nama, (String) cmbJenis.getSelectedItem(), satuan, stok));
            refresh();
            UiUtil.info(this, "Bahan baku berhasil ditambahkan.");
        } catch (SQLException ex) {
            UiUtil.sqlError(this, ex);
        }
    }

    private void ubahStok() {
        BahanBaku b = getSelected();
        if (b == null) return;
        String input = JOptionPane.showInputDialog(this,
                "Stok saat ini: " + b.getStok() + " " + b.getSatuan()
                + "\nMasukkan perubahan stok (positif = tambah, negatif = kurangi):",
                "Ubah Stok - " + b.getNama(), JOptionPane.QUESTION_MESSAGE);
        if (input == null) return;   // dibatalkan
        try {
            int delta = Integer.parseInt(input.trim()); // Diubah ke int
            int baru = b.getStok() + delta;
            if (baru < 0) {
                UiUtil.warn(this, "Stok tidak boleh menjadi negatif.");
                return;
            }
            dao.updateStok(b.getId(), baru);
            refresh();
        } catch (NumberFormatException ex) {
            UiUtil.warn(this, "Masukkan angka bulat yang valid.");
        } catch (SQLException ex) {
            UiUtil.sqlError(this, ex);
        }
    }

    private void hapus() {
        BahanBaku b = getSelected();
        if (b == null) return;
        if (!UiUtil.confirm(this, "Hapus bahan baku \"" + b.getNama() + "\"?")) return;
        try {
            dao.delete(b.getId());
            refresh();
        } catch (SQLException ex) {
            UiUtil.sqlError(this, ex);
        }
    }
}
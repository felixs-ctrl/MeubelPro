package meubelpro.view;

import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import meubelpro.dao.ProdukDAO;
import meubelpro.model.Produk;

/** Kelola Produk: daftar, cari, tambah, lihat detail, edit, hapus. */
public class ProdukPanel extends RefreshablePanel {

    private static final String[] JENIS = {"Meja", "Kursi", "Lemari", "Rak"};

    private final ProdukDAO dao = new ProdukDAO();
    private final DefaultTableModel model = UiUtil.readOnlyModel("No", "Kode", "Nama Produk", "Jenis", "Deskripsi");
    private final JTable table = UiUtil.table(model);
    private final JTextField txtCari = new JTextField(20);
    private List<Produk> data = new ArrayList<>();   // sejajar dengan baris tabel

    public ProdukPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setBackground(UiUtil.BG);

        // ----- Bagian atas: judul + pencarian -----
        JPanel atas = new JPanel(new BorderLayout());
        atas.setOpaque(false);
        atas.add(UiUtil.title("Kelola Produk"), BorderLayout.WEST);
        JPanel cari = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        cari.setOpaque(false);
        JButton btnCari = UiUtil.button("Cari");
        JButton btnReset = UiUtil.button("Reset");
        cari.add(new JLabel("Cari (kode/nama/jenis):"));
        cari.add(txtCari);
        cari.add(btnCari);
        cari.add(btnReset);
        atas.add(cari, BorderLayout.EAST);
        add(atas, BorderLayout.NORTH);

        // ----- Tabel -----
        table.getColumnModel().getColumn(0).setMaxWidth(50);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // ----- Tombol aksi -----
        JPanel bawah = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        bawah.setOpaque(false);
        JButton btnTambah = UiUtil.button("Tambah Produk");
        JButton btnDetail = UiUtil.button("Lihat Detail");
        JButton btnEdit = UiUtil.button("Edit");
        JButton btnHapus = UiUtil.button("Hapus");
        bawah.add(btnTambah);
        bawah.add(btnDetail);
        bawah.add(btnEdit);
        bawah.add(btnHapus);
        add(bawah, BorderLayout.SOUTH);

        // ----- Event -----
        btnCari.addActionListener(e -> refresh());
        txtCari.addActionListener(e -> refresh());
        btnReset.addActionListener(e -> { txtCari.setText(""); refresh(); });
        btnTambah.addActionListener(e -> tambah());
        btnDetail.addActionListener(e -> detail());
        btnEdit.addActionListener(e -> edit());
        btnHapus.addActionListener(e -> hapus());
    }

    @Override
    public void refresh() {
        try {
            data = dao.search(txtCari.getText());
            model.setRowCount(0);
            int no = 1;
            for (Produk p : data) {
                model.addRow(new Object[]{no++, p.getKode(), p.getNama(), p.getJenis(), p.getDeskripsi()});
            }
        } catch (SQLException ex) {
            UiUtil.sqlError(this, ex);
        }
    }

    /** Mengambil produk yang dipilih di tabel (null jika belum memilih). */
    private Produk getSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            UiUtil.warn(this, "Pilih salah satu produk pada tabel terlebih dahulu.");
            return null;
        }
        return data.get(row);
    }

    private void tambah() {
        Produk p = tampilkanForm("Tambah Produk", null);
        if (p == null) return;
        try {
            dao.insert(p);
            refresh();
            UiUtil.info(this, "Produk berhasil ditambahkan.");
        } catch (SQLException ex) {
            UiUtil.sqlError(this, ex);
        }
    }

    private void detail() {
        Produk p = getSelected();
        if (p == null) return;
        String teks = "Kode      : " + p.getKode()
                + "\nNama      : " + p.getNama()
                + "\nJenis     : " + p.getJenis()
                + "\nDeskripsi : " + (p.getDeskripsi() == null || p.getDeskripsi().isEmpty() ? "-" : p.getDeskripsi());
        JTextArea area = new JTextArea(teks);
        area.setEditable(false);
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        JOptionPane.showMessageDialog(this, area, "Detail Produk", JOptionPane.PLAIN_MESSAGE);
    }

    private void edit() {
        Produk lama = getSelected();
        if (lama == null) return;
        Produk baru = tampilkanForm("Edit Produk", lama);
        if (baru == null) return;
        baru.setId(lama.getId());
        try {
            dao.update(baru);
            refresh();
            UiUtil.info(this, "Produk berhasil diperbarui.");
        } catch (SQLException ex) {
            UiUtil.sqlError(this, ex);
        }
    }

    private void hapus() {
        Produk p = getSelected();
        if (p == null) return;
        if (!UiUtil.confirm(this, "Hapus produk \"" + p.getNama() + "\"?")) return;
        try {
            dao.delete(p.getId());
            refresh();
        } catch (SQLException ex) {
            UiUtil.sqlError(this, ex);   // mis. produk masih dipakai di rencana produksi
        }
    }

    /** Dialog form tambah/edit. Mengembalikan Produk hasil input, atau null jika dibatalkan/tidak valid. */
    private Produk tampilkanForm(String judul, Produk src) {
        JTextField txtKode = new JTextField(src == null ? "" : src.getKode(), 20);
        JTextField txtNama = new JTextField(src == null ? "" : src.getNama(), 20);
        JComboBox<String> cmbJenis = new JComboBox<>(JENIS);
        if (src != null) cmbJenis.setSelectedItem(src.getJenis());
        JTextArea txtDeskripsi = new JTextArea(src == null || src.getDeskripsi() == null ? "" : src.getDeskripsi(), 4, 20);
        txtDeskripsi.setLineWrap(true);
        txtDeskripsi.setWrapStyleWord(true);

        JPanel form = UiUtil.formPanel(
                new String[]{"Kode Produk", "Nama Produk", "Jenis", "Deskripsi"},
                new JComponent[]{txtKode, txtNama, cmbJenis, new JScrollPane(txtDeskripsi)});

        int opsi = JOptionPane.showConfirmDialog(this, form, judul,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (opsi != JOptionPane.OK_OPTION) return null;

        String kode = txtKode.getText().trim();
        String nama = txtNama.getText().trim();
        if (kode.isEmpty() || nama.isEmpty()) {
            UiUtil.warn(this, "Kode dan Nama produk wajib diisi.");
            return null;
        }
        return new Produk(0, kode, nama, (String) cmbJenis.getSelectedItem(), txtDeskripsi.getText().trim());
    }
}

package meubelpro.view;

import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import meubelpro.dao.ProdukDAO;
import meubelpro.dao.ProduksiDAO;
import meubelpro.model.Produk;
import meubelpro.model.RencanaProduksi;

/** Kelola Produksi: tambah rencana, lihat jadwal, update status. */
public class ProduksiPanel extends RefreshablePanel {

    private final ProduksiDAO dao = new ProduksiDAO();
    private final ProdukDAO produkDAO = new ProdukDAO();
    private final DefaultTableModel model = UiUtil.readOnlyModel(
            "No", "Kode", "Produk", "Jumlah", "Tgl Mulai", "Tgl Selesai", "Status", "Hasil");
    private final JTable table = UiUtil.table(model);
    private final JComboBox<String> cmbFilter = new JComboBox<>();
    private List<RencanaProduksi> data = new ArrayList<>();

    public ProduksiPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setBackground(UiUtil.BG);

        // ----- Atas: judul + filter status -----
        JPanel atas = new JPanel(new BorderLayout());
        atas.setOpaque(false);
        atas.add(UiUtil.title("Kelola Produksi - Jadwal"), BorderLayout.WEST);
        cmbFilter.addItem("Semua");
        for (String s : RencanaProduksi.DAFTAR_STATUS) cmbFilter.addItem(s);
        JPanel filter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        filter.setOpaque(false);
        filter.add(new JLabel("Filter status:"));
        filter.add(cmbFilter);
        atas.add(filter, BorderLayout.EAST);
        add(atas, BorderLayout.NORTH);

        table.getColumnModel().getColumn(0).setMaxWidth(50);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // ----- Bawah: tombol aksi -----
        JPanel bawah = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        bawah.setOpaque(false);
        JButton btnTambah = UiUtil.button("Tambah Rencana Produksi");
        JButton btnStatus = UiUtil.button("Update Status");
        JButton btnHapus = UiUtil.button("Hapus");
        bawah.add(btnTambah);
        bawah.add(btnStatus);
        bawah.add(btnHapus);
        add(bawah, BorderLayout.SOUTH);

        cmbFilter.addActionListener(e -> refresh());
        btnTambah.addActionListener(e -> tambah());
        btnStatus.addActionListener(e -> updateStatus());
        btnHapus.addActionListener(e -> hapus());
    }

    @Override
    public void refresh() {
        try {
            String f = (String) cmbFilter.getSelectedItem();
            data = dao.getJadwal("Semua".equals(f) ? null : f);
            model.setRowCount(0);
            int no = 1;
            for (RencanaProduksi r : data) {
                model.addRow(new Object[]{no++, r.getKodeProduk(), r.getNamaProduk(), r.getJumlah(),
                        r.getTanggalMulai(), r.getTanggalSelesai(), r.getStatus(),
                        RencanaProduksi.SELESAI.equals(r.getStatus()) ? r.getJumlahHasil() : "-"});
            }
        } catch (SQLException ex) {
            UiUtil.sqlError(this, ex);
        }
    }

    private RencanaProduksi getSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            UiUtil.warn(this, "Pilih salah satu rencana produksi pada tabel terlebih dahulu.");
            return null;
        }
        return data.get(row);
    }

    private void tambah() {
        try {
            List<Produk> daftarProduk = produkDAO.getAll();
            if (daftarProduk.isEmpty()) {
                UiUtil.warn(this, "Belum ada produk. Tambahkan produk terlebih dahulu.");
                return;
            }
            JComboBox<Produk> cmbProduk = new JComboBox<>(daftarProduk.toArray(new Produk[0]));
            JSpinner spJumlah = new JSpinner(new SpinnerNumberModel(1, 1, 1_000_000, 1));
            JTextField txtMulai = new JTextField(LocalDate.now().toString(), 12);
            JTextField txtSelesai = new JTextField(LocalDate.now().plusDays(7).toString(), 12);

            JPanel form = UiUtil.formPanel(
                    new String[]{"Produk", "Jumlah dibuat", "Tanggal Mulai (yyyy-MM-dd)", "Tanggal Selesai (yyyy-MM-dd)"},
                    new JComponent[]{cmbProduk, spJumlah, txtMulai, txtSelesai});

            if (JOptionPane.showConfirmDialog(this, form, "Tambah Rencana Produksi",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) != JOptionPane.OK_OPTION) return;

            LocalDate mulai, selesai;
            try {
                mulai = LocalDate.parse(txtMulai.getText().trim());
                selesai = LocalDate.parse(txtSelesai.getText().trim());
            } catch (DateTimeParseException ex) {
                UiUtil.warn(this, "Format tanggal harus yyyy-MM-dd, contoh: 2026-11-30.");
                return;
            }
            if (selesai.isBefore(mulai)) {
                UiUtil.warn(this, "Tanggal selesai tidak boleh sebelum tanggal mulai.");
                return;
            }

            RencanaProduksi r = new RencanaProduksi();
            r.setProdukId(((Produk) cmbProduk.getSelectedItem()).getId());
            r.setJumlah((Integer) spJumlah.getValue());
            r.setTanggalMulai(mulai);
            r.setTanggalSelesai(selesai);
            dao.insert(r);
            refresh();
            UiUtil.info(this, "Rencana produksi berhasil ditambahkan.");
        } catch (SQLException ex) {
            UiUtil.sqlError(this, ex);
        }
    }

    private void updateStatus() {
        RencanaProduksi r = getSelected();
        if (r == null) return;

        JComboBox<String> cmbStatus = new JComboBox<>(RencanaProduksi.DAFTAR_STATUS);
        cmbStatus.setSelectedItem(r.getStatus());
        JPanel form = UiUtil.formPanel(new String[]{"Produk", "Status baru"},
                new JComponent[]{new JLabel(r.getNamaProduk() + " (" + r.getJumlah() + " unit)"), cmbStatus});

        if (JOptionPane.showConfirmDialog(this, form, "Update Status Produksi",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) != JOptionPane.OK_OPTION) return;

        String status = (String) cmbStatus.getSelectedItem();
        int hasil = 0;

        // Jika selesai, catat jumlah unit yang benar-benar dihasilkan (untuk laporan)
        if (RencanaProduksi.SELESAI.equals(status)) {
            String in = JOptionPane.showInputDialog(this, "Jumlah unit yang berhasil diproduksi:",
                    String.valueOf(r.getJumlah()));
            if (in == null) return;
            try {
                hasil = Integer.parseInt(in.trim());
                if (hasil < 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                UiUtil.warn(this, "Jumlah hasil harus berupa bilangan bulat >= 0.");
                return;
            }
        }
        try {
            dao.updateStatus(r.getId(), status, hasil);
            refresh();
        } catch (SQLException ex) {
            UiUtil.sqlError(this, ex);
        }
    }

    private void hapus() {
        RencanaProduksi r = getSelected();
        if (r == null) return;
        if (!UiUtil.confirm(this, "Hapus rencana produksi \"" + r.getNamaProduk() + "\"?")) return;
        try {
            dao.delete(r.getId());
            refresh();
        } catch (SQLException ex) {
            UiUtil.sqlError(this, ex);
        }
    }
}

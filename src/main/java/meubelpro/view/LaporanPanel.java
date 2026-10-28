package meubelpro.view;

import java.awt.*;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import meubelpro.dao.ProduksiDAO;
import meubelpro.model.RencanaProduksi;

/** Laporan ringkasan rencana dan hasil produksi. */
public class LaporanPanel extends RefreshablePanel {

    private final ProduksiDAO dao = new ProduksiDAO();
    private final JLabel lblTotal = new JLabel("0");
    private final JLabel lblRencana = new JLabel("0");
    private final JLabel lblKerja = new JLabel("0");
    private final JLabel lblSelesai = new JLabel("0");
    private final JLabel lblUnitRencana = new JLabel("0");
    private final JLabel lblUnitHasil = new JLabel("0");
    private final DefaultTableModel model = UiUtil.readOnlyModel(
            "Produk", "Rencana (unit)", "Tgl Mulai", "Tgl Selesai", "Status", "Hasil (unit)", "Pencapaian");
    private final JTable table = UiUtil.table(model);

    public LaporanPanel() {
        setLayout(new BorderLayout(10, 15));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setBackground(UiUtil.BG);

        JPanel atas = new JPanel(new BorderLayout(0, 10));
        atas.setOpaque(false);
        atas.add(UiUtil.title("Laporan Produksi"), BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(2, 3, 12, 12));
        grid.setOpaque(false);
        grid.add(UiUtil.statCard("Total Rencana", lblTotal));
        grid.add(UiUtil.statCard("Direncanakan", lblRencana));
        grid.add(UiUtil.statCard("Sedang Dikerjakan", lblKerja));
        grid.add(UiUtil.statCard("Selesai", lblSelesai));
        grid.add(UiUtil.statCard("Total Unit Direncanakan", lblUnitRencana));
        grid.add(UiUtil.statCard("Total Unit Dihasilkan", lblUnitHasil));
        atas.add(grid, BorderLayout.CENTER);
        add(atas, BorderLayout.NORTH);

        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton btnRefresh = UiUtil.button("Refresh Laporan");
        btnRefresh.addActionListener(e -> refresh());
        JPanel bawah = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bawah.setOpaque(false);
        bawah.add(btnRefresh);
        add(bawah, BorderLayout.SOUTH);
    }

    @Override
    public void refresh() {
        try {
            lblTotal.setText(String.valueOf(dao.countByStatus(null)));
            lblRencana.setText(String.valueOf(dao.countByStatus(RencanaProduksi.DIRENCANAKAN)));
            lblKerja.setText(String.valueOf(dao.countByStatus(RencanaProduksi.DIKERJAKAN)));
            lblSelesai.setText(String.valueOf(dao.countByStatus(RencanaProduksi.SELESAI)));
            int[] total = dao.getTotalUnit();
            lblUnitRencana.setText(String.valueOf(total[0]));
            lblUnitHasil.setText(String.valueOf(total[1]));

            model.setRowCount(0);
            for (RencanaProduksi r : dao.getJadwal(null)) {
                boolean selesai = RencanaProduksi.SELESAI.equals(r.getStatus());
                String capaian = selesai && r.getJumlah() > 0
                        ? String.format("%.0f%%", r.getJumlahHasil() * 100.0 / r.getJumlah()) : "-";
                model.addRow(new Object[]{r.getNamaProduk(), r.getJumlah(), r.getTanggalMulai(),
                        r.getTanggalSelesai(), r.getStatus(), selesai ? r.getJumlahHasil() : "-", capaian});
            }
        } catch (SQLException ex) {
            UiUtil.sqlError(this, ex);
        }
    }
}

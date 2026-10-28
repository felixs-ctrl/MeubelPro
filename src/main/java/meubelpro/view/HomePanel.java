package meubelpro.view;

import java.awt.*;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import meubelpro.dao.*;
import meubelpro.model.RencanaProduksi;

/** Halaman awal dashboard: ringkasan angka penting. */
public class HomePanel extends RefreshablePanel {

    private final JLabel lblProduk = new JLabel("0");
    private final JLabel lblBahan = new JLabel("0");
    private final JLabel lblRencana = new JLabel("0");
    private final JLabel lblKerja = new JLabel("0");
    private final JLabel lblSelesai = new JLabel("0");

    private final ProdukDAO produkDAO = new ProdukDAO();
    private final BahanBakuDAO bahanDAO = new BahanBakuDAO();
    private final ProduksiDAO produksiDAO = new ProduksiDAO();

    public HomePanel() {
        setLayout(new BorderLayout(10, 20));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(UiUtil.BG);

        add(UiUtil.title("Ringkasan Sistem"), BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(2, 3, 16, 16));
        grid.setOpaque(false);
        grid.add(UiUtil.statCard("Jumlah Produk", lblProduk));
        grid.add(UiUtil.statCard("Jenis Bahan Baku", lblBahan));
        grid.add(UiUtil.statCard("Total Rencana Produksi", lblRencana));
        grid.add(UiUtil.statCard("Sedang Dikerjakan", lblKerja));
        grid.add(UiUtil.statCard("Produksi Selesai", lblSelesai));

        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);
        wrap.add(grid, BorderLayout.NORTH);
        add(wrap, BorderLayout.CENTER);
    }

    @Override
    public void refresh() {
        try {
            lblProduk.setText(String.valueOf(produkDAO.count()));
            lblBahan.setText(String.valueOf(bahanDAO.count()));
            lblRencana.setText(String.valueOf(produksiDAO.countByStatus(null)));
            lblKerja.setText(String.valueOf(produksiDAO.countByStatus(RencanaProduksi.DIKERJAKAN)));
            lblSelesai.setText(String.valueOf(produksiDAO.countByStatus(RencanaProduksi.SELESAI)));
        } catch (SQLException ex) {
            UiUtil.sqlError(this, ex);
        }
    }
}

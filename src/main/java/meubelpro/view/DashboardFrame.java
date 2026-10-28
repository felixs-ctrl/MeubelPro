package meubelpro.view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import meubelpro.model.Admin;

/** Dashboard admin: menu di kiri, konten (CardLayout) di tengah. */
public class DashboardFrame extends JFrame {

    private final CardLayout cards = new CardLayout();
    private final JPanel content = new JPanel(cards);

    public DashboardFrame(Admin admin) {
        setTitle("MeubelPro - Dashboard Admin");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 680);
        setLocationRelativeTo(null);

        // ----- Header -----
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UiUtil.PRIMARY);
        header.setBorder(new EmptyBorder(10, 16, 10, 16));
        JLabel lblApp = new JLabel("MeubelPro - Dashboard Admin");
        lblApp.setForeground(Color.WHITE);
        lblApp.setFont(lblApp.getFont().deriveFont(Font.BOLD, 18f));
        JLabel lblUser = new JLabel("Halo, " + admin.getNamaLengkap() + "   ");
        lblUser.setForeground(Color.WHITE);
        JButton btnLogout = UiUtil.button("Logout");
        btnLogout.addActionListener(e -> {
            if (UiUtil.confirm(this, "Yakin ingin logout?")) {
                dispose();
                new LoginFrame().setVisible(true);
            }
        });
        JPanel kanan = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        kanan.setOpaque(false);
        kanan.add(lblUser);
        kanan.add(btnLogout);
        header.add(lblApp, BorderLayout.WEST);
        header.add(kanan, BorderLayout.EAST);

        // ----- Panel-panel konten -----
        RefreshablePanel home = new HomePanel();
        RefreshablePanel produk = new ProdukPanel();
        RefreshablePanel bahan = new BahanBakuPanel();
        RefreshablePanel produksi = new ProduksiPanel();
        RefreshablePanel laporan = new LaporanPanel();
        content.add(home, "home");
        content.add(produk, "produk");
        content.add(bahan, "bahan");
        content.add(produksi, "produksi");
        content.add(laporan, "laporan");

        // ----- Menu samping -----
        JPanel menu = new JPanel(new GridLayout(0, 1, 0, 6));
        menu.setBorder(new EmptyBorder(14, 10, 14, 10));
        menu.setBackground(UiUtil.ACCENT);
        addMenu(menu, "Beranda", "home", home);
        addMenu(menu, "Kelola Produk", "produk", produk);
        addMenu(menu, "Kelola Bahan Baku", "bahan", bahan);
        addMenu(menu, "Kelola Produksi", "produksi", produksi);
        addMenu(menu, "Laporan", "laporan", laporan);

        JPanel menuWrap = new JPanel(new BorderLayout());
        menuWrap.add(menu, BorderLayout.NORTH);
        menuWrap.setBackground(UiUtil.ACCENT);
        menuWrap.setPreferredSize(new Dimension(210, 0));

        JPanel root = new JPanel(new BorderLayout());
        root.add(header, BorderLayout.NORTH);
        root.add(menuWrap, BorderLayout.WEST);
        root.add(content, BorderLayout.CENTER);
        setContentPane(root);

        home.refresh();
    }

    private void addMenu(JPanel menu, String text, String card, RefreshablePanel panel) {
        JButton b = UiUtil.button(text);
        b.setFont(b.getFont().deriveFont(Font.BOLD, 13f));
        b.setPreferredSize(new Dimension(180, 40));
        b.addActionListener(e -> {
            cards.show(content, card);
            panel.refresh();      // muat ulang data terbaru dari database
        });
        menu.add(b);
    }
}

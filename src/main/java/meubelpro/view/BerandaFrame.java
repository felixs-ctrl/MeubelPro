package meubelpro.view;

import java.awt.*;
import javax.swing.*;

/** Halaman Beranda (landing page) - pintu masuk menuju Login. */
public class BerandaFrame extends JFrame {

    public BerandaFrame() {
        setTitle("MeubelPro - Beranda");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(560, 380);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(UiUtil.BG);

        JPanel box = new JPanel();
        box.setOpaque(false);
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));

        JLabel logo = new JLabel("MeubelPro");
        logo.setFont(logo.getFont().deriveFont(Font.BOLD, 44f));
        logo.setForeground(UiUtil.PRIMARY);
        logo.setAlignmentX(CENTER_ALIGNMENT);

        JLabel sub = new JLabel("Sistem Pengelolaan & Perencanaan Produksi Mebel");
        sub.setFont(sub.getFont().deriveFont(15f));
        sub.setAlignmentX(CENTER_ALIGNMENT);

        JButton btnMasuk = UiUtil.button("Masuk ke Sistem");
        btnMasuk.setFont(btnMasuk.getFont().deriveFont(Font.BOLD, 15f));
        btnMasuk.setAlignmentX(CENTER_ALIGNMENT);
        btnMasuk.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        box.add(logo);
        box.add(Box.createVerticalStrut(8));
        box.add(sub);
        box.add(Box.createVerticalStrut(35));
        box.add(btnMasuk);
        root.add(box);
        setContentPane(root);
    }
}

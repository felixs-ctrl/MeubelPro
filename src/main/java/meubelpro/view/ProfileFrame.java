package meubelpro.view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import meubelpro.model.Admin;

/** Halaman Profile admin setelah login, lalu lanjut ke Dashboard. */
public class ProfileFrame extends JFrame {

    public ProfileFrame(Admin admin) {
        setTitle("MeubelPro - Profile");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(440, 330);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout(10, 15));
        root.setBorder(new EmptyBorder(20, 30, 20, 30));
        root.setBackground(UiUtil.BG);

        JLabel judul = UiUtil.title("Profil Admin");
        judul.setHorizontalAlignment(SwingConstants.CENTER);
        root.add(judul, BorderLayout.NORTH);

        JPanel info = UiUtil.formPanel(
                new String[]{"Nama Lengkap", "Username", "Email"},
                new JComponent[]{
                    new JLabel(admin.getNamaLengkap()),
                    new JLabel(admin.getUsername()),
                    new JLabel(admin.getEmail() == null ? "-" : admin.getEmail())});
        info.setOpaque(false);
        root.add(info, BorderLayout.CENTER);

        JButton btnLanjut = UiUtil.button("Lanjut ke Dashboard");
        btnLanjut.addActionListener(e -> {
            dispose();
            new DashboardFrame(admin).setVisible(true);   // alur: Profile -> Dashboard
        });
        JPanel south = new JPanel();
        south.setOpaque(false);
        south.add(btnLanjut);
        root.add(south, BorderLayout.SOUTH);

        setContentPane(root);
    }
}

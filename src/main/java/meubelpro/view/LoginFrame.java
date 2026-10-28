package meubelpro.view;

import java.awt.*;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import meubelpro.dao.AdminDAO;
import meubelpro.model.Admin;

public class LoginFrame extends JFrame {

    private final JTextField txtUsername = new JTextField(18);
    private final JPasswordField txtPassword = new JPasswordField(18);
    private final AdminDAO adminDAO = new AdminDAO();

    public LoginFrame() {
        setTitle("MeubelPro - Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(420, 320);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(new EmptyBorder(20, 30, 20, 30));
        root.setBackground(UiUtil.BG);

        JLabel judul = UiUtil.title("Login Admin");
        judul.setHorizontalAlignment(SwingConstants.CENTER);
        root.add(judul, BorderLayout.NORTH);

        JPanel form = UiUtil.formPanel(new String[]{"Username", "Password"},
                new JComponent[]{txtUsername, txtPassword});
        form.setOpaque(false);
        root.add(form, BorderLayout.CENTER);

        JButton btnLogin = UiUtil.button("Login");
        JButton btnKembali = UiUtil.button("Kembali");
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        btns.setOpaque(false);
        btns.add(btnLogin);
        btns.add(btnKembali);
        root.add(btns, BorderLayout.SOUTH);

        btnLogin.addActionListener(e -> prosesLogin());
        btnKembali.addActionListener(e -> { dispose(); new BerandaFrame().setVisible(true); });
        getRootPane().setDefaultButton(btnLogin);   // tekan Enter = Login

        setContentPane(root);
    }

    private void prosesLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            UiUtil.warn(this, "Username dan password wajib diisi.");
            return;
        }
        try {
            Admin admin = adminDAO.login(username, password);
            if (admin == null) {
                UiUtil.warn(this, "Username atau password salah.");
                txtPassword.setText("");
                return;
            }
            dispose();
            new ProfileFrame(admin).setVisible(true);   // alur: Login -> Profile
        } catch (SQLException ex) {
            UiUtil.sqlError(this, ex);
        }
    }
}

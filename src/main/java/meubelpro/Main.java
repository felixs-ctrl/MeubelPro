package meubelpro;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import meubelpro.config.SpringConfig;
import meubelpro.config.AppServices;

import meubelpro.dao.AdminDAO;
import meubelpro.dao.ProdukDAO;
import meubelpro.dao.BahanBakuDAO;
import meubelpro.dao.ProduksiDAO;

import meubelpro.view.BerandaFrame;

public class Main {

    public static void main(String[] args) {

        // mengatur tampilan nimbus.
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {
        }

        // menjalankan spring container.
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(SpringConfig.class);

        // mendaftarkan shutdown hook spring.
        context.registerShutdownHook();

        // mengambil seluruh dao dari spring.
        AppServices services = new AppServices(
                context.getBean(AdminDAO.class),
                context.getBean(ProdukDAO.class),
                context.getBean(BahanBakuDAO.class),
                context.getBean(ProduksiDAO.class)
        );

        // membuka halaman beranda.
        SwingUtilities.invokeLater(() -> {
            BerandaFrame frame = new BerandaFrame(services);
            frame.setVisible(true);
        });
    }
}

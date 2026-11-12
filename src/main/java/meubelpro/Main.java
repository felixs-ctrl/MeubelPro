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

        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {
        }

        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(SpringConfig.class);

        context.registerShutdownHook();

        AppServices services = new AppServices(
                context.getBean(AdminDAO.class),
                context.getBean(ProdukDAO.class),
                context.getBean(BahanBakuDAO.class),
                context.getBean(ProduksiDAO.class)
        );

        SwingUtilities.invokeLater(() -> {
            BerandaFrame frame = new BerandaFrame(services);
            frame.setVisible(true);
        });
    }
}

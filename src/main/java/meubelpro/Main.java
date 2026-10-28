package meubelpro;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
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
        SwingUtilities.invokeLater(() -> new BerandaFrame().setVisible(true));
    }
}

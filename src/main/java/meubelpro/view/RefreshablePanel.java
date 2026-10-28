package meubelpro.view;

import javax.swing.JPanel;

/** Panel dasar yang dapat memuat ulang datanya dari database setiap kali ditampilkan. */
public abstract class RefreshablePanel extends JPanel {
    public abstract void refresh();
}

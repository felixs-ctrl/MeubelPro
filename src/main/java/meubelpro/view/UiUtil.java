package meubelpro.view;

import java.awt.*;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

/** Kumpulan helper tampilan agar kode panel tetap ringkas. */
public final class UiUtil {

    public static final Color PRIMARY = new Color(111, 78, 55);   // coklat kayu
    public static final Color ACCENT  = new Color(210, 170, 110);
    public static final Color BG      = new Color(250, 246, 240);

    private UiUtil() { }

    public static JButton button(String text) {
        JButton b = new JButton(text);
        b.setFocusPainted(false);
        return b;
    }

    public static JLabel title(String text) {
        JLabel l = new JLabel(text);
        l.setFont(l.getFont().deriveFont(Font.BOLD, 22f));
        l.setForeground(PRIMARY);
        return l;
    }

    /** Model tabel yang tidak bisa diedit langsung oleh pengguna. */
    public static DefaultTableModel readOnlyModel(String... columns) {
        return new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
    }

    public static JTable table(DefaultTableModel model) {
        JTable t = new JTable(model);
        t.setRowHeight(26);
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        t.getTableHeader().setReorderingAllowed(false);
        return t;
    }

    /** Form dua kolom (label - input) memakai GridBagLayout. */
    public static JPanel formPanel(String[] labels, JComponent[] fields) {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 4, 4, 4);
        g.anchor = GridBagConstraints.NORTHWEST;
        for (int i = 0; i < labels.length; i++) {
            g.gridy = i;
            g.gridx = 0; g.weightx = 0; g.fill = GridBagConstraints.NONE;
            p.add(new JLabel(labels[i]), g);
            g.gridx = 1; g.weightx = 1; g.fill = GridBagConstraints.HORIZONTAL;
            p.add(fields[i], g);
        }
        return p;
    }

    /** Kartu statistik kecil untuk dashboard & laporan. */
    public static JPanel statCard(String caption, JLabel valueLabel) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT, 2), new EmptyBorder(14, 16, 14, 16)));
        valueLabel.setFont(valueLabel.getFont().deriveFont(Font.BOLD, 30f));
        valueLabel.setForeground(PRIMARY);
        JLabel cap = new JLabel(caption);
        cap.setForeground(Color.DARK_GRAY);
        card.add(valueLabel, BorderLayout.CENTER);
        card.add(cap, BorderLayout.SOUTH);
        return card;
    }

    public static void info(Component parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg, "Informasi", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void warn(Component parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg, "Peringatan", JOptionPane.WARNING_MESSAGE);
    }

    public static boolean confirm(Component parent, String msg) {
        return JOptionPane.showConfirmDialog(parent, msg, "Konfirmasi",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
    }

    /** Menerjemahkan SQLException menjadi pesan yang mudah dipahami pengguna. */
    public static void sqlError(Component parent, SQLException e) {
        e.printStackTrace();
        String msg;
        String state = e.getSQLState();
        if (state != null && state.startsWith("08")) {
            msg = "Tidak dapat terhubung ke MySQL.\nPastikan server MySQL (XAMPP/Service) sudah berjalan.";
        } else {
            switch (e.getErrorCode()) {
                case 1062: msg = "Data gagal disimpan: kode sudah digunakan."; break;
                case 1451: msg = "Data tidak dapat dihapus karena masih dipakai data lain\n(mis. rencana produksi)."; break;
                case 1049: msg = "Database 'meubelpro' belum dibuat.\nJalankan skrip database/meubelpro.sql terlebih dahulu."; break;
                case 1045: msg = "Username/password MySQL salah.\nPeriksa DatabaseConnection.java."; break;
                default:   msg = "Terjadi kesalahan database:\n" + e.getMessage();
            }
        }
        JOptionPane.showMessageDialog(parent, msg, "Kesalahan Database", JOptionPane.ERROR_MESSAGE);
    }
}

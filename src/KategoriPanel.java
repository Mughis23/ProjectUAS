import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class KategoriPanel extends JFrame implements ActionListener {
  JTextField tfKategori;
  JButton btnSimpan;

  public KategoriPanel() {
    setTitle("Panel Kategori Buku");
    setSize(300, 300);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    JLabel lblKategori = new JLabel("Nama Kategori:");
    tfKategori = new JTextField(100);
    btnSimpan = new JButton("Simpan");
    btnSimpan.addActionListener(this);

    JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    panel.add(lblKategori);
    panel.add(tfKategori);
    panel.add(btnSimpan);

    add(panel);
    setVisible(true);
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == btnSimpan) {
      try (Connection conn = perpustakaanDB.getConnection()) {
        String kategori = tfKategori.getText().trim();
        if (kategori.isEmpty()) {
          JOptionPane.showMessageDialog(this, "Nama kategori tidak boleh kosong!", "Error", JOptionPane.ERROR_MESSAGE);
          return;
        }

        String sql = "INSERT INTO kategori (nama_kategori) VALUES (?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, kategori);

        pstmt.executeUpdate();
        JOptionPane.showMessageDialog(this, "Kategori berhasil disimpan!");
        dispose();

      } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Input tidak valid!", "Error", JOptionPane.ERROR_MESSAGE);
      } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Gagal menyimpan kategori: " + ex.getMessage(), "Error",
            JOptionPane.ERROR_MESSAGE);
      } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + ex.getMessage(), "Error",
            JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> new KategoriPanel());
  }
}

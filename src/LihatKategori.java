import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class LihatKategori extends JFrame {
  JTable tabelKategori;
  DefaultTableModel model;

  public LihatKategori() {
    setTitle("Data Kategori");
    setSize(400, 300);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    // Setup kolom tabel
    String[] kolom = { "ID Kategori", "Nama Kategori" };
    model = new DefaultTableModel(kolom, 0);
    tabelKategori = new JTable(model);

    // Scroll Pane
    JScrollPane scrollPane = new JScrollPane(tabelKategori);
    add(scrollPane, BorderLayout.CENTER);

    // Ambil data dari database
    tampilkanData();

    setVisible(true);
  }

  private void tampilkanData() {
    try (Connection conn = perpustakaanDB.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM kategori")) {

      while (rs.next()) {
        int id = rs.getInt("id_kategori");
        String nama = rs.getString("nama_kategori");
        model.addRow(new Object[] { id, nama });
      }

    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(this, "Gagal mengambil data kategori: " + ex.getMessage(),
          "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(LihatKategori::new);
  }
}

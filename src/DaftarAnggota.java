import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class DaftarAnggota extends JFrame {
  JTable table;
  DefaultTableModel model;

  public DaftarAnggota() {
    setTitle("Daftar Anggota");
    setSize(700, 400);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    // Setup kolom tabel
    model = new DefaultTableModel();
    model.addColumn("ID Anggota");
    model.addColumn("Nama");
    model.addColumn("Alamat");
    model.addColumn("Telepon");
    model.addColumn("Tanggal Daftar");

    table = new JTable(model);
    JScrollPane scrollPane = new JScrollPane(table);
    add(scrollPane, BorderLayout.CENTER);

    loadData();

    setVisible(true);
  }

  private void loadData() {
    try (Connection conn = perpustakaanDB.getConnection()) {
      String query = "SELECT * FROM anggota";
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery(query);

      while (rs.next()) {
        model.addRow(new Object[] {
            rs.getInt("id_anggota"),
            rs.getString("nama"),
            rs.getString("alamat"),
            rs.getString("telepon"),
            rs.getDate("tanggal_daftar")
        });
      }

    } catch (SQLException e) {
      JOptionPane.showMessageDialog(this, "Gagal memuat data anggota: " + e.getMessage());
    }
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(DaftarAnggota::new);
  }
}

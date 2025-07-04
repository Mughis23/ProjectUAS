import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class DaftarPeminjaman extends JFrame {
  JTable table;
  DefaultTableModel model;

  public DaftarPeminjaman() {
    setTitle("Daftar Peminjaman");
    setSize(800, 400);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    model = new DefaultTableModel();
    model.addColumn("ID Pinjam");
    model.addColumn("Judul Buku");
    model.addColumn("Nama Anggota");
    model.addColumn("Tanggal Pinjam");
    model.addColumn("Tanggal Kembali");
    model.addColumn("Status");

    table = new JTable(model);
    JScrollPane scrollPane = new JScrollPane(table);
    add(scrollPane, BorderLayout.CENTER);

    loadData();

    setVisible(true);
  }

  private void loadData() {
    try (Connection conn = perpustakaanDB.getConnection()) {
      String query = "SELECT p.id_pinjam, b.judul, a.nama, p.tgl_pinjam, p.tgl_kembali, p.status " +
          "FROM peminjaman p " +
          "JOIN buku b ON p.id_buku = b.id_buku " +
          "JOIN anggota a ON p.id_anggota = a.id_anggota";

      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery(query);

      while (rs.next()) {
        model.addRow(new Object[] {
            rs.getInt("id_pinjam"),
            rs.getString("judul"),
            rs.getString("nama"),
            rs.getDate("tgl_pinjam"),
            rs.getDate("tgl_kembali"),
            rs.getString("status")
        });
      }

    } catch (SQLException e) {
      JOptionPane.showMessageDialog(this, "Gagal memuat data peminjaman: " + e.getMessage());
    }
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(DaftarPeminjaman::new);
  }
}

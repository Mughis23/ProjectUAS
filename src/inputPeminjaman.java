import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class inputPeminjaman extends JFrame implements ActionListener {
  JTextField tfIdPinjam, tfIdBuku, tfIdAnggota, tfTglPinjam, tfTglKembali;
  JComboBox<String> cbStatus;
  JButton btnSimpan;

  public inputPeminjaman() {
    setTitle("Form Peminjaman Buku");
    setSize(400, 350);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    JLabel lblIdPinjam = new JLabel("ID Peminjaman:");
    JLabel lblIdBuku = new JLabel("ID Buku:");
    JLabel lblIdAnggota = new JLabel("ID Anggota:");
    JLabel lblTglPinjam = new JLabel("Tanggal Pinjam (YYYY-MM-DD):");
    JLabel lblTglKembali = new JLabel("Tanggal Kembali (YYYY-MM-DD):");
    JLabel lblStatus = new JLabel("Status:");

    tfIdPinjam = new JTextField(10);
    tfIdBuku = new JTextField(10);
    tfIdAnggota = new JTextField(10);
    tfTglPinjam = new JTextField(10);
    tfTglKembali = new JTextField(10);
    cbStatus = new JComboBox<>(new String[] { "dipinjam", "kembali" });
    btnSimpan = new JButton("Simpan");

    btnSimpan.addActionListener(this);

    JPanel panel = new JPanel(new GridLayout(7, 2, 5, 5));
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    panel.add(lblIdPinjam);
    panel.add(tfIdPinjam);
    panel.add(lblIdBuku);
    panel.add(tfIdBuku);
    panel.add(lblIdAnggota);
    panel.add(tfIdAnggota);
    panel.add(lblTglPinjam);
    panel.add(tfTglPinjam);
    panel.add(lblTglKembali);
    panel.add(tfTglKembali);
    panel.add(lblStatus);
    panel.add(cbStatus);
    panel.add(new JLabel(""));
    panel.add(btnSimpan);

    add(panel);
    setVisible(true);
  }

  public void actionPerformed(ActionEvent e) {
    String idPinjam = tfIdPinjam.getText().trim();
    String idBuku = tfIdBuku.getText().trim();
    String idAnggota = tfIdAnggota.getText().trim();
    String tglPinjam = tfTglPinjam.getText().trim();
    String tglKembali = tfTglKembali.getText().trim();
    String status = cbStatus.getSelectedItem().toString();

    if (idPinjam.isEmpty() || idBuku.isEmpty() || idAnggota.isEmpty() ||
        tglPinjam.isEmpty() || tglKembali.isEmpty()) {
      JOptionPane.showMessageDialog(this, "Semua field harus diisi!");
      return;
    }

    try (Connection conn = perpustakaanDB.getConnection()) {
      String sql = "INSERT INTO peminjaman (id_pinjam, id_buku, id_anggota, tgl_pinjam, tgl_kembali, status) " +
          "VALUES (?, ?, ?, ?, ?, ?)";
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setString(1, idPinjam);
      ps.setString(2, idBuku);
      ps.setString(3, idAnggota);
      ps.setString(4, tglPinjam);
      ps.setString(5, tglKembali);
      ps.setString(6, status);

      ps.executeUpdate();
      JOptionPane.showMessageDialog(this, "Peminjaman berhasil disimpan.");
      dispose();
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(this, "Gagal menyimpan: " + ex.getMessage());
    }
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(inputPeminjaman::new);
  }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class TambahBuku extends JFrame implements ActionListener {
  JTextField tfIdBuku, tfJudul, tfPengarang, tfPenerbit, tfTahunTerbit;
  JComboBox<String> cbKategori;
  JButton btnSimpan;

  public TambahBuku() {
    setTitle("Form Tambah Buku");
    setSize(400, 350);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    JLabel lblId = new JLabel("ID Buku:");
    JLabel lblJudul = new JLabel("Judul:");
    JLabel lblPengarang = new JLabel("Pengarang:");
    JLabel lblPenerbit = new JLabel("Penerbit:");
    JLabel lblTahun = new JLabel("Tahun Terbit:");
    JLabel lblKategori = new JLabel("Kategori:");

    tfIdBuku = new JTextField();
    tfJudul = new JTextField();
    tfPengarang = new JTextField();
    tfPenerbit = new JTextField();
    tfTahunTerbit = new JTextField();
    cbKategori = new JComboBox<>();

    btnSimpan = new JButton("Simpan");
    btnSimpan.addActionListener(this);

    // Panel dan layout
    JPanel panel = new JPanel(new GridLayout(7, 2, 5, 5));
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    panel.add(lblId);
    panel.add(tfIdBuku);
    panel.add(lblJudul);
    panel.add(tfJudul);
    panel.add(lblPengarang);
    panel.add(tfPengarang);
    panel.add(lblPenerbit);
    panel.add(tfPenerbit);
    panel.add(lblTahun);
    panel.add(tfTahunTerbit);
    panel.add(lblKategori);
    panel.add(cbKategori);
    panel.add(new JLabel());
    panel.add(btnSimpan);

    add(panel);

    // Load kategori dari DB ke combobox
    loadKategori();

    setVisible(true);
  }

  private void loadKategori() {
    try (Connection conn = perpustakaanDB.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM kategori")) {

      while (rs.next()) {
        int id = rs.getInt("id_kategori");
        String nama = rs.getString("nama_kategori");
        cbKategori.addItem(id + " - " + nama);
      }

    } catch (SQLException e) {
      JOptionPane.showMessageDialog(this, "Gagal mengambil kategori: " + e.getMessage());
    }
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == btnSimpan) {
      String id = tfIdBuku.getText().trim();
      String judul = tfJudul.getText().trim();
      String pengarang = tfPengarang.getText().trim();
      String penerbit = tfPenerbit.getText().trim();
      String tahun = tfTahunTerbit.getText().trim();
      String kategori = (String) cbKategori.getSelectedItem();

      if (id.isEmpty() || judul.isEmpty() || pengarang.isEmpty() || penerbit.isEmpty() || tahun.isEmpty()
          || kategori == null) {
        JOptionPane.showMessageDialog(this, "Semua kolom harus diisi.");
        return;
      }

      try {
        int tahunTerbit = Integer.parseInt(tahun);
        int idKategori = Integer.parseInt(kategori.split(" - ")[0]);

        try (Connection conn = perpustakaanDB.getConnection()) {
          String sql = "INSERT INTO buku (id_buku, judul, pengarang, penerbit, tahun_terbit, kategori_id) VALUES (?, ?, ?, ?, ?, ?)";
          PreparedStatement ps = conn.prepareStatement(sql);
          ps.setString(1, id);
          ps.setString(2, judul);
          ps.setString(3, pengarang);
          ps.setString(4, penerbit);
          ps.setInt(5, tahunTerbit);
          ps.setInt(6, idKategori);

          ps.executeUpdate();
          JOptionPane.showMessageDialog(this, "Buku berhasil ditambahkan.");
          dispose();
        }
      } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Tahun harus berupa angka.");
      } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Gagal menyimpan data: " + ex.getMessage());
      }
    }
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(TambahBuku::new);
  }
}

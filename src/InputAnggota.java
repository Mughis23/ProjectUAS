import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;

public class InputAnggota extends JFrame implements ActionListener {
    JTextField tfNama, tfAlamat, tfTelepon;
    JButton btnSimpan;

    public InputAnggota() {
        setTitle("Form Tambah Anggota");
        setSize(350, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JLabel lblNama = new JLabel("Nama:");
        JLabel lblAlamat = new JLabel("Alamat:");
        JLabel lblTelepon = new JLabel("Telepon:");

        tfNama = new JTextField();
        tfAlamat = new JTextField();
        tfTelepon = new JTextField();

        btnSimpan = new JButton("Simpan");
        btnSimpan.addActionListener(this);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(lblNama);
        panel.add(tfNama);
        panel.add(lblAlamat);
        panel.add(tfAlamat);
        panel.add(lblTelepon);
        panel.add(tfTelepon);
        panel.add(new JLabel()); // Kosong
        panel.add(btnSimpan);

        add(panel);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String nama = tfNama.getText().trim();
        String alamat = tfAlamat.getText().trim();
        String telepon = tfTelepon.getText().trim();
        String tanggalDaftar = LocalDate.now().toString(); // YYYY-MM-DD

        if (nama.isEmpty() || alamat.isEmpty() || telepon.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua kolom harus diisi.");
            return;
        }

        try (Connection conn = perpustakaanDB.getConnection()) {
            String sql = "INSERT INTO anggota (nama, alamat, telepon, tanggal_daftar) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nama);
            ps.setString(2, alamat);
            ps.setString(3, telepon);
            ps.setString(4, tanggalDaftar);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Anggota berhasil ditambahkan.");
            dispose(); // Tutup form setelah simpan
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan data: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(InputAnggota::new);
    }
}

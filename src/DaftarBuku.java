import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class DaftarBuku extends JFrame implements ActionListener {
    JTable table;
    DefaultTableModel model;
    JButton btnEdit, btnHapus, btnRefresh;

    public DaftarBuku() {
        setTitle("Daftar Buku");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        model = new DefaultTableModel(new String[] {
                "ID Buku", "Judul", "Pengarang", "Penerbit", "Tahun Terbit", "ID Kategori"
        }, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        btnEdit = new JButton("Edit");
        btnHapus = new JButton("Hapus");
        btnRefresh = new JButton("Refresh");

        JPanel panelBawah = new JPanel();
        panelBawah.add(btnEdit);
        panelBawah.add(btnHapus);
        panelBawah.add(btnRefresh);

        add(scrollPane, BorderLayout.CENTER);
        add(panelBawah, BorderLayout.SOUTH);

        btnEdit.addActionListener(this);
        btnHapus.addActionListener(this);
        btnRefresh.addActionListener(this);

        loadData();

        setVisible(true);
    }

    private void loadData() {
        model.setRowCount(0);
        try (Connection conn = perpustakaanDB.getConnection()) {
            String sql = "SELECT * FROM buku";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            while (rs.next()) {
                model.addRow(new Object[] {
                        rs.getString("id_buku"),
                        rs.getString("judul"),
                        rs.getString("pengarang"),
                        rs.getString("penerbit"),
                        rs.getString("tahun_terbit"),
                        rs.getInt("kategori_id")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int selected = table.getSelectedRow();
        if (selected == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris terlebih dahulu.");
            return;
        }

        String idBuku = model.getValueAt(selected, 0).toString();

        if (e.getSource() == btnHapus) {
            int konfirmasi = JOptionPane.showConfirmDialog(this,
                    "Yakin ingin menghapus buku ID: " + idBuku + "?",
                    "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
            if (konfirmasi == JOptionPane.YES_OPTION) {
                try (Connection conn = perpustakaanDB.getConnection()) {
                    PreparedStatement ps = conn.prepareStatement("DELETE FROM buku WHERE id_buku = ?");
                    ps.setString(1, idBuku);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Data berhasil dihapus.");
                    loadData();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Gagal menghapus data: " + ex.getMessage());
                }
            }

        } else if (e.getSource() == btnEdit) {
            String judul = JOptionPane.showInputDialog(this, "Judul Baru:", model.getValueAt(selected, 1));
            String pengarang = JOptionPane.showInputDialog(this, "Pengarang Baru:", model.getValueAt(selected, 2));
            String penerbit = JOptionPane.showInputDialog(this, "Penerbit Baru:", model.getValueAt(selected, 3));
            String tahun = JOptionPane.showInputDialog(this, "Tahun Terbit Baru:", model.getValueAt(selected, 4));
            String kategori = JOptionPane.showInputDialog(this, "ID Kategori Baru:", model.getValueAt(selected, 5));

            if (judul == null || pengarang == null || penerbit == null || tahun == null || kategori == null)
                return;

            try (Connection conn = perpustakaanDB.getConnection()) {
                String sql = "UPDATE buku SET judul=?, pengarang=?, penerbit=?, tahun_terbit=?, kategori_id=? WHERE id_buku=?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, judul);
                ps.setString(2, pengarang);
                ps.setString(3, penerbit);
                ps.setString(4, tahun);
                ps.setString(5, kategori);
                ps.setString(6, idBuku);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Data berhasil diperbarui.");
                loadData();

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Gagal memperbarui data: " + ex.getMessage());
            }

        } else if (e.getSource() == btnRefresh) {
            loadData();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DaftarBuku::new);
    }
}

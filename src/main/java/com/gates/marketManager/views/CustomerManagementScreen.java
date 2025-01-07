/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.gates.marketManager.views;


import com.gates.marketManager.util.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Vector;
import javax.swing.table.TableRowSorter;


/**
 *
 * @author gates
 */

public class CustomerManagementScreen extends javax.swing.JFrame {

    private TableRowSorter<DefaultTableModel> rowSorter;
    public CustomerManagementScreen() {
        initComponents();
        loadCustomers();
        
        DefaultTableModel tableModel = (DefaultTableModel) customerTable.getModel();

        rowSorter = new TableRowSorter<>(tableModel);
        customerTable.setRowSorter(rowSorter);
    }
    private void filterTable() {
            String text = searchField.getText();
            if (text.trim().isEmpty()) {
                rowSorter.setRowFilter(null); // Mostra tudo
            } else {
                rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
            }
        }
    
    private void loadCustomers() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM customers";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            DefaultTableModel model = (DefaultTableModel) customerTable.getModel();
            model.setRowCount(0); // Limpa a tabela
            while (rs.next()) {
                Vector<String> row = new Vector<>();
                row.add(String.valueOf(rs.getInt("id")));
                row.add(rs.getString("name"));
                row.add(rs.getString("phone"));
                row.add(rs.getString("email"));
                model.addRow(row);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados: " + ex.getMessage());
        }
    }
    
    private void handleAddCustomer() {
        String name = nameField.getText();
        String phone = phoneField.getText();
        String email = emailField.getText();

        if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO customers (name, phone, email) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, phone);
            stmt.setString(3, email);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Cliente adicionado com sucesso!");
            clearFields();
            loadCustomers();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao adicionar cliente, entre em contato com a empresa desenvolvedora!");
            clearFields();
        }
    }
    
    private void handleDeleteCustomer() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente para excluir!");
            return;
        }

        int customerId = Integer.parseInt(customerTable.getValueAt(selectedRow, 0).toString());
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Tem certeza de que deseja excluir este cliente?", 
            "Confirmação de exclusão", 
            JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {
            return; // Se o usuário não confirmar, interrompe o processo
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "DELETE FROM customers WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, customerId);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Cliente excluído com sucesso!");
            clearFields();
            loadCustomers();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir cliente, entre em contato com a empresa desenvolvedora!");
            clearFields();
        }
    }
    
        private void handleEditCustomer() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente para editar!");
            return;
        }

        int customerId = Integer.parseInt(customerTable.getValueAt(selectedRow, 0).toString());
        String name = nameField.getText();
        String phone = phoneField.getText();
        String email = emailField.getText();

        if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "UPDATE customers SET name = ?, phone = ?, email = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, phone);
            stmt.setString(3, email);
            stmt.setInt(4, customerId);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Cliente atualizado com sucesso!");
            clearFields();
            loadCustomers();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao editar cliente, entre em contato com a empresa desenvolvedora!");
            clearFields();
        }
    }
        
    private void populateFieldsFromSelectedRow() {
    // Obtem a linha selecionada
    int selectedRow = customerTable.getSelectedRow();
    if (selectedRow != -1) {
        // Converte para índice do modelo (caso esteja usando um TableRowSorter)
        int modelRow = customerTable.convertRowIndexToModel(selectedRow);

        // Recupera os dados do modelo de tabela
        String name = customerTable.getModel().getValueAt(modelRow, 1).toString();
        String email = customerTable.getModel().getValueAt(modelRow, 3).toString();
        String phone = customerTable.getModel().getValueAt(modelRow, 2).toString();

        // Preenche os campos de texto
        nameField.setText(name);
        emailField.setText(email);
        phoneField.setText(phone);
        }
    }
    
    private void clearFields() {
    // Limpa os campos
    nameField.setText(null);
    emailField.setText(null);
    phoneField.setText(null);
    
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        customerTable = new javax.swing.JTable();
        searchField = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        nameField = new javax.swing.JTextField();
        jButton8 = new javax.swing.JButton();
        phoneField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        emailField = new javax.swing.JTextField();
        jButton7 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Gestão de Clientes");
        setBackground(new java.awt.Color(255, 240, 245));
        setPreferredSize(new java.awt.Dimension(800, 600));
        setResizable(false);

        jPanel2.setBackground(new java.awt.Color(255, 240, 245));
        jPanel2.setPreferredSize(new java.awt.Dimension(800, 600));

        jLabel1.setFont(new java.awt.Font("Roboto Slab", 0, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(219, 112, 147));
        jLabel1.setText("Search:");

        customerTable.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 15, 5, 15));
        customerTable.setFont(new java.awt.Font("Roboto Slab", 0, 13)); // NOI18N
        customerTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Nome", "Telefone", "E-mail"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        customerTable.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        customerTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        customerTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        customerTable.setShowGrid(false);
        customerTable.getTableHeader().setReorderingAllowed(false);
        customerTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                customerTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(customerTable);

        searchField.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 15, 5, 15));
        searchField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchFieldActionPerformed(evt);
            }
        });
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchFieldKeyReleased(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 240, 245));

        nameField.setFont(new java.awt.Font("Roboto Slab", 0, 13)); // NOI18N
        nameField.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 15, 5, 15));

        jButton8.setFont(new java.awt.Font("Roboto Slab", 0, 14)); // NOI18N
        jButton8.setText("Limpar Campos");
        jButton8.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 15, 5, 15));
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        phoneField.setFont(new java.awt.Font("Roboto Slab", 0, 13)); // NOI18N
        phoneField.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 15, 5, 15));

        jLabel4.setFont(new java.awt.Font("Roboto Slab", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(219, 112, 147));
        jLabel4.setText("E-mail:");

        jButton6.setBackground(new java.awt.Color(220, 20, 60));
        jButton6.setFont(new java.awt.Font("Roboto Slab", 0, 16)); // NOI18N
        jButton6.setForeground(java.awt.Color.white);
        jButton6.setText("Excluir Cliente");
        jButton6.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 15, 5, 15));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        emailField.setFont(new java.awt.Font("Roboto Slab", 0, 13)); // NOI18N
        emailField.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 15, 5, 15));

        jButton7.setFont(new java.awt.Font("Roboto Slab", 0, 14)); // NOI18N
        jButton7.setText("Alterar Cliente");
        jButton7.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 15, 5, 15));
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Roboto Slab", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(219, 112, 147));
        jLabel2.setText("Nome:");

        jLabel3.setFont(new java.awt.Font("Roboto Slab", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(219, 112, 147));
        jLabel3.setText("Telefone:");

        jButton5.setFont(new java.awt.Font("Roboto Slab", 0, 14)); // NOI18N
        jButton5.setText("Adicionar Cliente");
        jButton5.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 15, 5, 15));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton6))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(phoneField, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(emailField, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(phoneField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(emailField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jButton6)
                    .addComponent(jButton8)
                    .addComponent(jButton7)
                    .addComponent(jButton5))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchField))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 12, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 425, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 803, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        handleAddCustomer();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        handleDeleteCustomer();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        handleEditCustomer();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void searchFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchFieldActionPerformed
        filterTable();
    }//GEN-LAST:event_searchFieldActionPerformed

    private void searchFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchFieldKeyReleased
        filterTable();
    }//GEN-LAST:event_searchFieldKeyReleased

    private void customerTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_customerTableMouseClicked
        populateFieldsFromSelectedRow();
    }//GEN-LAST:event_customerTableMouseClicked

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        clearFields();
    }//GEN-LAST:event_jButton8ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CustomerManagementScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CustomerManagementScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CustomerManagementScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CustomerManagementScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CustomerManagementScreen().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable customerTable;
    private javax.swing.JTextField emailField;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField nameField;
    private javax.swing.JTextField phoneField;
    private javax.swing.JTextField searchField;
    // End of variables declaration//GEN-END:variables
}

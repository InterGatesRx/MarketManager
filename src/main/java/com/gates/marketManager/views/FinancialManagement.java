/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.gates.marketManager.views;

import com.gates.marketManager.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author gates
 */
public class FinancialManagement extends javax.swing.JFrame {

    /**
     * Creates new form FinancialManagement
     */
    private TableRowSorter<DefaultTableModel> rowSorter;
    public FinancialManagement() {
        initComponents();
        loadTransactions();
        updateTotalLabelColor();
        
        DefaultTableModel tableModel = (DefaultTableModel) tableTransactions.getModel();

        rowSorter = new TableRowSorter<>(tableModel);
        tableTransactions.setRowSorter(rowSorter);
    }
    
    private void filterTable() {
            String text = searchField.getText();
            if (text.trim().isEmpty()) {
                rowSorter.setRowFilter(null); // Mostra tudo
            } else {
                rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
            }
        }
    
    private void loadTransactions() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM transactions";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            DefaultTableModel model = (DefaultTableModel) tableTransactions.getModel();
            model.setRowCount(0); // Limpar tabela

            double total = 0;

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("type"),
                    rs.getString("description"),
                    String.format("R$ %.2f", rs.getDouble("amount")),
                    rs.getString("category"),
                    rs.getDate("date")
                };
                model.addRow(row);

                // Calcular saldo total
                if (rs.getString("type").equals("Receita")) {
                    total += rs.getDouble("amount");
                } else {
                    total -= rs.getDouble("amount");
                }
            }

            lblTotal.setText(String.format("Saldo Total: R$ %.2f", total));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar transações: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateTotalLabelColor() {
    try {
        // Tente converter o texto para um valor numérico
        double total = Double.parseDouble(lblTotal.getText().replace("Saldo Total: R$ ", "").replace(",", "."));

        if (total < 0) {
            lblTotal.setForeground(new java.awt.Color(255, 0, 0)); // Vermelho
        } else if (total > 0) {
            lblTotal.setForeground(new java.awt.Color(0, 128, 0)); // Verde
        } else {
            lblTotal.setForeground(java.awt.Color.BLACK); // Preto, para zero
        }
    } catch (NumberFormatException e) {
        // Caso o texto não seja um número válido
        lblTotal.setForeground(java.awt.Color.BLACK);
    }
}

    
    private void handleAddTransaction() {
        String description = txtDescription.getText().trim();
        String amountText = txtAmount.getText().replace(",", ".");
        String type = (String) cbType.getSelectedItem();
        String category = (String) cbCategory.getSelectedItem();

        if (description.isEmpty() || amountText.isEmpty() || category.equals("Selecione uma Categoria")) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double amount = Double.parseDouble(amountText);
            try (Connection conn = DatabaseConnection.getConnection()) {
                String sql = "INSERT INTO transactions (type, description, amount, category, date) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, type);
                stmt.setString(2, description);
                stmt.setDouble(3, amount);
                stmt.setString(4, category);
                stmt.setDate(5, new java.sql.Date(System.currentTimeMillis()));
                stmt.executeUpdate();
                
                loadTransactions();
                txtDescription.setText("");
                txtAmount.setText("");
                cbCategory.setSelectedIndex(0);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, insira um valor numérico válido.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao adicionar transação: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
        updateTotalLabelColor();
        clearFields();
    }
    
    private void handleDeleteTransaction() {
        int selectedRow = tableTransactions.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione uma transação para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int transactionId = (int) tableTransactions.getValueAt(selectedRow, 0);
        int confirmation = JOptionPane.showConfirmDialog(this, "Tem certeza de que deseja excluir esta transação?", "Confirmação", JOptionPane.YES_NO_OPTION);

        if (confirmation != JOptionPane.YES_OPTION) {
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "DELETE FROM transactions WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, transactionId);
            stmt.executeUpdate();
            
            loadTransactions();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao excluir transação: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
        updateTotalLabelColor();
        clearFields();
    }
    
    private void populateFields() {
    // Verificar se uma linha foi selecionada
    int selectedRow = tableTransactions.getSelectedRow();
    if (selectedRow != -1) {
        // Obter os dados da linha selecionada
        String type = (String) tableTransactions.getValueAt(selectedRow, 1);
        String description = (String) tableTransactions.getValueAt(selectedRow, 2);
        double amount = Double.parseDouble(
            ((String) tableTransactions.getValueAt(selectedRow, 3)).replace("R$ ", "").replace(",", ".")
        );
        String category = (String) tableTransactions.getValueAt(selectedRow, 4);

        // Encontrar o índice do tipo selecionado no combobox
        for (int i = 0; i < cbType.getItemCount(); i++) {
            if (cbType.getItemAt(i).equals(type)) {
                cbType.setSelectedIndex(i);
                break;
            }
        }

        // Encontrar o índice da categoria selecionada no combobox
        for (int i = 0; i < cbCategory.getItemCount(); i++) {
            if (cbCategory.getItemAt(i).equals(category)) {
                cbCategory.setSelectedIndex(i);
                break;
            }
        }

        // Preencher os outros campos
        txtDescription.setText(description);
        txtAmount.setText(String.format("%.2f", amount));
    }
}
    
    private void clearFields() {
    txtDescription.setText("");
    cbType.setSelectedIndex(0); 
    cbCategory.setSelectedIndex(0);  
    
    txtAmount.setText("");  
  
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
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableTransactions = new javax.swing.JTable();
        searchField = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        btnAddTransaction = new javax.swing.JButton();
        cbType = new javax.swing.JComboBox<>();
        cbCategory = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        btnDeleteTransaction = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtAmount = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtDescription = new javax.swing.JTextField();
        lblTotal = new javax.swing.JLabel();
        clearFieldsButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Gestão Financeira");
        setPreferredSize(new java.awt.Dimension(800, 600));
        setResizable(false);

        jPanel2.setBackground(new java.awt.Color(255, 240, 245));

        jLabel5.setFont(new java.awt.Font("Roboto Slab", 0, 16)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(219, 112, 147));
        jLabel5.setText("Search:");

        tableTransactions.setFont(new java.awt.Font("Roboto Slab", 0, 13)); // NOI18N
        tableTransactions.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Tipo", "Descrição", "Valor", "Categoria", "Data"
            }
        ));
        tableTransactions.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tableTransactions.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tableTransactions.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tableTransactions.getTableHeader().setReorderingAllowed(false);
        tableTransactions.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableTransactionsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableTransactions);

        searchField.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 15, 5, 15));
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchFieldKeyReleased(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 240, 245));

        btnAddTransaction.setFont(new java.awt.Font("Roboto Slab", 0, 14)); // NOI18N
        btnAddTransaction.setText("Adicionar");
        btnAddTransaction.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 15, 5, 15));
        btnAddTransaction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddTransactionActionPerformed(evt);
            }
        });

        cbType.setFont(new java.awt.Font("Roboto Slab", 0, 13)); // NOI18N
        cbType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Receita", "Despesa" }));
        cbType.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 15, 5, 15));
        cbType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbTypeActionPerformed(evt);
            }
        });

        cbCategory.setFont(new java.awt.Font("Roboto Slab", 0, 13)); // NOI18N
        cbCategory.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione uma Categoria", "Vendas", "Salários", "Contas de Luz", "Outros" }));
        cbCategory.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 15, 5, 15));

        jLabel1.setFont(new java.awt.Font("Roboto Slab", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(219, 112, 147));
        jLabel1.setText("Descrição:");

        btnDeleteTransaction.setBackground(new java.awt.Color(220, 20, 60));
        btnDeleteTransaction.setFont(new java.awt.Font("Roboto Slab", 0, 16)); // NOI18N
        btnDeleteTransaction.setForeground(java.awt.Color.white);
        btnDeleteTransaction.setText("Excluir");
        btnDeleteTransaction.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 15, 5, 15));
        btnDeleteTransaction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteTransactionActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Roboto Slab", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(219, 112, 147));
        jLabel2.setText("Valor:");

        jLabel3.setFont(new java.awt.Font("Roboto Slab", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(219, 112, 147));
        jLabel3.setText("Tipo:");

        txtAmount.setFont(new java.awt.Font("Roboto Slab", 0, 13)); // NOI18N
        txtAmount.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 15, 5, 15));

        jLabel4.setFont(new java.awt.Font("Roboto Slab", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(219, 112, 147));
        jLabel4.setText("Categoria:");

        txtDescription.setFont(new java.awt.Font("Roboto Slab", 0, 13)); // NOI18N
        txtDescription.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 15, 5, 15));

        lblTotal.setFont(new java.awt.Font("Roboto Slab", 0, 14)); // NOI18N
        lblTotal.setText("Saldo Total: R$ 0.00");
        lblTotal.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        clearFieldsButton.setFont(new java.awt.Font("Roboto Slab", 0, 14)); // NOI18N
        clearFieldsButton.setText("Limpar Campos");
        clearFieldsButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 15, 5, 15));
        clearFieldsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearFieldsButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtDescription, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addGap(26, 26, 26)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(btnDeleteTransaction))
                                    .addComponent(cbType, 0, 128, Short.MAX_VALUE))
                                .addGap(15, 15, 15))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(cbCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(40, 40, 40)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(txtAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(btnAddTransaction)
                                .addGap(18, 18, 18)
                                .addComponent(clearFieldsButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblTotal)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAddTransaction)
                    .addComponent(lblTotal)
                    .addComponent(btnDeleteTransaction)
                    .addComponent(clearFieldsButton)))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(14, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(searchField)))
                        .addContainerGap())))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddTransactionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddTransactionActionPerformed
        handleAddTransaction();
    }//GEN-LAST:event_btnAddTransactionActionPerformed

    private void btnDeleteTransactionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteTransactionActionPerformed
        handleDeleteTransaction();
    }//GEN-LAST:event_btnDeleteTransactionActionPerformed

    private void tableTransactionsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableTransactionsMouseClicked
        populateFields();
    }//GEN-LAST:event_tableTransactionsMouseClicked

    private void searchFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchFieldKeyReleased
        filterTable();
    }//GEN-LAST:event_searchFieldKeyReleased

    private void cbTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbTypeActionPerformed

    private void clearFieldsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearFieldsButtonActionPerformed
        clearFields();
    }//GEN-LAST:event_clearFieldsButtonActionPerformed

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
            java.util.logging.Logger.getLogger(FinancialManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FinancialManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FinancialManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FinancialManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FinancialManagement().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddTransaction;
    private javax.swing.JButton btnDeleteTransaction;
    private javax.swing.JComboBox<String> cbCategory;
    private javax.swing.JComboBox<String> cbType;
    private javax.swing.JButton clearFieldsButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JTextField searchField;
    private javax.swing.JTable tableTransactions;
    private javax.swing.JTextField txtAmount;
    private javax.swing.JTextField txtDescription;
    // End of variables declaration//GEN-END:variables
}

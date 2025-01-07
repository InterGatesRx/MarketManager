/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.gates.marketManager.views;

import com.gates.marketManager.util.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author gates
 */
public class OrderManagementScreen extends javax.swing.JFrame {

    private TableRowSorter<DefaultTableModel> rowSorter;
    public OrderManagementScreen() {
        initComponents();
        loadCustomers();
        loadProducts();
        loadOrders();
        
        DefaultTableModel tableModel = (DefaultTableModel) orderTable.getModel();

        rowSorter = new TableRowSorter<>(tableModel);
        orderTable.setRowSorter(rowSorter);
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
        
        cbCustomer.addItem("Selecione um cliente");
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT id, name FROM customers";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                cbCustomer.addItem(rs.getInt("id") + " - " + rs.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadProducts() {
        
        cbProduct.addItem("Selecione um produto");
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT id, name, price FROM products";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                cbProduct.addItem(rs.getInt("id") + " - " + rs.getString("name") + " (R$" + rs.getDouble("price") + ")");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
  
    private void handleAddOrder() {
    // Verificar se o cliente e o produto foram selecionados
    if (cbCustomer.getSelectedIndex() == 0 || cbProduct.getSelectedIndex() == 0) {
        JOptionPane.showMessageDialog(this, "Por favor, selecione um cliente e um produto.", "Aviso", JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Obter o cliente selecionado
    String selectedCustomer = (String) cbCustomer.getSelectedItem();
    int customerId = Integer.parseInt(selectedCustomer.split(" - ")[0]); // Extrair o ID do cliente

    // Obter o produto selecionado
    String selectedProduct = (String) cbProduct.getSelectedItem();
    int productId = Integer.parseInt(selectedProduct.split(" - ")[0]); // Extrair o ID do produto

    // Obter o preço do produto diretamente do banco de dados
    double productPrice = getProductPriceById(productId);

    // Obter a quantidade
    int quantity = (int) spnQuantity.getValue();

    // Calcular o total
    double total = productPrice * quantity;

    int pedidoId = -1; // Variável para armazenar o ID do pedido

    try (Connection conn = DatabaseConnection.getConnection()) {
        // Inserir o pedido e recuperar o ID gerado
        String sql = "INSERT INTO orders (customer_id, product_id, quantity, total) VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        stmt.setInt(1, customerId);
        stmt.setInt(2, productId);
        stmt.setInt(3, quantity);
        stmt.setDouble(4, total);
        stmt.executeUpdate();

        // Obter o ID gerado do pedido
        ResultSet generatedKeys = stmt.getGeneratedKeys();
        if (generatedKeys.next()) {
            pedidoId = generatedKeys.getInt(1);
        }

        // Recarregar os pedidos na tabela
        loadOrders();

        // Limpar campos do formulário
        cbCustomer.setSelectedIndex(0);
        cbProduct.setSelectedIndex(0);
        spnQuantity.setValue(1);
        txtTotal.setText("");
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Erro ao adicionar pedido: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Inserir a transação relacionada ao pedido
    try (Connection conn = DatabaseConnection.getConnection()) {
        String sql = "INSERT INTO transactions (type, description, amount, date, category) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, "Receita");
        stmt.setString(2, "Venda - Pedido #" + pedidoId); // ID do pedido adicionado
        stmt.setDouble(3, total); // Total do pedido
        stmt.setDate(4, new java.sql.Date(System.currentTimeMillis())); // Data atual
        stmt.setString(5, "Vendas");
        stmt.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

private double getProductPriceById(int productId) {
    double price = 0.0;
    try (Connection conn = DatabaseConnection.getConnection()) {
        String sql = "SELECT price FROM products WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, productId);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            price = rs.getDouble("price");
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Erro ao recuperar preço do produto: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
    }
    return price;
}

    private void updateTotal() {
    try {
        String selectedProduct = (String) cbProduct.getSelectedItem();
        if (selectedProduct != null && !selectedProduct.equals("Selecione um produto")) {
            int productId = Integer.parseInt(selectedProduct.split(" - ")[0]);
            double price = getProductPriceById(productId);

            int quantity = (int) spnQuantity.getValue();
            double total = price * quantity;

            txtTotal.setText(String.format("R$ %.2f", total));
        }
    } catch (Exception e) {
        txtTotal.setText("Erro");
        e.printStackTrace();
    }
}


    
    private void handleEditOrder() {
    // Verificar se algum pedido foi selecionado na tabela
    int selectedRow = orderTable.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Selecione um pedido para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Obter o ID do pedido selecionado (primeira coluna da tabela)
    int orderId = (int) orderTable.getValueAt(selectedRow, 0);

    // Obter o cliente selecionado
    String selectedCustomer = (String) cbCustomer.getSelectedItem();
    int customerId = Integer.parseInt(selectedCustomer.split(" - ")[0]);

    // Obter o produto selecionado
    String selectedProduct = (String) cbProduct.getSelectedItem();
    int productId = Integer.parseInt(selectedProduct.split(" - ")[0]);

    // Obter o preço do produto diretamente do banco de dados
    double productPrice = getProductPriceById(productId);

    // Obter a nova quantidade
    int quantity = (int) spnQuantity.getValue();

    // Calcular o total
    double total = productPrice * quantity;

    // Atualizar o pedido no banco de dados
    try (Connection conn = DatabaseConnection.getConnection()) {
        String sql = "UPDATE orders SET customer_id = ?, product_id = ?, quantity = ?, total = ? WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, customerId);
        stmt.setInt(2, productId);
        stmt.setInt(3, quantity);
        stmt.setDouble(4, total);
        stmt.setInt(5, orderId); // Atualiza o pedido selecionado
        stmt.executeUpdate();

        // Atualizar a tabela com os pedidos alterados
        loadOrders();

        // Limpar os campos após editar
        cbCustomer.setSelectedIndex(0);
        cbProduct.setSelectedIndex(0);
        spnQuantity.setValue(1);
        txtTotal.setText("");
        
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Erro ao editar pedido: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
    }
}
    
    private void handleDeleteOrder() {
    int selectedRow = orderTable.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Selecione um pedido para excluir!");
        return;
    }

    // Obter ID do pedido selecionado
    int orderId = Integer.parseInt(orderTable.getValueAt(selectedRow, 0).toString());

    // Exibir caixa de confirmação
    int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir este pedido?", "Confirmar exclusão", JOptionPane.YES_NO_OPTION);
    if (confirm == JOptionPane.YES_OPTION) {
        // Excluir o pedido no banco de dados
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "DELETE FROM orders WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, orderId);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Pedido excluído com sucesso!");
            loadOrders(); // Recarregar a tabela de pedidos
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir pedido: " + e.getMessage());
        }
    }
}
    
    /*private void loadOrders() {
    try (Connection conn = DatabaseConnection.getConnection()) {
        String sql = "SELECT o.id, c.name AS customer, p.name AS product, o.quantity, o.total, o.order_date " +
                     "FROM orders o " +
                     "JOIN customers c ON o.customer_id = c.id " +
                     "JOIN products p ON o.product_id = p.id";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        // Limpar a tabela antes de adicionar os novos dados
        DefaultTableModel model = (DefaultTableModel) orderTable.getModel();
        model.setRowCount(0);

        while (rs.next()) {
            int orderId = rs.getInt("id");
            String customerName = rs.getString("customer");
            String productName = rs.getString("product");
            int quantity = rs.getInt("quantity");
            double total = rs.getDouble("total");
            String orderDate = rs.getString("order_date");

            model.addRow(new Object[]{orderId, customerName, productName, quantity, total, orderDate});
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Erro ao carregar pedidos: " + e.getMessage());
    }
}
    */
    
    private void loadOrders() {
    DefaultTableModel model = (DefaultTableModel) orderTable.getModel();
    model.setRowCount(0);  // Limpar os dados existentes

    try (Connection conn = DatabaseConnection.getConnection()) {
        String sql = "SELECT o.id, c.name AS customer_name, p.name AS product_name, o.quantity, o.total, o.order_date "
                   + "FROM orders o "
                   + "JOIN customers c ON o.customer_id = c.id "
                   + "JOIN products p ON o.product_id = p.id";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            // Adicionar os dados na tabela
            model.addRow(new Object[]{
                rs.getInt("id"),
                rs.getString("customer_name"),
                rs.getString("product_name"),
                rs.getInt("quantity"),
                rs.getDouble("total"),
                rs.getDate("order_date")
            });
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    private void populateFields() {
    // Verificar se uma linha foi selecionada
    int selectedRow = orderTable.getSelectedRow();
    if (selectedRow != -1) {
        // Obter os dados da linha selecionada
        int orderId = (int) orderTable.getValueAt(selectedRow, 0);
        String customerName = (String) orderTable.getValueAt(selectedRow, 1);
        String productName = (String) orderTable.getValueAt(selectedRow, 2);
        int quantity = (int) orderTable.getValueAt(selectedRow, 3);
        double total = (double) orderTable.getValueAt(selectedRow, 4);

        // Encontrar o índice do cliente selecionado
        for (int i = 0; i < cbCustomer.getItemCount(); i++) {
            if (cbCustomer.getItemAt(i).contains(customerName)) {
                cbCustomer.setSelectedIndex(i);
                break;
            }
        }

        // Encontrar o índice do produto selecionado
        for (int i = 0; i < cbProduct.getItemCount(); i++) {
            if (cbProduct.getItemAt(i).contains(productName)) {
                cbProduct.setSelectedIndex(i);
                break;
            }
        }

        // Preencher os outros campos
        spnQuantity.setValue(quantity);
        txtTotal.setText(String.format("R$ %.2f", total));
    }
}
    
    
    
    private void clearFields() {
    cbCustomer.setSelectedIndex(0);
    cbProduct.setSelectedIndex(0);
    spnQuantity.setValue(1);
    txtTotal.setText("");
}







    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        searchField = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cbCustomer = new javax.swing.JComboBox<>();
        btnDeleteOrder = new javax.swing.JButton();
        txtTotal = new javax.swing.JTextField();
        btnAddOrder = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        cbProduct = new javax.swing.JComboBox<>();
        btnEditOrder = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        spnQuantity = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        orderTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Gestão de Pedidos");
        setPreferredSize(new java.awt.Dimension(800, 600));
        setResizable(false);

        jPanel2.setBackground(new java.awt.Color(255, 240, 245));

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

        jLabel1.setFont(new java.awt.Font("Roboto Slab", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(219, 112, 147));
        jLabel1.setText("Cliente:");

        cbCustomer.setFont(new java.awt.Font("Roboto Slab", 0, 13)); // NOI18N
        cbCustomer.setMaximumRowCount(4);
        cbCustomer.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 15, 5, 15));

        btnDeleteOrder.setBackground(new java.awt.Color(220, 20, 60));
        btnDeleteOrder.setFont(new java.awt.Font("Roboto Slab", 0, 16)); // NOI18N
        btnDeleteOrder.setForeground(java.awt.Color.white);
        btnDeleteOrder.setText("Excluir Pedido");
        btnDeleteOrder.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 15, 5, 15));
        btnDeleteOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteOrderActionPerformed(evt);
            }
        });

        txtTotal.setEditable(false);
        txtTotal.setBackground(java.awt.Color.white);
        txtTotal.setFont(new java.awt.Font("Roboto Slab", 0, 13)); // NOI18N
        txtTotal.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 15, 5, 15));

        btnAddOrder.setFont(new java.awt.Font("Roboto Slab", 0, 14)); // NOI18N
        btnAddOrder.setText("Adicionar Pedido");
        btnAddOrder.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 15, 5, 15));
        btnAddOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddOrderActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Roboto Slab", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(219, 112, 147));
        jLabel2.setText("Produto:");

        cbProduct.setFont(new java.awt.Font("Roboto Slab", 0, 13)); // NOI18N
        cbProduct.setMaximumRowCount(4);
        cbProduct.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 15, 5, 15));
        cbProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbProductActionPerformed(evt);
            }
        });

        btnEditOrder.setFont(new java.awt.Font("Roboto Slab", 0, 14)); // NOI18N
        btnEditOrder.setText("Alterar Pedido");
        btnEditOrder.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 15, 5, 15));
        btnEditOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditOrderActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Roboto Slab", 0, 14)); // NOI18N
        jButton1.setText("Limpar Campos");
        jButton1.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 15, 5, 15));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        spnQuantity.setFont(new java.awt.Font("Roboto Slab", 0, 13)); // NOI18N
        spnQuantity.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));
        spnQuantity.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 15, 5, 15));
        spnQuantity.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spnQuantityStateChanged(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Roboto Slab", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(219, 112, 147));
        jLabel3.setText("Quantidade");

        jLabel4.setFont(new java.awt.Font("Roboto Slab", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(219, 112, 147));
        jLabel4.setText("Total:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnAddOrder)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnEditOrder)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 165, Short.MAX_VALUE)
                        .addComponent(btnDeleteOrder))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(cbCustomer, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(cbProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(spnQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel2)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbProduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(spnQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(38, 38, 38)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAddOrder)
                    .addComponent(btnEditOrder)
                    .addComponent(btnDeleteOrder)
                    .addComponent(jButton1))
                .addContainerGap())
        );

        jLabel5.setFont(new java.awt.Font("Roboto Slab", 0, 16)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(219, 112, 147));
        jLabel5.setText("Search:");

        orderTable.setFont(new java.awt.Font("Roboto Slab", 0, 13)); // NOI18N
        orderTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Cliente", "Produto", "Quantidade", "Total", "Data"
            }
        ));
        orderTable.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        orderTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        orderTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        orderTable.getTableHeader().setReorderingAllowed(false);
        orderTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                orderTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(orderTable);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 372, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddOrderActionPerformed
        handleAddOrder();
    }//GEN-LAST:event_btnAddOrderActionPerformed

    private void btnEditOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditOrderActionPerformed
        handleEditOrder();
    }//GEN-LAST:event_btnEditOrderActionPerformed

    private void btnDeleteOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteOrderActionPerformed
        handleDeleteOrder();
    }//GEN-LAST:event_btnDeleteOrderActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        clearFields();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void spnQuantityStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spnQuantityStateChanged
        updateTotal();
    }//GEN-LAST:event_spnQuantityStateChanged

    private void orderTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_orderTableMouseClicked
        populateFields();
    }//GEN-LAST:event_orderTableMouseClicked

    private void cbProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbProductActionPerformed
        updateTotal();
    }//GEN-LAST:event_cbProductActionPerformed

    private void searchFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchFieldActionPerformed
        filterTable();
    }//GEN-LAST:event_searchFieldActionPerformed

    private void searchFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchFieldKeyReleased
        filterTable();
    }//GEN-LAST:event_searchFieldKeyReleased

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
            java.util.logging.Logger.getLogger(OrderManagementScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(OrderManagementScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(OrderManagementScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(OrderManagementScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new OrderManagementScreen().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddOrder;
    private javax.swing.JButton btnDeleteOrder;
    private javax.swing.JButton btnEditOrder;
    private javax.swing.JComboBox<String> cbCustomer;
    private javax.swing.JComboBox<String> cbProduct;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable orderTable;
    private javax.swing.JTextField searchField;
    private javax.swing.JSpinner spnQuantity;
    private javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables
}

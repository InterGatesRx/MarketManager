/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.gates.marketManager.views;

import java.awt.Frame;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author gates
 */
public class MainMenu extends javax.swing.JFrame {
    

    public MainMenu(String username) {
        initComponents();
        lblWelcome.setText("Bem-vindo, " + username); 
        setLocationRelativeTo(null); 
    }
    
    private void openCustomerManagement() {
    // Verifica se a janela já está aberta
    for (Frame frame : JFrame.getFrames()) {
        if (frame instanceof CustomerManagementScreen && frame.isVisible()) {
            frame.setExtendedState(JFrame.NORMAL); // Garante que não esteja minimizada
            frame.toFront(); // Traz a janela para frente
            frame.requestFocus(); // Garante o foco na janela
            return;
        }
    }

    // Instancia e exibe a janela de gestão de clientes
    CustomerManagementScreen customerManagement = new CustomerManagementScreen();
    customerManagement.setVisible(true);
    }
    
    private void openProductManagement() {
    // Verifica se a janela já está aberta
    for (Frame frame : JFrame.getFrames()) {
        if (frame instanceof ProductManagementScreen && frame.isVisible()) {
            frame.setExtendedState(JFrame.NORMAL); // Garante que não esteja minimizada
            frame.toFront(); // Traz a janela para frente
            frame.requestFocus(); // Garante o foco na janela
            return;
        }
    }

    // Instancia e exibe a janela de gestão de clientes
    ProductManagementScreen productManagement = new ProductManagementScreen();
    productManagement.setVisible(true);
}
    
    private void openOrderManagement() {
    // Verifica se a janela já está aberta
    for (Frame frame : JFrame.getFrames()) {
        if (frame instanceof OrderManagementScreen && frame.isVisible()) {
            frame.setExtendedState(JFrame.NORMAL); // Garante que não esteja minimizada
            frame.toFront(); // Traz a janela para frente
            frame.requestFocus(); // Garante o foco na janela
            return;
        }
    }

    // Instancia e exibe a janela de gestão de clientes
    OrderManagementScreen orderManagement = new OrderManagementScreen();
    orderManagement.setVisible(true);
}
    
    private void openFinancialManagement() {
    // Verifica se a janela já está aberta
    for (Frame frame : JFrame.getFrames()) {
        if (frame instanceof FinancialManagement && frame.isVisible()) {
            frame.setExtendedState(JFrame.NORMAL); // Garante que não esteja minimizada
            frame.toFront(); // Traz a janela para frente
            frame.requestFocus(); // Garante o foco na janela
            return;
        }
    }

    // Instancia e exibe a janela de gestão de clientes
    FinancialManagement financialManagement = new FinancialManagement();
    financialManagement.setVisible(true);
}
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        Copyright = new javax.swing.JLabel();
        topPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblWelcome = new javax.swing.JLabel();
        btnLogout = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        productsButton = new javax.swing.JButton();
        usersButton = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        reportsButton = new javax.swing.JButton();
        settingsButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Home - MiniBags");
        setResizable(false);

        jPanel2.setBackground(new java.awt.Color(245, 245, 245));
        jPanel2.setPreferredSize(new java.awt.Dimension(500, 400));

        Copyright.setFont(new java.awt.Font("Roboto Slab", 0, 12)); // NOI18N
        Copyright.setForeground(new java.awt.Color(169, 169, 169));
        Copyright.setText("Versão 1.0.0 © marketManager");
        Copyright.setFocusable(false);

        topPanel.setBackground(new java.awt.Color(255, 182, 193));

        jLabel1.setFont(new java.awt.Font("Roboto Slab", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(199, 21, 133));
        jLabel1.setText("brand here");
        jLabel1.setFocusable(false);

        lblWelcome.setFont(new java.awt.Font("Roboto Slab", 1, 16)); // NOI18N
        lblWelcome.setForeground(java.awt.Color.white);
        lblWelcome.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblWelcome.setText("Bem-vindo, [Usuário]");
        lblWelcome.setFocusable(false);
        lblWelcome.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        btnLogout.setBackground(new java.awt.Color(220, 20, 60));
        btnLogout.setFont(new java.awt.Font("Roboto Slab", 0, 16)); // NOI18N
        btnLogout.setForeground(java.awt.Color.white);
        btnLogout.setText("Logout");
        btnLogout.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 15, 5, 15));
        btnLogout.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLogout.setFocusPainted(false);
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout topPanelLayout = new javax.swing.GroupLayout(topPanel);
        topPanel.setLayout(topPanelLayout);
        topPanelLayout.setHorizontalGroup(
            topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topPanelLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblWelcome, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(btnLogout)
                .addContainerGap(19, Short.MAX_VALUE))
        );
        topPanelLayout.setVerticalGroup(
            topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(btnLogout)
                    .addComponent(lblWelcome)
                    .addComponent(jLabel1))
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(245, 245, 245));
        jPanel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
        jPanel3.setLayout(new java.awt.GridLayout(2, 3, 10, 10));

        productsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/box-alt.png"))); // NOI18N
        productsButton.setText("Produtos");
        productsButton.setToolTipText("Gestão de Produtos");
        productsButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 15, 5, 15));
        productsButton.setBorderPainted(false);
        productsButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        productsButton.setFocusPainted(false);
        productsButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        productsButton.setOpaque(true);
        productsButton.setPreferredSize(new java.awt.Dimension(128, 128));
        productsButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        productsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                productsButtonActionPerformed(evt);
            }
        });
        jPanel3.add(productsButton);

        usersButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/users-alt.png"))); // NOI18N
        usersButton.setText("Clientes");
        usersButton.setToolTipText("Gestão de Clientes");
        usersButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 15, 5, 15));
        usersButton.setBorderPainted(false);
        usersButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        usersButton.setFocusPainted(false);
        usersButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        usersButton.setOpaque(true);
        usersButton.setPreferredSize(new java.awt.Dimension(128, 128));
        usersButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        usersButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usersButtonActionPerformed(evt);
            }
        });
        jPanel3.add(usersButton);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/handshake.png"))); // NOI18N
        jButton1.setText("Pedidos");
        jButton1.setToolTipText("Gestão de Pedidos");
        jButton1.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 15, 5, 15));
        jButton1.setBorderPainted(false);
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.setFocusPainted(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setOpaque(true);
        jButton1.setPreferredSize(new java.awt.Dimension(128, 128));
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton1);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/usd-circle.png"))); // NOI18N
        jButton2.setText("Financeiro");
        jButton2.setToolTipText("Gestão de Financeiro");
        jButton2.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 15, 5, 15));
        jButton2.setBorderPainted(false);
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.setFocusPainted(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setOpaque(true);
        jButton2.setPreferredSize(new java.awt.Dimension(128, 128));
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton2);

        reportsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/document.png"))); // NOI18N
        reportsButton.setText("Relatórios");
        reportsButton.setToolTipText("Criar Relatório");
        reportsButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 15, 5, 15));
        reportsButton.setBorderPainted(false);
        reportsButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        reportsButton.setFocusPainted(false);
        reportsButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        reportsButton.setOpaque(true);
        reportsButton.setPreferredSize(new java.awt.Dimension(128, 128));
        reportsButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel3.add(reportsButton);

        settingsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/settings.png"))); // NOI18N
        settingsButton.setText("Configurações");
        settingsButton.setToolTipText("Configurações");
        settingsButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 15, 5, 15));
        settingsButton.setBorderPainted(false);
        settingsButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        settingsButton.setFocusPainted(false);
        settingsButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        settingsButton.setOpaque(true);
        settingsButton.setPreferredSize(new java.awt.Dimension(128, 128));
        settingsButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel3.add(settingsButton);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(topPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(Copyright))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(topPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Copyright)
                .addGap(32, 32, 32))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        this.dispose();
        new LoginScreen().setVisible(true); 
    }//GEN-LAST:event_btnLogoutActionPerformed

    private void usersButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usersButtonActionPerformed
        openCustomerManagement();
    }//GEN-LAST:event_usersButtonActionPerformed

    private void productsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_productsButtonActionPerformed
        openProductManagement();
    }//GEN-LAST:event_productsButtonActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        openOrderManagement();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        openFinancialManagement();
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainMenu("Usuário Teste").setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Copyright;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel lblWelcome;
    private javax.swing.JButton productsButton;
    private javax.swing.JButton reportsButton;
    private javax.swing.JButton settingsButton;
    private javax.swing.JPanel topPanel;
    private javax.swing.JButton usersButton;
    // End of variables declaration//GEN-END:variables
}

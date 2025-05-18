/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

/**
 *
 * @author wijde
 */


import dao.EtudiantDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.List;
import model.Etudiant;

public class GestionEtudiantsPanel extends JPanel {
    private final EtudiantDAO etudiantDAO;
    private final JTable tableEtudiants;
    private final DefaultTableModel tableModel;

    public GestionEtudiantsPanel() throws SQLException {
        this.etudiantDAO = new EtudiantDAO();
        this.tableModel = new DefaultTableModel(new Object[]{"ID", "Nom", "Prénom", "Matricule"}, 0);
        this.tableEtudiants = new JTable(tableModel);
        
        initUI();
        chargerEtudiants();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        
        // Barre d'outils
        JToolBar toolBar = new JToolBar();
        JButton btnAjouter = new JButton("Ajouter");
        JButton btnModifier = new JButton("Modifier");
        JButton btnSupprimer = new JButton("Supprimer");
        
        btnAjouter.addActionListener(this::ajouterEtudiant);
        btnModifier.addActionListener(this::modifierEtudiant);
        btnSupprimer.addActionListener(this::supprimerEtudiant);
        
        toolBar.add(btnAjouter);
        toolBar.add(btnModifier);
        toolBar.add(btnSupprimer);
        
        // Tableau
        tableEtudiants.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(tableEtudiants);
        
        add(toolBar, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void chargerEtudiants() {
        try {
            tableModel.setRowCount(0); // Vider le tableau
            List<Etudiant> etudiants = etudiantDAO.getAllEtudiants();
            
            for (Etudiant e : etudiants) {
                tableModel.addRow(new Object[]{
                    e.getId(),
                    e.getNom(),
                    e.getPrenom(),
                    e.getMatricule()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Erreur lors du chargement des étudiants: " + e.getMessage(),
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ajouterEtudiant(ActionEvent e) {
        JDialog dialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), "Nouvel Étudiant", true);
        dialog.setSize(400, 250);
        dialog.setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        JTextField txtNom = new JTextField();
        JTextField txtPrenom = new JTextField();
        JTextField txtMatricule = new JTextField();
        
        formPanel.add(new JLabel("Nom:"));
        formPanel.add(txtNom);
        formPanel.add(new JLabel("Prénom:"));
        formPanel.add(txtPrenom);
        formPanel.add(new JLabel("Matricule:"));
        formPanel.add(txtMatricule);
        
        JButton btnValider = new JButton("Valider");
        btnValider.addActionListener(ev -> {
            try {
                Etudiant nouvelEtudiant = new Etudiant(
                    txtNom.getText(),
                    txtPrenom.getText(),
                    txtMatricule.getText()
                );
                etudiantDAO.ajouterEtudiant(nouvelEtudiant);
                chargerEtudiants();
                dialog.dispose();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Erreur lors de l'ajout: " + ex.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(btnValider, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void modifierEtudiant(ActionEvent e) {
        int selectedRow = tableEtudiants.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Veuillez sélectionner un étudiant",
                "Avertissement", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        // Implémentation similaire à ajouterEtudiant mais avec pré-remplissage
    }

    private void supprimerEtudiant(ActionEvent e) {
        int selectedRow = tableEtudiants.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Veuillez sélectionner un étudiant",
                "Avertissement", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String nom = (String) tableModel.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Supprimer l'étudiant " + nom + " ?",
            "Confirmation", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                etudiantDAO.supprimerEtudiant(id);
                chargerEtudiants();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                    "Erreur lors de la suppression: " + ex.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

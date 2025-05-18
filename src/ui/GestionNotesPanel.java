/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import dao.EtudiantDAO;
import dao.MatiereDAO;
import dao.NoteDAO;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.List;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import model.Etudiant;
import model.Matiere;
import model.Note;

/**
 *
 * @author wijde
 */

public class GestionNotesPanel extends JPanel {
    private final NoteDAO noteDAO;
    private final EtudiantDAO etudiantDAO;
    private final MatiereDAO matiereDAO;
    private final DefaultTableModel tableModel;
    private final JComboBox<Etudiant> comboEtudiants;
    private final JComboBox<Matiere> comboMatieres;

    public GestionNotesPanel() throws SQLException {
        this.noteDAO = new NoteDAO();
        this.etudiantDAO = new EtudiantDAO();
        this.matiereDAO = new MatiereDAO();
        this.tableModel = new DefaultTableModel(new Object[]{"ID", "Étudiant", "Matière", "Note", "Date"}, 0);
        
        this.comboEtudiants = new JComboBox<>();
        this.comboMatieres = new JComboBox<>();
        
        initUI();
        chargerDonnees();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        
        // Panel de contrôle
        JPanel controlPanel = new JPanel(new GridLayout(1, 4, 5, 5));
        
        comboEtudiants.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Etudiant e) {
                    setText(e.getPrenom() + " " + e.getNom() + " (" + e.getMatricule() + ")");
                }
                return this;
            }
        });
        
        comboMatieres.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Matiere m) {
                    setText(m.getCode() + " - " + m.getNom());
                }
                return this;
            }
        });
        
        JButton btnAjouter = new JButton("Ajouter Note");
        btnAjouter.addActionListener(this::ajouterNote);
        
        controlPanel.add(new JLabel("Étudiant:"));
        controlPanel.add(comboEtudiants);
        controlPanel.add(new JLabel("Matière:"));
        controlPanel.add(comboMatieres);
        controlPanel.add(btnAjouter);
        
        // Tableau
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        
        add(controlPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void chargerDonnees() {
        try {
            // Charger les combobox
            comboEtudiants.removeAllItems();
            for (Etudiant e : etudiantDAO.getAllEtudiants()) {
                comboEtudiants.addItem(e);
            }
            
            comboMatieres.removeAllItems();
            for (Matiere m : matiereDAO.getAllMatieres()) {
                comboMatieres.addItem(m);
            }
            
            // Charger les notes
            chargerNotes();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Erreur lors du chargement: " + e.getMessage(),
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void chargerNotes() {
        try {
            tableModel.setRowCount(0);
            Etudiant etudiant = (Etudiant) comboEtudiants.getSelectedItem();
            if (etudiant != null) {
                List<Note> notes = noteDAO.getNotesParEtudiant(etudiant.getId());
                for (Note n : notes) {
                    tableModel.addRow(new Object[]{
                        n.getId(),
                        n.getEtudiant().getPrenom() + " " + n.getEtudiant().getNom(),
                        n.getMatiere().getNom(),
                        n.getValeur(),
                        n.getDateEvaluation()
                    });
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Erreur lors du chargement des notes: " + e.getMessage(),
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ajouterNote(ActionEvent e) {
        Etudiant etudiant = (Etudiant) comboEtudiants.getSelectedItem();
        Matiere matiere = (Matiere) comboMatieres.getSelectedItem();
        
        if (etudiant == null || matiere == null) {
            JOptionPane.showMessageDialog(this,
                "Veuillez sélectionner un étudiant et une matière",
                "Avertissement", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JDialog dialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), "Nouvelle Note", true);
        dialog.setSize(300, 200);
        dialog.setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        JTextField txtNote = new JTextField();
        JTextField txtDate = new JTextField(new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()));
        
        formPanel.add(new JLabel("Note (0-20):"));
        formPanel.add(txtNote);
        formPanel.add(new JLabel("Date:"));
        formPanel.add(txtDate);
        
        JButton btnValider = new JButton("Valider");
        btnValider.addActionListener(ev -> {
            try {
                double noteValue = Double.parseDouble(txtNote.getText());
                if (noteValue < 0 || noteValue > 20) {
                    throw new NumberFormatException("La note doit être entre 0 et 20");
                }
                
                java.util.Date date = java.sql.Date.valueOf(txtDate.getText());
                
                Note nouvelleNote = new Note(etudiant, matiere, noteValue, date);
                noteDAO.ajouterNote(nouvelleNote);
                chargerNotes();
                dialog.dispose();
            } catch (NumberFormatException | SQLException ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Erreur: " + ex.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(btnValider, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author wijde
 */


import model.Note;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NoteDAO {
    private final Connection connexion;

    public NoteDAO() throws SQLException {
        this.connexion = DatabaseConnection.getConnection();
    }

    public void ajouterNote(Note note) throws SQLException {
        String sql = "INSERT INTO notes (etudiant_id, matiere_id, valeur, date_evaluation, appreciation) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, note.getEtudiant().getId());
            stmt.setInt(2, note.getMatiere().getId());
            stmt.setDouble(3, note.getValeur());
            stmt.setDate(4, new java.sql.Date(note.getDateEvaluation().getTime()));
            stmt.setString(5, note.getAppreciation());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    note.setId(rs.getInt(1));
                }
            }
        }
    }

    public List<Note> getNotesParEtudiant(int etudiantId) throws SQLException {
        List<Note> notes = new ArrayList<>();
        String sql = "SELECT n.*, m.code, m.nom as matiere_nom, m.coefficient, " +
                     "e.nom as etudiant_nom, e.prenom as etudiant_prenom, e.matricule " +
                     "FROM notes n " +
                     "JOIN matieres m ON n.matiere_id = m.id " +
                     "JOIN etudiants e ON n.etudiant_id = e.id " +
                     "WHERE n.etudiant_id = ?";

        try (PreparedStatement stmt = connexion.prepareStatement(sql)) {
            stmt.setInt(1, etudiantId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    notes.add(creerNoteDepuisResultSet(rs));
                }
            }
        }
        return notes;
    }

    public double calculerMoyenneEtudiant(int etudiantId) throws SQLException {
        String sql = "SELECT AVG(n.valeur) as moyenne FROM notes n WHERE n.etudiant_id = ?";
        
        try (PreparedStatement stmt = connexion.prepareStatement(sql)) {
            stmt.setInt(1, etudiantId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("moyenne");
                }
            }
        }
        return 0;
    }

    private Note creerNoteDepuisResultSet(ResultSet rs) throws SQLException {
        Note note = new Note();
        note.setId(rs.getInt("id"));
        note.setValeur(rs.getDouble("valeur"));
        note.setDateEvaluation(rs.getDate("date_evaluation"));
        note.setAppreciation(rs.getString("appreciation"));
        
        // Création des objets liés
        model.Etudiant etudiant = new model.Etudiant();
        etudiant.setId(rs.getInt("etudiant_id"));
        etudiant.setNom(rs.getString("etudiant_nom"));
        etudiant.setPrenom(rs.getString("etudiant_prenom"));
        etudiant.setMatricule(rs.getString("matricule"));
        
        model.Matiere matiere = new model.Matiere();
        matiere.setId(rs.getInt("matiere_id"));
        matiere.setCode(rs.getString("code"));
        matiere.setNom(rs.getString("matiere_nom"));
        matiere.setCoefficient(rs.getInt("coefficient"));
        
        note.setEtudiant(etudiant);
        note.setMatiere(matiere);
        
        return note;
    }
}

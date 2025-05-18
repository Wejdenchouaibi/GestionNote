/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author wijde
 */


import model.Etudiant;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EtudiantDAO {
    private final Connection connexion;

    public EtudiantDAO() throws SQLException {
        this.connexion = DatabaseConnection.getConnection();
    }

    public void ajouterEtudiant(Etudiant etudiant) throws SQLException {
        String sql = "INSERT INTO etudiants (nom, prenom, matricule) VALUES (?, ?, ?)";
        
        try (PreparedStatement stmt = connexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, etudiant.getNom());
            stmt.setString(2, etudiant.getPrenom());
            stmt.setString(3, etudiant.getMatricule());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    etudiant.setId(rs.getInt(1));
                }
            }
        }
    }

    public List<Etudiant> getAllEtudiants() throws SQLException {
        List<Etudiant> etudiants = new ArrayList<>();
        String sql = "SELECT * FROM etudiants ORDER BY nom, prenom";

        try (Statement stmt = connexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Etudiant e = new Etudiant();
                e.setId(rs.getInt("id"));
                e.setNom(rs.getString("nom"));
                e.setPrenom(rs.getString("prenom"));
                e.setMatricule(rs.getString("matricule"));
                etudiants.add(e);
            }
        }
        return etudiants;
    }

    public void modifierEtudiant(Etudiant etudiant) throws SQLException {
        String sql = "UPDATE etudiants SET nom = ?, prenom = ?, matricule = ? WHERE id = ?";
        
        try (PreparedStatement stmt = connexion.prepareStatement(sql)) {
            stmt.setString(1, etudiant.getNom());
            stmt.setString(2, etudiant.getPrenom());
            stmt.setString(3, etudiant.getMatricule());
            stmt.setInt(4, etudiant.getId());
            stmt.executeUpdate();
        }
    }

    public void supprimerEtudiant(int id) throws SQLException {
        String sql = "DELETE FROM etudiants WHERE id = ?";
        
        try (PreparedStatement stmt = connexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public Etudiant trouverParMatricule(String matricule) throws SQLException {
        String sql = "SELECT * FROM etudiants WHERE matricule = ?";
        
        try (PreparedStatement stmt = connexion.prepareStatement(sql)) {
            stmt.setString(1, matricule);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Etudiant e = new Etudiant();
                    e.setId(rs.getInt("id"));
                    e.setNom(rs.getString("nom"));
                    e.setPrenom(rs.getString("prenom"));
                    e.setMatricule(rs.getString("matricule"));
                    return e;
                }
            }
        }
        return null;
    }
}

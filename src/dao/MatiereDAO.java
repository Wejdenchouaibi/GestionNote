/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author wijde
 */

import model.Matiere;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MatiereDAO {
    private final Connection connexion;

    public MatiereDAO() throws SQLException {
        this.connexion = DatabaseConnection.getConnection();
    }

    public void ajouterMatiere(Matiere matiere) throws SQLException {
        String sql = "INSERT INTO matieres (code, nom, coefficient) VALUES (?, ?, ?)";
        
        try (PreparedStatement stmt = connexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, matiere.getCode());
            stmt.setString(2, matiere.getNom());
            stmt.setInt(3, matiere.getCoefficient());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    matiere.setId(rs.getInt(1));
                }
            }
        }
    }

    public List<Matiere> getAllMatieres() throws SQLException {
        List<Matiere> matieres = new ArrayList<>();
        String sql = "SELECT * FROM matieres ORDER BY nom";

        try (Statement stmt = connexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Matiere m = new Matiere();
                m.setId(rs.getInt("id"));
                m.setCode(rs.getString("code"));
                m.setNom(rs.getString("nom"));
                m.setCoefficient(rs.getInt("coefficient"));
                matieres.add(m);
            }
        }
        return matieres;
    }

    public Matiere trouverParCode(String code) throws SQLException {
        String sql = "SELECT * FROM matieres WHERE code = ?";
        
        try (PreparedStatement stmt = connexion.prepareStatement(sql)) {
            stmt.setString(1, code);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Matiere m = new Matiere();
                    m.setId(rs.getInt("id"));
                    m.setCode(rs.getString("code"));
                    m.setNom(rs.getString("nom"));
                    m.setCoefficient(rs.getInt("coefficient"));
                    return m;
                }
            }
        }
        return null;
    }
}

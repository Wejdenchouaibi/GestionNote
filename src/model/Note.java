/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author wijde
 */


import java.util.Date;

public class Note {
    private int id;
    private Etudiant etudiant;
    private Matiere matiere;
    private double valeur;
    private Date dateEvaluation;
    private String appreciation;
    
    // Constructeurs
    public Note() {}
    
    public Note(Etudiant etudiant, Matiere matiere, double valeur, Date dateEvaluation) {
        this.etudiant = etudiant;
        this.matiere = matiere;
        this.valeur = valeur;
        this.dateEvaluation = dateEvaluation;
    }
    
    // Getters et Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public Etudiant getEtudiant() {
        return etudiant;
    }
    
    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }
    
    public Matiere getMatiere() {
        return matiere;
    }
    
    public void setMatiere(Matiere matiere) {
        this.matiere = matiere;
    }
    
    public double getValeur() {
        return valeur;
    }
    
    public void setValeur(double valeur) {
        this.valeur = valeur;
    }
    
    public Date getDateEvaluation() {
        return dateEvaluation;
    }
    
    public void setDateEvaluation(Date dateEvaluation) {
        this.dateEvaluation = dateEvaluation;
    }
    
    public String getAppreciation() {
        return appreciation;
    }
    
    public void setAppreciation(String appreciation) {
        this.appreciation = appreciation;
    }
    
    // Méthode utilitaire
    public double getValeurPonderee() {
        return valeur * matiere.getCoefficient();
    }
    
    @Override
    public String toString() {
        return etudiant.getPrenom() + " " + etudiant.getNom() + 
               " - " + matiere.getNom() + " : " + valeur + "/20";
    }
}
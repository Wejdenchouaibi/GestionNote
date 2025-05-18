/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author wijde
 */
public class Matiere {
    private int id;
    private String code;
    private String nom;
    private int coefficient;
    
    // Constructeurs
    public Matiere() {}
    
    public Matiere(String code, String nom, int coefficient) {
        this.code = code;
        this.nom = nom;
        this.coefficient = coefficient;
    }
    
    // Getters et Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public int getCoefficient() {
        return coefficient;
    }
    
    public void setCoefficient(int coefficient) {
        this.coefficient = coefficient;
    }
    
    @Override
    public String toString() {
        return code + " - " + nom + " (Coef. " + coefficient + ")";
    }
}
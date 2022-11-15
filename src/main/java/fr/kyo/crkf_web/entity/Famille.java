package fr.kyo.crkf_web.entity;

import fr.kyo.crkf_web.dao.DAOFactory;

public class Famille {

    private final int familleId;
    private String familleLibelle;
    private int classificationId;

    public Famille(int familleId, String familleLibelle, int classificationId) {
        this.familleId = familleId;
        this.familleLibelle = familleLibelle;
        this.classificationId = classificationId;
    }

    public int getFamilleId() {
        return familleId;
    }

    public String getFamilleLibelle() {
        return familleLibelle;
    }

    public void setFamilleLibelle(String famille) {
        this.familleLibelle = famille;
    }

    public Classification getClassification() {
        return DAOFactory.getClassificationDAO().getByID(classificationId);
    }

    public void setClassification(Classification classification) {
        this.classificationId = classification.getClassificationId();
    }

    public int getClassificationId() {
        return classificationId;
    }

    @Override
    public String toString() {
        return familleLibelle;
    }
}

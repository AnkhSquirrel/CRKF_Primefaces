package fr.kyo.crkf_web.entity;

import fr.kyo.crkf_web.dao.DAOFactory;

public class Classification {
    private final int classificationId;
    private String classificationLibelle;

    public Classification(int classificationId, String classificationLibelle) {
        this.classificationId = classificationId;
        this.classificationLibelle = classificationLibelle;
    }

    public int getClassificationId() {
        return classificationId;
    }

    public String getClassificationLibelle() {
        return classificationLibelle;
    }

    public void setClassificationLibelle(String classificationLibelle) {
        this.classificationLibelle = classificationLibelle;
    }

    @Override
    public String toString() {
        return classificationLibelle;
    }
}

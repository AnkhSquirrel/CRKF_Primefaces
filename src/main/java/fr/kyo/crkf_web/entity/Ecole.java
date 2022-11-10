package fr.kyo.crkf_web.entity;

import fr.kyo.crkf_web.dao.DAOFactory;

public class Ecole {

    private final int ecoleId;
    private final String ecoleNom;
    private final int ecoleAdresse;

    public Ecole(int ecoleId, String ecoleNom ,int ecoleAdresse) {
        this.ecoleId = ecoleId;
        this.ecoleNom = ecoleNom;
        this.ecoleAdresse = ecoleAdresse;
    }

    public int getEcoleId() {
        return ecoleId;
    }

    public Adresse getEcoleAdresse() {
        return DAOFactory.getAdresseDAO().getByID(ecoleAdresse);
    }

    public int getIdAdresse(){
        return ecoleAdresse;
    }

    public String getEcoleNom() {
        return ecoleNom;
    }

    @Override
    public String toString() {
        return ecoleNom;
    }
}

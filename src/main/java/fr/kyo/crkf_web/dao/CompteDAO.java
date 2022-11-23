package fr.kyo.crkf_web.dao;

import fr.kyo.crkf_web.entity.Compte;

import java.sql.*;
import java.util.List;

public class CompteDAO extends DAO<Compte> {

    protected CompteDAO(Connection connection) {
        super(connection);
    }

    @Override
    public Compte getByID(int id) {
        return null;
    }

    @Override
    public List<Compte> getAll(int page) {
        return null;
    }

    @Override
    public int insert(Compte objet) {
        String requete = "INSERT INTO Compte (email,mot_de_passe) VALUES (?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(requete, Statement.RETURN_GENERATED_KEYS)){
            connection.setAutoCommit(false);
            preparedStatement.setString( 1 , objet.getEmail());
            preparedStatement.setString(2, objet.getMot_de_passe());
            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            connection.commit();
            rs.next();
            return rs.getInt(1);
        } catch(SQLException e) {
            try {
                connection.rollback();
                return 0;
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        }
        return 0;
    }

    @Override
    public boolean update(Compte object) {
        return false;
    }

    @Override
    public boolean delete(Compte object) {
        return false;
    }
}

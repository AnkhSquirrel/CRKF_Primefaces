package fr.kyo.crkf_web.dao;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

import java.sql.Connection;

public class CRKFConnect {

    private static Connection connexion;

    private CRKFConnect(){}

    public static Connection getInstance() {
        if (connexion == null) {
            try {
                SQLServerDataSource ds = new SQLServerDataSource();
                ds.setServerName("localhost");
                ds.setPortNumber(1433);
                ds.setDatabaseName("CRKF");
                ds.setIntegratedSecurity(false);
                ds.setEncrypt(false);
                ds.setUser("sa");
                ds.setPassword("azerty@123456");

                connexion = ds.getConnection();
            }

            // Handle any errors that may have occurred.
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return connexion;
    }

}

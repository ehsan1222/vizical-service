package com.mxgraph.examples.swing.db;

import com.mxgraph.examples.swing.log.ClientLog;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.util.Properties;

public class DbManager {

    public static final String DB_DRIVER = "org.postgresql.Driver";
    public static final String DB_URL = "jdbc:postgresql://localhost:5432/vizikadb";
    public static final String DB_USER = "vizika";
    public static final String DB_PASS = "vizika";
    public static final String DB_DIALECT = "org.hibernate.dialect.PostgreSQL10Dialect";
    public static final String DB_CURRENT_SESSION_CONTEXT_CLASS = "thread";
    public static final String DB_HBM2DDL_AUTO = "update";
    public static final String DB_SHOW_SQL = "false";

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try{
                Configuration configuration = new Configuration();

                Properties properties = new Properties();
                properties.put(Environment.DRIVER, DB_DRIVER);
                properties.put(Environment.URL, DB_URL);
                properties.put(Environment.USER, DB_USER);
                properties.put(Environment.PASS, DB_PASS);
                properties.put(Environment.DIALECT, DB_DIALECT);
                properties.put(Environment.SHOW_SQL, DB_SHOW_SQL);

                properties.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, DB_CURRENT_SESSION_CONTEXT_CLASS);
                properties.put(Environment.HBM2DDL_AUTO, DB_HBM2DDL_AUTO);

                configuration.setProperties(properties);
                configuration.addAnnotatedClass(ClientLog.class);

                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();

                sessionFactory = configuration.buildSessionFactory(serviceRegistry);

            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        return sessionFactory;
    }

}

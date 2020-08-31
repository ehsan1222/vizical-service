package com.mxgraph.examples.swing.service;

import com.mxgraph.examples.swing.db.DbManager;
import com.mxgraph.examples.swing.log.ActionType;
import com.mxgraph.examples.swing.log.ClientLog;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class LogService {

    public static final String USER = "user";

    public void saveLog(ActionType actionType) {
        Transaction transaction = null;

        try (Session session = DbManager.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            ClientLog clientLog = new ClientLog(USER, actionType);

            session.save(clientLog);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}

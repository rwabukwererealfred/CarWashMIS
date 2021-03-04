package com.carwash.dao;



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class SessionManager {

    private static SessionFactory fact = null;

    private SessionManager() {
    }

    public static Session getSession() {
        if (fact == null) {
            Configuration conf = new Configuration().configure();
            fact = conf.buildSessionFactory();
        }
        return fact.openSession();
    }
}

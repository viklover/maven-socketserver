package com.company.dao;

import com.company.HibernateSessionFactory;
import com.company.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserDAOImpl implements UserDAO {


    public SessionFactory sessionFactory;

    public UserDAOImpl() {
        sessionFactory = HibernateSessionFactory.getSessionFactory();
    }

    public void addUser(User user) {
        Session session = sessionFactory.openSession();

        session.beginTransaction();

        session.save(user);
        session.getTransaction().commit();

        session.close();
    };

    public List<User> listUsers() {
        Session session = sessionFactory.openSession();
        return session.createQuery("from UsersEntity ").list();
    };

    public boolean checkExist(String nickname) {
        return sessionFactory.openSession().get(User.class, nickname) != null;
    }

    public boolean auth(User user) {

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        User user_from_db = session.get(User.class, user.getNickname());

        return user_from_db.equals(user);
    }
}

package com.company.dao;

import com.company.User;

import java.awt.*;
import java.util.List;

public interface UserDAO {
    public void addUser(User user);
    public List<User> listUsers();
}

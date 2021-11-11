package com.company;

import org.hibernate.annotations.ColumnTransformer;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {

    public User() {}

    public User(String nickname) {
        this.nickname = nickname;
    }

    public User(String nickname, String password) {
        this.nickname = nickname;
        setPassword(password);
    }

    public User(String nickname, String password, boolean hashing_password) {
        this.nickname = nickname;
        setPassword(password, hashing_password);
    }

    @Id
    @Column(name="nickname", unique=true, nullable=false)
    private String nickname;

    @Column(name = "password", nullable=false)
    private String password;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = String.valueOf(password.hashCode());
    }

    public void setPassword(String password, boolean hashing_password) {
        if (hashing_password) {
           setPassword(password);
        } else {
            this.password = password;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(nickname, user.nickname) && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nickname, password);
    }
}

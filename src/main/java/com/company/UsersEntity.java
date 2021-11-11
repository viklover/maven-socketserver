package com.company;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "users", schema = "sserver", catalog = "")
public class UsersEntity {
    private String nickname;
    private String password;

    @Id
    @Column(name = "nickname", nullable = false, length = 20)
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Basic
    @Column(name = "password", nullable = false, length = 40)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsersEntity that = (UsersEntity) o;
        return Objects.equals(nickname, that.nickname) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nickname, password);
    }
}

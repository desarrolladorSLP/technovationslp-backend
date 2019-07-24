package org.desarrolladorslp.technovation.models;

import java.io.Serializable;
import java.util.Objects;
import java.util.StringJoiner;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "firebase_users")
public class FirebaseUser implements Serializable {

    private static final long serialVersionUID = 2592836077741923786L;

    @Id
    private String uid;

    private String email;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FirebaseUser that = (FirebaseUser) o;
        return uid.equals(that.uid) &&
                email.equals(that.email) &&
                Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, email, user);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", FirebaseUser.class.getSimpleName() + "[", "]")
                .add("uid='" + uid + "'")
                .add("email='" + email + "'")
                .add("user=" + user)
                .toString();
    }
}

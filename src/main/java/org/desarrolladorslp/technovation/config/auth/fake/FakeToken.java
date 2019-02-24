package org.desarrolladorslp.technovation.config.auth.fake;

import java.util.Objects;
import java.util.StringJoiner;

public class FakeToken {

    private String uid;
    private String name;
    private String email;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FakeToken fakeToken = (FakeToken) o;
        return uid.equals(fakeToken.uid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", FakeToken.class.getSimpleName() + "[", "]")
                .add("uid='" + uid + "'")
                .add("name='" + name + "'")
                .add("email='" + email + "'")
                .toString();
    }
}

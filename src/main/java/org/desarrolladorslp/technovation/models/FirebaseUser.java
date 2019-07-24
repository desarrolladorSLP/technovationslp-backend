package org.desarrolladorslp.technovation.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "firebase_users")
@Builder
@Data
public class FirebaseUser implements Serializable {

    private static final long serialVersionUID = 2592836077741923786L;

    @Id
    private String uid;

    private String email;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
}

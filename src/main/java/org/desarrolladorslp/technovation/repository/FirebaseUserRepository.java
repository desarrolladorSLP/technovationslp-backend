package org.desarrolladorslp.technovation.repository;

import org.desarrolladorslp.technovation.models.FirebaseUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FirebaseUserRepository extends CrudRepository<FirebaseUser, String> {

    FirebaseUser findByEmail(String email);
}

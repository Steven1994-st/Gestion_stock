package com.gestion.repositories;

import com.gestion.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends CrudRepository<User,Long> {

    /** Find an User in the database by using his login
     *
     * @param login the login
     * @return the User found or null
     */
    @Query("SELECT r FROM User r where r.login=:l")
    User findByLogin(@Param("l") String login);

    /** Find an User in the database by using his email
     *
     * @param email the email
     * @return the User found or null
     */
    @Query("SELECT r FROM User r where r.email=:e")
    User findByEmail( @Param("e") String email);
}

package com.gestion.repository;

import com.gestion.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {

    /** Find an User in the database by using his email
     * @param email the email
     * @return the User found or null
     */
    @Query("SELECT r FROM User r where r.email=:e")
    User findByEmail( @Param("e") String email);

    /** Find users by role
     * @param role the user role
     * @return the Users found or null
     */
    @Query("SELECT r FROM User r where r.role=:r")
    List<User> findByRole(@Param("r")User.ROLE role);
}

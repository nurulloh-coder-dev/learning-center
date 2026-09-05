package org.example.crm.repository;

import jakarta.transaction.Transactional;
import org.example.crm.entity.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, String> {
    @Query(value = "select * from users where deleted = false and fullname ilike concat('%',:search,'%')",
            countQuery = "select count(id) from users where deleted = false and fullname ilike concat('%',:search,'%')",
            nativeQuery = true)
    Page<User> findAll(Pageable pageable, @Param("search") String search);


    @Query("""
                                select u
                                from User u
                                left join fetch u.branch
                                where u.phone = :phone
                                and u.deleted = false
            """)
    Optional<User> findByPhoneAndDeletedFalse(String phone);


    @Query("select u from User u where u.phone=:phone")
    Optional<User> findByPhone(@Param("phone") String subject);

    @Query("""
                    select u
                    from User u where u.phone = :username
            """)
    Optional<User> findUserByPhone(String username);


    @Query("""
                    select u
                    from User u
                     left join fetch u.branch
                     where u.id = :s
                     and u.deleted = false
            """)
    Optional<User> findByIdAndDeletedFalse(String s);

    @Query("select exists (select u.id from User u where u.id=:id and u.deleted = false)")
    Optional<Boolean> checkId(@Param("id") String id);

    @Modifying
    @Transactional
    @Query("UPDATE User u set u.deleted = true where u.id=:id and u.organizationId = :orgId")
    int softDelete(@Param("id") String id, @Param("orgId") String organizationId);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.imageUrl =:imageUrl where u.id=:userId and u.deleted = false")
    void updateUserImage(@Param("userId") String userId, @Param("imageUrl") String presignedUrl);
}

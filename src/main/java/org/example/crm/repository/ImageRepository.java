package org.example.crm.repository;

import jakarta.transaction.Transactional;
import org.example.crm.entity.model.Image;
import org.example.crm.projection.ImageProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, String> {
    @Query("SELECT i from Image i where i.createdBy =:userId and i.deleted = false")
    Page<ImageProjection> findAllByUserId(@Param("userId") String userId, Pageable pageable);

    @Query("SELECT EXISTS (SELECT i.id FROM Image i WHERE i.id=:id)")
    Optional<Boolean> checkId(@Param("id") String id);

    @Transactional
    @Modifying
    @Query("UPDATE User u set u.imageUrl =(SELECT i.imageUrl FROM Image i WHERE i.id =:imgId) WHERE u.id =:userId")
    void updateMainImg(@Param("imgId") String id, @Param("userId") String userId);

    @Transactional
    @Modifying
    @Query("UPDATE Image i set i.deleted = true where i.id =:id and i.createdBy =:userId")
    void softDelete(String id, String userId);
}

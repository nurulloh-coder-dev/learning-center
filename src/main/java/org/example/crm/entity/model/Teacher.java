package org.example.crm.entity.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.example.crm.entity.base.BaseEntity;
import org.example.crm.entity.base.IdEntity;

import java.util.List;

@Entity
@Table(name = "teachers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Teacher extends IdEntity {


    @OneToOne(optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id", referencedColumnName = "id", unique = true)
    private User user;

    private float totalTeachingExp;
    private float currPlaceTeachingExp;

    @ManyToMany(fetch = FetchType.LAZY)
    List<Course> courses;
}
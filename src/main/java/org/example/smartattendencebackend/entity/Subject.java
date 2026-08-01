package org.example.smartattendencebackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false , unique = true)
    private String code;

    @ManyToOne
    @JoinColumn(name = "department_id" , nullable = false)
    private Department department;

    @ManyToOne
    @JoinColumn(name = "teacher_id" , nullable = false)
    private Teacher teacher;
}

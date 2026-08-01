package org.example.smartattendencebackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Student extends User{

    @Column(nullable = false , unique = true)
    private String rollNumber;

    @ManyToOne
    @JoinColumn(name = "department_id" , nullable = false)
    private Department department;

    @ManyToOne
    @JoinColumn(name = "session_id" , nullable = false)
    private Session session;
}

package br.com.bibliotec.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Table
@Entity(name = "student")
public class Student {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @NotBlank
    @Column(name = "ra", updatable = false)
    private String ra;
    
    @NotBlank
    @Size(min = 10, message = "O nome deve ter no mínimo 10 caracteres.")
    @Column(name = "name", length = 60)
    private String name;

    @NotBlank
    @Column(name = "student_class", length = 60)
    private String studentClass;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRa() {
        return ra;
    }

    public void setRa(String ra) {
        this.ra = ra;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStudentClass() {
        return studentClass;
    }

    public void setStudentClass(String studentClass) {
        this.studentClass = studentClass;
    }

    @Override
    public String toString() {
        return "Student{" +
                "ra=" + ra +
                ", name='" + name + '\'' +
                ", studentClass='" + studentClass + '\'' +
                '}';
    }
}

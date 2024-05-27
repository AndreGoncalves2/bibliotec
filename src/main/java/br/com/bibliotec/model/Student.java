package br.com.bibliotec.model;

import br.com.bibliotec.interfaces.HasId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Table
@Entity(name = "student")
public class Student implements HasId<Long> {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotEmpty(message = "Campo obrigatório")
    @Size(max = 10, min = 10, message = "O RA deve conter 10 caracteres.")
    @Column(name = "ra", updatable = false)
    private String ra;

    @NotEmpty(message = "Campo obrigatório")
    @Size(min = 5, message = "O nome deve ter no mínimo 5 caracteres.")
    @Column(name = "name", length = 60)
    private String name;

    @NotEmpty(message = "Campo obrigatório")
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
}

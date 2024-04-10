package br.com.bibliotec.builder;

import br.com.bibliotec.model.Student;

import java.util.Random;

public class StudentBuilder {
    
    private Student student;
    
    public static StudentBuilder build() {
        StudentBuilder builder = new StudentBuilder();
        builder.student = new Student();
        
        builder.student.setRa(String.valueOf((new Random().nextInt(100001))));
        builder.student.setName("nomeTesteee");
        builder.student.setStudentClass("Classe teste");
        
        return builder;
    }
    
    public Student now() {
        return student;
    }
}

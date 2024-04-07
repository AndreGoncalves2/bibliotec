package br.com.bibliotec.builder;

import br.com.bibliotec.model.Student;

public class StudentBuilder {
    
    private Student student;
    
    public static StudentBuilder build() {
        StudentBuilder builder = new StudentBuilder();
        builder.student = new Student();
        
        builder.student.setRa(1234567);
        builder.student.setName("nomeTest");
        builder.student.setStudentClass("Classe teste");
        
        return builder;
    }
    
    public Student now() {
        return student;
    }
}

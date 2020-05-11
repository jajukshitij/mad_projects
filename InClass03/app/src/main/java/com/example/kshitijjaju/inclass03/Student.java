package com.example.kshitijjaju.inclass03;

import java.io.Serializable;

public class Student implements Serializable {
    String name;
    int student_id;
    String dept;
    int image_id;

    public Student(String name, int student_id, String dept, int image_id) {
        this.name = name;
        this.student_id = student_id;
        this.dept = dept;
        this.image_id = image_id;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", student_id=" + student_id +
                ", dept='" + dept + '\'' +
                ", image=" + image_id +
                '}';
    }
}

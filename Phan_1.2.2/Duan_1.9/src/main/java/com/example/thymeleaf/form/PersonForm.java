package com.example.thymeleaf.form;

// Đối tượng nhận dữ liệu từ form addPerson.html (form-backing bean)
public class PersonForm {

    private String firstName;
    private String lastName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}

package com.noahmedhub.model;

public class User {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String comment;
    private String consultant1;
    private String consultant2;
    private String consultant3;

    public User() {}

    // 兼容 firstName
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    // 兼容 lastName
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    // 同时也保留旧名称的 Getter/Setter（防止 UserResource 编译报错）
    public String getFirstname() { return firstName; }
    public void setFirstname(String firstname) { this.firstName = firstname; }

    public String getLastname() { return lastName; }
    public void setLastname(String lastname) { this.lastName = lastname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public String getConsultant1() { return consultant1; }
    public void setConsultant1(String consultant1) { this.consultant1 = consultant1; }

    public String getConsultant2() { return consultant2; }
    public void setConsultant2(String consultant2) { this.consultant2 = consultant2; }

    public String getConsultant3() { return consultant3; }
    public void setConsultant3(String consultant3) { this.consultant3 = consultant3; }
}
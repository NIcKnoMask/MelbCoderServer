package com.unimelbCoder.melbcode.bean;

public class User {
    private int id;
    private String username;
    private String password;
    private String email;
    private String role;
    private int age;
    private boolean state;

    public User(int id, String username, String password, String email, String role, int age, boolean state) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.age = age;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public int getAge() {
        return age;
    }

    public boolean isState() {
        return state;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", age=" + age +
                ", state=" + state +
                '}';
    }
}

package com.example.user.druberapplication.network.model;

public class Test {
    private String name;
    private int age;
    private int image;

    public Test(String name, int age, int image) {
        this.name = name;
        this.age = age;
        this.image = image;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}

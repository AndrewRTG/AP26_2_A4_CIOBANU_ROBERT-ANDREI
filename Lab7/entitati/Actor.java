package com.example.demo.entitati;
public class Actor {
    private Integer id;
    private String name;

    public Actor() {}

    public Actor(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @Override
    public String toString() {
        return "Actor{id=" + id + ", name='" + name + "'}";
    }
}
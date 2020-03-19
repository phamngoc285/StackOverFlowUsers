package com.phamngoc.sofusers.Model;

public class User {

    public String name;
    public String avatar;
    public String reputation;
    public String location;
    public String lastAccessDate;

    public User(){

    }

    public User(String name, String avatar, String reputation, String location, String lastAccessDate) {
        this.name = name;
        this.avatar = avatar;
        this.reputation = reputation;
        this.location = location;
        this.lastAccessDate = lastAccessDate;
    }
}

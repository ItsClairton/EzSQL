package com.gitlab.pauloo27.core.sql;

@Name("friends")
public class Friend {
    @Id
    int id;

    @DefaultAttributes.NotNull
    @DefaultAttributes.Unique
    @Name("name")
    @Length(32)
    String username;

    @Length(32)
    @DefaultAttributes.Unique
    String phone;

    @DefaultAttributes.NotNull
    int age;

    @Length(64)
    @DefaultAttributes.NotNull
    @DefaultAttributes.Unique
    String email = "No e-mail";

    public Friend(String username, int age, String phone, String email) {
        this.username = username;
        this.age = age;
        this.phone = phone;
        this.email = email;
    }

    public Friend() {

    }

    @Override
    public String toString() {
        return String.format("{id: %d, name: %s, age: %d, phone: %s, email: %s}", id, username, age, phone, email);
    }
}

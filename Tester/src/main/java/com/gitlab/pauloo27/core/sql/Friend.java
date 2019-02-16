package com.gitlab.pauloo27.core.sql;

@Name("amigos")
public class Friend {
    @Id
    int id;

    @Length(30)
    @DefaultAttributes.Unique
    @DefaultAttributes.NotNull
    String name;

    @DefaultAttributes.NotNull
    @Name("idade")
    int age;

    int phone;

    @Length(30)
    @DefaultAttributes.NotNull
    @DefaultAttributes.Unique
    String email = "No email";

    @Override
    public String toString() {
        return String.format("{id: %d, name: %s, age: %d, phone: %d, email: %s}", id, name, age, phone, email);
    }

    public Friend() {
    }

    public Friend(String name, int age, int phone, String email) {
        this.name = name;
        this.age = age;
        this.phone = phone;
        this.email = email;
    }
}

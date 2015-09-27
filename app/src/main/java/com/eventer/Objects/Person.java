package com.eventer.Objects;

/**
 * Created by rg on 30-Aug-15.
 */
public class Person {
    String name;
    String email;

    public Person() {}

    public Person(String personName) {
        this.name = personName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

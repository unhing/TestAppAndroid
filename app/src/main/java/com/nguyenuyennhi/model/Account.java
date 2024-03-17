package com.nguyenuyennhi.model;

import java.io.Serializable;

public class Account implements Serializable {
    private String username;
    private String password;

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Account() {
    }
}

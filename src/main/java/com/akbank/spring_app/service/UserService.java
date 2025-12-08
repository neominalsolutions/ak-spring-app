package com.akbank.spring_app.service;


import com.akbank.spring_app.SpringAppApplication;
import org.springframework.stereotype.Service;

// Auto Bean Manuel User Bean içerisindeki kodlara ihtiyaç duydu en temiz yöntem ise DI kullanmak
// streotype annotation ile Bean i otomatik tanımladık
@Service
public class UserService {

    private final SpringAppApplication.User user2;


    public UserService(SpringAppApplication.User user2) {
        this.user2 = user2;
    }

    public void handleUser() {
        System.out.println("Handling user: " + user2.getName());
    }

}

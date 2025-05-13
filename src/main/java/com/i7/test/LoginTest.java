package com.i7.test;

import com.i7.entity.UserAccount;

import java.util.Scanner;

public class LoginTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Simulated Login Test ===");
        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        UserAccount user = UserAccount.authenticateLogin(email, password);

        if (user != null) {
            System.out.println("Login successful!");
            System.out.println("Welcome, " + user.getFirstName() + " " + user.getLastName());
            System.out.println("Your role: " + user.getProfileCode());
            System.out.println("Account status: " + user.getStatus());
        } else {
            System.out.println("Login failed. Invalid credentials or account not found.");
        }

        scanner.close();
    }
}

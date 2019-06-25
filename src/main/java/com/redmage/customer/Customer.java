package com.redmage.customer;

import com.redmage.bank.Account;
import com.redmage.bank.Card;

public class Customer {
    private String firstName;
    private String lastName;
    private String physicalAddress;
    private String emailAddress;
    private Card card;
    private Account account;

    public Customer(String firstName, String lastName, String physicalAddress,
                    String emailAddress, Card card, Account account) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.physicalAddress = physicalAddress;
        this.emailAddress = emailAddress;
        this.card = card;
        this.account = account;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhysicalAddress() {
        return physicalAddress;
    }

    public void setPhysicalAddress(String physicalAddress) {
        this.physicalAddress = physicalAddress;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "Card " + card.getNumber() + " | " +
                "Pin: " + card.getPinCode() + " | " +
                firstName + " " + lastName + " | " +
                physicalAddress + " | " +
                emailAddress;
    }
}

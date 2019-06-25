package com.redmage.bank;

import com.redmage.dao.BankDAO;

public class Card {
    private static int cardCount;
    private final int number;
    private int pinCode;

    static {
        cardCount = BankDAO.getLatestCardNumber();
    }

    public Card(int number, int pinCode) {
        this.number = number;
        this.pinCode = pinCode;
    }

    Card(int pinCode) {
        this(++cardCount, pinCode);
    }


    public int getNumber() {
        return number;
    }

    public int getPinCode() {
        return pinCode;
    }

    public void setPinCode(int pinCode) {
        this.pinCode = pinCode;
    }

    public static void setCardCount(int cardCount) {
        Card.cardCount = cardCount;
    }
}

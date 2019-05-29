package com.redmage.client;

import com.redmage.bank.ATM;
import com.redmage.bank.Bank;

public class ATMApp {
    public static void main(String[] args) {
        ATM atm = new ATM(new Bank("Java"));
        atm.startATM();
    }
}


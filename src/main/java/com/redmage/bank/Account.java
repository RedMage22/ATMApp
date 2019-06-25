package com.redmage.bank;

import com.redmage.dao.BankDAO;

import java.text.NumberFormat;
import java.util.Date;

public class Account {
    private String name;
    private double balance;
//    private List<Transaction> transactions;
    private NumberFormat numberFormat;

    public Account(String name) {
        this.name = name;
//        this.transactions = new ArrayList<>();
        this.numberFormat = NumberFormat.getCurrencyInstance();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

//    public List<Transaction> getTransactions() {
//        return transactions;
//    }

    public boolean withdrawal(int cardNumber, double amount) {
        if(balance >= amount && amount > 0.00) { // Assuming that a balance of zero is acceptable
            balance -= amount;
            Transaction transaction = new Transaction(new Date(),"Withdrawal", -amount, balance);
//            transactions.add(transaction);
            BankDAO.addTransaction(cardNumber, transaction.getType(),
                    transaction.getAmount(), transaction.getBalance());
            System.out.println("Balance = " + numberFormat.format(balance));
            return true;
        }
        return false;
    }

    public boolean deposit(int cardNumber, double amount) {
        if(amount > 0.00) { // Depositing an amount of zero is not considered a valid transaction
            balance += amount;
            Transaction transaction = new Transaction(new Date(),"Deposit", amount, balance);
//            transactions.add(transaction);
            BankDAO.addTransaction(cardNumber, transaction.getType(),
                    transaction.getAmount(), transaction.getBalance());
            System.out.println("Balance = " + numberFormat.format(balance));
            return true;
        }
        return false;
    }
}

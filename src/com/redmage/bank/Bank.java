package com.redmage.bank;

import com.redmage.customer.Customer;
import com.redmage.doa.BankDAO;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Bank {
    private String name;
    private NumberFormat numberFormat;

    public Bank(String name) {
        this.name = name;
        this.numberFormat = NumberFormat.getCurrencyInstance();
        // Create the bank database
    }

    String getName() {
        return name;
    }

    boolean addCustomer(String firstName, String lastName, String physicalAddress,
                        String emailAddress, int pinCode, double initialAmount) {
        if(initialAmount >= 50.00) {
            Card card = new Card(pinCode);
            Account account = new Account("Elite");
            account.deposit(card.getNumber(), initialAmount);
            Customer customer = new Customer(firstName, lastName, physicalAddress, emailAddress, card, account);
            BankDAO.addNewCustomer(customer);
            System.out.println("Processing...\nCustomer: " + customer.getFirstName() + " " + customer.getLastName() +
                    "\n" + account.getName() + " account\n" + "Card number: " + card.getNumber() +
                    "\nPin code: " + card.getPinCode() + "\nInitial balance: " + numberFormat.format(account.getBalance()));
            return true;
        }
        return false;
    }

    boolean removeCustomer(int cardNumber, int pinCode) {
        Customer customer = BankDAO.getCustomer(cardNumber);
        if(customer != null) {
            double balance = BankDAO.getCustomerBalance(cardNumber);
            Account account = customer.getAccount();
            account.setBalance(balance);
            Card card = customer.getCard();
            if(card.getPinCode() == pinCode) {
                System.out.println("-Removing customer-");
                System.out.println(customer.getFirstName() + " " + customer.getLastName() + "\n" +
                        account.getName() + " account\n" + "Card number: " + card.getNumber() +
                        "\nClosing balance = " + numberFormat.format(account.getBalance()) +
                        "\nProcessing...\n ");
                boolean deletedTransactions = BankDAO.removeCustomerTransactions(cardNumber);
                if(deletedTransactions) {
                    return BankDAO.removeCustomer(cardNumber);
                }

            } else {
                System.out.println("Pin code incorrect");
                return false;
            }
        }
        System.out.println("Card number not found in database");
        return false;
    }

    boolean verifyCardNumber(int cardNumber) {
        return cardNumber == BankDAO.getCardNumber(cardNumber);
    }

    boolean verifyPinCode(int cardNumber, int pinCode) {
        return pinCode == BankDAO.getPinCode(cardNumber);
    }

    Customer getCustomer(int cardNumber, int pinCode) {
        Customer customer = BankDAO.getCustomer(cardNumber);
        if(customer != null) {
            if(customer.getCard().getPinCode() == pinCode)
                return customer;
        }
        return null;
    }

    Customer getCustomer(int cardNumber) {
        Customer customer = BankDAO.getCustomer(cardNumber);
        if(customer != null) {
            return customer;
        }
        return null;
    }

    boolean withdrawal(int cardNumber, int pinCode, double amount) {
        Customer customer = BankDAO.getCustomer(cardNumber);
        if(customer != null) {
            Account account = customer.getAccount();
            account.setBalance(checkBalance(cardNumber));
            return account.withdrawal(cardNumber, amount);
        }
        return false;
    }

    boolean deposit(int cardNumber, int pinCode, double amount) {
        Customer customer = BankDAO.getCustomer(cardNumber);
        if(customer != null) {
            Account account = customer.getAccount();
            account.setBalance(checkBalance(cardNumber));
            return account.deposit(cardNumber, amount);
        }
        return false;
    }

    double checkBalance(int cardNumber) {
        return BankDAO.getCustomerBalance(cardNumber);
    }

    void printCustomerTransactions(int cardNumber, int pinCode) {
        Customer customer = BankDAO.getCustomer(cardNumber);
        if(customer != null) {
            System.out.println(this.name + " bank: " + customer.getAccount().getName() + " account");
            System.out.println("-Print Statement (" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + ")-");
            BankDAO.printCustomerTransactions(cardNumber);
            customer.getAccount().setBalance(checkBalance(cardNumber));
            System.out.println("------------------");
            System.out.println("Balance = " + numberFormat.format(customer.getAccount().getBalance()));
            System.out.println("------------------");
        }
    }

    public void updateFirstName(int cardNumber, int pinCode, String newFirstName) {
        Customer customer = BankDAO.getCustomer(cardNumber);
        if(customer != null) {
            Card card = customer.getCard();
            if(card.getPinCode() == pinCode) {
                boolean isUpdated = BankDAO.updateFirstName(cardNumber, newFirstName);
                if(isUpdated) {
                    System.out.println("Updated first name from " + customer.getFirstName() +
                            " to " + newFirstName);
                } else {
                    System.out.println("Update failed!");
                }
            }
        }
    }

    public void updateLastName(int cardNumber, int pinCode, String newLastName) {
        Customer customer = BankDAO.getCustomer(cardNumber);
        if(customer != null) {
            Card card = customer.getCard();
            if(card.getPinCode() == pinCode) {
                boolean isUpdated = BankDAO.updateLastName(cardNumber, newLastName);
                if(isUpdated) {
                    System.out.println("Updated last name from " + customer.getLastName() +
                            " to " + newLastName);
                } else {
                    System.out.println("Update failed!");
                }
            }
        }
    }

    public void updatePinCode(int cardNumber, int oldPinCode, int newPinCode) {
        Customer customer = BankDAO.getCustomer(cardNumber);
        if(customer != null) {
            Card card = customer.getCard();
            if(card.getPinCode() == oldPinCode) {
                boolean isUpdated = BankDAO.updatePinCode(cardNumber, newPinCode);
                if(isUpdated) {
                    System.out.println("Updated pin code from " + oldPinCode +
                            " to " + newPinCode);
                } else {
                    System.out.println("Update failed!");
                }
            }
        }
    }

    public void updatePhysicalAddress(int cardNumber, int pinCode, String newPhysicalAddress) {
        Customer customer = BankDAO.getCustomer(cardNumber);
        if(customer != null) {
            Card card = customer.getCard();
            if(card.getPinCode() == pinCode) {
                boolean isUpdated = BankDAO.updatePhysicalAddress(cardNumber, newPhysicalAddress);
                if(isUpdated) {
                    System.out.println("Updated address from " + customer.getPhysicalAddress() +
                            " to " + newPhysicalAddress);
                } else {
                    System.out.println("Update failed!");
                }
            }
        }
    }

    public void updateEmailAddress(int cardNumber, int pinCode, String newEmailAddress) {
        Customer customer = BankDAO.getCustomer(cardNumber);
        if(customer != null) {
            Card card = customer.getCard();
            if(card.getPinCode() == pinCode) {
                boolean isUpdated = BankDAO.updateEmailAddress(cardNumber, newEmailAddress);
                if(isUpdated) {
                    System.out.println("Updated email from " + customer.getEmailAddress() +
                            " to " + newEmailAddress);
                } else {
                    System.out.println("Update failed!");
                }
            }
        }
    }

    void printAllCustomers() {
        System.out.println("Customers of " + this.name + " bank");
        BankDAO.printAllCustomers();
    }

    void printAllTransactions() {
        System.out.println("Transactions done at " + this.name + " bank");
        BankDAO.printAllTransactions();
    }
}

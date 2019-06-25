package com.redmage.bank;

import com.redmage.customer.Customer;

import java.text.NumberFormat;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ATM {
    private String name;
    private int cardNumber;
    private int pinCode;
    private Customer customer;
    private Bank bank;
    private NumberFormat numberFormat;

    private static Scanner scanner = new Scanner(System.in);

    public ATM(Bank bank) {
        this.name = bank.getName() + " bank ATM #001";
        this.bank = bank;
        this.numberFormat = NumberFormat.getCurrencyInstance();
    }

    public void startATM() {
        boolean quit = false;
        while (!quit) {
            printStartMenu();
            System.out.println("Choose Option and press ENTER: ");
            int option = getOptionFromScanner();

            switch (option) {
                case 0:
                    quit = true;
                    System.out.print("ATM shutting down");
                    loadDots(3);
                    System.out.println("ATM is OFF");
                    break;
                case 1:
                    startAdminMode();
                    break;
                case 2:
                    startTransactionMode();
                    break;
                default:
                    System.out.println("Please enter a valid option");
                    break;
            }
        }
    }

    private void printStartMenu() {
        printLoadingMessage(1);
        System.out.println("\n*Welcome to " + this.name + "*\n-Start Menu-\n" +
                "Press\n" +
                "0 - Shutdown\n" +
                "1 - Admin Mode\n" +
                "2 - Transaction Mode\n");
    }

    private int getOptionFromScanner() {
        try {
            return scanner.nextInt();
        } catch (InputMismatchException e) {
            scanner.next();
            System.out.println("Characters not allowed.\nPlease use numbers only");
            return -1;
        }
    }

    private void startAdminMode() { // admin password required
        boolean quit = false;
        while (!quit) {
            printAdminModeMenu();
            System.out.println("Choose Option and press ENTER: ");
            int option = getOptionFromScanner();
            scanner.nextLine();
            switch (option) {
                case 0:
                    quit = true;
                    break;
                case 1:
                    addCustomer();
                    break;
                case 2:
                    removeCustomer();
                    break;
                case 3:
                    editCustomer();
                    break;
                case 4:
                    printLoadingMessage(3);
                    bank.printAllCustomers();
                    System.out.println("Press ENTER to return to the Previous Menu");
                    scanner.nextLine();
                    break;
                case 5:
                    printLoadingMessage(3);
                    bank.printAllTransactions();
                    System.out.println("Press ENTER to return to the Previous Menu");
                    scanner.nextLine();
                    break;
                default:
                    System.out.println("Please enter a valid option");
                    break;
            }
        }
    }

    private void printAdminModeMenu() {
        printLoadingMessage(1);
        System.out.println("\n-Admin Mode-\n" +
                "-Main Menu-\n" +
                "Press\n" +
                "0 - Previous Menu\n" +
                "1 - New Customer\n" +
                "2 - Remove Customer\n" +
                "3 - Edit Customer\n" +
                "4 - Show Customers\n" +
                "5 - Show Transactions\n");
    }

    private void addCustomer() {
        System.out.println("\n-Admin Mode-\n-Adding New Customer-\n");
        System.out.println("Please enter first name: ");
        String firstName = scanner.nextLine();
        System.out.println("Please enter last name: ");
        String lastName = scanner.nextLine();
        System.out.println("Please enter physical address: ");
        String physicalAddress = scanner.nextLine();
        System.out.println("Please enter email address: ");
        String emailAddress = scanner.nextLine();
        System.out.println("Please enter preferred pin code: "); // should only be 5 digits long
        int preferredPin = getIntegerValueFromScanner();
        System.out.println("Please enter initial amount: ");
        double initialAmount = getDoubleValueFromScanner();
        boolean isCustomer = bank.addCustomer(firstName, lastName, physicalAddress, emailAddress,
                preferredPin, initialAmount);
        printLoadingMessage(2);
        if (isCustomer) {
            System.out.println("Customer added successfully!");
        } else {
            System.out.println("Failed to add customer!");
        }
    }

    private int getIntegerValueFromScanner() {
        while (true) {
            try {
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                scanner.next();
                System.out.println("Characters not allowed.\nPlease use numbers only");
            }
        }
    }

    private double getDoubleValueFromScanner() {
        while (true) {
            try {
                return scanner.nextDouble();
            } catch (InputMismatchException e) {
                scanner.next();
                System.out.println("Characters not allowed.\nPlease use numbers only:");
            }
        }
    }

    private void removeCustomer() {
        System.out.println("\n-Admin Mode-\n-Removing Existing Customer-\n");
        System.out.println("Please enter card number: ");
        int cardNumber = getIntegerValueFromScanner();
        printLoadingMessage(1);
        System.out.println("Please enter pin code: "); // should only be 5 digits long
        int pinCode = getIntegerValueFromScanner();
        printLoadingMessage(2);
        boolean isCustomer = bank.removeCustomer(cardNumber, pinCode);
        if (isCustomer) {
            System.out.println("Customer removed successfully!");
        } else {
            System.out.println("Failed to remove customer!");
        }
    }

    private void editCustomer() { // admin password required
        printLoadingMessage(2);
        System.out.println("Please enter card number: ");
        this.cardNumber = getIntegerValueFromScanner();
        printLoadingMessage(3);
        System.out.println("Please enter pin code: ");
        this.pinCode = getIntegerValueFromScanner();
        printLoadingMessage(3);
        this.customer = bank.getCustomer(this.cardNumber);
        System.out.println("Selected customer:\n" + this.customer);

        boolean quit = false;
        while (!quit) {
            printEditCustomerMenu();
            System.out.println("Choose Option and press ENTER: ");
            int option = getOptionFromScanner();
            scanner.nextLine();

            switch (option) {
                case 0:
                    quit = true;
                    break;
                case 1:
                    System.out.println("Please enter the new first name: ");
                    String firstName = scanner.nextLine();
                    bank.updateFirstName(this.cardNumber, this.pinCode, firstName);
                    break;
                case 2:
                    System.out.println("Please enter the new last name: ");
                    String lastName = scanner.nextLine();
                    bank.updateLastName(this.cardNumber, this.pinCode, lastName);
                    break;
                case 3:
                    System.out.println("Please enter the new pin code: ");
                    int newPinCode = getIntegerValueFromScanner();
                    bank.updatePinCode(this.cardNumber, this.pinCode, newPinCode);
                    break;
                case 4:
                    System.out.println("Please enter the new address: ");
                    String physicalAddress = scanner.nextLine();
                    bank.updatePhysicalAddress(this.cardNumber, this.pinCode, physicalAddress);
                    break;
                case 5:
                    System.out.println("Please enter the new email: ");
                    String emailAddress = scanner.nextLine();
                    bank.updateEmailAddress(this.cardNumber, this.pinCode, emailAddress);
                    break;
                default:
                    System.out.println("Please enter a valid option");
                    break;
            }
        }
    }

    private void printEditCustomerMenu() {
        printLoadingMessage(1);
        System.out.println("\n-Admin Mode-\n" +
                "-Edit Customer Menu-\n" +
                "Press\n" +
                "0 - Previous Menu\n" +
                "1 - First Name\n" +
                "2 - Last Name\n" +
                "3 - Pin Code\n" +
                "4 - Address\n" +
                "5 - Email\n");
    }

    private void startTransactionMode() {
        boolean quit = false;
        while (!quit) {
            printTransactionModeMenu();
            System.out.println("Choose Option and press ENTER: ");
            int option = getOptionFromScanner();
            scanner.nextLine();
            switch (option) {
                case 0:
                    quit = true;
                    break;
                case 1:
                    startCardTransactionMode();
                    break;
                default:
                    System.out.println("Please enter a valid option");
                    break;
            }
        }
    }

    private void printTransactionModeMenu() {
        printLoadingMessage(1);
        System.out.println("-Transaction Mode Menu-\n" +
                "Press\n" +
                "0 - Previous menu\n" +
                "1 - Card Transactions\n");
    }

    private void startCardTransactionMode() {
        printLoadingMessage(1);
        System.out.println("\n*Welcome to " + bank.getName() + " bank*\n-Transactions-\n");
        int option = 0; // Moved option's scope to cater for cardTransactionMode loop
        boolean isVerified = cardVerification();

        if (isVerified) {
            boolean quit = false;
            while (!quit) {
                printCardTransactionModeMenu();
                System.out.println("Choose Option and press ENTER: ");
                option = getOptionFromScanner();
                scanner.nextLine();
                switch (option) {
                    case 0:
                        quit = true;
                        System.out.print("Returning card");
                        loadDots(3);
                        break;
                    case 1:
                        withdrawal();
                        break;
                    case 2:
                        deposit();
                        break;
                    case 3:
                        printLoadingMessage(2);
                        System.out.println("Remaining balance: " + numberFormat.format(bank.checkBalance(this.cardNumber)));
                        System.out.println("Press ENTER to return to the Previous Menu");
                        scanner.nextLine();
                        break;
                    case 4:
                        printLoadingMessage(3);
                        bank.printCustomerTransactions(this.cardNumber, this.pinCode);
                        System.out.println("Press ENTER to return to the Previous Menu");
                        scanner.nextLine();
                        break;
                    case 5:
                        System.out.print("Returning card");
                        loadDots(3);
                        System.out.println("Thank you for your business.\nPlease call again.");
                        quit = true;
                        break;
                    default:
                        System.out.println("Please enter a valid option");
                        break;
                }
            }
        }

        if (option == 5) { // Once card is ejected, waits for new card to be inserted
            startCardTransactionMode();
        }
    }

    private boolean cardVerification() {
        // ask for card number - call bank to verify - if correct, store card number, else return to startAdminMode menu
        // ask for pin number - call bank to verify - if correct, store pin code, else re-enter pin code or 0 for startAdminMode menu
        // if pin code stored, store Customer details for further transactions and display Transactions Menu
        printLoadingMessage(1);
        System.out.println("Ready to receive card number...\nPlease enter a valid card number: "); // replace with card insertion mechanism
        int cardNumber = getIntegerValueFromScanner();
        System.out.print("Receiving card");
        loadDots(2);
        System.out.print("Reading card");
        loadDots(3);
        boolean isCardNumber = bank.verifyCardNumber(cardNumber);
        if (isCardNumber) {
            this.cardNumber = cardNumber;
            System.out.println("Card number verified");
        } else {
            System.out.println("Invalid card number");
            return false;
        }

        System.out.println("Please enter pin code: ");
        int pinCode = getIntegerValueFromScanner();
        System.out.print("Checking pin code");
        loadDots(3);
        boolean isPinCode = bank.verifyPinCode(this.cardNumber, pinCode);
        if (isPinCode) {
            this.pinCode = pinCode;
            this.customer = bank.getCustomer(this.cardNumber, this.pinCode);
            System.out.println("Pin code verified!");
            return true;
        } else {
            // give customer 2 more chances or the option to go to startAdminMode menu
            boolean quit = false;
            int counter = 1;
            while (!quit) {
                System.out.println("Incorrect pin code! " + (4 - counter) + " chances remaining!");
                System.out.println("Please enter valid pin code or \"0\" (zero) for main menu (return card): ");
                pinCode = getIntegerValueFromScanner();
                System.out.print("Checking pin code");
                loadDots(2);
                isPinCode = bank.verifyPinCode(this.cardNumber, pinCode);
                if (isPinCode) {
                    this.pinCode = pinCode;
                    this.customer = bank.getCustomer(this.cardNumber, this.pinCode);
                    return true;
                } else if (pinCode == 0) {
                    System.out.print("Returning card");
                    loadDots(3);
                    quit = true;
                } else if (counter == 3) {
                    System.out.println("You have entered the incorrect pin code 3 times");
                    System.out.print("Returning card");
                    loadDots(3);
                    quit = true;
                }
                counter++;
            }
            return false;
        }
    }

    private void printCardTransactionModeMenu() {
        printLoadingMessage(2);
        System.out.println("-Card Transaction Menu-\n" +
                "Press\n" +
                "0 - Previous menu (Return Card)\n" +
                "1 - Withdrawal\n" +
                "2 - Deposit\n" +
                "3 - Check Balance\n" +
                "4 - Print Statement\n" +
                "5 - Return Card");
    }

    private void withdrawal() {
        boolean quit = false;
        while (!quit) {
            printWithdrawalMenu();
            System.out.println("Choose Option and press ENTER: ");
            int option = getOptionFromScanner();
            scanner.nextLine();
            double amount = 0.0;

            switch (option) {
                case 0:
                    quit = true;
                    break;
                case 1:
                    amount = 50.0;
                    executeWithdrawal(this.cardNumber, this.pinCode, amount);
                    break;
                case 2:
                    amount = 100.0;
                    executeWithdrawal(this.cardNumber, this.pinCode, amount);
                    break;
                case 3:
                    amount = 200.0;
                    executeWithdrawal(this.cardNumber, this.pinCode, amount);
                    break;
                case 4:
                    amount = 350.0;
                    executeWithdrawal(this.cardNumber, this.pinCode, amount);
                    break;
                case 5:
                    amount = 500.0;
                    executeWithdrawal(this.cardNumber, this.pinCode, amount);
                    break;
                case 6:
                    System.out.println("Please enter withdrawal amount: ");
                    amount = getDoubleValueFromScanner();
                    executeWithdrawal(this.cardNumber, this.pinCode, amount);
                    break;
                default:
                    System.out.println("Please enter a valid option");
                    break;
            }
        }
    }

    private void printWithdrawalMenu() {
        printLoadingMessage(1);
        System.out.println("\n-Transactions-\n-Withdrawal Menu-\n" +
                "Press\n" +
                "0 - Previous Menu\n" +
                "1 - " + numberFormat.format(50) + "\n" +
                "2 - " + numberFormat.format(100) + "\n" +
                "3 - " + numberFormat.format(200) + "\n" +
                "4 - " + numberFormat.format(350) + "\n" +
                "5 - " + numberFormat.format(500) + "\n" +
                "6 - Enter Amount\n");
    }

    private void executeWithdrawal(int cardNumber, int pinCode, double amount) {
        printLoadingMessage(3);
        boolean canWithdraw = bank.withdrawal(this.cardNumber, this.pinCode, amount);
        if (!canWithdraw) {
            System.out.println("Insufficient funds!\nPlease check balance");
        }
    }

    private void deposit() {
        boolean quit = false;
        while (!quit) {
            printDepositMenu();
            System.out.println("Choose Option and press ENTER: ");
            int option = getOptionFromScanner();
            scanner.nextLine();
            double amount = 0.0;

            switch (option) {
                case 0:
                    quit = true;
                    break;
                case 1:
                    amount = 50.0;
                    executeDeposit(this.cardNumber, this.pinCode, amount);
                    break;
                case 2:
                    amount = 100.0;
                    executeDeposit(this.cardNumber, this.pinCode, amount);
                    break;
                case 3:
                    amount = 200.0;
                    executeDeposit(this.cardNumber, this.pinCode, amount);
                    break;
                case 4:
                    amount = 350.0;
                    executeDeposit(this.cardNumber, this.pinCode, amount);
                    break;
                case 5:
                    amount = 500.0;
                    executeDeposit(this.cardNumber, this.pinCode, amount);
                    break;
                case 6:
                    System.out.println("Please enter withdrawal amount: ");
                    amount = getDoubleValueFromScanner();
                    executeDeposit(this.cardNumber, this.pinCode, amount);
                    break;
                default:
                    System.out.println("Please enter a valid option");
                    break;
            }
        }
    }

    private void printDepositMenu() {
        printLoadingMessage(1);
        System.out.println("\n-Transactions-\n-Deposit Menu-\n" +
                "Press\n" +
                "0 - Previous Menu\n" +
                "1 - " + numberFormat.format(50) + "\n" +
                "2 - " + numberFormat.format(100) + "\n" +
                "3 - " + numberFormat.format(200) + "\n" +
                "4 - " + numberFormat.format(350) + "\n" +
                "5 - " + numberFormat.format(500) + "\n" +
                "6 - Enter Amount\n");
    }

    private void executeDeposit(int cardNumber, int pinCode, double amount) {
        printLoadingMessage(3);
        boolean canDeposit = bank.deposit(this.cardNumber, this.pinCode, amount);
        if (!canDeposit) {
            System.out.println("Deposit failed!\n");
        }
    }

    private void loadDots(int loadingTimeInSeconds) {
        int counter = 0;
        int dots = 3;
        loadingTimeInSeconds *= 1000 / dots;
        while(counter < dots) {
            try {
                Thread.sleep(loadingTimeInSeconds);
                System.out.print(".");
            } catch (InterruptedException ie) {
                System.out.println("Process interrupted");
            }
            counter++;
        }
        System.out.println();
    }

    private void printLoadingMessage(int loadingTimeInSeconds) {
        System.out.print("Loading");
        loadDots(loadingTimeInSeconds);
    }
}

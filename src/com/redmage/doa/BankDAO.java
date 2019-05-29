package com.redmage.doa;

import com.redmage.bank.Account;
import com.redmage.bank.Card;
import com.redmage.bank.Transaction;
import com.redmage.customer.Customer;

import java.sql.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BankDAO {
    static NumberFormat numberFormat = NumberFormat.getCurrencyInstance();

    private static Connection getConnection() {
//        System.out.println("getConnection()");
        Connection connection = null;
        try {
            // this section is for connection to SQLite database
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:database/bank.db";
            connection = DriverManager.getConnection(url);

            // this section is for connecting to MySQL database
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            String url = "jdbc:mysql://localhost:3306/bank?useSSL=false&allowPublicKeyRetrieval=true";
//            String url = "jdbc:mysql://localhost/bank";
//            String user = "root";
//            String password = "******"; // Enter a password
//            connection = DriverManager.getConnection(url, user, password);
        }
        catch (ClassNotFoundException e) {
            System.out.println("Class not found: " + e.getMessage());
            System.exit(0);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
        return connection;
    }

    public static void createCustomerTable() {
//        System.out.println("createCustomerTable()");
        String createCustomerTableSQL = "create table if not exists customers (\n" +
                " card_id integer primary key,\n" +
                " pin_code integer,\n" +
                " first_name text not null,\n" +
                " last_name text not null,\n" +
                " physical_address text not null,\n" +
                " email_address text\n" +
                ")";

        try (Connection connection = getConnection();
            Statement statement = connection.createStatement()) {
            statement.execute(createCustomerTableSQL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createTransactionTable() {
//        System.out.println("createTransactionTable()");
        String createTransactionTableSQL = "create table if not exists transactions (\n" +
                "card_id integer not null,\n" +
                " date text not null,\n" +
                " type text not null,\n" +
                " amount real,\n" +
                " balance real\n" +
                ")";

        try (Connection connection = getConnection();
            Statement statement = connection.createStatement()) {
            statement.execute(createTransactionTableSQL);
        } catch (SQLException e) {
            System.out.println("Couldn't create transactions table: " + e.getMessage());
        }
    }

    static {
        createCustomerTable();
        createTransactionTable();
    }

    // Should retrieve the last record created via date or auto incremented value
    // to keep track of card numbers generated
    public static int getLatestCardNumber() {
//        System.out.println("getLatestCardNumber()");
        String select = "select max(card_id) from customers";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet rows = statement.executeQuery(select);
            int latestCardNumber = 0;
            if(rows.next()) {
                latestCardNumber = rows.getInt(1);
//                System.out.println("Latest card number: " + latestCardNumber);
            } else {
                System.out.println("No card number found");
            }

            return latestCardNumber;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    public static int getCardNumber(int cardNumber) {
//        System.out.println("getCardNumber()");
        String select = "select card_id from customers where card_id = " + cardNumber;

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet rows = statement.executeQuery(select);
            if(rows.next()) {
                return rows.getInt("card_id");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Could not retrieve card number");
        return -1;
    }

    // use card number to query customer info, pin codes and transactions
    public static int getPinCode(int cardNumber) {
//        System.out.println("getPinCode()");
        String select = "select pin_code from customers where card_id = " + cardNumber;

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet rows = statement.executeQuery(select);

            if(rows.next()) {
                return rows.getInt("pin_code");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Could not retrieve pin code");
        return -1;
    }

    public static Customer getCustomer(int cardNumber) {
//        System.out.println("getCustomer()");
        String select = "select pin_code, first_name, last_name, physical_address, email_address" +
                " from customers where card_id = " + cardNumber;

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet rows = statement.executeQuery(select);

            if(rows.next()) {
                int pinCode = rows.getInt("pin_code");
                String firstName = rows.getString("first_name");
                String lastName = rows.getString("last_name");
                String physicalAddress = rows.getString("physical_address");
                String emailAddress = rows.getString("email_address");
                Customer customer = new Customer(firstName, lastName, physicalAddress, emailAddress,
                        new Card(cardNumber, pinCode), new Account("Elite"));
                return customer;
            }

        } catch (SQLException e) {
            System.out.println("Couldn't retrieve customer: " + e.getMessage());
        }
        return null;
    }


    // SQLite fix from here
    public static void addNewCustomer(Customer customer) {
//        System.out.println("addNewCustomer()");
//        int cardNumber = customer.getCard().getNumber();
        int pinCode = customer.getCard().getPinCode();
        String firstName = customer.getFirstName();
        String lastName = customer.getLastName();
        String physicalAddress = customer.getPhysicalAddress();
        String emailAddress = customer.getEmailAddress();

//        String insert = "insert into customer (pin_code, first_name, last_name, physical_address, email_address) " +
//                "values (" + pinCode + ", \"" + firstName + "\", \"" + lastName + "\", \"" +
//                physicalAddress + "\", \"" + emailAddress + "\")";

        String insert = "insert into customers (pin_code, first_name, last_name, physical_address, email_address) " +
                "values (?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insert)) {
            preparedStatement.setInt(1, pinCode);
            preparedStatement.setString(2, firstName);
            preparedStatement.setString(3, lastName);
            preparedStatement.setString(4, physicalAddress);
            preparedStatement.setString(5, emailAddress);

            int rowsAffected = preparedStatement.executeUpdate();

            if(rowsAffected == 1) {
                System.out.println("Successfully added new customer");
            } else {
                System.out.println("Failed to add customer");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // add time column to the table to accurately track last transaction
    public static void addTransaction(int cardNumber, String type, double amount, double balance) {
//        System.out.println("addTransaction()");

//        String insert = "insert into transaction (card_id, date, type, amount, balance) " +
//                "values (" + cardNumber + ", \'" + date + "\', \"" + type + "\", " +
//                amount + ", " + balance + ")";

        String insert = "insert into transactions (card_id, date, type, amount, balance) " +
                "values (?, datetime('now', 'localtime'), ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insert)) {
            preparedStatement.setInt(1, cardNumber);
            preparedStatement.setString(2, type);
            preparedStatement.setDouble(3, amount);
            preparedStatement.setDouble(4, balance);

            int rowsAffected = preparedStatement.executeUpdate();

            if(rowsAffected == 1) {
                System.out.println("Successfully added new transaction");
            } else {
                System.out.println("Failed to add transaction");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Check if this method works correctly
    public static boolean removeCustomer(int cardNumber) {
//        System.out.println("removeCustomer()");
        String delete = "delete from customers where " +
                "card_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(delete)) {
            preparedStatement.setInt(1, cardNumber);

            int rowsAffected = preparedStatement.executeUpdate();

            if(rowsAffected == 1) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Couldn't remove customer: " + e.getMessage());
        }
        return false;
    }

    public static boolean removeCustomerTransactions(int cardNumber) {
//        System.out.println("removeCustomerTransactions()");
        String delete = "delete from transactions where " +
                "card_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(delete)) {
            preparedStatement.setInt(1, cardNumber);
            int rowsAffected = preparedStatement.executeUpdate();

            if(rowsAffected >= 1) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Couldn't delete customer transactions: " + e.getMessage());
        }
        return false;
    }

    public static boolean updateFirstName(int cardNumber, String firstName) {
//        System.out.println("updateFirstName()");

//        String update = "update customer set first_name = " + "\"" + firstName  + "\"" +
//                " where card_id = " + cardNumber;

        String update = "update customers set first_name = ? where card_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(update);) {
            preparedStatement.setString(1, firstName);
            preparedStatement.setInt(2, cardNumber);

            int rowsAffected = preparedStatement.executeUpdate();

            if(rowsAffected == 1) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Couldn't update first name: " + e.getMessage());
        }
        return false;
    }

    public static boolean updateLastName(int cardNumber, String lastName) {
//        System.out.println("updateLastName()");
        String update = "update customers set last_name = ? where card_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(update)) {
            preparedStatement.setString(1, lastName);
            preparedStatement.setInt(2, cardNumber);

            int rowsAffected = preparedStatement.executeUpdate();

            if(rowsAffected == 1) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Couldn't update last name: " + e.getMessage());
        }
        return false;
    }

    public static boolean updatePinCode(int cardNumber, int pinCode) {
//        System.out.println("updatePinCode()");
        String update = "update customers set pin_code = ? where card_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(update)) {
            preparedStatement.setInt(1, pinCode);
            preparedStatement.setInt(2, cardNumber);

            int rowsAffected = preparedStatement.executeUpdate();

            if(rowsAffected == 1) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Couldn't update pin code: " + e.getMessage());
        }
        return false;
    }

    public static boolean updatePhysicalAddress(int cardNumber, String physicalAddress) {
//        System.out.println("updatePhysicalAddress()");
        String update = "update customers set physical_address = ? where card_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(update)) {
            preparedStatement.setString(1, physicalAddress);
            preparedStatement.setInt(2, cardNumber);

            int rowsAffected = preparedStatement.executeUpdate();

            if(rowsAffected == 1) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Couldn't update physical address: " + e.getMessage());
        }
        return false;
    }

    public static boolean updateEmailAddress(int cardNumber, String emailAddress) {
//        System.out.println("updateEmailAddress()");
        String update = "update customers set email_address = ? where card_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(update)) {
            preparedStatement.setString(1, emailAddress);
            preparedStatement.setInt(2, cardNumber);

            int rowsAffected = preparedStatement.executeUpdate();

            if(rowsAffected == 1) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Couldn't update email address: " + e.getMessage());
        }
        return false;
    }

    public static void printAllCustomers() {
        String select = "select * from customers";
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet rows = statement.executeQuery(select);
            int count = 0;
            while(rows.next()) {
                int cardNumber = rows.getInt(1);
                int pinCode = rows.getInt(2);
                String firstName = rows.getString(3);
                String lastName = rows.getString(4);
                String physicalAddress = rows.getString(5);
                String emailAddress = rows.getString(6);

                System.out.println("Card " + cardNumber + " | " +
                        "Pin: " + pinCode + " | " +
                        firstName + " " + lastName + " | " +
                        physicalAddress + " | " +
                        emailAddress);
                count++;
            }
            if(count == 0) {
                System.out.println("No customers found");
            }

        } catch (SQLException e) {
            System.out.println("Couldn't print all customers: " + e.getMessage());
        }
    }

    public static void printCustomerTransactions(int cardNumber) {
        String select = "select * from transactions where card_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(select)) {
            preparedStatement.setInt(1, cardNumber);
            ResultSet rows = preparedStatement.executeQuery();

            while(rows.next()) {
//                int cardID = rows.getInt(1);
                Date date = Timestamp.valueOf(rows.getString(2)); // Timestamp extends Date
                String type = rows.getString(3);
                double amount = rows.getDouble(4);
                double balance = rows.getDouble(5);

                System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date) +
                        " | " + type  + " | " +
                        "Amount: " + numberFormat.format(amount) + " | " +
                        "Balance: " + numberFormat.format(balance) + " || ");
            }
        } catch (SQLException e) {
            System.out.println("Couldn't print customer transactions: " + e.getMessage());
        }
    }

    public static double getCustomerBalance(int cardNumber) {
//        System.out.println("getCustomerBalance()");
        String select = "select balance from transactions where card_id = ? " +
                "order by date desc limit 1";

        double balance = 0.0;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(select);) {
            preparedStatement.setInt(1, cardNumber);

            ResultSet rows = preparedStatement.executeQuery();

            if(rows.next()) {
                balance = rows.getDouble("balance");
            }
//            System.out.println("Balance returned = " + balance);
            return balance;
        } catch (SQLException e) {
            System.out.println("Couldn't retrieve customer's latest balance: " + e.getMessage());
        }
        System.out.println("Failed to retrieve balance");
        return balance;
    }

    public static void printAllTransactions() {
        String select = "select * from transactions";
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet rows = statement.executeQuery(select);
            int count = 0;

            while(rows.next()) {
                int cardNumber = rows.getInt(1);
                Date date = Timestamp.valueOf(rows.getString(2));
                String type = rows.getString(3);
                double amount = rows.getDouble(4);
                double balance = rows.getDouble(5);

                System.out.println("Card " + cardNumber + " | " +
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date) + " | " +
                        type  + " | " +
                        "Amount: " + numberFormat.format(amount) + " | " +
                        "Balance: " + numberFormat.format(balance) + " || ");
                count++;
            }
            if(count == 0) {
                System.out.println("No transactions found");
            }
        } catch (SQLException e) {
            System.out.println("Couldn't print all transactions: " + e.getMessage());
        }
    }

    // TODO method to reset card number to 1 when customers and transactions tables are cleared in the database
    // should have the following select statement
    // ALTER TABLE table_name AUTO_INCREMENT = 1;

}

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Calendar;

class User{
    private  String username;
    private  String password;
    private List<Account> accounts = new ArrayList<>();

    public User(String username,String password) {
        this.username=username;
        this.password=password;
    }
    public  boolean verify(String username,String password){
        if(this.username.equals(username) && this.password.equals(password)){
            return true;
        }
        return false;
    }

    public Account openAccount(String accountType, double initialDeposit) {
        Account account = new Account(accountType, initialDeposit);
        accounts.add(account);
        return account;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public Account getAccountByNumber(int accountNumber) {
        for (Account account : accounts) {
            if (account.getAccountNumber() == accountNumber) {
                return account;
            }
        }
        return null;
    }

}

class Account{
    static int accCounter=1000;
    int accountNumber;
    String accountType;
    double balance;
    List<String> transactionHistory=new ArrayList<>();

    private Date lastInterestDate = null;  // Tracks last date of interest calculation
    private static final double INTEREST_RATE = 0.01;  // 1% monthly interest

    public Account(String accountType,double initialDeposit){
        this.accountType=accountType;
        this.balance=initialDeposit;
        this.accountNumber=accCounter++;
        transactionHistory.add(new Date()+": Account opened with initial deposit of "+initialDeposit);
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
        transactionHistory.add(new Date() + ": Deposit of " + amount);
    }

    public boolean withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
            transactionHistory.add(new Date() + ": Withdrawal of " + amount);
            return true;
        }
        return false;
    }

    public void viewStatement() {
        System.out.println("Transaction History:");
        for (String transaction : transactionHistory) {
            System.out.println(transaction);
        }
    }

    public void calculateInterest(){
        if ("savings".equalsIgnoreCase(accountType)) {
            Calendar current = Calendar.getInstance();
            if (lastInterestDate != null) {
                Calendar lastInterest = Calendar.getInstance();
                lastInterest.setTime(lastInterestDate);

                if (current.get(Calendar.YEAR) == lastInterest.get(Calendar.YEAR) &&
                        current.get(Calendar.MONTH) == lastInterest.get(Calendar.MONTH)) {
                    System.out.println("Interest already added this month.");
                    return;
                }
            }
            double interest = balance * INTEREST_RATE;
            balance += interest;
            transactionHistory.add(new Date() + ": Interest added of " + interest);
            lastInterestDate = new Date();  // Update last interest date
            System.out.println("Interest of " + interest + " added.");

        }else {
            System.out.println("Interest is only applicable for savings account");
        }
    }
}

public class BankingApplication {
    public static Scanner sc = new Scanner(System.in);
    static List<User> users = new ArrayList<User>();
    static User currentUser = null;

    public static void main(String[] args) {
        System.out.println("----------Welcome to SBI Bank-------");
        while (true) {
            if (currentUser == null) {

                System.out.println("Press 1 for new Registration");
                System.out.println("Press 2 to Login to your Account");
                System.out.println("Press 3 to exit");

                int n = sc.nextInt();
                sc.nextLine();

                switch (n) {
                    case 1:
                        registration();
                        break;
                    case 2:
                        login();
                        break;
                    case 3:
                        return;
                    default:
                        System.out.println("Enter valid numbers");
                }
            } else {
                showMenu();
            }
        }

    }
    public static void registration() {
        System.out.print("enter username ");
        String username = sc.nextLine();
        System.out.print("Enter Password");
        String password = sc.nextLine();

        User userobj = new User(username, password);
        users.add(userobj);
        System.out.println("----------Registered Successfully---------");
    }

    public static void login() {
        System.out.print("enter username ");
        String username = sc.nextLine();
        System.out.print("Enter Password");
        String password = sc.nextLine();

        for (User u : users) {
            if (u.verify(username, password)) {
                System.out.println("Logged in sucessfully");
                currentUser = u;
                System.out.println("");
                return;
            }

        }
        System.out.println("Invalid Credentials");
    }
    public static void showMenu() {
        System.out.println("\nPress 1 to open a new account");
        System.out.println("Press 2 to select an account");
        System.out.println("Press 3 to logout");
        int choice = sc.nextInt();
        sc.nextLine();

        switch (choice) {
            case 1:
                openAccount();
                break;
            case 2:
                selectAccount();
                break;
            case 3:
                currentUser = null;
                System.out.println("Logged out successfully.");
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    public static void openAccount() {
        System.out.print("Enter account type(savings/checking): ");
        String accountType = sc.nextLine();
        System.out.print("Enter initial Deposit");
        Double initialDeposit = sc.nextDouble();
        sc.nextLine();

        Account account = currentUser.openAccount(accountType, initialDeposit);
        System.out.println("Account Created Successfully with the account Number " + account.getAccountNumber());
    }

    public static void selectAccount() {
        List<Account> accounts = currentUser.getAccounts();
        if (accounts.isEmpty()) {
            System.out.println("No accounts available for Current User");
            return;
        }
        System.out.println("Your Accounts are: ");
        for (Account a : accounts) {
            System.out.println("Account number: " + a.getAccountNumber());
        }
        System.out.print("\n Select Account by Account number ");

        int accountNumber = sc.nextInt();
        sc.nextLine();
        Account selectedAccount = currentUser.getAccountByNumber(accountNumber);

        if (selectedAccount != null) {
            operations(selectedAccount);
        } else {
            System.out.println("Invalid account Number");
            return;
        }
    }

    static void operations(Account account) {

        while (true) {
            System.out.println("\nenter 1 to Deposit");
            System.out.println("enter 2 to Withdraw");
            System.out.println("enter 3 to Check Balance");
            System.out.println("enter 4 to View Statement");
            System.out.println("enter 5 to Calculate Interest (Savings Only)");
            System.out.println("enter 6 to go Back");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter deposit amount: ");
                    double depositAmount = sc.nextDouble();
                    sc.nextLine();
                    account.deposit(depositAmount);
                    System.out.println("Deposit successful.");
                    break;
                case 2:
                    System.out.print("Enter withdrawal amount: ");
                    double withdrawalAmount = sc.nextDouble();
                    sc.nextLine();
                    if (account.withdraw(withdrawalAmount)) {
                        System.out.println("Withdrawal successful.");
                    } else {
                        System.out.println("Insufficient funds.");
                    }
                    break;
                case 3:
                    System.out.println("Current Balance: " + account.getBalance());
                    break;
                case 4:
                    account.viewStatement();
                    break;
                case 5:
                    account.calculateInterest();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}

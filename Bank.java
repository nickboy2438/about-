import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// ==========================================
// 1. TRANSACTION LOG MODEL
// ==========================================
class Transaction {
    private final String type;
    private final double amount;
    private final double postBalance;
    private final LocalDateTime timestamp;

    public Transaction(String type, double amount, double postBalance) {
        this.type = type;
        this.amount = amount;
        this.postBalance = postBalance;
        this.timestamp = LocalDateTime.now();
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format("[%s] %-10s | Amount: ₹%,.2f | Balance: ₹%,.2f", 
                timestamp.format(formatter), type, amount, postBalance);
    }
}

// ==========================================
// 2. BANK ACCOUNT ENTITY (Business Logic)
// ==========================================
class BankAccount {
    private final String accountNumber;
    private final String accountHolder;
    private double balance;
    private final List<Transaction> transactionHistory;

    public BankAccount(String accountNumber, String accountHolder, double initialDeposit) {
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.balance = initialDeposit;
        this.transactionHistory = new ArrayList<>();
        
        // Record initial deposit setup
        recordTransaction("INITIAL", initialDeposit);
    }

    // Deposit Method
    public void deposit(double amount) {
        if (amount <= 0) {
            System.out.println("❌ Error: Deposit amount must be greater than zero.");
            return;
        }
        balance += amount;
        recordTransaction("DEPOSIT", amount);
        System.out.printf("✅ Successfully deposited ₹%,.2f. New Balance: ₹%,.2f\n", amount, balance);
    }

    // Withdrawal Method
    public void withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("❌ Error: Withdrawal amount must be greater than zero.");
            return;
        }
        if (amount > balance) {
            System.out.printf("❌ Error: Insufficient funds! Current Balance: ₹%,.2f\n", balance);
            return;
        }
        balance -= amount;
        recordTransaction("WITHDRAW", amount);
        System.out.printf("✅ Successfully withdrew ₹%,.2f. New Balance: ₹%,.2f\n", amount, balance);
    }

    // Balance Checker
    public double getBalance() {
        return balance;
    }

    // Display Statement
    public void printStatement() {
        System.out.println("\n==================================================");
        System.out.println("               TRANSACTION HISTORY                ");
        System.out.println("==================================================");
        System.out.println("Account Holder: " + accountHolder);
        System.out.println("Account Number: " + accountNumber);
        System.out.println("--------------------------------------------------");
        
        if (transactionHistory.isEmpty()) {
            System.out.println("No transactions recorded yet.");
        } else {
            for (Transaction t : transactionHistory) {
                System.out.println(t);
            }
        }
        System.out.println("--------------------------------------------------");
        System.out.printf("Current Dynamic Balance: ₹%,.2f\n", balance);
        System.out.println("==================================================");
    }

    private void recordTransaction(String type, double amount) {
        transactionHistory.add(new Transaction(type, amount, this.balance));
    }

    public String getAccountHolder() { return accountHolder; }
    public String getAccountNumber() { return accountNumber; }
}

// ==========================================
// 3. MAIN RUNTIME INTERFACE (Console CLI)
// ==========================================
class BankingSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== Welcome to the Java Apex Bank ===");
        System.out.print("Enter Account Holder Name: ");
        String name = scanner.nextLine();
        
        System.out.print("Create Account Number: ");
        String accNum = scanner.nextLine();
        
        System.out.print("Enter Initial Deposit Amount (₹): ");
        double initialDeposit = scanner.nextDouble();
        
        // Creating the bank account object instance
        BankAccount account = new BankAccount(accNum, name, initialDeposit);
        System.out.println("\n🎉 Account successfully created for " + account.getAccountHolder() + "!");

        int choice;
        do {
            System.out.println("\n--- BANKING MENU ---");
            System.out.println("1. Deposit Money");
            System.out.println("2. Withdraw Money");
            System.out.println("3. Check Balance");
            System.out.println("4. View Transaction History");
            System.out.println("5. Exit Application");
            System.out.print("Select an option (1-5): ");
            
            choice = scanner.nextInt();
            
            switch (choice) {
                case 1:
                    System.out.print("Enter deposit amount (₹): ");
                    double depAmt = scanner.nextDouble();
                    account.deposit(depAmt);
                    break;
                case 2:
                    System.out.print("Enter withdrawal amount (₹): ");
                    double witAmt = scanner.nextDouble();
                    account.withdraw(witAmt);
                    break;
                case 3:
                    System.out.printf("💰 Current Available Balance: ₹%,.2f\n", account.getBalance());
                    break;
                case 4:
                    account.printStatement();
                    break;
                case 5:
                    System.out.println("Thank you for banking with Java Apex Bank. Goodbye!");
                    break;
                default:
                    System.out.println("❌ Invalid selection! Please enter a number between 1 and 5.");
            }
        } while (choice != 5);

        scanner.close();
    }
}

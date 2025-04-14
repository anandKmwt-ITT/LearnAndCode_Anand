package question2;

public class Customer {
    private String firstName;
    private String lastName;
    private Wallet myWallet;

    public Customer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.myWallet = new Wallet();
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public float getWalletBalance() {
        return myWallet.getTotalMoney();
    }

    public boolean hasEnoughMoney(float amount) {
        return myWallet.getTotalMoney() >= amount;
    }

    public boolean pay(float amount) {
        if (hasEnoughMoney(amount)) {
            myWallet.subtractMoney(amount);
            return true;
        }
        return false;
    }

    public void addMoney(float amount) {
        myWallet.addMoney(amount);
    }
}
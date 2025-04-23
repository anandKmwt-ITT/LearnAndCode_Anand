package question2;

public class Wallet {
    private float amount;

    public Wallet(float initialAmount) {
        this.amount = initialAmount;
    }

    public float getTotalMoney() {
        return amount;
    }

    public void setTotalMoney(float amount) {
        this.amount = amount;
    }

    public void addMoney(float amount) {
        this.amount += amount;
    }

    public void subtractMoney(float amount) {
        this.amount -= amount ;
    }
}
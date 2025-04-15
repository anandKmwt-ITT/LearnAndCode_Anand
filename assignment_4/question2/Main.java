package question2;

public class Main {
    public static void main(String[] args) {
        Customer anand = new Customer("Anand", "Kumawat", 5.00f);
        float payment = 2.00f;

        System.out.println("Delivery boy says: I want my two dollars!");
        boolean paid = anand.pay(payment);

        if (paid) {
            System.out.println("Payment successful!");
        } else {
            System.out.println("Not enough balance.");
        }
    }
}

import java.util.Random;
import java.util.Scanner;

public class NumberGuesser {

    private static boolean isValidGuess(int number) {
        return number >= 1 && number <= 100;
    }

    private static void play(){
        Random random = new Random();
        Scanner scanner = new Scanner(System.in);
        int targetNumber = random.nextInt(100) + 1;

        boolean isGuessedCorrectly = false;
        int numberOfGuesses = 0;

        System.out.print("Please guess a number between 1 and 100: ");
        int guessedNumber = scanner.nextInt();

        while (!isGuessedCorrectly) {
            if (!isValidGuess(guessedNumber)) {
                System.out.print("Invalid input. Please enter a number between 1 and 100: ");
                guessedNumber = scanner.nextInt();
                continue;
            }

            numberOfGuesses++;

            if (guessedNumber < targetNumber) {
                System.out.print("Guessed number is too low. Guess again: ");
            } else if (guessedNumber > targetNumber) {
                System.out.print("Guessed number is too high. Guess again: ");
            } else {
                System.out.println("You guessed the correct number in " + numberOfGuesses + " attempts!");
                System.out.println("So the actual number was: "+ targetNumber);
                isGuessedCorrectly = true;
            }

            if (!isGuessedCorrectly) {
                guessedNumber = scanner.nextInt();
            }
        }
    }

    public static void main(String[] args) {
        play();
    }
}

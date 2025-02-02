import java.util.Scanner;

public class MeanCalculator {

    private static long[] readArray(Scanner sc, int size) {
        long[] elements = new long[size];
        for (int index = 0; index < size; index++) {
            elements[index] = sc.nextLong();
        }
        return elements;
    }

    private static long[] computePrefixSums(long[] elements, int size) {
        long[] prefixSums = new long[size + 1];
        prefixSums[0] = 0;
        for (int index = 1; index <= size; index++) {
            prefixSums[index] = prefixSums[index - 1] + elements[index - 1];
        }
        return prefixSums;
    }

    private static void handleQueries(Scanner sc, int numQueries, long[] prefixSums) {
        for (int query = 0; query < numQueries; query++) {
            int startIndex = sc.nextInt();
            int endIndex = sc.nextInt();

            long subarraySum = prefixSums[endIndex] - prefixSums[startIndex - 1];
            int subarrayLength = endIndex - startIndex + 1;

            long mean = subarraySum / subarrayLength;
            System.out.println(mean);
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int numberOfElements = sc.nextInt();
        int numberOfQueries = sc.nextInt();

        long[] elements = readArray(sc, numberOfElements);

        long[] prefixSums = computePrefixSums(elements, numberOfElements);

        handleQueries(sc, numberOfQueries, prefixSums);
    }
}

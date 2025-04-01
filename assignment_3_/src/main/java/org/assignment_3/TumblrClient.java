package org.assignment_3;

import java.io.IOException;
import java.util.Scanner;

public class TumblrClient {
    private final TumblrService tumblrService;
    private final TumblrParser tumblrParser;

    public TumblrClient(TumblrService tumblrService, TumblrParser tumblrParser) {
        this.tumblrService = tumblrService;
        this.tumblrParser = tumblrParser;
    }

    public void startApplication() {
        try {
            String[] userInputs = processUserInput();
            String jsonResponse = tumblrService.fetchBlogData(userInputs[0], Integer.parseInt(userInputs[1]), Integer.parseInt(userInputs[2]));
            tumblrParser.displayBlogInfo(jsonResponse, Integer.parseInt(userInputs[1]));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private String[] processUserInput() throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Tumblr blog name: ");
        String blogName = scanner.nextLine().trim();

        System.out.print("Enter post range (start-end): ");
        String rangeInput = scanner.nextLine().trim();

        String[] range = rangeInput.split("-");
        if (range.length != 2) {
            throw new IllegalArgumentException("Invalid input! Please enter the range in 'start-end' format.");
        }

        int startingBlog = Integer.parseInt(range[0].trim());
        int numberOfBlogs = Integer.parseInt(range[1].trim()) - startingBlog + 1;
        if (startingBlog < 1 || numberOfBlogs < 1) {
            throw new IllegalArgumentException("Invalid range values! Ensure 'start' is positive and 'end' is greater than 'start'.");
        }

        return new String[]{blogName, String.valueOf(startingBlog), String.valueOf(numberOfBlogs)};
    }

    public static void main(String[] args) {
        TumblrService tumblrService = new TumblrService();
        TumblrParser tumblrParser = new TumblrParser();
        TumblrClient client = new TumblrClient(tumblrService, tumblrParser);
        client.startApplication();
    }
}
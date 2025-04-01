package org.assignment_3;

import org.json.*;

public class TumblrParser {
    public void displayBlogInfo(String jsonResponse, int start) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONObject blog = jsonObject.getJSONObject("tumblelog");

        System.out.println("\nTitle: " + blog.optString("title", "N/A"));
        System.out.println("Name: " + blog.optString("name", "N/A"));
        System.out.println("Description: " + blog.optString("description", "N/A"));
        System.out.println("Total Posts: " + jsonObject.optInt("posts-total", 0));
        System.out.println("\nImages:");

        JSONArray posts = jsonObject.getJSONArray("posts");
        for (int count = 0; count < posts.length(); count++) {
            JSONObject post = posts.getJSONObject(count);
            System.out.println((start + count) + ". " + post.optString("photo-url-1280", "No Image Available"));
        }
    }
}

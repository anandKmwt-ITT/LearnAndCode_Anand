package Q4;

// No this class was not following SRP earlier as it had many reasons to change this class.

public class Book {

    public String getTitle() {
        return "Do Bailon ki Katha";
    }

    public String getAuthor() {
        return "Munshi Premchand";
    }

    public void turnPage() {
        // pointer to next page
    }

    public int getCurrentPage() {
        return pageNumber;
    }

}

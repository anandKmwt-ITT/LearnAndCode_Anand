package Q4;

public class BookManager {

    public String getLocation(String serialNumber) {
        // returns the position in the library (e.g., shelf number & room number)
    }

    public void save(Book book) {
        filename = '/documents/' . book->getTitle() . ' - ' . book->getAuthor();
        file_put_contents(filename, serialize(book));
    }

}

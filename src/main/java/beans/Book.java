package beans;

public class Book {

	private String book_name;
	private String isbn;
	private int aisle;
	private String author;
	
	public Book() {
        super();
    }
	
	public String getBook_name() {
		return book_name;
	}

	public void setBook_name(String book_name) {
		this.book_name = book_name;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public int getAisle() {
		return aisle;
	}

	public void setAisle(int aisle) {
		this.aisle = aisle;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
	public Book(String book_name, String isbn, String author, int aisle) {
		this.book_name = book_name;
		this.isbn = isbn;
		this.aisle = aisle;
		this.author = author;

	}

}

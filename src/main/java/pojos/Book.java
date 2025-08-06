package pojos;

/** Represents a Book entity with id, title, and author. */
public class Book {
  private int id;
  private String title;
  private String author;

  /** Default constructor for Book. */
  public Book() {}

  /**
   * Constructs a Book with the specified title and author.
   *
   * @param title the title of the book
   * @param author the author of the book
   */
  public Book(String title, String author) {
    this.title = title;
    this.author = author;
  }

  /**
   * Constructs a Book with the specified id, title, and author.
   *
   * @param id the id of the book
   * @param title the title of the book
   * @param author the author of the book
   */
  public Book(int id, String title, String author) {
    this.id = id;
    this.title = title;
    this.author = author;
  }

  /**
   * Gets the id of the book.
   *
   * @return the id
   */
  public int getId() {
    return id;
  }

  /**
   * Sets the id of the book.
   *
   * @param id the id to set
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Gets the title of the book.
   *
   * @return the title
   */
  public String getTitle() {
    return title;
  }

  /**
   * Sets the title of the book.
   *
   * @param title the title to set
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Gets the author of the book.
   *
   * @return the author
   */
  public String getAuthor() {
    return author;
  }

  /**
   * Sets the author of the book.
   *
   * @param author the author to set
   */
  public void setAuthor(String author) {
    this.author = author;
  }

  /**
   * Returns a string representation of the Book.
   *
   * @return string representation
   */
  @Override
  public String toString() {
    return "Book [id=" + id + ", title=" + title + ", author=" + author + "]";
  }
}

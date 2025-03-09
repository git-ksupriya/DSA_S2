public class LibrarySystem {
    public static void main(String[] args) {
        LinkedList booksList = new LinkedList();
        LinkedList usersList = new LinkedList();
        LinkedList transactionsList = new LinkedList();

        Librarian librarian = new Librarian(1, "Mr. Smith");
        usersList.insertEnd(librarian);

        Book book1 = new HardCopy("1984", "George Orwell", "12345", 328);
        Book book2 = new EBook("Digital Fortress", "Dan Brown", "67890", "5MB");

        System.out.println(librarian.addBook(booksList, book1));
        System.out.println(librarian.addBook(booksList, book2));

        booksList.displayList();

        Student student = new Student(101, "Alice", 3);
        usersList.insertEnd(student);

        System.out.println(student.borrowBook(book1, transactionsList));
        System.out.println(student.borrowBook(book1, transactionsList)); // Should show "Sorry, 1984 is already borrowed."

        System.out.println(student.returnBook(book1, transactionsList));
        System.out.println(student.returnBook(book1, transactionsList)); // Should show "Alice does not have 1984."

        System.out.println(librarian.removeBook(booksList, "1984"));

        transactionsList.displayList();
    }
}
class Node {
    Object data; // Can store Book, User, or Transaction
    Node next; // Pointer to the next node

    public Node(Object data) {
        this.data = data;
        this.next = null;
    }
}

// Base class for Users (Inheritance)
class Person {
    int userId;
    String name;

    public Person(int userId, String name) {
        this.userId = userId;
        this.name = name;
    }
}

// Student class inheriting from Person
class Student extends Person {
    int maxBooks;
    LinkedList borrowedBooks;

    public Student(int userId, String name, int maxBooks) {
        super(userId, name);
        this.maxBooks = maxBooks;
        this.borrowedBooks = new LinkedList();
    }

    public String borrowBook(Book book, LinkedList transactionsList) {
        if (!book.isAvailable) {
            return "Sorry, " + book.title + " is already borrowed.";
        }
        if (borrowedBooks.size() < maxBooks) {
            borrowedBooks.insertEnd(book);
            book.isAvailable = false;
            transactionsList.insertEnd(name + " borrowed " + book.title);
            return name + " borrowed " + book.title;
        }
        return name + " cannot borrow more than " + maxBooks + " books.";
    }

    public String returnBook(Book book, LinkedList transactionsList) {
        if (borrowedBooks.delete(book.title)) {
            book.isAvailable = true;
            transactionsList.insertEnd(name + " returned " + book.title);
            return name + " returned " + book.title;
        }
        return name + " does not have " + book.title;
    }
}

// Librarian class inheriting from Person
class Librarian extends Person {
    public Librarian(int userId, String name) {
        super(userId, name);
    }

    public String addBook(LinkedList bookList, Book book) {
        bookList.insertEnd(book);
        return book.title + " added to the library.";
    }

    public String removeBook(LinkedList bookList, String bookTitle) {
        return bookList.delete(bookTitle) ? "Book '" + bookTitle + "' removed successfully." : "Book '" + bookTitle + "' not found.";
    }
}

// Book Base Class
class Book {
    String title, author, isbn;
    boolean isAvailable;

    public Book(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.isAvailable = true;
    }

    public String bookType() {
        return "General Book";
    }
}

// HardCopy class (Extends Book)
class HardCopy extends Book {
    int pages;

    public HardCopy(String title, String author, String isbn, int pages) {
        super(title, author, isbn);
        this.pages = pages;
    }

    @Override
    public String bookType() {
        return "Hard Copy";
    }
}

// EBook class (Extends Book)
class EBook extends Book {
    String fileSize;

    public EBook(String title, String author, String isbn, String fileSize) {
        super(title, author, isbn);
        this.fileSize = fileSize;
    }

    @Override
    public String bookType() {
        return "E-Book";
    }
}

// Linked List Class
class LinkedList {
    private Node head;

    public void insertEnd(Object data) {
        Node newNode = new Node(data);
        if (head == null) {
            head = newNode;
            return;
        }
        Node temp = head;
        while (temp.next != null) {
            temp = temp.next;
        }
        temp.next = newNode;
    }

    public boolean delete(String title) {
        Node temp = head, prev = null;
        while (temp != null) {
            if (temp.data instanceof Book && ((Book) temp.data).title.equals(title)) {
                if (prev != null) {
                    prev.next = temp.next;
                } else {
                    head = temp.next;
                }
                return true;
            }
            prev = temp;
            temp = temp.next;
        }
        return false;
    }

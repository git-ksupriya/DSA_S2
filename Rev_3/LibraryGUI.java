import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// Simple Book, HardCopy, EBook, Person, Student, Librarian, LinkedList, Node definitions assumed

public class LibraryGUI extends JFrame {
    LinkedList booksList = new LinkedList();
    LinkedList transactionsList = new LinkedList();
    Person currentUser;

    public LibraryGUI(Person user) {
        this.currentUser = user;

        setTitle("Library Management System - Welcome " + user.name);
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        if (user instanceof Librarian) {
            tabbedPane.addTab("Add Book", createAddBookPanel());
        }

        tabbedPane.addTab("Borrow/Return", createBorrowReturnPanel());
        tabbedPane.addTab("Books List", createViewBooksPanel());
        tabbedPane.addTab("Transactions List", createViewTransactionsPanel());

        add(tabbedPane);
        setVisible(true);
    }

    private JPanel createAddBookPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));
        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField isbnField = new JTextField();
        JTextField extraField = new JTextField();
        JComboBox<String> typeBox = new JComboBox<>(new String[]{"HardCopy 📚", "EBook"});
        JButton addButton = new JButton("Add Book");

        panel.add(new JLabel("Title : *"));
        panel.add(titleField);
        panel.add(new JLabel("Author :"));
        panel.add(authorField);
        panel.add(new JLabel("ISBN : *"));
        panel.add(isbnField);
        panel.add(new JLabel("Pages (if HardCopy) or Size (if EBook):"));
        panel.add(extraField);
        panel.add(new JLabel("Type :"));
        panel.add(typeBox);
        panel.add(new JLabel(" "));
        panel.add(addButton);

        addButton.addActionListener(e -> {
            String title = titleField.getText();
            String author = authorField.getText();
            String isbn = isbnField.getText();
            String extra = extraField.getText();
            String type = (String) typeBox.getSelectedItem();

            if (title.isEmpty() || isbn.isEmpty() || extra.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all required fields (*)", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Book book;
            try {
                if (type.equals("HardCopy")) {
                    int pages = Integer.parseInt(extra);
                    book = new HardCopy(title, author, isbn, pages);
                } else {
                    book = new EBook(title, author, isbn, extra);
                }
                booksList.insertEnd(book);
                JOptionPane.showMessageDialog(this, title + " added to library!");
                titleField.setText("");
                authorField.setText("");
                isbnField.setText("");
                extraField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid number for pages!", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    private JPanel createBorrowReturnPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));

        JTextField bookTitleField = new JTextField();
        JTextField studentNameField = new JTextField();

        JButton borrowButton = new JButton("Borrow Book");
        JButton returnButton = new JButton("Return Book");

        panel.add(new JLabel("Book Title:"));
        panel.add(bookTitleField);
        panel.add(new JLabel("Student Name:"));
        panel.add(studentNameField);
        panel.add(borrowButton);
        panel.add(returnButton);

        borrowButton.addActionListener(e -> {
            String title = bookTitleField.getText().trim();
            String studentName = studentNameField.getText().trim();

            if (title.isEmpty() || studentName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter both book title and student name.");
                return;
            }

            Book book = findBookByTitle(title);
            if (book == null) {
                JOptionPane.showMessageDialog(this, "Book not found.");
                return;
            }

            if (!book.isAvailable) {
                JOptionPane.showMessageDialog(this, "Book is already borrowed.");
                return;
            }

            book.isAvailable = false;
            transactionsList.insertEnd(new Transaction(studentName, book.title, "Borrowed"));
            JOptionPane.showMessageDialog(this, studentName + " borrowed \"" + book.title + "\".");
        });

        returnButton.addActionListener(e -> {
            String title = bookTitleField.getText().trim();
            String studentName = studentNameField.getText().trim();

            if (title.isEmpty() || studentName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter both book title and student name.");
                return;
            }

            Book book = findBookByTitle(title);
            if (book == null) {
                JOptionPane.showMessageDialog(this, "Book not found.");
                return;
            }

            if (book.isAvailable) {
                JOptionPane.showMessageDialog(this, "Book is already in library.");
                return;
            }

            book.isAvailable = true;
            transactionsList.insertEnd(new Transaction(studentName, book.title, "Returned"));
            JOptionPane.showMessageDialog(this, studentName + " returned \"" + book.title + "\".");
        });

        return panel;
    }

    private JPanel createViewBooksPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea area = new JTextArea();
        JButton refresh = new JButton("Refresh ↻");

        panel.add(new JScrollPane(area), BorderLayout.CENTER);
        panel.add(refresh, BorderLayout.SOUTH);

        refresh.addActionListener(e -> {
            area.setText("");
            Node current = booksList.getHead();
            while (current != null) {
                Book book = (Book) current.data;
                area.append(book.title + " - " + book.bookType() + " - " + (book.isAvailable ? "Available" : "Borrowed") + "\n");
                current = current.next;
            }
        });

        return panel;
    }

    private JPanel createViewTransactionsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea area = new JTextArea();
        JButton refresh = new JButton("Refresh ↻");

        panel.add(new JScrollPane(area), BorderLayout.CENTER);
        panel.add(refresh, BorderLayout.SOUTH);

        refresh.addActionListener(e -> {
            area.setText("");
            Node current = transactionsList.getHead();
            while (current != null) {
                area.append(current.data.toString() + "\n");
                current = current.next;
            }
        });

        return panel;
    }

    private Book findBookByTitle(String title) {
        Node current = booksList.getHead();
        while (current != null) {
            Book book = (Book) current.data;
            if (book.title.equalsIgnoreCase(title)) {
                return book;
            }
            current = current.next;
        }
        return null;
    }

    public static void main(String[] args) {
        // You can update constructor args as per your Student/Librarian constructor
        Person user = new Student(1, "Dear Student", 101); // Replace with your actual constructor
        new LibraryGUI(user);
    }
}

// Minimal Transaction class
class Transaction {
    String studentName;
    String bookTitle;
    String action;

    public Transaction(String studentName, String bookTitle, String action) {
        this.studentName = studentName;
        this.bookTitle = bookTitle;
        this.action = action;
    }

    @Override
    public String toString() {
        return studentName + " " + action + " \"" + bookTitle + "\"";
    }
}


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginWindow extends JFrame {

    public LoginWindow() {
        setTitle("Library Login");
        setSize(350, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 2, 10, 10));

        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField();

        JLabel roleLabel = new JLabel("Role:");
        JComboBox<String> roleBox = new JComboBox<>(new String[]{"Librarian", "Student"});

        JButton loginButton = new JButton("Login");

        add(userLabel);
        add(userField);
        add(roleLabel);
        add(roleBox);
        add(new JLabel()); // Empty cell
        add(loginButton);

        loginButton.addActionListener(e -> {
            String username = userField.getText().trim().toLowerCase();
            String role = (String) roleBox.getSelectedItem();

            if (role.equals("Librarian") && username.equals("nitha")) {
                Librarian librarian = new Librarian(1, "Ms. Nitha Mam");
                new LibraryGUI(librarian);
                dispose(); // Close login window
            } else if (role.equals("Student") && username.equals("meghana")) {
                Student student = new Student(101, "Meghana", 3);
                new LibraryGUI(student);
                dispose();
            } else if (role.equals("Student") && username.equals("supriya")) {
                Student student = new Student(102, "Supriya", 3);
                new LibraryGUI(student);
                dispose();
            } else if (role.equals("Student") && username.equals("siva kumar")) {
                Student student = new Student(103, "Siva Kumar", 3);
                new LibraryGUI(student);
                dispose();
            } else if (role.equals("Student") && username.equals("mahendra")) {
                Student student = new Student(104, "Mahendra", 3);
                new LibraryGUI(student);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new LoginWindow();
    }
}

class Node:
    def __init__(self, data):
        self.data = data
        self.next = None

# Base class for Users (Inheritance)
class Person:
    def __init__(self, user_id, name):
        self.user_id = user_id
        self.name = name

# Student class inheriting from Person
class Student(Person):
    def __init__(self, user_id, name, max_books=3):
        super().__init__(user_id, name)
        self.max_books = max_books
        self.borrowed_books = []
    
    def borrow_book(self, book, transactions_list):
        if not book.is_available:
            return f"Sorry, {book.title} is already borrowed."
        if len(self.borrowed_books) < self.max_books:
            self.borrowed_books.append(book)
            book.is_available = False
            transactions_list.insert_end(f"{self.name} borrowed {book.title}")
            return f"{self.name} borrowed {book.title}"
        return f"{self.name} cannot borrow more than {self.max_books} books."

    def return_book(self, book, transactions_list):
        if book in self.borrowed_books:
            self.borrowed_books.remove(book)
            book.is_available = True
            transactions_list.insert_end(f"{self.name} returned {book.title}")
            return f"{self.name} returned {book.title}"
        return f"{self.name} does not have {book.title}."

# Librarian class inheriting from Person
class Librarian(Person):
    def __init__(self, user_id, name):
        super().__init__(user_id, name)
    
    def add_book(self, book_list, book):
        book_list.insert_end(book)
        return f"{book.title} added to the library."
    
    def remove_book(self, book_list, book_title):
        return book_list.delete(book_title)

# Book Class
class Book:
    def __init__(self, title, author, isbn):
        self.title = title
        self.author = author
        self.isbn = isbn
        self.is_available = True

    def book_type(self):
        return "General Book"

# HardCopy Class
class HardCopy(Book):
    def __init__(self, title, author, isbn, pages):
        super().__init__(title, author, isbn)
        self.pages = pages

    def book_type(self):
        return "Hard Copy"

# EBook Class
class EBook(Book):
    def __init__(self, title, author, isbn, file_size):
        super().__init__(title, author, isbn)
        self.file_size = file_size

    def book_type(self):
        return "E-Book"

# Linked List Class
class LinkedList:
    def __init__(self):
        self.head = None

    def insert_end(self, data):
        new_node = Node(data)
        if not self.head:
            self.head = new_node
        else:
            temp = self.head
            while temp.next:
                temp = temp.next
            temp.next = new_node

    def delete(self, title):
        temp = self.head
        prev = None

        while temp:
            if isinstance(temp.data, Book) and temp.data.title == title:
                if prev:
                    prev.next = temp.next  #here temp is holding the the reference to the next node after 'to be deleted node' 
                else:
                    self.head = temp.next #when we want to delete the first node (if temp.data is the self.data of the first node), we are making sure we assign .head (the pointer) is updated to the next node  
                return f"Book '{title}' removed successfully."
            prev = temp
            temp = temp.next
        return f"Book '{title}' not found."
    
    def display(self):
        if not self.head:
            print("No records found.")
        else:
            temp = self.head
            while temp:
                if isinstance(temp.data, Book):
                    print(f"{temp.data.title} by {temp.data.author} ({temp.data.book_type()}) - {'Available' if temp.data.is_available else 'Borrowed'}")
                else:
                    print(temp.data)
                temp = temp.next

# Create Three Linked Lists
books_list = LinkedList()
users_list = LinkedList()
transactions_list = LinkedList()

librarian = Librarian(1, "Mr. Smith")

users_list.insert_end(librarian)

# Creating Books
book1 = HardCopy("1984", "George Orwell", "12345", 328)
book2 = EBook("Digital Fortress", "Dan Brown", "67890", "5MB")

# Adding Books
print(librarian.add_book(books_list, book1))
print(librarian.add_book(books_list, book2))

# Display Books
books_list.display()

# Student Borrowing a Book
student = Student(101, "Alice")
users_list.insert_end(student)
print(student.borrow_book(book1, transactions_list))
print(student.borrow_book(book1, transactions_list))

# Student Returning a Book
print(student.return_book(book1, transactions_list))
print(student.return_book(book1, transactions_list))

# Remove Book
print(librarian.remove_book(books_list, "1984"))

# Display Books Again
books_list.display()

# Display Transactions
transactions_list.display()

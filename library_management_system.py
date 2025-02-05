#initializing a node class in Linked list
class Node:
    def __init__(self, data):
        self.data = data 
        self.next = None  # Pointer to next node
# Book Base Class
class Book:
    def __init__(self, title, author, book_id):
        self.title = title
        self.author = author
        self.book_id = book_id
        self.is_available = True

    def book_type(self):
        return "General Book"


# HardCopy class (Extends Book)
class HardCopy(Book):
    def __init__(self, title, author, book_id, pages):
        super().__init__(title, author, book_id)
        self.pages = pages

    def book_type(self):
        return "Hard Copy"

# EBook class (Extends Book)
class EBook(Book):
    def __init__(self, title, author, book_id, file_size):
        super().__init__(title, author, book_id)
        self.file_size = file_size

    def book_type(self):
        return "E-Book"

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
        self.borrowed_books = 0

class Librarian(Person):
    def __init__(self, user_id, name):
        super().__init__(user_id, name)



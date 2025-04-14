class Node:
    def __init__(self, data):
        self.data = data 
        self.next = None

class LinkedList:
    def __init__(self):
        self.head = None

    def insert_data(self, data):
        new_node = Node(data)
        if not self.head:
            self.head = new_node
        else:
            new_node.next = self.head
            self.head = new_node


    def delete(self, title):
        temp = self.head
        prev = None

        while temp:
            if isinstance(temp.data, Book) and temp.data.get_title() == title:
                if prev:
                    prev.next = temp.next
                else:
                    self.head = temp.next
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
                    status = "Available" if temp.data.is_available() else "Borrowed"
                    print(f"{temp.data.get_title()} by {temp.data.get_author()} ({temp.data.book_type()}) - {status}")
                else:
                    print(temp.data)
                temp = temp.next


class Person:
    def __init__(self, user_id, name):
        self.__user_id = user_id
        self.__name = name

    def get_user_id(self):
        return self.__user_id

    def get_name(self):
        return self.__name


class Student(Person):
    def __init__(self, user_id, name, max_books=3):
        super().__init__(user_id, name)
        self.max_books = max_books
        self._borrowed_books = []

    def get_borrowed_books(self):
        return self._borrowed_books

    def borrow_book(self, book, transactions_list):
        if not book.is_available():
            return f"Sorry, {book.get_title()} is already borrowed."
        if len(self._borrowed_books) < self.max_books:
            self._borrowed_books.append(book)
            book.set_availability(False)
            transactions_list.insert_data(f"{self.get_name()} borrowed {book.get_title()}")
            return f"{self.get_name()} borrowed {book.get_title()}"
        return f"{self.get_name()} cannot borrow more than {self.max_books} books."

    def return_book(self, book, transactions_list):
        if book in self._borrowed_books:
            self._borrowed_books.remove(book)
            book.set_availability(True)
            transactions_list.insert_data(f"{self.get_name()} returned {book.get_title()}")
            return f"{self.get_name()} returned {book.get_title()}"
        return f"{self.get_name()} does not have {book.get_title()}."


class Librarian(Person):
    def __init__(self, user_id, name):
        super().__init__(user_id, name)

    def add_book(self, book_list, book):
        book_list.insert_data(book)
        return f"{book.get_title()} added to the library."

    def remove_book(self, book_list, book_title):
        return book_list.delete(book_title)


class Book:
    def __init__(self, title, author, isbn):
        self.__title = title
        self.__author = author
        self.__isbn = isbn
        self.__is_available = True

    def book_type(self):
        return "General Book"

    def is_available(self):
        return self.__is_available

    def set_availability(self, value):
        self.__is_available = value

    def get_title(self):
        return self.__title

    def get_author(self):
        return self.__author

    def get_isbn(self):
        return self.__isbn


class HardCopy(Book):
    def __init__(self, title, author, isbn, pages):
        super().__init__(title, author, isbn)
        self.pages = pages

    def book_type(self):
        return "Hard Copy"


class EBook(Book):
    def __init__(self, title, author, isbn, file_size):
        super().__init__(title, author, isbn)
        self.file_size = file_size

    def book_type(self):
        return "E-Book"


class LMS:
    def __init__(self):
        self.users_list = LinkedList()
        self.transactions_list = LinkedList()
        self.books_list = LinkedList()

    def check_id(self, uid):
        temp = self.users_list.head
        while temp:
            if temp.data.get_user_id() == uid:
                return temp.data
            temp = temp.next
        return None

    def add_person(self, uid):
        name = input("Enter your name: ")
        if str(uid)[0] == "1":
            print("It's a librarian.")
            person = Librarian(uid, name)
        else:
            print("It's a student.")
            person = Student(uid, name)
        self.users_list.insert_data(person)
        return person

    def make_book(self, typ):
        title = input("Enter book title: ")
        author = input("Enter author name: ")
        isbn = input("Enter ISBN: ")
        if "ook" in typ.lower():
            file_size = input("Enter file size: ")
            return EBook(title, author, isbn, file_size)
        else:
            pages = int(input("Enter number of pages: "))
            return HardCopy(title, author, isbn, pages)

    def student_borrow(self, user, book_title):  
        temp = self.books_list.head
        while temp:
            if temp.data.get_title() == book_title:
                print(user.borrow_book(temp.data, self.transactions_list))  
                return
            temp = temp.next
        print("Book not found.")

    def student_return(self, user, book_title): 
        for book in user.get_borrowed_books():
            if book.get_title() == book_title:
                print(user.return_book(book, self.transactions_list))  
                return
        print("You don't have this book.")

    def borrows(self, user):
        borrowed_books = user.get_borrowed_books()
        if borrowed_books:
            print("\nYour Borrowed Books:")
            for book in borrowed_books:
                print(f"{book.get_title()} by {book.get_author()} ({book.book_type()})")
        else:
            print("You have not borrowed any books.")


# Main System Loop
lms = LMS()
print("Welcome to our Library Management System!")
while True:
    choice = int(input("Enter 1 to proceed, 0 to stop system: "))
    if choice == 0:
        print("Shutting down system...")
        break
    try:
        
        uid = int(input("Enter your user ID: "))
        user = lms.check_id(uid)

        if not user:
            user = lms.add_person(uid)

        if isinstance(user, Librarian):
            while True:
                choice = int(input("\nWelcome, Librarian.\n1. Add Book\n2. Remove Book\n3. Display Books\n4. Display Transaction Logs\n5. Exit\nEnter your choice: "))
                if choice == 5:
                    break
                elif choice == 1:
                    book_type = input("Enter type of book (EBook/HardCopy): ")
                    book = lms.make_book(book_type)
                    print(user.add_book(lms.books_list, book))
                elif choice == 2:
                    book_title = input("Enter the title of the book to remove: ")
                    print(user.remove_book(lms.books_list, book_title))
                elif choice == 3:
                    lms.books_list.display()
                elif choice == 4:
                    print("\nTransaction Logs:")
                    lms.transactions_list.display()
                else:
                    print("Invalid choice.")

        elif isinstance(user, Student):
            while True:
                choice = int(input("\nWelcome, Student.\n1. Borrow Book\n2. Return Book\n3. Display Books\n4. Display Borrowed Books\n5. Exit\nEnter your choice: "))
                if choice == 5:
                    break
                elif choice == 1:
                    book_title = input("Enter book title to borrow: ")
                    lms.student_borrow(user, book_title)
                elif choice == 2:
                    book_title = input("Enter book title to return: ")
                    lms.student_return(user, book_title)
                elif choice == 3:
                    lms.books_list.display()
                elif choice == 4:
                    lms.borrows(user)
                else:
                    print("Invalid choice.")
    except:
        print("Error. Please try again.")

/*
 * This should be your main class where all your objects will be created
 */
package org.example;

public class App {
    public static void main(String[] args) {
        // Create a library instance
        Library library = new Library();

        // Create some books
        Book book1 = new Book("1984", "George Orwell", "123456789");
        Book book2 = new Book("To Kill a Mockingbird", "Harper Lee", "987654321");
        
        // Add books to the library
        library.addBook(book1);
        library.addBook(book2);

        // Create patrons
        Patron patron1 = new Patron("Alice");
        Patron patron2 = new Patron("Bob");

        // Register patrons
        library.registerPatron(patron1);
        library.registerPatron(patron2);

        // Simulate borrowing and returning books
        patron1.borrowBook(book1); // Alice borrows 1984
        patron2.borrowBook(book1); // Bob tries to borrow 1984 (should be unavailable)
        
        patron1.returnBook(book1); // Alice returns 1984
        patron2.borrowBook(book1); // Bob borrows 1984 (should be successful)
        
        patron2.returnBook(book2); // Bob tries to return a book he didn't borrow
    }
}

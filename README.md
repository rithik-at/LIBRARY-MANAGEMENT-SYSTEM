# Library Management System

A **Java Swing-based desktop application** for managing books in a library. This system allows librarians to **store, track, and update the status of books** in real-time using a **MySQL database** for persistent storage. It is designed to be user-friendly, efficient, and scalable.

---

## Features

- **Add Books:** Enter book details including Title, Author, and ISBN.  
- **View Books:** Display all books in a table with current availability.  
- **Issue Books:** Issue a book to a member and update its status automatically.  
- **Return Books:** Return issued books and mark them as available.  
- **Remove Books:** Delete books from the library database.  
- **Real-time Status Updates:** Track the availability of each book instantly.  
- **GUI:** Intuitive and professional Java Swing interface with gradient background.

---

## Technologies Used

- **Java (JDK 17+)** – For GUI and application logic  
- **Java Swing** – Desktop GUI development  
- **MySQL** – Relational database for storing book records  
- **JDBC** – Java Database Connectivity for interacting with MySQL

  
## Project Workflow

1. **Add a Book:** Librarian enters book details → Book is added to the database → Status set as `Available`.  
2. **View Books:** All books are displayed in a table showing ID, Title, Author, ISBN, and Status.  
3. **Issue Book:** Enter Book ID → Status changes from `Available` to `Issued` → Librarian is notified.  
4. **Return Book:** Enter Book ID → Status changes back to `Available` → System updates in real-time.  
5. **Remove Book:** Enter Book ID → Book record is deleted from the database → Table refreshes automatically.


<img width="1156" height="729" alt="Screenshot 2025-10-18 212813" src="https://github.com/user-attachments/assets/441a9dea-4c9f-40ee-bc75-439a244a853a" />

<img width="1348" height="961" alt="Screenshot 2025-10-18 212939" src="https://github.com/user-attachments/assets/24b5ae5a-8825-43bd-829e-00b88c065be4" />





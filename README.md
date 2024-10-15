# JavaFX Sales Management System  
A comprehensive system for managing sales, inventory, expenses, and customers, designed for businesses dealing with multiple products from the same brand.

![Platform](https://img.shields.io/badge/Platform-JavaFX-green)
![Database](https://img.shields.io/badge/Database-MySQL-blue)
![Status](https://img.shields.io/badge/Status-In_Development-orange)

## Table of Contents
- [About the Project](#about-the-project)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Installation](#installation)
- [Usage](#usage)
- [Screenshots](#screenshots)

## About the Project
This JavaFX Sales Management System is designed to streamline the sales process for businesses that deal with multiple products from the same brand. It provides an easy way to manage inventory, track sales, calculate daily and monthly profit, and automatically handle ledger entries. The system includes CRUD functionalities for customers, items, and expenses while also allowing for the application of discounts and tracking bills. It generates detailed monthly reports on profits and related financial metrics, helping businesses make informed decisions.

## Features
- Sales management: Add, edit, delete, and view sales records with date-based filtering.
- Inventory management: CRUD operations for items and product purchases.
- Expense tracking: Record and manage business expenses.
- Customer management: CRUD functionality for customers.
- Discount and bill management: Apply discounts and manage customer bills.
- Automatic ledger entries: Automate calculation of COGS (cost of goods sold), revenue, and profit. (~)
- Reporting: Generate monthly profit reports and detailed financial summaries. (~)

## Tech Stack
- **Frontend**: JavaFX
- **Backend**: MySQL
- **Database Connection**: JDBC
- **Architecture**: MVC (Model-View-Controller)
- **Tools**: IntelliJ IDEA, phpMyAdmin

## Installation
1. Clone the repository
   ```bash
   git clone https://github.com/Clesssss/Cleo
   ```
2. Open the project in IntelliJ IDEA or your preferred Java IDE
3. Ensure you have a local MySQL database setup. Import the provided database schema into MySQL.
4. Update the database connection settings in the JDBC configuration.
5. Run the application.

## Usage
- Daily Sales Report: Navigate to "today" to view today sales report or use the date picker to select desired date
- Sales Entry: Navigate to the "Daily Sales", below the sales table there is a section to add, edit, and delete daily sales records.
- Inventory Management: Manage products and purchases in the "Inventory" section. (~)
- Expense Tracking: Record daily business expenses in the "Expenses" section.
- Customer Management: Add, edit, and delete customer information.
- Reports: Generate monthly financial reports from the "Reports" tab to analyze profit and performance. (~)
- Discounts: Apply discounts on sales and track bills for each transaction.

## Screenshots
### Menu
<img src="https://github.com/Clesssss/Cleo/blob/master/screenshots/Menu.png" alt="Menu" width="400" />  

### Daily Sales
<img src="https://github.com/Clesssss/Cleo/blob/master/screenshots/Daily%20Sales.png" alt="Daily Sales" width="400" />  

### Sales Invoice
<img src="https://github.com/Clesssss/Cleo/blob/master/screenshots/Sales%20Invoice.png" alt="Sales Invoice" width="400" />  

### Item
<img src="https://github.com/Clesssss/Cleo/blob/master/screenshots/Item.png" alt="Item" width="400" />  

### Customer
<img src="https://github.com/Clesssss/Cleo/blob/master/screenshots/Customer.png" alt="Customer" width="400" />  

### Purchase
<img src="https://github.com/Clesssss/Cleo/blob/master/screenshots/Purchase.png" alt="Purchase" width="400" />  

### Add Ledger Entry
<img src="https://github.com/Clesssss/Cleo/blob/master/screenshots/Add%20Ledger%20Entry.png" alt="Add Ledger Entry" width="400" />  

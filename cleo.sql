-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Oct 15, 2024 at 10:27 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `cleo`
--

-- --------------------------------------------------------

--
-- Table structure for table `account`
--

CREATE TABLE `account` (
  `account_id` int(10) NOT NULL,
  `name` varchar(50) NOT NULL,
  `type` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `customer`
--

CREATE TABLE `customer` (
  `customer_id` int(10) NOT NULL,
  `name` varchar(50) NOT NULL,
  `area` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `item`
--

CREATE TABLE `item` (
  `item_id` int(10) NOT NULL,
  `name` varchar(50) NOT NULL,
  `buy_price` int(10) NOT NULL,
  `sell_price` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `item`
--

INSERT INTO `item` (`item_id`, `name`, `buy_price`, `sell_price`) VALUES
(1, 'CLEO CUP 220 ML', 17475, 25000),
(2, 'CLEO CLASSIC 330 ML', 39375, 47500),
(3, 'CLEO CLASSIC 550 ML', 50075, 58500),
(4, 'CLEO 4 6000 ML', 49830, 65000),
(5, 'CLEO SMART 220 ML', 17200, 24000),
(6, 'CLEO ECO 330 ML', 28975, 38500),
(7, 'CLEO ECO 550 ML', 33175, 45000),
(8, 'CLEO ECO 1500 ML', 38325, 51000),
(9, 'CLEO GALON 19 LT', 14050, 24000),
(10, 'GALON KOSONG', 35000, 50000);

-- --------------------------------------------------------

--
-- Table structure for table `ledger_entry`
--

CREATE TABLE `ledger_entry` (
  `ledger_entry_id` int(10) NOT NULL,
  `account_id` int(10) NOT NULL,
  `sales_id` int(10) DEFAULT NULL,
  `purchase_id` int(10) DEFAULT NULL,
  `amount` int(10) NOT NULL,
  `description` varchar(50) NOT NULL,
  `entry_date` date NOT NULL,
  `is_sales` tinyint(1) NOT NULL,
  `is_paid` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `purchase`
--

CREATE TABLE `purchase` (
  `purchase_id` int(10) NOT NULL,
  `date` date NOT NULL,
  `total_purchase` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `purchase_detail`
--

CREATE TABLE `purchase_detail` (
  `purchase_id` int(10) NOT NULL,
  `item_id` int(10) NOT NULL,
  `quantity` int(10) NOT NULL,
  `total_price` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `sales`
--

CREATE TABLE `sales` (
  `sales_id` int(10) NOT NULL,
  `store_name` varchar(50) NOT NULL,
  `area` varchar(50) NOT NULL,
  `date` date NOT NULL,
  `total_sales` int(10) NOT NULL,
  `discount_amount` int(10) DEFAULT NULL,
  `is_bill` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `sales_detail`
--

CREATE TABLE `sales_detail` (
  `sales_id` int(10) NOT NULL,
  `item_id` int(10) NOT NULL,
  `amount` int(10) NOT NULL,
  `total_price` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `account`
--
ALTER TABLE `account`
  ADD PRIMARY KEY (`account_id`);

--
-- Indexes for table `customer`
--
ALTER TABLE `customer`
  ADD PRIMARY KEY (`customer_id`);

--
-- Indexes for table `item`
--
ALTER TABLE `item`
  ADD PRIMARY KEY (`item_id`);

--
-- Indexes for table `ledger_entry`
--
ALTER TABLE `ledger_entry`
  ADD PRIMARY KEY (`ledger_entry_id`),
  ADD KEY `fk_account_id` (`account_id`),
  ADD KEY `fk_sales_id` (`sales_id`),
  ADD KEY `fk_purchase_id` (`purchase_id`);

--
-- Indexes for table `purchase`
--
ALTER TABLE `purchase`
  ADD PRIMARY KEY (`purchase_id`);

--
-- Indexes for table `purchase_detail`
--
ALTER TABLE `purchase_detail`
  ADD PRIMARY KEY (`purchase_id`,`item_id`),
  ADD KEY `fk_item_id` (`item_id`);

--
-- Indexes for table `sales`
--
ALTER TABLE `sales`
  ADD PRIMARY KEY (`sales_id`);

--
-- Indexes for table `sales_detail`
--
ALTER TABLE `sales_detail`
  ADD PRIMARY KEY (`sales_id`,`item_id`),
  ADD KEY `fk_item_id2` (`item_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `account`
--
ALTER TABLE `account`
  MODIFY `account_id` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `customer`
--
ALTER TABLE `customer`
  MODIFY `customer_id` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `item`
--
ALTER TABLE `item`
  MODIFY `item_id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `ledger_entry`
--
ALTER TABLE `ledger_entry`
  MODIFY `ledger_entry_id` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `purchase`
--
ALTER TABLE `purchase`
  MODIFY `purchase_id` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `sales`
--
ALTER TABLE `sales`
  MODIFY `sales_id` int(10) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `ledger_entry`
--
ALTER TABLE `ledger_entry`
  ADD CONSTRAINT `fk_account_id` FOREIGN KEY (`account_id`) REFERENCES `account` (`account_id`),
  ADD CONSTRAINT `fk_purchase_id` FOREIGN KEY (`purchase_id`) REFERENCES `purchase` (`purchase_id`),
  ADD CONSTRAINT `fk_sales_id` FOREIGN KEY (`sales_id`) REFERENCES `sales` (`sales_id`);

--
-- Constraints for table `purchase_detail`
--
ALTER TABLE `purchase_detail`
  ADD CONSTRAINT `fk_item_id` FOREIGN KEY (`item_id`) REFERENCES `item` (`item_id`),
  ADD CONSTRAINT `fk_purchase_id2` FOREIGN KEY (`purchase_id`) REFERENCES `purchase` (`purchase_id`);

--
-- Constraints for table `sales_detail`
--
ALTER TABLE `sales_detail`
  ADD CONSTRAINT `fk_item_id2` FOREIGN KEY (`item_id`) REFERENCES `item` (`item_id`),
  ADD CONSTRAINT `fk_sales_id2` FOREIGN KEY (`sales_id`) REFERENCES `sales` (`sales_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

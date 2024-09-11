-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 19, 2024 at 07:37 PM
-- Server version: 10.4.27-MariaDB
-- PHP Version: 8.1.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `360`
--

-- --------------------------------------------------------

--
-- Table structure for table `customer`
--

CREATE TABLE `customer` (
  `Id_User` int(11) NOT NULL,
  `Name` varchar(100) NOT NULL,
  `Address` varchar(100) NOT NULL,
  `Date` date NOT NULL,
  `LicenseNumber` varchar(100) DEFAULT NULL,
  `CreditCard_Id` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `customer`
--

INSERT INTO `customer` (`Id_User`, `Name`, `Address`, `Date`, `LicenseNumber`, `CreditCard_Id`) VALUES
(1, 'myron', 'naxou', '2002-09-09', '1234567', '1234'),
(2, 'peosman', 'naxou 7', '2002-05-23', '', '123123'),
(3, 'Stavros', 'naksou 7', '2001-01-29', '123', '2390239fs4'),
(4, 'nikos', 'trixa 2', '2023-01-19', 'asdasd', '12345a'),
(5, 'telefteos', 'kolitos 2', '1823-02-01', '', 'asdadjs');

-- --------------------------------------------------------

--
-- Table structure for table `rental`
--

CREATE TABLE `rental` (
  `Rental_Id` int(11) NOT NULL,
  `Id_User` int(11) NOT NULL,
  `Date` datetime NOT NULL,
  `Duration` int(100) NOT NULL,
  `Payment` int(100) NOT NULL,
  `RegistrationNumber` varchar(100) NOT NULL,
  `VehicleType` varchar(100) NOT NULL,
  `InsurancePaid` tinyint(1) NOT NULL DEFAULT 0,
  `returned` tinyint(4) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `rental`
--

INSERT INTO `rental` (`Rental_Id`, `Id_User`, `Date`, `Duration`, `Payment`, `RegistrationNumber`, `VehicleType`, `InsurancePaid`, `returned`) VALUES
(13, 1, '2024-01-19 18:29:55', 1, 5, 'GAMER', 'SUV', 1, 1),
(14, 1, '2024-01-19 18:38:05', 21, 42, 'TEST', 'SUV', 1, 0),
(15, 3, '2024-01-19 20:14:29', 23, 2323, 'asda1', 'SMART', 1, 0),
(16, 1, '2024-01-19 20:16:38', 2, 312, 'testarw', 'MOTORCYCLE', 1, 0),
(17, 3, '2024-01-19 20:17:42', 2, 50, '8bc54f1e-67a7-49ae-b6fc-13488d6921c6', 'SCOOTER', 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `vehicle`
--

CREATE TABLE `vehicle` (
  `brand` varchar(100) NOT NULL,
  `model` varchar(100) NOT NULL,
  `color` varchar(100) NOT NULL,
  `autonomy` int(100) NOT NULL,
  `RegistrationNumber` varchar(100) NOT NULL,
  `VehicleType` varchar(100) NOT NULL,
  `Passengers` int(100) NOT NULL,
  `InsuranceCost` int(100) NOT NULL,
  `DailyRental` int(100) NOT NULL,
  `Availability` tinyint(1) NOT NULL DEFAULT 1,
  `RepairDays` int(11) NOT NULL DEFAULT 0,
  `Rented` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `vehicle`
--

INSERT INTO `vehicle` (`brand`, `model`, `color`, `autonomy`, `RegistrationNumber`, `VehicleType`, `Passengers`, `InsuranceCost`, `DailyRental`, `Availability`, `RepairDays`, `Rented`) VALUES
('toyota', 'as2', 'white', 2, '12gf', 'MOTORCYCLE', 2, 12, 150, 0, 3, 1),
('PORTA', 'TO2', 'GREY', 12000, '8bc54f1e-67a7-49ae-b6fc-13488d6921c6', 'SCOOTER', 2, 12000, 25, 0, 0, 1),
('ferrari', 'wheel', 'purple', 22000, '9ddd9bec-8e38-41d6-99a3-34f9edf2a1b5', 'BICYCLE', 2, 12, 50, 1, 0, 0),
('ford', 't2', 'red', 12000, 'asda1', 'SMART', 2, 23, 100, 0, 0, 1),
('POUTSA', 'POUTASZ', 'MPLE', 20000, 'GAMER', 'SUV', 2, 2, 3, 1, 0, 3),
('FERRARI', 'R2', 'RED', 20000, 'SDKSDK2323', 'CABRIO', 2, 200, 300, 1, 0, 0),
('CHEVROLET', 'B12', 'BLUE', 3000, 'TEST', 'SUV', 2, 2, 2, 0, 0, 1),
('toyota', 'motorola', 'pink', 23000, 'testarw', 'MOTORCYCLE', 1, 12, 150, 0, 0, 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `customer`
--
ALTER TABLE `customer`
  ADD PRIMARY KEY (`Id_User`);

--
-- Indexes for table `rental`
--
ALTER TABLE `rental`
  ADD PRIMARY KEY (`Rental_Id`),
  ADD KEY `RegistrationNumber` (`RegistrationNumber`),
  ADD KEY `Id_User` (`Id_User`);

--
-- Indexes for table `vehicle`
--
ALTER TABLE `vehicle`
  ADD PRIMARY KEY (`RegistrationNumber`),
  ADD UNIQUE KEY `RegistrationNumber` (`RegistrationNumber`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `customer`
--
ALTER TABLE `customer`
  MODIFY `Id_User` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `rental`
--
ALTER TABLE `rental`
  MODIFY `Rental_Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `rental`
--
ALTER TABLE `rental`
  ADD CONSTRAINT `rental_ibfk_1` FOREIGN KEY (`RegistrationNumber`) REFERENCES `vehicle` (`RegistrationNumber`),
  ADD CONSTRAINT `rental_ibfk_2` FOREIGN KEY (`Id_User`) REFERENCES `customer` (`Id_User`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

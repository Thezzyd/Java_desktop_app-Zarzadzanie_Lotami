-- phpMyAdmin SQL Dump
-- version 4.7.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Czas generowania: 25 Kwi 2021, 13:40
-- Wersja serwera: 10.1.28-MariaDB
-- Wersja PHP: 7.1.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Baza danych: `zl`
--

DROP DATABASE IF EXISTS zl;
CREATE DATABASE IF NOT EXISTS `zl` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `zl`;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `aircraft`
--

CREATE TABLE IF NOT EXISTS `aircraft` (
  `id_aircraft` int(10) NOT NULL AUTO_INCREMENT,
  `brand_name` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `model` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `engine_number` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `seats_number` int(10) NOT NULL,
  `technical_condition` set('for_check_up','operational','not_operational') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'for_check_up',
  `is_tanked` set('true','false') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'false',
  `status` set('available','in_flight') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'available',
  PRIMARY KEY (`id_aircraft`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Zrzut danych tabeli `aircraft`
--

INSERT INTO `aircraft` (`id_aircraft`, `brand_name`, `model`, `engine_number`, `seats_number`, `technical_condition`, `is_tanked`, `status`) VALUES
(1, 'Ryanair', '787 Dreamliner', 'G189X001', 186, 'operational', 'true', 'available'),
(2, 'Ryanair', '787 Dreamliner', 'G189X002', 186, 'for_check_up', 'true', 'available'),
(3, 'Ryanair', '787 Dreamliner', 'G189X003', 186, 'operational', 'false', 'available'),
(4, 'Ryanair', '787 Dreamliner', 'G189X004', 186, 'operational', 'true', 'available'),
(5, 'Ryanair', '787 Dreamliner', 'G189X005', 186, 'operational', 'false', 'available'),
(6, 'Ryanair', '777 X', 'G234X001', 204, 'operational', 'false', 'available'),
(7, 'Ryanair', '777 X', 'G234X002', 204, 'operational', 'false', 'available'),
(8, 'Ryanair', '777 X', 'G234X003', 204, 'operational', 'true', 'available'),
(9, 'Ryanair', '777 X', 'G234X004', 204, 'operational', 'true', 'available'),
(10, 'EasyJet', 'Airbus A319-100', 'F174X001', 186, 'for_check_up', 'false', 'available'),
(11, 'EasyJet', 'Airbus A319-100', 'F174X002', 186, 'for_check_up', 'false', 'available'),
(12, 'EasyJet', 'Airbus A319-100', 'F174X003', 186, 'operational', 'true', 'available'),
(13, 'EasyJet', 'Airbus A319-100', 'F174X004', 186, 'operational', 'true', 'available'),
(14, 'EasyJet', 'Airbus A319-100', 'F174X005', 186, 'operational', 'true', 'available'),
(15, 'EasyJet', 'Airbus A319-100', 'F174X006', 186, 'operational', 'false', 'available'),
(16, 'EasyJet', 'Airbus A320-200', 'U332X007', 146, 'for_check_up', 'false', 'available'),
(17, 'Jet2', 'Boeing 737', 'H432X001', 146, 'operational', 'false', 'available'),
(18, 'Jet2', 'Boeing 737', 'H432X002', 146, 'for_check_up', 'false', 'available'),
(19, 'Jet2', 'Boeing 737', 'H432X003', 146, 'operational', 'false', 'available'),
(20, 'Jet2', 'Boeing 757', 'H434X001', 124, 'not_operational', 'true', 'available'),
(21, 'Jet2', 'Boeing 757', 'H434X002', 124, 'for_check_up', 'true', 'available');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `flight_schedule`
--

CREATE TABLE IF NOT EXISTS `flight_schedule` (
  `id_flight_schedule` int(10) NOT NULL AUTO_INCREMENT,
  `id_aircraft` int(10) DEFAULT NULL,
  `departure_place` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `departure_date` date NOT NULL,
  `departure_time` time NOT NULL,
  `destination` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `arrival_date` date NOT NULL,
  `arrival_time` time NOT NULL,
  `price` double NOT NULL,
  PRIMARY KEY (`id_flight_schedule`),
  KEY `id_aircraft1` (`id_aircraft`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Zrzut danych tabeli `flight_schedule`
--

INSERT INTO `flight_schedule` (`id_flight_schedule`, `id_aircraft`, `departure_place`, `departure_date`, `departure_time`, `destination`, `arrival_date`, `arrival_time`, `price`) VALUES
(1, NULL, 'Rzeszów', '2020-04-27', '14:15:00', 'Warsaw', '2020-04-27', '15:00:00', 130),
(2, NULL, 'Rzeszow', '2020-04-27', '16:00:00', 'London', '2020-04-27', '17:10:00', 315),
(3, NULL, 'Rzeszow', '2020-04-15', '08:00:00', 'Chicago', '2020-04-27', '14:35:00', 980);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `position`
--

CREATE TABLE IF NOT EXISTS `position` (
  `id_position` int(10) NOT NULL AUTO_INCREMENT,
  `position_name` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id_position`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Zrzut danych tabeli `position`
--

INSERT INTO `position` (`id_position`, `position_name`) VALUES
(1, 'Administrator'),
(2, 'Manager'),
(3, 'Avionics technician'),
(4, 'Flight controller'),
(5, 'Customer service');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `privilege`
--

CREATE TABLE IF NOT EXISTS `privilege` (
  `id_privilege` int(10) NOT NULL AUTO_INCREMENT,
  `id_position` int(10) NOT NULL,
  `module` set('Account manager','Technical efficiency','Air traffic','Customer service') COLLATE utf8mb4_unicode_ci NOT NULL,
  `access` tinyint(1) NOT NULL,
  PRIMARY KEY (`id_privilege`),
  KEY `id_position111` (`id_position`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Zrzut danych tabeli `privilege`
--

INSERT INTO `privilege` (`id_privilege`, `id_position`, `module`, `access`) VALUES
(1, 1, 'Account manager', 1),
(2, 1, 'Technical efficiency', 1),
(3, 1, 'Air traffic', 1),
(4, 1, 'Customer service', 1),
(5, 2, 'Account manager', 1),
(6, 2, 'Technical efficiency', 1),
(7, 2, 'Air traffic', 1),
(8, 2, 'Customer service', 1),
(9, 3, 'Account manager', 0),
(10, 3, 'Technical efficiency', 1),
(11, 3, 'Air traffic', 0),
(12, 3, 'Customer service', 0),
(13, 4, 'Account manager', 0),
(14, 4, 'Technical efficiency', 0),
(15, 4, 'Air traffic', 1),
(16, 4, 'Customer service', 0),
(17, 5, 'Account manager', 0),
(18, 5, 'Technical efficiency', 0),
(19, 5, 'Air traffic', 0),
(20, 5, 'Customer service', 1);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `ticket`
--

CREATE TABLE IF NOT EXISTS `ticket` (
  `id_ticket` int(10) NOT NULL AUTO_INCREMENT,
  `id_flight_schedule` int(10) NOT NULL,
  `firstname` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `lastname` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `phone_number` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id_ticket`),
  KEY `id_flight_schedule1` (`id_flight_schedule`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Zrzut danych tabeli `ticket`
--

INSERT INTO `ticket` (`id_ticket`, `id_flight_schedule`, `firstname`, `lastname`, `phone_number`, `email`) VALUES
(1, 1, 'Anna', 'Dynia', '121883949', 'anna.dynia12@gmail.com'),
(2, 1, 'Robert', 'Stępień', '753425755', 'robercikS12@gmail.com'),
(3, 1, 'Błażej', 'Mazurek', '765322313', 'b.mazur3@gmail.com'),
(4, 1, 'Kornel', 'Kucharski', '134453236', 'kornello@gmail.com'),
(5, 1, 'Oktawia', 'Sadowska', '322156326', 'oktawia123321@gmail.com'),
(6, 1, 'Zofia', 'Jaworska', '753215965', 'anna.jaworska.zofia.94@gmail.com'),
(7, 1, 'Alan', 'Brzeziński', '321488775', 'alan_brz011@gmail.com'),
(8, 1, 'Jagoda', 'Cieślak', '998562156', 'jagodaCieslak991@gmail.com'),
(9, 1, 'Frańciszka', 'Wójcik', '664584215', 'franwojcik87@gmail.com'),
(10, 1, 'Antoni', 'Krawczyk', '639874785', 'anna.antoskrawczykowy@gmail.com'),
(11, 1, 'Beata', 'Nowak', '458745874', 'beataaaNowak11@gmail.com'),
(12, 1, 'Radosław', 'Makowski', '959598745', 'radziox33@gmail.com'),
(13, 1, 'Maurycy', 'Mróz', '652325414', 'mroz.maurycy.official@gmail.com'),
(14, 1, 'Józefa', 'Laskowska', '555214587', 'JozkaLaskowa333@gmail.com');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `time_simulation`
--

CREATE TABLE IF NOT EXISTS `time_simulation` (
  `date` date NOT NULL,
  `time` time NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Zrzut danych tabeli `time_simulation`
--

INSERT INTO `time_simulation` (`date`, `time`) VALUES
('2021-04-09', '13:39:00');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `id_user` int(10) NOT NULL AUTO_INCREMENT,
  `firstname` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `lastname` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `username` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `id_position` int(10) NOT NULL,
  `email` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `phone_number` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id_user`),
  UNIQUE KEY `username_UQ` (`username`),
  KEY `id_position2` (`id_position`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Zrzut danych tabeli `user`
--

INSERT INTO `user` (`id_user`, `firstname`, `lastname`, `username`, `password`, `id_position`, `email`, `phone_number`) VALUES
(1, 'admin', 'admin', 'admin', 'admin', 1, 'najlepszyAdmin@gmail.com', '999999111'),
(2, 'Staszek', 'Super', 'stachu1', 'haslo', 1, 'stachu1@gmali.com', '666111222'),
(3, 'Marian', 'Kowalski', 'marianoo', 'haslo', 2, 'mario1@gmali.com', '787541256'),
(4, 'Zbyszekff', 'Malinowski', 'zbyniu', 'haslo', 2, 'zbynioo1@gmali.com', '987789656');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `work_schedule`
--

CREATE TABLE IF NOT EXISTS `work_schedule` (
  `id_work_schedule` int(10) NOT NULL AUTO_INCREMENT,
  `id_user` int(10) NOT NULL,
  `year` int(10) NOT NULL,
  `week` int(10) NOT NULL,
  `monday_hours` varchar(80) COLLATE utf8mb4_unicode_ci NOT NULL,
  `tuesday_hours` varchar(80) COLLATE utf8mb4_unicode_ci NOT NULL,
  `wednesday_hours` varchar(80) COLLATE utf8mb4_unicode_ci NOT NULL,
  `thursday_hours` varchar(80) COLLATE utf8mb4_unicode_ci NOT NULL,
  `friday_hours` varchar(80) COLLATE utf8mb4_unicode_ci NOT NULL,
  `saturday_hours` varchar(80) COLLATE utf8mb4_unicode_ci NOT NULL,
  `sunday_hours` varchar(80) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id_work_schedule`),
  KEY `id_user1` (`id_user`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Zrzut danych tabeli `work_schedule`
--

INSERT INTO `work_schedule` (`id_work_schedule`, `id_user`, `year`, `week`, `monday_hours`, `tuesday_hours`, `wednesday_hours`, `thursday_hours`, `friday_hours`, `saturday_hours`, `sunday_hours`) VALUES
(1, 1, 2021, 13, '8,9,10,11,12,13,14,15,16,', '8,9,10,11,12,13,14,15,16,', '8,9,10,11,12,13,14,15,16,', '8,9,10,11,12,13,14,15,16,', '8,9,10,11,12,13,14,15,16,', '8,9,10,11,12,13,14,15,16,', '8,9,10,11,12,13,14,15,16,'),
(2, 2, 2021, 13, '0,1,2,3,4,5,6,7,', '0,1,2,3,4,5,6,7,', '0,1,2,3,4,5,6,7,', '0,1,2,3,4,5,6,7,', '0,1,2,3,4,5,6,7,', '0,1,2,3,4,5,6,7,', '0,1,2,3,4,5,6,7,');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `work_schedule_template`
--

CREATE TABLE IF NOT EXISTS `work_schedule_template` (
  `template_name` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `monday_hours` varchar(80) COLLATE utf8mb4_unicode_ci NOT NULL,
  `tuesday_hours` varchar(80) COLLATE utf8mb4_unicode_ci NOT NULL,
  `wednesday_hours` varchar(80) COLLATE utf8mb4_unicode_ci NOT NULL,
  `thursday_hours` varchar(80) COLLATE utf8mb4_unicode_ci NOT NULL,
  `friday_hours` varchar(80) COLLATE utf8mb4_unicode_ci NOT NULL,
  `saturday_hours` varchar(80) COLLATE utf8mb4_unicode_ci NOT NULL,
  `sunday_hours` varchar(80) COLLATE utf8mb4_unicode_ci NOT NULL,
  UNIQUE KEY `template_name` (`template_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Zrzut danych tabeli `work_schedule_template`
--

INSERT INTO `work_schedule_template` (`template_name`, `monday_hours`, `tuesday_hours`, `wednesday_hours`, `thursday_hours`, `friday_hours`, `saturday_hours`, `sunday_hours`) VALUES
('midday_hours', '14,15,16,17,18,19,20', '14,15,16,17,18,19,20', '14,15,16,17,18,19,20', '14,15,16,17,18,19,20', '14,15,16,17,18,19,20', '14,15,16,17,18,19,20', '14,15,16,17,18,19,20'),
('morning_hours', '6,7,8,9,10,11,12,13', '6,7,8,9,10,11,12,13', '6,7,8,9,10,11,12,13', '6,7,8,9,10,11,12,13', '6,7,8,9,10,11,12,13', '6,7,8,9,10,11,12,13', '6,7,8,9,10,11,12,13'),
('night_hours', '22,23,0,1,2,3,4,5', '22,23,0,1,2,3,4,5', '22,23,0,1,2,3,4,5', '22,23,0,1,2,3,4,5', '22,23,0,1,2,3,4,5', '22,23,0,1,2,3,4,5', '22,23,0,1,2,3,4,5');

--
-- Ograniczenia dla zrzutów tabel
--

--
-- Ograniczenia dla tabeli `flight_schedule`
--
ALTER TABLE `flight_schedule`
  ADD CONSTRAINT `id_aircraft1` FOREIGN KEY (`id_aircraft`) REFERENCES `aircraft` (`id_aircraft`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ograniczenia dla tabeli `privilege`
--
ALTER TABLE `privilege`
  ADD CONSTRAINT `id_position111` FOREIGN KEY (`id_position`) REFERENCES `position` (`id_position`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ograniczenia dla tabeli `ticket`
--
ALTER TABLE `ticket`
  ADD CONSTRAINT `id_flight_schedule1` FOREIGN KEY (`id_flight_schedule`) REFERENCES `flight_schedule` (`id_flight_schedule`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ograniczenia dla tabeli `user`
--
ALTER TABLE `user`
  ADD CONSTRAINT `id_position2` FOREIGN KEY (`id_position`) REFERENCES `position` (`id_position`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ograniczenia dla tabeli `work_schedule`
--
ALTER TABLE `work_schedule`
  ADD CONSTRAINT `id_user1` FOREIGN KEY (`id_user`) REFERENCES `user` (`id_user`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

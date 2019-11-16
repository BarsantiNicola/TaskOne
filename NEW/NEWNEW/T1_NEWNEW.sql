CREATE DATABASE  IF NOT EXISTS `taskone` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `taskone`;
-- MySQL dump 10.13  Distrib 8.0.17, for Win64 (x86_64)
--
-- Host: localhost    Database: taskone
-- ------------------------------------------------------
-- Server version	8.0.17

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `hibernate_sequence`
--

DROP TABLE IF EXISTS `hibernate_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hibernate_sequence`
--

LOCK TABLES `hibernate_sequence` WRITE;
/*!40000 ALTER TABLE `hibernate_sequence` DISABLE KEYS */;
INSERT INTO `hibernate_sequence` VALUES (1);
/*!40000 ALTER TABLE `hibernate_sequence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `IDorder` int(11) NOT NULL,
  `price` int(11) NOT NULL,
  `purchaseDate` datetime NOT NULL,
  `status` varchar(45) NOT NULL,
  `customer` varchar(45) DEFAULT NULL,
  `productStock` int(11) DEFAULT NULL,
  PRIMARY KEY (`IDorder`),
  KEY `FKaa85k139lngb2pouwc6atnejk` (`customer`),
  KEY `FK7ho9v3n7jpgryad7mo7fe01sm` (`productStock`),
  CONSTRAINT `FK7ho9v3n7jpgryad7mo7fe01sm` FOREIGN KEY (`productStock`) REFERENCES `productstock` (`IDstock`),
  CONSTRAINT `FKaa85k139lngb2pouwc6atnejk` FOREIGN KEY (`customer`) REFERENCES `users` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,10,'2019-09-08 11:12:00','delivered','adri',76),(2,55,'2019-09-11 17:54:00','delivered','adri',111),(3,20,'2019-09-06 11:12:00','delivered','adri',4),(4,45,'2019-09-11 18:11:00','delivered','amy',25),(5,55,'2019-09-13 10:48:00','delivered','amy',112),(6,120,'2019-09-07 12:27:00','delivered','amy',51),(7,10,'2019-09-10 14:15:00','delivered','anto83',77),(8,10,'2019-09-14 22:15:00','delivered','anto83',78),(9,20,'2019-09-05 15:18:00','delivered','anto83',1),(10,20,'2019-09-08 12:00:00','delivered','betty',2),(11,45,'2019-09-09 09:15:00','delivered','betty',23),(12,120,'2019-09-06 18:15:00','delivered','betty',50),(13,10,'2019-09-07 02:47:00','delivered','brad',74),(14,10,'2019-09-08 07:12:00','delivered','brad',75),(15,55,'2019-09-14 19:21:00','delivered','brad',110),(16,20,'2019-09-07 11:05:00','delivered','Dani',3),(17,45,'2019-09-08 14:56:00','delivered','Dani',24),(18,20,'2019-09-15 13:29:00','delivered','Dani',5),(19,45,'2019-09-20 15:14:00','delivered','david91',26),(20,120,'2019-09-21 19:51:00','delivered','david91',52),(21,55,'2019-09-18 23:14:00','delivered','david91',113),(22,20,'2019-09-24 17:12:00','delivered','Dia',6),(23,120,'2019-09-17 08:15:00','delivered','Dia',53),(24,10,'2019-09-22 13:22:00','delivered','donny',79),(25,55,'2019-09-22 15:34:00','delivered','donny',114),(26,20,'2019-10-12 23:55:00','received','eth',7),(27,45,'2019-10-01 17:12:00','shipping','eth',27),(28,120,'2019-09-28 07:52:00','delivered','Gabriel',54),(29,10,'2019-10-05 22:14:00','shipping','Gabriel',80),(30,55,'2019-09-30 18:25:00','delivered','imanne',115),(31,45,'2019-10-01 11:14:00','delivered','imanne',28),(32,120,'2019-10-03 21:32:00','delivered','james96',55),(33,55,'2019-10-10 20:19:00','received','james96',116),(34,20,'2019-10-03 05:22:00','delivered','john72',8),(35,45,'2019-10-12 08:23:00','received','john72',29),(36,120,'2019-10-12 08:24:00','received','lisa',56),(37,10,'2019-10-05 19:47:00','shipping','lisa',81),(38,20,'2019-10-04 03:32:00','delivered','lola',9),(39,10,'2019-10-09 11:36:00','shipping','lola',82),(40,55,'2019-10-08 16:41:00','shipping','magicBob',117),(41,20,'2019-10-08 11:32:00','shipping','magicBob',10),(42,120,'2019-10-09 18:15:00','shipping','Marianne',57),(43,10,'2019-10-06 15:25:00','delivered','Marianne',83),(44,20,'2019-10-08 22:23:00','shipping','matty',11),(45,45,'2019-10-11 08:45:00','received','matty',30),(46,120,'2019-10-08 23:42:00','shipping','PaulPaul',58),(47,55,'2019-10-11 06:37:00','received','PaulPaul',118),(48,20,'2019-10-08 15:24:00','delivered','Philip',12),(49,120,'2019-10-09 17:55:00','shipping','Philip',59),(50,10,'2019-10-11 14:15:00','received','ralph68',84),(51,120,'2019-10-12 10:15:00','received','ralph68',60),(52,55,'2019-10-09 12:11:00','shipping','Sarah',119),(53,120,'2019-10-05 23:41:00','delivered','Sarah',61),(54,10,'2019-10-07 12:25:00','shipping','soJenny',85),(55,55,'2019-10-12 14:15:00','received','soJenny',120),(56,20,'2019-10-06 19:37:00','delivered','timJ',13),(57,20,'2019-10-06 20:21:00','delivered','timJ',14),(58,120,'2019-10-09 05:42:00','shipping','Vince',62),(59,10,'2019-10-12 17:42:00','received','Vince',86);
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `productName` varchar(45) NOT NULL,
  `productAvailability` int(11) NOT NULL,
  `productDescription` varchar(200) NOT NULL,
  `productPrice` int(11) NOT NULL,
  `IDteam` int(11) DEFAULT NULL,
  PRIMARY KEY (`productName`),
  KEY `FKi586b2swuk24a42yhyn3lq6ea` (`IDteam`),
  CONSTRAINT `FKi586b2swuk24a42yhyn3lq6ea` FOREIGN KEY (`IDteam`) REFERENCES `teams` (`IDteam`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES ('ISmartBand',8,'A smartband with fitness and medical tracking functions that can be connected via bluetooth to a smartphone',20,1),('ISmartLight',23,'A light bulb allowing remote control via Bluetooth',10,4),('ISmartLock',19,'A lock that can be installed into a door to allow it to be opened remotely or via a touchpad',45,2),('ISmartThermo',17,'A smart thermostat that allows remote readings and programming',55,1),('ISmartVideoBell',11,'A smart doorbell with an integrated display that initiates a video call when someone rings at the door',120,3);
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `productstock`
--

DROP TABLE IF EXISTS `productstock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `productstock` (
  `IDstock` int(11) NOT NULL,
  `product` varchar(45) NOT NULL,
  PRIMARY KEY (`IDstock`),
  KEY `FK9bbubko9mp3sjhpfaf1joc08b` (`product`),
  CONSTRAINT `FK9bbubko9mp3sjhpfaf1joc08b` FOREIGN KEY (`product`) REFERENCES `products` (`productName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `productstock`
--

LOCK TABLES `productstock` WRITE;
/*!40000 ALTER TABLE `productstock` DISABLE KEYS */;
INSERT INTO `productstock` VALUES (1,'ISmartBand'),(2,'ISmartBand'),(3,'ISmartBand'),(4,'ISmartBand'),(5,'ISmartBand'),(6,'ISmartBand'),(7,'ISmartBand'),(8,'ISmartBand'),(9,'ISmartBand'),(10,'ISmartBand'),(11,'ISmartBand'),(12,'ISmartBand'),(13,'ISmartBand'),(14,'ISmartBand'),(15,'ISmartBand'),(16,'ISmartBand'),(17,'ISmartBand'),(18,'ISmartBand'),(19,'ISmartBand'),(20,'ISmartBand'),(21,'ISmartBand'),(22,'ISmartBand'),(74,'ISmartLight'),(75,'ISmartLight'),(76,'ISmartLight'),(77,'ISmartLight'),(78,'ISmartLight'),(79,'ISmartLight'),(80,'ISmartLight'),(81,'ISmartLight'),(82,'ISmartLight'),(83,'ISmartLight'),(84,'ISmartLight'),(85,'ISmartLight'),(86,'ISmartLight'),(87,'ISmartLight'),(88,'ISmartLight'),(89,'ISmartLight'),(90,'ISmartLight'),(91,'ISmartLight'),(92,'ISmartLight'),(93,'ISmartLight'),(94,'ISmartLight'),(95,'ISmartLight'),(96,'ISmartLight'),(97,'ISmartLight'),(98,'ISmartLight'),(99,'ISmartLight'),(100,'ISmartLight'),(101,'ISmartLight'),(102,'ISmartLight'),(103,'ISmartLight'),(104,'ISmartLight'),(105,'ISmartLight'),(106,'ISmartLight'),(107,'ISmartLight'),(108,'ISmartLight'),(109,'ISmartLight'),(23,'ISmartLock'),(24,'ISmartLock'),(25,'ISmartLock'),(26,'ISmartLock'),(27,'ISmartLock'),(28,'ISmartLock'),(29,'ISmartLock'),(30,'ISmartLock'),(31,'ISmartLock'),(32,'ISmartLock'),(33,'ISmartLock'),(34,'ISmartLock'),(35,'ISmartLock'),(36,'ISmartLock'),(37,'ISmartLock'),(38,'ISmartLock'),(39,'ISmartLock'),(40,'ISmartLock'),(41,'ISmartLock'),(42,'ISmartLock'),(43,'ISmartLock'),(44,'ISmartLock'),(45,'ISmartLock'),(46,'ISmartLock'),(47,'ISmartLock'),(48,'ISmartLock'),(49,'ISmartLock'),(110,'ISmartThermo'),(111,'ISmartThermo'),(112,'ISmartThermo'),(113,'ISmartThermo'),(114,'ISmartThermo'),(115,'ISmartThermo'),(116,'ISmartThermo'),(117,'ISmartThermo'),(118,'ISmartThermo'),(119,'ISmartThermo'),(120,'ISmartThermo'),(121,'ISmartThermo'),(122,'ISmartThermo'),(123,'ISmartThermo'),(124,'ISmartThermo'),(125,'ISmartThermo'),(126,'ISmartThermo'),(127,'ISmartThermo'),(128,'ISmartThermo'),(129,'ISmartThermo'),(130,'ISmartThermo'),(131,'ISmartThermo'),(132,'ISmartThermo'),(133,'ISmartThermo'),(134,'ISmartThermo'),(135,'ISmartThermo'),(136,'ISmartThermo'),(137,'ISmartThermo'),(50,'ISmartVideoBell'),(51,'ISmartVideoBell'),(52,'ISmartVideoBell'),(53,'ISmartVideoBell'),(54,'ISmartVideoBell'),(55,'ISmartVideoBell'),(56,'ISmartVideoBell'),(57,'ISmartVideoBell'),(58,'ISmartVideoBell'),(59,'ISmartVideoBell'),(60,'ISmartVideoBell'),(61,'ISmartVideoBell'),(62,'ISmartVideoBell'),(63,'ISmartVideoBell'),(64,'ISmartVideoBell'),(65,'ISmartVideoBell'),(66,'ISmartVideoBell'),(67,'ISmartVideoBell'),(68,'ISmartVideoBell'),(69,'ISmartVideoBell'),(70,'ISmartVideoBell'),(71,'ISmartVideoBell'),(72,'ISmartVideoBell'),(73,'ISmartVideoBell');
/*!40000 ALTER TABLE `productstock` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `teams`
--

DROP TABLE IF EXISTS `teams`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `teams` (
  `IDteam` int(11) NOT NULL,
  `location` varchar(45) NOT NULL,
  PRIMARY KEY (`IDteam`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teams`
--

LOCK TABLES `teams` WRITE;
/*!40000 ALTER TABLE `teams` DISABLE KEYS */;
INSERT INTO `teams` VALUES (1,'Seattle'),(2,'Washington'),(3,'New York'),(4,'Boston');
/*!40000 ALTER TABLE `teams` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `usertype` varchar(31) NOT NULL,
  `username` varchar(45) NOT NULL,
  `mail` varchar(45) NOT NULL,
  `name` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `surname` varchar(45) NOT NULL,
  `role` varchar(45) DEFAULT NULL,
  `salary` int(11) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `IDTeam` int(11) DEFAULT NULL,
  PRIMARY KEY (`username`),
  KEY `FK7rlglehe8twx599w5so3snnou` (`IDTeam`),
  CONSTRAINT `FK7rlglehe8twx599w5so3snnou` FOREIGN KEY (`IDTeam`) REFERENCES `teams` (`IDteam`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES ('HAdministrator','admin','admin@example.com','admin','adminPassword','admin','Administrator',10000,NULL,NULL),('HCustomer','adri','adri@example.com','Adrienne','adriPassword','Andrews',NULL,NULL,'s.Maria 7',NULL),('HTeamLeader','adrian','adrian@example.com','Adrian','adrianPassword','Robinson','Engineer',3000,NULL,1),('HEmployee','agatha','agatha@example.com','Agatha','agathaPassword','Lane','Janitor',1200,NULL,NULL),('HCustomer','amy','amy@example.com','Amy','amyPassword','Lowe',NULL,NULL,'v. Ferruccio Parri 15',NULL),('HCustomer','anto83','anto83@example.com','Anthony','anto83Password','Griffin',NULL,NULL,'via DioTiSalvi, 20',NULL),('HCustomer','betty','betty@example.com','Betty','bettyPassword','Jordan',NULL,NULL,'via Rome, 5',NULL),('HTeamedEmployee','bob','bob@example.com','Robert','bobPassword','Caldwell','Technician',1800,NULL,1),('HCustomer','brad','brad@example.com','Bradley','bradPassword','Proctor',NULL,NULL,'via Marco Polo, 10',NULL),('HTeamedEmployee','christopher','christopher@example.com','Christopher','christopherPassword','Austin','Technician',1800,NULL,4),('HCustomer','Dani','Dani@example.com','Danielle','DaniPassword','Johnson',NULL,NULL,'Piazza 11 settembre',NULL),('HCustomer','david91','david91@example.com','David','david91Password','Baldwin',NULL,NULL,'Piazza napoleone, 10',NULL),('HCustomer','Dia','Dia@example.com','Diana','DiaPassword','Williams',NULL,NULL,'via Vicolo Stretto 1',NULL),('HCustomer','donny','donny@example.com','Donny','donnyPassword','Hudson',NULL,NULL,'via Bastione di gransasso, 12',NULL),('HCustomer','eth','eth@example.com','Ethan','ethPassword','Rivas',NULL,NULL,'via Piazza Dante, 13',NULL),('HCustomer','Gabriel','Gabriel@example.com','Gabriel','GabrielPassword','Foster',NULL,NULL,'via PonteCorvo, 102',NULL),('HCustomer','imanne','anne@example.com','Anne','imannePassword','Taylor',NULL,NULL,'via Roma, 105',NULL),('HTeamedEmployee','jacob','jacob@example.com','Jacob','jacobPassword','Chan','Technician',1800,NULL,1),('HTeamLeader','james','james@example.com','James','jamesPassword','Burns','Engineer',3000,NULL,2),('HCustomer','james96','james@example.com','James','james96Password','Parker',NULL,NULL,'via Accademia, 24',NULL),('HEmployee','john','john@example.com','John','johnPassword','Carter','CEO',20000,NULL,NULL),('HCustomer','john72','john72@example.com','John','john72Password','Branch',NULL,NULL,'via Largo Colombo, 205',NULL),('HTeamLeader','laura','laura@example.com','Laura','lauraPassword','Schmitt','Engineer',3000,NULL,3),('HCustomer','lisa','lisa@example.com','Lisa','lisaPassword','Smith',NULL,NULL,'piazza D\'azeglio , 1',NULL),('HCustomer','lola','lola@example.com','Karola','lolaPassword','Leal',NULL,NULL,'piazzale Verdi, 5',NULL),('HTeamedEmployee','lorry','lorry@example.com','Lorry','lorryPassword','Prenton','Technician',1800,NULL,1),('HEmployee','madeline','madeline@example.com','Madelaine','madelinePassword','Terrel','Technician',3000,NULL,NULL),('HCustomer','magicBob','magicbob@example.com','Bob','magicBobPassword','White',NULL,NULL,'via Parco della Vittoria, 60',NULL),('HTeamedEmployee','maria','maria@example.com','Maria','mariaPassword','Morrison','Technician',1800,NULL,2),('HCustomer','Marianne','Marianne@example.com','Marianne','MariannePassword','Payne',NULL,NULL,'via Firenze, 14',NULL),('HTeamedEmployee','matt','matt@example.com','Matthew','mattPassword','Duncan','Technician',1800,NULL,2),('HCustomer','matty','matty@example.com','Matthew','mattyPassword','Simon',NULL,NULL,'via Largo Lazzarino, 20',NULL),('HTeamedEmployee','michael','michael@example.com','Michael','michaelPassword','Woods','Technician',1800,NULL,3),('HCustomer','PaulPaul','paul@example.com','Paul','PaulPaulPassword','Scott',NULL,NULL,'via Napolo 2',NULL),('HCustomer','Philip','Philip@example.com','Philip','PhilipPassword','Sanders',NULL,NULL,'via Trenta 5',NULL),('HCustomer','ralph68','ralph68@example.com','Ralph','ralph68Password','Mayo',NULL,NULL,'via Palerma  15',NULL),('HEmployee','randi','randi@example.com','Randi','randiPassword','Dickson','Janitor',1200,NULL,NULL),('HEmployee','richard','richard@example.com','Richard','richardPassword','Johnson','CTO',15000,NULL,NULL),('HTeamLeader','rob','rob@example.com','Robert','robPassword','Kelley','Engineer',3000,NULL,4),('HTeamedEmployee','roy','roy@example.com','Roy','royPassword','Smith','Technician',1800,NULL,2),('HTeamedEmployee','ruth','ruth@example.com','Ruth','ruthPassword','Spencer','Technician',1800,NULL,3),('HCustomer','Sarah','Sarah@example.com','Sarah','SarahPassword','Washington',NULL,NULL,'via Garibaldi 18',NULL),('HTeamedEmployee','seli','seli@example.com','Selinda','seliPassword','Reyna','Technician',1800,NULL,4),('HCustomer','soJenny','jenny@example.com','Jenny','soJennyPassword','Lingard',NULL,NULL,'via Novara 42',NULL),('HTeamedEmployee','susy','susy@example.com','Susan','susyPassword','Parks','Technician',2000,NULL,4),('HCustomer','timJ','tim@example.com','Tim','timJPassword','Jones',NULL,NULL,'via Rossellini 43',NULL),('HCustomer','Vince','Vince@example.com','Vincent','VincePassword','Ross',NULL,NULL,'via Avane 2',NULL);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'taskone'
--

--
-- Dumping routines for database 'taskone'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-11-16 12:38:49

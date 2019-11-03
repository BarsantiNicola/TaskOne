-- MySQL dump 10.13  Distrib 8.0.18, for Win64 (x86_64)
--
-- Host: localhost    Database: taskone
-- ------------------------------------------------------
-- Server version	8.0.18

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
INSERT INTO `hibernate_sequence` VALUES (60);
/*!40000 ALTER TABLE `hibernate_sequence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `members`
--

DROP TABLE IF EXISTS `members`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `members` (
  `teamLeader` varchar(45) NOT NULL,
  `IDemployee` varchar(45) NOT NULL,
  UNIQUE KEY `UK_pds9ags8lp16yus389sgotgqe` (`IDemployee`),
  KEY `FK2pr4rvwm5mxxfxajaqpckb51p` (`teamLeader`),
  CONSTRAINT `FK2pr4rvwm5mxxfxajaqpckb51p` FOREIGN KEY (`teamLeader`) REFERENCES `teams` (`teamLeader`),
  CONSTRAINT `FKm7cy4fvdi6pnxqfv378yu79fy` FOREIGN KEY (`IDemployee`) REFERENCES `users` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `members`
--

LOCK TABLES `members` WRITE;
/*!40000 ALTER TABLE `members` DISABLE KEYS */;
INSERT INTO `members` VALUES ('adrian','bob'),('adrian','jacob'),('adrian','lorry'),('james','maria'),('james','matt'),('james','roy'),('laura','michael'),('laura','ruth'),('rob','christopher'),('rob','seli'),('rob','susy');
/*!40000 ALTER TABLE `members` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `myorders`
--

DROP TABLE IF EXISTS `myorders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `myorders` (
  `username` varchar(45) NOT NULL,
  `IDorder` int(11) NOT NULL,
  UNIQUE KEY `UK_rmltp3p40s9nwv1tjii2vmhfp` (`IDorder`),
  KEY `FK4q1rc3hw9t6x26w84h2ls6jq9` (`username`),
  CONSTRAINT `FK4q1rc3hw9t6x26w84h2ls6jq9` FOREIGN KEY (`username`) REFERENCES `users` (`username`),
  CONSTRAINT `FK5x0oa7egp45y94c9gvxjade8b` FOREIGN KEY (`IDorder`) REFERENCES `orders` (`IDorder`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `myorders`
--

LOCK TABLES `myorders` WRITE;
/*!40000 ALTER TABLE `myorders` DISABLE KEYS */;
INSERT INTO `myorders` VALUES ('adri',1),('adri',2),('adri',3),('amy',4),('amy',5),('amy',6),('anto83',7),('anto83',8),('anto83',9),('betty',10),('betty',11),('betty',12),('brad',13),('brad',14),('brad',15),('Dani',16),('Dani',17),('Dani',18),('david91',19),('david91',20),('david91',21),('Dia',22),('Dia',23),('donny',24),('donny',25),('eth',26),('eth',27),('Gabriel',28),('Gabriel',29),('imanne',30),('imanne',31),('james96',32),('james96',33),('john72',34),('john72',35),('lisa',36),('lisa',37),('lola',38),('lola',39),('magicBob',40),('magicBob',41),('Marianne',42),('Marianne',43),('matty',44),('matty',45),('PaulPaul',46),('PaulPaul',47),('Philip',48),('Philip',49),('ralph68',50),('ralph68',51),('Sarah',52),('Sarah',53),('soJenny',54),('soJenny',55),('timJ',56),('timJ',57),('Vince',58),('Vince',59);
/*!40000 ALTER TABLE `myorders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `myteam`
--

DROP TABLE IF EXISTS `myteam`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `myteam` (
  `myTeam_teamLeader` varchar(45) DEFAULT NULL,
  `username` varchar(45) NOT NULL,
  PRIMARY KEY (`username`),
  KEY `FKfhrklfle2hw9ws7vpr406rbd3` (`myTeam_teamLeader`),
  CONSTRAINT `FKfhrklfle2hw9ws7vpr406rbd3` FOREIGN KEY (`myTeam_teamLeader`) REFERENCES `teams` (`teamLeader`),
  CONSTRAINT `FKgrvxa8qy4l1ao1s1frfivpus6` FOREIGN KEY (`username`) REFERENCES `users` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `myteam`
--

LOCK TABLES `myteam` WRITE;
/*!40000 ALTER TABLE `myteam` DISABLE KEYS */;
INSERT INTO `myteam` VALUES ('adrian','adrian'),('james','james'),('laura','laura'),('rob','rob');
/*!40000 ALTER TABLE `myteam` ENABLE KEYS */;
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
  `productStock` int(11) DEFAULT NULL,
  PRIMARY KEY (`IDorder`),
  KEY `FK7ho9v3n7jpgryad7mo7fe01sm` (`productStock`),
  CONSTRAINT `FK7ho9v3n7jpgryad7mo7fe01sm` FOREIGN KEY (`productStock`) REFERENCES `productstock` (`IDstock`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,10,'2019-09-08 11:12:00','delivered',76),(2,55,'2019-09-11 17:54:00','delivered',111),(3,20,'2019-09-06 11:12:00','delivered',4),(4,45,'2019-09-11 18:11:00','delivered',25),(5,55,'2019-09-13 10:48:00','delivered',112),(6,120,'2019-09-07 12:27:00','delivered',51),(7,10,'2019-09-10 14:15:00','delivered',77),(8,10,'2019-09-14 22:15:00','delivered',78),(9,20,'2019-09-05 15:18:00','delivered',1),(10,20,'2019-09-08 12:00:00','delivered',2),(11,45,'2019-09-09 09:15:00','delivered',23),(12,120,'2019-09-06 18:15:00','delivered',50),(13,10,'2019-09-07 02:47:00','delivered',74),(14,10,'2019-09-08 07:12:00','delivered',75),(15,55,'2019-09-14 19:21:00','delivered',110),(16,20,'2019-09-07 11:05:00','delivered',3),(17,45,'2019-09-08 14:56:00','delivered',24),(18,20,'2019-09-15 13:29:00','delivered',5),(19,45,'2019-09-20 15:14:00','delivered',26),(20,120,'2019-09-21 19:51:00','delivered',52),(21,55,'2019-09-18 23:14:00','delivered',113),(22,20,'2019-09-24 17:12:00','delivered',6),(23,120,'2019-09-17 08:15:00','delivered',53),(24,10,'2019-09-22 13:22:00','delivered',79),(25,55,'2019-09-22 15:34:00','delivered',114),(26,20,'2019-10-12 23:55:00','received',7),(27,45,'2019-10-01 17:12:00','shipping',27),(28,120,'2019-09-28 07:52:00','delivered',54),(29,10,'2019-10-05 22:14:00','shipping',80),(30,55,'2019-09-30 18:25:00','delivered',115),(31,45,'2019-10-01 11:14:00','delivered',28),(32,120,'2019-10-03 21:32:00','delivered',55),(33,55,'2019-10-10 20:19:00','received',116),(34,20,'2019-10-03 05:22:00','delivered',8),(35,45,'2019-10-12 08:23:00','received',29),(36,120,'2019-10-12 08:24:00','received',56),(37,10,'2019-10-05 19:47:00','shipping',81),(38,20,'2019-10-04 03:32:00','delivered',9),(39,10,'2019-10-09 11:36:00','shipping',82),(40,55,'2019-10-08 16:41:00','shipping',117),(41,20,'2019-10-08 11:32:00','shipping',10),(42,120,'2019-10-09 18:15:00','shipping',57),(43,10,'2019-10-06 15:25:00','delivered',83),(44,20,'2019-10-08 22:23:00','shipping',11),(45,45,'2019-10-11 08:45:00','received',30),(46,120,'2019-10-08 23:42:00','shipping',58),(47,55,'2019-10-11 06:37:00','received',118),(48,20,'2019-10-08 15:24:00','delivered',12),(49,120,'2019-10-09 17:55:00','shipping',59),(50,10,'2019-10-11 14:15:00','received',84),(51,120,'2019-10-12 10:15:00','received',60),(52,55,'2019-10-09 12:11:00','shipping',119),(53,120,'2019-10-05 23:41:00','delivered',61),(54,10,'2019-10-07 12:25:00','shipping',85),(55,55,'2019-10-12 14:15:00','received',120),(56,20,'2019-10-06 19:37:00','delivered',13),(57,20,'2019-10-06 20:21:00','delivered',14),(58,120,'2019-10-09 05:42:00','shipping',62),(59,10,'2019-10-12 17:42:00','received',86);
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
  `IDteam` int(11) NOT NULL,
  `productAvailability` int(11) NOT NULL,
  `productDescription` varchar(200) NOT NULL,
  `productPrice` int(11) NOT NULL,
  `productType` int(11) NOT NULL,
  PRIMARY KEY (`productName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES ('ISmartBand',1,8,'A smartband with fitness and medical tracking functions that can be connected via bluetooth to a smartphone',20,1),('ISmartLight',4,23,'A light bulb allowing remote control via Bluetooth',10,4),('ISmartLock',2,19,'A lock that can be installed into a door to allow it to be opened remotely or via a touchpad',45,2),('ISmartThermo',1,17,'A smart thermostat that allows remote readings and programming',55,5),('ISmartVideoBell',3,11,'A smart doorbell with an integrated display that initiates a video call when someone rings at the door',120,3);
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
  `product` varchar(45) DEFAULT NULL,
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
-- Table structure for table `teamproducts`
--

DROP TABLE IF EXISTS `teamproducts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `teamproducts` (
  `teamLeader` varchar(45) NOT NULL,
  `productName` varchar(45) NOT NULL,
  UNIQUE KEY `UK_fbb9yjrm731mqtw76fam2o5hn` (`productName`),
  KEY `FK3q5ppa767s2g9djsva8mmaycy` (`teamLeader`),
  CONSTRAINT `FK3q5ppa767s2g9djsva8mmaycy` FOREIGN KEY (`teamLeader`) REFERENCES `teams` (`teamLeader`),
  CONSTRAINT `FK93jsf5itykuajqsa2309fxwj4` FOREIGN KEY (`productName`) REFERENCES `products` (`productName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teamproducts`
--

LOCK TABLES `teamproducts` WRITE;
/*!40000 ALTER TABLE `teamproducts` DISABLE KEYS */;
INSERT INTO `teamproducts` VALUES ('adrian','ISmartBand'),('adrian','ISmartThermo'),('james','ISmartLock'),('laura','ISmartVideoBell'),('rob','ISmartLight');
/*!40000 ALTER TABLE `teamproducts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `teams`
--

DROP TABLE IF EXISTS `teams`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `teams` (
  `teamLeader` varchar(45) NOT NULL,
  `location` varchar(45) NOT NULL,
  PRIMARY KEY (`teamLeader`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teams`
--

LOCK TABLES `teams` WRITE;
/*!40000 ALTER TABLE `teams` DISABLE KEYS */;
INSERT INTO `teams` VALUES ('adrian','Seattle'),('james','Washington'),('laura','New York'),('rob','Boston');
/*!40000 ALTER TABLE `teams` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `DTYPE` varchar(31) NOT NULL,
  `username` varchar(45) NOT NULL,
  `mail` varchar(45) NOT NULL,
  `name` varchar(45) NOT NULL,
  `surname` varchar(45) NOT NULL,
  `role` varchar(45) DEFAULT NULL,
  `salary` int(11) DEFAULT NULL,
  `IDteam` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES ('HAdministrator','admin','admin@example.com','admin','admin','Administrator',10000,NULL),('HCustomer','adri','adri@example.com','Adrienne','Andrews',NULL,NULL,NULL),('HHeadDepartment','adrian','adrian@example.com','Adrian','Robinson','Engineer',3000,NULL),('HEmployee','agatha','agatha@example.com','Agatha','Lane','Janitor',1200,NULL),('HCustomer','amy','amy@example.com','Amy','Lowe',NULL,NULL,NULL),('HCustomer','anto83','anto83@example.com','Anthony','Griffin',NULL,NULL,NULL),('HCustomer','betty','betty@example.com','Betty','Jordan',NULL,NULL,NULL),('HTeamedEmployee','bob','bob@example.com','Robert','Caldwell','Technician',1800,'adrian'),('HCustomer','brad','brad@example.com','Bradley','Proctor',NULL,NULL,NULL),('HTeamedEmployee','christopher','christopher@example.com','Christopher','Austin','Technician',1800,'rob'),('HCustomer','Dani','Dani@example.com','Danielle','Johnson',NULL,NULL,NULL),('HCustomer','david91','david91@example.com','David','Baldwin',NULL,NULL,NULL),('HCustomer','Dia','Dia@example.com','Diana','Williams',NULL,NULL,NULL),('HCustomer','donny','donny@example.com','Donny','Hudson',NULL,NULL,NULL),('HCustomer','eth','eth@example.com','Ethan','Rivas',NULL,NULL,NULL),('HCustomer','Gabriel','Gabriel@example.com','Gabriel','Foster',NULL,NULL,NULL),('HCustomer','imanne','anne@example.com','Anne','Taylor',NULL,NULL,NULL),('HTeamedEmployee','jacob','jacob@example.com','Jacob','Chan','Technician',1800,'adrian'),('HHeadDepartment','james','james@example.com','James','Burns','Engineer',3000,NULL),('HCustomer','james96','james@example.com','James','Parker',NULL,NULL,NULL),('HEmployee','john','john@example.com','John','Carter','CEO',20000,NULL),('HCustomer','john72','john72@example.com','John','Branch',NULL,NULL,NULL),('HHeadDepartment','laura','laura@example.com','Laura','Schmitt','Engineer',3000,NULL),('HCustomer','lisa','lisa@example.com','Lisa','Smith',NULL,NULL,NULL),('HCustomer','lola','lola@example.com','Karola','Leal',NULL,NULL,NULL),('HTeamedEmployee','lorry','lorry@example.com','Lorry','Prenton','Technician',1800,'adrian'),('HEmployee','madeline','madeline@example.com','Madelaine','Terrel','Technician',1800,NULL),('HCustomer','magicBob','magicbob@example.com','Bob','White',NULL,NULL,NULL),('HTeamedEmployee','maria','maria@example.com','Maria','Morrison','Technician',1800,'james'),('HCustomer','Marianne','Marianne@example.com','Marianne','Payne',NULL,NULL,NULL),('HTeamedEmployee','matt','matt@example.com','Matthew','Duncan','Technician',1800,'james'),('HCustomer','matty','matty@example.com','Matthew','Simon',NULL,NULL,NULL),('HTeamedEmployee','michael','michael@example.com','Michael','Woods','Technician',1800,'laura'),('HCustomer','PaulPaul','paul@example.com','Paul','Scott',NULL,NULL,NULL),('HCustomer','Philip','Philip@example.com','Philip','Sanders',NULL,NULL,NULL),('HCustomer','ralph68','ralph68@example.com','Ralph','Mayo',NULL,NULL,NULL),('HEmployee','randi','randi@example.com','Randi','Dickson','Janitor',1200,NULL),('HEmployee','richard','richard@example.com','Richard','Johnson','CTO',15000,NULL),('HHeadDepartment','rob','rob@example.com','Robert','Kelley','Engineer',3000,NULL),('HTeamedEmployee','roy','roy@example.com','Roy','Smith','Technician',1800,'james'),('HTeamedEmployee','ruth','ruth@example.com','Ruth','Spencer','Technician',1800,'laura'),('HCustomer','Sarah','Sarah@example.com','Sarah','Washington',NULL,NULL,NULL),('HTeamedEmployee','seli','seli@example.com','Selinda','Reyna','Technician',1800,'rob'),('HCustomer','soJenny','jenny@example.com','Jenny','Lingard',NULL,NULL,NULL),('HTeamedEmployee','susy','susy@example.com','Susan','Parks','Technician',2000,'rob'),('HCustomer','timJ','tim@example.com','Tim','Jones',NULL,NULL,NULL),('HCustomer','Vince','Vince@example.com','Vincent','Ross',NULL,NULL,NULL);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-11-03  2:12:47

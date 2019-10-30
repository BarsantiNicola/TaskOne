-- MySQL dump 10.13  Distrib 8.0.17, for Win64 (x86_64)
--
-- Host: localhost    Database: innovativesolutionsdb_proposal
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
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer` (
  `IDcustomer` varchar(45) NOT NULL,
  `address` varchar(45) NOT NULL,
  PRIMARY KEY (`IDcustomer`),
  CONSTRAINT `customerCostraintUser` FOREIGN KEY (`IDcustomer`) REFERENCES `user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES ('adri','Boston'),('amy','New York'),('anto83','San Francisco'),('betty','Dallas'),('brad','New Orleans'),('Dani','Greenville'),('david91','Phoenix'),('Dia','Florence'),('donny','Douglas'),('eth','Mesa'),('Gabriel','Pine Bluff'),('imanne','Beverly Hills'),('james96','New York'),('laura','Hollywood'),('lisa','Boston'),('lola','Washington'),('magicBob','Richmond'),('Marianne','San Jose'),('matty','San Francisco'),('PaulPaul','Manchester'),('Philip','Miami'),('ralph68','Chicago'),('Sarah','New York'),('soJenny','Boston'),('timJ','Washington'),('Vince','Panama City');
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee` (
  `IDemployee` varchar(45) NOT NULL,
  `salary` int(11) NOT NULL,
  `role` varchar(45) NOT NULL,
  `team` int(11) DEFAULT NULL,
  PRIMARY KEY (`IDemployee`),
  KEY `teamCostraint_idx` (`team`),
  CONSTRAINT `teamCostraint` FOREIGN KEY (`team`) REFERENCES `team` (`IDteam`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `usernameCostraint` FOREIGN KEY (`IDemployee`) REFERENCES `user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee`
--

LOCK TABLES `employee` WRITE;
/*!40000 ALTER TABLE `employee` DISABLE KEYS */;
INSERT INTO `employee` VALUES ('admin',10000,'Administrator',NULL),('adrian',3000,'Engineer',1),('agatha',1200,'Janitor',NULL),('bob',1800,'Technician',1),('christopher',1800,'Technician',4),('jacob',1800,'Technician',1),('james',3000,'Engineer',2),('john',20000,'CEO',NULL),('laura',3000,'Engineer',3),('lorry',1800,'Technician',1),('madeline',1800,'Technician',NULL),('maria',1800,'Technician',2),('matt',1800,'Technician',2),('michael',1800,'Technician',3),('randi',1200,'Janitor',NULL),('richard',15000,'CTO',NULL),('rob',3000,'Engineer',4),('roy',1800,'Technician',2),('ruth',1800,'Technician',3),('seli',1800,'Technician',4),('susy',1800,'Technician',4);
/*!40000 ALTER TABLE `employee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `IDorder` int(11) NOT NULL,
  `purchaseDate` datetime NOT NULL,
  `price` int(11) NOT NULL DEFAULT '0',
  `status` varchar(45) NOT NULL,
  `customer` varchar(45) DEFAULT NULL,
  `product` int(11) DEFAULT NULL,
  PRIMARY KEY (`IDorder`),
  KEY `customerConstraint_idx` (`customer`),
  KEY `productConstraint_idx` (`product`),
  CONSTRAINT `customerConstraint` FOREIGN KEY (`customer`) REFERENCES `customer` (`IDcustomer`),
  CONSTRAINT `productConstraint` FOREIGN KEY (`product`) REFERENCES `product_stock` (`IDproduct`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,'2019-09-05 15:18:00',20,'delivered','betty',1),(2,'2019-09-06 11:12:00',20,'delivered','amy',2),(3,'2019-09-06 18:15:00',120,'delivered','eth',50),(4,'2019-09-07 02:47:00',10,'delivered','eth',74),(5,'2019-09-07 11:05:00',20,'delivered','Gabriel',3),(6,'2019-09-07 12:27:00',120,'delivered','anto83',51),(7,'2019-09-08 07:12:00',10,'delivered','eth',75),(8,'2019-09-08 11:12:00',10,'delivered','betty',4),(9,'2019-09-08 12:00:00',20,'delivered','Gabriel',23),(10,'2019-09-08 14:56:00',45,'delivered','betty',24),(11,'2019-09-09 09:15:00',45,'delivered','anto83',76),(12,'2019-09-10 14:15:00',10,'delivered','amy',25),(13,'2019-09-11 17:54:00',55,'delivered','amy',110),(14,'2019-09-11 18:11:00',45,'delivered','eth',111),(15,'2019-09-13 10:48:00',55,'delivered','anto83',77),(16,'2019-09-14 19:21:00',55,'delivered','imanne',5),(17,'2019-09-14 22:15:00',10,'delivered','james96',52),(18,'2019-09-15 13:29:00',20,'delivered','imanne',112),(19,'2019-09-17 08:15:00',120,'delivered','imanne',26),(20,'2019-09-18 23:14:00',55,'delivered','imanne',53),(21,'2019-09-20 15:14:00',45,'delivered','james96',78),(22,'2019-09-21 19:51:00',120,'delivered','james96',113),(23,'2019-09-22 13:22:00',10,'delivered','james96',6),(24,'2019-09-22 15:34:00',55,'delivered','laura',54),(25,'2019-09-24 17:12:00',20,'delivered','laura',114),(26,'2019-09-28 07:52:00',120,'delivered','lisa',27),(27,'2019-09-30 18:25:00',55,'delivered','laura',28),(28,'2019-10-01 11:14:00',45,'delivered','lola',7),(29,'2019-10-01 17:12:00',45,'shipping','lisa',79),(30,'2019-10-03 05:22:00',20,'delivered','Marianne',8),(31,'2019-10-03 21:32:00',120,'delivered','lola',80),(32,'2019-10-04 03:32:00',20,'delivered','laura',81),(33,'2019-10-05 19:47:00',10,'shipping','timJ',55),(34,'2019-10-05 22:14:00',10,'shipping','matty',82),(35,'2019-10-05 23:41:00',120,'delivered','Vince',9),(36,'2019-10-06 15:25:00',10,'delivered','Vince',10),(37,'2019-10-06 19:37:00',20,'delivered','timJ',83),(38,'2019-10-06 20:21:00',20,'delivered','matty',11),(39,'2019-10-07 12:25:00',10,'shipping','ralph68',12),(40,'2019-10-08 11:32:00',20,'shipping','Marianne',115),(41,'2019-10-08 15:24:00',20,'delivered','Philip',13),(42,'2019-10-08 16:41:00',55,'shipping','Philip',14),(43,'2019-10-08 22:23:00',20,'shipping','Vince',56),(44,'2019-10-08 23:42:00',120,'shipping','Marianne',57),(45,'2019-10-09 05:42:00',120,'shipping','Sarah',84),(46,'2019-10-09 11:36:00',10,'shipping','ralph68',116),(47,'2019-10-09 12:11:00',55,'shipping','matty',58),(48,'2019-10-09 17:55:00',120,'shipping','lisa',59),(49,'2019-10-09 18:15:00',120,'shipping','Philip',117),(50,'2019-10-10 20:19:00',55,'received','Philip',118),(51,'2019-10-11 06:37:00',55,'received','ralph68',29),(52,'2019-10-11 08:45:00',45,'received','lola',85),(53,'2019-10-11 14:15:00',10,'received','lola',30),(54,'2019-10-12 08:23:00',45,'received','Sarah',60),(55,'2019-10-12 08:24:00',120,'received','timJ',61),(56,'2019-10-12 10:15:00',120,'received','Vince',119),(57,'2019-10-12 14:15:00',55,'received','laura',86),(58,'2019-10-12 17:42:00',10,'received','Gabriel',15),(59,'2019-10-12 23:55:00',20,'received','anto83',31);
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `orders_AFTER_INSERT` AFTER INSERT ON `orders` FOR EACH ROW BEGIN
	UPDATE product P SET P.productAvailability = P.productAvailability - 1 WHERE P.productType = productType;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `productName` varchar(45) NOT NULL,
  `productPrice` int(11) NOT NULL,
  `productDescription` varchar(200) NOT NULL,
  `productAvailability` int(11) NOT NULL DEFAULT '0',
  `team` int(11) NOT NULL,
  PRIMARY KEY (`productName`),
  KEY `teamConstraint_idx` (`team`),
  CONSTRAINT `teamConstraint` FOREIGN KEY (`team`) REFERENCES `team` (`IDteam`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES ('ISmartBand',20,'A smartband with fitness and medical tracking functions that can be connected via bluetooth to a smartphone',7,1),('ISmartLight',10,'A light bulb allowing remote control via Bluetooth',23,4),('ISmartLock',45,'A lock that can be installed into a door to allow it to be opened remotely or via a touchpad',19,2),('ISmartThermo',55,'A smart thermostat that allows remote readings and programming',18,1),('ISmartVideoBell',120,'A smart doorbell with an integrated display that initiates a video call when someone rings at the door',12,3);
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `product_AFTER_UPDATE` AFTER UPDATE ON `product` FOR EACH ROW BEGIN
IF NEW.productAvailability > OLD.productAvailability THEN
	INSERT INTO product_stock VALUES (NULL,NEW.productName);
END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `product_stock`
--

DROP TABLE IF EXISTS `product_stock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_stock` (
  `IDproduct` int(11) NOT NULL AUTO_INCREMENT,
  `productName` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`IDproduct`),
  KEY `productNameConstraint_idx` (`productName`),
  CONSTRAINT `productNameConstraint` FOREIGN KEY (`productName`) REFERENCES `product` (`productName`)
) ENGINE=InnoDB AUTO_INCREMENT=142 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_stock`
--

LOCK TABLES `product_stock` WRITE;
/*!40000 ALTER TABLE `product_stock` DISABLE KEYS */;
INSERT INTO `product_stock` VALUES (1,'ISmartBand'),(2,'ISmartBand'),(3,'ISmartBand'),(4,'ISmartBand'),(5,'ISmartBand'),(6,'ISmartBand'),(7,'ISmartBand'),(8,'ISmartBand'),(9,'ISmartBand'),(10,'ISmartBand'),(11,'ISmartBand'),(12,'ISmartBand'),(13,'ISmartBand'),(14,'ISmartBand'),(15,'ISmartBand'),(16,'ISmartBand'),(17,'ISmartBand'),(18,'ISmartBand'),(19,'ISmartBand'),(20,'ISmartBand'),(21,'ISmartBand'),(22,'ISmartBand'),(74,'ISmartLight'),(75,'ISmartLight'),(76,'ISmartLight'),(77,'ISmartLight'),(78,'ISmartLight'),(79,'ISmartLight'),(80,'ISmartLight'),(81,'ISmartLight'),(82,'ISmartLight'),(83,'ISmartLight'),(84,'ISmartLight'),(85,'ISmartLight'),(86,'ISmartLight'),(87,'ISmartLight'),(88,'ISmartLight'),(89,'ISmartLight'),(90,'ISmartLight'),(91,'ISmartLight'),(92,'ISmartLight'),(93,'ISmartLight'),(94,'ISmartLight'),(95,'ISmartLight'),(96,'ISmartLight'),(97,'ISmartLight'),(98,'ISmartLight'),(99,'ISmartLight'),(100,'ISmartLight'),(101,'ISmartLight'),(102,'ISmartLight'),(103,'ISmartLight'),(104,'ISmartLight'),(105,'ISmartLight'),(106,'ISmartLight'),(107,'ISmartLight'),(108,'ISmartLight'),(109,'ISmartLight'),(23,'ISmartLock'),(24,'ISmartLock'),(25,'ISmartLock'),(26,'ISmartLock'),(27,'ISmartLock'),(28,'ISmartLock'),(29,'ISmartLock'),(30,'ISmartLock'),(31,'ISmartLock'),(32,'ISmartLock'),(33,'ISmartLock'),(34,'ISmartLock'),(35,'ISmartLock'),(36,'ISmartLock'),(37,'ISmartLock'),(38,'ISmartLock'),(39,'ISmartLock'),(40,'ISmartLock'),(41,'ISmartLock'),(42,'ISmartLock'),(43,'ISmartLock'),(44,'ISmartLock'),(45,'ISmartLock'),(46,'ISmartLock'),(47,'ISmartLock'),(48,'ISmartLock'),(49,'ISmartLock'),(140,'ISmartLock'),(110,'ISmartThermo'),(111,'ISmartThermo'),(112,'ISmartThermo'),(113,'ISmartThermo'),(114,'ISmartThermo'),(115,'ISmartThermo'),(116,'ISmartThermo'),(117,'ISmartThermo'),(118,'ISmartThermo'),(119,'ISmartThermo'),(120,'ISmartThermo'),(121,'ISmartThermo'),(122,'ISmartThermo'),(123,'ISmartThermo'),(124,'ISmartThermo'),(125,'ISmartThermo'),(126,'ISmartThermo'),(127,'ISmartThermo'),(128,'ISmartThermo'),(129,'ISmartThermo'),(130,'ISmartThermo'),(131,'ISmartThermo'),(132,'ISmartThermo'),(133,'ISmartThermo'),(134,'ISmartThermo'),(135,'ISmartThermo'),(136,'ISmartThermo'),(137,'ISmartThermo'),(50,'ISmartVideoBell'),(51,'ISmartVideoBell'),(52,'ISmartVideoBell'),(53,'ISmartVideoBell'),(54,'ISmartVideoBell'),(55,'ISmartVideoBell'),(56,'ISmartVideoBell'),(57,'ISmartVideoBell'),(58,'ISmartVideoBell'),(59,'ISmartVideoBell'),(60,'ISmartVideoBell'),(61,'ISmartVideoBell'),(62,'ISmartVideoBell'),(63,'ISmartVideoBell'),(64,'ISmartVideoBell'),(65,'ISmartVideoBell'),(66,'ISmartVideoBell'),(67,'ISmartVideoBell'),(68,'ISmartVideoBell'),(69,'ISmartVideoBell'),(70,'ISmartVideoBell'),(71,'ISmartVideoBell'),(72,'ISmartVideoBell'),(73,'ISmartVideoBell');
/*!40000 ALTER TABLE `product_stock` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `team`
--

DROP TABLE IF EXISTS `team`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `team` (
  `IDteam` int(11) NOT NULL AUTO_INCREMENT,
  `location` varchar(45) NOT NULL,
  `teamLeader` varchar(45) NOT NULL,
  PRIMARY KEY (`IDteam`),
  KEY `leaderCostraint_idx` (`teamLeader`),
  CONSTRAINT `leaderCostraint` FOREIGN KEY (`teamLeader`) REFERENCES `employee` (`IDemployee`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `team`
--

LOCK TABLES `team` WRITE;
/*!40000 ALTER TABLE `team` DISABLE KEYS */;
INSERT INTO `team` VALUES (1,'Seattle','adrian'),(2,'Washington','james'),(3,'New York','laura'),(4,'Boston','rob');
/*!40000 ALTER TABLE `team` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `username` varchar(45) NOT NULL,
  `name` varchar(45) NOT NULL,
  `surname` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `mail` varchar(45) NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('admin','admin','admin','adminPassword','admin@example.com'),('adri','Adrienne','Andrews','adriPassword','adri@example.com'),('adrian','Adrian','Robinson','adrianPassword','adrian@example.com'),('agatha','Agatha','Lane','agathaPassword','agatha@example.com'),('amy','Amy','Lowe','amyPassword','amy@example.com'),('anto83','Anthony','Griffin','anto83Password','anto83@example.com'),('betty','Betty','Jordan','bettyPassword','betty@example.com'),('bob','Robert','Caldwell','bobPassword','bob@example.com'),('brad','Bradley','Proctor','bradPassword','brad@example.com'),('christopher','Christopher','Austin','christopherPassword','christopher@example.com'),('Dani','Danielle','Johnson','DaniPassword','Dani@example.com'),('david91','David','Baldwin','david91Password','david91@example.com'),('Dia','Diana','Williams','DiaPassword','Dia@example.com'),('donny','Donny','Hudson','donnyPassword','donny@example.com'),('eth','Ethan','Rivas','ethPassword','eth@example.com'),('Gabriel','Gabriel','Foster','GabrielPassword','Gabriel@example.com'),('imanne','Anne','Taylor','imannePassword','anne@example.com'),('jacob','Jacob','Chan','jacobPassword','jacob@example.com'),('james','James','Burns','jamesPassword','james@example.com'),('james96','James','Parker','james96Password','james@example.com'),('john','John','Carter','johnPassword','john@example.com'),('john72','John','Branch','john72Password','john72@example.com'),('laura','Laura','Schmitt','lauraPassword','laura@example.com'),('lisa','Lisa','Smith','lisaPassword','lisa@example.com'),('lola','Karola','Leal','lolaPassword','lola@example.com'),('lorry','Lorry','Prenton','lorryPassword','lorry@example.com'),('madeline','Madelaine','Terrel','madelinePassword','madeline@example.com'),('magicBob','Bob','White','magicBobPassword','magicbob@example.com'),('maria','Maria','Morrison','mariaPassword','maria@example.com'),('Marianne','Marianne','Payne','MariannePassword','Marianne@example.com'),('matt','Matthew','Duncan','mattPassword','matt@example.com'),('matty','Matthew','Simon','mattyPassword','matty@example.com'),('michael','Michael','Woods','michaelPassword','michael@example.com'),('PaulPaul','Paul','Scott','PaulPaulPassword','paul@example.com'),('Philip','Philip','Sanders','PhilipPassword','Philip@example.com'),('ralph68','Ralph','Mayo','ralph68Password','ralph68@example.com'),('randi','Randi','Dickson','randiPassword','randi@example.com'),('richard','Richard','Johnson','richardPassword','richard@example.com'),('rob','Robert','Kelley','robPassword','rob@example.com'),('roy','Roy','Smith','royPassword','roy@example.com'),('ruth','Ruth','Spencer','ruthPassword','ruth@example.com'),('Sarah','Sarah','Washington','SarahPassword','Sarah@example.com'),('seli','Selinda','Reyna','seliPassword','seli@example.com'),('soJenny','Jenny','Lingard','soJennyPassword','jenny@example.com'),('susy','Susan','Parks','susyPassword','susy@example.com'),('timJ','Tim','Jones','timJPassword','tim@example.com'),('Vince','Vincent','Ross','VincePassword','Vince@example.com');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'innovativesolutionsdb_proposal'
--

--
-- Dumping routines for database 'innovativesolutionsdb_proposal'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-10-19 14:03:25

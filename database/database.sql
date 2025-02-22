CREATE DATABASE  IF NOT EXISTS `traineeship_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `traineeship_db`;
-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: localhost    Database: traineeship_db
-- ------------------------------------------------------
-- Server version	8.0.37

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
-- Table structure for table `companies`
--

DROP TABLE IF EXISTS `companies`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `companies` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `company_name` varchar(100) NOT NULL,
  `location` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_companies_user` (`user_id`),
  CONSTRAINT `fk_companies_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `companies`
--

LOCK TABLES `companies` WRITE;
/*!40000 ALTER TABLE `companies` DISABLE KEYS */;
/*!40000 ALTER TABLE `companies` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `evaluations`
--

DROP TABLE IF EXISTS `evaluations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `evaluations` (
  `id` int NOT NULL AUTO_INCREMENT,
  `traineeship_id` int NOT NULL,
  `evaluator_role` enum('COMPANY','PROFESSOR') NOT NULL,
  `motivation_rating` int DEFAULT NULL,
  `effectiveness_rating` int DEFAULT NULL,
  `efficiency_rating` int DEFAULT NULL,
  `company_facilities_rating` int DEFAULT NULL,
  `company_guidance_rating` int DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_evaluations_traineeship` (`traineeship_id`),
  CONSTRAINT `fk_evaluations_traineeship` FOREIGN KEY (`traineeship_id`) REFERENCES `traineeships` (`id`) ON DELETE CASCADE,
  CONSTRAINT `evaluations_chk_1` CHECK ((`motivation_rating` between 1 and 5)),
  CONSTRAINT `evaluations_chk_2` CHECK ((`effectiveness_rating` between 1 and 5)),
  CONSTRAINT `evaluations_chk_3` CHECK ((`efficiency_rating` between 1 and 5)),
  CONSTRAINT `evaluations_chk_4` CHECK ((`company_facilities_rating` between 1 and 5)),
  CONSTRAINT `evaluations_chk_5` CHECK ((`company_guidance_rating` between 1 and 5))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `evaluations`
--

LOCK TABLES `evaluations` WRITE;
/*!40000 ALTER TABLE `evaluations` DISABLE KEYS */;
/*!40000 ALTER TABLE `evaluations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `professors`
--

DROP TABLE IF EXISTS `professors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `professors` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `full_name` varchar(100) NOT NULL,
  `interests` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_professors_user` (`user_id`),
  CONSTRAINT `fk_professors_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `professors`
--

LOCK TABLES `professors` WRITE;
/*!40000 ALTER TABLE `professors` DISABLE KEYS */;
/*!40000 ALTER TABLE `professors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `students`
--

DROP TABLE IF EXISTS `students`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `students` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `full_name` varchar(100) NOT NULL,
  `university_id` varchar(50) DEFAULT NULL,
  `interests` varchar(255) DEFAULT NULL,
  `skills` varchar(255) DEFAULT NULL,
  `preferred_location` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_students_user` (`user_id`),
  CONSTRAINT `fk_students_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `students`
--

LOCK TABLES `students` WRITE;
/*!40000 ALTER TABLE `students` DISABLE KEYS */;
/*!40000 ALTER TABLE `students` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `traineeship_applications`
--

DROP TABLE IF EXISTS `traineeship_applications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `traineeship_applications` (
  `id` int NOT NULL AUTO_INCREMENT,
  `traineeship_id` int NOT NULL,
  `student_id` int NOT NULL,
  `application_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `status` enum('PENDING','REJECTED','ACCEPTED') DEFAULT 'PENDING',
  PRIMARY KEY (`id`),
  KEY `fk_applications_traineeship` (`traineeship_id`),
  KEY `fk_applications_student` (`student_id`),
  CONSTRAINT `fk_applications_student` FOREIGN KEY (`student_id`) REFERENCES `students` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_applications_traineeship` FOREIGN KEY (`traineeship_id`) REFERENCES `traineeships` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `traineeship_applications`
--

LOCK TABLES `traineeship_applications` WRITE;
/*!40000 ALTER TABLE `traineeship_applications` DISABLE KEYS */;
/*!40000 ALTER TABLE `traineeship_applications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `traineeships`
--

DROP TABLE IF EXISTS `traineeships`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `traineeships` (
  `id` int NOT NULL AUTO_INCREMENT,
  `company_id` int NOT NULL,
  `title` varchar(100) DEFAULT NULL,
  `description` text,
  `start_date` date DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `topics` varchar(255) DEFAULT NULL,
  `status` enum('OPEN','ASSIGNED','COMPLETED') DEFAULT 'OPEN',
  `assigned_student_id` int DEFAULT NULL,
  `assigned_professor_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_traineeships_company` (`company_id`),
  KEY `fk_traineeships_student` (`assigned_student_id`),
  KEY `fk_traineeships_professor` (`assigned_professor_id`),
  CONSTRAINT `fk_traineeships_company` FOREIGN KEY (`company_id`) REFERENCES `companies` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_traineeships_professor` FOREIGN KEY (`assigned_professor_id`) REFERENCES `professors` (`id`) ON DELETE SET NULL,
  CONSTRAINT `fk_traineeships_student` FOREIGN KEY (`assigned_student_id`) REFERENCES `students` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `traineeships`
--

LOCK TABLES `traineeships` WRITE;
/*!40000 ALTER TABLE `traineeships` DISABLE KEYS */;
/*!40000 ALTER TABLE `traineeships` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `role` enum('STUDENT','PROFESSOR','COMPANY','COMMITTEE','ADMIN') NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `v_traineeships`
--

DROP TABLE IF EXISTS `v_traineeships`;
/*!50001 DROP VIEW IF EXISTS `v_traineeships`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `v_traineeships` AS SELECT 
 1 AS `traineeship_id`,
 1 AS `traineeship_title`,
 1 AS `traineeship_status`,
 1 AS `offering_company`,
 1 AS `assigned_student`,
 1 AS `supervising_professor`*/;
SET character_set_client = @saved_cs_client;

--
-- Final view structure for view `v_traineeships`
--

/*!50001 DROP VIEW IF EXISTS `v_traineeships`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `v_traineeships` AS select `t`.`id` AS `traineeship_id`,`t`.`title` AS `traineeship_title`,`t`.`status` AS `traineeship_status`,`c`.`company_name` AS `offering_company`,`s`.`full_name` AS `assigned_student`,`p`.`full_name` AS `supervising_professor` from (((`traineeships` `t` join `companies` `c` on((`t`.`company_id` = `c`.`id`))) left join `students` `s` on((`t`.`assigned_student_id` = `s`.`id`))) left join `professors` `p` on((`t`.`assigned_professor_id` = `p`.`id`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-02-22 14:12:48

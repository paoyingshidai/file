/*
SQLyog Professional v12.09 (64 bit)
MySQL - 5.7.18 : Database - file
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`file` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `file`;

/*Table structure for table `t_file` */

DROP TABLE IF EXISTS `t_file`;

CREATE TABLE `t_file` (
  `file_id` int(10) NOT NULL AUTO_INCREMENT,
  `file_name` varchar(128) DEFAULT NULL,
  `path` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`file_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;

/*Data for the table `t_file` */

insert  into `t_file`(`file_id`,`file_name`,`path`) values (1,'1.jpg','g1/M00/00/00/wKjmgVtpxY6AHa-_AAAM53U0amM052.jpg'),(2,'1.jpg','g1/M00/00/00/wKjmgVtpy6KAC54KAAAM53U0amM944.jpg'),(3,'1.jpg',NULL),(4,'1.jpg',NULL),(5,'1.jpg',NULL),(6,'1.jpg',NULL),(7,'1.jpg',NULL),(8,'1.jpg',NULL),(9,'1.jpg','g1/M00/00/00/wKjmgltqnoqATleHAAAM53U0amM219.jpg'),(10,'2.jpg','g1/M00/00/00/wKjmgltqqdmACcXoAAAnhAJYDV0129.jpg');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

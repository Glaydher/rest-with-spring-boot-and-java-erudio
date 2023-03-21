CREATE TABLE `books` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `author` varchar(80) NOT NULL,
  `launch_date` datetime NOT NULL,
  `price` decimal (65,2) NOT NULL,
  `title` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
);
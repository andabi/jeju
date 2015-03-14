DROP TABLE if EXISTS `message`;
CREATE TABLE `message` (
  id            int NOT NULL AUTO_INCREMENT,
  writer        varchar(16) NOT NULL,
  content       varchar(255) NOT NULL,
  created_at    datetime NOT NULL,
  PRIMARY KEY (id)
)
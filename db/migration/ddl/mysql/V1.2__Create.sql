
CREATE TABLE `office_222` (
  `office_code`         VARCHAR(10) NOT NULL,
  `city`                VARCHAR(50) DEFAULT NULL,
  `phone`               VARCHAR(50) NOT NULL,
  `address_line_first`  VARCHAR(50) NOT NULL,
  `address_line_second` VARCHAR(50) DEFAULT NULL,
  `state`               VARCHAR(50) DEFAULT NULL,
  `country`             VARCHAR(50) DEFAULT NULL,
  `postal_code`         VARCHAR(15) NOT NULL, 
  `territory`           VARCHAR(10) NOT NULL,
  /* [jooq ignore start] */
  `location`            POINT       DEFAULT NULL,
  /* [jooq ignore stop] */
  `internal_budget`     INT         NOT NULL,
  CONSTRAINT `office_pk_9` PRIMARY KEY (`office_code`),
  CONSTRAINT `office_postal_code_uk_9` UNIQUE (`postal_code`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

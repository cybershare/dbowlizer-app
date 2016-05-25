
CREATE TABLE `City` (
  `city_id` int(10) unsigned NOT NULL,
  `city_name` varchar(45) NOT NULL,
  `province` varchar(45) NOT NULL,
  PRIMARY KEY (`city_id`) 
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `Contract` (
  `id` int(10) unsigned NOT NULL,
  `company` varchar(45) NOT NULL,
  `duration` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `Department` (
  `id` int(10) unsigned NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `location` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `Deparments_in_ontario` AS select `Department`.`id` AS `id`,`Department`.`name` AS `Name`,`Department`.`location` AS `Location`,`City`.`city_id` AS `city_id`,`City`.`city_name` AS `City_name`,`City`.`province` AS `Province` from (`Department` join `City` on((`Department`.`location` = `City`.`city_name`))) where (`City`.`province` = 'ONTARIO');

CREATE TABLE `Employee` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `address` varchar(100) NOT NULL,
  `salary` float unsigned NOT NULL DEFAULT '1000',
  PRIMARY KEY (`id`),
  UNIQUE KEY `NameIndex` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

CREATE TABLE `Dependent` (
  `employee_id` int(10) unsigned NOT NULL,
  `id` int(10) unsigned NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`employee_id`,`id`),
  CONSTRAINT `FK_dependent_emp` FOREIGN KEY (`employee_id`) REFERENCES `Employee` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `Employee_copy` AS select `Employee`.`id` AS `id`,`Employee`.`name` AS `Name`,`Employee`.`address` AS `Address`,`Employee`.`salary` AS `Salary` from `Employee`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `Employee_name` AS select `Employee`.`name` AS `Name` from `Employee`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `High_salary_employee` AS select `Employee`.`id` AS `id`,`Employee`.`name` AS `Name`,`Employee`.`address` AS `Address`,`Employee`.`salary` AS `Salary` from `Employee` where (`Employee`.`salary` > 800);

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `High_salary_employee_salary` AS select `Employee`.`name` AS `Name`,`Employee`.`salary` AS `Salary` from `Employee` where (`Employee`.`salary` > 800);

CREATE TABLE `Job` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `description` varchar(100) NOT NULL DEFAULT 'ITJob',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

CREATE TABLE `Project` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `description` varchar(45) NOT NULL,
  `cost` float unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;

CREATE TABLE `Manager` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `entry_date` datetime NOT NULL,
  `project` int(10) unsigned NOT NULL,
  `time_assigned` enum('full-time','part-time') DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_manager_proj` (`Project`),
  CONSTRAINT `FK_manager_emp` FOREIGN KEY (`id`) REFERENCES `Employee` (`id`),
  CONSTRAINT `FK_manager_proj` FOREIGN KEY (`Project`) REFERENCES `Project` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

CREATE TABLE `Outsourceproject` (
  `project_id` int(10) unsigned NOT NULL,
  `contract_id` int(10) unsigned NOT NULL,
  `phase` int(5) unsigned NOT NULL,
  PRIMARY KEY (`project_id`,`contract_id`,`phase`) USING BTREE,
  KEY `FK_outsourceproject_contract` (`contract_id`),
  CONSTRAINT `FK_outsourceproject_contract` FOREIGN KEY (`contract_id`) REFERENCES `Contract` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_outsourceproject_project` FOREIGN KEY (`project_id`) REFERENCES `Project` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `Managers_by_time` AS select `Manager`.`time_assigned` AS `Time_assigned`,count(0) AS `count(*)` from `Manager` group by `Manager`.`time_assigned`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `Outsource_project_manager` AS select `Manager`.`id` AS `id`,`Manager`.`entry_date` AS `Entry_date`,`Manager`.`project` AS `Project`,`Manager`.`time_assigned` AS `Time_assigned`,`Outsourceproject`.`project_id` AS `project_id`,`Outsourceproject`.`contract_id` AS `contract_id`,`Outsourceproject`.`phase` AS `Phe` from (`Manager` join `Outsourceproject` on((`Manager`.`project` = `Outsourceproject`.`project_id`)));

CREATE TABLE `Outsourceworkers` (
  `employee_id` int(10) unsigned NOT NULL,
  `project_id` int(10) unsigned NOT NULL,
  `start_date` datetime NOT NULL,
  PRIMARY KEY (`employee_id`,`project_id`),
  KEY `FK_outsourceworkers_proj` (`project_id`),
  CONSTRAINT `FK_outsourceworkers_emp` FOREIGN KEY (`employee_id`) REFERENCES `Employee` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_outsourceworkers_proj` FOREIGN KEY (`project_id`) REFERENCES `Project` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE `Projectjobemployee` (
  `project_id` int(10) unsigned NOT NULL,
  `employee_id` int(10) unsigned NOT NULL,
  `job_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`project_id`,`employee_id`,`job_id`) USING BTREE,
  KEY `FK_projectJobPosition_2` (`employee_id`),
  KEY `FK_projectJobPosition_3` (`job_id`) USING BTREE,
  CONSTRAINT `FK_projectJobPosition_1` FOREIGN KEY (`project_id`) REFERENCES `Project` (`id`),
  CONSTRAINT `FK_projectJobPosition_2` FOREIGN KEY (`employee_id`) REFERENCES `Employee` (`id`),
  CONSTRAINT `FK_projectJobPosition_3` FOREIGN KEY (`job_id`) REFERENCES `Job` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `Subproject` (
  `project` int(10) unsigned NOT NULL,
  `subproject` int(10) unsigned NOT NULL,
  PRIMARY KEY (`project`,`subproject`),
  KEY `FK_subproject_2` (`subproject`),
  CONSTRAINT `FK_subproject_1` FOREIGN KEY (`project`) REFERENCES `Project` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_subproject_2` FOREIGN KEY (`subproject`) REFERENCES `Project` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `Temporalassignment` (
  `department_id` int(10) unsigned NOT NULL,
  `employee_id` int(10) unsigned NOT NULL,
  `project_id` int(10) unsigned NOT NULL,
  `year` int(4) unsigned NOT NULL,
  PRIMARY KEY (`department_id`,`employee_id`,`project_id`,`year`),
  KEY `FK_temporalassignment_emp` (`employee_id`),
  KEY `FK_temporalassignment_proj` (`project_id`),
  CONSTRAINT `FK_temporalassignment_dept` FOREIGN KEY (`department_id`) REFERENCES `Department` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_temporalassignment_emp` FOREIGN KEY (`employee_id`) REFERENCES `Employee` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_temporalassignment_proj` FOREIGN KEY (`project_id`) REFERENCES `Project` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `Temporal_outsource_project` AS select `Project`.`name` AS `name` from ((`Project` join `Temporalassignment` on((`Temporalassignment`.`project_id` = `Project`.`id`))) join `Outsourceproject` on((`Outsourceproject`.`project_id` = `Project`.`id`)));

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `Temporal_outsource_project_filtered` AS select `Project`.`name` AS `Name` from ((`Project` join `Temporalassignment` on((`Temporalassignment`.`project_id` = `Project`.`id`))) join `Outsourceproject` on((`Outsourceproject`.`project_id` = `Project`.`id`))) where (`Project`.`cost` > 3000);

CREATE TABLE `Users` (
  `id` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  `pwd` varchar(45) NOT NULL,
  `creator` int(11) NOT NULL,
  `authorized` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `FK_users_creator` (`creator`),
  KEY `FK_users_authorized` (`authorized`),
  CONSTRAINT `FK_users_authorized` FOREIGN KEY (`authorized`) REFERENCES `Users` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `FK_users_creator` FOREIGN KEY (`creator`) REFERENCES `Users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `Worksfor` (
  `employee_id` int(10) unsigned NOT NULL,
  `department_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`employee_id`,`department_id`) USING BTREE,
  KEY `FK_worksfor_dept` (`department_id`),
  CONSTRAINT `FK_worksfor_dept` FOREIGN KEY (`department_id`) REFERENCES `Department` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_worksfor_emp` FOREIGN KEY (`employee_id`) REFERENCES `Employee` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `Worksfor_dept2` AS select `Worksfor`.`employee_id` AS `employee_id`,`Worksfor`.`department_id` AS `department_id` from `Worksfor` where (`Worksfor`.`department_id` = 2);

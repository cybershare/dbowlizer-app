
CREATE TABLE `city` (
  `city_id` int(10) unsigned NOT NULL,
  `city_name` varchar(45) NOT NULL,
  `province` varchar(45) NOT NULL,
  PRIMARY KEY (`city_id`) 
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `contract` (
  `id` int(10) unsigned NOT NULL,
  `company` varchar(45) NOT NULL,
  `duration` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `department` (
  `id` int(10) unsigned NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `location` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `deparments_in_ontario` AS select `department`.`id` AS `id`,`department`.`name` AS `name`,`department`.`location` AS `location`,`city`.`city_id` AS `city_id`,`city`.`city_name` AS `city_name`,`city`.`province` AS `province` from (`department` join `city` on((`department`.`location` = `city`.`city_name`))) where (`city`.`province` = 'ONTARIO');

CREATE TABLE `employee` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `Name` varchar(45) NOT NULL,
  `Address` varchar(100) NOT NULL,
  `Salary` float unsigned NOT NULL DEFAULT '1000',
  PRIMARY KEY (`id`),
  UNIQUE KEY `NameIndex` (`Name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

CREATE TABLE `dependent` (
  `employee_id` int(10) unsigned NOT NULL,
  `id` int(10) unsigned NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`employee_id`,`id`),
  CONSTRAINT `FK_dependent_emp` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `employee_copy` AS select `employee`.`id` AS `id`,`employee`.`Name` AS `Name`,`employee`.`Address` AS `Address`,`employee`.`Salary` AS `Salary` from `employee`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `employee_name` AS select `employee`.`Name` AS `Name` from `employee`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `high_salary_employee` AS select `employee`.`id` AS `id`,`employee`.`Name` AS `Name`,`employee`.`Address` AS `Address`,`employee`.`Salary` AS `Salary` from `employee` where (`employee`.`Salary` > 800);

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `high_salary_employee_salary` AS select `employee`.`Name` AS `Name`,`employee`.`Salary` AS `Salary` from `employee` where (`employee`.`Salary` > 800);

CREATE TABLE `job` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `Name` varchar(45) NOT NULL,
  `Description` varchar(100) NOT NULL DEFAULT 'ITJob',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

CREATE TABLE `project` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `description` varchar(45) NOT NULL,
  `cost` float unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;

CREATE TABLE `manager` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `Entry_date` datetime NOT NULL,
  `Project` int(10) unsigned NOT NULL,
  `Time_assigned` enum('full-time','part-time') DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_manager_proj` (`Project`),
  CONSTRAINT `FK_manager_emp` FOREIGN KEY (`id`) REFERENCES `employee` (`id`),
  CONSTRAINT `FK_manager_proj` FOREIGN KEY (`Project`) REFERENCES `project` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

CREATE TABLE `outsourceproject` (
  `project_id` int(10) unsigned NOT NULL,
  `contract_id` int(10) unsigned NOT NULL,
  `phase` int(5) unsigned NOT NULL,
  PRIMARY KEY (`project_id`,`contract_id`,`phase`) USING BTREE,
  KEY `FK_outsourceproject_contract` (`contract_id`),
  CONSTRAINT `FK_outsourceproject_contract` FOREIGN KEY (`contract_id`) REFERENCES `contract` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_outsourceproject_project` FOREIGN KEY (`project_id`) REFERENCES `project` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `managers_by_time` AS select `manager`.`Time_assigned` AS `time_assigned`,count(0) AS `count(*)` from `manager` group by `manager`.`Time_assigned`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `outsource_project_manager` AS select `manager`.`id` AS `id`,`manager`.`Entry_date` AS `Entry_date`,`manager`.`Project` AS `Project`,`manager`.`Time_assigned` AS `Time_assigned`,`outsourceproject`.`project_id` AS `project_id`,`outsourceproject`.`contract_id` AS `contract_id`,`outsourceproject`.`phase` AS `phase` from (`manager` join `outsourceproject` on((`manager`.`Project` = `outsourceproject`.`project_id`)));

CREATE TABLE `outsourceworkers` (
  `employee_id` int(10) unsigned NOT NULL,
  `project_id` int(10) unsigned NOT NULL,
  `start_date` datetime NOT NULL,
  PRIMARY KEY (`employee_id`,`project_id`),
  KEY `FK_outsourceworkers_proj` (`project_id`),
  CONSTRAINT `FK_outsourceworkers_emp` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_outsourceworkers_proj` FOREIGN KEY (`project_id`) REFERENCES `project` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE `projectjobemployee` (
  `project_id` int(10) unsigned NOT NULL,
  `employee_id` int(10) unsigned NOT NULL,
  `job_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`project_id`,`employee_id`,`job_id`) USING BTREE,
  KEY `FK_projectJobPosition_2` (`employee_id`),
  KEY `FK_projectJobPosition_3` (`job_id`) USING BTREE,
  CONSTRAINT `FK_projectJobPosition_1` FOREIGN KEY (`project_id`) REFERENCES `project` (`id`),
  CONSTRAINT `FK_projectJobPosition_2` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`),
  CONSTRAINT `FK_projectJobPosition_3` FOREIGN KEY (`job_id`) REFERENCES `job` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `subproject` (
  `project` int(10) unsigned NOT NULL,
  `subproject` int(10) unsigned NOT NULL,
  PRIMARY KEY (`project`,`subproject`),
  KEY `FK_subproject_2` (`subproject`),
  CONSTRAINT `FK_subproject_1` FOREIGN KEY (`project`) REFERENCES `project` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_subproject_2` FOREIGN KEY (`subproject`) REFERENCES `project` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `temporalassignment` (
  `department_id` int(10) unsigned NOT NULL,
  `employee_id` int(10) unsigned NOT NULL,
  `project_id` int(10) unsigned NOT NULL,
  `year` int(4) unsigned NOT NULL,
  PRIMARY KEY (`department_id`,`employee_id`,`project_id`,`year`),
  KEY `FK_temporalassignment_emp` (`employee_id`),
  KEY `FK_temporalassignment_proj` (`project_id`),
  CONSTRAINT `FK_temporalassignment_dept` FOREIGN KEY (`department_id`) REFERENCES `department` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_temporalassignment_emp` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_temporalassignment_proj` FOREIGN KEY (`project_id`) REFERENCES `project` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `temporal_outsource_project` AS select `project`.`name` AS `name` from ((`project` join `temporalassignment` on((`temporalassignment`.`project_id` = `project`.`id`))) join `outsourceproject` on((`outsourceproject`.`project_id` = `project`.`id`)));

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `temporal_outsource_project_filtered` AS select `project`.`name` AS `name` from ((`project` join `temporalassignment` on((`temporalassignment`.`project_id` = `project`.`id`))) join `outsourceproject` on((`outsourceproject`.`project_id` = `project`.`id`))) where (`project`.`cost` > 3000);

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  `pwd` varchar(45) NOT NULL,
  `creator` int(11) NOT NULL,
  `authorized` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `FK_users_creator` (`creator`),
  KEY `FK_users_authorized` (`authorized`),
  CONSTRAINT `FK_users_authorized` FOREIGN KEY (`authorized`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `FK_users_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `worksfor` (
  `employee_id` int(10) unsigned NOT NULL,
  `department_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`employee_id`,`department_id`) USING BTREE,
  KEY `FK_worksfor_dept` (`department_id`),
  CONSTRAINT `FK_worksfor_dept` FOREIGN KEY (`department_id`) REFERENCES `department` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_worksfor_emp` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `worksfor_dept2` AS select `worksfor`.`employee_id` AS `employee_id`,`worksfor`.`department_id` AS `department_id` from `worksfor` where (`worksfor`.`department_id` = 2);

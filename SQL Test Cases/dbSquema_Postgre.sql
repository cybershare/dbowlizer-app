/* Test Schema Script for Oracle */

/* pass */
CREATE TABLE city (
  city_id INT  NOT NULL,
  city_name VARCHAR(45) NOT NULL,
  province VARCHAR(45) NOT NULL,
  PRIMARY KEY (city_id) 
) ;

/* pass */
CREATE TABLE contract (
  id INT NOT NULL,
  company VARCHAR(45) NOT NULL,
  duration VARCHAR(45) NOT NULL,
  PRIMARY KEY (id)
) ;

/* pass */
CREATE TABLE department (
  id INT NOT NULL,
  name VARCHAR(45) DEFAULT NULL,
  location VARCHAR(45) DEFAULT NULL,
  PRIMARY KEY (id)
) ;

/* pass */
CREATE  VIEW deparments_in_ontario AS select department.id AS id, department.name AS name,department.location AS location,city.city_id AS city_id,city.city_name AS city_name,city.province AS province from (department join city on((department.location = city.city_name))) where (city.province = 'ONTARIO');

/* pass */
CREATE TABLE employee (
  id SERIAL  NOT NULL,
  Name VARCHAR(45) NOT NULL,
  Address VARCHAR(100) NOT NULL,
  Salary NUMERIC  DEFAULT '1000' NOT NULL ,
  PRIMARY KEY (id),
  CONSTRAINT NameIndex UNIQUE  (Name) 
) ;

/* pass */
CREATE TABLE dependent (
  employee_id INT  NOT NULL,
  id INT  NOT NULL,
  name VARCHAR(45) DEFAULT NULL,
  PRIMARY KEY (employee_id,id),
  CONSTRAINT FK_dependent_emp FOREIGN KEY (employee_id) REFERENCES employee (id) 
) ;

/*pass */
CREATE VIEW employee_copy AS select employee.id AS id,employee.Name AS Name,employee.Address AS Address,employee.Salary AS Salary from employee;
/*pass */
CREATE VIEW employee_name AS select employee.Name AS Name from employee;
/*pass */
CREATE VIEW high_salary_employee AS select employee.id AS id,employee.Name AS Name,employee.Address AS Address,employee.Salary AS Salary from employee where (employee.Salary > 800);
/*pass */
CREATE VIEW high_salary_employee_salary AS select employee.Name AS Name,employee.Salary AS Salary from employee where (employee.Salary > 800);

/*pass */
CREATE TABLE job (
  id SERIAL  NOT NULL,
  Name VARCHAR(45) NOT NULL,
  Description VARCHAR(100) DEFAULT 'ITJob' NOT NULL ,
  PRIMARY KEY (id)
) ;

/*pass */
CREATE TABLE project (
  id SERIAL  NOT NULL,
  name VARCHAR(45) NOT NULL,
  description VARCHAR(45) NOT NULL,
  cost NUMERIC  NOT NULL,
  PRIMARY KEY (id)
) ;

/*pass */
CREATE TYPE assigned AS ENUM('full-time','part-time');

/*pass */
CREATE TABLE manager (
  id SERIAL  NOT NULL,
  Entry_date DATE NOT NULL,
  Project INT  NOT NULL,
  Time_assigned assigned DEFAULT NULL,
  PRIMARY KEY (id),
  /*KEY FK_manager_proj (Project),*/
  CONSTRAINT FK_manager_emp FOREIGN KEY (id) REFERENCES employee (id),
  CONSTRAINT FK_manager_proj FOREIGN KEY (Project) REFERENCES project (id)
) ;

/*pass */
CREATE TABLE outsourceproject (
  project_id INT  NOT NULL,
  contract_id INT  NOT NULL,
  phase INT  NOT NULL,
  PRIMARY KEY (project_id,contract_id,phase),
  /* KEY FK_outsourceproject_contract (contract_id), */
  CONSTRAINT FK_outsourceproject_contract FOREIGN KEY (contract_id) REFERENCES contract (id) ON DELETE CASCADE,
  CONSTRAINT FK_outsourceproject_project FOREIGN KEY (project_id) REFERENCES project (id)
) ;

/*pass*/
CREATE VIEW managers_by_time AS select manager.Time_assigned AS time_assigned,count(0) AS counter from manager group by manager.Time_assigned;
/*pass*/
CREATE VIEW outsource_project_manager AS select manager.id AS id,manager.Entry_date AS Entry_date,manager.Project AS Project,manager.Time_assigned AS Time_assigned,outsourceproject.project_id AS project_id,outsourceproject.contract_id AS contract_id,outsourceproject.phase AS phase from (manager join outsourceproject on((manager.Project = outsourceproject.project_id)));

/*pass*/
CREATE TABLE outsourceworkers (
  employee_id INT  NOT NULL,
  project_id INT  NOT NULL,
  start_date DATE NOT NULL,
  PRIMARY KEY (employee_id,project_id),
  /* KEY FK_outsourceworkers_proj (project_id),*/
  CONSTRAINT FK_outsourceworkers_emp FOREIGN KEY (employee_id) REFERENCES employee (id) ON DELETE CASCADE,
  CONSTRAINT FK_outsourceworkers_proj FOREIGN KEY (project_id) REFERENCES project (id) ON DELETE CASCADE
) ;

/*pass*/
CREATE TABLE projectjobemployee (
  project_id INT  NOT NULL,
  employee_id INT  NOT NULL,
  job_id INT  NOT NULL,
  PRIMARY KEY (project_id,employee_id,job_id),
  /* KEY FK_projectJobPosition_2 (employee_id),
      KEY FK_projectJobPosition_3 (job_id) USING BTREE, */
  CONSTRAINT FK_projectJobPosition_1 FOREIGN KEY (project_id) REFERENCES project (id),
  CONSTRAINT FK_projectJobPosition_2 FOREIGN KEY (employee_id) REFERENCES employee (id),
  CONSTRAINT FK_projectJobPosition_3 FOREIGN KEY (job_id) REFERENCES job (id)
) ;

/*pass*/
CREATE TABLE subproject (
  project INT  NOT NULL,
  subproject INT  NOT NULL,
  PRIMARY KEY (project,subproject),
  /* KEY FK_subproject_2 (subproject), */
  CONSTRAINT FK_subproject_1 FOREIGN KEY (project) REFERENCES project (id) ON DELETE CASCADE,
  CONSTRAINT FK_subproject_2 FOREIGN KEY (subproject) REFERENCES project (id) ON DELETE CASCADE
) ;

/*pass*/
CREATE TABLE temporalassignment (
  department_id INT  NOT NULL,
  employee_id INT  NOT NULL,
  project_id INT  NOT NULL,
  year NUMERIC(4)  NOT NULL,
  PRIMARY KEY (department_id,employee_id,project_id,year),
  /* KEY FK_temporalassignment_emp (employee_id),
  KEY FK_temporalassignment_proj (project_id), */
  CONSTRAINT FK_temporalassignment_dept FOREIGN KEY (department_id) REFERENCES department (id) ON DELETE CASCADE,
  CONSTRAINT FK_temporalassignment_emp FOREIGN KEY (employee_id) REFERENCES employee (id) ON DELETE CASCADE,
  CONSTRAINT FK_temporalassignment_proj FOREIGN KEY (project_id) REFERENCES project (id) ON DELETE CASCADE 
) ;

/*pass*/
CREATE VIEW temporal_outsource_project AS select project.name AS name from ((project join temporalassignment on((temporalassignment.project_id = project.id))) join outsourceproject on((outsourceproject.project_id = project.id)));
/*pass had to cut identifier name due to oracle length restriction*/
CREATE VIEW temporal_out_project_filtered AS select project.name AS name from ((project join temporalassignment on((temporalassignment.project_id = project.id))) join outsourceproject on((outsourceproject.project_id = project.id))) where (project.cost > 3000);

CREATE TABLE users (
  id INT NOT NULL,
  name VARCHAR(45) NOT NULL,
  pwd VARCHAR(45) NOT NULL,
  creator INT NOT NULL,
  authorized INT NOT NULL,
  PRIMARY KEY (id),
  /* KEY FK_users_creator (creator),
     KEY FK_users_authorized (authorized), */
  CONSTRAINT FK_users_authorized FOREIGN KEY (authorized) REFERENCES users (id) ON DELETE CASCADE,
  CONSTRAINT FK_users_creator FOREIGN KEY (creator) REFERENCES users (id)
) ;

/*pass*/
CREATE TABLE worksfor (
  employee_id INT  NOT NULL,
  department_id INT  NOT NULL,
  PRIMARY KEY (employee_id,department_id),
  /* KEY FK_worksfor_dept (department_id), */
  CONSTRAINT FK_worksfor_dept FOREIGN KEY (department_id) REFERENCES department (id) ON DELETE CASCADE,
  CONSTRAINT FK_worksfor_emp FOREIGN KEY (employee_id) REFERENCES employee (id)
) ;

/*pass*/
CREATE  VIEW worksfor_dept2 AS select worksfor.employee_id AS employee_id,worksfor.department_id AS department_id from worksfor where (worksfor.department_id = 2);





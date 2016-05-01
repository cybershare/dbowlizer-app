/* DROP EVERYTHING FIRST */

drop view DEPARMENTS_IN_ONTARIO;          
drop view EMPLOYEE_COPY;                  
drop view EMPLOYEE_NAME;                  
drop view HIGH_SALARY_EMPLOYEE;           
drop view HIGH_SALARY_EMPLOYEE_SALARY;    
drop view MANAGERS_BY_TIME;               
drop view OUTSOURCE_PROJECT_MANAGER;      
drop view TEMPORAL_OUTSOURCE_PROJECT;     
drop view TEMPORAL_OUT_PROJECT_FILTERED;  
drop view WORKSFOR_DEPT2;                 


/* Database dependant oracle or postgres 
drop sequence PROJECT_SEQ;                    
drop sequence JOB_SEQ;                        
drop sequence MANAGER_SEQ;    */      

drop table MANAGER;                        
drop table SUBPROJECT;                     
drop table DEPARTMENT;                     
drop table OUTSOURCEPROJECT;               
drop table PROJECTJOBEMPLOYEE;             
drop table TEMPORALASSIGNMENT;             
drop table JOB;                            
drop table CONTRACT;                       
drop table EMPLOYEE;                       
drop table DEPENDENT;                      
drop table PROJECT;                        
drop table USERS;                          
drop table WORKSFOR;                       
drop table CITY;                           
drop table OUTSOURCEWORKERS;    
-----------------------------------------------------------------------------------------------------------------------------------dabases structure
------global names-----------
ALTER DATABASE RENAME GLOBAL_NAME TO life.dept;
ALTER DATABASE RENAME GLOBAL_NAME TO car.dept;
ALTER DATABASE RENAME GLOBAL_NAME TO goods.dept;
--------users on SALMA's PC---------

---------user---------
CREATE USER c##dept_admin 
IDENTIFIED BY dept_admin;

--to let user make tables and solve:  no privileges on tablespace 'USERS"
ALTER USER c##dept_admin  quota unlimited on USERS;

GRANT CREATE SESSION,
 CREATE TABLE,
 CREATE PROCEDURE, 
 CREATE SEQUENCE, 
 CREATE TRIGGER, 
 CREATE VIEW, 
 CREATE SYNONYM,
 ALTER SESSION,
 CREATE MATERIALIZED VIEW,
 ALTER ANY MATERIALIZED VIEW,
 CREATE DATABASE LINK
 TO c##dept_admin ;

 


 
 --**************************************************************************----

----------------TABLES---------------

------CAR-------
CREATE TABLE CAR (
id NUMBER PRIMARY KEY,
price FLOAT,
state VARCHAR2(50),
power NUMBER,
mark VARCHAR2(50),
creationYEAR VARCHAR2(4),
model VARCHAR2(10),
StartMoovingDate DATE
)
/

INSERT INTO CAR VALUES(1,3000,'Used',89,'Mercedes','2000','m2121','2-DEC-2005');
INSERT INTO CAR VALUES(2,4000,'Exhasted',76,'Ford','1998','f240','2-DEC-1998');
INSERT INTO CAR VALUES(3,5000,'New',99,'BMW','1999','i350','2-DEC-2000');
INSERT INTO CAR VALUES(4,9000,'Used',77,'Honda','2008','h837','2-DEC-2010');
INSERT INTO CAR VALUES(5,9000,'Used',80,'Renault','2012','r987','2-DEC-2013');

commit;


------REGION-------
CREATE TABLE REGION(
id NUMBER PRIMARY KEY,
name VARCHAR2(50)
)
/
INSERT INTO REGION VALUES(1,'Beirut');
INSERT INTO REGION VALUES(2,'South');
INSERT INTO REGION VALUES(3,'North');
INSERT INTO REGION VALUES(4,'Bikaa');


------CITY-------
CREATE TABLE CITY(
id NUMBER PRIMARY KEY,
name VARCHAR2(50),
id_region NUMBER
)
/
ALTER TABLE CITY 
ADD CONSTRAINT FK_CITY_REGION
FOREIGN KEY (id_region) REFERENCES REGION(id);
/
INSERT INTO CITY VALUES(1,'Beirut',1);
INSERT INTO CITY VALUES(2,'Nabatiye',2);
INSERT INTO CITY VALUES(3,'Tyre',2);
INSERT INTO CITY VALUES(4,'Saida',2);
INSERT INTO CITY VALUES(5,'Trablous',3);
INSERT INTO CITY VALUES(6,'Akkar',3);
INSERT INTO CITY VALUES(7,'Zgharta',4);



------ADDRESS-------
CREATE TABLE ADDRESS(
id NUMBER PRIMARY KEY,
id_city NUMBER,
description VARCHAR2(100)
)
/

ALTER TABLE ADDRESS 
ADD CONSTRAINT FK_ADDRESS_CITY 
FOREIGN KEY (id_city) REFERENCES CITY(id);


INSERT INTO ADDRESS VALUES(1,1,'Street Riad Solh,HAMRA');
INSERT INTO ADDRESS VALUES(2,1,'4th floor,Red Building,HAMRA');
INSERT INTO ADDRESS VALUES(3,1,'Bank XXX,Street Y');
INSERT INTO ADDRESS VALUES(4,3,'Blng SAMO,sNabih Berri Street');
INSERT INTO ADDRESS VALUES(5,2,'Bldng Zanoubia, Main Street');
INSERT INTO ADDRESS VALUES(6,2,'Bldng Mirza, Main Street');
INSERT INTO ADDRESS VALUES(7,4,'Moazzen Bldng, Main Street');
INSERT INTO ADDRESS VALUES(8,4,'Joun Bldgn, Main Street');
INSERT INTO ADDRESS VALUES(9,4,'AlShams Bldng, Main Street');



-------PERSON------
CREATE TABLE PERSON(
id NUMBER PRIMARY KEY,
fName VARCHAR2(50),
mName VARCHAR2(50),
lName VARCHAR2(50),
date_of_birth DATE,
id_adress NUMBER,
phone_number NUMBER(10),
health_state VARCHAR2(50),
capital FLOAT
)
/

ALTER TABLE PERSON ADD CONSTRAINT
address_Fkey foreign key(id_adress)
references ADDRESS(id)
/

INSERT INTO PERSON VALUES(1,'Sana','miso','Farhat','23-OCT-1993',5,71855791,'Diabetic',45000);
INSERT INTO PERSON VALUES(2,'Salma','simo','Tofaily','9-NOV-1988',6,71874713,'Good health',6000);
INSERT INTO PERSON VALUES(3,'Afif','mafif','mahfouz','23-JAN-1960',7,713235791,'Hiv',90000);
INSERT INTO PERSON VALUES(4,'Samir','mimo','Abdo','23-DEC-1975',8,03822791,'Asmathic',60000);
INSERT INTO PERSON VALUES(5,'Mira','noni','Kays','2-OCT-1987',8,70896574,'Good health',41000);
INSERT INTO PERSON VALUES(6,'Saeed','miso','Berro','12-JUL-1991',9,71009773,'Good Health',10000);

commit;

------Beneficiaries-----------
CREATE TABLE BENEFICIARIES (
id_client NUMBER,
id_designated NUMBER
)
/

ALTER TABLE BENEFICIARIES ADD CONSTRAINT
client_Fkey foreign key(id_client)
references PERSON(id)
ADD CONSTRAINT
designated_Fkey foreign key(id_designated)
references PERSON(id)
/

INSERT INTO BENEFICIARIES VALUES(1,2);
INSERT INTO BENEFICIARIES VALUES(2,4);
INSERT INTO BENEFICIARIES VALUES(3,1);
INSERT INTO BENEFICIARIES VALUES(4,6);
commit;



------GOODS-------
CREATE TABLE GOODS(
id NUMBER PRIMARY KEY,
state VARCHAR2(50),
price FLOAT,
description VARCHAR2(100),
id_address NUMBER 
)
/

ALTER TABLE GOODS 
ADD CONSTRAINT FK_GOODS_ADDRESS 
FOREIGN KEY (id_address) REFERENCES ADDRESS(id);

INSERT INTO GOODS VALUES(1,'Good',20000,'Pool',4);
INSERT INTO GOODS VALUES(2,'Good',20000,'Home',2);
INSERT INTO GOODS VALUES(3,'Good',1000000,'Hotel',1);
INSERT INTO GOODS VALUES(4,'Good',100000,'Necklace',3);

commit;


-----Contracts: 3 types-----

-------------------table:contract for cars insurrance--------------------------
CREATE TABLE CONTRACT_CAR(
id NUMBER PRIMARY KEY,
annualPayment FLOAT,
id_client NUMBER,
id_car NUMBER,
insurance_start_date DATE,
insurance_expiary_date DATE,
maximum_compensation FLOAT
)
/

ALTER TABLE CONTRACT_CAR 
ADD CONSTRAINT FK_CONTRACTCAR_CAR 
FOREIGN KEY (id_car) REFERENCES CAR(id) 
ADD CONSTRAINT FK_CONTRACTCAR_CLIENT 
FOREIGN KEY (id_client) REFERENCES PERSON(id);

--------table:contract for goods insurrance ----------
CREATE TABLE CONTRACT_GOODS(
id NUMBER PRIMARY KEY,
annualPayment FLOAT,
id_client NUMBER,
id_goods NUMBER,
insurance_start_date DATE,
insurance_expiary_date DATE,
maximum_compensation FLOAT
)
/


ALTER TABLE CONTRACT_GOODS 
ADD CONSTRAINT FK_CONTRACTGOODS_GOODS
FOREIGN KEY (id_goods) REFERENCES GOODS(id) 
ADD CONSTRAINT FK_CONTRACTGOODS_CLIENT 
FOREIGN KEY (id_client) REFERENCES PERSON(id);

INSERT INTO CONTRACT_GOODS VALUES(1,200,1,1,'23-JAN-2016','23-JAN-2017',20000);
INSERT INTO CONTRACT_GOODS VALUES(2,200,3,2,'23-JAN-2016','23-JAN-2017',20000);
INSERT INTO CONTRACT_GOODS VALUES(3,5000,2,3,'23-JAN-2016','23-JAN-2017',1000000);
INSERT INTO CONTRACT_GOODS VALUES(4,1000,1,4,'23-JAN-2016','23-JAN-2017',100000);



--------table:contract for life insurrance----------
CREATE TABLE CONTRACT_LIFE(
id NUMBER PRIMARY KEY,
annualPayment FLOAT,
id_client NUMBER,
insurance_start_date DATE,
insurance_expiary_date DATE,
compensation FLOAT
)
/
ALTER TABLE CONTRACT_LIFE 
ADD CONSTRAINT FK_CONTRACTLIFE_PERSON
FOREIGN KEY (id_client) REFERENCES PERSON(id);

INSERT INTO CONTRACT_LIFE VALUES(1,450,1,'23-OCT-2016','23-OCT-2017',20000);
INSERT INTO CONTRACT_LIFE VALUES(2,60,2,'23-OCT-2016','23-OCT-2017',3000);
INSERT INTO CONTRACT_LIFE VALUES(3,900,3,'23-OCT-2016','23-OCT-2017',40000);
INSERT INTO CONTRACT_LIFE VALUES(4,600,4,'23-OCT-2016','23-OCT-2017',30000);
INSERT INTO CONTRACT_LIFE VALUES(5,410,5,'23-OCT-2016','23-OCT-2017',20000);
INSERT INTO CONTRACT_LIFE VALUES(6,100,6,'23-OCT-2016','23-OCT-2017',5000);


----refunds tables:3 tables------

------------table : refunds for cars insurrance----------
CREATE TABLE REFUNDS_CAR(
id NUMBER PRIMARY KEY,
id_contract NUMBER,
amount FLOAT,
report VARCHAR2(4000)
)
/
ALTER TABLE REFUNDS_CAR
ADD CONSTRAINT FK_REFUNDS_CAR_CONTRACT_CAR
FOREIGN KEY (id_contract) REFERENCES CONTRACT_CAR(id);


----------table : refunds for life insurrance-----------
CREATE TABLE REFUNDS_LIFE(
id NUMBER PRIMARY KEY,
id_contract NUMBER,
report VARCHAR2(4000)
)
/
ALTER TABLE REFUNDS_LIFE
ADD CONSTRAINT FK_REFUNDS_LIFE_CONTRACT_LIFE
FOREIGN KEY (id_contract) REFERENCES CONTRACT_LIFE(id);

INSERT INTO REFUNDS_LIFE VALUES(1,3,'HEART ATTACK');
INSERT INTO REFUNDS_LIFE VALUES(2,4,'KILLED BY A ROCKET');

--------------table : refunds for goods insurrance---------
CREATE TABLE REFUNDS_GOODS(
id NUMBER PRIMARY KEY,
id_contract NUMBER,
amount FLOAT,
report VARCHAR2(4000)
)
/
ALTER TABLE REFUNDS_GOODS
ADD CONSTRAINT FK_REFUND_GDS_CONTRACT_GD
FOREIGN KEY (id_contract) REFERENCES CONTRACT_GOODS(id);

INSERT INTO REFUNDS_GOODS VALUES(1,3,20000,'FIRE DAMAGED THE INTRANCE OF THE HOTEL.');
INSERT INTO REFUNDS_GOODS VALUES(2,2,1000,'ALL GLASS WAS DAMAGED DUE TO A ROCKET HIT BESIDE THE HOME.');

commit;
-----------------------------------------------------------------------------------------------------------------------------------dabases replication


begin
DBMS_REPCAT_ADMIN.GRANT_ADMIN_ANY_REPGROUP(userid => 'c##dept_admin');
end;
/
connect system
@c:\app\salmaoracle\product\12.1.0\dbhome_1\RDBMS\ADMIN\catrep.sql
 
 --on life site--
CREATE PUBLIC DATABASE LINK car.dept USING 'car.dept';
CREATE PUBLIC DATABASE LINK goods.dept USING 'goods.dept';
--on goods site-------
CREATE PUBLIC DATABASE LINK car.dept USING 'car.dept';
CREATE PUBLIC DATABASE LINK life.dept USING 'life.dept';

--ON ALL SITES http://www.orafaq.com/wiki/Advanced_Replication#Remove_replication_support
EXECUTE Dbms_Defer_Sys.Register_Propagator(username=>'c##dept_admin');

GRANT EXECUTE ANY PROCEDURE TO C##DEPT_ADMIN;

EXECUTE Dbms_Repcat_Admin.Grant_Admin_Any_Repgroup('c##dept_admin');
EXECUTE Dbms_Repcat_Admin.Grant_Admin_Any_Schema (username => 'c##dept_admin');

GRANT LOCK ANY TABLE TO c##dept_admin;
GRANT COMMENT ANY TABLE TO c##dept_admin;

CONNECT c##dept_admin/dept_admin
-- Create private db links for all repadmin users
--on life site
CREATE DATABASE LINK car.dept CONNECT TO c##dept_admin IDENTIFIED BY dept_admin USING 'car.dept';
CREATE DATABASE LINK goods.dept CONNECT TO c##dept_admin IDENTIFIED BY dept_admin USING 'goods.dept';
--on goods dept
CREATE DATABASE LINK car.dept CONNECT TO c##dept_admin IDENTIFIED BY dept_admin USING 'car.dept';
CREATE DATABASE LINK life.dept CONNECT TO c##dept_admin IDENTIFIED BY dept_admin USING 'life.dept';

-- Schedule job to push transactions to all master sites with appropriate intervals
--on life site
BEGIN
DBMS_DEFER_SYS.SCHEDULE_PUSH(
destination => 'car.dept',
interval => 'sysdate + 1/(60*24)',
next_date => SYSDATE,
stop_on_error => FALSE,
delay_seconds => 0,
parallelism => 0);
END;
/
		
BEGIN
DBMS_DEFER_SYS.SCHEDULE_PUSH(
destination => 'goods.dept',
interval => 'sysdate + 1/(60*24)',
next_date => SYSDATE,
stop_on_error => FALSE,
delay_seconds => 0,
parallelism => 0);
END;
/
--on goods site
		
BEGIN
DBMS_DEFER_SYS.SCHEDULE_PUSH(
destination => 'car.dept',
interval => 'sysdate + 1/(60*24)',
next_date => SYSDATE,
stop_on_error => FALSE,
delay_seconds => 0,
parallelism => 0);
END;
/
		
BEGIN
DBMS_DEFER_SYS.SCHEDULE_PUSH(
destination => 'life.dept',
interval => 'sysdate + 1/(60*24)',
next_date => SYSDATE,
stop_on_error => FALSE,
delay_seconds => 0,
parallelism => 0);
END;
/

-- Schedule job to delete successfully replicated transactions
--on life site and goods site

BEGIN
DBMS_DEFER_SYS.SCHEDULE_PURGE(
next_date => SYSDATE,
interval => 'sysdate + 1/(60*24)',
delay_seconds => 0,
rollback_segment => '');
END;
/

----------------Setup master definition site 0n each site

-- on both sites
CONNECT c##dept_admin/dept_admin
-- Create replication group 
--add tables to group

begin
Dbms_Repcat.Drop_Master_Repobject(
sname => 'c##dept_admin' ,
oname =>  'region',
type => 'TABLE');
end;
 /
 
 begin
Dbms_Repcat.Drop_Master_Repobject(
sname => 'c##dept_admin' ,
oname =>  'person',
type => 'TABLE');
end;
 /
 begin
Dbms_Repcat.Drop_Master_Repobject(
sname => 'c##dept_admin' ,
oname =>  'city',
type => 'TABLE');
end;
 /
 begin
Dbms_Repcat.Drop_Master_Repobject(
sname => 'c##dept_admin' ,
oname =>  'address',
type => 'TABLE');
end;
 /
 

--give receiver roles on both sites

BEGIN
   DBMS_REPCAT_ADMIN.REGISTER_USER_REPGROUP (
      username => 'c##dept_admin',
      privilege_type => 'receiver',
      list_of_gnames => NULL);
END;
/

--on both sites

--grant execute on sys.DBMS_REPCAT to c##dept_admin;
connect system/ZainabBatoul31
connect system/SanaSalma
EXECUTE Dbms_Repcat_Admin.Grant_Admin_Any_Schema (username => 'c##dept_admin'); 

--change globalname param to true
alter system set global_names=true;


----now make all tables in each site replicable


--I dropped link with life.dept (public and user) (use public keyword for public) then i remade them, the error became  rollback required, I tryied before to use the sid name in the link name but it game me that feature does not exost in the version. the error is no more about remote objecct not found
--on goods

begin
dbms_repcat.drop_master_repgroup(
gname => 'groups',
all_sites => true,
drop_contents => true
);
end;
/

BEGIN
   DBMS_REPCAT.REMOVE_MASTER_DATABASES (
      gname => 'rep',
      master_list => 'life.dept');
END;
/
BEGIN
   DBMS_REPCAT.REMOVE_MASTER_DATABASES (
      gname => 'rep',
      master_list => 'car.dept');
END;
/
--but public was created again!

EXECUTE Dbms_Repcat.Create_Master_Repgroup('rep_groups');


BEGIN
   DBMS_REPCAT.CREATE_MASTER_REPOBJECT (
      gname => 'rep_groups',
      type  => 'TABLE',
      oname => 'ADDRESS',
      sname => 'c##dept_admin',
      use_existing_object => TRUE,
      copy_rows => false);

END;
/

BEGIN
   DBMS_REPCAT.CREATE_MASTER_REPOBJECT (
      gname => 'rep_groups',
      type  => 'TABLE',
      oname => 'CITY',
      sname => 'c##dept_admin',
      use_existing_object => TRUE,
      copy_rows => false);

END;
/


BEGIN
   DBMS_REPCAT.CREATE_MASTER_REPOBJECT (
      gname => 'rep_groups',
      type  => 'TABLE',
      oname => 'REGION',
      sname => 'c##dept_admin',
      use_existing_object => TRUE,
      copy_rows => false);

END;
/

BEGIN
   DBMS_REPCAT.CREATE_MASTER_REPOBJECT (
      gname => 'rep_groups',
      type  => 'TABLE',
      oname => 'PERSON',
      sname => 'c##dept_admin',
      use_existing_object => TRUE,
      copy_rows => false);

END;
/
--execute this at goods site, it is the master defenition site
begin
   DBMS_REPCAT.ADD_MASTER_DATABASE (

      gname => 'rep_groups',
      master => 'life.dept',
      use_existing_objects => TRUE,
      copy_rows => false,
      propagation_mode => 'ASYNCHRONOUS');
END;
/
begin
   DBMS_REPCAT.ADD_MASTER_DATABASE (

      gname => 'rep_groups',
      master => 'car.dept',
      use_existing_objects => TRUE,
      copy_rows => false,
		propagation_mode => 'ASYNCHRONOUS');
END;
/

begin
DBMS_REPCAT.ALTER_MASTER_PROPAGATION (
   gname  =>'rep_groups', 
   master => 'goods.dept',
   dblink_list => 'car.dept,life.dept',
   propagation_mode    =>'SYNCHRONOUS',
    comment    =>''
  );
end;
/


SELECT DBLINK FROM DBA_REPSITES WHERE GNAME = 'GROUPS';

sELECT table_owner, table_name, db_link
  FROM dba_synonyms 
 WHERE owner        = 'c##dept_admin';
 --no synonyms
 
 BEGIN
   DBMS_REPCAT.SUSPEND_MASTER_ACTIVITY (
      gname => 'rep_groups');
END;
/

BEGIN
    DBMS_REPCAT.GENERATE_REPLICATION_SUPPORT (
      sname => 'c##dept_admin',
      oname => 'city',
      type => 'TABLE',
      min_communication => TRUE);
END;
/

BEGIN
    DBMS_REPCAT.GENERATE_REPLICATION_SUPPORT (
      sname => 'c##dept_admin',
      oname => 'region',
      type => 'TABLE',
      min_communication => TRUE);
END;
/

BEGIN
    DBMS_REPCAT.GENERATE_REPLICATION_SUPPORT (
      sname => 'c##dept_admin',
      oname => 'person',
      type => 'TABLE',
      min_communication => TRUE);
END;
/


 
 BEGIN
    DBMS_REPCAT.GENERATE_REPLICATION_SUPPORT (
      sname => 'c##dept_admin',
      oname => 'address',
      type => 'TABLE',
      min_communication => TRUE);
END;
/


-- Start Replication
begin
 Dbms_Repcat.Resume_Master_Activity(gname => 'REP_GROUPS');
 end;
 /

SELECT COUNT(*) FROM DBA_REPCATLOG WHERE GNAME = 'REP_GROUPS';

select sname, master, status from dba_repgroup;
BEGIN
   DBMS_REPCAT.SUSPEND_MASTER_ACTIVITY (
      gname => 'rep_groups');
END;
/

delete FROM DBA_REPCATLOG WHERE GNAME = 'rep_groups';



execute DBMS_JOB.RUN(3);
http://www2.sas.com/proceedings/sugi27/p142-27.pdf


create view myView as select * from contract_car@car.dept;

--we use views to collect data from distributed databases
--collect time
create view v_years  as select distinct to_char(insurance_start_date,'yyyy') as year_l from contract_life@life.dept union select distinct to_char(insurance_start_date,'yyyy') as year_c from contract_car@car.dept union select distinct to_char(insurance_start_date,'yyyy') as year_g from contract_goods;
--views of refunds
create view v_refunds_cars as select id,id_contract,amount from refunds_car@car.dept;
create view v_refunds_life as select id,id_contract,amount from refunds_goods@goods.dept;
create view v_refunds_goods as select l.id,id_contract,compensation from refunds_life@life.dept l, contract_life@life.dept k  where id_contract=k.id;
--views of benefits
create view v_benefits_life as select id,annualPayment, insurance_start_date from contract_life@life.dept;
create view v_benefits_goods as select id,annualPayment, insurance_start_date from contract_goods@goods.dept;
create view v_benefits_car as select id,annualPayment, insurance_start_date from contract_car@car.dept;
--view of 


drop  view v_refunds_cars;
drop view v_refunds_life;
drop view v_refunds_goods;
drop view  v_benefits_life;
drop view v_benefits_goods;
drop view v_benefits_car;

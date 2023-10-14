INSERT INTO users (user_name, password, enabled)
values ('admin', '$2a$10$bltA1SzOChKZIYJe47s0IuboUDNJLY6KgLj76orBDSsgjUbd3NCla', true),
       ('user', '$2a$10$bltA1SzOChKZIYJe47s0IuboUDNJLY6KgLj76orBDSsgjUbd3NCla', true),
       ('accountant', '$2a$10$bltA1SzOChKZIYJe47s0IuboUDNJLY6KgLj76orBDSsgjUbd3NCla', true),
       ('hr', '$2a$10$bltA1SzOChKZIYJe47s0IuboUDNJLY6KgLj76orBDSsgjUbd3NCla', true),
       ('boss', '$2a$10$bltA1SzOChKZIYJe47s0IuboUDNJLY6KgLj76orBDSsgjUbd3NCla', true)
;

INSERT INTO authorities (id, user_name, authority_role)
values (default, 'admin', 'ADMIN'),
       (default, 'admin', 'EDIT'),
       (default, 'user', 'VIEW'),
       (default, 'accountant', 'VIEW'),
       (default, 'accountant', 'FIN'),
       (default, 'hr', 'EDIT'),
       (default, 'boss', 'EDIT'),
       (default, 'boss', 'FIN')
;
alter table departments drop constraint if exists FK_dep_manager;

INSERT INTO departments (department_id, department_name, manager_id)
values ('AD', 'Administration', 100),
       ('SA', 'Sales', 145),
       ('HR', 'Human Resources', 101),
       ('FI', 'Accounting', 108),
       ('IT', 'IT', 102),
       ('AN', 'Analytics department', 144),
       ('DO', 'Documentation department', 180),
       ('QA', 'Quality Assurance', 120),
       ('DV', 'Development', 205),
       ('IM', 'Implementation', 204),
       ('SP', 'Support', 114)
;

INSERT INTO jobs (job_id, job_title, min_salary, max_salary)
values ('AD_PRES', 'President', 20080, 40000),
       ('AD_VP', 'Administration Vice President', 15000, 30000),
       ('AD_ASST', 'Administration Assistant', 3000, 6000),
       ('SA_MAN', 'Sales Manager', 10000, 20080),
       ('SA_REP', 'Sales Representative', 6000, 12008),
       ('FI_MGR', 'Finance Manager', 8200, 16000),
       ('FI_ACCOUNT', 'Accountant', 4200, 9000),
       ('DV_CSA', 'Chief software architect', 8200, 16000),
       ('DV_ENG', 'Software Engeneer', 4200, 9000),
       ('DO_MAN', 'Documentation Manager', 8000, 15000),
       ('DO_CLERK', 'Documentation Clerk', 2500, 5500),
       ('AN_MAN', 'Analytics Manager', 5500, 8500),
       ('AN_CLERK', 'Analytic', 2008, 5000),
       ('SP_MAN', 'Support Manager', 4500, 4500),
       ('SP_CLERK', 'Support Clerk', 2500, 5500),
       ('IT_PROG', 'Programmer', 4000, 10000),
       ('QA_MAN', 'Quality Assurance Manager', 9000, 15000),
       ('QA_ENG', 'Quality Assurance Engeneer', 4000, 9000),
       ('HR_REP', 'Human Resources Representative', 4000, 9000),
       ('IM_ENG', 'Implementation Engeneer', 4500, 10500)
;
-- CFO	chief financial officer			финансовый директор
-- CTO	chief technical / technology officer	главный инженер компании (технический директор)
-- CCO	chief commercial officer		коммерческий директор
-- CCO	chief compliance officer		начальник отдела корпоративного регулирования и контроля
-- CBDO	chief business development officer	директор по развитию бизнеса
-- CBO	chief business officer			директор по управлению бизнесом
-- CIO	chief information officer		IT-директор
-- CPO	chief product officer			руководитель производственного отдела
-- CAO	chief administrative officer		директор по административным вопросам
-- CHRO	chief human resources officer		ведущий эксперт отдела кадров
-- CSO	chief security officer			начальник службы безопасности
-- CSA	chief software architect		руководитель отдела разработок программного обеспечения

INSERT INTO employees(employee_id, first_name, last_name, email, phone_number, hire_date, job_id, salary, manager_id, department_id)
VALUES
    (100, 'Steven', 'King', 'SKING', '+7(915)555-0100', '2013-06-17', 'AD_PRES', 24000, NULL, 'AD'),
    (101, 'Neena', 'Yang', 'NYANG', '+7(915)555-0101', '2015-09-21', 'AD_VP', 17000, 100, 'AD'),
    (102, 'Lex', 'Garcia', 'LGARCIA', '+7(915)555-0102', '2011-01-13', 'AD_VP', 17000, 100, 'AD'),
    (103, 'Alexander', 'James', 'AJAMES', '+7(990)555-0103', '2016-01-03', 'IT_PROG', 9000, 102, 'IT'),
    (104, 'Bruce', 'Miller', 'BMILLER', '+7(990)555-0104', '2017-05-21', 'IT_PROG', 6000, 103, 'IT'),
    (105, 'David', 'Williams', 'DWILLIAMS', '+7(990)555-0105', '2015-06-25', 'IT_PROG', 4800, 103, 'IT'),
    (106, 'Valli', 'Jackson', 'VJACKSON', '+7(990)555-0106', '2016-02-05', 'IT_PROG', 4800, 103, 'IT'),
    (107, 'Diana', 'Nguyen', 'DNGUYEN', '+7(990)555-0107', '2017-02-07', 'IT_PROG', 4200, 103, 'IT'),
    (108, 'Nancy', 'Gruenberg', 'NGRUENBE', '+7(915)555-0108', '2012-08-17', 'FI_MGR', 12008, 101, 'FI'),
    (109, 'Daniel', 'Faviet', 'DFAVIET', '+7(915)555-0109', '2012-08-16', 'FI_ACCOUNT', 9000, 108, 'FI'),
    (110, 'John', 'Chen', 'JCHEN', '+7(915)555-0110', '2015-09-28', 'FI_ACCOUNT', 8200, 108, 'FI'),
    (111, 'Ismael', 'Sciarra', 'ISCIARRA', '+7(915)555-0111', '2015-09-30', 'FI_ACCOUNT', 7700, 108, 'FI'),
    (112, 'Jose Manuel', 'Urman', 'JMURMAN', '+7(915)555-0112', '2016-03-07', 'FI_ACCOUNT', 7800, 108, 'FI'),
    (113, 'Luis', 'Popp', 'LPOPP', '+7(915)555-0113', '2017-12-07', 'FI_ACCOUNT', 6900, 108, 'FI'),
    (114, 'Den', 'Li', 'DLI', '+7(915)555-0114', '2012-12-07', 'SP_MAN', 11000, 100, 'SP'),
    (115, 'Alexander', 'Khoo', 'AKHOO', '+7(915)555-0115', '2013-05-18', 'SP_CLERK', 3100, 114, 'SP'),
    (116, 'Shelli', 'Baida', 'SBAIDA', '+7(915)555-0116', '2015-12-24', 'SP_CLERK', 2900, 114, 'SP'),
    (117, 'Sigal', 'Tobias', 'STOBIAS', '+7(915)555-0117', '2015-07-24', 'SP_CLERK', 2800, 114, 'SP'),
    (118, 'Guy', 'Himuro', 'GHIMURO', '+7(915)555-0118', '2016-11-15', 'SP_CLERK', 2600, 114, 'SP'),
    (119, 'Karen', 'Colmenares', 'KCOLMENA', '+7(915)555-0119', '2017-08-10', 'SP_CLERK', 2500, 114, 'SP'),
    (120, 'Matthew', 'Weiss', 'MWEISS', '+7(950)555-0120', '2014-07-18', 'QA_MAN', 8000, 100, 'QA'),
    (121, 'Adam', 'Fripp', 'AFRIPP', '+7(950)555-0121', '2015-04-10', 'QA_MAN', 8200, 100, 'QA'),
    (122, 'Payam', 'Kaufling', 'PKAUFLIN', '+7(950)555-0122', '2013-05-01', 'QA_MAN', 7900, 100, 'QA'),
    (123, 'Shanta', 'Vollman', 'SVOLLMAN', '+7(950)555-0123', '2015-10-10', 'QA_MAN', 6500, 100, 'QA'),
    (124, 'Kevin', 'Mourgos', 'KMOURGOS', '+7(950)555-0124', '2017-11-16', 'QA_MAN', 5800, 100, 'QA'),
    (125, 'Julia', 'Nayer', 'JNAYER', '+7(950)555-0125', '2015-07-16', 'QA_ENG', 3200, 120, 'QA'),
    (126, 'Irene', 'Mikkilineni', 'IMIKKILI', '+7(950)555-0126', '2016-09-28', 'QA_ENG', 2700, 120, 'QA'),
    (127, 'James', 'Landry', 'JLANDRY', '+7(950)555-0127', '2017-01-14', 'QA_ENG', 2400, 120, 'QA'),
    (128, 'Steven', 'Markle', 'SMARKLE', '+7(950)555-0128', '2018-03-08', 'QA_ENG', 2200, 120, 'QA'),
    (129, 'Laura', 'Bissot', 'LBISSOT', '+7(950)555-0129', '2015-08-20', 'QA_ENG', 3300, 121, 'QA'),
    (130, 'Mozhe', 'Atkinson', 'MATKINSO', '+7(950)555-0130', '2015-10-30', 'QA_ENG', 2800, 121, 'QA'),
    (131, 'James', 'Marlow', 'JAMRLOW', '+7(950)555-0131', '2015-02-16', 'QA_ENG', 2500, 121, 'QA'),
    (132, 'TJ', 'Olson', 'TJOLSON', '+7(950)555-0132', '2017-04-10', 'QA_ENG', 2100, 121, 'QA'),
    (133, 'Jason', 'Mallin', 'JMALLIN', '+7(950)555-0133', '2014-06-14', 'QA_ENG', 3300, 122, 'QA'),
    (134, 'Michael', 'Rogers', 'MROGERS', '+7(950)555-0134', '2016-08-26', 'QA_ENG', 2900, 122, 'QA'),
    (135, 'Ki', 'Gee', 'KGEE', '+7(950)555-0135', '2017-12-12', 'QA_ENG', 2400, 122, 'QA'),
    (136, 'Hazel', 'Philtanker', 'HPHILTAN', '+7(950)555-0136', '2018-02-06', 'QA_ENG', 2200, 122, 'QA'),
    (137, 'Renske', 'Ladwig', 'RLADWIG', '+7(950)555-0137', '2013-07-14', 'QA_ENG', 3600, 123, 'QA'),
    (138, 'Stephen', 'Stiles', 'SSTILES', '+7(950)555-0138', '2015-10-26', 'QA_ENG', 3200, 123, 'QA'),
    (139, 'John', 'Seo', 'JSEO', '+7(950)555-0139', '2016-02-12', 'QA_ENG', 2700, 123, 'QA'),
    (140, 'Joshua', 'Patel', 'JPATEL', '+7(950)555-0140', '2016-04-06', 'QA_ENG', 2500, 123, 'QA'),
    (141, 'Trenna', 'Rajs', 'TRAJS', '+7(950)555-0141', '2013-10-17', 'QA_ENG', 3500, 124, 'QA'),
    (142, 'Curtis', 'Davies', 'CDAVIES', '+7(950)555-0142', '2015-01-29', 'QA_ENG', 3100, 124, 'QA'),
    (143, 'Randall', 'Matos', 'RMATOS', '+7(950)555-0143', '2016-03-15', 'AN_MAN', 2600, 102, 'AN'),
    (144, 'Peter', 'Vargas', 'PVARGAS', '+7(950)555-0144', '2016-07-09', 'AN_CLERK', 2500, 143, 'AN'),
    (145, 'John', 'Singh', 'JSINGH', '+7(963)296-0000', '2014-10-01', 'SA_MAN', 14000, 100, 'SA'),
    (146, 'Karen', 'Partners', 'KPARTNER', '+7(963)296-0001', '2015-01-05', 'SA_MAN', 13500, 100, 'SA'),
    (147, 'Alberto', 'Errazuriz', 'AERRAZUR', '+7(963)296-0002', '2015-03-10', 'SA_MAN', 12000, 100, 'SA'),
    (148, 'Gerald', 'Cambrault', 'GCAMBRAU', '+7(963)296-0003', '2017-10-15', 'SA_MAN', 11000, 100, 'SA'),
    (149, 'Eleni', 'Zlotkey', 'EZLOTKEY', '+7(963)296-0004', '2018-01-29', 'SA_MAN', 10500, 100, 'SA'),
    (150, 'Sean', 'Tucker', 'STUCKER', '+7(963)296-0005', '2015-01-30', 'SA_REP', 10000, 145, 'SA'),
    (151, 'David', 'Bernstein', 'DBERNSTE', '+7(963)296-0006', '2015-03-24', 'SA_REP', 9500, 145, 'SA'),
    (152, 'Peter', 'Hall', 'PHALL', '+7(963)296-0007', '2015-08-20', 'SA_REP', 9000, 145, 'SA'),
    (153, 'Christopher', 'Olsen', 'COLSEN', '+7(963)296-0008', '2016-03-30', 'SA_REP', 8000, 145, 'SA'),
    (154, 'Nanette', 'Cambrault', 'NCAMBRAU', '+7(963)296-0009', '2016-12-09', 'SA_REP', 7500, 145, 'SA'),
    (155, 'Oliver', 'Tuvault', 'OTUVAULT', '+7(963)296-0010', '2017-11-23', 'SA_REP', 7000, 145, 'SA'),
    (156, 'Janette', 'King', 'JKING', '+7(963)296-0011', '2014-01-30', 'SA_REP', 10000, 146, 'SA'),
    (157, 'Patrick', 'Sully', 'PSULLY', '+7(963)296-0012', '2014-03-04', 'SA_REP', 9500, 146, 'SA'),
    (158, 'Allan', 'McEwen', 'AMCEWEN', '+7(963)296-0013', '2014-08-01', 'SA_REP', 9000, 146, 'SA'),
    (159, 'Lindsey', 'Smith', 'LSMITH', '+7(963)296-0014', '2015-03-10', 'SA_REP', 8000, 146, 'SA'),
    (160, 'Louise', 'Doran', 'LDORAN', '+7(963)296-0015', '2015-12-15', 'SA_REP', 7500, 146, 'SA'),
    (161, 'Sarath', 'Sewall', 'SSEWALL', '+7(963)296-0016', '2016-11-03', 'SA_REP', 7000, 146, 'SA'),
    (162, 'Clara', 'Vishney', 'CVISHNEY', '+7(963)296-0017', '2015-11-11', 'SA_REP', 10500, 147, 'SA'),
    (163, 'Danielle', 'Greene', 'DGREENE', '+7(963)296-0018', '2017-03-19', 'SA_REP', 9500, 147, 'SA'),
    (164, 'Mattea', 'Marvins', 'MMARVINS', '+7(963)296-0019', '2018-01-24', 'SA_REP', 7200, 147, 'SA'),
    (165, 'David', 'Lee', 'DLEE', '+7(963)296-0020', '2018-02-23', 'SA_REP', 6800, 147, 'SA'),
    (166, 'Sundar', 'Ande', 'SANDE', '+7(963)296-0021', '2018-03-24', 'SA_REP', 6400, 147, 'SA'),
    (167, 'Amit', 'Banda', 'ABANDA', '+7(963)296-0022', '2018-04-21', 'SA_REP', 6200, 147, 'SA'),
    (168, 'Lisa', 'Ozer', 'LOZER', '+7(963)296-0023', '2015-03-11', 'SA_REP', 11500, 148, 'SA'),
    (169, 'Harrison', 'Bloom', 'HBLOOM', '+7(963)296-0024', '2016-03-23', 'SA_REP', 10000, 148, 'SA'),
    (170, 'Tayler', 'Fox', 'TFOX', '+7(963)296-0025', '2016-01-24', 'SA_REP', 9600, 148, 'SA'),
    (171, 'William', 'Smith', 'WSMITH', '+7(963)296-0026', '2017-02-23', 'SA_REP', 7400, 148, 'SA'),
    (172, 'Elizabeth', 'Bates', 'EBATES', '+7(963)296-0027', '2017-03-24', 'SA_REP', 7300, 148, 'SA'),
    (173, 'Sundita', 'Kumar', 'SKUMAR', '+7(963)296-0028', '2018-04-21', 'SA_REP', 6100, 148, 'SA'),
    (174, 'Ellen', 'Abel', 'EABEL', '+7(963)296-0029', '2014-05-11', 'SA_REP', 11000, 149, 'SA'),
    (175, 'Alyssa', 'Hutton', 'AHUTTON', '+7(963)296-0030', '2015-03-19', 'SA_REP', 8800, 149, 'SA'),
    (176, 'Jonathon', 'Taylor', 'JTAYLOR', '+7(963)296-0031', '2016-03-24', 'SA_REP', 8600, 149, 'SA'),
    (177, 'Jack', 'Livingston', 'JLIVINGS', '+7(963)296-0032', '2016-04-23', 'SA_REP', 8400, 149, 'SA'),
    (178, 'Kimberely', 'Grant', 'KGRANT', '+7(963)296-0033', '2017-05-24', 'SA_REP', 7000, 149, 'SA'),
    (179, 'Charles', 'Johnson', 'CJOHNSON', '+7(963)296-0034', '2018-01-04', 'SA_REP', 6200, 149, 'SA'),
    (180, 'Winston', 'Taylor', 'WTAYLOR', '+7(950)555-0145', '2016-01-24', 'DO_MAN', 3200, 120, 'DO'),
    (181, 'Jean', 'Fleaur', 'JFLEAUR', '+7(950)555-0146', '2016-02-23', 'DO_CLERK', 3100, 180, 'DO'),
    (182, 'Martha', 'Sullivan', 'MSULLIVA', '+7(950)555-0147', '2017-06-21', 'DO_CLERK', 2500, 180, 'DO'),
    (183, 'Girard', 'Geoni', 'GGEONI', '+7(950)555-0148', '2018-02-03', 'DO_CLERK', 2800, 180, 'DO'),
    (184, 'Nandita', 'Sarchand', 'NSARCHAN', '+7(950)555-0149', '2014-01-27', 'DO_CLERK', 4200, 180, 'DO'),
    (185, 'Alexis', 'Bull', 'ABULL', '+7(950)555-0150', '2015-02-20', 'DO_CLERK', 4100, 180, 'DO'),
    (186, 'Julia', 'Dellinger', 'JDELLING', '+7(950)555-0151', '2016-06-24', 'DO_CLERK', 3400, 180, 'DO'),
    (187, 'Anthony', 'Cabrio', 'ACABRIO', '+7(950)555-0152', '2017-02-07', 'DO_CLERK', 3000, 180, 'DO'),
    (188, 'Kelly', 'Chung', 'KCHUNG', '+7(950)555-0153', '2015-06-14', 'DO_CLERK', 3800, 180, 'DO'),
    (189, 'Jennifer', 'Dilly', 'JDILLY', '+7(950)555-0154', '2015-08-13', 'DO_CLERK', 3600, 180, 'DO'),
    (190, 'Timothy', 'Venzl', 'TVENZL', '+7(950)555-0155', '2016-07-11', 'DO_CLERK', 2900, 180, 'DO'),
    (191, 'Randall', 'Perkins', 'RPERKINS', '+7(950)555-0156', '2017-12-19', 'DO_CLERK', 2500, 180, 'DO'),
    (192, 'Sarah', 'Bell', 'SBELL', '+7(950)555-0157', '2014-02-04', 'DO_CLERK', 4000, 180, 'DO'),
    (193, 'Britney', 'Everett', 'BEVERETT', '+7(950)555-0158', '2015-03-03', 'DO_CLERK', 3900, 180, 'DO'),
    (194, 'Samuel', 'McLeod', 'SMCLEOD', '+7(950)555-0159', '2016-07-01', 'DO_CLERK', 3200, 180, 'DO'),
    (195, 'Vance', 'Jones', 'VJONES', '+7(950)555-0160', '2017-03-17', 'DO_CLERK', 2800, 180, 'DO'),
    (196, 'Alana', 'Walsh', 'AWALSH', '+7(950)555-0161', '2016-04-24', 'DO_CLERK', 3100, 180, 'DO'),
    (197, 'Kevin', 'Feeney', 'KFEENEY', '+7(950)555-0162', '2016-05-23', 'DO_CLERK', 3000, 180, 'DO'),
    (198, 'Donald', 'OConnell', 'DOCONNEL', '+7(950)555-0163', '2017-06-21', 'DO_CLERK', 2600, 180, 'DO'),
    (199, 'Douglas', 'Grant', 'DGRANT', '+7(950)555-0164', '2018-01-13', 'DO_CLERK', 2600, 180, 'DO'),
    (200, 'Jennifer', 'Whalen', 'JWHALEN', '+7(915)555-0165', '2013-09-17', 'AD_ASST', 4400, 101, 'AD'),
    (201, 'Michael', 'Martinez', 'MMARTINE', '+7(915)555-0166', '2014-02-17', 'QA_MAN', 13000, 100, 'QA'),
    (202, 'Pat', 'Davis', 'PDAVIS', '+7(903)555-0167', '2015-08-17', 'QA_ENG', 6000, 201, 'QA'),
    (203, 'Susan', 'Jacobs', 'SJACOBS', '+7(915)555-0168', '2012-06-07', 'HR_REP', 6500, 101, 'HR'),
    (204, 'Hermann', 'Brown', 'HBROWN', '+7(915)555-0169', '2012-06-07', 'IM_ENG', 10000, 101, 'IM'),
    (205, 'Shelley', 'Higgins', 'DOIGGINS', '+7(915)555-0170', '2012-06-07', 'DV_CSA', 12008, 101, 'DV'),
    (206, 'William', 'Gietz', 'WGIETZ', '+7(915)555-0171', '2012-06-07', 'DV_ENG', 8300, 205, 'DV')
;

INSERT INTO job_history(job_history_id, employee_id, start_date, end_date, job_id, department_id)
values (1, 102, '2011-01-13', '2016-07-24', 'IT_PROG', 'IT'),
       (2, 101, '2007-09-21', '2011-10-27', 'FI_ACCOUNT', 'FI'),
       (3, 101, '2011-10-28', '2015-03-15', 'FI_MGR', 'FI'),
       (4, 201, '2014-02-17', '2017-12-19', 'QA_ENG', 'QA'),
       (5, 114, '2016-03-24', '2017-12-31', 'SP_CLERK', 'SP'),
       (6, 122, '2017-01-01', '2017-12-31', 'SP_CLERK', 'SP'),
       (7, 200, '2005-09-17', '2011-06-17', 'AD_ASST', 'AD'),
       (8, 176, '2016-03-24', '2016-12-31', 'SA_REP', 'SA'),
       (9, 176, '2017-01-01', '2017-12-31', 'SA_MAN', 'SA'),
       (10, 200, '2012-07-01', '2016-12-31', 'FI_ACCOUNT', 'FI')
;

alter table departments
    add constraint FK_dep_manager foreign key (manager_id) references employees (employee_id);

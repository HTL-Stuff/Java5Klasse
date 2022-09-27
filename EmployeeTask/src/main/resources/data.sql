INSERT INTO staff
VALUES ('HUBE', 'Franz', 'Huber');
INSERT INTO staff
VALUES ('SCMI', 'Barbara', 'Schmidt');
INSERT INTO staff
VALUES ('BAUE', 'Fritz', 'Bauer');

INSERT INTO tasks (description, finished_date, hours_worked, employee_id)
VALUES ('Implementierung JUnit Tests', '2019-05-17', 120, 'HUBE');
INSERT INTO tasks (description, finished_date, hours_worked, employee_id)
VALUES ('Erstellung UML-Diagramm', '2019-05-17', 90, 'SCMI');
INSERT INTO tasks (description, finished_date, hours_worked, employee_id)
VALUES ('Projektmeeting', '2019-05-18', 60, 'HUBE');
INSERT INTO tasks (description, finished_date, hours_worked, employee_id)
VALUES ('Projektmeeting', '2019-05-18', 60, 'SCMI');
INSERT INTO tasks (description, finished_date, hours_worked, employee_id)
VALUES ('Projektmeeting', '2019-05-18', 60, 'BAUE');
INSERT INTO tasks (description, finished_date, hours_worked, employee_id)
VALUES ('Implementierung', '2019-05-19', 350, 'BAUE');
INSERT INTO tasks (description, finished_date, hours_worked, employee_id)
VALUES ('Implementierung', '2019-05-19', 420, 'SCMI');
INSERT INTO tasks (description, finished_date, hours_worked, employee_id)
VALUES ('Tests und Bugfixes', '2019-05-19', 300, 'HUBE');
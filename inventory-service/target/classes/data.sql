INSERT INTO inventories (id, name)
VALUES (1, 'Amazon');
INSERT INTO inventorylookup (id, iid, pid, quantity)
VALUES (1, 1, 1, 200);
INSERT INTO inventorylookup (id, iid, pid, quantity)
VALUES (2, 1, 2, 100);
INSERT INTO inventorylookup (id, iid, pid, quantity)
VALUES (3, 1, 3, 1000);
INSERT INTO inventorylookup (id, iid, pid, quantity)
VALUES (4, 1, 4, 1500);
INSERT INTO users (username, password, loggedin)
VALUES ('admin', 'admin', false);
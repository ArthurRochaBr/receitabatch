DROP TABLE CONTA IF EXISTS;

CREATE TABLE CONTA (
	agencia VARCHAR(20),
	conta VARCHAR(20),
	saldo DOUBLE(40,2),
	status VARCHAR(20),
	resultado VARCHAR(20)
);
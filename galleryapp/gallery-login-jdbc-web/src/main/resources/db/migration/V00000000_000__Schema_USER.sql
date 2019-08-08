CREATE TABLE user_master (
	login_id VARCHAR(100) NOT NULL,
	passwd VARCHAR(100) NOT NULL,
	CONSTRAINT user_master_pkc PRIMARY KEY (login_id)
);

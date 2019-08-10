CREATE TABLE item_master (
	id IDENTITY NOT NULL,
	name VARCHAR(100) NOT NULL,
	price DECIMAL(10) NOT NULL,
	lock_ver BIGINT NOT NULL DEFAULT 1,
	CONSTRAINT item_master PRIMARY KEY (id)
);
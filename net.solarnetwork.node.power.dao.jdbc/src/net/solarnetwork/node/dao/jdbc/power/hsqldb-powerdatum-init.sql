CREATE CACHED TABLE solarnode.sn_power_datum (
	id				BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
	created			TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	pv_volts 		DOUBLE,
	pv_amps			DOUBLE,
	bat_volts		DOUBLE,
	bat_amp_hrs		DOUBLE,
	dc_out_volts	DOUBLE,
	dc_out_amps		DOUBLE,
	ac_out_volts	DOUBLE,
	ac_out_amps		DOUBLE,
	kwatt_hours		DOUBLE,
	amp_hours		DOUBLE,
	error_msg		VARCHAR(32672),
	PRIMARY KEY (id)
);

CREATE INDEX power_datum_created_idx ON solarnode.sn_power_datum (created);

INSERT INTO solarnode.sn_settings (skey, svalue) 
VALUES ('solarnode.sn_power_datum.version', '5');

CREATE CACHED TABLE solarnode.sn_power_datum_upload (
	power_datum_id	BIGINT NOT NULL,
	destination		VARCHAR(255) NOT NULL,
	created			TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	track_id		BIGINT NOT NULL,
	PRIMARY KEY (power_datum_id, destination),
	CONSTRAINT sn_power_datum_upload_power_datum_fk FOREIGN KEY (power_datum_id)
		REFERENCES solarnode.sn_power_datum ON DELETE CASCADE
);

INSERT INTO solarnode.sn_settings (skey, svalue) 
VALUES ('solarnode.sn_power_datum_upload.version', '1');

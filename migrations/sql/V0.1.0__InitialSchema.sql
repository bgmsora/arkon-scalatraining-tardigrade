CREATE EXTENSION IF NOT EXISTS "postgis";

DROP TABLE IF EXISTS comercial_activity;

CREATE TABLE comercial_activity (
    id INT PRIMARY KEY
    , name TEXT NOT NULL
);

DROP TABLE IF EXISTS stratum;

CREATE TABLE stratum (
    id INT PRIMARY KEY
    , name TEXT NOT NULL
);

DROP TABLE IF EXISTS shop_type;

CREATE TABLE shop_type (
    id INT PRIMARY KEY
    , name TEXT NOT NULL
);

DROP TABLE IF EXISTS shop;

CREATE TABLE shop (
    id INT PRIMARY KEY
    , name TEXT NOT NULL
    , business_name TEXT
    , activity_id INT REFERENCES comercial_activity (id)
    , stratum_id INT REFERENCES stratum (id)
    , address TEXT NOT NULL
    , phone_number TEXT
    , email TEXT
    , website TEXT
    , shop_type_id INT REFERENCES shop_type (id)
    , position GEOGRAPHY (POINT) NOT NULL
);

--Datos para probar
INSERT INTO comercial_activity  (id,"name") VALUES
     (1,'Test '),
	 (112311,'Explotación de gallinas para la producción de huevo fértil '),
	 (112312,'Explotación de gallinas para la producción de huevo para plato '),
	 (11232,'Explotación de pollos para la producción de carne'),
	 (112320,'Explotación de pollos para la producción de carne '),
	 (11233,'Explotación de guajolotes o pavos'),
	 (112330,'Explotación de guajolotes o pavos ');
INSERT INTO stratum (id,"name") VALUES
    (1,'0 a 5 personas'),
    (2,'6 a 10 personas'),
    (3,'11 a 30 personas'),
    (4,'31 a 50 personas'),
    (5,'51 a 100 personas'),
    (6,'101 a 250 personas'),
    (7,'251 y más personas');
INSERT INTO shop_type (id,"name") VALUES
    (1,'Alquiler sin intermediación de salones para fiestas y convenciones'),
    (2,'Consultorios dentales del sector privado'),
    (3,'otro');
INSERT INTO shop (id,"name","business_name","activity_id","stratum_id","address","phone_number","email","website","shop_type_id","position") VALUES
    (34186,'ConsultorioDentalSinNombre','Consultorios dentales del sector privado',112311,1,'AGUASCALIENTES, Aguascalientes, AGUASCALIENTES','4499137286','example@gmail.com','abc.com', 1, ST_MakePoint(19.432406173983384, -99.13364393069804)::geography);
CREATE TABLE [dbo].[species](
	[id] [float] NULL,
	[scientific_name] [nvarchar](255) NULL,
	[genus] [nvarchar](255) NULL,
	[family] [nvarchar](255) NULL,
	[common_name] [nvarchar](255) NULL,
	[image_url] [nvarchar](255) NULL,
	[light] [nvarchar](255) NULL,
	[url_wikipedia_en] [nvarchar](255) NULL,
	[water_frequency] [nvarchar](255) NULL
) ON [PRIMARY]
GO

CREATE TABLE User(
	id SERIAL PRIMARY Key,
	username NVARCHAR (25) NOT NULL,
	email VARCHAR (50),
	password VARCHAR (100) NOT NULL,
	 notification_activated BOOLEAN,
  	fun_facts_activated BOOLEAN
);



CREATE TABLE Plant (
    id SERIAL NOT NULL,
    user_id INT NOT NULL,
    nickname VARCHAR(255),
    last_watered DATE NOT NULL,
    plant_id VARCHAR(255) NOT NULL,
    image_url VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_id, nickname),
    FOREIGN KEY (user_id) REFERENCES User (id)
);

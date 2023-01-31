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

CREATE TABLE [User] (
id INT identity(1,1) NOT NULL,
username NVARCHAR(25) NOT NULL,
email NVARCHAR(50) NULL,
password NVARCHAR(100) NOT NULL,
notification_activated BIT NULL,
fun_facts_activated BIT NULL,
PRIMARY KEY (id));







CREATE TABLE [Plant] (
id INT identity(1,1) NOT NULL,
user_id INT NOT NULL,
nickname NVARCHAR(255),
last_watered DATE NOT NULL,
plant_id NCHAR(255) NOT NULL,
image_url NCHAR(255) NOT NULL,
PRIMARY KEY(user_id, nickname),
FOREIGN KEY (user_id) REFERENCES [User] (id));

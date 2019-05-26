CREATE DATABASE COFFEEHOUSE
GO
USE COFFEEHOUSE
GO
CREATE TABLE TypeRight
(
	id	INT	PRIMARY KEY, 
	typeName NVARCHAR(100) NOT NULL
)
GO 
CREATE TABLE Account
(
	id	INT	PRIMARY KEY IDENTITY, 
	displayName NVARCHAR(100) NOT NULL,
	username  NVARCHAR(50) NOT NULL,
	pass  NVARCHAR(100) NOT NULL,
	TypeRight INT, --0: admin, 1: staff
	FOREIGN KEY (TypeRight) REFERENCES dbo.TypeRight(id)
)
GO
CREATE TABLE FoodCategory
(
	id INT PRIMARY KEY IDENTITY,
	nameCategory NVARCHAR(100) NOT NULL 
)
GO 
CREATE TABLE Food
(
	id INT PRIMARY KEY IDENTITY,
	nameFood NVARCHAR(100) NOT NULL,
	price FLOAT NOT NULL DEFAULT 0,
	idCategory INT NOT NULL,
	FOREIGN KEY (idCategory) REFERENCES dbo.FoodCategory(id),
)
GO
CREATE TABLE TableFood
(
	id INT PRIMARY KEY IDENTITY,
	nameTable NVARCHAR(100) NOT NULL,
	tableStatus NVARCHAR(100) DEFAULT N'Trống' , -- 0: chua dat ban && 1: da dat ban,
)
GO 
CREATE TABLE Bill
(
	id INT PRIMARY KEY IDENTITY,
	DateCheckIn DATETIME NOT NULL DEFAULT GETDATE(),
	DateCheckOut DATETIME,
	idTable INT NOT NULL,
	BillStatus INT NOT NULL DEFAULT 0, -- 0: chua thanh toan && 1: da thanh toan,
	totalPrice INT NOT NULL DEFAULT 0,
	FOREIGN KEY (idTable) REFERENCES dbo.TableFood(id)
)
GO 
CREATE TABLE BillDetail
(
	id INT PRIMARY KEY IDENTITY,
	idBill INT NOT NULL,
	idFood INT NOT NULL,
	quantity INT NOT NULL DEFAULT 0,
	FOREIGN KEY(idBill) REFERENCES dbo.Bill(id),
	FOREIGN KEY (idFood) REFERENCES dbo.Food(id)
)
GO 
CREATE TABLE OrderTable(
	id INT PRIMARY KEY IDENTITY,
	nameCustomer NVARCHAR(100) NOT NULL,
	phone NVARCHAR(12) NOT NULL,
	checkin VARCHAR(100) NOT NULL,
	timeOrder VARCHAR(100) DEFAULT GETDATE(),
	idTable INT NOT NULL,
	FOREIGN KEY (idTable) REFERENCES dbo.TableFood(id)
)
GO
INSERT dbo.TypeRight (id, typeName) VALUES (0,N'Quản lí')
INSERT dbo.TypeRight (id, typeName) VALUES (1,N'Nhân viên thu ngân') 
GO
INSERT dbo.Account (displayName,username,pass,TypeRight) VALUES (N'Hồ Quốc Thông',N'admin',N'202cb962ac59075b964b07152d234b70', 0)
INSERT dbo.Account (displayName,username,pass,TypeRight) VALUES (N'Trần Thị Tường Vi',N'nvtn01',N'202cb962ac59075b964b07152d234b70', 1)
INSERT dbo.Account (displayName,username,pass,TypeRight) VALUES (N'Nguyễn Sỹ Quyết',N'nvtn02',N'202cb962ac59075b964b07152d234b70', 1)
GO 
--Add FoodCategory
INSERT dbo.FoodCategory (nameCategory) VALUES (N'Vietnamese coffee')
INSERT dbo.FoodCategory (nameCategory) VALUES (N'Espresso')
INSERT dbo.FoodCategory (nameCategory) VALUES (N'Special tea')
INSERT dbo.FoodCategory (nameCategory) VALUES (N'Ice Blended Coffee')
INSERT dbo.FoodCategory (nameCategory) VALUES (N'Chocolate')
INSERT dbo.FoodCategory (nameCategory) VALUES (N'Mocktail & Smoothie')
INSERT dbo.FoodCategory (nameCategory) VALUES (N'Food')
--Add Food
INSERT dbo.Food (nameFood,price,idCategory) VALUES (N'Cà phê đen',29000, 1)
INSERT dbo.Food (nameFood,price,idCategory) VALUES (N'Cà phê sữa',29000, 1)
INSERT dbo.Food (nameFood,price,idCategory) VALUES (N'Bạc xỉu',29000, 1)
INSERT dbo.Food (nameFood,price,idCategory) VALUES (N'Espresso',35000, 2)
INSERT dbo.Food (nameFood,price,idCategory) VALUES (N'Condensed Milk Espresso',35000, 2)
INSERT dbo.Food (nameFood,price,idCategory) VALUES (N'Americano',39000, 2)
INSERT dbo.Food (nameFood,price,idCategory) VALUES (N'Cappuccino',45000, 2)
INSERT dbo.Food (nameFood,price,idCategory) VALUES (N'Late',45000, 2)
INSERT dbo.Food (nameFood,price,idCategory) VALUES (N'Mocha',49000, 2)
INSERT dbo.Food (nameFood,price,idCategory) VALUES (N'Black Tea',35000, 3)
INSERT dbo.Food (nameFood,price,idCategory) VALUES (N'Black Tea Macchiato',38000, 3)
INSERT dbo.Food (nameFood,price,idCategory) VALUES (N'Matcha Latte',55000, 3)
INSERT dbo.Food (nameFood,price,idCategory) VALUES (N'Peach Tea Mania',42000, 3)

INSERT dbo.Food (nameFood,price,idCategory) VALUES (N'Mocha Ice Blended',59000, 4)
INSERT dbo.Food (nameFood,price,idCategory) VALUES (N'Cookie Ice Blended',59000, 4)
INSERT dbo.Food (nameFood,price,idCategory) VALUES (N'Hot Chocolate',52000, 5)
INSERT dbo.Food (nameFood,price,idCategory) VALUES (N'Chocolate Ice Blended',52000, 5)
INSERT dbo.Food (nameFood,price,idCategory) VALUES (N'Green Apple Soda',45000, 6)
INSERT dbo.Food (nameFood,price,idCategory) VALUES (N'Fresh Orange Juice',40000, 6)
INSERT dbo.Food (nameFood,price,idCategory) VALUES (N'Mojito Lemon',45000, 6)
INSERT dbo.Food (nameFood,price,idCategory) VALUES (N'Sandwich',35000, 7)
INSERT dbo.Food (nameFood,price,idCategory) VALUES (N'Phở bò',49000, 7)
INSERT dbo.Food (nameFood,price,idCategory) VALUES (N'Phở gà',45000, 7)
--Add FoodTable
DECLARE @i INT = 1
WHILE @i <=20
BEGIN
	INSERT dbo.TableFood (nameTable) VALUES (N''+@i+'')
	SET @i = @i +1
END

GO
INSERT dbo.Bill (DateCheckIn,DateCheckOut,idTable, BillStatus,totalPrice) VALUES ( N'2019-03-11 16:34:42.127',  N'2019-03-11 16:34:42.127', 2, 1, 400000 )
INSERT dbo.Bill (DateCheckIn,DateCheckOut,idTable, BillStatus,totalPrice) VALUES ( N'2019-02-11 16:34:42.127',  N'2019-02-11 16:34:42.127', 3, 1, 200000 )
INSERT dbo.Bill (DateCheckIn,DateCheckOut,idTable, BillStatus,totalPrice) VALUES ( N'2019-01-11 16:34:42.127',  N'2019-01-11 16:34:42.127', 7, 1, 500000 )
INSERT dbo.Bill (DateCheckIn,DateCheckOut,idTable, BillStatus,totalPrice) VALUES ( N'2019-04-11 16:34:42.127',  N'2019-04-11 16:34:42.127', 6, 1, 800000 )
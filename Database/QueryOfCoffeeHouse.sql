USE COFFEEHOUSE
GO 
--Tao stored procedure de bao mat (loi SQL Injection)
--SELECT *FROM dbo.Account WHERE username ='admin' AND pass='1' OR 1=1
CREATE PROC USP_Login
@username nvarchar(100), @pass nvarchar(100)
AS
BEGIN 
	SELECT a.id,a.displayName,a.username,a.pass,t.typeName FROM dbo.Account AS a,dbo.TypeRight AS t WHERE username = @username AND pass= @pass
END
GO 
CREATE PROC USP_GetAccountByUsername
@username nvarchar(100)
AS
BEGIN 
	SELECT a.id,a.displayName,a.username,a.pass,t.typeName FROM dbo.Account AS a,dbo.TypeRight AS t WHERE username = @username AND a.TypeRight = t.id
END
GO 
--Lay table list
CREATE PROC USP_GetTableList
AS 
BEGIN
	SELECT * FROM dbo.TableFood
END
GO 
CREATE PROC USP_GetTableListEmpty
AS 
BEGIN
	SELECT * FROM dbo.TableFood
END
GO 
--Lay uncheck bill
CREATE PROC USP_GetUncheckBill
@id int
AS 
BEGIN
	SELECT * FROM dbo.Bill WHERE idTable=@id AND BillStatus = 0
END
GO
--Lay list bill detail
CREATE PROC USP_GetListBillInfo
@id int
AS 
BEGIN
	SELECT * FROM dbo.BillDetail WHERE idBill =@id
END

GO 
--Hoa don
CREATE PROC USP_GetListMenu
@id int
AS 
BEGIN
	SELECT f.nameFood, bd.quantity, f.price, f.price*bd.quantity AS totalPrice 
	FROM dbo.BillDetail AS bd, dbo.Bill AS b, dbo.Food AS f 
	WHERE bd.idBill=b.id AND bd.idFood =f.id AND b.idTable = @id AND b.BillStatus = 0
END
GO 
--Load comboboxFoodCategory
CREATE PROC USP_GetFoodCategory
AS 
BEGIN
	SELECT * FROM dbo.FoodCategory AS fc
END

GO 
--Load comboxFood
CREATE PROC USP_GetFoodByCategory
@idCategory int
AS 
BEGIN
	SELECT * FROM dbo.Food AS f WHERE f.idCategory = @idCategory
END
--EXEC dbo.USP_GetFoodByCategory @idCategory = 1 -- int
GO 
--InsertBill ->Bill moi
CREATE PROC USP_InsertBill
@idTable INT 
AS
BEGIN
	INSERT dbo.Bill (DateCheckIn, DateCheckOut,idTable,BillStatus)
	VALUES (GETDATE(), NULL , @idTable, 0)
END 
GO 
--Insert BillDetail
CREATE PROC USP_InsertBillDetail
@idBill INT, @idFood INT , @quantity INT 
AS
BEGIN
	DECLARE @isExitBillDetail INT
	DECLARE @foodQuantity INT = 1;

	SELECT @isExitBillDetail = id, @foodQuantity = bd.quantity
	FROM dbo.BillDetail AS bd
	WHERE bd.idBill = @idBill AND bd.idFood = @idFood
	
	IF(@isExitBillDetail >0)
		BEGIN
			DECLARE @newQuantity INT = @foodQuantity + @quantity
			IF(@newQuantity > 0)
				UPDATE dbo.BillDetail SET quantity = @foodQuantity + @quantity WHERE idFood = @idFood
			ELSE 
				DELETE dbo.BillDetail WHERE idBill = @idBill AND idFood = @idFood
		END
	ELSE
		BEGIN
			INSERT dbo.BillDetail (idBill,idFood,quantity) 
			VALUES (@idBill, @idFood, @quantity)
		END 
END
GO 
--SELECT MAX(id) FROM dbo.Bill
--SELECT id FROM dbo.Food WHERE nameFood = N'Black Tea'

--Update Bill (bot mon an)
CREATE PROC USP_UpdateBillDetail
@idBill INT, @idFood INT , @quantity INT 
AS
BEGIN
	DECLARE @isExitBillDetail INT
	DECLARE @foodQuantity INT = 1;

	SELECT @isExitBillDetail = id, @foodQuantity = bd.quantity
	FROM dbo.BillDetail AS bd
	WHERE bd.idBill = @idBill AND bd.idFood = @idFood
	
	IF(@isExitBillDetail > 0)
		BEGIN
			DECLARE @newQuantity INT = @foodQuantity - @quantity
			IF(@newQuantity > 0)
				UPDATE dbo.BillDetail SET quantity = @foodQuantity - @quantity WHERE idFood = @idFood
			ELSE 
				DELETE dbo.BillDetail WHERE idBill = @idBill AND idFood = @idFood
		END
END
GO 
--Kiem tra IDfood co ton tai trong BillDetail
CREATE PROC USP_CheckIDFoodInFoodDetail
@idFood INT, @idBill INT 
AS
BEGIN
	SELECT COUNT(*) FROM dbo.BillDetail WHERE idFood = @idFood AND idBill = @idBill
END
GO 
--Sau khi thanh toan, update status
CREATE PROC USP_UpdateBillTotal
@idBill INT, @totalPrice INT
AS
BEGIN
	UPDATE dbo.Bill SET BillStatus = 1, totalPrice = @totalPrice WHERE id = @idBill
END
GO 
--
CREATE TRIGGER UTG_UpdateBillDetail
ON dbo.BillDetail FOR INSERT, UPDATE
AS 
BEGIN
	DECLARE @idBill INT
	SELECT @idBill = Inserted.idBill FROM Inserted

	DECLARE @idTable INT
	SELECT @idTable = idTable FROM dbo.Bill WHERE id = @idBill AND BillStatus = 0

	DECLARE @count INT
	SELECT @count = COUNT(*) FROM dbo.BillDetail WHERE idBill = @idBill

	IF (@count >0) 
		UPDATE dbo.TableFood SET tableStatus = N'Có người' WHERE id = @idTable
	ELSE
		UPDATE dbo.TableFood SET tableStatus = N'Trống' WHERE id = @idTable
END
GO 
CREATE TRIGGER UTG_UpdateBill
ON dbo.Bill FOR UPDATE
AS 
BEGIN
	DECLARE @idBill INT
	SELECT @idBill = Inserted.id FROM Inserted

	DECLARE @idTable INT
	SELECT @idTable = idTable FROM dbo.Bill WHERE id = @idBill

	DECLARE @count INT = 0
	SELECT @count = COUNT(*) FROM dbo.Bill WHERE idTable = @idTable AND BillStatus = 0

	IF(@count = 0)
		BEGIN
			UPDATE dbo.TableFood SET tableStatus = N'Trống' WHERE id = @idTable
			UPDATE dbo.Bill SET DateCheckOut = GETDATE() WHERE id = @idBill AND BillStatus = 1
		END 
END
GO 
-----------------------------------------
--CHuyen ban
CREATE PROC USP_SwitchTable
@idTable1 INT, @idTable2 int
AS
BEGIN 
	DECLARE @idFirstBill INT
	DECLARE @idSecorndBill INT

	DECLARE @isFirstTableEmpty INT = 1
	DECLARE @isSecordTableEmpty INT = 1

	SELECT @idSecorndBill = id FROM dbo.Bill WHERE idTable = @idTable2 AND BillStatus = 0
	SELECT @idFirstBill = id FROM dbo.Bill WHERE idTable = @idTable1 AND BillStatus = 0

	IF(@idFirstBill IS  NULL)
		BEGIN
			INSERT INTO dbo.Bill (DateCheckIn,DateCheckOut,idTable,BillStatus) VALUES (GETDATE(),NULL,@idTable1 ,0 )
			SELECT @idFirstBill = MAX(id) FROM dbo.Bill WHERE idTable = @idTable1 AND BillStatus = 0
		END
	SELECT @isFirstTableEmpty = COUNT(*) FROM dbo.BillDetail WHERE idBill = @idFirstBill

	IF(@idSecorndBill IS  NULL)
		BEGIN
			INSERT INTO dbo.Bill (DateCheckIn,DateCheckOut,idTable,BillStatus) VALUES (GETDATE(),NULL,@idTable2 ,0 )
			SELECT @idSecorndBill = MAX(id) FROM dbo.Bill WHERE idTable = @idTable2 AND BillStatus = 0
		END
	SELECT @isSecordTableEmpty = COUNT(*) FROM dbo.BillDetail WHERE idBill = @idSecorndBill
	--Luu cac gia tri cua billdetail vao bang
	SELECT id INTO idBillDetailTable FROM dbo.BillDetail WHERE idBill = @idSecorndBill
	--Dua tat ca bill detail cua bill thu 2 vao bill 1
	UPDATE dbo.BillDetail SET idBill = @idSecorndBill WHERE idBill = @idFirstBill
	--Dua tat ca bill detail cua bill thu 1 vao bill 2
	UPDATE dbo.BillDetail SET idBill = @idFirstBill WHERE id IN (SELECT * FROM dbo.idBillDetailTable)
	DROP TABLE dbo.idBillDetailTable

	IF (@isFirstTableEmpty = 0)
		UPDATE dbo.TableFood SET tableStatus = N'Trống' WHERE id = @idTable2
	IF (@isSecordTableEmpty = 0)
		UPDATE dbo.TableFood SET tableStatus = N'Trống' WHERE id = @idTable1
END 
GO 
--Show onl Table
CREATE PROC USP_TableThongKe
AS 
BEGIN
	SELECT b.id, b.DateCheckIn, b.DateCheckOut, f.price*bd.quantity AS totalPrice 
	FROM dbo.BillDetail AS bd, dbo.Bill AS b, dbo.Food AS f 
	WHERE bd.idBill=b.id AND bd.idFood =f.id AND b.BillStatus = 1
END
--Thong Ke
GO 
CREATE PROC USP_GetListBillByDate
@dateCheckIn DATE, @dateCheckOut DATE
AS
BEGIN	
	SELECT b.id, tf.nameTable, CAST(b.DateCheckIn AS DATE) AS DateCheckIn, CAST(b.DateCheckOut AS DATE) AS DateCheckOut,  b.totalPrice
	FROM dbo.Bill AS b, dbo.TableFood AS tf
	WHERE CAST(b.DateCheckIn AS DATE) >= @dateCheckIn AND  CAST(b.DateCheckOut AS DATE) <= @dateCheckOut
	AND b.BillStatus = 1
	AND tf.id = b.idTable
END
GO 
CREATE PROC USP_CountBill
@dateCheckIn DATE, @dateCheckOut DATE
AS
BEGIN	
	SELECT	COUNT( b.id)
	FROM dbo.Bill AS b, dbo.TableFood AS tf
	WHERE CAST(b.DateCheckIn AS DATE) >= @dateCheckIn AND  CAST(b.DateCheckOut AS DATE) <= @dateCheckOut
	AND b.BillStatus = 1
	AND tf.id = b.idTable
END
GO
-- xem lai
GO 
CREATE PROC USP_UpdateAccountInfo
@userName NVARCHAR(50), @password NVARCHAR(50), @newPassword NVARCHAR(50)
AS
BEGIN
	DECLARE @isRightPass INT = 0
	SELECT @isRightPass = COUNT(*) FROM dbo.Account WHERE username = @userName AND pass = @password
	IF (@isRightPass = 1)
	BEGIN
	    IF (@newPassword <> NULL OR @newPassword <> '')
			BEGIN
				UPDATE dbo.Account SET pass = @newPassword WHERE username = @userName
			END 
	END
END
GO
CREATE PROC USP_GetListFood
AS 
BEGIN
	SELECT f.id,f.nameFood,f.price,f.idCategory,fc.nameCategory FROM dbo.Food AS f,dbo.FoodCategory AS fc WHERE f.idCategory =fc.id
END
GO
CREATE PROC USP_GetListFoodCategory
AS 
BEGIN
	SELECT * FROM dbo.FoodCategory
END
GO
CREATE PROC USP_InsertFoodCategory
@nameCategory NVARCHAR(100)
AS 
BEGIN
	INSERT dbo.FoodCategory (nameCategory) VALUES (@nameCategory)
END
GO
--Xoa Food truoc roi moi xoa Category
CREATE PROC USP_DeleteFoodByIDCategory
@idCategory INT
AS
BEGIN
    DELETE dbo.Food WHERE idCategory = @idCategory
END
GO
CREATE PROC USP_DeleteCategory
@idCategory INT
AS
BEGIN
    DELETE dbo.FoodCategory WHERE id = @idCategory
END
GO
CREATE PROC USP_UpdateCategory
@idCategory INT, @newName NVARCHAR(100)
AS
BEGIN
    UPDATE dbo.FoodCategory SET nameCategory = @newName WHERE id = @idCategory
END
GO
CREATE PROC USP_CheckSameNameCategory
@NameCategory NVARCHAR(100)
AS
BEGIN
    SELECT COUNT(*) FROM dbo.FoodCategory WHERE nameCategory = @NameCategory
END
GO
CREATE PROC USP_CheckSameNameFood
@NameFood NVARCHAR(100)
AS
BEGIN
    SELECT COUNT(*) FROM dbo.Food WHERE nameFood = @NameFood
END
SELECT nameFood FROM dbo.Food WHERE id = 1
GO 
CREATE PROC USP_CheckSameNameTable
@NameTable NVARCHAR(100)
AS
BEGIN
    SELECT COUNT(*) FROM dbo.TableFood WHERE nameTable = @NameTable
END
GO
CREATE PROC USP_InsertFood
@nameFood NVARCHAR(100), @price INT, @idCategory INT
AS
BEGIN
    INSERT dbo.Food (nameFood,price,idCategory) VALUES (@nameFood, @price,@idCategory)
END
GO 
CREATE TRIGGER UTP_DeleteBillDetail
ON dbo.BillDetail FOR DELETE
AS 
BEGIN
    DECLARE @idBillDetail INT
	DECLARE @idBill INT
	SELECT @idBillDetail = id, @idBill = Deleted.idBill FROM Deleted

	DECLARE @idTable INT
	SELECT @idTable = idTable FROM dbo.Bill WHERE id= @idBill

	DECLARE @count INT = 0
	SELECT @count = COUNT(*) FROM dbo.BillDetail AS bd, dbo.Bill AS b WHERE b.id = bd.idBill AND b.id = @idBill AND b.BillStatus = 0
	IF (@count = 0)
		UPDATE dbo.TableFood SET tableStatus = N'Trống' WHERE id = @idTable
END
GO
CREATE PROC USP_DeleteBillDetailByIdFood
@idFood INT
AS
BEGIN
    DELETE dbo.BillDetail WHERE idFood = @idFood
END
GO 
CREATE PROC USP_DeleteFood
@idFood INT
AS
BEGIN
    DELETE dbo.Food WHERE id = @idFood
END
GO 
CREATE PROC USP_UpdateFood
@idFood INT, @newName NVARCHAR(100), @price INT, @idCategory INT 
AS
BEGIN
    UPDATE dbo.Food SET nameFood = @newName, price = @price, idCategory = @idCategory WHERE id = @idFood
END
GO
CREATE PROC USP_InsertTable
@nameTable NVARCHAR(100)
AS 
BEGIN
	INSERT dbo.TableFood (nameTable) VALUES (@nameTable)
END
GO 
CREATE PROC USP_DeleteTableFood
@idTable INT
AS
BEGIN
    DELETE dbo.TableFood WHERE id = @idTable
END
GO
CREATE PROC USP_UpdateTableFood
@idTable INT, @newName NVARCHAR(100)
AS
BEGIN
    UPDATE dbo.TableFood SET nameTable = @newName WHERE id = @idTable
END
GO
CREATE PROC USP_GetAccount
AS
BEGIN
	SELECT a.id, a.displayName, a.username,a.pass,t.typeName FROM dbo.Account AS a,dbo.TypeRight AS t WHERE a.TypeRight = t.id
END
GO
CREATE PROC USP_CheckSameUsernameAndId
@id INT, @username NVARCHAR(100)
AS
BEGIN
    SELECT COUNT(*) FROM dbo.Account WHERE username = @username AND id = @id
END
GO 
CREATE PROC USP_CheckSameUsername
@username NVARCHAR(100)
AS
BEGIN
    SELECT COUNT(*) FROM dbo.Account WHERE username = @username
END
GO
CREATE PROC USP_GetListTypeRight
AS 
BEGIN
	SELECT * FROM dbo.TypeRight
END
GO
CREATE PROC USP_InsertAccount
@displayName NVARCHAR(100), @username NVARCHAR(100), @pass NVARCHAR(100), @typeRight INT
AS 
BEGIN
	INSERT dbo.Account (displayName,username,pass,TypeRight) VALUES (@displayName,@username, @pass,@typeRight)
END
GO
CREATE PROC USP_UpdateAccount
@idAccount INT ,@displayName NVARCHAR(100), @username NVARCHAR(100), @typeRight INT
AS
BEGIN
    UPDATE dbo.Account SET displayName = @displayName, username =@username,TypeRight = @typeRight WHERE id = @idAccount
END
GO 
CREATE PROC USP_DeleteAccount
@idAccount INT
AS
BEGIN
    DELETE dbo.Account WHERE id = @idAccount
END
--Khong xoa tai khoan login
GO
CREATE PROC USP_ResetPasswordAccount
@idAccount INT
AS
BEGIN
    UPDATE dbo.Account SET pass = N'202cb962ac59075b964b07152d234b70' WHERE id = @idAccount
END
GO
CREATE FUNCTION [dbo].[fuConvertToUnsign1] ( @strInput NVARCHAR(4000) ) RETURNS NVARCHAR(4000) AS BEGIN IF @strInput IS NULL RETURN @strInput IF @strInput = '' RETURN @strInput DECLARE @RT NVARCHAR(4000) DECLARE @SIGN_CHARS NCHAR(136) DECLARE @UNSIGN_CHARS NCHAR (136) SET @SIGN_CHARS = N'ăâđêôơưàảãạáằẳẵặắầẩẫậấèẻẽẹéềểễệế ìỉĩịíòỏõọóồổỗộốờởỡợớùủũụúừửữựứỳỷỹỵý ĂÂĐÊÔƠƯÀẢÃẠÁẰẲẴẶẮẦẨẪẬẤÈẺẼẸÉỀỂỄỆẾÌỈĨỊÍ ÒỎÕỌÓỒỔỖỘỐỜỞỠỢỚÙỦŨỤÚỪỬỮỰỨỲỶỸỴÝ' +NCHAR(272)+ NCHAR(208) SET @UNSIGN_CHARS = N'aadeoouaaaaaaaaaaaaaaaeeeeeeeeee iiiiiooooooooooooooouuuuuuuuuuyyyyy AADEOOUAAAAAAAAAAAAAAAEEEEEEEEEEIIIII OOOOOOOOOOOOOOOUUUUUUUUUUYYYYYDD' DECLARE @COUNTER int DECLARE @COUNTER1 int SET @COUNTER = 1 WHILE (@COUNTER <=LEN(@strInput)) BEGIN SET @COUNTER1 = 1 WHILE (@COUNTER1 <=LEN(@SIGN_CHARS)+1) BEGIN IF UNICODE(SUBSTRING(@SIGN_CHARS, @COUNTER1,1)) = UNICODE(SUBSTRING(@strInput,@COUNTER ,1) ) BEGIN IF @COUNTER=1 SET @strInput = SUBSTRING(@UNSIGN_CHARS, @COUNTER1,1) + SUBSTRING(@strInput, @COUNTER+1,LEN(@strInput)-1) ELSE SET @strInput = SUBSTRING(@strInput, 1, @COUNTER-1) +SUBSTRING(@UNSIGN_CHARS, @COUNTER1,1) + SUBSTRING(@strInput, @COUNTER+1,LEN(@strInput)- @COUNTER) BREAK END SET @COUNTER1 = @COUNTER1 +1 END SET @COUNTER = @COUNTER +1 END SET @strInput = replace(@strInput,' ','-') RETURN @strInput END
GO
CREATE PROC USP_FindFood
@findfood NVARCHAR(100)
AS
BEGIN
	SELECT f.id,f.nameFood,f.price,f.idCategory,fc.nameCategory FROM dbo.Food AS f, dbo.FoodCategory AS fc WHERE dbo.fuConvertToUnsign1(nameFood) LIKE dbo.fuConvertToUnsign1(@findfood) AND f.idCategory =fc.id
END 
GO
CREATE PROC USP_ThongKeTuThangTheoNam
@year NVARCHAR(20)
AS
BEGIN	
	SELECT	MONTH(CAST(b.DateCheckOut AS DATE)) AS MonthOfYear,SUM(b.totalPrice) AS TotalPrice
	FROM dbo.Bill AS b
	WHERE YEAR(CAST(b.DateCheckOut AS DATE)) = @year AND YEAR(CAST(b.DateCheckIn AS DATE)) = @year
	AND b.BillStatus = 1
	GROUP BY MONTH(CAST(b.DateCheckOut AS DATE))
END
GO
CREATE PROC USP_GetListOrderTable
@idTableFood INT
AS
BEGIN
	SELECT id,nameCustomer,phone,CONVERT(VARCHAR,checkin,121) AS checkin,CONVERT(VARCHAR,timeOrder,121)AS timeOrder,idTable
    FROM dbo.OrderTable WHERE idTable = @idTableFood
END
GO
CREATE PROC USP_InsertOrderTable
@nameCustomer NVARCHAR(100), @phone NVARCHAR(100), @checkin VARCHAR(100), @idTable INT
AS
BEGIN
	INSERT dbo.OrderTable (nameCustomer,phone,checkin,timeOrder,idTable)
		VALUES (@nameCustomer,@phone,@checkin,CONVERT(VARCHAR,GETDATE(),126), @idTable)
	UPDATE dbo.TableFood SET tableStatus = N'Đã đặt' WHERE id = @idTable
END
GO
CREATE PROC USP_DeleteOrderTable
@idTable INT
AS
BEGIN
	DELETE dbo.OrderTable WHERE idTable = @idTable
	UPDATE dbo.TableFood SET tableStatus = N'Trống' WHERE id = @idTable
END
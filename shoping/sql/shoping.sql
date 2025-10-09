SHOW DATABASES;
use hrdtest;

DROP TABLE IF EXISTS Sale;
DROP TABLE IF EXISTS shopmember;


CREATE TABLE ShopMember (
	CustNo INT PRIMARY KEY AUTO_INCREMENT,
	CustName VARCHAR(30) NOT null,
	Phone VARCHAR(13) UNIQUE,
	Address VARCHAR(50),
	JoinDate DATE NOT NULL,
	Grade VARCHAR(1) NOT NULL CHECK(Grade IN('A','B','C','D')),
	City VARCHAR(2)
);

CREATE TABLE Sale (
	SaleNo INT PRIMARY KEY AUTO_INCREMENT,
	CustNo INT  NOT NULL,
	PCost INT,
	Amount INT,
	Price INT,
	PCode VARCHAR(3),
	CONSTRAINT fk_sale_member FOREIGN KEY (CustNo) REFERENCEs ShopMember(CustNo)
);

SELECT * FROM shopmember;
SELECT * FROM Sale;

-- 2.(1) 회원등록테이블
INSERT INTO ShopMember ( CustName, Phone, Address, JoinDate, Grade, City) values
	('홍길동', '010-1234-5678', '서울시 강남구', '2020-01-01', 'A', '01'),
	('이순신', '010-2222-3333', '부산시 해운대구', '2021-03-15', 'B', '02'),
	('강감찬', '010-7777-8888', '대구시 달서구', '2019-05-20', 'C', '03'),
	('을지문덕', '010-2222-4555', '광주시 중앙구', '2023-03-30', 'D', '04');
		
-- 2.(2)	A등급인 회원찾기
SELECT CustName, Phone, JoinDate FROM ShopMember
WHERE Grade = 'A';

-- 2.(2) 가입일자 2020이후인 회원
SELECT * FROM ShopMember
WHERE Year(JoinDate) > '2020' ;

-- 2.(3) sale 테이블
INSERT INTO Sale ( CustNo, PCost, Amount, Price, PCode )VALUES
(1, 1000, 10, 10000, 'P01'),
(2, 2000, 5,  10000, 'P02'),
(3, 1500, 7,  10500, 'P03'),
(2, 1500, 7,  10500, 'P03'),
(4, 1600, 6,  20500, 'P04');

SELECT * FROM Sale;

-- 2.(4) 판매조회
SELECT s.CustNo, m.CustName, SUM(s.Price) AS Totalprice
FROM Sale s
JOIN ShopMember m ON m.CustNo = s.CustNo
GROUP BY m.CustName
ORDER BY Totalprice DESC
LIMIT 2;

-- 2.(5) 데이터수정/삭제
UPDATE ShopMember
SET Grade = 'A'
WHERE CustName = '이순신';

-- 3번삭제
DELETE FROM ShopMember
where CustNo = 3;


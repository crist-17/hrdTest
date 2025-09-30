CREATE DATABASE hrdtest;

USE hrdtest;
SHOW DATABASES;
SELECT DATABASE(); 
SHOW VARIABLES LIKE 'port';
-- 자식→부모 순서로 제거
DROP TABLE IF EXISTS Rental;
DROP TABLE IF EXISTS Book;
DROP TABLE IF EXISTS Member;


-- 회원
CREATE TABLE Member (
  MemberID INT NOT NULL PRIMARY KEY,
  Name     VARCHAR(50)  NOT NULL,
  Phone    VARCHAR(20),
  Address  VARCHAR(100)
);

-- 도서
CREATE TABLE Book (
  BookID    INT NOT NULL PRIMARY KEY,
  Title     VARCHAR(100) NOT NULL,
  Author    VARCHAR(100),
  Publisher VARCHAR(100),
  Price     int NOT NULL,
  PubYear   YEAR NOT NULL,
  CONSTRAINT chk_book_price CHECK (Price >= 0)
);


-- 대출
CREATE TABLE Rental (
  RentalID   INT NOT NULL PRIMARY KEY,
  MemberID   INT NOT NULL,
  BookID     INT NOT NULL,
  RentDate   DATE NOT NULL,
  ReturnDate DATE NULL,
  CONSTRAINT fk_rental_member FOREIGN KEY (MemberID) REFERENCES Member(MemberID)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT fk_rental_book   FOREIGN KEY (BookID)   REFERENCES Book(BookID)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT chk_return_after_rent CHECK (ReturnDate IS NULL OR ReturnDate >= RentDate)
);

-- ===== 샘플 데이터 (각 6행) =====


-- Member: 이름/주소는 한국어
INSERT INTO Member (MemberID, Name, Phone, Address) VALUES
(1, '김민수', '010-1234-5678', '서울특별시 강남구'),
(2, '이서연', '010-2345-6789', '부산광역시 해운대구'),
(3, '박지훈', '010-3456-7890', '대구광역시 수성구'),
(4, '최유진', '010-4567-8901', '광주광역시 북구'),
(5, '정우영', '010-5678-9012', '대전광역시 서구'),
(6, '한지민', '010-6789-0123', '인천광역시 연수구');


-- Book: 도서 필드는 간략히, PubYear는 YYYY
INSERT INTO Book (BookID, Title, Author, Publisher, Price, PubYear) VALUES
(101, '자바의 정석',       '남궁성',   '도우출판',     32000, 2022),
(201, '생활코딩 웹',       '이고잉',   '위키북스',     25000, 2014),
(301, '데이터베이스 입문', '김철수',   '한빛미디어',   28000, 2020),
(401, '파이썬 한입',       '박민지',   '길벗',         22000, 2019),
(501, '클린 코드',         '김현수',   '인사이트',     30000, 2021),
(601, '알고리즘 문제해결', '이은지',   '한빛미디어',   27000, 2013);


-- Rental: 대출/반납일자는 2020년 전후, 반납 NULL 허용
-- (PubYear는 모두 대출일보다 이전으로 맞춤)
INSERT INTO Rental (RentalID, MemberID, BookID, RentDate, ReturnDate) VALUES
(1, 1, 101, '2023-12-20', '2024-01-05'),
(2, 4, 301, '2024-02-10', '2024-02-20'),
(3, 3, 201, '2023-11-25', NULL),
(4, 2, 501, '2023-03-15', '2023-04-01'),
(5, 5, 401, '2023-07-02', '2023-07-15'),
(6, 6, 601, '2023-12-28', '2024-01-10');

SELECT * FROM Member;
SELECT * FROM Book;
SELECT * FROM Rental;

-- (1)
SELECT * FROM book where PubYear >= 2020;

-- (2)
SELECT b.bookid, b.title, r.rentalid, r.rentdate, returndate FROM rental r
JOIN MEMBER m ON r.memberid = m.memberid
JOIN book b ON r.bookid = b.bookid
WHERE m.name = '박지훈'
ORDER BY r.rentdate DESC;

-- (3)
SELECT b.bookid, b.title, r.rentalid, r.rentdate, m.name FROM rental r
JOIN MEMBER m ON r.memberid = m.memberid
JOIN book b ON r.bookid = b.bookid
WHERE r.returndate is NULL;

-- (4)
SELECT b.bookid, b.title,COUNT(*) AS rencount
 FROM rental r
 JOIN book b ON r.bookid = b.bookid
GROUP BY b.bookid, b.Title;

-- (5)
SELECT * FROM book b
ORDER BY price DESC
LIMIT 1;





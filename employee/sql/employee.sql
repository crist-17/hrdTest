CREATE DATABASE employee;
USE employee;

-- 1) 테이블 생성 (제약조건 포함)
CREATE TABLE Employee (
  EmpNo     INT AUTO_INCREMENT PRIMARY KEY,          -- PK, 자동번호
  EmpName   VARCHAR(30) NOT NULL,                    -- 사원명
  Dept      VARCHAR(20) NOT NULL,                    -- 부서명
  HireDate  DATE NOT NULL,                           -- 입사일
  Salary    INT NOT NULL,                            -- 급여
  CONSTRAINT uq_emp_empname UNIQUE (EmpName),        -- (조건) 사원명 중복 불가
  CONSTRAINT chk_emp_salary CHECK (Salary >= 2000000) -- (조건) 급여 200만 이상
);

-- 2-1) 사원 등록 (INSERT)
INSERT INTO Employee (EmpName, Dept, HireDate, Salary) VALUES
('홍길동', '영업부', '2020-03-01', 2500000),
('이순신', '인사부', '2019-07-15', 3200000),
('강감찬', '개발부', '2021-01-10', 2800000);

-- 2-2) 조회
-- (a) 부서가 '개발부'인 사원의 사번, 이름, 급여
SELECT EmpNo, EmpName, Salary
FROM Employee
WHERE Dept = '개발부';

-- (b) 급여가 3,000,000원 이상인 사원의 이름, 부서
SELECT EmpName, Dept
FROM Employee
WHERE Salary >= 3000000;

-- 2-3) 수정: 이순신의 급여를 3,500,000원으로
UPDATE Employee
SET Salary = 3500000
WHERE EmpName = '이순신';

-- 2-4) 삭제: 사번이 1번인 사원 삭제
DELETE FROM Employee
WHERE EmpNo = 1;

SELECT * FROM employee;

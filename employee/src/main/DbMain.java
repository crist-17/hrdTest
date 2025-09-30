package main;

import crud.CRUDEmployee;

public class DbMain {
    public static void main(String[] args) {
        CRUDEmployee crud = new CRUDEmployee();

        crud.createTable(); // CREATE

        // INSERT (중복이면 무시; 여러 번 실행해도 에러 없음)
        crud.insert("홍길동", "영업부", "2020-03-01", 2_500_000);
        crud.insert("이순신", "인사부", "2019-07-15", 3_200_000);
        crud.insert("강감찬", "개발부", "2021-01-10", 2_800_000);

        // SELECT 들
        crud.selectByDept("개발부");        // 개발부 사번/이름/급여
        crud.selectByMinSalary(3_000_000);  // 급여 300만 이상 이름/부서

        // UPDATE / DELETE
        crud.updateSalaryByName("이순신", 3_500_000);
        crud.deleteByEmpNo(1);
    }
}

package main;

import crud.CRUDclass;

public class DbMain {
    public static void main(String[] args) {

        CRUDclass c = new CRUDclass();

        c.createTable();   // DROP > CREATE
        c.insertMem();     // 2.(1) 회원 4명 넣기
        c.queryAgrade();    // 2.(2)-1 A등급
        c.queryJoin2020();   // 2.(2)-2 2020년 이후
        c.insertSale();      // 2.(3) 판매 5건 넣기
        c.queryTop();        // 2.(4) 총액 상위 2명
        c.updateleeA();      // 2.(5)-1 이순신 → A
        c.deleteCust3();      // 2.(5)-2 CustNo=3 삭제

    }
}

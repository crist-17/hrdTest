package main;

import crud.CRUDclass;
import ko.pci.conn.DBConnection;

public class DbMain {
    public static void main(String[] args) {
        CRUDclass c = new CRUDclass();
        c.q1();
        c.q2(args.length > 0 ? args[0] : "박지훈");
        c.q3();
        c.q4();
        c.q5();
    }
}

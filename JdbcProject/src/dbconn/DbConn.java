package dbconn;

import emp.Emp;

import java.sql.*;


public class DbConn {
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;

    public void selectAll() throws SQLException {
        Emp emp = new Emp();
        //Class.forName("oracle.jdbc.driver.OracleDriver");
        conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","SCOTT","1234");
        stmt = conn.createStatement();
        String query = "SELECT * FROM emp";

        rs = stmt.executeQuery(query);
        while(rs.next()){
            emp.setEmpno(rs.getInt("empno"));
            emp.setEname(rs.getString("ename"));
            emp.setJob(rs.getString("job"));
            emp.setMgr(rs.getInt("mgr"));
            emp.setHiredate(rs.getDate("hiredate"));
            emp.setSal(rs.getInt("sal"));
            emp.setComm(rs.getInt("comm"));
            emp.setDeptno(rs.getInt("deptno"));

            System.out.println("[empno : " +emp.getEmpno()
                    +"] [ename : " + emp.getEname()
                    +"] [job : " + emp.getJob()
                    +"] [mgr : " + emp.getMgr()
                    +"] [hiredate : " + emp.getHiredate()
                    +"] [sal : " + emp.getSal()
                    +"] [comm : " + emp.getComm()
                    +"] [deptno : " + emp.getDeptno() + "]");
        }
        rs.close();
        stmt.close();
        conn.close();
    }
}

package SQL;

import javax.swing.*;
import java.sql.*;


public class SQL {
    Connection connection;
    public SQL(){

        String driverName="com.microsoft.sqlserver.jdbc.SQLServerDriver";
        String dbURL="jdbc:sqlserver://localhost:1433;DatabaseName=Mail";
        String userName="sa";//不要写错
        String userPwd="154013";//一定不要写错
        try
        {
            Class.forName(driverName);
            //System.out.println("加载驱动成功！");
        }catch(Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"SQL加载驱动失败");
        }
        try{
            connection = DriverManager.getConnection(dbURL,userName,userPwd);
            Statement st= connection.createStatement();
            //System.out.println("连接数据库成功！");
            //createStatement()

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.print(e.getErrorCode());
            System.out.println("数据库连接错误");
            System.exit(0);
        }
    }
    public Connection getConnection() {
        return connection;
    }
    String insertMailer=new String("insert into Mailer values(?,?)");
    String deleteMailer=new String("delete from Mailer where Act=? ");
    public void insertMailer(String act,String psw) throws SQLException {
        PreparedStatement preparedStatement=connection.prepareStatement(insertMailer);
        preparedStatement.setString(1,act);
        preparedStatement.setString(2,psw);
        preparedStatement.executeUpdate();
    }
    public void deleteMailer(String act) throws SQLException {
        PreparedStatement preparedStatement=connection.prepareStatement(deleteMailer);
        preparedStatement.setString(1,act);
        preparedStatement.executeUpdate();
    }
    String selectMailer=new String("select * from Mailer where act=?");
    public ResultSet selectMailer(String act) throws SQLException {
        PreparedStatement preparedStatement=connection.prepareStatement(selectMailer);
        preparedStatement.setString(1,act);
        return preparedStatement.executeQuery();
    }

}

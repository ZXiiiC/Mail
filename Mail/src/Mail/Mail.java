package Mail;

import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class Mail {
    String senderAct;
    String senderPsw;
    String targetAdr;

    static String Send_Server_Host="smtp.163.com";
    static String Send_Protocal="stmp";
    static String Acpt_Server_Host="pop3.163.com";
    static String Acpt_Protocal="pop3";


    Session session;
    Transport transport;
    Store store;

    public static void updateSend_Server_Host(String newSSH){
        Send_Server_Host=newSSH;
    }
    public static void updateSend_Protocal(String newSP){
        Send_Protocal=newSP;
    }
    public static void updateAcpt_Server_Host(String newASH){
        Acpt_Server_Host=newASH;
    }
    public static void updateAcpt_Protocal(String newAP){
        Acpt_Protocal=newAP;
    }


    public Session getSession() {
        return session;
    }
    public Transport getTransport(){
        return transport;
    }
    public Store getStore(){
        return store;
    }
    public Mail(String act, String psw) throws Exception{
        senderAct=act;
        senderPsw=psw;
        //邮箱服务器配置
        Properties props=new Properties();
        //接收配置
        props.setProperty("mail.store.protocal",Acpt_Protocal);
        props.setProperty("mail."+Acpt_Protocal+".host",Acpt_Server_Host);
        props.setProperty("mail."+Acpt_Protocal+".starttles.enable","true");
        //发送配置
        props.setProperty("mail.transport.protocal",Send_Protocal);
        props.setProperty("mail."+Send_Protocal+".host",Send_Server_Host);
        props.setProperty("mail."+Send_Protocal+".auth","true");
        //1.创建session
        session=Session.getInstance(props);
        session.setDebug(true);
        //2.得到transport对象

//        try {
            transport =session.getTransport();
            store= session.getStore(Acpt_Protocal);
/*        }catch (Exception e_ts){
            e_ts.printStackTrace();
            JOptionPane.showMessageDialog(null,"连接服务器错误");
            return ;
        }
*/
        //3.使用用户名和密码连接服务器（用户名只需写@前面的,密码指服务器)
//        try {
            transport.connect(Send_Server_Host,act,psw);
            store.connect(Acpt_Server_Host,act,psw);
/*        }catch (Exception e_cn){
            e_cn.printStackTrace();
            JOptionPane.showMessageDialog(null,"登陆失败");
            return ;
        }
*/
    }
    public MimeMessage getMimeMessage()throws Exception{
        MimeMessage msg=new MimeMessage(session);
        msg.setFrom(new InternetAddress(senderAct+"@163.com"));
        return msg;
    }
}

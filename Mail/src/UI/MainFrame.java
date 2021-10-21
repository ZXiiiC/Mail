package UI;

import CONFIG.CONFIG;
import Mail.Mail;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainFrame extends JFrame {
    JLayeredPane jLayeredPane=new JLayeredPane();
    LoginPanel loginPanel=new LoginPanel();
    MainPanel   mainPanel=new MainPanel();
    FilePanel   filePanel=new FilePanel();
    LinkPanel  linkPanel=new LinkPanel();
    Mail mail;
    JPanel bgJpn=new JPanel();
    JLabel bgJlb;
    JLabel    mailJlb;//邮件图标
    Dimension    mailSize=new Dimension(CONFIG.rate*100,CONFIG.rate*100);
    boolean mainPaneldebug=false;
    boolean filePaneldebug=false;
    public MainFrame(){
        this.setSize(CONFIG.frameDim);
        jLayeredPane.add(loginPanel,JLayeredPane.MODAL_LAYER);
        jLayeredPane.add(mainPanel,JLayeredPane.MODAL_LAYER);
        jLayeredPane.add(filePanel,JLayeredPane.MODAL_LAYER);
        jLayeredPane.add(linkPanel,JLayeredPane.MODAL_LAYER);
        //背景图片设置
        Image bgImg= getToolkit().getImage("img/bg.png");
        bgImg=bgImg.getScaledInstance(CONFIG.panelDim.width,CONFIG.panelDim.height,Image.SCALE_DEFAULT);
        bgJlb=new JLabel(new ImageIcon(bgImg));
        bgJlb.setSize(CONFIG.panelDim);
        bgJpn.setSize(CONFIG.panelDim);
        bgJpn.add(bgJlb);
        //邮件图标初始化
        Image mailImg=getToolkit().getImage("img/mail.png");
        mailImg=mailImg.getScaledInstance(mailSize.width,mailSize.height,Image.SCALE_DEFAULT);
        mailJlb=new JLabel(new ImageIcon(mailImg));
        mailJlb.setSize(mailSize);
        mailJlb.setLocation(270*CONFIG.rate,30*CONFIG.rate);
        this.add(mailJlb);


        jLayeredPane.add(bgJpn,JLayeredPane.DEFAULT_LAYER);
        jLayeredPane.setBackground(Color.white);
        this.add(jLayeredPane);
        this.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        if(mainPaneldebug){
            loginPanel.setVisible(false);
            mainPanel.setVisible(true);
            repaint();
        }
        if(filePaneldebug){
            filePanel.setVisible(true);
            mainPanel.setVisible(false);
            loginPanel.setVisible(false);
        }

    }
    class LoginPanel extends JPanel {


        JTextField     actJtf=new JTextField();    //账号文本框
        JPasswordField pswJtf=new JPasswordField();//密码文本框
        JLabel         actJlb=new JLabel("账号",JLabel.CENTER);//账号标签
        JButton        pswJbt=new JButton("记忆");
        JLabel         pswJlb=new JLabel("密码",JLabel.CENTER);//密码标签
        JLabel         sfxJlb=new JLabel("@163.com",JLabel.CENTER);//邮箱后缀标签

        JButton      loginJbt;//登录按钮
        JButton      lkSetJbt;//连接配置按钮

        //Location配置
        final  int    jlbLocX=CONFIG.rate*140;
        final  int    jtfLocX=CONFIG.rate*200;
        final  int    actLocY=CONFIG.rate*160;
        final  int    pswLocY=CONFIG.rate*220;
        final  int    jbtLocX=CONFIG.rate*140;
        final  int    jbtLocY=CONFIG.rate*280;

        //Size配置
        final  int  defaultH =CONFIG.rate*40;//常用默认高度
        Dimension     jtfSize=new Dimension(CONFIG.rate*300, defaultH);
        Dimension     jlbSize=new Dimension(CONFIG.rate*60, defaultH);

        Dimension     jbtSize=new Dimension(CONFIG.rate*360,CONFIG.rate*40);
        Dimension     sfxSize=new Dimension(CONFIG.rate*80, defaultH);
        public LoginPanel(){
            this.setOpaque(false);
            this.setVisible(true);


            pswJbt.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String act=actJtf.getText();
                    if(act.equals("")||act.length()<1){
                        JOptionPane.showMessageDialog(null,"请先输入账号");
                        return;
                    }
                    try {
                        ResultSet resultSet=CONFIG.sql.selectMailer(act);
                        if(resultSet.next()==false){
                            JOptionPane.showMessageDialog(null,"当前账号尚未登陆过");
                            return ;
                        }
                        else{
                            String psw=resultSet.getString(2).trim();
                            pswJtf.setText(psw);
                        }
                    }catch (Exception e_select){
                        JOptionPane.showMessageDialog(null,"查找密码失败");
                        e_select.printStackTrace();
                        return;
                    }

                }
            });
        /*Image lgImg=getToolkit().getImage("img/lgbt.png");
        lgImg=lgImg.getScaledInstance(jbtSize.width,jbtSize.height,Image.SCALE_DEFAULT);
        loginJbt=new JButton(new ImageIcon(lgImg));
        */
            loginJbt=new JButton("登    录");
            loginJbt.setBackground(Color.WHITE);
            loginJbt.setFont(new Font("微软雅黑",Font.BOLD,18));
            loginJbt.setSize(jbtSize);
            loginJbt.setLocation(jbtLocX,jbtLocY);
            loginJbt.setBorder(BorderFactory.createLineBorder(Color.CYAN));
            this.add(loginJbt);

            lkSetJbt=new JButton("连接配置");
            lkSetJbt.setBackground(Color.WHITE);
            lkSetJbt.setFont(new Font("微软雅黑",Font.BOLD,18));
            lkSetJbt.setSize(jbtSize);
            lkSetJbt.setLocation(jbtLocX,jbtLocY+CONFIG.rate*60);
            lkSetJbt.setBorder(BorderFactory.createLineBorder(Color.CYAN));
            this.add(lkSetJbt);

            this.setSize(CONFIG.panelDim);
            this.setVisible(true);
            this.setLayout(null);
            actJtf.setSize(jtfSize.width-sfxSize.width+2*CONFIG.rate,jtfSize.height);
            pswJtf.setSize(actJtf.getSize());
            actJlb.setSize(jlbSize);
            pswJlb.setSize(jlbSize);
            sfxJlb.setSize(sfxSize);
            pswJbt.setSize(sfxSize);
            //字体设置
            actJlb.setFont(new Font("微软雅黑",Font.BOLD,16));
            pswJlb.setFont(new Font("微软雅黑",Font.BOLD,16));
            sfxJlb.setFont(new Font("微软雅黑",Font.BOLD,13));
            pswJbt.setFont(new Font("微软雅黑",Font.BOLD,13));
            actJtf.setFont(new Font("微软雅黑",Font.BOLD,13));
            pswJtf.setFont(new Font("微软雅黑",Font.BOLD,13));
            //边框设置
            actJlb.setBorder(BorderFactory.createLineBorder(Color.blue));
            pswJlb.setBorder(BorderFactory.createLineBorder(Color.blue));
            sfxJlb.setBorder(BorderFactory.createLineBorder(Color.blue));
            pswJbt.setBorder(BorderFactory.createLineBorder(Color.blue));
            //背景颜色设置
            actJlb.setBackground(Color.WHITE);
            pswJlb.setBackground(Color.WHITE);
            sfxJlb.setBackground(Color.WHITE);sfxJlb.setOpaque(true);
            pswJbt.setBackground(Color.WHITE);pswJbt.setOpaque(true);
            //位置设置
            actJlb.setLocation(jlbLocX,actLocY);
            actJtf.setLocation(jtfLocX,actLocY);
            sfxJlb.setLocation(jtfLocX+jtfSize.width-sfxSize.width,actLocY);
            pswJbt.setLocation(sfxJlb.getX(),pswLocY);
            pswJlb.setLocation(jlbLocX,pswLocY);
            pswJtf.setLocation(jtfLocX,pswLocY);



            this.add(actJtf);
            this.add(pswJtf);
            this.add(actJlb);
            this.add(pswJlb);
            this.add(sfxJlb);
            this.add(pswJbt);
            //事件响应
            loginJbt.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String act=actJtf.getText();
                    String psw=new String(pswJtf.getPassword());
                    if(act.equals("")){
                        JOptionPane.showMessageDialog(null,"账号不能为空");
                        return ;
                    }
                    if(psw.equals("")){
                        JOptionPane.showMessageDialog(null,"密码不能为空");
                        return;
                    }
                    try {
                        mail=new Mail(act,psw);
                        callHide();
                    }catch (Exception e_lg){
                        JOptionPane.showMessageDialog(null,"登录失败");
                    }
                    try {
                        CONFIG.sql.deleteMailer(act);
                        CONFIG.sql.insertMailer(act,psw);
                    }catch (SQLException sqlException){
                        sqlException.printStackTrace();
                        JOptionPane.showMessageDialog(null,"账号密码记录失败");
                    }

                }
            });
            lkSetJbt.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    linkPanel.setVisible(true);
                    loginPanel.setVisible(false);
                }
            });

        }
        void callHide(){
            this.setVisible(false);
            mainPanel.callApear();
        }

    }
    class MainPanel extends JPanel {

        JButton writeMailJbt=new JButton("编辑邮件");
        JButton myMailBoxJbt=new JButton("我的信箱");
        JButton fileSetJbt =new JButton("文件配置");

        final  int defaultLocX=180*CONFIG.rate;
        Dimension jbtDim=new Dimension(280*CONFIG.rate, CONFIG.rate*50);

        public MainPanel(){
            this.setSize(CONFIG.panelDim);
            this.setLayout(null);

            writeMailJbt.setSize(jbtDim);
            myMailBoxJbt.setSize(jbtDim);
            fileSetJbt.setSize(jbtDim);

            writeMailJbt.setLocation(defaultLocX,160*CONFIG.rate);
            myMailBoxJbt.setLocation(defaultLocX,240*CONFIG.rate);
            fileSetJbt.setLocation(defaultLocX,320*CONFIG.rate);
            writeMailJbt.setFont(new Font("微软雅黑",Font.BOLD,18));
            myMailBoxJbt.setFont(new Font("微软雅黑",Font.BOLD,18));
            fileSetJbt.setFont(new Font("微软雅黑",Font.BOLD,18));
            writeMailJbt.setBorder(BorderFactory.createLineBorder(Color.blue));
            myMailBoxJbt.setBorder(BorderFactory.createLineBorder(Color.blue));
            fileSetJbt.setBorder(BorderFactory.createLineBorder(Color.blue));
            writeMailJbt.setBackground(Color.WHITE);
            myMailBoxJbt.setBackground(Color.WHITE);
            fileSetJbt.setBackground(Color.WHITE);
        /*
        writeMailJbt.setOpaque(false);
        myMailBoxJbt.setOpaque(false);
        linkSetJbt.setOpaque(false);
        */
            this.add(writeMailJbt);
            this.add(myMailBoxJbt);
            this.add(fileSetJbt);

            writeMailJbt.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    sendMsgFrame smf=new sendMsgFrame(mail);
                }
            });
            myMailBoxJbt.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //MailBoxFrame mailBoxFrame=new MailBoxFrame(mail.getSession());
                    MailBoxFrame mailBoxFrame=new MailBoxFrame(mail.getSession(),mail.getStore());
                }
            });
            fileSetJbt.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    mainPanel.setVisible(false);
                    filePanel.setVisible(true);
                }
            });
            this.setOpaque(false);
            this.setVisible(false);

        }
        public void callApear(){
            this.setVisible(true);
        }

    }
    class LinkPanel extends JPanel{
        //640x400
        JLabel SendProtocalJlabel=new JLabel("发送协议",JLabel.CENTER);
        JLabel AcptProtovalJlabel=new JLabel("接收协议",JLabel.CENTER);
        JLabel SendServerHostJlabel=new JLabel("发送服务器",JLabel.CENTER);
        JLabel AcptServerHosetJlabel=new JLabel("接收服务器",JLabel.CENTER);
        JComboBox<String> spCB=new JComboBox();
        JComboBox<String> apCB=new JComboBox();
        JComboBox<String> sshCB=new JComboBox();
        JComboBox<String> ashCB=new JComboBox();
        JButton   ret=new JButton("返回");
        JButton   set=new JButton("设置");
        Dimension cbSize=new Dimension(CONFIG.rate*130,CONFIG.rate*40);
        void jlbInit(JLabel jLabel){
            jLabel.setSize(CONFIG.rate*100,40);
            jLabel.setBackground(Color.WHITE);
            jLabel.setBorder(BorderFactory.createLineBorder(Color.BLUE));
            jLabel.setFont(new Font("微软雅黑",Font.BOLD,18));
            this.add(jLabel);
        }
        public LinkPanel(){
            this.setSize(CONFIG.panelDim);
            this.setLayout(null);
            this.setVisible(false);
            jlbInit(SendProtocalJlabel);
            jlbInit(AcptProtovalJlabel);
            jlbInit(SendServerHostJlabel);
            jlbInit(AcptServerHosetJlabel);
            //640x400 100x40
            SendProtocalJlabel.setLocation(CONFIG.rate*60,CONFIG.rate*160);
            AcptProtovalJlabel.setLocation(CONFIG.rate*60,CONFIG.rate*220);
            SendServerHostJlabel.setLocation(350*CONFIG.rate,160*CONFIG.rate);
            AcptServerHosetJlabel.setLocation(350*CONFIG.rate,CONFIG.rate*220);
            spCB.setLocation(CONFIG.rate*160,SendProtocalJlabel.getY());
            apCB.setLocation(CONFIG.rate*160,AcptProtovalJlabel.getY());
            sshCB.setLocation(CONFIG.rate*450,SendServerHostJlabel.getY());
            ashCB.setLocation(CONFIG.rate*450,AcptServerHosetJlabel.getY());
            spCB.setSize(cbSize);
            apCB.setSize(cbSize);
            sshCB.setSize(cbSize);
            ashCB.setSize(cbSize);

            spCB.addItem("stmp");
            spCB.addItem("imap");
            apCB.addItem("pop3");
            apCB.addItem("imap");

            sshCB.addItem("smtp.163.com");
            sshCB.addItem("smtp.qq.com");
            ashCB.addItem("pop3.163.com");
            ashCB.addItem("pop.qq.com");
            this.add(spCB);
            this.add(apCB);
            this.add(sshCB);
            this.add(ashCB);
            ret.setLocation(0,0);
            ret.setSize(CONFIG.rate*60,CONFIG.rate*35);
            ret.setFont(new Font("微软雅黑",Font.BOLD,15));
            ret.setBorder(BorderFactory.createLineBorder(Color.white));
            ret.setBackground(Color.WHITE);
            ret.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    linkPanel.setVisible(false);
                    loginPanel.setVisible(true);
                }
            });

            set.setSize(CONFIG.rate*320,CONFIG.rate*60);
            set.setLocation(CONFIG.rate*160,CONFIG.rate*300);
            set.setFont(new Font("微软雅黑",Font.BOLD,20));
            set.setBackground(Color.WHITE);
            set.setBorder(BorderFactory.createLineBorder(Color.BLUE));
            this.add(set);
            this.add(ret);
            set.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    mail.updateSend_Protocal((String) spCB.getSelectedItem());
                    mail.updateAcpt_Protocal((String) apCB.getSelectedItem());
                    mail.updateSend_Server_Host((String) sshCB.getSelectedItem());
                    mail.updateAcpt_Server_Host((String) ashCB.getSelectedItem());
                    JOptionPane.showMessageDialog(null,"更新配置成功");

                }
            });
        }
    }
    class FilePanel extends JPanel{
        final  int defaultLocX=180*CONFIG.rate;
        Dimension jbtDim=new Dimension(250*CONFIG.rate, CONFIG.rate*50);
        JButton srchJbt  =new JButton("选择");
        JButton retJbt   =new JButton("返回");
        JLabel AdrJlb =new JLabel();
        String path;
        JFileChooser jfileChooser=new JFileChooser();

        FilePanel(){
            this.setSize((int) (CONFIG.rate*640),(int) (CONFIG.rate*400));
            this.setLayout(null);
            this.setOpaque(false);
            retJbt.setSize(CONFIG.rate*60,CONFIG.rate*30);
            retJbt.setBackground(Color.white);

            jfileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            srchJbt.setLocation((int) (CONFIG.rate*430),(int) (CONFIG.rate*200));
            srchJbt.setSize((int) (CONFIG.rate*80),(int) (CONFIG.rate*50));
            AdrJlb.setLocation((int) (CONFIG.rate*160),(int) (CONFIG.rate*200));
            AdrJlb.setSize((int) (CONFIG.rate*272),(int) (CONFIG.rate*51));
            AdrJlb.setFont(new Font("宋体",Font.BOLD,(int) (CONFIG.rate*10)));
            AdrJlb.setFont(new Font("微软雅黑",Font.BOLD,20));
            AdrJlb.setBackground(Color.WHITE);
            AdrJlb.setOpaque(true);
            //AdrJlb.setBorder(BorderFactory.createLineBorder(Color.blue));
            srchJbt.setBackground(Color.WHITE);
            srchJbt.setBorder(BorderFactory.createLineBorder(Color.BLUE));

            File pathfile=new File(CONFIG.pathFile);
            try {
                BufferedReader br=new BufferedReader(new FileReader(pathfile));
                path=br.readLine();
                AdrJlb.setText(path);
            }catch (Exception e_pf){
                e_pf.printStackTrace();
                JOptionPane.showMessageDialog(null,"读取路径文件失败");
            }
            srchJbt.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    jfileChooser.showDialog(new JLabel(),"存储路径选择");
                    File file=jfileChooser.getSelectedFile();
                    if(file==null) return;
                    String newpath=file.getPath()+'\\';
                    AdrJlb.setText(newpath);
                    try{
                        PrintStream ps=new PrintStream(new FileOutputStream(pathfile));
                        ps.println(newpath);
                    }catch (Exception e_update){
                        JOptionPane.showMessageDialog(null,"写入文件失败");
                        e_update.printStackTrace();
                        return;
                    }
                    repaint();
                }
            });
            retJbt.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    filePanel.setVisible(false);
                    mainPanel.setVisible(true);
                }
            });
            this.setVisible(false);
            this.add(srchJbt);
            this.add(AdrJlb);
            this.add(retJbt);
        }
    }

}

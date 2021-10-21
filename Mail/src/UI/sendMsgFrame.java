package UI;

import CONFIG.CONFIG;
import Mail.Mail;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
public class sendMsgFrame extends JFrame {

    Mail mail;

    JLabel tarJlb=new JLabel("收件地址",JLabel.CENTER);
    JLabel thmJlb=new JLabel("主      题",JLabel.CENTER);
    JLabel namJlb=new JLabel("收 件 人",JLabel.CENTER);
    JTextField tarJtf=new JTextField();
    JTextField thmJtf=new JTextField();
    JTextField namJtf=new JTextField();
    JTextArea  wrtJta=new JTextArea("请输入邮件内容");
    JTextArea  filJta=new JTextArea("附件：");
    JPanel jPanel=new JPanel();
    JPanel jtaPanel=new JPanel();

    JButton sendJbt=new JButton("发    送");
    JButton addfJbt=new JButton("添加附件");
    JFileChooser jfileChooser =new JFileChooser();
    ArrayList<File> fileArrayList=new ArrayList<>();

    int defaultH=CONFIG.rate*50;
    Dimension jlbDim=new Dimension(CONFIG.rate*80,defaultH);
    Dimension jtfDim=new Dimension(CONFIG.rate*320,defaultH);
    Dimension jtADim=new Dimension(CONFIG.rate*400,CONFIG.rate*475);
    Dimension wrtDim=new Dimension(CONFIG.rate*400,CONFIG.rate*350);
    Dimension filDim=new Dimension(CONFIG.rate*400,CONFIG.rate*100);
    Dimension jbtDim=new Dimension(CONFIG.rate*175,defaultH);


    //500x800
    public sendMsgFrame(Mail m){
        mail=m;
        jPanel.setSize(CONFIG.msgPanelDim);

        jPanel.setLayout(null);
        jtaPanel.setLayout(null);

        tarJlb.setSize(jlbDim);
        thmJlb.setSize(jlbDim);
        namJlb.setSize((jlbDim));
        tarJtf.setSize(jtfDim);
        thmJtf.setSize(jtfDim);
        namJtf.setSize(jtfDim);
        sendJbt.setSize(jbtDim);
        addfJbt.setSize(jbtDim);


        namJlb.setLocation(CONFIG.rate*50,CONFIG.rate*25);
        tarJlb.setLocation(CONFIG.rate*50,CONFIG.rate*100);
        thmJlb.setLocation(CONFIG.rate*50,CONFIG.rate*175);
        namJtf.setLocation(CONFIG.rate*130,CONFIG.rate*25);
        tarJtf.setLocation(CONFIG.rate*130,CONFIG.rate*100);
        thmJtf.setLocation(CONFIG.rate*130,CONFIG.rate*175);
        sendJbt.setLocation(CONFIG.rate*50,CONFIG.rate*745);
        addfJbt.setLocation(CONFIG.rate*275,CONFIG.rate*745);
        //wrtJta.setLocation(0,0);


        tarJlb.setFont(new Font("微软雅黑",Font.BOLD,17));
        thmJlb.setFont(new Font("微软雅黑",Font.BOLD,17));
        namJlb.setFont(new Font("微软雅黑",Font.BOLD,17));
        tarJtf.setFont(new Font("微软雅黑",Font.PLAIN,18));
        thmJtf.setFont(new Font("微软雅黑",Font.PLAIN,18));
        namJtf.setFont(new Font("微软雅黑",Font.PLAIN,18));
        wrtJta.setFont(new Font("宋体",Font.PLAIN,18));
        filJta.setFont(new Font("宋体",Font.PLAIN,18));
        addfJbt.setFont(new Font("微软雅黑",Font.BOLD,20));
        sendJbt.setFont(new Font("微软雅黑",Font.BOLD,20));
        jPanel.add(tarJlb);
        jPanel.add(thmJlb);
        jPanel.add(tarJtf);
        jPanel.add(thmJtf);
        jPanel.add(addfJbt);
        jPanel.add(sendJbt);
        jPanel.add(namJlb);
        jPanel.add(namJtf);
        jtaPanel.setSize(jtADim);
        wrtJta.setSize(wrtDim);//500x800 400x375
        filJta.setSize(filDim);
        wrtJta.setLocation(0,0);
        filJta.setLocation(0,0);
        JScrollPane j1=new JScrollPane(wrtJta);
        JScrollPane j2=new JScrollPane(filJta);
        j1.setLocation(CONFIG.rate*50,CONFIG.rate*250);
        j2.setLocation(CONFIG.rate*50,CONFIG.rate*625);
        j1.setSize(wrtJta.getSize());
        j2.setSize(filJta.getSize());
        this.add(j1);
        this.add(j2);

        //jScrollPane.getViewport().add(jtaPanel);
        filJta.setEditable(false);
        jtaPanel.setLocation(CONFIG.rate*50,CONFIG.rate*250);


        tarJlb.setBackground(Color.WHITE);
        thmJlb.setBackground(Color.WHITE);
        tarJlb.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        thmJlb.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        addfJbt.setBackground(Color.WHITE);
        sendJbt.setBackground(Color.WHITE);
        addfJbt.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        sendJbt.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        wrtJta.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        namJlb.setBorder(BorderFactory.createLineBorder(Color.BLUE));

        wrtJta.setLineWrap(true);
        jtaPanel.setVisible(true);
        jPanel.setVisible(true);
        //jScrollPane.setVisible(true);

        sendJbt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Session ss=mail.getSession();
                try {
                    MimeMessage msg=mail.getMimeMessage();
                    String tarAdr=tarJtf.getText();
                    String thmStr=thmJtf.getText();
                    String namStr=namJtf.getText();
                    msg.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(tarAdr,namStr,"UTF-8"));
                    msg.setSubject(thmStr,CONFIG.encodingFormat);

                    MimeBodyPart text=new MimeBodyPart();
                    text.setContent(wrtJta.getText(),"text/html;charset=UTF-8");
                    MimeMultipart mp=new MimeMultipart();
                    mp.addBodyPart(text);
                    if(!fileArrayList.isEmpty()){
                        for(File f:fileArrayList){
                            MimeBodyPart attach=new MimeBodyPart();
                            DataHandler dh=new DataHandler(new FileDataSource(f));
                            attach.setDataHandler(dh);
                            attach.setFileName(f.getName());
                            mp.addBodyPart(attach);
                        }
                        mp.setSubType("mixed");
                    }
                    msg.setContent(mp);
                    msg.setSentDate(new Date());
                    msg.saveChanges();
                    Transport ts=mail.getTransport();
                    ts.sendMessage(msg,msg.getAllRecipients());
                    JOptionPane.showMessageDialog(null,"发送邮件成功");
                    wrtJta.setText("");

                    thmJtf.setText("");
                    fileArrayList.clear();
                    filJta.setText("附件：");

                }catch (Exception e_mail){
                    JOptionPane.showMessageDialog(null,"发送邮件失败");
                    e_mail.printStackTrace();
                    return ;
                }

            }
        });
        addfJbt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jfileChooser.showDialog(new JLabel(),"添加附件");
                File file= jfileChooser.getSelectedFile();
                if(file!=null)
                    fileArrayList.add(file);
                    filJta.append(file.getName());

            }
        });




        this.setSize(CONFIG.msgFrameDim);
        this.add(jPanel);
        this.add(jtaPanel);
        this.setVisible(true);
        repaint();
    }

}
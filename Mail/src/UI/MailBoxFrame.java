package UI;

import CONFIG.CONFIG;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MailBoxFrame extends JFrame {
    Session session;
    Store store;
    JPanel jPanel=new JPanel();
    JTable mailJtb;
    JButton readJbt=new JButton("查 看");
    JButton del_Jbt=new JButton("删 除");
    JButton downJbt=new JButton("下载附件");
    Dimension jbtSize=new Dimension(CONFIG.rate*200,CONFIG.rate*75);
    Dimension frameSize=new Dimension(CONFIG.msgFrameDim.height-CONFIG.rate*45,CONFIG.rate*15+CONFIG.msgFrameDim.width);
    Dimension panelSize=new Dimension(CONFIG.msgPanelDim.height,CONFIG.msgPanelDim.width);

    Folder folder;
    Message[] messages;
    int jbtLocY=385*CONFIG.rate;
    boolean deldebug=false;
    //JScrollPane jScrollPane;//=new JScrollPane(mailJtb);
    //500x800
    public MailBoxFrame(Session ss,Store st){

        super("收件箱");
        session=ss;
        store=st;
        try {
            folder=store.getFolder("inbox");
            folder.open(Folder.READ_WRITE);
            messages=folder.getMessages();
        }catch (Exception ee){
            ee.printStackTrace();
            JOptionPane.showMessageDialog(null,"邮箱连接失败");
        }

        this.setSize(frameSize);
        this.setLayout(null);

        jPanel.setSize(panelSize);
        jPanel.setLocation(0,0);
        jPanel.setLayout(null);

        readJbt.setSize(jbtSize);
        del_Jbt.setSize(jbtSize);
        downJbt.setSize(jbtSize);
        readJbt.setLocation(50*CONFIG.rate,jbtLocY);
        del_Jbt.setLocation(300*CONFIG.rate,jbtLocY);
        downJbt.setLocation(550*CONFIG.rate,jbtLocY);

        readJbt.setBackground(Color.WHITE);
        del_Jbt.setBackground(Color.WHITE);
        downJbt.setBackground(Color.WHITE);
        readJbt.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        del_Jbt.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        downJbt.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        readJbt.setFont(new Font("微软雅黑",Font.BOLD,22));
        del_Jbt.setFont(new Font("微软雅黑",Font.BOLD,22));
        downJbt.setFont(new Font("微软雅黑",Font.BOLD,22));


        jPanel.add(readJbt);
        jPanel.add(del_Jbt);
        jPanel.add(downJbt);

        Vector colName=new Vector();

        colName.add("主题");
        colName.add("发件人");
        colName.add("时间");
        colName.add("包含附件");
        colName.add("索引");
        Vector rowData=new Vector();
        //开始读邮件
        try {

            for(int i=0;i<messages.length;i++){
                Message msg=messages[i];
                Vector line=new Vector();
                //获得主题
                String subject=MimeUtility.decodeText(msg.getSubject());
                line.add(subject);
                //获得发件人
                String sender=getSender(msg);
                line.add(sender);
                //获得发件日期
                Date sentDate=msg.getSentDate();
                String date=new String("yyyy年MM月dd日 E HH:mm ");
                if(sentDate==null) date="";
                else date=new SimpleDateFormat(date).format(sentDate);
                line.add(date);
                line.add(IsAttach(msg));
                line.add(i);
                rowData.add(line);
            }
        }catch (Exception e_read){
            e_read.printStackTrace();
            JOptionPane.showMessageDialog(null,"读取邮件错误");
        }

        mailJtb=new JTable(rowData,colName){
            public boolean isCellEditable(int row,int column){
                return false;
            }
        };


        mailJtb.setSize(CONFIG.rate*700,CONFIG.rate*300);
//jScrollPane=new JScrollPane(mailJtb);
        mailJtb.setLocation(CONFIG.rate*50,CONFIG.rate*50);
        mailJtb.setFont(new Font("宋体",Font.PLAIN,15));
        //设置列宽
        TableColumn column;
        column=mailJtb.getColumnModel().getColumn(3);
        column.setPreferredWidth(70);
        //630
        for(int i=0;i<3;i++){
            column=mailJtb.getColumnModel().getColumn(i);
            column.setPreferredWidth(200);
        }
        column=mailJtb.getColumnModel().getColumn(4);
        column.setPreferredWidth(30);
        JScrollPane jScrollPane=new JScrollPane(mailJtb);
        jScrollPane.setSize(mailJtb.getSize());
        jScrollPane.setLocation(mailJtb.getLocation());

        jPanel.add(jScrollPane);

        this.add(jPanel);
        this.setVisible(true);
        repaint();

        //事件响应
        readJbt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] indexs=mailJtb.getSelectedRows();
                if(indexs.length<1){
                    JOptionPane.showMessageDialog(null,"请至少选择一封邮件");
                    return;
                }
                for(int index:indexs) {
                    Message msg=messages[(int) mailJtb.getValueAt(index,4)];
                    try {
                        ReadFrame readFrame = new ReadFrame(msg);
                    } catch (MessagingException messagingException) {
                        messagingException.printStackTrace();
                    } catch (UnsupportedEncodingException unsupportedEncodingException) {
                        unsupportedEncodingException.printStackTrace();
                    }
                }
            }
        });
        downJbt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] indexs=mailJtb.getSelectedRows();
                if(indexs.length<1){
                    JOptionPane.showMessageDialog(null,"请至少选择一封邮件下载");
                    return ;
                }
                for(int index:indexs){
                    Message msg=messages[(int)mailJtb.getValueAt(index,4)];
                    String subject;
                    try {
                        subject=MimeUtility.decodeText(msg.getSubject());
                        String path=CONFIG.getPath()+"\\";

                        File txt=new File(path+subject+".txt");
                        if(!txt.exists()) txt.createNewFile();
                        StringBuffer strBuffer=new StringBuffer();
                        //获得文本内容
                        getMailText(msg,strBuffer);
                        //保存文本内容
                        saveFile(strBuffer,txt);
                        //保存附件
                        saveAttachment(msg,path);
                        JOptionPane.showMessageDialog(null,"文件下载成功，路径:"+path);
                    } catch (UnsupportedEncodingException unsupportedEncodingException) {
                        unsupportedEncodingException.printStackTrace();
                    } catch (MessagingException messagingException) {
                        messagingException.printStackTrace();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }

            }
        });
        del_Jbt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] indexs= mailJtb.getSelectedRows();
                if(indexs.length==0){
                    return;
                }

                if(deldebug)
                    System.out.println(indexs.length);
                for(int i=0;i<indexs.length;i++){
                    //获得索引号
                    int index=indexs[i];
                    try {
                        DefaultTableModel tableModel=(DefaultTableModel) mailJtb.getModel();
                        int msgIndex=(int) mailJtb.getValueAt(index-i,4);
                        messages[msgIndex].setFlag(Flags.Flag.DELETED,true);
                        tableModel.removeRow(index-i);
                    }catch (Exception e_del){

                        JOptionPane.showMessageDialog(null,"邮件删除失败");
                    }
                }
            }
        });
        this.addWindowListener(new MywindowListener());
    }
    class MywindowListener extends WindowAdapter  {
        public void windowClosing(WindowEvent e) {
            super.windowClosing(e);
            try {
                folder.close(true);
            } catch (MessagingException messagingException) {
                messagingException.printStackTrace();
                JOptionPane.showMessageDialog(null,"收件箱关闭失败");

            }
        }
    }
    //获得发件人
    String getSender(Message msg) throws MessagingException, UnsupportedEncodingException {
        Address[] froms=msg.getFrom();
        String from="";
        InternetAddress address = (InternetAddress) froms[0];
        String person = address.getPersonal();
        if (person != null) {
            person = MimeUtility.decodeText(person) + " ";
        } else {
            person = "";
        }
        from = person + "<" + address.getAddress() + ">";
        return from;
    }
    //判断是否有附件
    boolean IsAttach(Part part) throws MessagingException, IOException {
        boolean flag = false;
        if (part.isMimeType("multipart/*")) {
            MimeMultipart multipart = (MimeMultipart) part.getContent();
            int partCount = multipart.getCount();
            for (int i = 0; i < partCount; i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                String disp = bodyPart.getDisposition();
                boolean Is = (disp != null &&
                        (disp.equalsIgnoreCase(Part.ATTACHMENT)
                                || disp.equalsIgnoreCase(Part.INLINE)));
                if (Is) return true;
                else if (bodyPart.isMimeType("multipart/*")) {
                    flag=  IsAttach(bodyPart);
                    if(flag) return true;
                }
                else {
                    String contentType = bodyPart.getContentType();
                    if (contentType.indexOf("application") != -1) {
                        return true;
                    }
                    if (contentType.indexOf("name") != -1) {
                        return true;
                    }
                }

            }
        }
        else if (part.isMimeType("message/rfc822")) {
            flag = IsAttach((Part) part.getContent());
        }
        return flag;
    }
    //递归获得邮件文本
    void getMailText(Part part, StringBuffer content)
            throws MessagingException, IOException {
        // 如果是文本类型的附件，通过getContent方法可以取到文本内容，但这不是我们需要的结果，所以在这里要做判断
        boolean isContainTextAttach = part.getContentType().indexOf("name") > 0;
        if (part.isMimeType("text/*") && !isContainTextAttach) {
            content.append(part.getContent().toString());
        } else if (part.isMimeType("message/rfc822")) {
            getMailText((Part) part.getContent(), content);
        } else if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();
            int partCount = multipart.getCount();
            for (int i = 0; i < partCount; i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                getMailText(bodyPart, content);
            }
        }
    }
    //保存文本内容
    void saveFile(StringBuffer strBuffer, File txt) throws FileNotFoundException, IOException {
        BufferedWriter bw=new BufferedWriter(new FileWriter(txt));
        Document document =  Jsoup.parse(strBuffer.toString());
        bw.write(document.text());
        bw.close();
    }
    //文本解码
    String decodeText(String encodeText) throws UnsupportedEncodingException {
        if (encodeText == null || "".equals(encodeText)) {
            return "";
        } else {
            return MimeUtility.decodeText(encodeText);
        }
    }
    //保存附件
    void saveFile(InputStream is, String destDir, String fileName) throws FileNotFoundException, IOException {
        BufferedInputStream bis = new BufferedInputStream(is);
        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(new File(destDir + fileName)));
        int len = -1;
        while ((len = bis.read()) != -1) {
            bos.write(len);
            bos.flush();
        }
        bos.close();
        bis.close();
    }
    void saveAttachment(Part part, String destDir) throws UnsupportedEncodingException, MessagingException, FileNotFoundException, IOException {
        if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();    //复杂体邮件
            //复杂体邮件包含多个邮件体
            int partCount = multipart.getCount();
            for (int i = 0; i < partCount; i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                String disp = bodyPart.getDisposition();
                if (disp != null && (disp.equalsIgnoreCase(Part.ATTACHMENT) || disp.equalsIgnoreCase(Part.INLINE))) {
                    InputStream is = bodyPart.getInputStream();
                    saveFile(is, destDir, decodeText(bodyPart.getFileName()));
                } else if (bodyPart.isMimeType("multipart/*")) {
                    saveAttachment(bodyPart, destDir);
                } else {
                    String contentType = bodyPart.getContentType();
                    if (contentType.indexOf("name") != -1 || contentType.indexOf("application") != -1) {
                        saveFile(bodyPart.getInputStream(), destDir, decodeText(bodyPart.getFileName()));
                    }
                }
            }
        }
        else if (part.isMimeType("message/rfc822")) {
            saveAttachment((Part) part.getContent(), destDir);
        }
    }
    //邮件文本过滤
    String Stringfilter(String str){
        String regEx_script="<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式
        String regEx_style="<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式
        String regEx_html="<[^>]+>"; //定义HTML标签的正则表达式
        Pattern p_script=Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE);
        Matcher m_script=p_script.matcher(str);
        str=m_script.replaceAll(""); //过滤script标签

        Pattern p_style= Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE);
        Matcher m_style=p_style.matcher(str);
        str=m_style.replaceAll(""); //过滤style标签

        Pattern p_html=Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE);
        Matcher m_html=p_html.matcher(str);
        str=m_html.replaceAll(""); //过滤html标签

        return str.trim(); //返回文本字符串

    }
    class ReadFrame extends JFrame{
        JPanel readPanel=new JPanel();
        JTextArea txtArea;
        JTextArea htmlArea;
        final int rate=CONFIG.rate;
        ReadFrame (Message msg) throws MessagingException, UnsupportedEncodingException {
            super(MimeUtility.decodeText(msg.getSubject()));
            this.setSize(CONFIG.msgFrameDim);
            this.setVisible(true);
            this.setLayout(null);
            this.add(readPanel);
            readPanel.setSize(CONFIG.msgPanelDim);
            StringBuffer stringBuffer=new StringBuffer();
            readPanel.setLayout(null);
            //500x800
            Dimension size=new Dimension(rate*400,rate*375);
            try {
                getMailText(msg,stringBuffer);
                String text=stringBuffer.toString();
                txtArea =new JTextArea(Stringfilter(text));
                txtArea.setSize(size);
                txtArea.setLocation(rate*50,25*rate);
                htmlArea=new JTextArea(text);
                htmlArea.setSize(size);
                htmlArea.setLocation(50*rate,425*rate);

                txtArea.setLineWrap(true);
                htmlArea.setLineWrap(true);

                JScrollPane txtscroll=new JScrollPane(txtArea);
                JScrollPane htmlscroll=new JScrollPane(htmlArea);
                txtscroll.setSize(txtArea.getSize());
                txtscroll.setLocation(50*rate,rate*50);
                htmlscroll.setBounds(htmlArea.getBounds());
               // txtscroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
               // htmlscroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                readPanel.add(txtscroll);
                readPanel.add(htmlscroll);
            } catch (MessagingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}

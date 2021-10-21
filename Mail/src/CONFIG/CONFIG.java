package CONFIG;

import SQL.SQL;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class CONFIG {

    public static SQL sql=new SQL();

    public static int rate           =(int)(Toolkit.getDefaultToolkit().getScreenSize().width/1920);
    public static Dimension panelDim =new Dimension((int) (640*rate),rate*400);
    public static Dimension frameDim=new Dimension(658*rate,450*rate);
    public static Dimension msgFrameDim=new Dimension(517*rate,860*rate);
    public static Dimension msgPanelDim=new Dimension(500*rate,800*rate);
    public static Dimension BoxPanelDim=new Dimension(rate*800,rate*500);
    public static Dimension BoxFrameDim=new Dimension(rate*820,rate*550);
    public static String encodingFormat=new String("UTF-8");
    public static String pathFile=new String("file/pathfile.txt");
    static boolean debug=false;
    public static String getPath(){
        File pathfile=new File(CONFIG.pathFile);
        try {
            BufferedReader br=new BufferedReader(new FileReader(pathfile));
            String path=br.readLine();
            if(debug) JOptionPane.showMessageDialog(null,path);
            return path;
        }catch (Exception e_pf){
            e_pf.printStackTrace();
            JOptionPane.showMessageDialog(null,"读取路径文件失败");
            return null;
        }
    }
}

package com.newSystem;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URL;

public class Settings {
    static private String userNmae;
    static private String rpcUser;
    static private String rpcPassword;
    private Font font;
    static public Font Font12;
    static public Font Font15;
    static public Font Font16;
    public Settings() {
        ////////////////////////////////////////////////////////////////
        userNmae = System.getProperty("user.name");
        ////////////////////////////////////////////////////////////////
        // AppData\\Roaming\\Bitcoin 폴더에 bincoind.exe가 있다고 가정.
        if (!isThereBitcoind()) {
            // bincoind.exe가 없을 경우 jar 파일로부터 복사해서옴.
            copyBitcoind();
        }
        ////////////////////////////////////////////////////////////////
        // AppData\\Roaming\\Bitcoin 폴더에 bincoin.conf 파일이 없을 경우
        // tempuser, temppassord로 파일을 생성.
        if (!isThereConfFile())
            makeConfFile();
            // bitcoin.conf 파일에서 rpcuser와 rpcpassword를 읽어 오는 함수.
        getRPCUserInfo();
        /////////////////////////////////////////////////////////////
        // 폰트 추가.
        ClassLoader classLoader = getClass().getClassLoader();
        URL fontUrl = classLoader.getResource("DejaVuSansMono.ttf");
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, fontUrl.openStream());
        } catch (Exception e) {
            System.err.println("Font loading Error.");
        }
        Font12 = font.deriveFont(Font.PLAIN, 12);
        Font15 = font.deriveFont(Font.PLAIN, 15);
        Font16 = font.deriveFont(Font.PLAIN, 16);
        ////////////////////////////////////////////////////////////////
        // License.txt 파일 복사.
        if (!isThereLicense())
            copyLicense();
        ////////////////////////////////////////////////////////////////
        globalUISettings();
        ////////////////////////////////////////////////////////////////
    }
    private void globalUISettings() {
        UIManager.put("Panel.background", Color.WHITE);
        UIManager.put("Button.background", Color.WHITE);
    }
    static public String getUserNmae() { return userNmae; }
    public void getRPCUserInfo() {
        try {
            ////////////////////////////////////////////////////////////////
            // bitcoin.conf file //
            String bitcoin_conf = "C:\\Users\\"
                    + userNmae
                    + "\\AppData\\Roaming\\Bitcoin\\bitcoin.conf";
            BufferedReader in = new BufferedReader(new FileReader(bitcoin_conf));
            String s;
            while ((s = in.readLine()) != null) {
                if (s.contains("rpcuser")) {
                    int equalIndex = s.indexOf('=');
                    rpcUser = s.substring(equalIndex + 1);
                }
                if (s.contains("rpcpassword")) {
                    int equalIndex = s.indexOf('=');
                    rpcPassword = s.substring(equalIndex + 1);
                }
            }
            in.close();
            ////////////////////////////////////////////////////////////////
        } catch (IOException e) {
            // 에러가 있다면 메시지 출력
            System.err.println("Error : From reading rpcuser & rpcpassword from bitcoin.conf file.");
            System.exit(1);
        }
    }
    public static String getRpcUser() {
        return rpcUser;
    }
    public static String getRpcPassword() {
        return rpcPassword;
    }
    private boolean isThereConfFile() {
        File bitcoinDIrectory = new File(
                "C:\\Users\\"
                        + userNmae
                        + "\\AppData\\Roaming\\Bitcoin\\bitcoin.conf");
        if (bitcoinDIrectory.exists() && bitcoinDIrectory.isDirectory())
            return true;
        else
            return false;
    }
    private boolean isThereBitcoind() {
        ///////////////////////////////////////////////////////////
        // AppData/Roaming/ 에 BItcoin 폴더가 없을 때 폴더를 새로 생성.
        File bitcoin_directory = new File(
                "C:\\Users\\"
                + userNmae
                + "\\AppData\\Roaming\\Bitcoin");
        if (!bitcoin_directory.exists()) {
            bitcoin_directory.mkdir();
        }
        //////////////////////////////////////////////////////////
        File bincoind_exe = new File(
                "C:\\Users\\"
                        + userNmae
                        + "\\AppData\\Roaming\\Bitcoin\\bincoind.exe");
        if (bincoind_exe.exists())
            return true;
        else
            return false;
    }
    private boolean isThereLicense() {
        //////////////////////////////////////////////////////////
        File licenses_txt = new File(
                "C:\\Users\\"
                        + userNmae
                        + "\\AppData\\Roaming\\Bitcoin\\Licenses.txt");
        if (licenses_txt.exists())
            return true;
        else
            return false;
    }
    private void copyLicense() {
        try {
            InputStream is = getClass().getClassLoader().getResource("Licenses.txt").openStream();

            //sets the output stream to a system folder
            OutputStream os = new FileOutputStream(
                    "C:\\Users\\"
                    + userNmae
                    + "\\AppData\\Roaming\\Bitcoin\\Licenses.txt");
            //2048 here is just my preference
            byte[] b = new byte[4096];
            int length;
            while ((length = is.read(b)) != -1) {
                os.write(b, 0, length);
            }
            is.close();
            os.close();
        } catch (Exception e) {
            System.err.println("Error : Copy Licenses.txt into System");
            System.exit(1);
        }
    }
    private void copyBitcoind() {
        try {
            InputStream is = getClass().getClassLoader().getResource("bincoind.exe").openStream();

            //sets the output stream to a system folder
            OutputStream os = new FileOutputStream("C:\\Users\\"
                                                          + userNmae
                                                          + "\\AppData\\Roaming\\Bitcoin\\bincoind.exe");
            //2048 here is just my preference
            byte[] b = new byte[4096];
            int length;
            while ((length = is.read(b)) != -1) {
                os.write(b, 0, length);
            }
            is.close();
            os.close();
        } catch (Exception e) {
            System.err.println("Error : Copy Bitcoind.exe into System");
            System.exit(1);
        }
    }
    private void makeConfFile() {
        String rpcuser = "tempuser";
        String rpcpassword = "tempassword";
        String fileName = "C:\\Users\\" + userNmae + "\\AppData\\Roaming\\Bitcoin\\bitcoin.conf";
        try{
            BufferedWriter fw = new BufferedWriter(new FileWriter(fileName, false));
            // 파일안에 문자열 쓰기
            fw.write("rpcuser=" + rpcuser);
            fw.newLine();
            fw.write("rpcpassword=" + rpcpassword);
            fw.newLine();
            fw.write("server=1");
            fw.newLine();
            fw.write("msnet=1");
            fw.newLine();
            fw.write("printtoconsole=1");
            fw.flush();
            // 객체 닫기
            fw.close();
        } catch(Exception e){
            System.err.println("Error : Making bitcoin.conf file error.");
        }
    }
}

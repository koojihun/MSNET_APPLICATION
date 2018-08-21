package com.newSystem.Dialogs;

import com.mommoo.flat.layout.linear.LinearLayout;
import com.mommoo.flat.layout.linear.Orientation;
import com.mommoo.flat.layout.linear.constraints.LinearConstraints;
import com.mommoo.flat.layout.linear.constraints.LinearSpace;

import javax.net.ssl.HttpsURLConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class SignUpForm extends JDialog {
    private DialogDefaultPanel mainPanel;
    private JButton signUpBtn;
    public SignUpForm() {
        setTitle("Sign Up");
        setSize(500, 500);
        setLocationRelativeTo(null); // 화면 중앙에 생기게 하는 옵션.
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        setIconImage(toolkit.getImage(getClass().getClassLoader().getResource("icon.png")));

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        mainPanel = new DialogDefaultPanel(10, DialogDefaultPanel.DIALOG.SIGNUP);
        mainPanel.setLayout(new LinearLayout(Orientation.VERTICAL, 0));
        mainPanel.makeExplainLine("Registeration");
        mainPanel.makeNonEmptyLine("Company Name", "", true);
        mainPanel.makePasswordLine("Password");
        mainPanel.makeNonEmptyLine("Director's Name", "", true);
        mainPanel.makeNonEmptyLine("Director's Email", "", true);
        mainPanel.makeNonEmptyLine("Director's Phone", "", true);

        for (int cnt = 0; cnt < mainPanel.eachLine.size(); cnt++) {
            mainPanel.eachLine.get(cnt).setBackground(Color.WHITE);
        }

        makeButtonLine();
        add(mainPanel);
        setVisible(true);
    }
    public void makeButtonLine() {
        JPanel targetLine = new JPanel(new LinearLayout(Orientation.HORIZONTAL, 0));
        targetLine.setBackground(Color.WHITE);
        targetLine.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        targetLine.add(signUpBtn = new JButton("SIGN UP"),
                new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.WRAP_CENTER_CONTENT));
        signUpBtn.setFont(new Font("Consolas", Font.PLAIN, 16));
        signUpBtn.setFocusPainted(false);
        signUpBtn.addActionListener(new clickListener());
        mainPanel.add(targetLine, new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
    }
    class clickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String CompanyName = mainPanel.eachText.get(1).getText();
            String Password = mainPanel.eachText.get(2).getText();
            String DirectorName = mainPanel.eachText.get(3).getText();
            String DirectorEmail = mainPanel.eachText.get(4).getText();
            String DirectorPhone = mainPanel.eachText.get(5).getText();

            try {
                String url = "https://selfsolve.apple.com/wcResults.do";
                URL obj = new URL(url);
                HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

                //add reuqest header
                con.setRequestMethod("POST");
                con.setRequestProperty("User-Agent", "Chrome/version");
                con.setRequestProperty("Accept-Charset", "UTF-8");
                con.setRequestProperty("Content-Type", "text/plain; charset=utf-8");

                String urlParameters = "company=" + CompanyName + "&password=" + Password +
                        "&name=" + DirectorName + "&email=" + DirectorEmail + "&phone=" + DirectorPhone;

                // Send post request
                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();

                int responseCode = con.getResponseCode();
                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters);
                System.out.println("Response Code : " + responseCode);

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                //print result
                System.out.println(response.toString());
                if (response.equals("success")) {
                    // TODO:
                } else {
                    // sign up fail --> program exit
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}

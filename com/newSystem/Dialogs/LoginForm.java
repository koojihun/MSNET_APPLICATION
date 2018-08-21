package com.newSystem.Dialogs;

import com.mommoo.flat.layout.linear.LinearLayout;
import com.mommoo.flat.layout.linear.Orientation;
import com.mommoo.flat.layout.linear.constraints.LinearConstraints;
import com.mommoo.flat.layout.linear.constraints.LinearSpace;
import com.newSystem.Settings;

import javax.net.ssl.HttpsURLConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class LoginForm extends JDialog {
    private DialogDefaultPanel mainPanel;
    private JButton loginBtn;
    private JButton SignUpBtn;
    public LoginForm() {
        setTitle("Log In");
        setSize(500, 350);
        setLocationRelativeTo(null); // 화면 중앙에 생기게 하는 옵션.
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        setIconImage(toolkit.getImage(getClass().getClassLoader().getResource("icon.png")));

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                System.exit(0);
            }
        });

        mainPanel = new DialogDefaultPanel(10, DialogDefaultPanel.DIALOG.SIGNUP);
        mainPanel.setLayout(new LinearLayout(Orientation.VERTICAL, 0));
        mainPanel.makeExplainLine("Log Into New System.");
        mainPanel.makeNonEmptyLine("Company Name", "", true);
        mainPanel.makePasswordLine("Password");

        for (int cnt = 0; cnt < mainPanel.eachLine.size(); cnt++) {
            mainPanel.eachLine.get(cnt).setBackground(Color.WHITE);
        }

        JPanel targetLine;
        mainPanel.eachLine.add(targetLine = new JPanel(new LinearLayout(Orientation.HORIZONTAL, 10)));
        targetLine.setBackground(Color.WHITE);

        targetLine.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        targetLine.add(loginBtn = new JButton("Log In"), new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.WRAP_CENTER_CONTENT));
        loginBtn.setFont(Settings.Font14);
        loginBtn.setFocusPainted(false);
        loginBtn.addActionListener(new clickListener());

        targetLine.add(SignUpBtn = new JButton("Sign Up"), new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.WRAP_CENTER_CONTENT));
        SignUpBtn.setFont(Settings.Font14);
        SignUpBtn.setFocusPainted(false);
        SignUpBtn.addActionListener(new clickListener());

        loginBtn.setBackground(Color.WHITE);
        SignUpBtn.setBackground(Color.WHITE);
        loginBtn.setFont(new Font("Consolas", Font.PLAIN, 16));
        SignUpBtn.setFont(new Font("Consolas", Font.PLAIN, 16));

        mainPanel.add(targetLine, new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
        add(mainPanel);
        setVisible(true);
    }

    class clickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == SignUpBtn)
                new SignUpForm();
            else if (e.getSource() == loginBtn) {
                String companyName = mainPanel.eachText.get(1).getText();
                String password = mainPanel.eachText.get(2).getText();
                try {
                    String url = "https://selfsolve.apple.com/wcResults.do";
                    URL obj = new URL(url);
                    HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

                    //add reuqest header
                    con.setRequestMethod("POST");
                    con.setRequestProperty("User-Agent", "Chrome/version");
                    con.setRequestProperty("Accept-Charset", "UTF-8");
                    con.setRequestProperty("Content-Type", "text/plain; charset=utf-8");

                    String urlParameters = "company=" + companyName + "&password=" + password;

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
}

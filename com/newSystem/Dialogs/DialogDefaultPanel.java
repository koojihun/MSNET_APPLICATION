package com.newSystem.Dialogs;


import com.mommoo.flat.layout.linear.LinearLayout;
import com.mommoo.flat.layout.linear.Orientation;
import com.mommoo.flat.layout.linear.constraints.LinearConstraints;
import com.mommoo.flat.layout.linear.constraints.LinearSpace;
import com.newSystem.Bitcoins.Bitcoind;
import com.newSystem.MainFrame;
import com.newSystem.MidPanel;
import com.newSystem.Settings;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.net.URL;

import static com.newSystem.MainFrame.bitcoinJSONRPCClient;

public class DialogDefaultPanel extends JPanel {
    public enum DIALOG {
        ADDPRODUCT, ADDMANYPRODUCT, ADDADDRESS, ADDPEER, INFO, MINING, FIND, TRACK, IMPORTADDRESS, TXINFO, SAVEADDRESS, SENDADDRESSTOSERVER, TRACKLOCATION
    }

    public JPanel[] eachLine; // if total = 4 Lines, 4th line will contain ok & cancel buttons.
    public JLabel[] eachLabel;
    public JTextField[] eachText;
    JButton okBtn;
    JButton cancelBtn;
    JButton findBtn;
    JButton trackBtn;
    int lineCount;
    int padding;
    DIALOG dialog;
    ClickListener clickListener;

    DialogDefaultPanel(int max, int padding, DIALOG dialog) {
        lineCount = 0;
        this.padding = padding;
        eachLine = new JPanel[max];
        eachLabel = new JLabel[max];
        eachText = new JTextField[max];
        this.dialog = dialog;
        setLayout(new LinearLayout(Orientation.VERTICAL, 10));
        clickListener = new ClickListener();
    }

    public void makeEmptyLine() {
        JPanel targetLine = eachLine[lineCount++] = new JPanel(new LinearLayout(Orientation.HORIZONTAL, 10));
        targetLine.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
        add(targetLine, new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
    }

    public void makeNonEmptyLine(String l, String t, boolean textFieldChange) {
        JPanel targetLine = eachLine[lineCount] = new JPanel(new LinearLayout(Orientation.HORIZONTAL, 10));
        JLabel targetLabel = eachLabel[lineCount] = new JLabel(l);
        JTextField targetText = eachText[lineCount++] = new JTextField();
        targetLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        targetLabel.setVerticalAlignment(SwingConstants.CENTER);
        targetLabel.setFont(Settings.Font14);
        targetText.setFont(Settings.Font14);
        targetText.setText(t);

        if (!textFieldChange)
            targetText.setEditable(false);

        targetLine.add(targetLabel, new LinearConstraints().setWeight(4).setLinearSpace(LinearSpace.WRAP_CENTER_CONTENT));
        targetLine.add(targetText, new LinearConstraints().setWeight(6).setLinearSpace(LinearSpace.WRAP_CENTER_CONTENT));
        targetLine.setBorder(BorderFactory.createEmptyBorder(padding + 10, padding, padding, padding));
        add(targetLine, new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
    }

    public void makeButtonLine() {
        JPanel targetLine = eachLine[lineCount++] = new JPanel(new LinearLayout(Orientation.HORIZONTAL, 10));
        targetLine.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
        targetLine.add(okBtn = new JButton("OK"), new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.WRAP_CENTER_CONTENT));
        okBtn.setFont(Settings.Font14);
        okBtn.setFocusPainted(false);
        okBtn.addActionListener(clickListener);

        if (dialog != DIALOG.INFO && dialog != DIALOG.MINING && dialog != DIALOG.FIND && dialog != DIALOG.TRACK) {
            targetLine.add(cancelBtn = new JButton("CANCEL"), new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.WRAP_CENTER_CONTENT));
            cancelBtn.setFont(Settings.Font14);
            cancelBtn.setFocusPainted(false);
            cancelBtn.addActionListener(clickListener);
        }

        add(targetLine, new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
    }

    public void makeLabelLine(String explanation) {

    }

    public void makeTrackButtonLine() {
        JPanel targetLine = eachLine[lineCount++] = new JPanel(new LinearLayout(Orientation.HORIZONTAL));
        targetLine.setBorder(BorderFactory.createEmptyBorder(0, padding, 0, padding));
        targetLine.add(trackBtn = new JButton("Track"), new LinearConstraints().setWeight(4).setLinearSpace(LinearSpace.WRAP_CENTER_CONTENT));
        trackBtn.setFont(Settings.Font14);
        trackBtn.setFocusPainted(false);
        trackBtn.addActionListener(clickListener);
        add(targetLine, new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
    }

    class ClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton clicked = (JButton) e.getSource();
            // Info 와 Mining 창은 버튼이 1개이므로 눌리면 바로 종료.
            if (clicked == cancelBtn || dialog == DIALOG.INFO || dialog == DIALOG.MINING)
                // 해당 다이얼로그 종료.
                SwingUtilities.getWindowAncestor(clicked).dispose();
            else if (clicked == okBtn) {
                if (dialog == DIALOG.ADDADDRESS) {
                    // add 창에서 address 추가인 경우 첫번째라인은 비어 있음.
                    String account = eachText[1].getText();
                    bitcoinJSONRPCClient.get_new_address(account);
                    SwingUtilities.getWindowAncestor(clicked).dispose();
                } else if (dialog == DIALOG.ADDPRODUCT) {
                    // add 창에서 product 추가인 경우.
                    String cc = eachText[0].getText();
                    String zc = eachText[1].getText();
                    String count = eachText[2].getText();
                    // Product info가 빈칸일 때 경고 메시지.
                    if (cc.equals("") || zc.equals("") || count.equals("")) {
                        JOptionPane.showMessageDialog(null,
                                "Insert product information.",
                                "Message", JOptionPane.WARNING_MESSAGE);
                    } else if (cc.length() > 6 || zc.length() > 10) {
                        JOptionPane.showMessageDialog(null,
                                "Too long Country Code & Zip Code.",
                                "Message", JOptionPane.WARNING_MESSAGE);
                    } else {
                        try {
                            FileWriter fw = new FileWriter("C:\\Users\\" + Settings.getUserNmae() + "\\AppData\\Roaming\\Bitcoin\\ProductList.txt", true);
                            for (int cnt = 0; cnt < Integer.valueOf(count); cnt++) {
                                String tmpPID = bitcoinJSONRPCClient.gen_new_product(cc, zc);
                                fw.write(tmpPID + "\r\n");
                            }
                            fw.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        SwingUtilities.getWindowAncestor(clicked).dispose();
                    }
                } else if (dialog == DIALOG.IMPORTADDRESS) {
                    String account = eachText[0].getText();
                    String address = eachText[1].getText();
                    // account 또는 address가 빈칸일 때 경고 메시지.
                    if (account.equals("") || address.equals("")) {
                        JOptionPane.showMessageDialog(null, "Insert Account and Address.", "Message", JOptionPane.WARNING_MESSAGE);
                    } else {
                        bitcoinJSONRPCClient.importAddress(address, account, true);
                        SwingUtilities.getWindowAncestor(clicked).dispose();
                        new ImportDialog();
                    }
                } else if (dialog == DIALOG.ADDPEER) {
                    // 아무것도 입력하지 않았을 때 경고 메시지.
                    if (eachText[1].getText().equals("")) {
                        JOptionPane.showMessageDialog(null, "Insert IP address.", "Message", JOptionPane.WARNING_MESSAGE);
                    } else {
                        try {
                            ////////////////////////////////////////////////////////////////////////////////////
                            String fileName = "C:\\Users\\" +
                                    System.getProperty("user.name") +
                                    "\\AppData\\Roaming\\Bitcoin\\bitcoin.conf";
                            BufferedWriter fw = new BufferedWriter(new FileWriter(fileName, true));
                            // 파일안에 문자열 쓰기
                            fw.newLine();
                            fw.write("addnode=" + eachText[1].getText());
                            fw.newLine();
                            fw.flush();
                            // 객체 닫기
                            fw.close();
                            ////////////////////////////////////////////////////////////////////////////////////
                            try {
                                Bitcoind.killBitcoind();
                                new Bitcoind(MidPanel.getBitcoindArea()).start();
                            } catch (Exception exception) {
                                System.err.println("Error : kill bitcoind.exe in add peer.");
                            }
                            ////////////////////////////////////////////////////////////////////////////////////
                            SwingUtilities.getWindowAncestor(clicked).dispose();
                        } catch (IOException ex) {
                            System.err.println("Error : Add Node Error.");
                        }
                    }
                } else if (dialog == DIALOG.SAVEADDRESS) {
                    String name = eachText[1].getText();
                    String address = eachText[2].getText();
                    if (name.length() == 0 || address.length() == 0)
                        return;
                    String[] addRow = {name, address};
                    AddressDialog.savedAddressTableModel.addRow(addRow);
                    SwingUtilities.getWindowAncestor(clicked).dispose();
                } else if (dialog == DIALOG.SENDADDRESSTOSERVER) {
                    String companyName = eachText[1].getText();
                    if (companyName.length() == 0) {
                        JOptionPane.showMessageDialog(null, "Insert name of Company.", "Message", JOptionPane.WARNING_MESSAGE);

                    } else {
                        String url = "http://166.104.126.21:9999/?method=0&account=" + companyName + "&address=" + bitcoinJSONRPCClient.get_account_address("");
                        try {
                            URL obj = new URL(url);
                            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                            con.setRequestMethod("GET");
                            //add request header 헤더를 만들어주는것.
                            con.setRequestProperty("User-Agent", "Chrome/version");
                            con.setRequestProperty("Accept-Charset", "UTF-8");
                            con.setRequestProperty("Content-Type", "text/plain; charset=utf-8");
                            int responseCode = con.getResponseCode();
                            BufferedReader in = new BufferedReader(
                                    new InputStreamReader(con.getInputStream()));
                            String inputLine;
                            StringBuffer response = new StringBuffer();

                            while ((inputLine = in.readLine()) != null) {
                                response.append(inputLine);
                            }
                            in.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                } else if (dialog == DIALOG.TRACKLOCATION) {
                    HashMap<String, Integer> map = new HashMap<String, Integer>();
                    String filename = eachText[0].getText();

                    try {

                        BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\" + Settings.getUserNmae() + "\\AppData\\Roaming\\Bitcoin\\" + filename));
                        while (true) {
                            String pID = br.readLine();
                            if (pID == null) {
                                break;
                            } else {
                                List<Map> track_prouct_Result = MainFrame.bitcoinJSONRPCClient.track_product(pID);
                                if (track_prouct_Result.size() != 0) {
                                    String userID = track_prouct_Result.get(0).get("\"ID\"").toString();
                                    if (map.containsKey(userID)) {
                                        map.put(userID, map.get(userID) + 1);
                                    } else {
                                        map.put(track_prouct_Result.get(0).get("\"ID\"").toString(), 1);
                                    }
                                }
                            }
                        }
                       br.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    TrackLocationDialog.getTrackLocationTableModel().setNumRows(0);
                    Iterator<String> itr = map.keySet().iterator();
                    String[] row = new String[3];
                    int count = 0;
                    while(itr.hasNext()){
                        String key = (String)itr.next();
                        int value = map.get(key);
                        count++;
                        row[0] = String.valueOf(count);
                        row[1] = key;
                        row[2] = String.valueOf(value);
                        TrackLocationDialog.getTrackLocationTableModel().addRow(row);
                    }
                }
            } else if (clicked == trackBtn) {
                // table을 초기화 시켜주기 위해서
                int rowCount = TrackDialog.getTrackTableModel().getRowCount();
                for (int i = rowCount - 1; i >= 0; i--) {
                    TrackDialog.getTrackTableModel().removeRow(i);
                }
                String id = eachText[0].getText();
                // Product ID를 빈칸으로 뒀을 때 경고 메시지.
                if (id.equals("")) {
                    JOptionPane.showMessageDialog(null, "Insert Product ID.", "Message", JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        List<Map> result = bitcoinJSONRPCClient.track_product(id);
                        int resultSize = result.size();
                        int count = 0;
                        String[][] rows = new String[result.size()][3];

                        for (Map map : result) {
                            rows[count][0] = String.valueOf(resultSize);
                            rows[count][1] = String.valueOf(map.get("\"Time\""));
                            rows[count][2] = String.valueOf(map.get("\"ID\""));

                            count++;
                            resultSize--;
                        }
                        for (int i = result.size() - 1; i >= 0; i--) {
                            TrackDialog.getTrackTableModel().addRow(rows[i]);
                        }
                    } catch (ClassCastException err) {
                        JOptionPane.showMessageDialog(null, "There is no product " + id, "Message", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        }
    }
}

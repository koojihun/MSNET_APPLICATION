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
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ListIterator;

public class DialogDefaultPanel extends JPanel {
    public enum DIALOG {
        ADDPRODUCT, ADDMANYPRODUCT, ADDADDRESS, ADDPEER, INFO, MINING, FIND, TRACK, IMPORTADDRESS, TXINFO, SAVEADDRESS
    }

    public JPanel[] eachLine; // total = 4 Lines, 4th line will contain ok & cancel buttons.
    public JLabel[] eachLabel;
    public JTextField[] eachText;
    JButton okBtn;
    JButton cancelBtn;
    JButton findBtn;
    JButton trackBtn;
    int lineCount;
    int padding;
    DIALOG dialog;

    DialogDefaultPanel(int max, int padding, DIALOG dialog) {
        lineCount = 0;
        this.padding = padding;
        eachLine = new JPanel[max];
        eachLabel = new JLabel[max];
        eachText = new JTextField[max];
        this.dialog = dialog;
        setLayout(new LinearLayout(Orientation.VERTICAL, 10));
        setBackground(Color.WHITE);
    }

    public void makeEmptyLine() {
        JPanel targetLine = eachLine[lineCount++] = new JPanel(new LinearLayout(Orientation.HORIZONTAL, 10));
        targetLine.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding)); //상하좌우 10씩 띄우기
        add(targetLine, new LinearConstraints().setWeight(2).setLinearSpace(LinearSpace.MATCH_PARENT));
    }

    public void makeNonEmptyLine(String l, String t, boolean textFieldChange) {
        JPanel targetLine = eachLine[lineCount] = new JPanel(new LinearLayout(Orientation.HORIZONTAL, 10));
        JLabel targetLabel = eachLabel[lineCount] = new JLabel(l);
        JTextField targetText = eachText[lineCount++] = new JTextField();
        targetLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        targetLabel.setVerticalAlignment(SwingConstants.CENTER);
        targetLabel.setFont(Settings.Font12);
        targetText.setFont(Settings.Font12);
        targetText.setText(t);
        if (!textFieldChange) {
            targetText.setEditable(false);
            targetText.setBorder(BorderFactory.createEtchedBorder());
        }
        targetLine.add(targetLabel, new LinearConstraints().setWeight(4));
        targetLine.add(targetText, new LinearConstraints().setWeight(6));
        targetLabel.setBackground(Color.white);
        targetText.setBackground(Color.WHITE);
        targetLine.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding)); //상하좌우 10씩 띄우기
        add(targetLine, new LinearConstraints().setWeight(2).setLinearSpace(LinearSpace.MATCH_PARENT));
    }

    public void makeButtonLine() {
        DialogDefaultPanel.ClickListener clickListener = new DialogDefaultPanel.ClickListener();
        JPanel targetLine = eachLine[lineCount++] = new JPanel(new LinearLayout(Orientation.HORIZONTAL, 10));
        targetLine.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding)); //상하좌우 10씩 띄우기 //!!!
        targetLine.add(okBtn = new JButton("OK"), new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.WRAP_CENTER_CONTENT));
        okBtn.setFont(Settings.Font12);
        okBtn.setFocusPainted(false);
        okBtn.addActionListener(clickListener);

        if (dialog != DIALOG.INFO && dialog != DIALOG.MINING && dialog != DIALOG.FIND && dialog != DIALOG.TRACK) {
            targetLine.add(cancelBtn = new JButton("CANCEL"), new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.WRAP_CENTER_CONTENT));
            cancelBtn.setFont(Settings.Font12);
            cancelBtn.setFocusPainted(false);
            cancelBtn.addActionListener(clickListener);
        }
        add(targetLine, new LinearConstraints().setWeight(2).setLinearSpace(LinearSpace.MATCH_PARENT));
    }

    public void makeFindButtonLine() {
        ClickListener clickListener = new ClickListener();
        JPanel targetLine = eachLine[lineCount++] = new JPanel(new LinearLayout(Orientation.HORIZONTAL, 10));
        targetLine.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding)); //상하좌우 10씩 띄우기 //!!!
        targetLine.add(findBtn = new JButton("Find"), new LinearConstraints().setWeight(4).setLinearSpace(LinearSpace.WRAP_CENTER_CONTENT));
        findBtn.setFont(Settings.Font12);
        findBtn.setFocusPainted(false);
        findBtn.addActionListener(clickListener);
        add(targetLine, new LinearConstraints().setWeight(2).setLinearSpace(LinearSpace.MATCH_PARENT));
    }

    public void makeTrackButtonLine() {
        DialogDefaultPanel.ClickListener clickListener = new DialogDefaultPanel.ClickListener();
        JPanel targetLine = eachLine[lineCount++] = new JPanel(new LinearLayout(Orientation.HORIZONTAL, 10));
        targetLine.setBackground(Color.WHITE);
        targetLine.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding)); //상하좌우 10씩 띄우기 //!!!
        targetLine.add(trackBtn = new JButton("Track"), new LinearConstraints().setWeight(4).setLinearSpace(LinearSpace.WRAP_CENTER_CONTENT));
        trackBtn.setFont(Settings.Font12);
        trackBtn.setBackground(Color.WHITE);
        trackBtn.setFocusPainted(false);
        trackBtn.addActionListener(clickListener);
        add(targetLine, new LinearConstraints().setWeight(2).setLinearSpace(LinearSpace.MATCH_PARENT));
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
                    MainFrame.bitcoinJSONRPCClient.get_new_address(account);
                    SwingUtilities.getWindowAncestor(clicked).dispose();
                } else if (dialog == DIALOG.ADDPRODUCT) {
                    // add 창에서 product 추가인 경우.
                    String cc = eachText[1].getText();
                    String zc = eachText[2].getText();
                    // Product info가 빈칸일 때 경고 메시지.
                    if (cc.equals("") || zc.equals("")) {
                        JOptionPane.showMessageDialog(null, "Insert product information.", "Message", JOptionPane.WARNING_MESSAGE);
                    } else {
                        MainFrame.bitcoinJSONRPCClient.gen_new_product(cc, zc);
                        SwingUtilities.getWindowAncestor(clicked).dispose();
                    }
                } else if (dialog == DIALOG.ADDMANYPRODUCT) {
                    String productNum = eachText[1].getText();
                    if (productNum.equals("")) {
                        JOptionPane.showMessageDialog(null, "Insert amount of products.", "Message", JOptionPane.WARNING_MESSAGE);
                    } else {
                        for (int i = 0; i < Integer.parseInt(productNum); i++) {
                            String cc = String.valueOf(i);
                            String zc = String.valueOf(i);
                            MainFrame.bitcoinJSONRPCClient.gen_new_product(cc, zc);
                            SwingUtilities.getWindowAncestor(clicked).dispose();
                        }
                    }
                } else if (dialog == DIALOG.IMPORTADDRESS) {
                    String account = eachText[0].getText();
                    String address = eachText[1].getText();
                    // account 또는 address가 빈칸일 때 경고 메시지.
                    if (account.equals("") || address.equals("")) {
                        JOptionPane.showMessageDialog(null, "Insert Account and Address.", "Message", JOptionPane.WARNING_MESSAGE);
                    } else {
                        MainFrame.bitcoinJSONRPCClient.importAddress(address, account, true);
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
                }
            } else if (clicked == findBtn) {
                MidPanel.getCurrentProducts();
                String id = eachText[0].getText();
                String cc = eachText[1].getText();
                String zc = eachText[2].getText();
                // Product info를 빈칸으로 뒀을 때 경고 메시지.
                if (id.equals("") || cc.equals("") || zc.equals("")) {
                    JOptionPane.showMessageDialog(null, "Insert product information.", "Message", JOptionPane.WARNING_MESSAGE);
                } else {
                    JTable productTable = MidPanel.getProductTable();
                    DefaultTableModel productTableModel = MidPanel.getProductTableModel();
                    int resultRow = -1;
                    for (int r = 0; r < productTable.getRowCount(); r++) {
                        if (productTableModel.getValueAt(r, 1).equals(id) &&
                            productTableModel.getValueAt(r, 2).equals(cc) &&
                            productTableModel.getValueAt(r, 3).equals(zc
                            )) {
                            resultRow = r;
                            break;
                        }
                    }
                    if (resultRow == -1) {
                        JOptionPane.showMessageDialog(null, "There is no " + id, "Message", JOptionPane.WARNING_MESSAGE);
                        SwingUtilities.getWindowAncestor(clicked).dispose();
                        return;
                    }
                    productTable.setRowSelectionInterval(resultRow, resultRow);
                    productTable.scrollRectToVisible(productTable.getCellRect(resultRow, productTable.getColumnCount(), true));
                    SwingUtilities.getWindowAncestor(clicked).dispose();
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
                        java.util.List<String> result = MainFrame.bitcoinJSONRPCClient.track_product(id);
                        ListIterator<String> itr = result.listIterator(result.size()); // reverse order로 iterate 함.
                        int cnt = 1;
                        String[] firstRow = {String.valueOf(cnt++), "", "Producer"}; // [No, Time, Sender] 순으로 출력
                        TrackDialog.getTrackTableModel().addRow(firstRow);

                        while (itr.hasPrevious()) {
                            long time = Long.parseLong(itr.previous()); // tx 시간을 입력 받음(1970.01.01 기준 몇 초 지났는지가 time에 저장됨)
                            Date d = new Date(time * 1000); // 시간 형식으로 변환
                            SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 2018-08-06 10:19:33 과 같은 형식으로 지정
                            String[] tmp = {String.valueOf(cnt++), timeFormat.format(d), itr.previous()};

                            TrackDialog.getTrackTableModel().addRow(tmp);
                        }
                    } catch (ClassCastException err) {
                        JOptionPane.showMessageDialog(null, "There is no product " + id, "Message", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        }
    }
}

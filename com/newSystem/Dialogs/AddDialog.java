package com.newSystem.Dialogs;

import com.mommoo.flat.layout.linear.LinearLayout;
import com.mommoo.flat.layout.linear.Orientation;
import com.mommoo.flat.layout.linear.constraints.LinearConstraints;
import com.mommoo.flat.layout.linear.constraints.LinearSpace;
import com.newSystem.Settings;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;

public class AddDialog extends JDialog {
    private DialogDefaultPanel productPanel;
    private DialogDefaultPanel productManyPanel;
    private DialogDefaultPanel addressPanel;
    private DialogDefaultPanel peerPanel;
    private DialogDefaultPanel sendAddressToServerPanel; // 생산자의 서버로 address를 보내줌


    public AddDialog() {
        setTitle("Make a new product OR Add a new Address");
        setLocation(200, 200);
        setSize(500, 300);
        // Icon 설정
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("icon.png")));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new LinearLayout(Orientation.VERTICAL));
        add(mainPanel);

        UIManager.put("TabbedPane.borderHightlightColor", new ColorUIResource( Color.RED ));
        UIManager.put("TabbedPane.darkShadow", new ColorUIResource( Color.RED ));

        JTabbedPane tabbedPane = new JTabbedPane();
        mainPanel.add(tabbedPane, new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));

        productPanel = new DialogDefaultPanel(4, 20, DialogDefaultPanel.DIALOG.ADDPRODUCT);
        //productPanel.makeNonEmptyLine("Product ID ", null, true);
        productPanel.makeEmptyLine();
        productPanel.makeNonEmptyLine("Country Code ", null, true);
        productPanel.makeNonEmptyLine("Zip Code ", null, true);
        productPanel.makeButtonLine();

        productManyPanel = new DialogDefaultPanel(4, 20, DialogDefaultPanel.DIALOG.ADDMANYPRODUCT);
        productManyPanel.makeEmptyLine();
        productManyPanel.makeNonEmptyLine("Total number of products ", null, true);
        productManyPanel.makeEmptyLine();
        productManyPanel.makeButtonLine();

        addressPanel = new DialogDefaultPanel(4, 20, DialogDefaultPanel.DIALOG.ADDADDRESS);
        addressPanel.makeEmptyLine();
        addressPanel.makeNonEmptyLine("Account ", null, true);
        addressPanel.makeEmptyLine();
        addressPanel.makeButtonLine();

        peerPanel = new DialogDefaultPanel(4, 20, DialogDefaultPanel.DIALOG.ADDPEER);
        peerPanel.makeEmptyLine();
        peerPanel.makeNonEmptyLine("Peer IP ", null, true);
        peerPanel.makeEmptyLine();
        peerPanel.makeButtonLine();

        sendAddressToServerPanel = new DialogDefaultPanel(4, 20, DialogDefaultPanel.DIALOG.SENDADDRESSTOSERVER);
        sendAddressToServerPanel.makeEmptyLine();
        sendAddressToServerPanel.makeNonEmptyLine("Company name", null, true);
        sendAddressToServerPanel.makeEmptyLine();
        sendAddressToServerPanel.makeButtonLine();

        tabbedPane.addTab("product", productPanel);
        tabbedPane.addTab("Many product", productManyPanel);
        tabbedPane.addTab("address", addressPanel);
        tabbedPane.addTab("peer", peerPanel);
        tabbedPane.addTab("send address", sendAddressToServerPanel);

        tabbedPane.setFont(Settings.Font12);
        tabbedPane.setBackgroundAt(0, Color.WHITE);
        tabbedPane.setBackgroundAt(1, Color.WHITE);
        tabbedPane.setBackgroundAt(2, Color.WHITE);
        tabbedPane.setBackgroundAt(3, Color.WHITE);
        tabbedPane.setBackgroundAt(4, Color.WHITE);

        setVisible(true);
    }
}

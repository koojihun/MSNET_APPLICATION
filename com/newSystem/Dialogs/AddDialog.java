package com.newSystem.Dialogs;

import com.mommoo.flat.layout.linear.LinearLayout;
import com.mommoo.flat.layout.linear.Orientation;
import com.mommoo.flat.layout.linear.constraints.LinearConstraints;
import com.mommoo.flat.layout.linear.constraints.LinearSpace;
import com.newSystem.Settings;

import javax.swing.*;

public class AddDialog extends JDialog {
    private DialogDefaultPanel productPanel;
    private DialogDefaultPanel addressPanel;
    private DialogDefaultPanel peerPanel;

    public AddDialog() {
        setTitle("Make a new product OR Add a new Address");
        setLocation(200, 200);
        setSize(500, 300);

        // Icon 설정
        setIconImage(Settings.icon);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new LinearLayout(Orientation.VERTICAL));
        add(mainPanel);

        JTabbedPane tabbedPane = new JTabbedPane();
        mainPanel.add(tabbedPane, new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));

        productPanel = new DialogDefaultPanel(4, 20, DialogDefaultPanel.DIALOG.ADDPRODUCT);
        productPanel.makeNonEmptyLine("Country Code ", null, true);
        productPanel.makeNonEmptyLine("Zip Code ", null, true);
        productPanel.makeNonEmptyLine("Count ", null, true);
        productPanel.makeButtonLine();

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

        tabbedPane.addTab("product", productPanel);
        tabbedPane.addTab("address", addressPanel);
        tabbedPane.addTab("peer", peerPanel);

        tabbedPane.setFont(Settings.Font14);

        setVisible(true);
    }
}

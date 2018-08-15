package com.newSystem.Dialogs;

import com.mommoo.flat.layout.linear.LinearLayout;
import com.mommoo.flat.layout.linear.Orientation;
import com.mommoo.flat.layout.linear.constraints.LinearConstraints;
import com.mommoo.flat.layout.linear.constraints.LinearSpace;
import com.newSystem.Settings;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TrackDialog extends JDialog {

    private JPanel mainPanel;
    private DialogDefaultPanel upperPanel;
    private JScrollPane underPanel;
    private static DefaultTableModel trackTableModel;
    private JTable trackTable;

    public TrackDialog() {
        setTitle("Tracking Product");
        setLocation(200, 200);
        setSize(500, 500);
        // Icon 설정
        setIconImage(Settings.icon);

        mainPanel = new JPanel();
        mainPanel.setLayout(new LinearLayout(Orientation.VERTICAL, 0));

        upperPanel = new DialogDefaultPanel(2, 20, DialogDefaultPanel.DIALOG.TRACK);
        upperPanel.makeNonEmptyLine("Product ID", null, true);
        upperPanel.makeTrackButtonLine();

        String[] col = {"No", "Time", "Receiver"};
        trackTableModel = new DefaultTableModel(col, 0);
        trackTable = new JTable(trackTableModel);
        trackTable.getColumnModel().getColumn(0).setPreferredWidth(10);
        trackTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        trackTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        underPanel = new JScrollPane(trackTable);
        mainPanel.add(upperPanel, new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
        mainPanel.add(underPanel, new LinearConstraints().setWeight(5).setLinearSpace(LinearSpace.MATCH_PARENT));

        add(mainPanel);
        setVisible(true);
    }
    public static DefaultTableModel getTrackTableModel() {
        return trackTableModel;
    }
}

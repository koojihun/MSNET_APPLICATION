package com.newSystem.Dialogs;

import com.mommoo.flat.layout.linear.LinearLayout;
import com.mommoo.flat.layout.linear.Orientation;
import com.mommoo.flat.layout.linear.constraints.LinearConstraints;
import com.mommoo.flat.layout.linear.constraints.LinearSpace;

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
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("icon.png")));

        mainPanel = new JPanel();
        mainPanel.setLayout(new LinearLayout(Orientation.VERTICAL));

        upperPanel = new DialogDefaultPanel(2, 10, DialogDefaultPanel.DIALOG.TRACK);
        upperPanel.makeNonEmptyLine("Product ID", null, true);
        upperPanel.makeTrackButtonLine();

        String[] col = {"No", "Time of receipt", "Sender"};
        trackTableModel = new DefaultTableModel(col, 0);
        trackTable = new JTable(trackTableModel);
        trackTable.getColumnModel().getColumn(0).setPreferredWidth(10);
        trackTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        trackTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        trackTable.getTableHeader().setBackground(Color.WHITE);
        trackTable.setBackground(Color.WHITE);
        underPanel = new JScrollPane(trackTable);
        underPanel.getViewport().setBackground(Color.WHITE);

        mainPanel.add(upperPanel, new LinearConstraints().setWeight(2).setLinearSpace(LinearSpace.MATCH_PARENT));
        mainPanel.add(underPanel, new LinearConstraints().setWeight(10).setLinearSpace(LinearSpace.MATCH_PARENT));

        add(mainPanel);
        setVisible(true);
    }
    public static DefaultTableModel getTrackTableModel() {
        return trackTableModel;
    }
}

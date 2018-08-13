package com.newSystem.Dialogs;

import com.mommoo.flat.layout.linear.LinearLayout;
import com.mommoo.flat.layout.linear.Orientation;
import com.mommoo.flat.layout.linear.constraints.LinearConstraints;
import com.mommoo.flat.layout.linear.constraints.LinearSpace;
import com.newSystem.MainFrame;
import com.newSystem.MidPanel;
import com.newSystem.Settings;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class AddressViewDialog extends JDialog {
    private DefaultTableModel savedAddressTableModel;
    private JTable savedAddressTable;
    private JButton okBtn;
    private JButton cancelBtn;
    String ids;
    boolean many;
    public AddressViewDialog(String products, boolean isMany) {
        // Icon 설정
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("icon.png")));
        setTitle("Select Address to send");
        ids = products;
        many = isMany;
        JPanel mainPanel = new JPanel(new LinearLayout(Orientation.VERTICAL, 10));
        setSize(550, 800);
        setLocation(200, 200);
        add(mainPanel);
        ////////////////////////////////////////////////////////////////////////////////////////////////////
        String[] col = {"Name", "Address"};
        savedAddressTableModel = new DefaultTableModel(col, 0);
        savedAddressTable = new JTable(savedAddressTableModel);
        savedAddressTable.getTableHeader().setBackground(Color.WHITE);
        savedAddressTable.setRowHeight(30);
        savedAddressTable.getTableHeader().setFont(Settings.Font16);
        savedAddressTable.setFont(Settings.Font12);
        //////////////////////////////////////////////////////////////////////////////////////////////////////
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        savedAddressTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        savedAddressTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        JScrollPane scrollPane = new JScrollPane(savedAddressTable);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        if(AddressDialog.isThereAddressTxt()) {
            ArrayList<String> names_addresses = AddressDialog.readNameAndAddressFromFile();
            for (int cnt = 0; cnt < names_addresses.size(); cnt += 2) {
                String[] row = {names_addresses.get(cnt), names_addresses.get(cnt + 1)};
                savedAddressTableModel.addRow(row);
            }
        }
        mainPanel.add(scrollPane, new LinearConstraints().setWeight(15).setLinearSpace(LinearSpace.MATCH_PARENT));
        JPanel targetLine = new JPanel(new LinearLayout(Orientation.HORIZONTAL, 10));
        targetLine.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); //상하좌우 10씩 띄우기 //!!!
        targetLine.add(okBtn = new JButton("OK"), new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
        okBtn.setFont(Settings.Font12);
        okBtn.setFocusPainted(false);
        okBtn.addActionListener(new ClickListener());
        targetLine.add(cancelBtn = new JButton("CANCEL"), new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
        cancelBtn.setFont(Settings.Font12);
        cancelBtn.setFocusPainted(false);
        cancelBtn.addActionListener(new ClickListener());
        mainPanel.add(targetLine, new LinearConstraints().setWeight(2).setLinearSpace(LinearSpace.MATCH_PARENT));
        setVisible(true);
    }
    private class ClickListener implements ActionListener {
        ClickListener() {}
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton clicked = (JButton) e.getSource();
            if (clicked == okBtn) {
                if (savedAddressTable.getSelectedRow() == -1)
                    return;
                String address = savedAddressTable.getValueAt(savedAddressTable.getSelectedRow(), 1).toString();

                if (many)
                    MainFrame.bitcoinJSONRPCClient.send_many(address, ids);
                else
                    MainFrame.bitcoinJSONRPCClient.send_to_address(address, ids);
                MidPanel.getCurrentProducts();
                SwingUtilities.getWindowAncestor(clicked).dispose();
            } else if (clicked == cancelBtn) {
                SwingUtilities.getWindowAncestor(clicked).dispose();
            }
        }
    }
}

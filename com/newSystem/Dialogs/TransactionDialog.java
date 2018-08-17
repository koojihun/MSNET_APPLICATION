package com.newSystem.Dialogs;

import com.bitcoinClient.javabitcoindrpcclient.BitcoindRpcClient;
import com.newSystem.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class TransactionDialog extends JDialog {
    public TransactionDialog(String txID) {
        // Icon 설정
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("icon.png")));
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////
        BitcoindRpcClient.Transaction transaction = MainFrame.bitcoinJSONRPCClient.get_transaction(txID);
        setLocation(200, 200);
        setSize(500, 500);
        DialogDefaultPanel panel = new DialogDefaultPanel(12, 10, DialogDefaultPanel.DIALOG.TXINFO);
        add(panel);
        setVisible(true);
        /*

        java.util.List<Map> maps = transaction.details();
        Map send = maps.get(0);
        Map receive = maps.get(1);
        String send_address = (String) send.get("address");
        String receive_address = (String) receive.get("address");
        Map item = (Map) send.get("product sent");
        */
        setVisible(true);
    }
}

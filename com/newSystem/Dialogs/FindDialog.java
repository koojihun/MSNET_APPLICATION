package com.newSystem.Dialogs;

import javax.swing.*;
import java.awt.*;

public class FindDialog extends JDialog {
    static private String isFind;
    private DialogDefaultPanel panel;

    public FindDialog() {
        setTitle("Find Product");
        setLocation(200, 200);
        setSize(500, 300);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("icon.png")));

        panel = new DialogDefaultPanel(4, 20, DialogDefaultPanel.DIALOG.FIND);
        panel.makeNonEmptyLine("Product ID ", null, true);
        panel.makeNonEmptyLine("Country Code ", null, true);
        panel.makeNonEmptyLine("Zip Code ", null, true);
        panel.makeFindButtonLine();
        add(panel);
        setVisible(true);
    }
}

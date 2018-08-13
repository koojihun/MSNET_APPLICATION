package com.newSystem;

import com.mommoo.flat.layout.linear.constraints.LinearConstraints;
import com.mommoo.flat.layout.linear.constraints.LinearSpace;
import com.newSystem.Dialogs.*;
import com.newSystem.Dialogs.AddDialog;
import com.newSystem.Dialogs.InfoDialog;
import com.newSystem.Dialogs.MiningDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

class TopPanelButtons {
    private JButton addBtn;             //  상품, 주소 추가
    private JButton stockBtn;           //  재고 표시
    private JButton searchBtn;          //  검색
    private JButton trackBtn;           //  삼품 유통 경로 추적 (track_product)
    private JButton informationBtn;     //  정보 표시 (getinfo, getpeerinfo)
    private JButton miningBtn;          //  블록 마이닝 (set_generate, auto_set_generate)
    private JButton importBtn;          //  import된 address 표시 (importaddress)
    private JButton addressBtn;         //  저장한 address를 표시
    private Vector<JButton> buttons;
    private MouseTracker mouseTracker;
    private ClickListener clickListener;
    public JPanel parent_panel;

    TopPanelButtons(JPanel parent) {
        parent_panel = parent;
        mouseTracker = new MouseTracker();
        clickListener = new ClickListener();
        buttons = new Vector<JButton>();

        // For Image load (get the path of the image files)
        // First, get the path of the class file
        // Second, and then add the "Font/img.png"
        ClassLoader classLoader = getClass().getClassLoader();
        buttons.add(addBtn = new JButton("ADD", new ImageIcon(classLoader.getResource("btn_plus.png"))));
        buttons.add(stockBtn = new JButton( "STOCK", new ImageIcon(classLoader.getResource("btn_stock.png"))));
        buttons.add(searchBtn = new JButton("FIND", new ImageIcon(classLoader.getResource("btn_search.png"))));
        buttons.add(trackBtn = new JButton("TRACKING", new ImageIcon(classLoader.getResource("btn_track.png"))));
        buttons.add(informationBtn = new JButton("INFO", new ImageIcon(classLoader.getResource("btn_info.png"))));
        buttons.add(miningBtn = new JButton("MINING", new ImageIcon(classLoader.getResource("btn_mining.png"))));
        buttons.add(importBtn = new JButton("IMPORT", new ImageIcon(classLoader.getResource("btn_import.png"))));
        buttons.add(addressBtn = new JButton("ADDRESS", new ImageIcon(classLoader.getResource("btn_address.png"))));
    }
    void addBtns() {
        LinearConstraints linearConstraints = new LinearConstraints().setLinearSpace(LinearSpace.MATCH_PARENT);
        for (int cnt = 0; cnt < buttons.size(); cnt++) {
            JButton tmp = buttons.get(cnt);
            parent_panel.add(tmp, linearConstraints);
            tmp.addMouseListener(mouseTracker);
            tmp.addActionListener(clickListener);
        }
        setAllBtnsBorder(false);
        setAllBtnsSize(70, 80);
        setAllBtnsFocusPainted(false);
        setAllBtnsWhite();
        setAllBtnsFont();
    }
    void setAllBtnsFont() {
        for (int cnt = 0; cnt < buttons.size(); cnt++) {
            buttons.get(cnt).setFont(Settings.Font12);
            buttons.get(cnt).setIconTextGap(6);
            buttons.get(cnt).setVerticalTextPosition(SwingConstants.BOTTOM);
            buttons.get(cnt).setHorizontalTextPosition(SwingConstants.CENTER);
        }
    }
    void setAllBtnsBorder(boolean b) {
        if (!b) {
            for (int cnt = 0; cnt < buttons.size(); cnt++)
                buttons.get(cnt).setBorderPainted(false);
        }
    }
    void setAllBtnsSize(int x, int y) {
        Dimension tmp = new Dimension(x, y);
        for (int cnt = 0; cnt < buttons.size(); cnt++)
            buttons.get(cnt).setPreferredSize(tmp);
        // tracking 버튼과 mining 버튼, address 버튼은 정해진 size로 만들 경우, 텍스트가 잘림
        // 그래서 사이즈 조정.
        trackBtn.setPreferredSize(new Dimension(92, 80));
        miningBtn.setPreferredSize(new Dimension(80, 80));
        importBtn.setPreferredSize(new Dimension(85, 80));
        addressBtn.setPreferredSize(new Dimension(85, 80));
    }
    void setAllBtnsFocusPainted(boolean b) {
        if (!b) {
            for (int cnt = 0; cnt < buttons.size(); cnt++)
                buttons.get(cnt).setFocusPainted(false);
        }
    }
    void setAllBtnsWhite() {
        for (int cnt = 0; cnt < buttons.size(); cnt++)
            buttons.get(cnt).setBackground(Color.WHITE);;
    }
    ///////////////////////////////////////////////////////////////////////
    class ClickListener implements ActionListener {
        ClickListener() {}
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton clicked = (JButton) e.getSource();
            if (clicked == addBtn)
                new AddDialog();
            else if (clicked == stockBtn)
                MidPanel.getCurrentProducts();
            else if (clicked == informationBtn)
                new InfoDialog();
            else if (clicked == miningBtn)
                new MiningDialog();
            else  if (clicked == searchBtn)
                new FindDialog();
            else if (clicked == trackBtn)
                new TrackDialog();
            else if (clicked == importBtn)
                new ImportDialog();
            else if (clicked == addressBtn)
                new AddressDialog();
        }
    }
    ///////////////////////////////////////////////////////////////////////
    class MouseTracker implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) { }
        @Override
        public void mousePressed(MouseEvent e) { }
        @Override
        public void mouseReleased(MouseEvent e) { }
        @Override//마우스가 버튼 안으로 들어오면 파란색으로 바뀜
        public void mouseEntered(MouseEvent e) {
            JButton b = (JButton)e.getSource();
            b.setBackground(new Color(210, 233, 253));
        }
        @Override//마우스가 버튼 밖으로 나가면 흰색으로 바뀜
        public void mouseExited(MouseEvent e) {
            JButton b = (JButton)e.getSource();
            b.setBackground(Color.WHITE);
        }
    }
}


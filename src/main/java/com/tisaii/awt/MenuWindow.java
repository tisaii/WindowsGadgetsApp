package com.tisaii.awt;

import com.tisaii.awt.frame.QueryIpFrame;
import com.tisaii.awt.frame.QueryPortFrame;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static com.tisaii.util.CustomFont.BUTTON_DATA_FONT;

/**
 * @author Tian
 * 菜单窗口, 用于展示所有按钮
 */
public class MenuWindow {

    public static Frame frame;

    public static void openMenuFrame() {
        frame = new Frame();
        frame.setBounds(1200, 400, 250, 300);
        frame.setTitle("Menu");
        frame.setLayout(null);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        Button queryPortButton = new Button("查询端口占用");
        Button queryIpButton = new Button("查询IP");
        queryPortButton.setBounds(25, 70, 200, 50);
        queryPortButton.setFont(BUTTON_DATA_FONT);
        queryIpButton.setBounds(25, 150, 200, 50);
        queryIpButton.setFont(BUTTON_DATA_FONT);
        frame.add(queryPortButton);
        frame.add(queryIpButton);

        queryPortButton.addActionListener(e -> {
            QueryPortFrame.openResultWindow();
            frame.setVisible(false);
        });

        queryIpButton.addActionListener(e -> {
            QueryIpFrame.openIpFrame();
            frame.setVisible(false);
        });
    }


}

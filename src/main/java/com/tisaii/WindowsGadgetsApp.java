package com.tisaii;

import com.tisaii.awt.MenuWindow;

import java.awt.*;

/**
 * @author Tian
 * 主类
 * 打开菜单窗口
 */
public class WindowsGadgetsApp {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                MenuWindow.openMenuFrame();
                MenuWindow.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}

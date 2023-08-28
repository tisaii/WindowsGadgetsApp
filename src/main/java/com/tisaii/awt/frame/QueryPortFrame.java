package com.tisaii.awt.frame;

import com.tisaii.awt.MenuWindow;
import com.tisaii.util.SystemCommandUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.tisaii.util.CustomFont.*;

/**
 * @author Tian
 * 查询端口占用的框架窗口
 */
public class QueryPortFrame {
    public static Frame resultFrame;

    public static void openResultWindow() {
        resultFrame = new Frame();
        resultFrame.setLayout(null);
        resultFrame.setBounds(1200, 400, 250, 300);
        resultFrame.setTitle("端口");

        TextField textField = new TextField();
        textField.setBounds(25, 60, 200, 40);
        textField.setFont(TEXT_FILED_FONT);
        resultFrame.add(textField);

        Button queryPortButton = new Button("查询端口占用");
        queryPortButton.setBounds(25, 110, 200, 50);
        queryPortButton.setFont(BUTTON_DATA_FONT);
        resultFrame.add(queryPortButton);

        queryPortButton.addActionListener(e -> {
            String inputText = textField.getText();
            String result = checkPort(inputText);
            showResultDialog(result,inputText);
            resultFrame.setVisible(false);
        });

        resultFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                resultFrame.dispose();
                MenuWindow.frame.setVisible(true);
            }
        });

        resultFrame.setVisible(true);
    }

    private static String checkPort(String port) {
        String command = "netstat -ano";
        boolean isEmpty = port == null || port.isEmpty();
        if(!isEmpty){
            command= command+" | findstr \""+port+"\"";
        }
        String result = SystemCommandUtil.executeCommand(command);
        if(isEmpty){
            result = result.substring(result.indexOf("PID")+5);
        }
        return result;
    }

    private static void showResultDialog(String result, String port) {
        JFrame jFrame = new JFrame("表格");
        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                jFrame.dispose();
                resultFrame.setVisible(true);
            }
        });
        jFrame.setBounds(800,400,1000,700);
        String[] columnNames = {"协议", "本机地址", "外部地址", "状态", "PID"};
        String[][] data = parsePortQueryResult(result);

        DefaultTableModel defaultTableModel = new DefaultTableModel(data, columnNames);
        JTable table = new JTable(defaultTableModel);
        table.setRowHeight(30);
        table.setFont(ROW_DATA_FONT);
        //设置列名字体
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setFont(COLUMN_NAME_FONT);
        JScrollPane scrollPane = new JScrollPane(table);

        jFrame.add(scrollPane);
        jFrame.setVisible(true);

        // 菜单逻辑
        // 创建右键菜单
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem killMenuItem = new JMenuItem("Kill");
        killMenuItem.setSize(50,20);
        killMenuItem.setFont(MENU_DATA_FONT);

        killMenuItem.addActionListener(e -> {
            // 在这里执行 Kill 操作
            // 获取选中的行和相关数据，然后执行 Kill 操作
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                // 执行 Kill 操作，使用 rowData 中的数据
                boolean executeR = executeKillOperation(table.getValueAt(selectedRow, 4).toString());
                if(executeR){
                    defaultTableModel.setDataVector(parsePortQueryResult(checkPort(port)),columnNames);
                    defaultTableModel.fireTableDataChanged();
                }
            }
        });

        popupMenu.add(killMenuItem);

// 为表格添加鼠标右键菜单
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger() && table.getSelectedRow() >= 0) {
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

    }
    /**
     * 执行 Kill 操作的方法
     */
    private static boolean executeKillOperation(String pid) {
        // 在这里实现 Kill 操作的逻辑
        String result = SystemCommandUtil.executeCommand("taskkill /f /t /pid \"" + pid + "\"");
        return result.startsWith("成功");
    }

    public static String[][] parsePortQueryResult(String result){
        if(result == null || result.isEmpty()){
            return new String[1][5];
        }
        String[] firstParseLineData = result.split("\r\n");
        String[][] parseData = new String[firstParseLineData.length][5];
        for (int i = 0; i < firstParseLineData.length; i++) {
            List<String> list = new ArrayList<>(Arrays.asList(firstParseLineData[i].trim().split("\\s+")));
            if(list.size()==4){
                list.add(3,"-");
            }
            parseData[i]=list.toArray(new String[5]);
        }
        return parseData;
    }
}

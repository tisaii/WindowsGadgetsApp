package com.tisaii.awt.frame;

import com.tisaii.awt.MenuWindow;
import com.tisaii.util.SystemCommandUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static com.tisaii.util.CustomFont.COLUMN_NAME_FONT;
import static com.tisaii.util.CustomFont.ROW_DATA_FONT;

/**
 * @author Tian
 * 查询IP 框架界面
 */
public class QueryIpFrame {
    public static JFrame IPFrame;

    public static final String UNCONNECTED = "已断开";
    public static final String CONNECTED = "已连接";

    public static void openIpFrame(){
        IPFrame=new JFrame("IP Address");
        IPFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                IPFrame.dispose();
                MenuWindow.frame.setVisible(true);
            }
        });
        IPFrame.setBounds(800,400,1200,700);
        String[] columnNames = {"名称", "IPv4地址", "Ipv6地址", "状态"};
        String[][] data = parseIpData();
        DefaultTableModel defaultTableModel = new DefaultTableModel(data,columnNames);
        JTable table = new JTable(defaultTableModel);
        //依据连接状态设置行颜色
        CustomIpTableCellRenderer renderer = new CustomIpTableCellRenderer();
        table.setDefaultRenderer(Object.class, renderer);
        //设置行数据字体
        table.setRowHeight(30);
        table.setFont(ROW_DATA_FONT);
        //设置列名字体
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setFont(COLUMN_NAME_FONT);
        JScrollPane scrollPane = new JScrollPane(table);

        IPFrame.add(scrollPane);
        IPFrame.setVisible(true);

        IPFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                IPFrame.dispose();
                MenuWindow.frame.setVisible(true);
            }
        });
    }

    private static String[][] parseIpData() {
        String result = SystemCommandUtil.executeCommand("ipconfig");
        if(result.isEmpty()){
            return new String[1][4];
        }
        String[] split = result.split("\r\n\r\n");
        int j=0;
        String[][] parseData = new String[(split.length-1)/2][4];
        //第一条数据(索引为0)为Windows IP 配置, 无用数据, 直接跳过
        for (int i = 1; i < split.length; i++) {
            if(i%2!=0){
                //奇数索引为对应网卡名, 去掉前后空格后再将末尾":"去掉
                parseData[j][0] = split[i].trim().replace(":","");
            }else{
                //偶数索引为对应网卡IP属性
                String originalProperty = split[i].replace(" ","");
                //ipv4
                int ipv4 = originalProperty.indexOf("IPv4地址");
                if(ipv4==-1){
                    parseData[j][1] = "-";
                }else{
                    parseData[j][1] = originalProperty.substring(
                            originalProperty.indexOf(":",ipv4)+1,
                            originalProperty.indexOf("\r\n",ipv4)
                    );
                }
                //ipv6
                int ipv6 = originalProperty.indexOf("本地链接IPv6地址");
                if(ipv6==-1){
                    parseData[j][2] = "-";
                }else{
                    parseData[j][2] = originalProperty.substring(
                            originalProperty.indexOf(":",ipv6)+1,
                            originalProperty.indexOf("\r\n",ipv6)
                    );
                }
                //状态
                int status = originalProperty.indexOf("媒体状态");
                if(status==-1){
                    parseData[j][3] = CONNECTED;
                }else {
                    parseData[j][3] = UNCONNECTED;
                }
                //处理完后将二维数组下标j+1
                j = j + 1;
            }
        }
        return parseData;
    }

    static class CustomIpTableCellRenderer extends DefaultTableCellRenderer{
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component rendererComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            String status = table.getValueAt(row, 3).toString();
            if(status==null){
                rendererComponent.setBackground(Color.gray);
            }else if(CONNECTED.equals(status)){
                rendererComponent.setBackground(Color.GREEN);
            }else if(UNCONNECTED.equals(status)){
                rendererComponent.setBackground(Color.ORANGE);
            }else{
                rendererComponent.setBackground(Color.gray);
            }
            return rendererComponent;
        }
    }
}

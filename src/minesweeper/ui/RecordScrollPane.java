package minesweeper.ui;

import java.io.File;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import minesweeper.bean.MinesweeperRecords;
import minesweeper.bean.Player;
  
public class RecordScrollPane{  
  

  
    public JScrollPane getReadScrollPane(MinesweeperRecords records,  
            File recordFile) {  
        Object[][] data = new Object[records.getNumberInRecord()][3];  
        for (int i = 0; i < records.getNumberInRecord(); i++) {  
            Player record = records.getRecords()[i];  
            data[i][0] = String.valueOf(i + 1);  
            data[i][1] = record.getName();  
            data[i][2] = String.valueOf(record.getScore());  
        }  
        Object[] columnNames = new Object[3];  
        columnNames[0] = "ID";  
        columnNames[1] = "Name";  
        columnNames[2] = "Score";  
        JTable table = new JTable(data, columnNames);  
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);  
        JScrollPane pane = new JScrollPane(table);  
        return pane;  
    }  
}  
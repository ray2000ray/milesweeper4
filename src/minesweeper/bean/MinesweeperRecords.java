package minesweeper.bean;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import minesweeper.handlers.RecordComparator;
  
  
/** 
 * @author  
 
 * @desc minesweeper Rank list 
 */  
public class MinesweeperRecords implements Serializable {  
  
    private static final long serialVersionUID = 4513299590910220441L;  
  
    private static final int TOP_TEN = 10;  
  
    private Player[] records = null;  
  
    private int numberInRecord = 0; // the records in rank list  
  
    public MinesweeperRecords() {  
        records = new Player[TOP_TEN];  
    }  
  
    //@SuppressWarnings("unchecked")  
    public List<Player> sortRecords() {
    	List<Player> r = Arrays.asList(getAvailableRecords());
        Collections.sort(r,new RecordComparator());
        										//new RecordComparator());
        
        for(int i=0;i<getAvailableRecords().length;i++){
        	
        	System.out.println("getAvailableRecords["+i+"] Name:"+ r.get(i).getName()+"--score"+r.get(i).getScore());
        	
        }
        
        return r;
    }  
      
    private Player[] getAvailableRecords(){  
    	Player[] availableRecords = new Player[numberInRecord];  
        for(int i=0;i<numberInRecord;i++){  
            availableRecords[i] = new Player(records[i].getName(),records[i].getScore()); 
            //System.out.println("records["+i+"] "+records[i].getName()+"---"+ records[i].getScore()) ;
        }  
        return availableRecords;  
    }  
  
    /** 
     *  
     * @return 
     */  
    public Player getLastAvailableRecord() {  
        return isEmpty() ? null : records[numberInRecord - 1];  
    }  
  
    /** 
     *  
     * @param record 
     */  
    public void addRecordToTopTen(Player record) {  
        if (isEmpty()) {  
            records[0] = record;  
            numberInRecord++;  
            return;  
        }  
  
        if (isFull()) {  
            if (records[TOP_TEN - 1].getScore() < record.getScore()) {
            	System.out.println("topTen-1:"+records[TOP_TEN - 1].getScore()+"---newrecord:"+record.getScore());
                records[TOP_TEN - 1] = record;  
                sortRecords();  
                return;  
            }  
        }  
  
        records[numberInRecord] = record; 
        System.out.println("insert record into records["+numberInRecord+"]" );
        numberInRecord++;  
        sortRecords();  
    }  
  
    /** 
     *  
     * @return 
     */  
    public boolean isEmpty() {  
        return 0 == numberInRecord;  
    }  
  
    /** 
     *  
     * @return 
     */  
    public boolean isFull() {  
        return TOP_TEN == numberInRecord;  
    }  
  
    /** 
     * @return the numberInRecord 
     */  
    public int getNumberInRecord() {  
        return numberInRecord;  
    }  
  
    /** 
     * @param numberInRecord 
     *            the numberInRecord to set 
     */  
    public void setNumberInRecord(int numberInRecord) {  
        this.numberInRecord = numberInRecord;  
    }  
  
    /** 
     * @return the records 
     */  
    public Player[] getRecords() {  
        return records;  
    }  
  
    /** 
     * @param records 
     *            the records to set 
     */  
    public void setRecords(Player[] records) {  
        this.records = records;  
    }  
          	
   
  
}  
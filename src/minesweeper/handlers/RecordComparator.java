package minesweeper.handlers;

import java.util.Comparator;

import minesweeper.bean.Player;

public class RecordComparator implements Comparator<Player> {  
    
    public int compare(Player r1, Player r2) { 
    	 
    	System.out.println("compare object1 & object2");
        return (0 == compareScore(r1, r2)) ? compareName(r1, r2)  
                : compareScore(r1, r2);  
    }  

    private int compareScore(Player r1, Player r2) {  
    	System.out.println("compare score1 & score2: "+ r1.getScore()+"-----"+ r2.getScore());
    	if(r1.getScore()-r2.getScore()<0){
    		System.out.println("return:"+1);
        return 1;}
    	if(r1.getScore()-r2.getScore()>0){
    		System.out.println("return:"+-1);
    		return -1;}
    	
    	else
    		{
    		System.out.println("return:"+0);
    		return 0;
    		}
    		}
    	  

    private int compareName(Player r1, Player r2) {  
    	System.out.println("compare Name1 & Name2: "+ r1.getName().compareTo(r2.getName()));
        return r1.getName().compareTo(r2.getName());  
    }
}
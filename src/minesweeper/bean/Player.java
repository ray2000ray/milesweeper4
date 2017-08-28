package minesweeper.bean;

import java.io.Serializable;

public class Player implements Serializable, Comparable<Object> {
	
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 2845000191607495749L;
	//private int id;
	private String name;
	private int score;
	private int lives;
			

	public Player() {}


	public Player(int lives) {
		this.lives = lives;
	}
	
	public Player(String name, int score) {
		super();
		this.name = name;
		this.score = score;
	}
	
	
	public Player(int id, String name, int score, int lives) {
		super();
		//this.id = id;
		this.name = name;
		this.score = score;
		this.lives = lives;
	}


	public int getLives() {
		return lives;
	}
	public void setLives(int lives) {
		this.lives = lives;
	}
	/*public int getId() {
		return id;
	}*/
	/*public void setId(int id) {
		this.id = id;
	}*/
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}


	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		Player p = (Player)o;
		System.out.println("p:"+p.getScore()+" -- this:"+this.getScore());
		return this.getScore()-p.getScore();
	}

	
	
	

}

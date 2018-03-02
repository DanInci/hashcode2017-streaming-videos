package model;

public class Video {
	private int id;
	private int size;
	private boolean added = false;
	
	public Video(int id, int size) {
		this.id = id;
		this.size = size;
	}
	
	public int getId() {
		return id;
	}
	
	public int getSize() {
		return size;
	}
	
	public void setAdded(Boolean added) {
		this.added = added;
	}
	
	public Boolean hasBeenAddedBefore() {
		return added;
	}
	
	@Override
	public String toString() {
		return id + "";
	}
}

package model;

import java.util.ArrayList;
import java.util.List;

public class Cache {
	private int id;
	private int remainingCapacity;
	private List<Video> storedVideos = new ArrayList<Video>();
	private List<Connection> connectedEndpoints = new ArrayList<Connection>();
	
	public Cache(int id, int capacity) {
		this.id = id;
		this.remainingCapacity = capacity;
	}
	
	public boolean addVideo(Video video) {
		if(remainingCapacity > video.getSize()) {
			storedVideos.add(video);
			remainingCapacity -= video.getSize();
			return true;
		}
		return false;
	}
	
	public boolean hasVideo(Video video) {
		return storedVideos.contains(video);
	}
	
	public int getId() {
		return id;
	}
	
	public void addEndpointConnection(Connection connection) {
		this.connectedEndpoints.add(connection);
	}
	
	public List<Connection> getConnectedEndpoints() {
		return connectedEndpoints;
	}
	
	@Override
	public String toString() {
		String desc = id + " ";
		for(Video video : storedVideos) {
			desc += video + " ";
		}
		return desc;
	}
}

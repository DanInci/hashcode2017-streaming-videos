package model;

import java.util.ArrayList;
import java.util.List;

public class Endpoint {
	private int id;
	private int latencyToDataCenter;
	private List<Connection> connectionsToCache = new ArrayList<Connection>();
	private List<RequestDescription> requests = new ArrayList<RequestDescription>();
	
	public Endpoint(int id, int latencyToDataCenter) {
		this.id = id;
		this.latencyToDataCenter = latencyToDataCenter;
	}
	
	public int getId() {
		return id;
	}
	
	public int getLatencyToDataCenter() {
		return latencyToDataCenter;
	}
	
	public void addRequest(RequestDescription requestDescription) {
		this.requests.add(requestDescription);
	}
	
	public void addConnectionToCache(Connection connection) {
		this.connectionsToCache.add(connection);
	}
	
	public List<Connection> getConnectionsToCache() {
		return connectionsToCache;
	}
	
	public List<RequestDescription> getRequests() {
		return requests;
	}
}

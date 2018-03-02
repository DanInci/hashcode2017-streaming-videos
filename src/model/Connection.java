package model;

public class Connection {
	private Cache cache;
	private Endpoint endpoint;
	private int latency;
	
	public Connection(Cache cache, Endpoint endpoint, int latency) {
		this.cache = cache;
		this.endpoint = endpoint;
		this.latency = latency;
	}
	
	public Cache getCache() {
		return cache;
	}
	
	public Endpoint getEndpoint() {
		return endpoint;
	}
	
	public int getLatency() {
		return latency;
	}
}

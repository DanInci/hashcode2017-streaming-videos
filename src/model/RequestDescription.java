package model;

public class RequestDescription {
	private int requestsNo;
	private Endpoint endpoint;
	private Video video;
	
	public RequestDescription(int requestNo, Endpoint endpoint, Video video) {
		this.requestsNo = requestNo;
		this.endpoint = endpoint;
		this.video = video;
	}
	
	public int getRequestsNo() {
		return requestsNo;
	}
	
	public Endpoint getEndpoint() {
		return endpoint;
	}
	
	public Video getVideo() {
		return video;
	}
}

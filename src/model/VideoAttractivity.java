package model;

public class VideoAttractivity {
	private Video video;
	private float attractivity;
	
	public VideoAttractivity(Video video) {
		this.video = video;
		this.attractivity = 0f;
	}
	
	public void addScoreToAttractivity(int score) {
		this.attractivity += score;
	}
	
	public void addScoreToAttractivity(float score) {
		this.attractivity += score;
	}
	
	public void divideAttractivity(int divider) {
		divider = divider == 0 ? 1 : divider;
		this.attractivity = attractivity / divider;
	}
	
	public float getAttractivity() {
		return attractivity;
	}
	
	public Video getVideo() {
		return video;
	}
}

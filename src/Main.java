import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import comparator.AttractivityComparator;
import model.Cache;
import model.Connection;
import model.Endpoint;
import model.RequestDescription;
import model.Video;
import model.VideoAttractivity;

public class Main {
	
	private static int addedVideoPonder = 30;
	
 	private static int videosCount;
	private static int endpointsCount;
	private static int requestCount;
	private static int cacheCount;
	private static int cacheCapacity;
	
	private static Video[] videos;
	private static Cache[] caches;
	private static Endpoint[] endpoints;
	private static RequestDescription[] requests;
	
	private static AttractivityComparator comparator = new AttractivityComparator();

	public static void main(String[] args) throws IOException {
		if(args.length == 0) {
			System.out.println("Give the filename as an argument!");
			System.exit(1);
		}
		readInput(args[0] + ".in");
		optimize();
		System.out.println("Calculated Score: " + processScore());
		writeOutput(args[0] + ".out");
	}
	
	private static void readInput(String fileName) throws IOException {
		File file = new File("input/" + fileName);
		Scanner scanner = new Scanner(file);

        // first line contains configuration
        String line = scanner.nextLine();
        String[] constants = line.split(" ");

        videosCount = Integer.parseInt(constants[0]);
        endpointsCount = Integer.parseInt(constants[1]);
        requestCount = Integer.parseInt(constants[2]);
        cacheCount = Integer.parseInt(constants[3]);
        cacheCapacity = Integer.parseInt(constants[4]);
       
        caches = new Cache[cacheCount];
        for(int i=0;i<cacheCount;i++) {
        	Cache cache = new Cache(i, cacheCapacity);
        	caches[i] = cache;
        }
        
       //read the videos line
       videos = new Video[videosCount];
       for(int i=0; i<videosCount; i++) {
    	   Video video = new Video(i, scanner.nextInt());
    	   videos[i] = video;
       }
       
       endpoints = new Endpoint[endpointsCount];
       for(int i=0;i<endpointsCount; i++) {
    	   Endpoint endpoint = new Endpoint(i, scanner.nextInt());
    	   endpoints[i] = endpoint;
    	   
    	   int numberOfConnectedCaches = scanner.nextInt();
    	   for(int j=0;j<numberOfConnectedCaches;j++) {
    		   int cacheServerId = scanner.nextInt();
    		   int latency = scanner.nextInt();
    		   
    		   Connection connection = new Connection(caches[cacheServerId],endpoint, latency);
    		   caches[cacheServerId].addEndpointConnection(connection);
    		   endpoint.addConnectionToCache(connection);
    	   }
       }
       
       requests = new RequestDescription[requestCount];
       for(int i=0;i<requestCount;i++) {
    	   int videoId = scanner.nextInt();
    	   int endpointId = scanner.nextInt();
    	   int requestsNo = scanner.nextInt();
    	   RequestDescription request = new RequestDescription(requestsNo, endpoints[endpointId], videos[videoId]);
    	   requests[i] = request;
    	   endpoints[endpointId].addRequest(request);
       }
       scanner.close();
       System.out.println("Read completed");
	}
	
	private static void optimize() {
		for(int i=0;i<cacheCount;i++) {
			int sumaPonderilor = 0;
			List<VideoAttractivity> cachesAttractivities = newAttractivityList();
			
			//se calculeaza attractivitatea ponderata a unui video in functie de requesturile lui
			for(Connection conn : caches[i].getConnectedEndpoints()) {
				Endpoint endpoint = conn.getEndpoint();
				int dataCenterLatency = endpoint.getLatencyToDataCenter();
				int latencyToCache = conn.getLatency();
				int pondere = dataCenterLatency - latencyToCache;
				sumaPonderilor += pondere;
				
				int totalRequestNo = 0;
				List<VideoAttractivity> endpointAttractivities = newAttractivityList();
				
				for(RequestDescription request : endpoint.getRequests()) {
					int videoId = request.getVideo().getId();
					int requestNo = request.getRequestsNo();
					endpointAttractivities.get(videoId).addScoreToAttractivity(requestNo);
					totalRequestNo += requestNo;
				}
				
				for(int j=0;j<cachesAttractivities.size();j++) {
					VideoAttractivity  attr = endpointAttractivities.get(j);
					attr.divideAttractivity(totalRequestNo);
					float scorePonderat = pondere * attr.getAttractivity();
					if(attr.getVideo().hasBeenAddedBefore()) {
						scorePonderat = (addedVideoPonder * scorePonderat) / 100;
					}
					cachesAttractivities.get(j).addScoreToAttractivity(scorePonderat);
				}
			}
			
			for(VideoAttractivity attr : cachesAttractivities) {
				attr.divideAttractivity(sumaPonderilor);
			}
			
			
			//after the video attractivities are computed, we add them in reverse order with respect to their attractivity;
			Collections.sort(cachesAttractivities, comparator);
			for(VideoAttractivity videoAttr : cachesAttractivities) {
				if(caches[i].addVideo(videoAttr.getVideo())) {
					videoAttr.getVideo().setAdded(true);
				}
			}
			System.out.println("Finished for cache #" + i);
		}
		System.out.println("Optimization complete");
	}
	
	private static List<VideoAttractivity> newAttractivityList() {
		List<VideoAttractivity> attractivities = new ArrayList<VideoAttractivity>();
		for(int j=0;j<videosCount;j++) {
			VideoAttractivity attr = new VideoAttractivity(videos[j]);
			attractivities.add(attr);
		}
		return attractivities;
	}
	
	private static int processScore() {
		float score = 0f;
		int totalRequestNo = 0;
		for(int i=0;i<requestCount;i++) {
			Endpoint endpoint = requests[i].getEndpoint();
			Video video = requests[i].getVideo();
			int requestsNo = requests[i].getRequestsNo();
			int bestScore = endpoint.getLatencyToDataCenter();
			for(Connection conn : endpoint.getConnectionsToCache()) {
				if(conn.getCache().hasVideo(video)) {
					int cacheScore = conn.getLatency();
					if(cacheScore < bestScore) {
						bestScore = cacheScore;
					}
				}
			}
			score += requestsNo * (endpoint.getLatencyToDataCenter() - bestScore);
			totalRequestNo += requestsNo;
		}
		return Math.round((score/totalRequestNo) * 1000);
	}
	
	private static void writeOutput(String fileName) throws IOException {
		File file = new File("output/" + fileName);
		BufferedWriter bf = new BufferedWriter(new FileWriter(file));
		bf.write(cacheCount + "\n");
		for(int i=0;i<cacheCount;i++) {
			bf.write(caches[i] + "\n");
		}
		bf.close();
		System.out.println("Write completed");
	}

}

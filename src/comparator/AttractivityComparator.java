package comparator;

import java.util.Comparator;

import model.VideoAttractivity;

public class AttractivityComparator implements Comparator<VideoAttractivity>{

	@Override
	public int compare(VideoAttractivity o1, VideoAttractivity o2) {
		return Float.compare(o2.getAttractivity(), o1.getAttractivity());
	}

}

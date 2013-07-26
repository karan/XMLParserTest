public class Info {
	private String name;
	private String mbid;
	private String artist;
	private int playCount;
	private int listeners;

	public Info(String name, String mbid, String artist, int playCount, int listeners) {
		this.name = name;
		this.mbid = mbid;
		this.artist = artist;
		this.playCount = playCount;
		this.listeners = listeners;
	}
	
	public String toString() {
		return name + " by " + artist + " - " + playCount + " -- " + listeners + mbid;
	}
}
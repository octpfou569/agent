package device.agent;

import java.io.Serializable;
import java.util.List;

import device.agent.access.AccessRecord;
import device.agent.barrel.FeedBarrel;
import device.agent.barrel.WaterBarrel;

public class DeviceInfo implements Serializable {
	private String uid;
	private List<FeedBarrel> feedBarrel;
	private List<WaterBarrel> waterBarrel;
	private AccessRecord accessRecord;
	
	public DeviceInfo() {
	}
	
	public DeviceInfo(String uid, List<FeedBarrel> feedBarrel, List<WaterBarrel> waterBarrel,
			AccessRecord accessRecord) {
		this.uid = uid;
		this.feedBarrel = feedBarrel;
		this.waterBarrel = waterBarrel;
		this.accessRecord = accessRecord;
	}
	
	public String getUid() {
		return uid;
	}
	
	public void setUid(String uid) {
		this.uid = uid;
	}
	
	public List<FeedBarrel> getFeedBarrel() {
		return feedBarrel;
	}
	
	public void setFeedBarrel(List<FeedBarrel> feedBarrel) {
		this.feedBarrel = feedBarrel;
	}
	
	public List<WaterBarrel> getWaterBarrel() {
		return waterBarrel;
	}
	
	public void setWaterBarrel(List<WaterBarrel> waterBarrel) {
		this.waterBarrel = waterBarrel;
	}
	
	public AccessRecord getAccessRecord() {
		return accessRecord;
	}
	
	public void setAccessRecord(AccessRecord accessRecord) {
		this.accessRecord = accessRecord;
	}
}
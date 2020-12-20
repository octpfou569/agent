package device.agent.access;

import java.io.Serializable;
import java.time.LocalDateTime;

public class AccessRecord implements Serializable {
	private String uid;
	private String accessTime;
	
	public AccessRecord() {
		super();
	}
	
	public AccessRecord(String uid, String accessTime) {
		super();
		this.uid = uid;
		this.accessTime = accessTime;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getAccessTime() {
		return accessTime;
	}

	public void setAccessTime(String accessTime) {
		this.accessTime = accessTime;
	}
}

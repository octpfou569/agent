package device.agent.barrel;

import java.io.Serializable;

/**
 * @author dbsehdghks45@naver.com
*/
public class FeedBarrel implements Serializable {
    private int no;
    private String facilityNo;
    private int capacity;
    private String status;
    
    public FeedBarrel(String facilityNo, String supplementStatus, int criticalValue, int capacity, String status) {
    	
		this.facilityNo = facilityNo;
		this.capacity = capacity;
		this.status = status;
	}

	public FeedBarrel() {
		super();
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getFacilityNo() {
		return facilityNo;
	}

	public void setFacilityNo(String facilityNo) {
		this.facilityNo = facilityNo;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
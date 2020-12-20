package device.agent.barrel;

import java.io.Serializable;
import java.util.List;

/**
 * @author dbsehdghks45@naver.com
*/
public class WaterBarrel implements Serializable {
	private int no;
    private int facilityNo;
    private int capacity;
    private String status;
    
	public WaterBarrel() {
	}

	public WaterBarrel(int facilityNo, String supplementStatus, int criticalValue, int capacity, String status) {
		
		this.facilityNo = facilityNo;
		this.capacity = capacity;
		this.status = status;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public int getFacilityNo() {
		return facilityNo;
	}

	public void setFacilityNo(int facilityNo) {
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
package device.agent.access;

import java.time.LocalDateTime;

import com.diozero.devices.MFRC522;

public interface AccessService {
	public void sendAccessRecord(MFRC522.UID uid, LocalDateTime time);
	public void waitForTagId(MFRC522 mfrc522);
}

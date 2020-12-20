package device.agent.access;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.tinylog.Logger;

import com.diozero.devices.MFRC522;
import com.diozero.util.Hex;
import com.diozero.util.SleepUtil;

@Repository
public class AccessRepository implements InitializingBean {
	@Value("${device.rfid.spi-controller}")
	private int controller;
	@Value("${device.rfid.chip-select}")
	private int chipSelect;
	@Value("${device.rfid.rst-gpio}")
	private int gpioNo;
	
	private MFRC522 mfrc522 = null;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Logger.info("init mfrc522 sensor");
		
		setMFRC522(controller, chipSelect, gpioNo);
	}
	
	public void setMFRC522(int controller, int chipSelect, int gpioNo) {
		this.mfrc522 = new MFRC522(controller, chipSelect, gpioNo);
	}
	
	public MFRC522 getMFRC522() {
		return mfrc522;
	}
	
	public MFRC522.UID getID(MFRC522 mfrc522) {
		// If a new PICC placed to RFID reader continue
		if (! mfrc522.isNewCardPresent()) {
			return null;
		}
		Logger.debug("A card is present!");
		// Since a PICC placed get Serial and continue
		MFRC522.UID uid = mfrc522.readCardSerial();
		if (uid == null) {
			return null;
		}
		
		// There are Mifare PICCs which have 4 byte or 7 byte UID care if you use 7 byte PICC
		// I think we should assume every PICC as they have 4 byte UID
		// Until we support 7 byte PICCs
		Logger.info("Scanned PICC's UID: {}", Hex.encodeHexString(uid.getUidBytes()));
		
		mfrc522.haltA();
		
		return uid;
	}
}
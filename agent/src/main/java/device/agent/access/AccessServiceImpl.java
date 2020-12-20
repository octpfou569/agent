package device.agent.access;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.tinylog.Logger;

import com.diozero.devices.MFRC522;
import com.diozero.devices.MFRC522.UID;
import com.diozero.util.Hex;
import com.diozero.util.SleepUtil;
import com.google.gson.Gson;

import device.agent.DeviceInfo;
import device.agent.DeviceInfoRepository;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

@Service
public class AccessServiceImpl implements AccessService, CommandLineRunner {
	@Autowired
	private AccessRepository accessRepository;
	@Autowired
	private DeviceInfoRepository deviceInfoRepository;
	@Value("${agent.send.ip}")
	private String serverUrl;
	
	@Override
	public void sendAccessRecord(UID uid, LocalDateTime time) {
		String url = "http://" + serverUrl + "/cat/data";
		
		DeviceInfo deviceInfo = new DeviceInfo();
		deviceInfo.setUid(deviceInfoRepository.getSerialNo());
		
		AccessRecord accessRecord = new AccessRecord();
		accessRecord.setUid(String.valueOf(Hex.encodeHexString(uid.getUidBytes())));
		accessRecord.setAccessTime(time.toString());
		deviceInfo.setAccessRecord(accessRecord);

		String json = new Gson().toJson(deviceInfo);
		
		RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));

		Request request = new Request.Builder().url(url).post(body).build();
		
		Logger.info("접근 정보 : {}", json);
		
		OkHttpClient client = new OkHttpClient();
		try {
			client.newCall(request).execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void waitForTagId(MFRC522 mfrc522) {
		try (mfrc522) {
			// Wait for a card
			Logger.info("태그를 기다리는 중입니다.");
			
			MFRC522.UID pastUid = null;
			MFRC522.UID nowUid = null;
			long lastUidAccessTime = 0;
			while (true) {
				nowUid = accessRepository.getID(mfrc522);
				
				if (nowUid != null) {
					long nowTime = System.currentTimeMillis();
					
					if (pastUid != null && pastUid.equals(nowUid)) {
						long resultTime = (nowTime - lastUidAccessTime) / 1000;
						
						if (resultTime < 30) {
							Logger.info("이미 접근한 uid(고양이), 경과시간 : {}초", resultTime);
							
							SleepUtil.sleepSeconds(1);
							continue;
						}
					}
					Logger.info("새로 접근 확인된 uid(고양이) : {}", nowUid);
					
					sendAccessRecord(nowUid, LocalDateTime.now());
					
					lastUidAccessTime = System.currentTimeMillis();
					pastUid = nowUid;
				}
				
				SleepUtil.sleepSeconds(1);
			}
		}
	}

	@Override
	public void run(String... args) throws Exception {
		waitForTagId(accessRepository.getMFRC522());
	}
}

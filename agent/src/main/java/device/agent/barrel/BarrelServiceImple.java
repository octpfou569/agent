package device.agent.barrel;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.tinylog.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import device.agent.DeviceInfo;
import device.agent.DeviceInfoRepository;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

@Service
public class BarrelServiceImple implements BarrelService {
//	public static final MediaType MEDIA_TYPE_HTML = MediaType.get("tex/html; charset=utf-8");

	@Autowired
	private BarrelRepository barrelRepository;
	@Autowired
	private DeviceInfoRepository deviceInfoRepository;
	@Value("${agent.send.ip}")
	private String serverUrl;
	
	@Override
	@Scheduled(fixedDelay = 5000) // (1000=1초) 밀리 세컨드 단위 호출
	public void sendVolume() {
		String url = "http://" + serverUrl + "/facility/data";
		
		DeviceInfo deviceInfo = new DeviceInfo();
		deviceInfo.setUid(deviceInfoRepository.getSerialNo());
		deviceInfo.setFeedBarrel(barrelRepository.getFeedBarrels());
		deviceInfo.setWaterBarrel(barrelRepository.getWaterBarrels());
		
		String json = toJSON(deviceInfo);
		
		RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));

		Request request = new Request.Builder().url(url).post(body).build();
		
		Logger.info("{} / {}", json.toString(), LocalDateTime.now());
		
		OkHttpClient client = new OkHttpClient();
		try {
			client.newCall(request).execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String toJSON(Object object) {
		return new Gson().toJson(object);
	}
}
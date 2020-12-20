package device.agent.barrel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.tinylog.Logger;

//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;

import okhttp3.MediaType;

@Repository
public class BarrelRepository implements InitializingBean {
	public static final MediaType MEDIA_TYPE_HTML = MediaType.get("tex/html; charset=utf-8");
	@Value("${device.hx.feed.first}")
	private String[] firstFeedConfig;
	@Value("${device.hx.feed.second}")
	private String[] secondFeedConfig;
	@Value("${device.hx.feed.third}")
	private String[] thirdFeedConfig;
	@Value("${device.hx.water.first}")
	private String[] firstWaterConfig;
	@Value("${device.hx.water.second}")
	private String[] secondWaterConfig;
	@Value("${device.hx.water.third}")
	private String[] thirdWaterConfig;

	private List<Hx711> feedBarrels = new ArrayList<Hx711>();
	private List<Hx711> waterBarrels = new ArrayList<Hx711>();

	@Override
	public void afterPropertiesSet() throws Exception {
		Logger.info("hx711 센서들 초기화 시작!!!");
		
		Hx711 temp = null;
		
		System.out.println("사료통 초기화 시작!!!");

		// 사료통 초기화
		for (int i = 0; i < 3; i++) {
			Object[] object = { firstFeedConfig, secondFeedConfig, thirdFeedConfig };
			String[] config = (String[]) object[i];

			temp = new Hx711(Integer.parseInt(config[0]), Integer.parseInt(config[1]), Integer.parseInt(config[2]),
					Double.parseDouble(config[3]));
			if (temp != null) {
				temp.measureAndSetTare();

				if (temp.readValue() != 8388608) {
					feedBarrels.add(temp);
					System.out.println(i + 1 + "번 사료통 연결 상태");
					Logger.info(i + 1 + "번 사료통 정보 : {} / {} / {} / {}", Integer.parseInt(config[0]),
							Integer.parseInt(config[1]), Integer.parseInt(config[2]), Double.parseDouble(config[3]));
				} else {
					feedBarrels.add(null);
				}
			}
		}
		
		System.out.println("사료통 초기화 완료!!!");
		System.out.println("---------------------");
		System.out.println("물통 초기화 시작!!!");
		

		Logger.info("hx711 센서들 초기화 완료!!!");
	}

	public List<FeedBarrel> getFeedBarrels() {
		List<FeedBarrel> list = new ArrayList<FeedBarrel>();

		for (int i = 0; i < feedBarrels.size(); i++) {
			Hx711 feedSensor = feedBarrels.get(i);
			if (feedSensor != null) {
				FeedBarrel feedBarrel = new FeedBarrel();
				feedBarrel.setNo(i + 1);
				feedBarrel.setCapacity(Math.toIntExact(feedSensor.getGramFromValue()));

				// gpio가 연결돼있지 않다면
				if (feedSensor.readValue() == 8388608) {
					feedBarrel.setStatus("O");
				} else {
					feedBarrel.setStatus("X");
				}

				list.add(feedBarrel);
			}
		}

		return list;
	}

	public List<WaterBarrel> getWaterBarrels() {
		List<WaterBarrel> list = new ArrayList<WaterBarrel>();

		for (int i = 0; i < waterBarrels.size(); i++) {
			Hx711 waterSensor = waterBarrels.get(i);
			if (waterSensor != null) {
				WaterBarrel waterBarrel = new WaterBarrel();
				waterBarrel.setNo(i + 1);
				waterBarrel.setCapacity(Math.toIntExact(waterSensor.getGramFromValue()));

				// gpio가 연결돼있지 않다면
				if (waterSensor.readValue() == 8388608) {
					waterBarrel.setStatus("O");
				} else {
					waterBarrel.setStatus("X");
				}

				list.add(waterBarrel);
			}
		}

		return list;
	}
}
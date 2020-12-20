package device.agent;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Repository;
import org.tinylog.Logger;

@Repository
public class DeviceInfoRepository implements InitializingBean {
	public String serialNo;
	
	public String getSerialNo() {
	    return serialNo;
	}
	
	public void setSerialNo() throws Exception {
		String[] command = {
	    		"/bin/sh",
	    		"-c",
		    	"cat /proc/cpuinfo | grep Serial | awk '{print $3}'"
	    };
		    
	    String result;
	    Runtime runtime = Runtime.getRuntime();
	    Process process = runtime.exec(command);
	    BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
	    while ((result = stdInput.readLine()) != null) {
	        Logger.info("디바이스(시설물) 일련번호 : {}", result);
	        this.serialNo = result;
	    }
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Logger.info("디바이스(시설물) 일련번호를 초기화합니다.");
		
		this.setSerialNo();
	}
}
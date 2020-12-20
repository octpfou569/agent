package device.agent.barrel;

import com.pi4j.wiringpi.Gpio;

public class Hx711 {
    private final int pinSCK;
    private final int pinDAT;
    private int gain;
    private final int loadCellMaxWeight;
    private double ratedOutput;
    private long tareOffset;
	
    public Hx711(int pinDAT, int pinSCK, int loadCellMaxWeight,
            double ratedOutput) {
        this.pinSCK = pinSCK;
        this.pinDAT = pinDAT;
        this.loadCellMaxWeight = loadCellMaxWeight;
        this.ratedOutput = ratedOutput;
        this.gain = 24;
        
        Gpio.wiringPiSetupGpio();
		Gpio.pinMode(pinSCK, Gpio.OUTPUT);
		Gpio.pinMode(pinDAT, Gpio.INPUT);
		Gpio.digitalWrite(pinSCK, Gpio.LOW);
		Gpio.digitalWrite(pinDAT, Gpio.HIGH);
    }
    
    public long getGramFromValue() {

        double changeValueToGram = (((readValue() - tareOffset) * 0.5 * loadCellMaxWeight) / ((ratedOutput / 1000) * 128 * 8388608));
        long roundedGram = Math.abs(Math.round(changeValueToGram));
        
        return roundedGram;
    }
    
    public long readValue() {
    	Gpio.digitalWrite(pinSCK, Gpio.LOW);
        while (!isReadyForMeasurement()) {
            sleepSafe(1);
        }

        long count = 0;
        for (int i = 0; i < this.gain; i++) {
        	Gpio.digitalWrite(pinSCK, Gpio.HIGH);
        	
            count = count << 1;
            
            Gpio.digitalWrite(pinSCK, Gpio.LOW);
            
            if (Gpio.digitalRead(pinDAT) == Gpio.HIGH) {
                count++;
            }
        }
        
        Gpio.digitalWrite(pinSCK, Gpio.HIGH);
        
        count = count ^ 0x800000;
        
        Gpio.digitalWrite(pinSCK, Gpio.LOW);
        
//        System.out.println("Read value from sensor: " + count);
        
        return count;
    }
    
    public long measureAndSetTare() {
        long tareValue = readValue();
        this.tareOffset = tareValue;
        
        return tareValue;
    }
    
    public void setTareValue(long tareValue) {
        this.tareOffset = tareValue;
    }

    public boolean isReadyForMeasurement() {
        return (Gpio.digitalRead(pinDAT) == Gpio.LOW);
    }

    private void sleepSafe(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
            System.err.println(ex.getMessage());
        }
    }
}

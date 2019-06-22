package com.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;

@Document(collection = "swinStatus")
public class SwinStatus {
	@Id
	public String id;
	@Indexed(unique = true, direction = IndexDirection.DESCENDING, dropDups = true)
	public String swinIp;
	public String swinName;
	public int rainSensor;
	public int illuSensor;
	public boolean open;
	public boolean blind;
	public String userId;

	public SwinStatus() {}

	public SwinStatus(String id, String swinIp, String swinName, int rainSensor, int illuSensor, boolean open, boolean blind, String userId) {
		this.id = id;
		this.swinIp = swinIp;
		this.swinName = swinName;
		this.rainSensor = rainSensor;
		this.illuSensor = illuSensor;
		this.open = open;
		this.blind = blind;
		this.userId = userId;
	}

	public String getId() { return id; }
	public void setId(String id) { this.id = id; }

	public String getSwinIp() { return swinIp; }
	public void setSwinIp(String swinIp) { this.swinIp = swinIp; }

	public String getSwinName() { return swinName; }
	public void setSwinName(String swinName) { this.swinName = swinName; }

	public int getRainSensor() { return rainSensor; }
	public void setRainSensor(int rainSensor) { this.rainSensor = rainSensor; }

	public int getIlluSensor() { return illuSensor; }
	public void setIlluSensor(int illuSensor) { this.illuSensor = illuSensor; }

	public boolean getOpen() { return open; }
	public void setOpen(boolean open) { this.open = open; }

	public boolean getBlind() { return blind; }
	public void setBlind(boolean blind) { this.blind = blind; }

	public String getUserId() { return userId; }
	public void setUserId(String userId) { this.userId = userId; }
}

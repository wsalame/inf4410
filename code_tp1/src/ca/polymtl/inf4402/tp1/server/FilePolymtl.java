package ca.polymtl.inf4402.tp1.server;

import ca.polymtl.inf4402.tp1.shared.FileInterface;

public class FilePolymtl implements FileInterface{

	private String filename;
	private String clientId;
	private String checksum;
	private byte[] data;
	
	public FilePolymtl(String filename, String clientId, String checksum){
		this.filename = filename;
		this.clientId = clientId;
		this.checksum = checksum;
	}
	
	@Override
	public String getFilename() {
		return filename;
	}

	@Override
	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getChecksum() {
		return checksum;
	}

	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}
	
	public boolean isLocked(){
		return clientId != null;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
}
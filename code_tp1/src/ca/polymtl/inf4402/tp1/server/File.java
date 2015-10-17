package ca.polymtl.inf4402.tp1.server;

import ca.polymtl.inf4402.tp1.shared.FileProxy;

public class File implements FileProxy{

	private String filename;
	private String clientId;
	private String checksum;
	
	public File(String filename, String clientId, String checksum){
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

	public void setFilename(String filename) {
		this.filename = filename;
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
}
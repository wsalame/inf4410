package ca.polymtl.inf4402.tp1.server;

public class FilePoly {

	private String filename;
	private String clientId;
	private String checksum;
	private byte[] data;

	public FilePoly(String filename, String clientId, String checksum, byte[] data) {
		this.filename = filename;
		this.clientId = clientId;
		this.checksum = checksum;
		this.data = data;
	}

	public String getFilename() {
		return filename;
	}

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

	public boolean isLocked() {
		return clientId != null;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
	
	public String getExportData(){
		StringBuilder wannabeJsonBuilder = new StringBuilder();
		wannabeJsonBuilder.append("[").append(getFilename()).append("],").append("[")
    .append(new String(getData())).append("]");
		return wannabeJsonBuilder.toString();
	}
}
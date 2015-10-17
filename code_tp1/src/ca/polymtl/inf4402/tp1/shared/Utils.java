package ca.polymtl.inf4402.tp1.shared;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {

	public static String toMd5(byte[] file) throws NoSuchAlgorithmException{
		byte[] digest = MessageDigest.getInstance("MD5").digest(file);
		StringBuffer sb = new StringBuffer();
		for (byte b : digest) {
			sb.append(String.format("%02x", b & 0xff));
		}
		return sb.toString();
	}
}

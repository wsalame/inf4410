package ca.polymtl.inf4402.tp1.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public interface ServerInterface extends Remote {
	public enum AllowedCommand {
		CREATE("create"), LIST("list"), SYNC_LOCAL_DIR("syncLocalDir"), GET("get"), LOCK("lock"), PUSH("push");

		private String commandName;
		private static Map<String, AllowedCommand> commands = new HashMap<String, ServerInterface.AllowedCommand>();

		static {
			for (AllowedCommand command : AllowedCommand.values()) {
				commands.put(command.commandName.toLowerCase(), command);
			}
		}

		private AllowedCommand(String commandName) {
			this.commandName = commandName;
		}

		public static AllowedCommand valueOfCustom(String commandName) {
			return commands.get(commandName.toLowerCase());
		}
	}

	String generateClientId() throws RemoteException;

	String create(String fileName) throws RemoteException;

	String lock(String filename, String clientId, String checksum) throws RemoteException;

	byte[] get(String filename, String checksum) throws RemoteException;
}

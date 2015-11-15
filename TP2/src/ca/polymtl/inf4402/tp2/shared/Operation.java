package ca.polymtl.inf4402.tp2.shared;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Operation implements Serializable {

	private static final long serialVersionUID = 1L;
	private OperationType operation;
	private int operande;

	public Operation(String operation, String operande) {
		this.operation = OperationType.valueOfCustom(operation);
		this.operande = Integer.parseInt(operande);
	}

	
	private interface HasOperation {
		int execute(int operande);
	}

	public int execute() {
		return operation.execute(operande);
	}
	
	public enum OperationType implements HasOperation {
		FIBONACCI("fib") {
			@Override
			public int execute(int operande) {
				return Calculator.fib(operande);
			}
		},
		PRIME("prime") {
			@Override
			public int execute(int operande) {
				return Calculator.prime(operande);
			}
		};

		private static Map<String, OperationType> operations = new HashMap<String, Operation.OperationType>();

		static {
			for (OperationType operation : OperationType.values()) {
				operations.put(operation.name, operation);
			}
		}

		private String name;

		private OperationType(String name) {
			this.name = name;
		}

		public static OperationType valueOfCustom(String name) {
			return operations.get(name);
		}
	}
}

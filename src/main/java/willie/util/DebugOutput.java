package willie.util;

public class DebugOutput{
	public static Boolean debug = true;
	public static int debugLevel = 3;

	public static void print(String message){
		if(debug){
			System.out.println(message);
		}
	}

	public static void print(int debugLevel, String message){
		if(debug && debugLevel <= DebugOutput.debugLevel){
			System.out.println(message);
		}
	}

	public static void printArray(String prefix, String[] array){
		if(debug){
			System.out.println(prefix + arrayToString(array));
		}
	}

	public static void printArray(int debugLevel, String prefix, String[] array){
		if(debug && debugLevel <= DebugOutput.debugLevel){
			System.out.println(prefix + arrayToString(array));
		}
	}

	public static void printArray(String[] array){
		if(debug){
			System.out.println(arrayToString(array));
		}
	}

	public static void printArray(int debugLevel, String[] array){
		if(debug && debugLevel <= DebugOutput.debugLevel){
			System.out.println(arrayToString(array));
		}
	}

	public static void printError(String message){
		if(debug){
			System.err.println(message);
		}
	}

	public static String arrayToString(String[] array){
		StringBuilder sb = new StringBuilder();
		for(Object o : array){
			sb.append(o.toString()).append(", ");
		}
		return sb.toString();
	}
}

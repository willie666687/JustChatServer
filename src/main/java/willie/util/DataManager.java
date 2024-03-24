package willie.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import willie.impl.Account;

import java.io.*;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.util.Collection;

public class DataManager{
	public static File UserData = new File("Data","UserData.txt");
	public static void createFile(File file){
		try{
			if(file.getParentFile().mkdirs()){
				DebugOutput.print("Directory created: " + file.getName());
			}
			if(file.createNewFile()){
				DebugOutput.print("File created: " + file.getName());
			}else{
				DebugOutput.print("File already exists.");
			}
		}catch(Exception e){
			DebugOutput.printError("create file error: " + file.getName());
			e.printStackTrace();
		}
	}
	public static void writeData(File file, Object data){
		try{
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			FileWriter fileWriter = new FileWriter(file);
			gson.toJson(data, fileWriter);
			fileWriter.close();
			DebugOutput.print("write data success: " + file.getName());
		}catch(Exception e){
			DebugOutput.printError("write data error: " + file.getName());
			e.printStackTrace();
		}
	}
	public static Object readData(File file, Object data){
		try{
			Gson gson = new Gson();
			FileReader fileReader = new FileReader(file);
			data = gson.fromJson(fileReader, data.getClass());
			fileReader.close();
			DebugOutput.print("read data success: " + file.getName());
			return data;
		}catch(Exception e){
			DebugOutput.printError("read data error: " + file.getName());
			e.printStackTrace();
		}
		return null;
	}
}

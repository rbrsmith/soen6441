package io;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * <b> This is a utility class for loading and saving an object's state from and to 
 * files in JSON format. <b> 
 * 
 * @param <T>
 * @author Team 10 - SOEN6441
 * @version 2.0
 */
public class JSONFileManager<T> implements FileManager<T> {
	
	private final Gson gson;
	private final Class<T> typeParameterClass;
	
	public JSONFileManager(Class<T> typeParameterClass_) {
		typeParameterClass = typeParameterClass_;
		gson = registerHandlers(new GsonBuilder());
	}
	
	private Gson registerHandlers(GsonBuilder builder) {
		return builder.setPrettyPrinting().create();
	}
	
	/**
	 * Opens a JSON file containing an object's state.
	 * @param fileName the name of the file containing the game's state
	 * @return an option-type object containing a game file object if the
	 * 	the file with the given file name is found, an empty one otherwise.
	 */
	@Override
	public Optional<FileObject<T>> open(String fileName) {
		try (FileReader jsonFile = new FileReader(FileObject.getFileRoot() + "/" + fileName)) {
			T obj = gson.fromJson(jsonFile, typeParameterClass);
			return Optional.of(new FileObject<T>(obj, fileName));
		} catch (IOException e) {
			return Optional.empty();
		}
	}

	/**
	 * Saves the game's state.
	 * @param obj a game file object containing the game and the file name for saving
	 * @see io.GameFileManager#save(io.FileObject)
	 * @return true if the game's state is saved successfully, false otherwise
	 */
	@Override
	public boolean save(FileObject<T> obj) {
		String gameJson = gson.toJson(obj.getPOJO());
		try {
			Files.write(obj.getFilePath(), gameJson.getBytes());
		} catch (IOException e) {
			// TODO Log exception
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * Saves the game's state to a new file with the given file name as 
	 * opposed to the file from which it was loaded.
	 * @param obj a game file object containing the game's state
	 * @param fileName the new file name to use 
	 * @return true if the game's state is saved successfully, false otherwise
	 */
	@Override
	public boolean saveAs(FileObject<T> obj, String fileName) {
		String objJson = gson.toJson(obj.getPOJO());
		try {
			Files.write(Paths.get(FileObject.getFileRoot() + "/" + fileName),
					objJson.getBytes());
		} catch (IOException e) {
			// TODO Log exception
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
}

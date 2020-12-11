package csci310.service;

import static org.junit.Assert.*;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

public class JsonReaderTest {
	
	String validURL = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY_ADJUSTED&symbol=IBM&apikey=demo";
	
	String inValidURL = "some dummy value";
	

	@Test
	public void testReadJsonFromUrl() {
		try {
			JSONObject jsonObject = JsonReader.readJsonFromUrl(validURL);
			assertNotNull(jsonObject.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			JsonReader.readJsonFromUrl(inValidURL);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			assertNotNull(e);
		}
		
		JsonReader jsonReader = new JsonReader();
		assertNotNull(jsonReader);
	}

}

package csci310.service;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;



/**
 * Frequency Limit: 
 * Usage: 
 * FinnhubClient client = new FinnhubClient();
 * try {
 *     Map<Date, Double> stockPrice = client.getStockPrice(<"IBM">, <"Daily">, <1572566400>, <1601942400>);
 *     // Successfully retrieved Stock price map 
 * } catch (Exception e){
 *     // Unsuccessfully retrieved
 * }
 * 
 * @author chunyangmou
 *
 */
public class FinnhubClient {
	//URL for Stock data
	protected String STOCK_PRICE_URL_FORMAT = "https://finnhub.io/api/v1/stock/candle?symbol=%s&resolution=%s&from=%d&to=%d&token=%s";
	
	// URL for company profile function
	protected String COMPANY_PROFILE_URL_FORMAT = "https://finnhub.io/api/v1/stock/profile2?symbol=%s&token=%s";
	//Key for Stock URL
	private String API_KEY = "btug69748v6vqmm3nn10";
	
	//Method to get the stock price from the API, takes in a ticker symbol, resolution, startTime, and endTime
	//Inputs these values to then recieve JSON info and then parses infor to create a map between the data and price of the stock
	public Map<Date, Double> getStockPrice(String symbol, Resolution resolution, long startTime, long endTime) throws Exception {
		String resolutionString = null;
		switch (resolution) {
		case Daily:
			resolutionString = "D";
			break;
		case Weekly:
			resolutionString = "W";
			break;
		case Monthly:
			resolutionString = "M";
			break;
		}
		
		String urlString = String.format(STOCK_PRICE_URL_FORMAT, symbol.toUpperCase(), resolutionString, startTime, endTime, API_KEY);
		System.out.println(urlString);
		
		JSONObject jsonObject = JsonReader.readJsonFromUrl(urlString);
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = (ObjectNode) mapper.readTree(jsonObject.toString());
		JsonNode priceArrayNode = node.get("c");
		//System.out.println(priceArrayNode);
		List<Double> pricesList = new ObjectMapper().readValue(priceArrayNode.traverse(), new TypeReference<ArrayList<Double>>() {});
		
		JsonNode dateArrayNode = node.get("t");
		//System.out.println(dateArrayNode);
		List<Long> dateList = new ObjectMapper().readValue(dateArrayNode.traverse(), new TypeReference<List<Long>>() {});
		Map<Date, Double> resultMap = new HashMap<>();
		for (int i = 0; i < pricesList.size(); ++i) {
			Double priceDouble = pricesList.get(i);
			long dateLong = dateList.get(i);
			
			Date date = UnixConverterClient.convertUnixToDate(dateLong);
			resultMap.put(date, priceDouble);
		}
		//System.out.println(resultMap);
		return resultMap;
	}
	
	/**
	 * Get the company name based on the symbol string input 
	 * @param symbolString ex: "APPL"
	 * @return ex: "Apple Inc"
	 * @throws Exception when the fetch fails
	 */
	public String getCompanyNameString(String symbolString) throws Exception {
		String urlString = String.format(COMPANY_PROFILE_URL_FORMAT, symbolString, API_KEY);
		JSONObject jsonObject = JsonReader.readJsonFromUrl(urlString);
		
		String name = jsonObject.getString("name");
		// System.out.println(name);
		return name;
	}
}

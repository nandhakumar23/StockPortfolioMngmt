package csci310.service;
//Class that provides resolution for other functions within the class, it only holds static functions inside
public class ResolutionGetter {
	//Static functiom: Returns resolution.monthly
	public static Resolution Month()
	{
		
		return Resolution.Monthly;
	}
	//Static function: Returns resolution.weekly
	public static Resolution Week()
	{
		
		return Resolution.Weekly;
	}
	//Static function: returns resolution.daily
	public static Resolution Day()
	{
		
		return Resolution.Daily;
	}
}

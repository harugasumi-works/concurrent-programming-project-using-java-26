package _StatusCheck;

import tools.jackson.databind.*;
public class JSON_DTO{
	
	private static final ObjectMapper mapper = new ObjectMapper();
	
	public static String convert(Object object) {
		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
	}
	

}

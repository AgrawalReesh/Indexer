package web.search.uncompressor;

import java.io.File;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class PageParser {
	String parsed_string;
	
	PageParser(String file_location){
		File input = new File(file_location);
		try {
			Document doc = Jsoup.parse(input, "UTF-8", "http://example.com/");
			Element content = doc.body();
			parsed_string = content.text();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getString(){
		return parsed_string;
	}
}

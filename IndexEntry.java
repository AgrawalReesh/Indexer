package web.search.uncompressor;

public class IndexEntry {
	String index_url;
	int unknown_1;
	int unknown_2;
	int content_length;
	String addr;
	int port_number;
	String status_code;
	
	IndexEntry(String entry_string){
		String arr[] = entry_string.split(" ");
		index_url = arr[0];
		unknown_1 = Integer.parseInt(arr[1]);
		unknown_2 = Integer.parseInt(arr[2]);
		content_length = Integer.parseInt(arr[3]);
		addr = arr[4];
		port_number = Integer.parseInt(arr[5]);
		status_code = arr[6];
	}

}

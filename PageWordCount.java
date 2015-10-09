package web.search.uncompressor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

public class PageWordCount {
	public static void writeWordCount(String str, int _doc_id){
		
		int doc_id = _doc_id;
		String instring =  str;
		
		String[] words = instring.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
		
		/* Put the list of words in a hashtable to get frequency of each word*/
		//System.out.println(Arrays.toString(words));
		Hashtable<String, Integer> word_freq = new Hashtable<String, Integer>();
		for(int i = 0 ; i < words.length; i++){
			if(word_freq.containsKey(words[i])){
				int value = word_freq.get(words[i]);
				word_freq.put(words[i], ++value);
			}else{
				word_freq.put(words[i], 1);
			}
		}
		
		/* This enumeration is being used to iterate through the hashtable and 
		 * for writing its contents onto the intermediate inverted index file 
		 * which will be sorted in the main using unix sort */
		//System.out.println(word_freq.toString());
		Enumeration<String> entries = word_freq.keys();
			
		/*writing onto the intermediate index file here*/
		try{
			File inter_index = new File("intermediate_non_aggregated.txt");
			if(!inter_index.exists())
			inter_index.createNewFile();
			FileWriter fw = new FileWriter(inter_index.getName(), true);
			BufferedWriter bw = new BufferedWriter(fw);
			
			while(entries.hasMoreElements()){
				String key = entries.nextElement().toString();
				bw.write(key+" "+String.valueOf(doc_id)+" "+word_freq.get(key).toString()+"\n");
			}
			bw.close();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
	}
}

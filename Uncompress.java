package web.search.uncompressor;

import java.io.BufferedReader;
//import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
//import java.util.Collections;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;

public class Uncompress {
	
	public static void main(String args[]) throws IOException{
		try {	
			/* uncompressing a sample tar file */
			TarArchiveInputStream myTar = new TarArchiveInputStream(new FileInputStream(new File("vol_0_99.tar")));
			
			/* get the second entry in the tar file*/
			ArchiveEntry ignore_entry, data_entry;
			ignore_entry = myTar.getNextEntry();
			
			/* while loop comes here to start looking at the next data file */
			
			data_entry = myTar.getNextEntry();
			String fileName = data_entry.getName();
			
			/* uncompress the index file, read every line 
			 * and store data as object in an array list
			 */
			ArchiveEntry index_entry;
			index_entry = myTar.getNextEntry();
			String index_name = index_entry.getName();
			GZIPInputStream gindex = new GZIPInputStream(new FileInputStream(index_name));
			BufferedReader br = new BufferedReader(new InputStreamReader(gindex));
			String index_str;
			ArrayList<IndexEntry> indexArray = new ArrayList<IndexEntry>();
			while((index_str = br.readLine())!=null){
				indexArray.add(new IndexEntry(index_str));
			}
			br.close();
			Iterator<IndexEntry> index_it = indexArray.iterator();
			
			int temp_test = 0; //delete this right away
			
			/* uncompress gzip file*/
			byte[] buffer = new byte[512];
			GZIPInputStream gzis = new GZIPInputStream(new FileInputStream(fileName));
			FileOutputStream out = new FileOutputStream("file1.txt", false);
			int len;
			int now = 0;   									//controlling amount of file read
			int total = index_it.next().content_length;		//should not exceed more than this

			/*operating at data file level
			 * Create an inverted index for every data file. For this unzip
			 * data while in chunks of 512 bytes. Parse every page which is 
			 * specified by the index file. Receive the string and use 
			 * PageWordCount to write -> Word DocID Freq on the intermediate
			 * inverted index file. Sort this file after the end of while loop
			 * using unix sort and write it to the same file */
			

			int document_id = 0; 
			while((len = gzis.read(buffer, 0, 512)) > 0 && temp_test < 3000){
				
				temp_test++;		//delete this right away
				
				int offset = 0;
				
				//total file size smaller than 512 --> ignore it
				while(total < 512){	
			
					System.out.println("ignoring small page sizes");
					//change offset to write from buffer
					offset = (offset + total)%512;
					//change now to 0
					now = 512;
					//get the next total
					total = index_it.next().content_length;
					if((total+offset) > 512 && total < 512)
						len = gzis.read(buffer, 0, 512);
					
				}
				
				//end of file reached
				if (total-now < len){
					//copy remaining byte array
					byte[] temp = Arrays.copyOfRange(buffer, total-now, len);
					out.write(buffer, 0, total-now);
					
					//call parser with the current file
					PageParser pp = new PageParser("file1.txt");
					System.out.println(++document_id + ")" + pp.getString());
					
					//receive page  word_freq array list from PageWordCount
					//and add it to the data array list. 
					//PageWordCount.writeWordCount(pp.getString(), document_id);
					
					/* page_list = */
					PageWordCount.writeWordCount(pp.getString(), document_id);
					
					/*
					for(String page_entry : page_list){
						data_list.add(page_entry);
					}
					*/
					System.out.println();
					
					//truncate file
					FileWriter f_truncate = new FileWriter("file1.txt");
					f_truncate.write("");
					f_truncate.close();
					
					//add remaining buffer
					out.write(temp, 0, temp.length);
					
					//initialize now to size of remaining buffer and total to next size
					now = len - (total - now);
					
					if(index_it.hasNext())
						total = index_it.next().content_length;
				}
				else {
					out.write(buffer, offset, len-offset);
					now = now + len-offset;
				}
		
			} //reading one data file ends here.

			/* sort intermediate files via unix command. 
			 * This is for sorting the words according to
			 * the first field --> word and the second 
			 * field --> doc_id. */
			Process sorter = Runtime.getRuntime().exec("sort -k 1,1 -k 2,2n intermediate_non_aggregated.txt -o intermediate_non_aggregated.txt");
			sorter.waitFor();
			
			/*close the resources*/
			gzis.close(); 
			out.close();
			myTar.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

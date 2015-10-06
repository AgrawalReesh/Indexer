package web.search.uncompressor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
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
			
			
			
			/* uncompress gzip file*/
			byte[] buffer = new byte[512];
			GZIPInputStream gzis = new GZIPInputStream(new FileInputStream(fileName));
			FileOutputStream out = new FileOutputStream("file1.txt", false);
			int len;
			int now = 0;   			//controlling amount of file read
			int total = 12786;		//should not exceed more than this
			while((len = gzis.read(buffer, 0, 512)) > 0 && now < total){
				
				//end of file reached
				if (total-now < len){
					out.write(buffer, 0, total-now);
					//call parser with the current file
					PageParser pp = new PageParser("file1.txt");
					System.out.println(pp.getString());
					//truncate file
					//add remaining buffer
					//initialize now to size of remaining buffer and total to next size
				}
				else {
					out.write(buffer, 0, len);
				}
				now = now + len;
				
			}
			
			/*close the resources*/
			gzis.close(); 
			out.close();
			myTar.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

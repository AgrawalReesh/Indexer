package web.search.uncompressor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.GZIPInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;

public class Uncompress {
	/* uncompressing a sample tar file */
	public static void main(String args[]) throws IOException{
		try {
			TarArchiveInputStream myTar = new TarArchiveInputStream(new FileInputStream(new File("vol_0_99.tar")));
			
			//print file names in the tar 
			
			ArchiveEntry entry;
			entry = myTar.getNextEntry();
			entry = myTar.getNextEntry();
			String fileName = entry.getName();
			
//			System.out.println(fileName);
//			byte[] content = new byte[32];
//			myTar.read(content, 0, content.length);
//			String str = new String(content, StandardCharsets.UTF_16);
//			System.out.println(str);
			
			byte[] buffer = new byte[1024];
			GZIPInputStream gzis = new GZIPInputStream(new FileInputStream(fileName));
			FileOutputStream out = new FileOutputStream("file1.txt", false);
			int len;
			int now = 0;
			int total = 12786;
			while((len = gzis.read(buffer, 0, 1024)) > 0 && now < total){
				out.write(buffer, 0, len);
				now = now + len;
				
			}
			gzis.close();
			out.close();
			myTar.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

package zip.tool;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Administrator
 *
 */
public class Decompression {

	private String path;
	
	public Decompression(String path){
		this.path = path;
	}
	
	
	public void read(){
		
	}
	
	public InputStream getAndroidManifest(){
		ZipFile zFile;
		try {
			zFile = new ZipFile(this.path);
			ZipEntry entry = zFile.getEntry("AndroidManifest.xml");  
			entry.getComment();
			entry.getCompressedSize();
			entry.getCrc();
			entry.isDirectory();
			entry.getSize();
			entry.getMethod();
			return zFile.getInputStream(entry);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
    public InputStream getAndroidAppIcon(){
        ZipFile zFile;
        try {
            zFile = new ZipFile(this.path);
            ZipEntry entry = zFile.getEntry("res/drawable/app_icon.png"); //
//            entry.getComment();
//            entry.getCompressedSize();
//            entry.getCrc();
//            entry.isDirectory();
//            entry.getSize();
//            entry.getMethod();
            return zFile.getInputStream(entry);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}

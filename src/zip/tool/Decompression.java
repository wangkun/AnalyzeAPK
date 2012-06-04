package zip.tool;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * ��ѹ��ָ�����ļ���Ϣ
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
	
	//���AndroidManifest.xml�ļ���
	public InputStream getAndroidManifest(){
		ZipFile zFile;
		try {
			zFile = new ZipFile(this.path);
			ZipEntry entry = zFile.getEntry("AndroidManifest.xml"); //ָ����ѹ��Android�������ļ�
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
	
	//���getAndroidAppIcon�ļ���
    public InputStream getAndroidAppIcon(){
        ZipFile zFile;
        try {
            zFile = new ZipFile(this.path);
            ZipEntry entry = zFile.getEntry("res/drawable/app_icon.png"); //ָ����ѹ��Android�������ļ�
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

import java.io.*;
import java.net.*;
import java.util.*;

public class Download extends Observable implements Runnable{
	//buffer size for downloading in memory allocated by application
	private static final int MAX_BUFFER_SIZE=1024;
	//statuses during downloading 
	public static final String STATUSES[]= {"Downloading","Paused","Complete","Cancelled","Error"};
	
	public static final int DOWNLOADING=0;
	public static final int PAUSED=1;
	public static final int COMPLETE=2;
	public static final int CANCELLED=3;
	public static final int ERROR=4;
	//attributes of download class
	
	//url of file to be downloaded
	private URL url;
	//size of file
	private int size;
	//size of downloaded file
	private int downloaded;
	//status of downloading
	private int status;
	//constructor for getting URL of download 
	public Download(URL url) {
		this.url=url;
		size=-1;
		downloaded=0;
		status =DOWNLOADING;
		download();
	}
	public String getUrl() {
		return url.toString();
	}
    //To get size of file
	public int getSize() {
		return size;
	}
	//to get progress of download
	public float getProgress() {
		return((float)downloaded/size)*100;
	}
	// to get status of download
	public int getStatus() {
		return status;
	}
	//when pause button is clicked
	public void pause() {
		status=PAUSED;
		stateChanged();
	}
	//when resume button is clicked 
	public void resume() {
		status=DOWNLOADING;
		stateChanged();
		download();
	}
	//when cancel button is clicked
	public void cancel() {
		status=CANCELLED;
		stateChanged();
	}
	//in case error happens to handle it
	public void error() {
		status=ERROR;
		stateChanged();
	}
	
	// creates a separate thread for every download link
	private void download() {
		Thread thread=new Thread(this);
		thread.start();
	}
	//to get file name from the URL link 
	private String getFileName(URL url) {
		String fileName=url.getFile();
		return fileName.substring(fileName.lastIndexOf('/')+1);
	}
	//Overriding the run method of runnable interface
	public void run() {
		//since we have pause and resume options 
		RandomAccessFile file=null;
		//input stream to capture data from the httpURLconnection
		InputStream stream=null;
		try {
			//getting the connection from the URL
			HttpURLConnection connection=(HttpURLConnection)url.openConnection();
			connection.setRequestProperty("Range","bytes="+downloaded+"-");
			connection.connect();
			//200 is the header code to OK
			if(connection.getResponseCode()/100!=2) {
				error();
			}
			
			int contentLength=connection.getContentLength();
			if(contentLength<1) {
				error();
			}
			if(size==-1) {
				size=contentLength;
				stateChanged();
			}
			file=new RandomAccessFile(getFileName(url),"rw");
			//setting the offset till where file has been downlaoded 
			file.seek(downloaded);
			stream=connection.getInputStream();
			//reading the file into the buffer
			while(status==DOWNLOADING) {
				byte buffer[];
				if(size-downloaded>MAX_BUFFER_SIZE) {
					buffer=new byte[MAX_BUFFER_SIZE];
				}else {
					buffer=new byte[size-downloaded];
				}
				int read=stream.read(buffer);
				if(read==-1) {
					break;
				}
				//writing the buffer into the file
				file.write(buffer,0,read);
				downloaded+=read;
				stateChanged();
				
			}
			if(status==DOWNLOADING) {
				status=COMPLETE;
				stateChanged();
			}
		}catch(Exception e) {
			error();
		}
		//closing the file and stream
		finally {
			if(file!=null) {
				try {
					file.close();
				}catch(Exception e) {
					
				}
			}
			if(stream!=null) {
				try {
					stream.close();
				}catch(Exception e) {}
			}
		}
		
	}
	//observer design pattern implementation
	private void stateChanged() {
		setChanged();
		notifyObservers();
	}
	
	
}

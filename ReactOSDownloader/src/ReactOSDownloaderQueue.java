import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.Queue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ReactOSDownloaderQueue
{
	public static String url="http://svn.reactos.org/";
	public static String path="k:/reactos/";
	public static int filethreadnum=0;
	 
	public static boolean setinit=false;//是否强制初始化 
	public static String[] initstring={};//初始化目录位要开始更新的目录，按深度顺序
	public static int curdepth=0;//当前初始化深度 
	public static Queue<FolderClass> folders=new LinkedList<FolderClass>();

	public static class SerialElement implements Serializable
	{
		/**
		 * 
		 */
		public String attrhref="";
		private static final long serialVersionUID = 1L;
		
		public SerialElement(Element ele) 
		{
			attrhref=ele.attr("href");
		}
	}
	
	public static class SerialData implements Serializable
	{
		/**
		 * 
		 */
		public Queue<FolderClass> inner=new LinkedList<FolderClass>();
		private static final long serialVersionUID = 1L;
		public SerialData(Queue<FolderClass> data)
		{
			inner.clear();
			inner.addAll(data);
		}
		public void ReadFromData(Queue<FolderClass> data)
		{
			data.clear();
			data.addAll(inner);
		}
	}
	
	public static class FileThread extends Thread
	{
		String filepath;
		String curnode;
		  
		FileThread(String filepath,String curnode)
		{
			this.filepath=filepath;
			this.curnode=curnode;
		}
		
		@Override
		public void run()
		{
			try
			{
				while(filethreadnum>30)
				{
					sleep(1000);
				}
				filethreadnum++;
				int byteread=0;
				int bytesum=0;
				URL weburl=new URL(url+filepath+curnode);
				URLConnection con=weburl.openConnection();
				InputStream instream=con.getInputStream();
				FileOutputStream fs=new FileOutputStream(escape((path+filepath+curnode).replace("%20"," ")));
				byte[] buffer=new byte[65536];
				while((byteread=instream.read(buffer)) != -1)
				{
					bytesum+=byteread;
					fs.write(buffer,0,byteread);
					System.out.println("\t\t当前下载文件："+filepath+curnode+"\t当前大小："+bytesum);
				}
				fs.close(); 
				instream.close();
				filethreadnum--;
			}
			catch(Exception e)
			{
				System.out.println("error");
				filethreadnum--;
				new File(path+filepath+curnode).deleteOnExit();;
			}
		}
	}
	 
	public static String escape(String src)
	{
		StringBuffer sbuf=new StringBuffer();
		int len=src.length();

		for(int i=3;i<len;i++)
		{
			char ch=src.charAt(i);
			if(ch == '\\' || ch == ':' || ch == '*' || ch == '?' || ch == '"' || ch == '<' || ch == '>' || ch == '|')
				;
			else 
				sbuf.append(ch);
		}
		return src.substring(0,3)+formatpath(sbuf.toString());
	}
	 
	public static String formatpath(String src)
	{
		if(!src.contains("http"))
			return src;
		String newstr=src.substring(0,10)+src.substring(10).replaceAll("/{2,}", "/");
		return newstr;
	}
	 
	public static String createFolder(String folderPath) 
	{
		String txt = folderPath;
		try 
		{
			File myFilePath = new File(txt);
			txt = folderPath;
			if (!myFilePath.exists()) 
			{
				myFilePath.mkdir();
            }
        }
        catch (Exception e) 
        {
            System.out.println("错误!");
        }
		return txt;
	}

	public static class FolderClass implements Serializable
	{
		String filepath;
		SerialElement e;
		  
		FolderClass(SerialElement e,String filepath)
		{
			this.filepath=filepath;
			this.e=e;
		}
		  
		public void ResolveFolder()
		{
			try
			{		    
				String curnode=e.attrhref;
				if(curnode.indexOf(';') != -1 || curnode.charAt(0) == '/' || curnode.equals("../") || curnode.equals("svn/"))
					return;
				System.out.println(curnode+"\t"+filepath);
				if(setinit)
				{
					if(!curnode.equals(initstring[curdepth]))
						return;
					else
						curdepth++;
					if(curdepth >= initstring.length)
						setinit=false;
				}
				   
				if(curnode.charAt(curnode.length()-1) == '/')
				{//目录
					createFolder(escape((path+filepath+curnode).replace("%20"," ")));
					Document doc=Jsoup.connect(formatpath(url+filepath+curnode)).timeout(0).get();
					System.out.println("当前目录："+url+formatpath(filepath)+curnode);
					Elements items=doc.select("tbody tr a");
					for(Element ele1:items)
					{
						folders.offer(new FolderClass(new SerialElement(ele1),filepath+curnode));
					}
					items.clear();
					items=doc.select("ul li a");
					for(Element ele2:items)
					{
						folders.offer(new FolderClass(new SerialElement(ele2),filepath+curnode));
					}
				}
				else
				{//文件
					File curfile=new File((path+filepath+curnode).replace("%20"," "));
					if(curfile.exists())
						return;
					(new FileThread(filepath,curnode)).start();
				}
			}
			catch(Exception e)
			{
				System.out.println("error");
			}
		} 
	}
	
	public static void main(String[] args) throws IOException
	{ 
		try
		{
			if(new File("savedata").exists())
			{
				FileInputStream fis=new FileInputStream("savedata");
				ObjectInputStream ois=new ObjectInputStream(fis);
				SerialData data=(SerialData) ois.readObject();
				data.ReadFromData(folders);
				ois.close();
				fis.close();
			}
			else
			{
				Document doc = Jsoup.connect(url).timeout(0).get();
				Elements items=doc.select("tbody tr a");
				createFolder(path);
				for(Element e1:items)
				{
					folders.offer(new FolderClass(new SerialElement(e1),""));
				}
				items=doc.select("ul li a");
				for(Element e2:items)
				{
					folders.offer(new FolderClass(new SerialElement(e2),""));
				}
				new File("savedata").createNewFile();
			}
				
			new Thread()
			{
				@Override
				public void run()
				{
					while(true)
					{
						try
						{
							sleep(1000);
							FileOutputStream fos=new FileOutputStream("savedata");
							ObjectOutputStream oos=new ObjectOutputStream(fos);
							SerialData data=new SerialData(folders);
							oos.writeObject(data);
							oos.close();
							fos.close();
						}
						catch(Exception e)
						{
						}
					}
				}
			}.start();
			
			new Thread()
			{
				@Override
				public void run()
				{
					while(!folders.isEmpty())
					{
						int searchonce=30;
						while(searchonce-- > 0 && !folders.isEmpty())
						{
							folders.poll().ResolveFolder();
						}
					}
				}
			}.start();
			   
			while(filethreadnum != 0 || folders.size() != 0)
			{
				Thread.sleep(1000);
			}
		}
		catch(Exception exc)
		{
			System.out.println("错误!"+exc.getMessage());
		}
	}
}


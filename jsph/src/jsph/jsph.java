package jsph;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class jsph 
{
	private String keshijieshao="http://www.jsph.net/col/col25/index.html";
	private String[] names={"内科","外科","老年医学科","康复医学科","妇产科","儿科","门急诊","医技"};
	private String[] addrs=
	{
		"http://www.jsph.net/col/col861/index.html",
		"http://www.jsph.net/col/col862/index.html",
		"http://www.jsph.net/col/col863/index.html",
		"http://www.jsph.net/col/col864/index.html",
		"http://www.jsph.net/col/col865/index.html",
		"http://www.jsph.net/col/col866/index.html",
		"http://www.jsph.net/col/col867/index.html",
		"http://www.jsph.net/col/col868/index.html",
	};
	
	private String url="http://www.jsph.net/script/0/nav.js";
	private int threadnum=0;
	
	public jsph()
	{
		try 
		{
			Document keshidoc=Jsoup.connect(keshijieshao).timeout(0).get();
			Elements toprocess=keshidoc.select("a[style=font-size:14px;]");
			toprocess.addAll(keshidoc.select("a[style=font-size:14px; width:100px]"));
//			processKeshi(toprocess);
			Elements doctorlist=new Elements();
			
			int i,j;
			for(i=0;i<names.length;i++)
			{
				Document doc=Jsoup.connect(addrs[i]).timeout(0).get();
				Elements eles=doc.select("td[width=752]").get(0).children();
				for(j=11;j<eles.size();j+=2)
				{
					doctorlist.addAll(eles.get(j).select("a"));
					if(j+1<eles.size() && eles.get(j+1).text().equals(""))
						j++;
				}				
			}
//			for(Element ss:doctorlist)
//				System.out.println(ss.text());
			processYisheng(doctorlist);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public String c(String src) throws Exception
	{
//		return src;
		return new String(src.getBytes(),"gb2312");
	}
	
	public void processKeshi(Elements eles) throws Exception
	{
		FileOutputStream fos=new FileOutputStream("重点专科.xml");
		OutputStreamWriter osw=new OutputStreamWriter(fos,"UTF-8");
		BufferedWriter bw=new BufferedWriter(osw);
		bw.write(c("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"));
		bw.write(c("<keshis>\n"));
		bw.write(c("\t<keshi>\n"));
		bw.write(c("\t\t<name>内科</name>\n"));
		bw.write(c("\t\t<id></id>\n"));
		bw.write(c("\t\t<isroot>true</isroot>\n"));
		bw.write(c("\t</keshi>\n"));
		
		bw.write(c("\t<keshi>\n"));
		bw.write(c("\t\t<name>外科</name>\n"));
		bw.write(c("\t\t<id></id>\n"));
		bw.write(c("\t\t<isroot>true</isroot>\n"));
		bw.write(c("\t</keshi>\n"));	
		
		bw.write(c("\t<keshi>\n"));
		bw.write(c("\t\t<name>老年病学科</name>\n"));
		bw.write(c("\t\t<id>13</id>\n"));
		bw.write(c("\t\t<isroot>true</isroot>\n"));
		bw.write(c("\t</keshi>\n"));
		
		bw.write(c("\t<keshi>\n"));
		bw.write(c("\t\t<name>康复医学科</name>\n"));
		bw.write(c("\t\t<summary>江苏省人民医院康复医学科成立于1956年，55年来学科发展迅速，目前已成立了康复医学中心。"
				+ "现有康复门诊800平方米，病房76张床位、盛泽分院康复医学科22张床位，江苏省人民医院康复医学分院是国内三级甲等"
				+ "医院中临床规模最大、设备最齐全、技术实力最强的科室之一</summary>\n"));
		bw.write(c("\t\t<detailurl>康复医学科.htm</detailurl>\n"));
		bw.write(c("\t\t<id>37</id>\n"));
		bw.write(c("\t\t<isroot>true</isroot>\n"));
		bw.write(c("\t</keshi>\n"));
		
		bw.write(c("\t<keshi>\n"));
		bw.write(c("\t\t<name>妇产科</name>\n"));
		bw.write(c("\t\t<id></id>\n"));
		bw.write(c("\t\t<isroot>true</isroot>\n"));
		bw.write(c("\t</keshi>\n"));
	
		bw.write(c("\t<keshi>\n"));
		bw.write(c("\t\t<name>儿科</name>\n"));
		bw.write(c("\t\t<id>28</id>\n"));
		bw.write(c("\t\t<isroot>true</isroot>\n"));
		bw.write(c("\t</keshi>\n"));
		
		bw.write(c("\t<keshi>\n"));
		bw.write(c("\t\t<name>门急诊</name>\n"));
		bw.write(c("\t\t<id></id>\n"));
		bw.write(c("\t\t<isroot>true</isroot>\n"));
		bw.write(c("\t</keshi>\n"));
		
		bw.write(c("\t<keshi>\n"));
		bw.write(c("\t\t<name>医技</name>\n"));
		bw.write(c("\t\t<id></id>\n"));
		bw.write(c("\t\t<isroot>true</isroot>\n"));
		bw.write(c("\t</keshi>\n"));
		
		String fatherkeshiname="";
		for(int i=0;i<eles.size();i++)
		{
			Document curdoc=Jsoup.connect(eles.get(i).attr("abs:href")).timeout(0).get();
			Elements temp1=curdoc.select("a[style=color:#0053a3;]");
			String curfather=temp1.get(temp1.size()-2).text();
			System.out.println("father:"+curfather);
			if(i == 0)
			{
				bw.write(c("\t<"+curfather+">\n"));
				fatherkeshiname=curfather;
			}
			else if(!fatherkeshiname.equals(curfather))
			{
				bw.write(c("\t</"+fatherkeshiname+">\n"));
				fatherkeshiname=curfather;
				bw.write(c("\t<"+fatherkeshiname+">\n"));
			}
			
			bw.write(c("\t\t<keshi>\n"));
			bw.write(c("\t\t\t<name>"+eles.get(i).text()+"</name>\n"));
			System.out.println(eles.get(i).text());
			bw.write(c("\t\t\t<id></id>\n"));
			Elements inner=curdoc.select("td[style=padding:0px 10px 0px 10px;]\n");
			bw.write(c("\t\t\t<summary>"+getProperText(inner.text())+"</summary>\n"));
			String keshiname=eles.get(i).text();
			bw.write(c("\t\t\t<detailurl>"+keshiname+".htm</detailurl>\n"));
			bw.write(c("\t\t</keshi>\n"));
			
			if(i == eles.size()-1)
			{
				bw.write(c("\t</"+curfather+">\n"));
			}
			
			FileOutputStream infos=new FileOutputStream(keshiname+".htm");
			OutputStreamWriter inosw=new OutputStreamWriter(infos,"UTF-8");
			BufferedWriter inbw=new BufferedWriter(inosw);
			inbw.write(c("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"));
			inbw.write(c("<!DOCTYPE html PUBLIC \"-//WAPFORUM//DTD XHTML Mobile 1.0//EN\"\"http://www.wapforum.org/DTD/xhtml-mobile10.dtd\">\n"));
			inbw.write(c("<html xmlns=\"http://www.w3.org/1999/xhtml\">\n"));
			inbw.write(c("<head><style type=\"text/css\">body{font-size:medium;line-height:1.5em;text-align:left}</style><title>"+keshiname+"</title></head>\n"));
			inbw.write(c("<body>\n\t<div>\n"));
			if(inner.select("a").size() != 0)
			{
				Document detailpage=Jsoup.connect(inner.select("a").attr("abs:href")).timeout(0).get();
				Elements content=detailpage.select("div#zoom p");
				for(Element el:content.select("img"))
				{
					final String url=el.attr("abs:src");
					final String dst=el.attr("src");
					
					while(threadnum > 10)
					{
						Thread.sleep(1000);
					}
					
					new Thread()
					{
						@Override
						public void run()
						{
							threadnum++;
							try 
							{
								downloadImage(url,dst,false);
							} 
							catch (Exception e) 
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							finally
							{
								threadnum--;
							}
						}
					}.start();
				}
				
				inbw.write(c("\t\t"+content.html().replace("/picture","picture").toLowerCase()+"\n"));	
			}
			
			inbw.write(c("\t</div>\n</body>\n</html>"));
			inbw.close();
			inosw.close();
			infos.close();
		}
		bw.write(c("</keshis>\n"));
		
		bw.close();
		osw.close();
		fos.close();
	}
	
	public void downloadImage(String url,String dir,boolean change) throws Exception//下载图片并处理成合适大小
	{
//		String origindir=new String(dir);
//		File file=null;
//		StringBuilder curpath=new StringBuilder();
//		if(dir.charAt(0) == '/')
//			dir=dir.substring(1);
//		while(dir.indexOf('/') != -1)
//		{
//			int index=dir.indexOf('/');
//			curpath.append(dir.substring(0,index));
//			file=new File(curpath.toString());
//			if(!file.exists())
//			{
//				file.mkdir();
//			}
//			curpath.append('/');
//			dir=dir.substring(index+1);
//		}
//		
//
//	    int byteread=0;
//	    int bytesum=0;
//	    URL weburl=new URL(url);
//	    URLConnection con=weburl.openConnection();
//	    InputStream instream=con.getInputStream();
//	    if(new File(curpath.toString()+dir).exists())
//	    	return;
//	    FileOutputStream fs=new FileOutputStream(curpath.toString()+dir);
//	    FileOutputStream sfs=null;
//	    if(change)
//	    	sfs=new FileOutputStream(curpath.toString()+"small_"+dir);
//	    byte[] buffer=new byte[65536];
//	    while((byteread=instream.read(buffer)) != -1)
//	    {
//	    	Thread.sleep(200);
//		     bytesum+=byteread;
//		     fs.write(buffer,0,byteread);
//		     if(change)
//		    	 sfs.write(buffer,0,byteread);
//		     System.out.println("\t\t当前下载文件："+url+"\t当前大小："+bytesum);
//	    }
//	    if(change)
//	    	sfs.close();
//	    fs.close(); 
//	    instream.close();
//	    
//	    if(change)
//	    {
//		//处理图片
//	    	ImageUtil.resize(curpath.toString()+dir,curpath.toString()+dir,160,200);
//	  		ImageUtil.resize(curpath.toString()+"small_"+dir,curpath.toString()+"small_"+dir,80,100);
//	    }
	}
	
	private class MyThread extends Thread
	{
		private String downloadurl;
		private String picurl;
		private BufferedWriter bwp;
		private String doctorname;
		
		public MyThread(Elements info,String picurl,BufferedWriter bwp,int i,Elements eles)
		{
			downloadurl=info.get(0).select("img").attr("abs:src");
			this.picurl=picurl;
			this.bwp=bwp;
			doctorname=eles.get(i).text();
		}
		
		@Override
		public void run()
		{
			threadnum++;
			try
			{
				downloadImage(downloadurl,picurl,true);
			}
			catch(Exception e)
			{
				try 
				{
					bwp.write("无头像："+doctorname+"\n");
				} 
				catch (IOException e1) 
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			finally
			{
				threadnum--;
			}
		}
	}
	
	public void processYisheng(Elements eles) throws Exception
	{
		FileOutputStream fos=new FileOutputStream("名医大全.xml");
		OutputStreamWriter osw=new OutputStreamWriter(fos,"UTF-8");
		BufferedWriter bw=new BufferedWriter(osw);
		bw.write(c("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"));
		bw.write(c("<doctors>\n"));
		String fatherkeshiname="";
		
		FileOutputStream fosp=new FileOutputStream("其他信息.xml");
		OutputStreamWriter oswp=new OutputStreamWriter(fosp);
		BufferedWriter bwp=new BufferedWriter(oswp);	
		
		for(int i=0;i<eles.size();i++)
		{
			Thread.sleep(500);
			System.out.println(eles.get(i).attr("abs:href"));
			System.out.println("\t"+i);
			Document curdoc;
			try 
			{
				curdoc = Jsoup.connect(eles.get(i).attr("abs:href")).timeout(0).get();
			} 
			catch (Exception e1) 
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
				i--;
				continue;
			}
			Elements temp1=curdoc.select("a[style=color:#0053a3;]");
			if(temp1.size() < 2)//竟然使用了个人主页!!!
			{
				bwp.write("个人主页："+eles.get(i).text()+"\n");
				continue;
			}

			String curfather=temp1.get(temp1.size()-2).text();
			System.out.println("father:"+curfather);
			Elements info=curdoc.select("td[style=padding:6px 0px;] table tbody tr td table tbody tr").get(0).children();
			String picurl=info.get(0).select("img").attr("src");
			int index=picurl.lastIndexOf('/');
			String spicurl=picurl.substring(0,index+1)+"small_"+picurl.substring(index+1);
			if(spicurl.charAt(0) == '/')
				spicurl=spicurl.substring(1);
			
			if(i == 0)
			{
				bw.write(c("\t<"+curfather+">\n"));
				fatherkeshiname=curfather;
			}
			else if(!fatherkeshiname.equals(curfather))
			{
				bw.write(c("\t</"+fatherkeshiname+">\n"));
				fatherkeshiname=curfather;
				bw.write(c("\t<"+fatherkeshiname+">\n"));
			}
			
			bw.write(c("\t\t<doctor>\n"));
			bw.write(c("\t\t\t<name>"+eles.get(i).text()+"</name>\n"));
			System.out.println(eles.get(i).text());
			bw.write(c("\t\t\t<id></id>\n"));
			Elements inner=info.get(2).children();
			bw.write(c("\t\t\t<summary>"+inner.get(2).text()+getProperText(inner.get(4).text())+"</summary>\n"));
			String doctorname=eles.get(i).text();
			bw.write(c("\t\t\t<detailurl>"+doctorname+".htm</detailurl>\n"));
			
			while(threadnum > 2)
			{
				Thread.sleep(1000);
			}
			new MyThread(info,picurl,bwp,i,eles).start();
			
			bw.write(c("\t\t\t<smallpicurl>"+spicurl+"</smallpicurl>\n"));
			bw.write(c("\t\t\t<usefulinfourl>"+doctorname+".xml</usefulinfourl>\n"));//排班、评价、，，，
			bw.write(c("\t\t</doctor>\n"));
			
			if(i == eles.size()-1)
			{
				bw.write(c("\t</"+curfather+">\n"));
			}
			
			FileOutputStream infos=new FileOutputStream(doctorname+".htm");
			OutputStreamWriter inosw=new OutputStreamWriter(infos,"UTF-8");
			BufferedWriter inbw=new BufferedWriter(inosw);
			inbw.write(c("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"));
			inbw.write(c("<!DOCTYPE html PUBLIC \"-//WAPFORUM//DTD XHTML Mobile 1.0//EN\"\"http://www.wapforum.org/DTD/xhtml-mobile10.dtd\">\n"));
			inbw.write(c("<html xmlns=\"http://www.w3.org/1999/xhtml\">\n"));
			inbw.write(c("<head><style type=\"text/css\">body{font-size:medium;line-height:1.5em;text-align:left}</style><title>"+doctorname+"</title></head>\n"));
			inbw.write(c("<body>\n\t<div>\n"));
			
			for(Element el:info.select("img"))
			{
				final String url=el.attr("abs:src");
				final String dst=el.attr("src");
				
				while(threadnum > 10)
				{
					Thread.sleep(1000);
				}
				
				new Thread()
				{
					@Override
					public void run()
					{
						threadnum++;
						try 
						{
							downloadImage(url,dst,false);
						} 
						catch (Exception e) 
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						finally
						{
							threadnum--;
						}
					}
				}.start();
			}
			inbw.write(c("\t\t"+info.html().replace("/picture","picture").toLowerCase()+"\n"));
			inbw.write(c("\t</div>\n</body>\n</html>"));
			inbw.close();
			inosw.close();
			infos.close();
		}
		
		bw.write(c("</doctors>\n"));
		
		bw.close();
		osw.close();
		fos.close();
		
		bwp.close();
		oswp.close();
		fosp.close();
	}
	
	private String getProperText(String src)
	{
		src=src.replace("????","");
		if(src.length() > 128)
		{
			StringBuilder sb=new StringBuilder(src.substring(0, 128));
			for(int i=128;i<src.length();i++)
			{
				char ch=src.charAt(i);
				sb.append(ch);
				if(ch == '，' || ch == '。' || ch == '、')
				{
					break;
				}
			}
			sb.setCharAt(sb.length()-1,'。');
			return sb.toString();
		}
		else
			return src;
	}
	
	public static void main(String[] args) throws Exception
	{	
		new jsph();
	}
}

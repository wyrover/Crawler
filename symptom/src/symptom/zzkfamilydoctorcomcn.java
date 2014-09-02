package symptom;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import SQLite.Database;
import SQLite.Exception;

public class zzkfamilydoctorcomcn
{
	public final static int SYMPTOM=0;
	public final static int DISEASE=1;
	public static int threadnum=16;
	
	private Queue<ELE> dataQueue=new LinkedList<ELE>();//症状队列
	public Database db=null;
	
	public class ELE
	{
		public ELE(String url,int type)
		{
			this.url=url;
			this.type=type;
		}
		
		/**数据库格式
		 * 	Name		症状/疾病名
			Id			全局唯一标识
			IsSymptom	症状/疾病？
			BodyPart	身体部位		----症状	头:眼睛
			Keshi		挂号科室		----疾病	内科:男科
			Description	概述			
			DDescription详述
			WithSymptom	伴随症状		----疾病对应症状简述，逗号隔开
			WithDSymptom详细症状		----疾病对应症状详述
			PossibleIll	相关疾病		----症状可能疾病，大项逗号隔开，小项冒号隔开 疾病:科室:伴随症状
			Pathogeny	病因			
			Check		检查
			Diagnosis	诊断鉴别
			Popularity	疾病/症状 搜索热度
		 * 
		 */
		
		public String getName() 
		{
			return name;
		}

		public void setName(String name) 
		{
			this.name = name;
		}

		public String getUrl() 
		{
			return url;
		}

		public void setUrl(String url) 
		{
			this.url = url;
		}

		public int getType() 
		{
			return type;
		}

		public void setType(int type) 
		{
			this.type = type;
		}

		public String getBodypart() 
		{
			return bodypart;
		}

		public void setBodypart(String bodypart) 
		{
			this.bodypart = bodypart;
		}

		public void setwithsymptom(String withsymptom)
		{
			this.withsymptom=withsymptom;
		}
		
		public String getwithsymptom()
		{
			return this.withsymptom;
		}
		
		private String name="";//症状名
		private String url="";//数据地址
		private int type=SYMPTOM;//疾病还是症状
		private String bodypart="";//身体部位	头:眼睛
		private String withsymptom="";
		private String keshi="";
		
		public void resolve()
		{
			String description="";
			String ddescription="";
			String withdsymptom="";
			StringBuilder possibleill=new StringBuilder();
			String pathogeny="";
			String check="";
			String diagnosis="";
			threadnum--;
			String sql="";
			
			try 
			{
				Document doc=null;
				try 
				{
					doc = Jsoup.connect(url).timeout(0).get();
				} 
				catch (java.lang.Exception e) 
				{
					e.printStackTrace();
					return;
				}
				
				if(type == SYMPTOM)
				{//如果是症状
					String[] dataset=doc.select("div.brumbs").text().split(" > ");
					if(dataset.length <= 2)
						return;
					name=dataset[dataset.length-1];
					System.out.println(name);
					for(Element ll:doc.select("div.jzxx li"))
					{
						if(ll.text().contains("发病部位"))
						{
							bodypart=ll.select("a").text();
						}
						else if(ll.text().contains("挂号科室"))
						{
							keshi=ll.select("a").text();
						}
					}
					keshi=dataset[dataset.length-2];
			
					description=doc.select("div.introductionTextBox").text();
					ddescription=description;
					
					Elements tagnames=doc.select("div.subNav a");
					for(Element tag:tagnames)
					{
						if(tag.text().equals("病因"))
						{
							pathogeny=Jsoup.connect(tag.attr("abs:href")).timeout(0).get().select("div.module.mPathogeny div.moduleContent").text();
						}
						else if(tag.text().equals("诊断"))
						{
							diagnosis=Jsoup.connect(tag.attr("abs:href")).timeout(0).get().select("div.module.mFeature div.moduleContent").text();
						}
					}

					//处理相关疾病
					Elements ll=doc.select("div.area.aXgjb div.colMain tr");
					if(!ll.isEmpty())
						ll.remove(0);
					if(!ll.isEmpty())
					{
						for(Element ele:ll)
						{
							Elements temp2=ele.children();
							possibleill.append(temp2.get(0).text());
							possibleill.append(":");
							possibleill.append(temp2.get(1).text());
							possibleill.append(":");
							possibleill.append(temp2.get(2).text());
							possibleill.append(",");
						}
						if(possibleill.length() >= 1)
						{
							possibleill.deleteCharAt(possibleill.length()-1);//去掉多余的','
						}
					}
					
					description=description.replace('\'',' ').replace('"',' ');
					ddescription=ddescription.replace('\'',' ').replace('"',' ');
					withsymptom=withsymptom.replace('\'',' ').replace('"',' ');
					withdsymptom=withdsymptom.replace('\'',' ').replace('"',' ');
					String ill=possibleill.toString().replace('\'',' ').replace('"',' ');
					pathogeny=pathogeny.replace('\'',' ').replace('"',' ');
					check=check.replace('\'',' ').replace('"',' ');
					diagnosis=diagnosis.replace('\'',' ').replace('"',' ');
					
					sql="insert into symptomdata values('"+name+"','0','1','"+bodypart+"','"+keshi+"','"+description+
							"','"+ddescription+"','"+withsymptom+"','"+withdsymptom+"','"+ill+"','"+
							pathogeny+"','"+check+"','"+diagnosis+"','0')";
					db.exec(sql,null);
					System.out.println(name+" left:"+dataQueue.size());
				}
				else if(type == DISEASE)
				{	
					String[] dataset=doc.select("div.brumbs").text().split(" > ");
					if(dataset.length <= 2)
						return;
					name=dataset[dataset.length-1];
					System.out.println(name);
					bodypart=doc.select("span[itemprop=bodyLocation]").text();
					keshi=dataset[dataset.length-2];
					
					description=doc.select("div.introductionTextBox").text();
					ddescription=description;
					Elements tagnames=doc.select("div.subNav a");
					
					for(Element tag:tagnames)
					{
						if(tag.text().equals("病因"))
						{
							pathogeny=Jsoup.connect(tag.attr("abs:href")).timeout(0).get().select("div.module.mPathogeny div.moduleContent").text();
						}
						else if(tag.text().equals("诊断"))
						{
							diagnosis=Jsoup.connect(tag.attr("abs:href")).timeout(0).get().select("div.module.mFeature div.moduleContent").text();
						}
						else if(tag.text().equals("症状"))
						{
							withdsymptom=Jsoup.connect(tag.attr("abs:href")).timeout(0).get().select("div.module.mIntro.mSymptom div.moduleContent").text();
						}
					}
						
					//处理相关疾病
					Elements ll=doc.select("div.area.aXgjb div.colMain tr");
					if(!ll.isEmpty())
						ll.remove(0);
					if(!ll.isEmpty())
					{
						for(Element ele:ll)
						{
							Elements temp2=ele.children();
							possibleill.append(temp2.get(0).text());
							possibleill.append(":");
							possibleill.append(temp2.get(1).text());
							possibleill.append(":");
							possibleill.append(temp2.get(2).text());
							possibleill.append(",");
						}
						if(possibleill.length() >= 1)
						{
							possibleill.deleteCharAt(possibleill.length()-1);//去掉多余的','
						}
					}
					
					description=description.replace('\'',' ').replace('"',' ');
					ddescription=ddescription.replace('\'',' ').replace('"',' ');
					withsymptom=withsymptom.replace('\'',' ').replace('"',' ');
					withdsymptom=withdsymptom.replace('\'',' ').replace('"',' ');
					String ill=possibleill.toString().replace('\'',' ').replace('"',' ');
					pathogeny=pathogeny.replace('\'',' ').replace('"',' ');
					check=check.replace('\'',' ').replace('"',' ');
					diagnosis=diagnosis.replace('\'',' ').replace('"',' ');
					
					sql="insert into symptomdata values('"+name+"','0','0','"+bodypart+"','"+keshi+"','"+description+
							"','"+ddescription+"','"+withsymptom+"','"+withdsymptom+"','"+ill+"','"+
							pathogeny+"','"+check+"','"+diagnosis+"','0')";
					db.exec(sql,null);
					System.out.println(name+" left:"+dataQueue.size());
				}
			} 
			catch (IOException e) 
			{
				System.out.println(sql);
				System.out.println(url);
				e.printStackTrace();
			} 
			catch (Exception e) 
			{
				System.out.println(sql);
				System.out.println(url);
				e.printStackTrace();
			}
			finally
			{
				threadnum++;
			}
		}
	}
	
	public zzkfamilydoctorcomcn()
	{
		try
		{
			db=new Database();
			db.open("J:/symptom.db",0666);
			
//			//按症状查询
//			for(int i=1;i<8000;i++)
//			{
//				String cururl="http://zzk.familydoctor.com.cn/"+i;
//				dataQueue.offer(new ELE(cururl,SYMPTOM));
//			}
			
			//按疾病查询
			for(int i=8900;i<11000;i++)
			{
				String cururl="http://jbk.familydoctor.com.cn/info"+i;
				dataQueue.offer(new ELE(cururl,DISEASE));
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		new Thread()
		{
			@Override
			public void run()
			{
				try 
				{
					sleep(200);
					while(!dataQueue.isEmpty())
					{
						int num=16;
						while(num-- > 0 && threadnum > 0 && !dataQueue.isEmpty())
						{
							new Thread()
							{
								@Override
								public void run()
								{
									ELE curele=dataQueue.poll();
									if(curele != null)
										curele.resolve();
								}
							}.start();
						}
						sleep(200);
					}
				} 
				catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	public static void main(String[] args)
	{
		new zzkfamilydoctorcomcn();
	}
}

import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ResultList extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7586074484755866311L;
	public JTable table=null;
	private JScrollPane sp=null;
	private JButton copyaddress=null;
	private JButton stop=null;
	private int index=0;
	private Queue<MySearchThread> threadqueue=null;
	private JLabel state=null;
	private String errorstring=null;
	
	class MySearchThread extends Thread
	{
		private String cururl=null;
		private boolean findsub=false;
		public MySearchThread(String cururl,boolean findsub)
		{
			super();
			this.cururl=cururl;
			this.findsub=findsub;
			System.out.println(cururl);
		}
		
		@Override
		public void run()
		{
			errorstring=null;
			try
			{
				if(DevineSearch.StopSearch)
				{
					return;
				}
				
				Document doc=Jsoup.connect(cururl).timeout(0).get();//源1		
				Elements curresult=doc.select("table.result");
			    for(Element e:curresult)
			    {
			    	String[] arr=new String[]{""+index,"","",""};
			    	Elements rt1=e.select(".t a");
			    	if(!rt1.isEmpty())
			    	{
			    		arr[1]=RemoveEm(rt1.get(0).text());
			    		arr[3]=RemoveEm(rt1.get(0).attr("href"));
			    	}
			    	rt1=e.select(".c-abstract");
			    	if(!rt1.isEmpty())
			    	{
			    		arr[2]=RemoveEm(rt1.get(0).text());
			    	}

			    	DefaultTableModel tableModel=(DefaultTableModel)table.getModel();
			    	tableModel.addRow(arr);
			    	index++;
			    }		
			    
			    if(findsub)
			    {
					Elements otherresults=doc.select("p#page a");
					for(Element other:otherresults)
					{
						if(!other.text().contains("下一页"));
						{
							threadqueue.add(new MySearchThread(other.attr("abs:href"),false));
						}
					}
			    }
			}
			catch(Exception e)
			{
				e.printStackTrace();
				errorstring=e.getCause().toString();
			}	
		}
	}
	
	public void ClearDefaultTable()
	{
		index=0;
		if(table == null)
			return;
		table.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
					"\u5E8F\u53F7", "\u6807\u9898", "\u5176\u4ED6\u4FE1\u606F", "\u7F51\u5740"
				}
			));
	}
	
	public ResultList(DevineSearch father) 
	{
		super("搜索结果");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent arg0) 
			{
				DevineSearch.result=null;
				DevineSearch.StopSearch=true;
			}	
		});

		addWindowStateListener(new WindowStateListener() 
		{
			@Override
			public void windowStateChanged(WindowEvent e)
			{		
				Rectangle rtb=getContentPane().getBounds();
				if(sp != null)
				{
					Rectangle rta=sp.getBounds();
					sp.setBounds(rta.x,rta.y,rtb.width,rtb.height-30);
					table.setBounds(rta.x,rta.y,rtb.width,rtb.height-30);
				}
				if(copyaddress != null)
				{
					Rectangle rta=copyaddress.getBounds();
					copyaddress.setBounds(rta.x,rtb.y+rtb.height-rta.height,rta.width,rta.height);
				}
				if(stop != null)
				{
					Rectangle rta=stop.getBounds();
					stop.setBounds(rta.x,rtb.y+rtb.height-rta.height,rta.width,rta.height);
				}
				if(state != null)
				{
					Rectangle rta=state.getBounds();
					state.setBounds(rta.x,rtb.y+rtb.height-rta.height,rta.width,rta.height);	
				}
			}
		});

		addComponentListener(new ComponentAdapter() 
		{
			@Override
			public void componentResized(ComponentEvent arg0) 
			{
				Rectangle rtb=getContentPane().getBounds();
				if(sp != null)
				{
					Rectangle rta=sp.getBounds();
					sp.setBounds(rta.x,rta.y,rtb.width,rtb.height-30);
				}
				if(copyaddress != null)
				{
					Rectangle rta=copyaddress.getBounds();
					copyaddress.setBounds(rta.x,rtb.y+rtb.height-rta.height,rta.width,rta.height);
				}
				if(stop != null)
				{
					Rectangle rta=stop.getBounds();
					stop.setBounds(rta.x,rtb.y+rtb.height-rta.height,rta.width,rta.height);
				}
				if(state != null)
				{
					Rectangle rta=state.getBounds();
					state.setBounds(rta.x,rtb.y+rtb.height-rta.height,rta.width,rta.height);				
				}
			}
		});
		
		setTitle("搜索结果");
		getContentPane().setLayout(null);
		
		stop = new JButton("\u505C\u6B62\u641C\u7D22");
		stop.setBounds(32, 339, 93, 23);
		getContentPane().add(stop);
		
		copyaddress = new JButton("\u590D\u5236\u94FE\u63A5");
		copyaddress.setBounds(150, 339, 93, 23);
		getContentPane().add(copyaddress);
		
		table = new JTable();
		ClearDefaultTable();
		table.getColumnModel().getColumn(0).setPreferredWidth(50);
		table.getColumnModel().getColumn(0).setMinWidth(50);
		table.getColumnModel().getColumn(1).setPreferredWidth(250);
		table.getColumnModel().getColumn(2).setPreferredWidth(200);
		table.getColumnModel().getColumn(3).setPreferredWidth(200);
		table.setBounds(10, 10, 564, 288);
		sp=new JScrollPane(table);
		sp.setBounds(0, 0, 584, 329);
		getContentPane().add(sp);
		
		state = new JLabel("");
		state.setBounds(270, 339, 271, 19);
		getContentPane().add(state);
				
		try
		{
			stop.addMouseListener(new MouseAdapter() 
			{
				@Override
				public void mouseClicked(MouseEvent arg0) 
				{
					DevineSearch.StopSearch=true;
				}
			});
			
			copyaddress.addMouseListener(new MouseAdapter() 
			{
				@Override
				public void mouseClicked(MouseEvent arg0) 
				{
					if(table.getSelectedRowCount() > 0)
					{
						String cursel=(String)table.getValueAt(table.getSelectedRow(),3);
						Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(cursel),null);				
					}
				}
			});
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		setSize(600,400);
		setVisible(true);
		
		threadqueue=new LinkedList<MySearchThread>();
		new Thread()
		{
			@Override
			public void run()
			{
				try
				{
					while(true)
					{
						Thread.sleep(100);
						if(!threadqueue.isEmpty())
						{
							state.setText("处理中");
							MySearchThread cur=threadqueue.poll();
							cur.start();
							cur.join();
							if(errorstring == null)
								state.setText("处理完毕");
							else
								state.setText("出错："+errorstring);
						}
						else
							state.setText("空闲");
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}	
			}
		}.start();
	}
	
	public void SearchOne(String url,boolean findsub)
	{
		threadqueue.add(new MySearchThread(url,true));
	}
	
	private String RemoveEm(String src)
	{
		src.replace("<em>","");
		src.replace("</em>","");
		return src;
	}
	
	public static void main(String[] args)
	{
		new ResultList(null).SearchOne("",true);
	}
}

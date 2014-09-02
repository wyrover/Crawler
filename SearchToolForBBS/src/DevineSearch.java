import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.JTextField;

public class DevineSearch extends JPanel implements MouseListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final ButtonGroup searchallconfig = new ButtonGroup();
	private JCheckBox[] wangpanarray=null;
	private JCheckBox[] websitearray=null;
	private JCheckBox[] otherarray=null;
	private JCheckBox allthings=null;
	private JCheckBox wangpan=null;
	private JCheckBox wangzhanluntan=null;
	private JCheckBox others=null;
	public static ResultList result=null;
	public static boolean StopSearch=false;
	private JTextField ToSearch;

	private static final int selallhtmsandfiles=0;
	private static final int selpdfs=1;
	private static final int seldocs=2;
	private static final int selxlss=3;
	private static final int selppts=4;
	private static final int selrtfs=5;
	private static final int selallformats=6;
	private static final int seaanywhere=7;
	private static final int seatitle=8;
	private static final int seaurl=9;
	private static int selformat=selallhtmsandfiles;
	private static int seaplace=seatitle;
	private final ButtonGroup searchplace = new ButtonGroup();
	
	public DevineSearch() 
	{
		super();
		setLayout(null);
		
		JCheckBox baidu = new JCheckBox("\u767E\u5EA6\u7F51\u76D8");
		baidu.setForeground(Color.BLACK);
		baidu.setSelected(true);
		baidu.setBounds(138, 43, 100, 23);
		add(baidu);
		
		JCheckBox _115wangpan = new JCheckBox("115\u7F51\u76D8");
		_115wangpan.setSelected(true);
		_115wangpan.setBounds(240, 43, 100, 23);
		add(_115wangpan);
		
		JCheckBox huawei1 = new JCheckBox("\u534E\u4E3A\u7F51\u76D81");
		huawei1.setSelected(true);
		huawei1.setBounds(342, 43, 100, 23);
		add(huawei1);
		
		JCheckBox huawei2 = new JCheckBox("\u534E\u4E3A\u7F51\u76D82");
		huawei2.setSelected(true);
		huawei2.setBounds(444, 43, 100, 23);
		add(huawei2);
		
		JCheckBox jinshankuaipan = new JCheckBox("\u91D1\u5C71\u5FEB\u76D8");
		jinshankuaipan.setSelected(true);
		jinshankuaipan.setBounds(546, 43, 100, 23);
		add(jinshankuaipan);
		
		JCheckBox lianxiang = new JCheckBox("\u8054\u60F3\u7F51\u76D8");
		lianxiang.setSelected(true);
		lianxiang.setBounds(648, 43, 100, 23);
		add(lianxiang);
		
		JCheckBox yimuhe = new JCheckBox("\u4E00\u6728\u79BE\u7F51\u76D8");
		yimuhe.setSelected(true);
		yimuhe.setBounds(138, 68, 100, 23);
		add(yimuhe);
		
		JSeparator separator = new JSeparator();
		separator.setBackground(Color.BLUE);
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setForeground(Color.BLUE);
		separator.setBounds(130, 0, 2, 400);
		add(separator);
		
		allthings = new JCheckBox("\u7F51\u9875\u548C\u6587\u4EF6");
		allthings.setBounds(6, 6, 118, 23);
		add(allthings);
		
		wangpan = new JCheckBox("\u7F51\u76D8\u8D44\u6E90");
		wangpan.setSelected(true);
		wangpan.setBounds(6, 43, 118, 23);
		add(wangpan);
		
		wangzhanluntan = new JCheckBox("\u7F51\u7AD9\u8BBA\u575B\u8D44\u6E90");
		wangzhanluntan.setBounds(6, 153, 118, 23);
		add(wangzhanluntan);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setForeground(Color.BLUE);
		separator_1.setBackground(Color.BLUE);
		separator_1.setBounds(0, 35, 764, 2);
		add(separator_1);
		
		JCheckBox namipan = new JCheckBox("\u7EB3\u7C73\u76D8");
		namipan.setSelected(true);
		namipan.setBounds(240, 68, 100, 23);
		add(namipan);
		
		JCheckBox qianjunwanma = new JCheckBox("\u5343\u519B\u4E07\u9A6C\u7F51\u76D8");
		qianjunwanma.setSelected(true);
		qianjunwanma.setBounds(342, 68, 100, 23);
		add(qianjunwanma);
		
		JCheckBox keleyun = new JCheckBox("\u53EF\u4E50\u4E91\u7F51\u76D8");
		keleyun.setSelected(true);
		keleyun.setBounds(444, 68, 100, 23);
		add(keleyun);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setForeground(Color.BLUE);
		separator_2.setBackground(Color.BLUE);
		separator_2.setBounds(0, 145, 764, 2);
		add(separator_2);
		
		JRadioButton allhtmsandfiles = new JRadioButton("\u6240\u6709\u7F51\u9875\u548C\u6587\u4EF6");
		allhtmsandfiles.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent arg0) 
			{
				selformat=selallhtmsandfiles;
			}
		});
		allhtmsandfiles.setSelected(true);
		searchallconfig.add(allhtmsandfiles);
		allhtmsandfiles.setBounds(138, 6, 120, 23);
		add(allhtmsandfiles);
		
		JRadioButton pdfs = new JRadioButton("pdf");
		pdfs.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent arg0) 
			{
				selformat=selpdfs;
			}
		});
		searchallconfig.add(pdfs);
		pdfs.setBounds(260, 6, 60, 23);
		add(pdfs);
		
		JRadioButton docs = new JRadioButton("doc");
		docs.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent arg0) 
			{
				selformat=seldocs;
			}
		});
		searchallconfig.add(docs);
		docs.setBounds(322, 6, 60, 23);
		add(docs);
		
		JRadioButton xlss = new JRadioButton("xls");
		xlss.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent arg0) 
			{
				selformat=selxlss;
			}
		});
		searchallconfig.add(xlss);
		xlss.setBounds(384, 6, 60, 23);
		add(xlss);
		
		JRadioButton ppts = new JRadioButton("ppt");
		ppts.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent arg0) 
			{
				selformat=selppts;
			}
		});
		searchallconfig.add(ppts);
		ppts.setBounds(446, 6, 60, 23);
		add(ppts);
		
		JRadioButton rtfs = new JRadioButton("rtf");
		rtfs.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent arg0) 
			{
				selformat=selrtfs;
			}
		});
		searchallconfig.add(rtfs);
		rtfs.setBounds(506, 6, 60, 23);
		add(rtfs);
		
		JRadioButton allformats = new JRadioButton("\u6240\u6709\u683C\u5F0F");
		allformats.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent arg0) 
			{
				selformat=selallformats;
			}
		});
		searchallconfig.add(allformats);
		allformats.setBounds(568, 6, 80, 23);
		add(allformats);
		
		JCheckBox chengtong = new JCheckBox("\u57CE\u901A\u7F51\u76D8");
		chengtong.setSelected(true);
		chengtong.setBounds(546, 68, 100, 23);
		add(chengtong);
		
		JCheckBox xunleikuaichuan = new JCheckBox("\u8FC5\u96F7\u5FEB\u4F20");
		xunleikuaichuan.setSelected(true);
		xunleikuaichuan.setBounds(648, 68, 100, 23);
		add(xunleikuaichuan);
		
		JCheckBox _360yunchuan = new JCheckBox("360\u4E91\u4F20");
		_360yunchuan.setSelected(true);
		_360yunchuan.setBounds(138, 91, 100, 23);
		add(_360yunchuan);
		
		JCheckBox weipan1 = new JCheckBox("\u5A01\u76D81");
		weipan1.setSelected(true);
		weipan1.setBounds(240, 93, 100, 23);
		add(weipan1);
		
		JCheckBox rayfile = new JCheckBox("rayfile\u7F51\u76D8");
		rayfile.setSelected(true);
		rayfile.setBounds(444, 91, 100, 23);
		add(rayfile);
		
		JCheckBox xunzai = new JCheckBox("\u8FC5\u8F7D\u7F51\u76D8");
		xunzai.setSelected(true);
		xunzai.setBounds(546, 91, 100, 23);
		add(xunzai);
		
		JCheckBox _163wangpan = new JCheckBox("163\u7F51\u76D8");
		_163wangpan.setSelected(true);
		_163wangpan.setBounds(648, 93, 100, 23);
		add(_163wangpan);
		
		JCheckBox verycd = new JCheckBox("verycd\u79CD\u5B50");
		verycd.setBounds(138, 153, 100, 23);
		add(verycd);
		
		JCheckBox ed2000 = new JCheckBox("ed2000\u79CD\u5B50");
		ed2000.setBounds(240, 153, 100, 23);
		add(ed2000);
		
		JCheckBox xinlangaiwen = new JCheckBox("\u7231\u95EE\u5171\u4EAB");
		xinlangaiwen.setBounds(444, 153, 100, 23);
		add(xinlangaiwen);
		
		JCheckBox weipan2 = new JCheckBox("\u5A01\u76D82");
		weipan2.setSelected(true);
		weipan2.setBounds(342, 93, 100, 23);
		add(weipan2);
		
		JCheckBox qianyi = new JCheckBox("\u5343\u6613\u7F51\u76D8");
		qianyi.setSelected(true);
		qianyi.setBounds(138, 116, 100, 23);
		add(qianyi);
		
		others = new JCheckBox("\u5F71\u89C6|\u8D44\u6599|\u5176\u4ED6");
		others.setBounds(6, 240, 118, 23);
		add(others);
		
		JCheckBox bthome = new JCheckBox("bt\u4E4B\u5BB6");
		bthome.setBounds(342, 153, 100, 23);
		add(bthome);
		
		JCheckBox dajialuntan = new JCheckBox("\u5927\u5BB6\u8BBA\u575B");
		dajialuntan.setBounds(546, 153, 100, 23);
		add(dajialuntan);
		
		JCheckBox qiannao = new JCheckBox("\u5343\u8111\u4E0B\u8F7D");
		qiannao.setBounds(648, 153, 100, 23);
		add(qiannao);
		
		JCheckBox _51cto = new JCheckBox("51cto\u4E0B\u8F7D");
		_51cto.setBounds(138, 178, 100, 23);
		add(_51cto);
		
		JCheckBox csdn = new JCheckBox("CSDN\u4E0B\u8F7D");
		csdn.setBounds(240, 178, 100, 23);
		add(csdn);
		
		JCheckBox xixi = new JCheckBox("\u897F\u897F\u4E0B\u8F7D");
		xixi.setBounds(342, 178, 100, 23);
		add(xixi);
		
		JSeparator separator_3 = new JSeparator();
		separator_3.setForeground(Color.BLUE);
		separator_3.setBackground(Color.BLUE);
		separator_3.setBounds(0, 232, 764, 2);
		add(separator_3);
		
		JCheckBox baiduwenku = new JCheckBox("\u767E\u5EA6\u6587\u5E93");
		baiduwenku.setBounds(240, 240, 100, 23);
		add(baiduwenku);
		
		JCheckBox xuexiziliaoku = new JCheckBox("\u5B66\u4E60\u8D44\u6599\u5E93");
		xuexiziliaoku.setBounds(342, 240, 100, 23);
		add(xuexiziliaoku);
		
		JCheckBox lanying = new JCheckBox("\u84DD\u5F71\u8BBA\u575B");
		lanying.setBounds(240, 290, 100, 23);
		add(lanying);
		
		JCheckBox zhenhao = new JCheckBox("\u771F\u597D\u8BBA\u575B");
		zhenhao.setBounds(342, 290, 100, 23);
		add(zhenhao);
		
		JCheckBox googlecode = new JCheckBox("googlecode");
		googlecode.setBounds(240, 340, 100, 23);
		add(googlecode);
		
		JCheckBox pudn = new JCheckBox("pudn");
		pudn.setBounds(342, 340, 100, 23);
		add(pudn);
		
		JCheckBox jiaobenzhijia = new JCheckBox("\u811A\u672C\u4E4B\u5BB6");
		jiaobenzhijia.setBounds(444, 240, 100, 23);
		add(jiaobenzhijia);
		
		JLabel label_1 = new JLabel("\u8D44\u6599\uFF1A");
		label_1.setBounds(142, 242, 44, 19);
		add(label_1);
		
		JLabel label_2 = new JLabel("\u7535\u5F71\uFF1A");
		label_2.setBounds(142, 292, 44, 19);
		add(label_2);
		
		JLabel label_3 = new JLabel("\u4EE3\u7801\uFF1A");
		label_3.setBounds(142, 342, 44, 19);
		add(label_3);
		
		JSeparator separator_4 = new JSeparator();
		separator_4.setForeground(Color.BLUE);
		separator_4.setBackground(Color.BLUE);
		separator_4.setBounds(0, 398, 764, 2);
		add(separator_4);
		
		JButton search = new JButton("\u5F00\u59CB\u641C\u7D22");
		search.setBounds(648, 410, 116, 23);
		add(search);
		
		ToSearch = new JTextField();
		ToSearch.setBounds(6, 411, 640, 21);
		add(ToSearch);
		ToSearch.setColumns(10);
		
		JLabel lbllichao = new JLabel("\u4F5C\u8005\u4FE1\u606F:lichao890427  \u8BBA\u575Bwww.0xaa55bbs.com  qq:571652571  \u7FA4:124408915  \u90AE\u7BB1lichao.890427@163.com");
		lbllichao.setForeground(Color.RED);
		lbllichao.setHorizontalAlignment(SwingConstants.LEFT);
		lbllichao.setBounds(6, 473, 640, 19);
		add(lbllichao);
		
		JLabel label = new JLabel("\u641C\u7D22\u5173\u952E\u8BCD\u4F4D\u4E8E\uFF1A");
		label.setBounds(6, 448, 100, 15);
		add(label);
		
		JRadioButton searchanywhere = new JRadioButton("\u7F51\u9875\u7684\u4EFB\u4F55\u5730\u65B9");
		searchanywhere.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent arg0) 
			{
				seaplace=seaanywhere;
			}
		});
		searchplace.add(searchanywhere);
		searchanywhere.setBounds(130, 444, 128, 23);
		add(searchanywhere);
		
		JRadioButton searchtitle = new JRadioButton("\u4EC5\u7F51\u9875\u7684\u6807\u9898\u4E2D");
		searchtitle.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent arg0) 
			{
				seaplace=seatitle;
			}
		});
		searchplace.add(searchtitle);
		searchtitle.setSelected(true);
		searchtitle.setBounds(261, 444, 121, 23);
		add(searchtitle);
		
		JRadioButton searchurl = new JRadioButton("\u4EC5\u7F51\u9875\u7684URL\u4E2D");
		searchurl.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent arg0) 
			{
				seaplace=seaurl;
			}
		});
		searchplace.add(searchurl);
		searchurl.setBounds(385, 444, 121, 23);
		add(searchurl);
		
		try
		{
			wangpanarray=new JCheckBox[]
			{
				baidu,			_115wangpan,	huawei1,		huawei2,		jinshankuaipan,	lianxiang,
				yimuhe,			namipan,		qianjunwanma,	keleyun,		chengtong,		xunleikuaichuan,
				_360yunchuan,	weipan1,		weipan2,		rayfile,		xunzai,			_163wangpan,
				qianyi,
			};
			
			websitearray=new JCheckBox[]
			{
				verycd,			ed2000,			bthome,			xinlangaiwen,	dajialuntan,	qiannao,
				_51cto,			csdn,			xixi,
			};
			
			otherarray=new JCheckBox[]
			{
				baiduwenku,		xuexiziliaoku,	jiaobenzhijia,	
				lanying,		zhenhao,
				googlecode,		pudn,
			};
			
			for(int i=0;i<wangpanarray.length;i++)
			{
				wangpanarray[i].addMouseListener(new MouseAdapter()
				{
					@Override
					public void mouseClicked(MouseEvent arg0) 
					{
						allthings.setSelected(false);
						others.setSelected(false);
					}
				});
			}
			
			for(int i=0;i<websitearray.length;i++)
			{
				websitearray[i].addMouseListener(new MouseAdapter()
				{
					@Override
					public void mouseClicked(MouseEvent arg0) 
					{
						allthings.setSelected(false);
						others.setSelected(false);
					}		
				});
			}
			
			for(int i=0;i<otherarray.length;i++)
			{
				otherarray[i].addMouseListener(new MouseAdapter()
				{
					@Override
					public void mouseClicked(MouseEvent arg0) 
					{
						allthings.setSelected(false);
						others.setSelected(false);
					}	
				});
			}
			
			allthings.addMouseListener(new MouseAdapter() 
			{
				@Override
				public void mouseClicked(MouseEvent arg0) 
				{
					if(allthings.isSelected())
					{
						wangpan.setSelected(false);
						for(int i=0;i<wangpanarray.length;i++)
						{
							wangpanarray[i].setSelected(false);
						}
						wangzhanluntan.setSelected(false);
						for(int i=0;i<websitearray.length;i++)
						{
							websitearray[i].setSelected(false);
						}	
						others.setSelected(false);
						for(int i=0;i<otherarray.length;i++)
						{
							otherarray[i].setSelected(false);
						}
					}
				}
			});
			
			wangpan.addMouseListener(new MouseAdapter() 
			{
				@Override
				public void mouseClicked(MouseEvent arg0) 
				{
					allthings.setSelected(false);
					if(wangpan.isSelected())
					{
						for(int i=0;i<wangpanarray.length;i++)
						{
							wangpanarray[i].setSelected(true);
						}
					}
					else
					{
						for(int i=0;i<wangpanarray.length;i++)
						{
							wangpanarray[i].setSelected(false);
						}
					}
				}
			});
			
			wangzhanluntan.addMouseListener(new MouseAdapter() 
			{
				@Override
				public void mouseClicked(MouseEvent arg0) 
				{
					allthings.setSelected(false);
					if(wangzhanluntan.isSelected())
					{
						for(int i=0;i<websitearray.length;i++)
						{
							websitearray[i].setSelected(true);
						}
					}
					else
					{
						for(int i=0;i<websitearray.length;i++)
						{
							websitearray[i].setSelected(false);
						}
					}
				}
			});
			
			others.addMouseListener(new MouseAdapter() 
			{
				@Override
				public void mouseClicked(MouseEvent arg0) 
				{
					allthings.setSelected(false);
					if(others.isSelected())
					{
						for(int i=0;i<otherarray.length;i++)
						{
							otherarray[i].setSelected(true);
						}
					}
					else
					{
						for(int i=0;i<otherarray.length;i++)
						{
							otherarray[i].setSelected(false);
						}
					}
				}
			});
			
			search.addMouseListener(this);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		setSize(784,547);
		setVisible(true);
	}
	
	public static void main(String[] args)
	{
		try
		{
			DevineSearch cursearch=new DevineSearch();
			DevineSearch.result=new ResultList(cursearch);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) 
	{
		StopSearch=false;
		result.ClearDefaultTable();
		
		try
		{
			String searchstring="http://www.baidu.com/s?q1="+ToSearch.getText()+"&q2=&q3=&q4=&rn=50&lm=0&ct=0&ft=";
			String seawhstring="&q5=1&q6=";
			if(seaplace == seaanywhere)
				seawhstring="&q5=&q6=";
			else if(seaplace == seaurl)
				seawhstring="&q5=2&q6=";
			
			if(allthings.isSelected())
			{
				switch(selformat)
				{
					case selallhtmsandfiles:
						break;
						
					case selpdfs:
						searchstring+="pdf";
						break;
						
					case seldocs:
						searchstring+="doc";
						break;
						
					case selxlss:
						searchstring+="xls";
						break;
						
					case selppts:
						searchstring+="ppt";
						break;
						
					case selrtfs:
						searchstring+="rtf";
						break;
						
					case selallformats:
						searchstring+="all";
						break;
						
					default:
						break;
				}
				result.SearchOne(searchstring+seawhstring,true);
			}
			else if(wangpan.isSelected() || wangzhanluntan.isSelected() || others.isSelected())
			{
				String wangpanarraystr[]=
				{
					"pan.baidu.com",	"q.115.com",		"dl.dbank.com",		"dl.vmall.com",		"www.kuaipan.cn",	"app.lenovo.com",
					"www.yimuhe.com",	"www.namipan.cc",	"7958.com",			"www.colafile.com",	"www.400gb.com",	"kuai.xunlei.com",
					"yunpan.cn",		"vdisk.cn",			"vdisk.weibo.com",	"www.rayfile.com",	"u.xunzai.com",		"www.163disk.com",
					"1000eb.com",
				};
				
				String websitearraystr[]=
				{
					"www.verycd.com",	"www.ed2000.com",		"www.btbbt.com",	"ishare.iask.sina.com.cn",	"club.topsage.com",	"www.qiannao.com",
					"down.51cto.com",	"download.csdn.net",	"www.cr173.com",
				};
				
				String otherarraystr[]=
				{
					"wenku.baidu.com",	"www.xuexi111.com",		"www.jb51.net",
					"www.blue08.cn",	"www.chinazhw.com",
					"googlecode.com",	"www.pudn.com",
				};
			
				searchstring+=seawhstring;
				for(int i=0;i<wangpanarray.length && !StopSearch;i++)
				{
					if(wangpanarray[i].isSelected())
					{
						result.SearchOne(searchstring+wangpanarraystr[i],true);
					}
				}
				for(int i=0;i<websitearray.length && !StopSearch;i++)
				{
					if(websitearray[i].isSelected())
					{
						result.SearchOne(searchstring+websitearraystr[i],true);
					}
				}	
				for(int i=0;i<otherarray.length && !StopSearch;i++)
				{
					if(otherarray[i].isSelected())
					{
						result.SearchOne(searchstring+otherarraystr[i],true);
					}
				}
			}	
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0){}
	@Override
	public void mouseExited(MouseEvent arg0){}
	@Override
	public void mousePressed(MouseEvent arg0){}
	@Override
	public void mouseReleased(MouseEvent arg0){}
}

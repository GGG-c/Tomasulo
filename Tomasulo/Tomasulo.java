
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.text.StyledEditorKit.ForegroundAction;

import org.omg.CORBA.PUBLIC_MEMBER;

import com.sun.org.apache.xml.internal.resolver.helpers.PublicId;

import jdk.internal.dynalink.beans.StaticClass;



public class Tomasulo extends JFrame implements ActionListener{
	
	private JFrame instf;
	private JPanel instp;
	private JTextArea  instinput; 
	private JButton confirm, cancel;
	
	/*
	 * 界面上有六个面板：
	 * panel1 : 指令设置        (已设置)
	 * panel2 : 参数设置   (已设置)
	 * panel3 : 指令状态
	 * panel4 : 保留站状态
	 * panel5 : Load部件
	 * panel6 : 寄存器状态
	 * panel7 : Store部件
	 * panel8 : 内存查询
	 * panel9 : 内存设置
	 */
	private JPanel panel1,panel2,panel3,panel4,panel5,panel6,panel7,panel8,panel9;
	

	/*
	 * 七个操作按钮：步进，进n步，重置，执行，内存查询，指令输入，内存设置
	 */
	private JButton stepbut,stepnbut,resetbut,startbut,checkmembut,instbut,setmembut;

	/*
	 * 指令选择框
	 */
	private JComboBox instbox[]=new JComboBox[24];

	/*
	 * 每个面板的名称
	 */
	private JLabel instl, timel, tl1,tl2,tl3,tl4,tln,resl,regl,ldl,stl,insl,stepsl,meml;
	private int time[]=new int[4];

	/*
	 * 部件执行时间的输入框
	 */
	private JTextField tt1,tt2,tt3,tt4,tn;
	private JTextField tmem[] = new JTextField[4];
	private JLabel datal[] = new JLabel[4];
	private JTextField smem[] = new JTextField[1];
	private JTextField data2[] = new JTextField[1];
	

	private int intv[][]=new int[6][4],instnow=0;

	int cnow;
	private int cal[][]={{-1,0,0},{-1,0,0},{-1,0,0},{-1,0,0},{-1,0,0}};
	private int ld[][]={{0,0},{0,0},{0,0}};
	private int ff[]={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};

	/*
	 * (1)说明：根据你的设计完善指令设置中的下拉框内容
	 * inst： 指令下拉框内容:"NOP","L.D","ADD.D","SUB.D","MULT.D","DIV.D","ST.D"…………
	 * fx：     浮点数寄存器下拉框内容:"F0","F1","F2","F3","F4" …………
	 * rx：       源操作数寄存器内容:"R0","R1","R2","R3","R4","R5","R6","R7","R8","R9" …………
	 * ix：       立即数下拉框内容:"0","1","2","3","4","5","6","7","8","9" …………
	 */
	public static int m=0;
	private String  inst[]={"NOP","L.D","ADD.D","SUB.D","MULT.D","DIV.D","ST.D"},
					fx[]={"F0","F1","F2","F3","F4","F5","F6","F7","F8","F9","F10"},
					rx[]={"R0","R1","R2","R3","R4","R5","R6","R7","R8","R9","R10"},
					ix[]={"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25"};
	private static final int INST_NOP = 0;
	private static final int INST_LD = INST_NOP + 1;
	private static final int INST_ADD = INST_LD + 1;
	private static final int INST_SUB = INST_ADD + 1;
	private static final int INST_MULT = INST_SUB + 1;
	private static final int INST_DIV = INST_MULT + 1;
	private static final int INST_ST = INST_DIV + 1;
	

	/*instst：指令状态列表(7行4列) 
	 *resst：保留站列表(6行8列) 
	 *ldst：load缓存列表(4行4列) 
	 *regst：寄存器列表(3行23列) 
	 *stst：store缓存列表(4行4列)
	 * */
	private	String  instst[][]=new String[7][5], resst[][]=new String[6][9],
					ldst[][]=new String[4][4], regst[][]=new String[3][12],
					stst[][]=new String[4][4];
	private String  culinstst[][]=new String[7][5], culresst[][]=new String[6][9],
			        culldst[][]=new String[4][4], culregst[][]=new String[3][12],
			        culstst[][]=new String[4][4];
	/*将以上String值加入到列表框中*/
	private	JLabel  instjl[][]=new JLabel[7][5], resjl[][]=new JLabel[6][9],
					ldjl[][]=new JLabel[4][4], regjl[][]=new JLabel[3][12],
					stjl[][]=new JLabel[4][4];
	
	/**
	 * 初始化指令队列，指令状态集，保留站，Load缓存站，Store缓存站，寄存器站
	 */
	private Instruction instruction[] = new Instruction[6];
	private InstructionStation IS[] = new InstructionStation[6];
	private ReservationStation RS[] = new ReservationStation[5];
	private LoadStoreStation LS[] = new LoadStoreStation[3];
	private LoadStoreStation SS[] = new LoadStoreStation[3];
	private RegisterStation RegS[] = new RegisterStation[11];
	
	private int load_queue[] = new int[3];
	private int store_queue[] = new int[3];
	
	
	/*
	 * 初始化内存
	 */
	private float mem[] = new float[4096];
	

//构造方法
	public Tomasulo(){
		super("Tomasulo Simulator");

		//设置布局
		Container cp=getContentPane();
		FlowLayout layout=new FlowLayout();
		cp.setLayout(layout);
		
		//指令设置。
		instl = new JLabel("指令设置");
		panel1 = new JPanel(new GridLayout(6,4,0,0));
		panel1.setPreferredSize(new Dimension(350, 150));
		panel1.setBorder(new EtchedBorder(EtchedBorder.RAISED));

		//参数设置
		timel = new JLabel("参数设置");
		panel2 = new JPanel(new GridLayout(3,4,0,0));
		panel2.setPreferredSize(new Dimension(280, 80));
		panel2.setBorder(new EtchedBorder(EtchedBorder.RAISED));

		//指令状态
		insl = new JLabel("指令状态");
		panel3 = new JPanel(new GridLayout(7,5,0,0));
		panel3.setPreferredSize(new Dimension(420, 175));
		panel3.setBorder(new EtchedBorder(EtchedBorder.RAISED));

		//保留站
		resl = new JLabel("保留站");
		panel4 = new JPanel(new GridLayout(6,8,0,0));
		panel4.setPreferredSize(new Dimension(420, 150));
		panel4.setBorder(new EtchedBorder(EtchedBorder.RAISED));

		//Load部件
		ldl = new JLabel("Load部件");
		panel5 = new JPanel(new GridLayout(4,4,0,0));
		panel5.setPreferredSize(new Dimension(200, 100));
		panel5.setBorder(new EtchedBorder(EtchedBorder.RAISED));
		
		//Store部件
		stl = new JLabel("Store部件");
		panel7 = new JPanel(new GridLayout(4,4,0,0));
		panel7.setPreferredSize(new Dimension(200, 100));
		panel7.setBorder(new EtchedBorder(EtchedBorder.RAISED));

		//寄存器状态
		regl = new JLabel("寄存器");
		panel6 = new JPanel(new GridLayout(3,12,0,0));
		panel6.setPreferredSize(new Dimension(740, 75));
		panel6.setBorder(new EtchedBorder(EtchedBorder.RAISED));
		
		//内存值查询
		for (int i=0;i<4096;i++){
			mem[i] = 0;
		}
		meml = new JLabel("内存");
		panel8 = new JPanel(new GridLayout(4,2,0,0));
		panel8.setPreferredSize(new Dimension(200,100));
		panel8.setBorder(new EtchedBorder(EtchedBorder.RAISED));

		panel9 = new JPanel(new GridLayout(1,2,0,0));
		panel9.setPreferredSize(new Dimension(200,100));
		panel9.setBorder(new EtchedBorder(EtchedBorder.RAISED));
		
		tl1 = new JLabel("Load/Store");
		tl2 = new JLabel("加/减");
		tl3 = new JLabel("乘法");
		tl4 = new JLabel("除法");
		tln = new JLabel("n");

//操作按钮:执行，重设，步进，步进n步
		stepsl = new JLabel();
		stepsl.setPreferredSize(new Dimension(200, 30));
		stepsl.setHorizontalAlignment(SwingConstants.CENTER);
		stepsl.setBorder(new EtchedBorder(EtchedBorder.RAISED));
		stepbut = new JButton("步进");
		stepbut.addActionListener(this);
		stepnbut = new JButton("步进n步");
		stepnbut.addActionListener(this);
		startbut = new JButton("执行");
		startbut.addActionListener(this);
		resetbut= new JButton("重设");
		resetbut.addActionListener(this);
		checkmembut= new JButton("内存查询");
		checkmembut.addActionListener(this);
		instbut= new JButton("指令输入");
		instbut.addActionListener(this);
		setmembut = new JButton("内存设置");
		setmembut.addActionListener(this);
		/*设置执行周期初始值*/
		tt1 = new JTextField("2");
		tt2 = new JTextField("2");
		tt3 = new JTextField("10");
		tt4 = new JTextField("40");
		tn = new JTextField("5");

//指令设置
		/*
		 * 设置指令选择框（操作码，操作数，立即数等）的default选择
		 */
		for (int i=0;i<2;i++)
			for (int j=0;j<4;j++){
				if (j==0){
					instbox[i*4+j]=new JComboBox(inst);
				}
				else if (j==1){
					instbox[i*4+j]=new JComboBox(fx);
				}
				else if (j==2){
					instbox[i*4+j]=new JComboBox(ix);
				}
				else {
					instbox[i*4+j]=new JComboBox(rx);
				}
				instbox[i*4+j].addActionListener(this);
				panel1.add(instbox[i*4+j]);
			}
		for (int i=2;i<6;i++)
			for (int j=0;j<4;j++){
				if (j==0){
					instbox[i*4+j]=new JComboBox(inst);
				}
				else {
					instbox[i*4+j]=new JComboBox(fx);
				}
				instbox[i*4+j].addActionListener(this);
				panel1.add(instbox[i*4+j]);
			}
		/*
		 * (3)说明：设置界面默认指令，根据你设计的指令，操作数等的选择范围进行设置。
		 * 默认6条指令。待修改
		 */
		/*L.D F3,21(R2)*/
		instbox[0].setSelectedIndex(1);
		instbox[1].setSelectedIndex(3);
		instbox[2].setSelectedIndex(21);
		instbox[3].setSelectedIndex(2);
        /*L.D F1,20(R3)*/
		instbox[4].setSelectedIndex(1);
		instbox[5].setSelectedIndex(1);
		instbox[6].setSelectedIndex(20);
		instbox[7].setSelectedIndex(3);
        /*MUL.D F0,F1,F2*/
		instbox[8].setSelectedIndex(4);
		instbox[9].setSelectedIndex(0);
		instbox[10].setSelectedIndex(1);
		instbox[11].setSelectedIndex(2);
        /*SUB.D F4,F3,F1*/
		instbox[12].setSelectedIndex(3);
		instbox[13].setSelectedIndex(4);
		instbox[14].setSelectedIndex(3);
		instbox[15].setSelectedIndex(1);
        /*DIV.D F5,F0,F3*/
		instbox[16].setSelectedIndex(5);
		instbox[17].setSelectedIndex(5);
		instbox[18].setSelectedIndex(0);
		instbox[19].setSelectedIndex(3);
        /*ADD.D F3,F4,F1*/
		instbox[20].setSelectedIndex(2);
		instbox[21].setSelectedIndex(3);
		instbox[22].setSelectedIndex(4);
		instbox[23].setSelectedIndex(1);

//执行时间设置
		panel2.add(tl1);
		panel2.add(tt1);
		panel2.add(tl2);
		panel2.add(tt2);
		panel2.add(tl3);
		panel2.add(tt3);
		panel2.add(tl4);
		panel2.add(tt4);
		panel2.add(tln);
		panel2.add(tn);

//指令状态设置
		for (int i=0;i<7;i++)
		{
			for (int j=0;j<5;j++){
				instjl[i][j]=new JLabel(instst[i][j]);
				instjl[i][j].setBorder(new EtchedBorder(EtchedBorder.RAISED));
				panel3.add(instjl[i][j]);
			}
		}
//保留站设置
		for (int i=0;i<6;i++)
		{
			for (int j=0;j<9;j++){
				resjl[i][j]=new JLabel(resst[i][j]);
				resjl[i][j].setBorder(new EtchedBorder(EtchedBorder.RAISED));
				panel4.add(resjl[i][j]);
			}
		}
//Load部件设置
		for (int i=0;i<4;i++)
		{
			for (int j=0;j<4;j++){
				ldjl[i][j]=new JLabel(ldst[i][j]);
				ldjl[i][j].setBorder(new EtchedBorder(EtchedBorder.RAISED));
				panel5.add(ldjl[i][j]);
			}
		}
//Store部件设置
		for (int i=0;i<4;i++)
		{
			for (int j=0;j<4;j++){
				stjl[i][j]=new JLabel(stst[i][j]);
				stjl[i][j].setBorder(new EtchedBorder(EtchedBorder.RAISED));
				panel7.add(stjl[i][j]);
			}
		}
//寄存器设置
		for (int i=0;i<3;i++)
		{
			for (int j=0;j<12;j++){
				regjl[i][j]=new JLabel(regst[i][j]);
				regjl[i][j].setBorder(new EtchedBorder(EtchedBorder.RAISED));
				panel6.add(regjl[i][j]);
			}
		}
		
//内存设置
		for(int i=0; i<tmem.length; i++){
			tmem[i] = new JTextField(Integer.toString(i));
			panel8.add(tmem[i]);
			datal[i] = new JLabel(Float.toString(mem[i]));
			panel8.add(datal[i]);
		}
		for(int i=0; i<smem.length; i++){
			smem[i] = new JTextField(Integer.toString(i));
			panel9.add(smem[i]);
			data2[i] = new JTextField(Float.toString(mem[i]));
			panel9.add(data2[i]);
		}

//向容器添加以上部件
		JPanel area1 = new JPanel();	
		area1.add(instbut);
		area1.add(instl);		
		area1.add(panel1);
		cp.add(area1);
		
		JPanel area2 = new JPanel();
		area2.add(timel);
		area2.add(panel2);
		cp.add(area2);

		JPanel area3 = new JPanel();
		area3.add(startbut);
		area3.add(resetbut);
		area3.add(stepbut);
		area3.add(stepnbut);
		cp.add(area3);

		JPanel area4 = new JPanel();
		area4.add(insl);
		area4.add(panel3);
		cp.add(area4);
		
		JPanel area5 = new JPanel();
		area5.add(ldl);
		area5.add(panel5);
		cp.add(area5);
	
		JPanel area6 = new JPanel();
		area6.add(stl);
		area6.add(panel7);
		cp.add(area6);
		
		JPanel area10 = new JPanel();
		area10.add(checkmembut);
		area10.add(panel8);
		cp.add(area10);
		
		JPanel area11 = new JPanel();
		area11.add(setmembut);
		area11.add(panel9);
		cp.add(area11);
		
		JPanel area7 = new JPanel();
		area7.add(resl);
		area7.add(panel4);
		cp.add(area7);
		
		cp.add(stepsl);
		
		JPanel area9 = new JPanel();
		area9.add(regl);
		area9.add(panel6);
		cp.add(area9);
		
		instf = new JFrame("指令输入");
		instf.setBounds(500, 300, 350, 200);
		instf.setLayout(null);
		//instf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		instp = new JPanel();        
		
		instinput = new JTextArea(7,10);
		instinput.setEditable(true);
		
		confirm = new JButton("Confirm");
		confirm.addActionListener(this);
		cancel = new JButton("Cancel");
		cancel.addActionListener(this);
		
		instp.add(instinput);
		instp.add(confirm);
		instp.add(cancel);
		
		instf.add(instp);
		instf.setVisible(false);
		instf.setContentPane(instp);      //这句加上
		

		stepbut.setEnabled(false);
		stepnbut.setEnabled(false);
		panel3.setVisible(false);
		insl.setVisible(false);
		panel4.setVisible(false);
		ldl.setVisible(false);
		panel5.setVisible(false);
		stl.setVisible(false);
		panel7.setVisible(false);
		resl.setVisible(false);
		stepsl.setVisible(false);
		panel6.setVisible(false);
		regl.setVisible(false);
		checkmembut.setVisible(false);
		setmembut.setVisible(false);
		panel8.setVisible(false);
		panel9.setVisible(false);
		setSize(860,1020);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

/*
 * 点击”执行“按钮后，根据选择的指令，初始化其他几个面板
 */
	public void init(){
		for(int i=0;i<3;i++){
			load_queue[i] = -1;
			store_queue[i] = -1;		
		}
		/*intv：6行4列的整型数组*/
		for (int i=0;i<6;i++){
			intv[i][0]=instbox[i*4].getSelectedIndex();
			if (intv[i][0]!=0){
				intv[i][1]=instbox[i*4+1].getSelectedIndex();
				/*指令形式为load/Store时，选择列表为fx,ix,rx*/
				if (intv[i][0]==INST_LD || intv[i][0]==INST_ST){
					intv[i][2]=instbox[i*4+2].getSelectedIndex();
					intv[i][3]=instbox[i*4+3].getSelectedIndex();
				}
				/*指令形式为算术运算指令时，选择列表为fx,fx,fx*/
				else {
					intv[i][2]=instbox[i*4+2].getSelectedIndex();
					intv[i][3]=instbox[i*4+3].getSelectedIndex();
				}
			}
		}
		/*获取文本框中字符串
		 * 为指令执行周期
		 * */
		time[0]=Integer.parseInt(tt1.getText());
		time[1]=Integer.parseInt(tt2.getText());
		time[2]=Integer.parseInt(tt3.getText());
		time[3]=Integer.parseInt(tt4.getText());
		//System.out.println(time[0]);
		// set 0
		instst[0][0]="指令";
		instst[0][1]="流出";
		instst[0][2]="执行";
		instst[0][3]="写回";
		instst[0][4]="剩余周期数";


		ldst[0][0]="名称";
		ldst[0][1]="Busy";
		ldst[0][2]="地址";
		ldst[0][3]="值";
		ldst[1][0]="Load1";
		ldst[2][0]="Load2";
		ldst[3][0]="Load3";
		ldst[1][1]="no";
		ldst[2][1]="no";
		ldst[3][1]="no";
		
		stst[0][0]="名称";
		stst[0][1]="Busy";
		stst[0][2]="地址";
		stst[0][3]="值";
		stst[1][0]="Store1";
		stst[2][0]="Store2";
		stst[3][0]="Store3";
		stst[1][1]="no";
		stst[2][1]="no";
		stst[3][1]="no";

		resst[0][0]="Time";
		resst[0][1]="名称";
		resst[0][2]="Busy";
		resst[0][3]="Op";
		resst[0][4]="Vj";
		resst[0][5]="Vk";
		resst[0][6]="Qj";
		resst[0][7]="Qk";
		resst[0][8]="Answer";
		resst[1][1]="ADD1";
		resst[2][1]="ADD2";
		resst[3][1]="ADD3";
		resst[4][1]="MULT1";
		resst[5][1]="MULT2";
		resst[1][2]="no";
		resst[2][2]="no";
		resst[3][2]="no";
		resst[4][2]="no";
		resst[5][2]="no";

		regst[0][0]="寄存器号";
		for (int i=1;i<=fx.length;i++){
			regst[0][i]=fx[i-1];

		}
		regst[1][0]="表达式";
		regst[2][0]="数据";

	for (int i=1;i<7;i++)
	{
		instruction[i-1] = new Instruction();
		for (int j=0;j<4;j++){
			if (j==0){
				int temp=i-1;
				/*disp为获取指令类型*/
				String disp;
				disp = inst[instbox[temp*4].getSelectedIndex()]+" ";
				/*NOP指令的情况*/
				if (instbox[temp*4].getSelectedIndex()==INST_NOP) {
					disp=disp;
					/**
					 * 每条指令所对应的指令名称、目的寄存器、源操作数1、源操作数2。对应instbox指定行4个元素
					 */
					instruction[i-1].name=inst[instbox[temp*4].getSelectedIndex()];
					instruction[i-1].opr1=fx[instbox[temp*4+1].getSelectedIndex()];
					instruction[i-1].opr2=fx[instbox[temp*4+2].getSelectedIndex()];
					instruction[i-1].opr3=fx[instbox[temp*4+3].getSelectedIndex()];
				}
				/*load/store指令的情况*/
				else if (instbox[temp*4].getSelectedIndex()==INST_LD || instbox[temp*4].getSelectedIndex()==INST_ST){
					disp=disp+fx[instbox[temp*4+1].getSelectedIndex()]+','+ix[instbox[temp*4+2].getSelectedIndex()]+'('+rx[instbox[temp*4+3].getSelectedIndex()]+')';
					/**
					 * 每条指令所对应的指令名称、目的寄存器、源操作数1、源操作数2。对应instbox指定行4个元素
					 */
					instruction[i-1].name=inst[instbox[temp*4].getSelectedIndex()];
					instruction[i-1].opr1=fx[instbox[temp*4+1].getSelectedIndex()];
					instruction[i-1].opr2=ix[instbox[temp*4+2].getSelectedIndex()];
					instruction[i-1].opr3=rx[instbox[temp*4+3].getSelectedIndex()];
				}
				/*运算指令的情况*/
				else {
					disp=disp+fx[instbox[temp*4+1].getSelectedIndex()]+','+fx[instbox[temp*4+2].getSelectedIndex()]+','+fx[instbox[temp*4+3].getSelectedIndex()];
					/**
					 * 每条指令所对应的指令名称、目的寄存器、源操作数1、源操作数2。对应instbox指定行4个元素
					 */
					instruction[i-1].name=inst[instbox[temp*4].getSelectedIndex()];
					instruction[i-1].opr1=fx[instbox[temp*4+1].getSelectedIndex()];
					instruction[i-1].opr2=fx[instbox[temp*4+2].getSelectedIndex()];
					instruction[i-1].opr3=fx[instbox[temp*4+3].getSelectedIndex()];
				}
				instst[i][j]=disp;
			}
			else instst[i][j]="";
			
		 }
		/**
		 * 指令状态列表初始化
		 */
		IS[i-1]=new InstructionStation();
		IS[i-1].state=0;
		IS[i-1].instruction=instruction[i-1];
		IS[i-1].excutetime=getTimeForEX(instruction[i-1]);
		IS[i-1].out = false; IS[i-1].exec = false; IS[i-1].wb = false;
	}
		
		
		for (int i=1;i<6;i++)
		for (int j=0;j<9;j++)
			if (j!=1&&j!=2){
			resst[i][j]="";
		}
		for (int i=1;i<4;i++)
		for (int j=2;j<4;j++){
			ldst[i][j]="";
		}
		for (int i=1;i<4;i++)
		for (int j=2;j<4;j++){
			stst[i][j]="";
		}
		for (int i=1;i<3;i++)
		for (int j=1;j<12;j++){
			regst[i][j]="";
			regst[2][j]="0.0";
		}
		instnow=0;
		for (int i=0;i<5;i++){
			for (int j=1;j<3;j++) cal[i][j]=0;
			cal[i][0]=-1;
		}
		/*ld中值可对ldst进行写操作*/
		for (int i=0;i<3;i++)
			for (int j=0;j<2;j++) ld[i][j]=0;
		/*ff可对regst进行写操作*/
		for (int i=0;i<12;i++) ff[i]=0;
	
	
	/**
	 * 保留站，寄存器站，Load缓存站获取面板初始值
	 */
	for(int i=0;i<5;i++)
	{
		RS[i]=new ReservationStation();
		RS[i].Qi=resst[i+1][1];
		RS[i].Busy=resst[i+1][2];
		RS[i].Op=resst[i+1][3];
		RS[i].Vj=resst[i+1][4];
		RS[i].Vk=resst[i+1][5];
		RS[i].Qj=resst[i+1][6];
		RS[i].Qk=resst[i+1][7];
		RS[i].Answer = resst[i+1][8];
//		System.out.print(RS[i].Qi);
	}
	for(int i=0;i<3;i++)
	{
		LS[i]=new LoadStoreStation();
		LS[i].Qi=ldst[i+1][0];
		LS[i].Busy=ldst[i+1][1];
		LS[i].Addr=ldst[i+1][2];
		LS[i].value=ldst[i+1][3];
		LS[i].ready = false;
	}
	for(int i=0;i<3;i++)
	{
		SS[i]=new LoadStoreStation();
		SS[i].Qi=stst[i+1][0];
		SS[i].Busy=stst[i+1][1];
		SS[i].Addr=stst[i+1][2];
		SS[i].value=stst[i+1][3];
		SS[i].ready = false;
	}
	for(int i=0;i<11;i++)
	{
		RegS[i]=new RegisterStation();
		RegS[i].state=regst[0][i+1];
		RegS[i].Qi=regst[1][i+1];
		RegS[i].value=regst[2][i+1];
	}
	
	}
	
	public int getTimeForEX(Instruction instruction){
		if(instruction.name=="L.D" || instruction.name=="ST.D")
		{
			return Integer.parseInt(tt1.getText());
//			return 2;
		}
		else if(instruction.name=="ADD.D" || instruction.name=="SUB.D")
		{
			return Integer.parseInt(tt2.getText());
//			return 2;
		}
		else if(instruction.name=="MULT.D")
		{
			return Integer.parseInt(tt3.getText());
//			return 10;
		}
		else if(instruction.name=="DIV.D")
		{
			return Integer.parseInt(tt4.getText());
//			return 40;
		}
		else {
			return 0;
		}
		
	}	
/*
 * 展示当前状态
 */
	public void display(){
		for (int i=0;i<7;i++)
			for (int j=0;j<5;j++){
				instjl[i][j].setText(instst[i][j]);
			}
		for (int i=0;i<6;i++)
			for (int j=0;j<9;j++){
				resjl[i][j].setText(resst[i][j]);
			}
		for (int i=0;i<4;i++)
			for (int j=0;j<4;j++){
				ldjl[i][j].setText(ldst[i][j]);
			}
		for (int i=0;i<4;i++)
			for (int j=0;j<4;j++){
				stjl[i][j].setText(stst[i][j]);
			}
		for (int i=0;i<3;i++)
			for (int j=0;j<12;j++){
				regjl[i][j].setText(regst[i][j]);
			}
		for (int i=0;i<tmem.length;i++){
			int addr = Integer.parseInt(tmem[i].getText());
			if(addr<4096){
				datal[i].setText(Float.toString(mem[addr]));
			}else{
				datal[i].setText("地址超限");
			}
		}
		stepsl.setText("当前周期："+String.valueOf(cnow-1));
	}

	public void actionPerformed(ActionEvent e){
//点击“执行”按钮的监听器
		if (e.getSource()==startbut) {
			//指令设置与执行时间设置关闭
			for (int i=0;i<24;i++) instbox[i].setEnabled(false);
			tt1.setEnabled(false);tt2.setEnabled(false);
			tt3.setEnabled(false);tt4.setEnabled(false);
			tn.setEnabled(false);
			instbut.setEnabled(false);
			instf.setVisible(false);
			stepbut.setEnabled(true);
			stepnbut.setEnabled(true);
			startbut.setEnabled(false);//执行按钮关闭
			//根据指令设置的指令初始化其他的面板
			init();
			cnow=1;
			//展示其他面板
			display();
			panel3.setVisible(true);
			panel4.setVisible(true);
			panel5.setVisible(true);
			panel6.setVisible(true);
			panel7.setVisible(true);
			panel8.setVisible(true);
			panel9.setVisible(true);
			insl.setVisible(true);
			ldl.setVisible(true);
			stl.setVisible(true);
			resl.setVisible(true);
			stepsl.setVisible(true);
			regl.setVisible(true);
			checkmembut.setVisible(true);
			setmembut.setVisible(true);
		}
//点击“重置”按钮的监听器
		if (e.getSource()==resetbut) {
			m=0;
			for (int i=0;i<24;i++) instbox[i].setEnabled(true);
			tt1.setEnabled(true);tt2.setEnabled(true);
			tt3.setEnabled(true);tt4.setEnabled(true);
			tn.setEnabled(true);
			stepbut.setEnabled(false);
			stepnbut.setEnabled(false);
			startbut.setEnabled(true);
			instbut.setEnabled(true);
			panel3.setVisible(false);
			insl.setVisible(false);
			panel4.setVisible(false);
			ldl.setVisible(false);
			panel5.setVisible(false);
			stl.setVisible(false);
			panel7.setVisible(false);
			resl.setVisible(false);
			stepsl.setVisible(false);
			panel6.setVisible(false);
			regl.setVisible(false);
			checkmembut.setVisible(false);
			setmembut.setVisible(false);
			panel8.setVisible(false);
			panel9.setVisible(false);
			
//			temp1=0;
			init();
			cnow=1;
		}
//点击“步进”按钮的监听器
		if (e.getSource()==stepbut) {
//			getVauleOfAll();
			core();
			cnow++;
			display();
		}
//点击“进n步”按钮的监听器
		if (e.getSource()==stepnbut) {
//			getVauleOfAll();
			for (int i=0;i<Integer.parseInt(tn.getText());i++){
				core();
				cnow++;
			}
			display();
		}
//点击“内存查询”按钮的监听器		
		if (e.getSource()==checkmembut) {
			for(int i = 0;i<tmem.length;i++){
				int addr = Integer.parseInt(tmem[i].getText());
				if(addr<4096){
					datal[i].setText(Float.toString(mem[addr]));
				}else{
					datal[i].setText("地址超限");
				}
			}
		}
//点击“内存设置”按钮的监听器		
		if (e.getSource()==setmembut) {
			for(int i = 0;i<smem.length;i++){
				int addr = Integer.parseInt(smem[i].getText());
				float da = Float.parseFloat(data2[i].getText());
				if(addr<4096){
					mem[addr] = da;
					//data2[i].setText("Addr "+ smem[i].getText() + " has changed to " + data2[i].getText());
				}else{
					data2[i].setText("地址超限");
				}
			}
		}
//点击“指令输入”按钮的监听器		
		if (e.getSource()==instbut) {
			instf.setVisible(true);
		}
//点击“Confirm”按钮的监听器		
		if (e.getSource()==confirm) {
			String input = instinput.getText();
			String[] ss = input.split("\n");
			//System.out.printf("%s", ss.length);
			int a1;
			if(ss.length <= 5){
				for(int i = 0;i < ss.length;i++){
					String[] arr = ss[i].split(" ");
					//System.out.printf("%s", arr[2]);
					for( a1=0;a1<=6; a1++){
						if(arr[0].equals(inst[a1])){
							//System.out.printf("%s", a1);
							instbox[4*i].setSelectedIndex(a1);
						}
					}
					for(int a2=0;a2<=10; a2++){
						if(arr[1].equals(fx[a2])){
							//System.out.printf("%s", a2);
							instbox[4*i+1].setSelectedIndex(a2);
						}
					}
					if (instbox[4*i].getSelectedIndex()==INST_LD || instbox[4*i].getSelectedIndex()==INST_ST){
						for(int a3=0;a3<=25; a3++){
							if(arr[2].equals(ix[a3])){
								System.out.printf("%s", a3);
								instbox[4*i+2].setSelectedIndex(a3);
							}
						}
						for(int a4=0;a4<=10; a4++){
							if(arr[3].equals(rx[a4])){
								//System.out.printf("%s", a4);
								instbox[4*i+3].setSelectedIndex(a4);
							}
						}
					}
					else{
						for(int a3=0;a3<=10; a3++){
							if(arr[2].equals(fx[a3])){
								//System.out.printf("%s", a3);
								instbox[4*i+2].setSelectedIndex(a3);
							}
						}
						for(int a4=0;a4<=10; a4++){
							if(arr[3].equals(fx[a4])){
								//System.out.printf("%s", a4);
								instbox[4*i+3].setSelectedIndex(a4);
							}
						}
					}
				}
				for(int k = ss.length;k<=5;k++){
					instbox[4*k].setSelectedIndex(0);
					instbox[4*k+1].setSelectedIndex(0);
					instbox[4*k+2].setSelectedIndex(0);
					instbox[4*k+3].setSelectedIndex(0);
				}
			}
			if(ss.length >5){
				for(int i = 0;i < 6;i++){
					String[] arr = ss[i].split(" ");
					for( a1=0;a1<=6; a1++){
						if(arr[0].equals(inst[a1])){
							instbox[4*i].setSelectedIndex(a1);
						}
					}
					for(int a2=0;a2<=10; a2++){
						if(arr[1].equals(fx[a2])){
							instbox[4*i+1].setSelectedIndex(a2);
						}
					}
					if (instbox[4*i].getSelectedIndex()==INST_LD || instbox[4*i].getSelectedIndex()==INST_ST){
						for(int a3=0;a3<=25; a3++){
							if(arr[2].equals(ix[a3])){
								instbox[4*i+2].setSelectedIndex(a3);
							}
						}
						for(int a4=0;a4<=10; a4++){
							if(arr[3].equals(rx[a4])){
								instbox[4*i+3].setSelectedIndex(a4);
							}
						}
					}
					else{
						for(int a3=0;a3<=10; a3++){
							if(arr[2].equals(fx[a3])){
								instbox[4*i+2].setSelectedIndex(a3);
							}
						}
						for(int a4=0;a4<=10; a4++){
							if(arr[3].equals(fx[a4])){
								instbox[4*i+3].setSelectedIndex(a4);
							}
						}
					}
				}
			}
			instf.setVisible(false);
		}
//点击“Cancel”按钮的监听器	
		if (e.getSource()==cancel) {
			instinput.setText("");
			instf.setVisible(false);
		}
			

		for (int i=0;i<24;i=i+4)
		{
			if (e.getSource()==instbox[i]) {
				//
				if (instbox[i].getSelectedIndex()==INST_LD || instbox[i].getSelectedIndex()==INST_ST){
					instbox[i+2].removeAllItems();
					for (int j=0;j<ix.length;j++) instbox[i+2].addItem(ix[j]);
					instbox[i+3].removeAllItems();
					for (int j=0;j<rx.length;j++) instbox[i+3].addItem(rx[j]);
				}
				else {
					instbox[i+2].removeAllItems();
					for (int j=0;j<fx.length;j++) instbox[i+2].addItem(fx[j]);
					instbox[i+3].removeAllItems();
					for (int j=0;j<fx.length;j++) instbox[i+3].addItem(fx[j]);
				}
			}
		}
	}
/*
 * (4)说明： Tomasulo算法实现
 */
	/*
	public void rest()
	{	
	}
	*/
	
	public void core(){
		send_out_inst();
		for(int i=0; i<IS.length;i++){
			if(IS[i].out && !IS[i].wb){
				if(checkrs(IS[i].Qi)){
					IS[i].exec = true;
					IS[i].excutetime -= 1;
					if(IS[i].excutetime == 0){
						if(!execute(IS[i].Qi)){
							System.out.printf("wrong : %s",IS[i].Qi);
						}
						IS[i].wb = true;
					}
				}
			}
		}
//		for(int i=0;i<3;i++){
//			System.out.printf("%d ",load_queue[i]);
//			System.out.printf("%d \n",store_queue[i]);
//		}
				
		for(int i = 0;i<RegS.length;i++){
			regst[1][i+1] = RegS[i].Qi;
			regst[2][i+1] = RegS[i].value;
		}
		for(int i = 0;i<LS.length;i++){
			ldst[i+1][1] = LS[i].Busy;
			ldst[i+1][2] = LS[i].Addr;
			ldst[i+1][3] = LS[i].value;
		}
		for(int i = 0;i<SS.length;i++){
			stst[i+1][1] = SS[i].Busy;
			stst[i+1][2] = SS[i].Addr;
			stst[i+1][3] = SS[i].value;
		}
		for(int i = 0;i<RS.length;i++){
			resst[i+1][1] = RS[i].Qi;
			resst[i+1][2] = RS[i].Busy;
			resst[i+1][3] = RS[i].Op;
			resst[i+1][4] = RS[i].Vj;
			resst[i+1][5] = RS[i].Vk;
			resst[i+1][6] = RS[i].Qj;
			resst[i+1][7] = RS[i].Qk;
			resst[i+1][8] = RS[i].Answer;
		}
		for(int i=0;i<IS.length;i++){
			if(IS[i].out){
				instst[i+1][1] = "√";
			}
			if(IS[i].exec){
				instst[i+1][2] = "√";
			}
			if(IS[i].wb){
				instst[i+1][3] = "√";
			}
			instst[i+1][4] = Integer.toString(IS[i].excutetime);
		}
		
	}
	public void guangbo(String Q,String value){
		for(int i =0; i<SS.length;i++){
			if(SS[i].value.equals(Q)){
				SS[i].value = value;
			}
		}
		for(int i =0; i<RS.length;i++){
			if(RS[i].Qj.equals(Q)){
				RS[i].Qj = "0";
				RS[i].Vj = value;
			}
			if(RS[i].Qk.equals(Q)){
				RS[i].Qk = "0";
				RS[i].Vk = value;
			}
		}
		for(int i =0; i<RegS.length;i++){
			if(RegS[i].Qi.equals(Q)){
				RegS[i].Qi = "";
				RegS[i].value = value;
			}
		}
	}
	
	
	public boolean execute(String Q){
		int num = Integer.parseInt(Q.substring(Q.length()-1, Q.length()));
		String type = Q.substring(0, Q.length()-1);
		//System.out.println(Q);
		if(type.equals("Load")){
			if(LS[num-1].ready){
				LS[num-1].ready = false;
				LS[num-1].Addr = "";
				LS[num-1].Busy = "no";
				guangbo(Q, LS[num-1].value);
				LS[num-1].value = "";
				for(int i=0;i<2;i++){
					load_queue[i] = load_queue[i+1];
				}
				load_queue[2] = -1;
				return true;
			}
		}else if(type.equals("Store")){
			if(SS[num-1].ready){
				int addr = Integer.parseInt(SS[num-1].Addr);
				mem[addr] = Float.parseFloat(SS[num-1].value);
				SS[num-1].ready = false;
				SS[num-1].Addr = "";
				SS[num-1].Busy = "no";
				SS[num-1].value = "";
				for(int i=0;i<2;i++){
					store_queue[i] = store_queue[i+1];
				}
				store_queue[2] = -1;
				return true;
			}
		}else if(type.equals("ADD")){
			if(RS[num-1].Qj=="0"&&RS[num-1].Qk=="0"){
				float op1 =Float.parseFloat(RS[num-1].Vj);
				float op2 =Float.parseFloat(RS[num-1].Vk);
				float res;
				if(RS[num-1].Op=="ADD.D"){
					res = op1 + op2;
					guangbo(Q,Float.toString(res));
				}else if(RS[num-1].Op=="SUB.D"){
					res = op1 - op2;
					guangbo(Q,Float.toString(res));
				}
				RS[num-1].Answer = "";
				RS[num-1].Busy = "no";
				RS[num-1].Op = "";
				RS[num-1].Qj = "";
				RS[num-1].Qk = "";
				RS[num-1].Vj = "";
				RS[num-1].Vk = "";
				return true;
			}
		}else if(type.equals("MULT")){
			if(RS[num+2].Qj=="0"&&RS[num+2].Qk=="0"){
				float op1 =Float.parseFloat(RS[num+2].Vj);
				float op2 =Float.parseFloat(RS[num+2].Vk);
				float res;
				if(RS[num+2].Op=="MULT.D"){
					res = op1 * op2;
					guangbo(Q,Float.toString(res));
				}else if(RS[num+2].Op=="DIV.D"){
					if(op2 == 0)op2 = (float) 0.1;
					res = op1 / op2;
					guangbo(Q,Float.toString(res));
				}
				RS[num+2].Answer = "";
				RS[num+2].Busy = "no";
				RS[num+2].Op = "";
				RS[num+2].Qj = "";
				RS[num+2].Qk = "";
				RS[num+2].Vj = "";
				RS[num+2].Vk = "";
				return true;
			}
		}
		return false;
	}
	
	
	public boolean checkrs(String Q){
		int num = Integer.parseInt(Q.substring(Q.length()-1, Q.length()));
		String type = Q.substring(0, Q.length()-1);
		if(type.equals("Load")){
			if(LS[num-1].ready && load_queue[0]==num-1){
				return true;
			}
		}else if(type.equals("Store") && store_queue[0]==num-1){
			if(SS[num-1].ready){
				return true;
			}
		}else if(type.equals("ADD")){
			if(RS[num-1].Qj=="0"&&RS[num-1].Qk=="0"){
				return true;
			}
		}else if(type.equals("MULT")){
			if(RS[num+2].Qj=="0"&&RS[num+2].Qk=="0"){
				return true;
			}
		}
		return false;
	}
	
	
	public void send_out_inst(){
		//需要发射的指令
		int need_out = -1;
		for(int i = 0; i<IS.length;i++){
			if(!IS[i].out){
				need_out = i;
				break;
			}
		}
		if(need_out != -1){
			Instruction out = IS[need_out].instruction;
			if(out.name.equals("ST.D")){//Store指令
				String reg = out.opr1;
				String addr = out.opr2;
				int regi = Integer.parseInt(reg.substring(1, reg.length()));	
				for(int i=0;i<SS.length;i++){//查询空闲的Store缓存，将regi里的值放入内存addr
					if(SS[i].Busy == "no"){//空闲Strore缓存
						IS[need_out].out = true;
						IS[need_out].Qi = SS[i].Qi;
						SS[i].Busy = "yes";
						SS[i].Addr = addr;
						for(int ii=0;ii<3;ii++){
							if(store_queue[ii]==-1){
								store_queue[ii] = i;
								break;
							}
						}
						
						if(RegS[regi].Qi == ""){//数据准备好了
							SS[i].value = RegS[regi].value;		
							SS[i].ready = true;
						}else{//数据没准备好
							SS[i].value = RegS[regi].Qi;							
						}
						break;
					}
				}
			}else if(out.name.equals("L.D")){//Load指令
				String reg = out.opr1;
				String addr = out.opr2;
				int regi = Integer.parseInt(reg.substring(1, reg.length()));
				for(int i=0;i<LS.length;i++){//查询空闲的Load缓存，将addr里的值放入寄存器regi
					if(LS[i].Busy == "no"){//空闲Load缓存
						IS[need_out].out = true;
						IS[need_out].Qi = LS[i].Qi;
						LS[i].Busy = "yes";
						LS[i].Addr = addr;
						LS[i].value = Float.toString(mem[Integer.parseInt(addr)]);
						for(int ii=0;ii<3;ii++){
							if(load_queue[ii]==-1){
								load_queue[ii] = i;
								break;
							}
						}
						if(RegS[regi].Qi == ""){//数据准备好了
							RegS[regi].Qi = "Load" + Integer.toString(i+1);	
							LS[i].ready = true;
						}else{//数据没准备好
							LS[i].value = RegS[regi].Qi;						
						}
						break;
					}
				}
			}else if(out.name.equals("ADD.D")||out.name.equals("SUB.D")||out.name.equals("MULT.D")||out.name.equals("DIV.D")){
				int rd = Integer.parseInt(out.opr1.substring(1, out.opr1.length()));
				int r1 = Integer.parseInt(out.opr2.substring(1, out.opr2.length()));
				int r2 = Integer.parseInt(out.opr3.substring(1, out.opr3.length()));
				for(int i=0;i<5;i++){//查询空闲的保留站，将rd = r1 + r2,或rd = r1 - r2
					if(RS[i].Busy == "no"){//空闲的保留站
						if(out.name.equals("ADD.D")||out.name.equals("SUB.D")){
							if(i>=3)continue;
						}
						if(out.name.equals("MULT.D")||out.name.equals("DIV.D")){
							if(i<3)continue;
						}
						IS[need_out].out = true;
						IS[need_out].Qi = RS[i].Qi;
						RS[i].Busy = "yes";
						RS[i].Op = out.name;
						RegS[rd].Qi = RS[i].Qi;
						if(RegS[r1].Qi == ""){
							RS[i].Vj = RegS[r1].value;
							RS[i].Qj = "0";
						}else{
							RS[i].Qj = RegS[r1].Qi;
						}
						if(RegS[r2].Qi == ""){
							RS[i].Vk= RegS[r2].value;
							RS[i].Qk = "0";
						}else{
							RS[i].Qk = RegS[r2].Qi;
						}
						break;
					}
				}
			}
			
		}
	}

	public static void main(String[] args) {
		new Tomasulo();
	}

}

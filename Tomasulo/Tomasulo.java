//请根据你的包路径修改
//package ustc.qyq;

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

/**
 * @author yanqing.qyq 2012-2015@USTC
 * 模板说明：该模板主要提供依赖Swing组件提供的JPanle，JFrame，JButton等提供的GUI。使用“监听器”模式监听各个Button的事件，从而根据具体事件执行不同方法。
 * Tomasulo算法核心需同学们自行完成，见说明（4）
 * 对于界面必须修改部分，见说明(1),(2),(3)
 *
 *  (1)说明：根据你的设计完善指令设置中的下拉框内容
 *	(2)说明：请根据你的设计指定各个面板（指令状态，保留站，Load部件，寄存器部件）的大小
 *	(3)说明：设置界面默认指令
 *	(4)说明： Tomasulo算法实现
 */

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
	 */
	private JPanel panel1,panel2,panel3,panel4,panel5,panel6,panel7,panel8,panel9;
	

	/*
	 * 四个操作按钮：步进，进n步，重置，执行
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
	 * inst： 指令下拉框内容:"NOP","L.D","ADD.D","SUB.D","MULT.D","DIV.D"…………
	 * fx：       目的寄存器下拉框内容:"F0","F2","F4","F6","F8" …………
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
	
	/*
	 * (2)说明：请根据你的设计指定各个面板（指令状态，保留站，Load部件，寄存器部件）的大小
	 * 		指令状态 面板
	 * 		保留站 面板
	 * 		Load部件 面板
	 * 		寄存器 面板
	 * 					的大小 
	 */
	/*instst：指令状态列表(7行4列) 
	 *resst：保留站列表(6行8列) 
	 *ldst：load缓存列表(4行4列) 
	 *regst：寄存器列表(3行23列) 
	 * */
	private	String  instst[][]=new String[7][4], resst[][]=new String[6][9],
					ldst[][]=new String[4][4], regst[][]=new String[3][23],
					stst[][]=new String[4][4];
	private String  culinstst[][]=new String[7][4], culresst[][]=new String[6][9],
			        culldst[][]=new String[4][4], culregst[][]=new String[3][23],
			        culstst[][]=new String[4][4];
	/*将以上String值加入到列表框中*/
	private	JLabel  instjl[][]=new JLabel[7][4], resjl[][]=new JLabel[6][9],
					ldjl[][]=new JLabel[4][4], regjl[][]=new JLabel[3][23],
					stjl[][]=new JLabel[4][4];
	
	/**
	 * 初始化指令队列，指令状态集，保留站，Load缓存站，Store缓存站，寄存器站
	 */
	private Instruction instruction[] = new Instruction[6];
	private InstructionStation IS[] = new InstructionStation[6];
	private ReservationStation RS[] = new ReservationStation[5];
	private LoadStation LS[] = new LoadStation[3];
	private LoadStation SS[] = new LoadStation[3];
	private RegisterStation RegS[] = new RegisterStation[16];
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
		
		//指令设置。GridLayout(int 指令条数, int 操作码+操作数, int hgap, int vgap)
		instl = new JLabel("指令设置");
		panel1 = new JPanel(new GridLayout(6,4,0,0));
		panel1.setPreferredSize(new Dimension(350, 150));
		panel1.setBorder(new EtchedBorder(EtchedBorder.RAISED));

		//操作按钮:执行，重设，步进，步进n步
		timel = new JLabel("参数设置");
		panel2 = new JPanel(new GridLayout(3,4,0,0));
		panel2.setPreferredSize(new Dimension(280, 80));
		panel2.setBorder(new EtchedBorder(EtchedBorder.RAISED));

		//指令状态
		insl = new JLabel("指令状态");
		panel3 = new JPanel(new GridLayout(7,4,0,0));
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
		panel6 = new JPanel(new GridLayout(3,23,0,0));
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
		/*L.D F6,21(R2)*/
		instbox[0].setSelectedIndex(1);
		instbox[1].setSelectedIndex(3);
		instbox[2].setSelectedIndex(21);
		instbox[3].setSelectedIndex(2);
        /*L.D F2,20(R3)*/
		instbox[4].setSelectedIndex(1);
		instbox[5].setSelectedIndex(1);
		instbox[6].setSelectedIndex(20);
		instbox[7].setSelectedIndex(3);
        /*MUL.D F0,F2,F4*/
		instbox[8].setSelectedIndex(4);
		instbox[9].setSelectedIndex(0);
		instbox[10].setSelectedIndex(1);
		instbox[11].setSelectedIndex(2);
        /*SUB.D F8,F6,F2*/
		instbox[12].setSelectedIndex(3);
		instbox[13].setSelectedIndex(4);
		instbox[14].setSelectedIndex(3);
		instbox[15].setSelectedIndex(1);
        /*DIV.D F10,F0,F6*/
		instbox[16].setSelectedIndex(5);
		instbox[17].setSelectedIndex(5);
		instbox[18].setSelectedIndex(0);
		instbox[19].setSelectedIndex(3);
        /*ADD.D F6,F8,F2*/
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
			for (int j=0;j<4;j++){
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
			for (int j=0;j<23;j++){
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
		setSize(860,820);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

/*
 * 点击”执行“按钮后，根据选择的指令，初始化其他几个面板
 */
	public void init(){
		// get value
		/*intv：6行4列的整型数组*/
		for (int i=0;i<6;i++){
			intv[i][0]=instbox[i*4].getSelectedIndex();
			if (intv[i][0]!=0){
				intv[i][1]=2*instbox[i*4+1].getSelectedIndex();//TODO:why * 2
				/*指令形式为load/Store时，选择列表为fx,ix,rx*/
				if (intv[i][0]==INST_LD || intv[i][0]==INST_ST){
					intv[i][2]=instbox[i*4+2].getSelectedIndex();
					intv[i][3]=instbox[i*4+3].getSelectedIndex();
				}
				/*指令形式为算术运算指令时，选择列表为fx,fx,fx*/
				else {
					intv[i][2]=2*instbox[i*4+2].getSelectedIndex();
					intv[i][3]=2*instbox[i*4+3].getSelectedIndex();
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
		resst[1][1]="Add1";
		resst[2][1]="Add2";
		resst[3][1]="Add3";
		resst[4][1]="Mult1";
		resst[5][1]="Mult2";
		resst[1][2]="no";
		resst[2][2]="no";
		resst[3][2]="no";
		resst[4][2]="no";
		resst[5][2]="no";

		regst[0][0]="寄存器号";
		for (int i=1;i<=fx.length;i++){
			//System.out.print(i+" "+fx[i-1];
			regst[0][i]=fx[i-1];

		}
		for (int i=1;i<=rx.length;i++){
			regst[0][i+fx.length] = rx[i-1];
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
	}
		
		/**
		 * test 输出六条指令
		 */
/*		for(int i=0;i<6;i++)
		{
		    System.out.println(instruction[i]);
		}
		*/
		
		for (int i=1;i<6;i++)
		for (int j=0;j<8;j++)
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
		for (int j=1;j<23;j++){
			regst[i][j]="";
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
		for (int i=0;i<23;i++) ff[i]=0;
	
	
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
//		System.out.print(RS[i].Qi);
	}
	for(int i=0;i<3;i++)
	{
		LS[i]=new LoadStation();
		LS[i].Qi=ldst[i+1][0];
		LS[i].Busy=ldst[i+1][1];
		LS[i].Addr=ldst[i+1][2];
		LS[i].value=ldst[i+1][3];
	}
	for(int i=0;i<3;i++)
	{
		SS[i]=new LoadStation();
		SS[i].Qi=stst[i+1][0];
		SS[i].Busy=stst[i+1][1];
		SS[i].Addr=stst[i+1][2];
		SS[i].value=stst[i+1][3];
	}
	for(int i=0;i<16;i++)
	{
		RegS[i]=new RegisterStation();
		RegS[i].state=regst[0][i+1];
		RegS[i].Qi=regst[1][i+1];
		RegS[i].value=regst[2][i+1];
	}
	/**
	 * 将指令状态集与指令队列中指令相互关联
	 */
	/*
	for(int i=0;i<6;i++)
	{
		IS[i]=new InstructionStation();
		IS[i].state=0;
		IS[i].instruction=instruction[i];
		IS[i].excutetime=getTimeForEX(instruction[i]);
	}
	*/
	
	}
	
	public int getTimeForEX(Instruction instruction){
		if(instruction.name=="L.D")
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
			for (int j=0;j<4;j++){
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
			for (int j=0;j<23;j++){
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
//	static int temp1=0;
//	static int ld_current=0;
	
	public void core()
	{
	    int num_issue,num_ex1[],num_ex2[],num_wb[];
	    num_issue=this.IS_getstate(IS);
	    num_ex1=this.EX1_getstate(IS);
	    num_ex2=this.EX2_getstate(IS);
	    num_wb=this.WB_getstate(IS);
	    /**
	     * 发射指令：state置1(执行条件为原值等于0，即指令队列中的等待状态)
	     */
	    if(num_issue!=-1)
	    {
	    	InstructionStation instrsn=IS[num_issue];
	    	/**
	    	 * 当前指令为Load指令时
	    	 */
	    	if(instrsn.instruction.name=="L.D")
	    	{
	    		int num_idld;
	    		num_idld=this.IDLE_load(LS);
	    		if(num_idld!=-1)
	    		{
	    			instrsn.Qi=LS[num_idld].Qi;
	    			LS[num_idld].Busy="yes";
	    			ldst[num_idld+1][1]=LS[num_idld].Busy;
	    			LS[num_idld].value=instrsn.instruction.opr2;
	    			ldst[num_idld+1][3]=LS[num_idld].value;
	    		}
	    		
	    	}
	    	/**
	    	 * 当前指令为运算类指令时
	    	 */
	    	else {
	    		int num_idrs;
	    		num_idrs=this.IDLE_resvstation(IS[num_issue], RS);
	    		if(num_idrs!=-1)
	    		{
	    			instrsn.Qi=RS[num_idrs].Qi;
	    			RS[num_idrs].Busy="yes";
	    			resst[num_idrs+1][2]=RS[num_idrs].Busy;
	    			RS[num_idrs].Op=instrsn.instruction.name;
	    			resst[num_idrs+1][3]=RS[num_idrs].Op;
	    			/**
	    			 * 循环查询已发射指令，如其有目的寄存器作为当前指令的源操作数来源时进行处理
	    			 */
	    			boolean opj,opk;
		    		opj=false;
		    		opk=false;
	    			for(int i=0;i<num_issue;i++)
	    			{
	    				String destination;
	    				destination=IS[i].instruction.opr1;
	    				/**
	    				 * 源操作第一寄存器：Qj,Qk
	    				 */
	    				if(instrsn.instruction.opr2==destination)
	    				{
	    					opj=true;
	    					for(int j=0;j<RegS.length;j++)
	    					{
	    						if(RegS[j].state==destination)
	    						{
	    							if(RegS[j].value=="")
	    							{
	    								RS[num_idrs].Qj=RegS[j].Qi;
	    								resst[num_idrs+1][6]=RS[num_idrs].Qj;
	    							}
	    							else {
										RS[num_idrs].Vj=RegS[j].value;
										resst[num_idrs+1][4]=RS[num_idrs].Vj;
									}
	    							//break;
	    						}
	    					}
	    				}
	    				/**
	    				 * 源操作第二寄存器：Vj,Vk
	    				 */
	    				if(instrsn.instruction.opr3==destination)
	    				{
	    					opk=true;
	    					for(int j=0;j<RegS.length;j++)
	    					{
	    						if(RegS[j].state==destination)
	    						{
	    							if(RegS[j].value=="")
	    							{
	    								RS[num_idrs].Qk=RegS[j].Qi;
	    								resst[num_idrs+1][7]=RS[num_idrs].Qk;
	    							}
	    							else {
										RS[num_idrs].Vk=RegS[j].value;
										resst[num_idrs+1][5]=RS[num_idrs].Vk;
									}
	    							//break;
	    						}
	    					}
	    				}
	    			}
	    			/**
	    			 * 若无寄存器相关，则直接对保留站操作数进行赋值
	    			 */
	    			if(!opj)
	    			{
	    				RS[num_idrs].Vj=instrsn.instruction.opr2;
	    				resst[num_idrs+1][4]="R["+RS[num_idrs].Vj+"]";
	    			}
	    			if(!opk)
	    			{
	    				RS[num_idrs].Vk=instrsn.instruction.opr3;
	    				resst[num_idrs+1][5]="R["+RS[num_idrs].Vk+"]";
	    			}
	    			
	    		}
	    	
			}
	    	/**
	    	 * 寄存器站对该发射指令做出响应
	    	 */
	    	String destination2,Qi;
	    	destination2=instrsn.instruction.opr1;
	    	Qi=instrsn.Qi;
	    	for(int i=0;i<this.RegS.length;i++)
	    	{
	    		if(RegS[i].state==destination2)
	    		{
	    			RegS[i].Qi=Qi;
	    			regst[1][i+1]=RegS[i].Qi;
	    			break;
	    		}
	    	}
	    	/**
	    	 * 修改该指令状态
	    	 */
	    	instst[num_issue+1][1]=String.valueOf(cnow);
	    	IS[num_issue].state=1;
	    }
	    
	    /**
	     * 指令进入执行阶段：state值为1
	     */
	    for(int i=0;i<num_ex1.length;i++)
	    {
	    	if(num_ex1[i]!=-1)
	    	{
	    		InstructionStation instrnsex1=IS[num_ex1[i]];
	    		/**
	    		 * 当为load指令时，更新地址栏，更新指令状态集中的执行列表
	    		 */
	    		if(instrnsex1.instruction.name=="L.D")
	    		{
	    			for(int j=0;j<LS.length;j++)
	    			{
	    				if(LS[j].Qi==instrnsex1.Qi)
	    				{
	    					LS[j].Addr="R["+instrnsex1.instruction.opr3+"]"+instrnsex1.instruction.opr2;
	    					ldst[j+1][2]=LS[j].Addr;
	    					instrnsex1.excutetime--;
	    					break;
	    				}
	    			}
	    			if(instrnsex1.excutetime>0)
	    			{
	    				instst[num_ex1[i]+1][2]=String.valueOf(cnow)+"->";
	    				IS[num_ex1[i]].state=2;
	    			}
	    			else if(instrnsex1.excutetime==0)
	    			{
	    				instst[num_ex1[i]+1][2]=String.valueOf(cnow);
	    				IS[num_ex1[i]].state=3;
	    			}
	    		}
	    		/**
	    		 * 当为运算指令时，更新指令集执行时间列表
	    		 */
	    		else {
	    			String Qi2= instrnsex1.Qi;
					for(int j=0;j<RS.length;j++)
					{
						if(RS[j].Qi==instrnsex1.Qi)
						{
							if(!RS[j].Vj.equals("") && !RS[j].Vk.equals(""))
							{
								instrnsex1.excutetime--;
								resst[j+1][0]=String.valueOf(instrnsex1.excutetime);
								//instrnsex1.excutetime--;
								
								if(instrnsex1.excutetime>0)
								{
									instst[num_ex1[i]+1][2]=String.valueOf(cnow)+"->";
									IS[num_ex1[i]].state=2;
									break;
									
								}
								else if(instrnsex1.excutetime==0)
								{
									instst[num_ex1[i]+1][2]=String.valueOf(cnow);
									IS[num_ex1[i]].state=3;
									break;
								}
							}
						}
					}
				}
	    	}
	    }
		/**
		 * 指令从执行到结束阶段，更新指令执行时间数
		 */
	    for(int i=0;i<num_ex2.length;i++)
	    {
	    	if(num_ex2[i]!=-1)
	    	{
	    		InstructionStation instrnsex2=IS[num_ex2[i]];
	    		/**
	    		 * 当指令为load指令时，更新load缓存中的值
	    		 */
	    		if(instrnsex2.instruction.name=="L.D")
	    		{
	    			for(int j=0;j<LS.length;j++)
	    			{
	    				if(LS[j].Qi==instrnsex2.Qi)
	    				{
	    					LS[j].value="M["+LS[j].Addr+"]";
	    					ldst[j+1][3]=LS[j].value;
	    					instrnsex2.excutetime--;
	    					break;
	    				}
	    			}
	    			if(instrnsex2.excutetime==0)
	    			{
	    				instst[num_ex2[i]+1][2]+=String.valueOf(cnow);
	    				IS[num_ex2[i]].state=3;
	    			}
	    		}
	    		/**
	    		 * 如果为其他运算指令,更新保留站中的执行计时时间
	    		 */
	    		else {
	    			int j;
	    			for(j=0;j<RS.length;j++)
					{
						if(RS[j].Qi==instrnsex2.Qi)
						{
						    instrnsex2.excutetime--;
							resst[j+1][0]=String.valueOf(instrnsex2.excutetime);
							break;
						}
					}
	    			if(instrnsex2.excutetime==0)
					{
						instst[num_ex2[i]+1][2]+=String.valueOf(cnow);
						IS[num_ex2[i]].state=3;
						resst[j+1][0]="";
					}
				}
	    	}
	    }
	    
	    /**
	     * 执行完毕，写回过程
	     */
	    for(int i=0;i<num_wb.length;i++)
	    {
	    	if(num_wb[i]!=-1)
	    	{
	    		InstructionStation instrnswb=IS[num_wb[i]];
	    		String Qi4=instrnswb.Qi;
	    		/**
	    		 * 指令为load指令时，写回，取消对load缓存站相应站位的占用
	    		 */
	    		if(instrnswb.instruction.name=="L.D")
	    		{
	    			for(int j=0;j<LS.length;j++)
	    			{
	    				if(LS[j].Qi==instrnswb.Qi)
	    				{
	    					LS[j].Busy="no";
	    					LS[j].Addr="";
	    					LS[j].value="";
	    					ldst[j+1][1]=LS[j].Busy;
	    					ldst[j+1][2]=LS[j].Addr;
	    					ldst[j+1][3]=LS[j].value;
	    					break;
	    				}
	    			}
	    		}
	    		/**
	    		 * 指令为其他指令时更新保留站的信息，解除相应保留站的占用
	    		 */
	    		else {
					for(int j=0;j<RS.length;j++)
					{
						if(RS[j].Qi==Qi4)
						{
							RS[j].Busy="no";
							RS[j].Op="";
							RS[j].Qj="";
							RS[j].Qk="";
							RS[j].Vj="";
							RS[j].Vk="";
							resst[j+1][2]=RS[j].Busy;
							for(int k=3;k<8;k++)
								resst[j+1][k]="";	
							break;
						}
					}
				}
	    		/**
	    		 * 更新指令目的寄存器对应的寄存器站
	    		 */
	    		for(int j=0;j<RegS.length;j++)
	    		{
	    			if(RegS[j].Qi==Qi4)
	    			{
	    				m++;
	    				RegS[j].value="M"+m;
	    				regst[2][j+1]=RegS[j].value;
	    			}
	    		}
	    		/**
	    		 * 更新保留站中需要该寄存器值的源操作数
	    		 */
	    		for(int j=0;j<RS.length;j++)
	    		{
	    			if(RS[j].Qj==Qi4)
	    			{
	    				RS[j].Vj="M"+m;
	    				RS[j].Qj="";
	    				resst[j+1][4]=RS[j].Vj;
	    				resst[j+1][6]=RS[j].Qj;
	    				continue;
	    			}
	    			if(RS[j].Qk==Qi4)
	    			{
	    				RS[j].Vk="M"+m;
	    				RS[j].Qk="";
	    				resst[j+1][5]=RS[j].Vk;
	    				resst[j+1][7]=RS[j].Qk;
	    			}
	    		}
	    		instst[num_wb[i]+1][3]=String.valueOf(cnow);
	    		IS[num_wb[i]].state=4;
	    	}
	    }
	    
		boolean completed=true;
		for(int l=0;l<IS.length;l++)
		{
			if(IS[l].instruction.name!="NOP" && instst[l+1][3]=="")
			{
				completed=false;
				break;
			}
		}
		if(completed==true)
		{
			stepbut.setEnabled(false);
			stepnbut.setEnabled(false);
		}
	   
	}
    /**
     * 获取空闲load缓存部件编号
     * @param LS
     * @return
     */
	private int IDLE_load(LoadStation LS[]){
		int num_idld=-1;
		for(int i=0;i<LS.length;i++)
		{
			if(LS[i].Busy=="no")
			{
				num_idld=i;
				break;
			}
		}
		return num_idld;
	}
	/**
	 * 获取空闲保留站计算部件(Add或Mult)编号
	 * @param RS
	 * @return
	 */
	private int IDLE_resvstation(InstructionStation IS,ReservationStation RS[])
	{
		int num_idrs=-1;
		
		if(IS.instruction.name=="MULT.D" || IS.instruction.name=="DIV.D")
		{
		    for(int i=3;i<5;i++)
		    {
			    if(RS[i].Busy=="no")
			    {
				    num_idrs=i;
				    break;
			    }
		    }
		}
		else if(IS.instruction.name=="ADD.D" || IS.instruction.name=="SUB.D")
		{
		    for(int i=0;i<3;i++)
		    {
			    if(RS[i].Busy=="no")
			    {
				    num_idrs=i;
				    break;
			    }
		    }
		}
		return num_idrs;
	}
    /**
     * 返回等待执行写回的指令编号(发射序排列)
     * @param IS
     * @return
     */
	private int[] WB_getstate(InstructionStation IS[]) {
		int n=0;
		for(int i=0;i<IS.length;i++)
		{
			if(IS[i].state==3 && IS[i].Qi!="NOP")
			{
				n++;
			}
		}
		int num_wb[] = new int[n];
        for(int i=0;i<n;i++)
        {
        	num_wb[i]=-1;
        }
        /**
         * 找出执行完毕却没有写回的指令，返回其在指令队列中的位置
         */
        for(int i=0,j=0;i<IS.length;i++)
		{
			if(IS[i].state==3 && IS[i].Qi!="NOP")
			{
				num_wb[j]=i;
				j++;
			}
		}
        return num_wb;
	}
    /**
     * 返回正在执行还未执行完毕的指令编号
     * @param IS
     * @return
     */
	private int[] EX2_getstate(InstructionStation IS[]) {
		int n=0;
		for(int i=0;i<IS.length;i++)
		{
			if(IS[i].state==2 && IS[i].Qi!="NOP")
			{
				n++;
			}
		}
		int num_ex2[] = new int[n];
        for(int i=0;i<n;i++)
        {
        	num_ex2[i]=-1;
        }
        /**
         * 找出正在执行却没有执行完毕的指令，返回其在指令队列中的位置
         */
        for(int i=0,j=0;i<IS.length;i++)
		{
			if(IS[i].state==2 && IS[i].Qi!="NOP")
			{
				num_ex2[j]=i;
				j++;
			}
		}
        return num_ex2;
	}
    /**
     * 返回等待执行的指令编号
     * @param IS
     * @return
     */
	private int[] EX1_getstate(InstructionStation IS[]) {
		int n=0;
		for(int i=0;i<IS.length;i++)
		{
			if(IS[i].state==1 && IS[i].Qi!="NOP")
			{
				n++;
			}
		}
		int num_ex1[] = new int[n];
        for(int i=0;i<n;i++)
        {
        	num_ex1[i]=-1;
        }
        /**
         * 找出已发射但还在等待执行的指令，返回其在指令队列中的位置
         */
        for(int i=0,j=0;i<IS.length;i++)
		{
			if(IS[i].state==1 && IS[i].Qi!="NOP")
			{
				num_ex1[j]=i;
				j++;
			}
		}
        return num_ex1;
		
	}
    /**
     * 返回等待发射的指令编号
     * @param IS
     * @return
     */
	private int IS_getstate(InstructionStation IS[]) {
		int num_issue=-1;
		   for(int i=0;i<IS.length;i++)
		   {
			   if(IS[i].state==0 && IS[i].Qi!="NOP")
			   {
				   num_issue=i;
				   break;
			   }
		   }
		   return num_issue;
	}

	public static void main(String[] args) {
		new Tomasulo();
	}

}

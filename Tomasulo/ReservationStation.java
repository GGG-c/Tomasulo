/**
 * 保留站
 * @author caoxin
 *
 */
public class ReservationStation{
	
	/**
	 * 设置Qi便于对寄存器Qi进行赋值，Qi为该保留站的名称，用以告知指定寄存器结果来源
	 * */
	String Qi;
	/**
	 * 指令第一个操作数：op2
	 * */
	String Vj;
	/**
	 * 指令第二个操作数：op3
	 * */
	String Vk;
	/**
	 * 产生指令第一个操作数值的保留站
	 * */
	String Qj;
	/**
	 * 产生指令第二个操作数值的保留站
	 * */
	String Qk;
	/**
	 * 指令操作的类型
	 * */
	String Op;
	/**
	 * 保留站工作状态
	 */
	String Busy;
	
}
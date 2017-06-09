
public class InstructionStation{
	/**
	 * 当前指令所在保留站名称
	 */
	String Qi;
	/**
	 * 当前指令所处的状态
	 */
	int state;
	/**
	 * 指令执行所需要的时间
	 */
	int excutetime;
	/**
	 * 该指令状态所对应的指令
	 */
	Instruction instruction;
	
	boolean out,exec,wb;
	
}
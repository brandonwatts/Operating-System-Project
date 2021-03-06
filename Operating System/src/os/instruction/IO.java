package os.instruction;

import os.OperatingSystem;
import os.Process;
import os.ProcessState;
import os.ui.TaskManager;

public class IO implements Instruction {
	@Override
	public void execute() {
		System.out.println("IO INSTRUCTION EXECUTING");
		
		int id = OperatingSystem.cpu.registers[OperatingSystem.PROCESS_ID_REGISTER];
		Process process = OperatingSystem.scheduler.getProcessByID(id);
		OperatingSystem.cpu.registers[OperatingSystem.INSTRUCTION_REGISTER]++;
		OperatingSystem.dispatcher.load(null, ProcessState.WAIT);
		
		OperatingSystem.scheduler.readyQueue.remove(process);
		OperatingSystem.scheduler.ioQueue.add(process);
		
		TaskManager.updateTaskManager();
	}
	
	@Override
	public String toString() {
		return "IO ";
	}
}

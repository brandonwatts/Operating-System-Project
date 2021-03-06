package os.instruction;

import os.OperatingSystem;
import os.ui.TaskManager;

public class Out implements Instruction {
	@Override
	public void execute() {
		int id = OperatingSystem.cpu.registers[OperatingSystem.PROCESS_ID_REGISTER];
		String string = OperatingSystem.scheduler.getProcessByID(id).toString();
		OperatingSystem.prompt.append(string + OperatingSystem.cpu.toString());
		OperatingSystem.cpu.registers[OperatingSystem.INSTRUCTION_REGISTER]++;
		TaskManager.updateTaskManager();
	}
	
	@Override
	public String toString() {
		return "OUT";
	}
}

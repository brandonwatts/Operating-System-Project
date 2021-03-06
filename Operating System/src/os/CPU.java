package os;

import os.instruction.Instruction;

import java.util.ArrayList;

public class CPU {
	public int[] registers = new int[OperatingSystem.NUMBER_OF_REGISTERS];
	public ArrayList<Interrupter> interruptQueue = new ArrayList<Interrupter>();
	
	public CPU() {
		registers[OperatingSystem.PROCESS_ID_REGISTER] = -1;
	}
	
	public void signalInterrupt(Interrupter interrupter) {
		interruptQueue.add(interrupter);
	}
	
	public void execute() {
		if (interruptQueue.isEmpty()) {
			if (registers[OperatingSystem.PROCESS_ID_REGISTER] >= 0) {
				int address = registers[OperatingSystem.INSTRUCTION_REGISTER];
				((Instruction) OperatingSystem.memory.read(address)).execute();
			}
		} else {
			interruptQueue.remove(0).interrupt();
		}
	}
	
	@Override
	public String toString() {
        String string = "";

        string += "CPU Information\n";
		for (int i = 0; i < OperatingSystem.NUMBER_OF_REGISTERS; i++) {
			string += "registers[" + i + "] = " + registers[i] + "\n";
		}

        return string;
	}
}

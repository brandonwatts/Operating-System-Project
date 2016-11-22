package os;

import java.util.ArrayList;

public class Scheduler {
    public ArrayList<ProcessData> newQueue;
    public ArrayList<Process> readyQueue;
    public ArrayList<Process> ioQueue;

    private int identifier = 1;

    public Scheduler() {
        this.newQueue = new ArrayList<>();
        this.readyQueue = new ArrayList<>();
        this.ioQueue = new ArrayList<>();
    }

    public void execute() {
        updateNewQueue();
    }

    private void updateNewQueue() {
        ArrayList<ProcessData> addedProcesses = new ArrayList<>();

        for (ProcessData data : newQueue) {
            if (data.startTime <= OperatingSystem.clock.clockCycle) {
                Process process = new Process(data, identifier);

                OperatingSystem.taskManager.addToModel(process);
                Tasks.refreshStats();
                readyQueue.add(process);
                addedProcesses.add(data);

                int[] backup = OperatingSystem.cpu.registers;
                OperatingSystem.cpu.registers = process.registers;

                for (int i = 0; i < data.instructions.size(); i++) {
                    int index = i + process.registers[OperatingSystem.PROC_BASE_REGISTER];
                    OperatingSystem.memory.write(index, data.instructions.get(i));
                }

                OperatingSystem.cpu.registers = backup;

                identifier++;

                while (getProcessByID(identifier) != null) {
                    identifier++;

                    if (identifier < 0) {
                        identifier = 1;
                    }
                }
            }
        }

        newQueue.removeAll(addedProcesses);
    }

    public Process nextReady() {
        if (readyQueue.isEmpty()) {
            return null;
        }

        Process process = this.readyQueue.remove(0);
        this.readyQueue.add(process);
        return process;
    }

    public Process nextIO() {
        if (ioQueue.isEmpty()) {
            return null;
        }

        return this.ioQueue.remove(0);
    }

    public void insertPCB(ProcessData data) {
        if (data.memory > OperatingSystem.MEMORY_SIZE) {
            return;
        }

        int i = 0;

        while (i < newQueue.size()) {
            if (data.startTime < newQueue.get(i).startTime) {
                break;
            }
            i++;
        }

        newQueue.add(i, data);
    }

    public void remove(Process process) {
        readyQueue.remove(process);
    }

    public int getNumberOfProcesses() {
        return newQueue.size() + readyQueue.size();
    }

    public Process getProcessByID(int id) {
        for (Process process : readyQueue) {
            if (process.registers[OperatingSystem.PROCESS_ID_REGISTER] == id) {
                return process;
            }
        }

        for (Process process : ioQueue) {
            if (process.registers[OperatingSystem.PROCESS_ID_REGISTER] == id) {
                return process;
            }
        }

        return null;
    }
}

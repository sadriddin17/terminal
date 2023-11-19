package uz.agrobank.terminal.service.impl;

import org.springframework.stereotype.Service;
import uz.agrobank.terminal.service.TerminalService;

import java.io.IOException;

@Service
public class TerminalServiceImp implements TerminalService {

    @Override
    public void purchase(Long amount) {
        try {
            // Command to be executed
            String command = "Terminal.exe /o1 /a100 /c000";

            // Use ProcessBuilder to execute the command
            ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
            Process process = processBuilder.start();

            // Wait for the process to complete
            int exitCode = process.waitFor();

            // Check the exit code
            if (exitCode == 0) {
                System.out.println("Command executed successfully");
            } else {
                System.err.println("Error executing the command. Exit code: " + exitCode);
            }

        } catch (IOException | InterruptedException e) {
            System.out.print("error");
        }
    }
}

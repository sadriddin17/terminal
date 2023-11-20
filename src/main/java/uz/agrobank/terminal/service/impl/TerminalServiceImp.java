package uz.agrobank.terminal.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uz.agrobank.terminal.service.TerminalService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TerminalServiceImp implements TerminalService {

    @Value("${terminal.exe.path}")
    private String exePath;

    @Value("${terminal.checkout-file.path}")
    private String checkoutFilePath;

    @Override
    public String purchase(Long amount) {
        String[] parameters = {"/o1", "/a".concat(String.valueOf(amount)), "/c000"};

        List<String> command = new ArrayList<>();
        command.add(exePath);
        command.addAll(Arrays.asList(parameters));

        execute(command);
        return extractRRNFromCheckout();
    }

    @Override
    public void cancelLast() {
        String[] parameters = {"/o2"};

        List<String> command = new ArrayList<>();
        command.add(exePath);
        command.addAll(Arrays.asList(parameters));

        execute(command);
    }

    private void execute(List<String> command) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command(command);

            Process process = processBuilder.start();

            // Wait for the process to complete (optional)
            int exitCode = process.waitFor();
            System.out.println("Exited with code " + exitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

//    private String extractRRNFromLog() {
//
//        File logDirectory = new File(logDirectoryPath);
//        File[] logFiles = logDirectory.listFiles();
//
//        // Check if logFiles is not null and has at least one file
//        if (logFiles != null && logFiles.length > 0) {
//            // Sort the files based on the last modified timestamp
//            Arrays.sort(logFiles, Comparator.comparingLong(File::lastModified).reversed());
//
//            File lastLogFile = logFiles[0];
//            System.out.println("Last log file: " + lastLogFile.getAbsolutePath());
//
//            // Use try-with-resources to automatically close the BufferedReader
//            try (BufferedReader reader = new BufferedReader(new FileReader(lastLogFile))) {
//                String lastRRN = reader.lines().reduce((first, second) -> second).orElse(null);
//
//                if (lastRRN != null) {
//                    // Extract the RRN field using the appropriate logic
//                    int startIndex = Math.max(0, lastRRN.length() - 10); // Assuming RRN is 10 characters wide
//                    String rrnField = lastRRN.substring(startIndex);
//                    System.out.println("Last RRN field: " + rrnField);
//                    return rrnField;
//                } else {
//                    System.out.println("Log file is empty");
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        } else {
//            System.out.println("No log files found in the directory");
//        }
//
//        return null;
//    }

    private String extractRRNFromCheckout(){
        String checkoutData = readFromFile(checkoutFilePath);
        String rrn = "";

        if (checkoutData != null) {
            rrn = extractRRN(checkoutData);
            if (rrn != null) {
                System.out.println("Extracted RRN: " + rrn);
            } else {
                System.out.println("RRN not found in the file.");
            }
        } else {
            System.out.println("Failed to read data from the file.");
        }
        return rrn;
    }

    private String readFromFile(String filePath) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            return content.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String extractRRN(String checkoutData) {
        // Define a regex pattern to match the RRN line
        String regex = "RRN\\s+([0-9]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(checkoutData);

        // Find the first match
        if (matcher.find()) {
            // Group 1 contains the RRN value
            return matcher.group(1);
        } else {
            // Return null if no match is found
            return null;
        }
    }
}

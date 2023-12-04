package uz.agrobank.terminal.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uz.agrobank.terminal.dto.PurchaseDto;
import uz.agrobank.terminal.enums.TerminalType;
import uz.agrobank.terminal.service.TerminalService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
    public PurchaseDto purchase(Long amount, TerminalType type) {
        String[] parameters = {"/o1", "/a".concat(String.valueOf(amount)), "/c000"};

        List<String> command = new ArrayList<>();
        command.add(exePath);
        command.addAll(Arrays.asList(parameters));

        execute(command);
        return new PurchaseDto(extractRRNFromCheckout(type));
    }

    @Override
    public void cancelLast() {
        String[] parameters = {"/o2"};

        List<String> command = new ArrayList<>();
        command.add(exePath);
        command.addAll(Arrays.asList(parameters));

        execute(command);
    }

    @Override
    public void sendDailyDetails() {
        String[] parameters = {"/o65"};

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
            cancelLast();
            throw new RuntimeException("command.execution.failed");
        }
    }

    private String extractRRNFromCheckout(TerminalType type){
        String checkoutData = readFromFile(checkoutFilePath);
        String rrn;

        if (checkoutData != null) {
            rrn = switch (type){
                case HUMO -> extractHumoRRN(checkoutData);
                case UZCARD -> extractUzcardRRN(checkoutData);
            };
            if (rrn != null) {
                System.out.println("Extracted RRN: " + rrn);
            } else {
                cancelLast();
                throw new RuntimeException("transaction.failed");
            }
        } else {
            cancelLast();
            throw new RuntimeException("transaction.failed");
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
            return null;
        }
    }

    private String extractUzcardRRN(String checkoutData) {
        // Define a regex pattern to match the RRN line
        String regex = "RRN\\s+(\\d+)";
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

    private String extractHumoRRN(String checkoutData) {
        // Define a regex pattern to match the RRN line
        String regex = "RRN:\\s+(\\d+)";
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

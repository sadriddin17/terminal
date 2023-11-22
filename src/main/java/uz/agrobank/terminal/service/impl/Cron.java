package uz.agrobank.terminal.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import uz.agrobank.terminal.config.Constants;
import uz.agrobank.terminal.service.TerminalService;

@Component
public class Cron {
    public final TerminalService terminalService;

    @Value("${terminal.zone}")
    private String checkoutFilePath;

    public Cron(TerminalService terminalService) {
        this.terminalService = terminalService;
    }

    @Scheduled(cron = "0 0 9,17 * * ?", zone = Constants.ZONE)
    public void triggerProductsUpdate() {
        terminalService.sendDailyDetails();
    }
}

package uz.agrobank.terminal.service.impl;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import uz.agrobank.terminal.config.Constants;
import uz.agrobank.terminal.service.TerminalService;

@Component
public class Cron {
    public final TerminalService terminalService;

    public Cron(TerminalService terminalService) {
        this.terminalService = terminalService;
    }

    @Scheduled(cron = "0 0 9,16 * * ?", zone = Constants.ZONE)
    public void triggerProductsUpdate() {
        terminalService.sendDailyDetails();
    }
}

package uz.agrobank.terminal.service;

import uz.agrobank.terminal.dto.PurchaseDto;
import uz.agrobank.terminal.enums.TerminalType;

public interface TerminalService {
    PurchaseDto purchase(Long amount, TerminalType type);
    void cancelLast();
    void sendDailyDetails();
}

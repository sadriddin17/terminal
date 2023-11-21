package uz.agrobank.terminal.service;

import uz.agrobank.terminal.enums.TerminalType;

public interface TerminalService {
    String purchase(Long amount, TerminalType type);
    void cancelLast();
}

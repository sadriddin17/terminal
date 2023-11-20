package uz.agrobank.terminal.service;

public interface TerminalService {
    String purchase(Long amount);
    void cancelLast();
}

package uz.agrobank.terminal.controller;

import org.springframework.web.bind.annotation.*;
import uz.agrobank.terminal.service.TerminalService;

@RestController
@RequestMapping("v1")
public class Terminal {

    private final TerminalService terminalService;

    public Terminal(TerminalService terminalService) {
        this.terminalService = terminalService;
    }

    @GetMapping("purchase")
    public void purchase(@RequestParam Long amount){
        terminalService.purchase(amount);
    }
}

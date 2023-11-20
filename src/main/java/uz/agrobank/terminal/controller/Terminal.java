package uz.agrobank.terminal.controller;

import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> purchase(@RequestParam Long amount){
        return ResponseEntity.ok(terminalService.purchase(amount));
    }
    @GetMapping("cancel-last")
    public void cancelLast(){
        terminalService.cancelLast();
    }
}

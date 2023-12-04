package uz.agrobank.terminal.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.agrobank.terminal.dto.PurchaseDto;
import uz.agrobank.terminal.enums.TerminalType;
import uz.agrobank.terminal.service.TerminalService;

@RestController
@RequestMapping("v1")
public class Terminal {

    private final TerminalService terminalService;

    public Terminal(TerminalService terminalService) {
        this.terminalService = terminalService;
    }

    @GetMapping("purchase")
    public ResponseEntity<PurchaseDto> purchase(@RequestParam Long amount, TerminalType type){
        return ResponseEntity.ok(terminalService.purchase(amount, type));
    }
//    @GetMapping("cancel-last")
//    public void cancelLast(){
//        terminalService.cancelLast();
//    }
}

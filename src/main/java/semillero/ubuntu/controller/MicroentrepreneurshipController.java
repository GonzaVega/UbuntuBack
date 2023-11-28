package semillero.ubuntu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import semillero.ubuntu.entities.Microentrepreneurship;
import semillero.ubuntu.service.contract.MicroentrepreneurshipService;

@RestController
@RequestMapping("/api/v1/")
public class MicroentrepreneurshipController {
    private final MicroentrepreneurshipService microentrepreneurshipService;

    @Autowired
    public MicroentrepreneurshipController(MicroentrepreneurshipService microentrepreneurshipService) {
        this.microentrepreneurshipService = microentrepreneurshipService;
    }

    @GetMapping("/microentrepreneurship/{id}")
    public ResponseEntity<Microentrepreneurship> getMicroentrepreneurshipById(@PathVariable Long id) {
        Microentrepreneurship microentrepreneurship = microentrepreneurshipService.getMicroentrepreneurshipById(id);
        return new ResponseEntity<>(microentrepreneurship, HttpStatus.OK);
    }

    @PostMapping("/microentrepreneurship")
    public ResponseEntity<Microentrepreneurship> createMicroentrepreneurship(
            @RequestBody Microentrepreneurship microentrepreneurship) {
        Microentrepreneurship createdMicroentrepreneurship = microentrepreneurshipService.createMicroentrepreneurship(microentrepreneurship);
        return new ResponseEntity<>(createdMicroentrepreneurship, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Microentrepreneurship> editMicroentrepreneurship(
            @PathVariable Long id) {
        Microentrepreneurship editedMicroentrepreneurship = microentrepreneurshipService.editMicroentrepreneurship(id);
        return new ResponseEntity<>(editedMicroentrepreneurship, HttpStatus.OK);
    }



}

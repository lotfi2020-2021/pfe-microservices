package esprit.tn.controller;

import esprit.tn.entity.Demande;
import esprit.tn.service.DemandeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/demande")
@RequiredArgsConstructor
public class DemandeController {

    private final DemandeService demandeService;

    @PostMapping("/create")
    public ResponseEntity<?> createDemande(@RequestBody @Valid Demande demande) {
        Demande demande1 = demandeService.AddDemande(demande);
        return new ResponseEntity<>(demande1, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> createDemande(@PathVariable Long id ) {
        Demande demande1 = demandeService.updateDemande(id) ;
        return new ResponseEntity<>(demande1, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDemande (@PathVariable Long id ) {
        demandeService.deleteDemande(id);
        return new ResponseEntity<>( HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity<?> detAllDemandes ( ) {
      List<Demande> list =  demandeService.getAllDemandes();
        return new ResponseEntity<>( list ,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> GetDemandeById (@PathVariable Long id ) {
      Demande  demande =  demandeService.getDemandeById(id);
        return new ResponseEntity<>(demande , HttpStatus.OK);
    }


}

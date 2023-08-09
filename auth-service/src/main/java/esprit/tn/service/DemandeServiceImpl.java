package esprit.tn.service;


import esprit.tn.common.AppConstants;
import esprit.tn.entity.Demande;
import esprit.tn.repository.DemandeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class DemandeServiceImpl  implements DemandeService{

    @Autowired
    DemandeRepository demandeRepository ;

     @Autowired EmailService emailService ;

    @Override
    public Demande AddDemande(Demande demande) {

        Demande demande1 =new Demande();
        demande1.setEmail(demande.getEmail());
        demande1.setFirstName(demande.getFirstName());
        demande1.setLastName(demande.getLastName());
        demande1.setTelephone(demande.getTelephone());
        demande1.setMessage(demande.getMessage());
        demande1.setCreatedAt(new Date());
        demande1.setValid(false);
        demandeRepository.save(demande1);
        String demandeMail =
                emailService.buildDemandeBody();

        String demandeEmailAdmin =  emailService.buildDemandeAdminBody();
        emailService.send(demande.getEmail() , AppConstants.DEMAND_ENTREPRISE, demandeMail);

        emailService.send("lotfi.hmida@esprit.tn",AppConstants.DEMAND_ENTREPRISE ,demandeEmailAdmin);
        return demande1 ;
    }

    @Override
    public void deleteDemande(Long id) {
        Optional<Demande> existingDemande = demandeRepository.findById(id);
        existingDemande.ifPresent(demandeRepository::delete);

    }

    @Override
    public List<Demande> getAllDemandes() {
      return  demandeRepository.findAll() ;
    }

    @Override
    public Demande getDemandeById(Long id) {
      Demande demande =  demandeRepository.findById(id).get();
      return demande ;
    }

    @Override
    public Demande updateDemande(Long id) {
        Demande demande1 =  demandeRepository.findById(id).get();
        demande1.setValid(true);
     return   demandeRepository.save(demande1);
    }
}

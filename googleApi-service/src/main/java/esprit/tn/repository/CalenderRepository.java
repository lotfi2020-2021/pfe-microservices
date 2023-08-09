package esprit.tn.repository;

import esprit.tn.entity.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalenderRepository   extends JpaRepository<Calendar, Long> {


    Calendar findByGoogleCalenderId(String id);


}

package esprit.tn.repository;


import esprit.tn.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository  extends JpaRepository<Event, Long> {

 Event findByGoogleEventId(String eventId);


}

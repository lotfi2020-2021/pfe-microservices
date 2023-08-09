package esprit.tn.controller;


import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;

import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.*;



import esprit.tn.repository.CalenderRepository;
import esprit.tn.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import com.google.api.client.json.jackson2.JacksonFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;


import java.util.*;

@RequestMapping(value = "/api/Oauth2")
@RestController
@RequiredArgsConstructor
public class GoogleOauth2Controller {

    private final static Log logger = LogFactory.getLog(GoogleOauth2Controller.class);
    private static final String APPLICATION_NAME = "Client web 1";
    private static HttpTransport httpTransport;
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static com.google.api.services.calendar.Calendar client;

    GoogleClientSecrets clientSecrets;
    GoogleAuthorizationCodeFlow flow;
   Credential credential;


    @Autowired
    EventRepository eventRepository ;



    @Autowired
    private CalenderRepository calenderRepository;
    @Value("${google.client.client-id}")
    private String clientId;
    @Value("${google.client.client-secret}")
    private String clientSecret;
    @Value("${google.client.redirectUri}")
    private String redirectURI;


    @RequestMapping(value = "/login/google", method = RequestMethod.GET)
    public RedirectView googleConnectionStatus(HttpServletRequest request) throws Exception {
        return new RedirectView(authorize());
    }

    @RequestMapping(value = "/login/google", method = RequestMethod.GET, params = "code")
    public ResponseEntity<?> oauth2Callback(@RequestParam(value = "code") String code ,  HttpServletResponse response ) throws IOException {
        try {
            // Exchange the authorization code for an access token and refresh token
            TokenResponse tokenResponse = flow.newTokenRequest(code).setRedirectUri(redirectURI).execute();

            // Get the authenticated user's email
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String userEmail = auth.getName();
            System.out.println("email"+ userEmail);
            // Create a credential object with the access token and refresh token
            credential = flow.createAndStoreCredential(tokenResponse, userEmail);
            String refreshToken = credential.getRefreshToken();

            // Store the refresh token securely (e.g. database, file)
            storeRefreshToken(userEmail,refreshToken);

            // Use the credential to access Google APIs
            client = new com.google.api.services.calendar.Calendar.Builder(
                    httpTransport, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME)
                    .build();
            response.sendRedirect("http://localhost:4200/calender");

            return new ResponseEntity<>(tokenResponse, HttpStatus.OK);
        } catch (TokenResponseException e) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String userEmail = auth.getName();

            // If the authorization code has already been used or expired, try to use the refresh token
            if (e.getDetails().getError().equals("invalid_grant")) {
                String refreshToken = loadRefreshToken(userEmail) ;  ; // Load the stored refresh token
                credential = new GoogleCredential.Builder()
                        .setTransport(httpTransport)
                        .setJsonFactory(JSON_FACTORY)
                        .setClientSecrets(clientId, clientSecret)
                        .build();
                credential.setRefreshToken(refreshToken);

                // Use the credential to access Google APIs
                client = new com.google.api.services.calendar.Calendar.Builder(
                        httpTransport, JSON_FACTORY, credential)
                        .setApplicationName(APPLICATION_NAME)
                        .build();

                return new ResponseEntity<>(refreshToken,HttpStatus.OK);
            }
            response.sendRedirect("http://localhost:8084/api/Oauth2/login/google");
            return new ResponseEntity<>("refreshtoken valid" , HttpStatus.OK);
        } catch (Exception e) {
            response.sendRedirect("http://localhost:8084/api/Oauth2/login/google");

            return new ResponseEntity<>("refreshTokenExpired", HttpStatus.OK);
        }
    }


    private void storeRefreshToken(String userEmail, String refreshToken) {
        // Store the refresh token securely, associated with the user's email
        // Here's an example of storing it in a file:
        try {
            FileWriter writer = new FileWriter("C:\\Users\\lotfi\\Desktop\\pfe-stage\\spring\\stage\\src\\main\\java\\esprit\\tn\\util\\refresh_token.txt", true);
            writer.write(userEmail + ":" + refreshToken + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String loadRefreshToken(String userEmail) {
        // Load the stored refresh token associated with the user's email
        // Here's an example of loading it from a file:
        String refreshToken = "";
        try {
            File file = new File("C:\\Users\\lotfi\\Desktop\\pfe-stage\\spring\\stage\\src\\main\\java\\esprit\\tn\\util\\refresh_token.txt");
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(":");
                if (parts[0].equals(userEmail)) {
                    refreshToken = parts[1];
                    break;
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return refreshToken;
    }

    private String authorize() throws Exception {
        AuthorizationCodeRequestUrl authorizationUrl;
        if (flow == null) {
            GoogleClientSecrets.Details web = new GoogleClientSecrets.Details();
            web.setClientId(clientId);
            web.setClientSecret(clientSecret);

            clientSecrets = new GoogleClientSecrets().setWeb(web);
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets, Arrays.asList(CalendarScopes.CALENDAR, "https://www.googleapis.com/auth/userinfo.email", "https://www.googleapis.com/auth/userinfo.profile")) .setAccessType("offline").build();

        }
        authorizationUrl = flow.newAuthorizationUrl().setRedirectUri(redirectURI);
        System.out.println("cal authorizationUrl->" + authorizationUrl);
        return authorizationUrl.build();
    }


    @PostMapping("/Calender/create")
    public ResponseEntity<?> createCalendar(  @RequestBody  esprit.tn.entity.Calendar calender) {
       com.google.api.services.calendar.model.Calendar calendar2 ;
        esprit.tn.entity.Calendar calenderEntity ;
        try {
            Calendar service = new Calendar.Builder(httpTransport, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME).build();
            com.google.api.services.calendar.model.Calendar calendar = new com.google.api.services.calendar.model.Calendar();
            TimeZone timeZone = TimeZone.getDefault();
            Locale locale = Locale.getDefault();

            calendar.setTimeZone(timeZone.getID()) ;
           // calendar.setLocation(locale.getDisplayName());
            calendar.setSummary(calender.getSummary());
            calendar.setLocation(locale.getDisplayName());
            calendar.setDescription(calender.getDescription());

            calendar2 = service.calendars().insert(calendar).execute();

            calender.setTimeZone(timeZone.getID()) ;
            calender.setLocation(locale.getDisplayName()) ;
            calender.setGoogleCalenderId(calendar2.getId());
         calenderEntity =    calenderRepository.save(calender) ;



        } catch (Exception e) {
            logger.error("Error creating calendar: " + e.getMessage());
            return new ResponseEntity<>("Error creating calendar", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(calenderEntity, HttpStatus.OK);
    }



    @PutMapping("/Calender/update/{calendarId}")
    public ResponseEntity<String> updateCalendar(@PathVariable String calendarId, @RequestBody esprit.tn.entity.Calendar calendar) {
        try {
            com.google.api.services.calendar.Calendar service = new Calendar.Builder(httpTransport, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME).build();

            com.google.api.services.calendar.model.Calendar calendarToUpdate = service.calendars().get(calendarId).execute();
            TimeZone timeZone = TimeZone.getDefault();
            Locale locale = Locale.getDefault();
            calendarToUpdate.setTimeZone(timeZone.getID());
            calendarToUpdate.setSummary(calendar.getSummary());
           // calendarToUpdate.setLocation(locale);
            calendarToUpdate.setDescription(calendar.getDescription());

            esprit.tn.entity.Calendar calendar2 = calenderRepository.findByGoogleCalenderId(calendarId) ;

            calendar2.setSummary(calendar.getSummary());
            calendar2.setDescription(calendar.getDescription());
            calenderRepository.save(calendar2);
            service.calendars().update(calendarId, calendarToUpdate).execute();

            return new ResponseEntity<>("Calendar updated ", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error updating calendar: " + e.getMessage());
            return new ResponseEntity<>("Error updating calendar", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping(value = "/Calender/delete/{calendarId}" )
    public ResponseEntity<String> deleteCalendar(@PathVariable String calendarId) {
        try {
            com.google.api.services.calendar.Calendar service = new Calendar.Builder(httpTransport, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME).build();

        esprit.tn.entity.Calendar calender = calenderRepository.findByGoogleCalenderId(calendarId) ;

            calenderRepository.delete(calender);
            service.calendars().delete(calendarId).execute();

            return new ResponseEntity<>( HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error deleting calendar: " + e.getMessage());
            return new ResponseEntity<>("Error deleting calendar", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/Calender/{calendarId}")
    public ResponseEntity<?> getCalendarById(@PathVariable String calendarId) {
        try {
            com.google.api.services.calendar.Calendar service = new Calendar.Builder(httpTransport, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME).build();

            esprit.tn.entity.Calendar  calender = calenderRepository.findByGoogleCalenderId(calendarId) ;
            com.google.api.services.calendar.model.Calendar calendar = service.calendars().get(calender.getGoogleCalenderId()).execute();

            return new ResponseEntity<>(calendar, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error retrieving calendar: " + e.getMessage());
            return new ResponseEntity<>("Error retrieving calendar", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/Calenders")
    public ResponseEntity<?> getAllCalendars() {
        try {
            com.google.api.services.calendar.Calendar service = new Calendar.Builder(httpTransport, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME).build();

            com.google.api.services.calendar.model.CalendarList calendarList = service.calendarList().list().execute();

            return new ResponseEntity<>(calendarList.getItems(), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error retrieving calendars: " + e.getMessage());
            return new ResponseEntity<>("Error retrieving calendars", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }





    @GetMapping("/allColors")
    public ResponseEntity<Map<String, ColorDefinition>> getCalendarColors() throws IOException {
        Colors colors = client.colors().get().execute();
        return new ResponseEntity<>(colors.getCalendar(), HttpStatus.OK);
    }

    @GetMapping("/events/colors")
    public ResponseEntity<Map<String, ColorDefinition>> getEventColors() throws IOException {
        Colors colors = client.colors().get().execute();
        return new ResponseEntity<>(colors.getEvent(), HttpStatus.OK);
    }

    @PostMapping("/events/create/{calendarId}")
    public ResponseEntity<?> createEvent(@RequestBody esprit.tn.entity.Event requestBody , @PathVariable String calendarId  ) throws IOException {


        TimeZone timeZone = TimeZone.getDefault();

        DateTime endDateTime = new DateTime(requestBody.getEndDateTime());
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone( timeZone.getID());

        DateTime startDateTime = new DateTime(requestBody.getStartDateTime());
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone( timeZone.getID());



        EventAttendee[] attendees = new EventAttendee[requestBody.getAttendees().length];
        for (int i = 0; i < requestBody.getAttendees().length; i++) {
            attendees[i] = new EventAttendee().setEmail(requestBody.getAttendees()[i]);
        }


//        // Add reminder overrides for conference event
//        EventReminder[] reminderOverrides = new EventReminder[requestBody.getReminderMethods().length];
//        for (int i = 0; i < requestBody.getReminderMethods().length; i++) {
//            reminderOverrides[i] = new EventReminder()
//                    .setMethod(requestBody.getReminderMethods()[i])
//                    .setMinutes(requestBody.getReminderMinutes()[i]);
//        }
//        Event.Reminders reminders = new Event.Reminders()
//                .setUseDefault(false)
//                .setOverrides(Arrays.asList(reminderOverrides));
                EventReminder[] reminderOverrides = new EventReminder[]{
                new EventReminder().setMethod("email").setMinutes(24 * 60),
                new EventReminder().setMethod("popup").setMinutes(10),
        };
        Event.Reminders reminders = new Event.Reminders()
                .setUseDefault(false)
                .setOverrides(Arrays.asList(reminderOverrides));



        Event event = new Event()
                .setSummary(requestBody.getSummary())
                //.setLocation(requestBody.getLocation())
                .setDescription(requestBody.getDescription())
                .setColorId(requestBody.getColorId())
                //.setTransparency(requestBody.getTransparency())
                .setVisibility(requestBody.getVisibility())
                .setGuestsCanSeeOtherGuests(requestBody.getGuestsCanSeeOtherGuests())
                .setGuestsCanInviteOthers(requestBody.getGuestsCanInviteOthers())
               //.setStatus(requestBody.getStatus())
                .setGuestsCanModify(requestBody.getGuestsCanModify())
               // .setPrivateCopy(requestBody.getPrivateCopy())
               // .setLocked(requestBody.getLocked())
                .setStart(start)
                .setEnd(end)
                .setAttendees(Arrays.asList(attendees))
                .setReminders(reminders);
              //  .setAnyoneCanAddSelf(requestBody.getAnyoneCanAddSelf()) ;
        if (requestBody.getIsAddVideoConference() == true) {
            String requestId = UUID.randomUUID().toString();
            ConferenceData conferenceData = new ConferenceData();
            conferenceData.setCreateRequest(new CreateConferenceRequest().setRequestId(requestId));
            event.setConferenceData(conferenceData);
            Event createdEvent =   client.events().insert(calendarId, event).setConferenceDataVersion(1).execute();

          esprit.tn.entity.Calendar calender = calenderRepository.findByGoogleCalenderId(calendarId);

            requestBody.setMeetUrl(createdEvent.getHangoutLink());
            requestBody.setGoogleEventId(createdEvent.getId());
            requestBody.setGoogleCalenderId(createdEvent.getOrganizer().getEmail());
            requestBody.setCreator(createdEvent.getCreator().getEmail());
            requestBody.setCalender(calender);
            eventRepository.save(requestBody);
            List<String> emailList = Arrays.asList(requestBody.getAttendees());


            return new ResponseEntity<>(createdEvent, HttpStatus.OK);

        }else{
            Event createdEvent =   client.events().insert(calendarId, event).setConferenceDataVersion(1).execute();
            requestBody.setGoogleCalenderId(createdEvent.getOrganizer().getEmail());
            requestBody.setGoogleEventId(createdEvent.getId());
            eventRepository.save(requestBody) ;

            List<String> emailList = Arrays.asList(requestBody.getAttendees());





            return new ResponseEntity<>(createdEvent, HttpStatus.OK);

        }
    }







    @PutMapping("/events/update/{eventId}")
    public ResponseEntity<?> updateEvent(@PathVariable String eventId, @RequestBody esprit.tn.entity.Event requestBody) throws IOException {

        esprit.tn.entity.Event eventbase = eventRepository.findByGoogleEventId(eventId) ;

        Event eventToUpdate = client.events().get( eventbase.getGoogleCalenderId(), eventId).execute();

        DateTime endDateTime = new DateTime(requestBody.getEndDateTime());
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone(requestBody.getTimeZone());

        DateTime startDateTime = new DateTime(requestBody.getStartDateTime());
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone(requestBody.getTimeZone());

        EventAttendee[] attendees = new EventAttendee[requestBody.getAttendees().length];
        for (int i = 0; i < requestBody.getAttendees().length; i++) {
            attendees[i] = new EventAttendee().setEmail(requestBody.getAttendees()[i]);
        }

        EventReminder[] reminderOverrides = new EventReminder[]{
                new EventReminder().setMethod("email").setMinutes(24 * 60),
                new EventReminder().setMethod("popup").setMinutes(10),
        };
        Event.Reminders reminders = new Event.Reminders()
                .setUseDefault(false)
                .setOverrides(Arrays.asList(reminderOverrides));

        eventToUpdate.setSummary(requestBody.getSummary())
              //  .setLocation(requestBody.getLocation())
                .setDescription(requestBody.getDescription())
                .setColorId(requestBody.getColorId())
                // .setTransparency(requestBody.getTransparency())
                .setVisibility(requestBody.getVisibility())
                .setGuestsCanSeeOtherGuests(requestBody.getGuestsCanSeeOtherGuests())
                .setGuestsCanInviteOthers(requestBody.getGuestsCanInviteOthers())
                // .setStatus(requestBody.getStatus())
                .setGuestsCanModify(requestBody.getGuestsCanModify())
                // .setPrivateCopy(requestBody.getPrivateCopy())
                //.setLocked(requestBody.getLocked())
                .setStart(start)
                .setEnd(end)
                .setAttendees(Arrays.asList(attendees))
                .setReminders(reminders);
        // .setAnyoneCanAddSelf(requestBody.getAnyoneCanAddSelf()) ;

        if (requestBody.getIsAddVideoConference() == true) {
            String requestId = UUID.randomUUID().toString();
            ConferenceData conferenceData = new ConferenceData();
            conferenceData.setCreateRequest(new CreateConferenceRequest().setRequestId(requestId));
            eventToUpdate.setConferenceData(conferenceData);
            Event updatedEvent = client.events().update(eventbase.getGoogleCalenderId(), eventId, eventToUpdate).setConferenceDataVersion(1).execute();



            eventbase.setMeetUrl(updatedEvent.getHangoutLink());
            eventbase.setSummary(requestBody.getSummary());
            eventbase   .setDescription(requestBody.getDescription());
            eventbase .setColorId(requestBody.getColorId());
            eventbase .setIsAddVideoConference(requestBody.getIsAddVideoConference());

            eventbase  .setVisibility(requestBody.getVisibility());
            eventbase  .setGuestsCanSeeOtherGuests(requestBody.getGuestsCanSeeOtherGuests());
            eventbase   .setGuestsCanInviteOthers(requestBody.getGuestsCanInviteOthers());

            eventbase   .setGuestsCanModify(requestBody.getGuestsCanModify()) ;
            eventbase .setStartDateTime(requestBody.getStartDateTime());
            eventbase .setEndDateTime(requestBody.getEndDateTime());
            eventbase .setAttendees(requestBody.getAttendees()) ;
            eventRepository.save(eventbase) ;

            return new ResponseEntity<>(updatedEvent, HttpStatus.OK);
        } else{

            Event updatedEvent = client.events().update(eventbase.getGoogleCalenderId(), eventId, eventToUpdate).setConferenceDataVersion(1).execute();

            eventbase .setIsAddVideoConference(requestBody.getIsAddVideoConference());
            eventbase.setSummary(requestBody.getSummary());
            eventbase   .setDescription(requestBody.getDescription());
            eventbase .setColorId(requestBody.getColorId());

            eventbase  .setVisibility(requestBody.getVisibility());
            eventbase  .setGuestsCanSeeOtherGuests(requestBody.getGuestsCanSeeOtherGuests());
            eventbase   .setGuestsCanInviteOthers(requestBody.getGuestsCanInviteOthers());

            eventbase   .setGuestsCanModify(requestBody.getGuestsCanModify()) ;
            eventbase .setStartDateTime(requestBody.getStartDateTime());
            eventbase .setEndDateTime(requestBody.getEndDateTime());
            eventbase .setAttendees(requestBody.getAttendees()) ;
            eventRepository.save(eventbase) ;

        return new ResponseEntity<>(updatedEvent, HttpStatus.OK);


    }}

    @DeleteMapping("/events/delete/{eventId}")
    public ResponseEntity<String> deleteEvent(@PathVariable String eventId) {
        try {

            esprit.tn.entity.Event event = eventRepository.findByGoogleEventId(eventId) ;
            eventRepository.delete(event);
            client.events().delete(event.getGoogleCalenderId(), eventId).execute();
            return new ResponseEntity<>( HttpStatus.OK);
        } catch (IOException e) {
            logger.error("Error deleting event: " + e.getMessage());
            return new ResponseEntity<>("Error deleting event", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/events/{eventId}")
    public ResponseEntity<?> getEventById(@PathVariable String eventId) {
        try {
            esprit.tn.entity.Event eventBase = eventRepository.findByGoogleEventId(eventId) ;
            Event event = client.events().get(eventBase.getGoogleCalenderId(), eventId).execute();
            return new ResponseEntity<>(event, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error getting event: " + e.getMessage());
            return new ResponseEntity<>("Error getting event", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/eventsBase/{eventId}")
    public ResponseEntity<?> getEventBaseById(@PathVariable String eventId) {
        try {

            esprit.tn.entity.Event event =eventRepository.findByGoogleEventId(eventId) ;
            return new ResponseEntity<>(event, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error getting event: " + e.getMessage());
            return new ResponseEntity<>("Error getting event", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/events")
    public ResponseEntity<?> getAllEvents() {
        List<Event> events;
        try {
            Calendar service = new Calendar.Builder(httpTransport, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME).build();
            events = service.events().list("primary").execute().getItems();                 /** jareb be id ta3 calendrier o5ra **/
        } catch (Exception e) {
            logger.error("Error getting events: " + e.getMessage());
            return new ResponseEntity<>("Error getting events", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @GetMapping("/events/Calender/{calendarId}")
    public ResponseEntity<?> getEventsByCalendarId(@PathVariable String calendarId) {
        List<Event> events;
        try {
            Calendar service = new Calendar.Builder(httpTransport, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME).build();
            events = service.events().list(calendarId).execute().getItems();
        } catch (Exception e) {
            logger.error("Error getting events: " + e.getMessage());
            return new ResponseEntity<>("Error getting events", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(events, HttpStatus.OK);
    }




}

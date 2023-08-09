package esprit.tn.client;

import esprit.tn.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
@FeignClient(name = "auth-service"  , url = "localhost:8080")
public interface ComunicatinClient {

    @GetMapping("/api/v1/User/emailid")
    Long getUserByEmail2(@RequestParam("email") @Valid String email);
}

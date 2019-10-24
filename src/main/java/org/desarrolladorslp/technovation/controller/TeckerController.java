package org.desarrolladorslp.technovation.controller;

import org.desarrolladorslp.technovation.config.auth.TokenInfoService;
import org.desarrolladorslp.technovation.dto.DeliverableByTeckerDTO;
import org.desarrolladorslp.technovation.services.TeckerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/tecker")
public class TeckerController {

    private TeckerService teckerService;

    private TokenInfoService tokenInfoService;

    @Secured({"ROLE_TECKER"})
    @GetMapping
    public ResponseEntity<List<DeliverableByTeckerDTO>> listDeliverable(Principal principal) {
        List<DeliverableByTeckerDTO> deliverables = teckerService.getDeliverableByTecker(tokenInfoService.getIdFromPrincipal(principal));
        return new ResponseEntity<>(deliverables, HttpStatus.OK);
    }

    @Secured({"ROLE_PARENT"})
    @GetMapping("{teckerId}/deliverables")
    public ResponseEntity<List<DeliverableByTeckerDTO>> listDeliverableByKid(@PathVariable UUID teckerId, Principal principal) {
        List<DeliverableByTeckerDTO> deliverablesByKid = teckerService.getDeliverableByKid(teckerId, tokenInfoService.getIdFromPrincipal(principal));
        return new ResponseEntity<>(deliverablesByKid, HttpStatus.OK);
    }

    @Autowired
    public void setTeckerService(TeckerService teckerService) {
        this.teckerService = teckerService;
    }

    @Autowired
    public void setTokenInfoService(TokenInfoService tokenInfoService) {
        this.tokenInfoService = tokenInfoService;
    }

}

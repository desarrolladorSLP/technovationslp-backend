package org.desarrolladorslp.technovation.controller;

import org.desarrolladorslp.technovation.config.auth.TokenInfoService;
import org.desarrolladorslp.technovation.dto.TeckerDTO;
import org.desarrolladorslp.technovation.dto.TeckersToParentDTO;
import org.desarrolladorslp.technovation.services.ParentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/parent")
public class ParentController {

    private ParentService parentService;
    private TokenInfoService tokenInfoService;


    @PutMapping("/{parentId}")
    public ResponseEntity<HttpStatus> assignTeckersToParent(@PathVariable UUID parentId, @RequestBody TeckersToParentDTO teckers) {
        parentService.assignToParent(parentId, teckers.getTeckersId());
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping("/{parentId}")
    public ResponseEntity<List<TeckerDTO>> getTeckersByParent(@PathVariable UUID parentId) {

        return new ResponseEntity<>(parentService.teckersByParent(parentId), HttpStatus.OK);
    }

    @Secured({"ROLE_PARENT"})
    @GetMapping("/teckers")
    public ResponseEntity<List<TeckerDTO>> getTeckers(Principal principal) {

        return new ResponseEntity<>(parentService.teckerByParentLogged(tokenInfoService.getIdFromPrincipal(principal)), HttpStatus.OK);

    }

    @Autowired
    public void setParentService(ParentService parentService) {
        this.parentService = parentService;
    }

    @Autowired
    public void setTokenInfoService(TokenInfoService tokenInfoService) {
        this.tokenInfoService = tokenInfoService;
    }

}

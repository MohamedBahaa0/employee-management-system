package com.logincrud.service;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KeycloakService {
    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;

    public void createUser( String email, String password) {
        // build the user representation
        UserRepresentation user = new UserRepresentation();
        user.setUsername(email);
        user.setEmail(email);
        user.setEnabled(true);
        user.setEmailVerified(true);

        // set password
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);
        user.setCredentials(List.of(credential));

        // create user in Keycloak
        RealmResource realmResource = keycloak.realm(realm);
        Response response = realmResource.users().create(user);

        if (response.getStatus() != 201) {
            throw new RuntimeException("Failed to create user in Keycloak: " + response.getStatus() );
        }

        // extract the new user's ID from the response
        String userId = response.getLocation().getPath()
                .replaceAll(".*/([^/]+)$", "$1");

    }

    public void deleteUser(String username) {
        keycloak.realm(realm).users()
                .search(username).stream()
                .findFirst()
                .ifPresent(user -> keycloak.realm(realm).users().get(user.getId()).remove());
    }

}

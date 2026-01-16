package com.example.documentanalyzer.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
// DTO(Data Transfer Object), c'est un objet simple utilisé uniquement pour transférer des données entres differentes couches de l'application, particulierement
//entre backend et frontend. Si on envoie directement l'entite document a angular, on aura trop dinfos, format de date bizarre, pas de message perso pour le user, couplage forst entre BDD et API, on expose des donnees sensible
// par accident et cela n'est pas securisé
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentUploadResponse {
    private Long id;
    private String filename;
    private String contentType;
    private Long size;
    private String filePath;
    private String uploadDate; //format personalisé
    private String message; // message for the user
}


// avantage : on controle exactement ce que l'on envoie, et separation entre la struc BDD et API
// flux complet de la transformation : MutipartFile(HTTP) -> DocumentService.uploadDocument() -> Document(JPA Entity) vers BDD -> mapToResponse() -> DocumentUploadResponse (DTO) ──→ JSON vers Angular
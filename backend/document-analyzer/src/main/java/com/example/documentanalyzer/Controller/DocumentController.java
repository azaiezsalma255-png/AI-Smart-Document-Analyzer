package com.example.documentanalyzer.Controller;


import com.example.documentanalyzer.DTO.DocumentUploadResponse;
import com.example.documentanalyzer.DocumentRepo.DocumentRepo;
import com.example.documentanalyzer.Model.Document;
import com.example.documentanalyzer.Service.DocumentService;
import com.example.documentanalyzer.Service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.MultipartStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.print.Doc;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/documents") // commun prefix for all routes
@RequiredArgsConstructor // cette pratique sert a faire les constructeurs de nos services pour eviter l'annotation autowired a chaque fois
public class DocumentController {
    private final DocumentService documentService;

    //Upload un document
    @PostMapping(value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadDocument (@RequestParam("file") MultipartFile file){
        try {
            DocumentUploadResponse response = documentService.uplaodDocument(file);
            return ResponseEntity.ok(response);
        }
        catch (IllegalArgumentException e){
             // erreur de validation exp : mauvais format, mauvaise taille..
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorResponse( e.getMessage()));
        }
        catch (IOException e){
            //erreur d'ecriture fichier
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse("Erreuer lors de upload" + e.getMessage()));
        }
        catch(Exception e){
            //autres erreurs innatendues
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse("Erreur inattendue: " + e.getMessage()));
        }

    }

    private Map<String,String> createErrorResponse(String message) // ici on fait une map, car l'erreur retourne a angular doit etre sous forme de message JSON object : object
    {
        Map<String,String> error = new HashMap<>();
        error.put("error",message);
        error.put("Timestamp", String.valueOf(System.currentTimeMillis()));
        return error;
    }


    }

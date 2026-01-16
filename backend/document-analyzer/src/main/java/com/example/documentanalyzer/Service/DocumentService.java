package com.example.documentanalyzer.Service;

import com.example.documentanalyzer.DTO.DocumentUploadResponse;
import com.example.documentanalyzer.DocumentRepo.DocumentRepo;
import com.example.documentanalyzer.Model.Document;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final FileStorageService fileStorageService;
    private final DocumentRepo documentRepo;

    @Transactional // si erreur tout annulé
    public DocumentUploadResponse uplaodDocument (MultipartFile file) throws IOException{
        try{
            //sauvegarder le fichier
            String filePath = fileStorageService.storeFile(file);
            // creer lentite document
            Document document = new Document(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getSize(),
                    filePath
            );
            // sauvegarder en db
            Document savedDocument = documentRepo.save(document);
            // creer la reponse
            return mapToResponse(savedDocument, "doc uploaded successfully");

        }
        catch (IOException e)
        {
            throw new IOException("Erreur lors de la sauvegarde du fichier:" +e.getMessage());
        }
    }

    private DocumentUploadResponse mapToResponse(Document document, String message) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        return new DocumentUploadResponse(
                document.getId(),
                document.getFilename(),
                document.getContentType(),
                document.getSize(),
                document.getFilePath(),
                document.getUploadDate().format(formatter),
                message
        );
    }
}

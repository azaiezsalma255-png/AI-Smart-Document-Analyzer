package com.example.documentanalyzer.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

// this class is the business logic, it takes the file received by the app and stocks it correctly on the disk
@Service
public class FileStorageService {

    @Value("${file.upload-dir}") // on injecte la val du path souhaite depuis application.properties
    private String uploadDir;

    //Type de fichier acceptes
    private static final String[] ALLOWED_EXTENSIONS = {".pdf",".docx",".txt",".doc"};
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; //10MB

    // valider le fichier uploadé
    public void validateFile(MultipartFile file)
    {   //verifier si le fichier est vide
        if(file.isEmpty()) {
            throw new IllegalArgumentException("le fichier est vide");
        }

        //verifier si le fichier depasse taille max
        if(file.getSize()>MAX_FILE_SIZE){
            throw new IllegalArgumentException(
                    "la taille du fichier est trop volumineuse. Taille max:" +(MAX_FILE_SIZE/1024/1024)+ "MB"
                    );
        }

        //Verifier lextensiosn du fichier
        if(file.getOriginalFilename()==null || !isValidExtension(file.getOriginalFilename()) ){
            throw new IllegalArgumentException(
                    "Format du fichier non autorisé. Format acceptés :" + String.join(", ", ALLOWED_EXTENSIONS)
                    );
        }
    }
    public boolean isValidExtension(String filename)
    {
        String lowerFilename = filename.toLowerCase();
        for( String var : ALLOWED_EXTENSIONS){
            if(lowerFilename.endsWith(var))
                return true;
        }
        return false;
    }

    // Generer un nome de fichier unique pour eviter les collisiosn
    public String generateUniqueFilename(String originalFilename)
    {
        String extension ="";
        int dotIndex = originalFilename.lastIndexOf('.'); // on va avoir la position du point juste avant l'extension du fichier
        if(dotIndex>0){
            extension = originalFilename.substring(dotIndex);}

        // nettoyer le nom original en enlevant les caracteres speciaux
        String cleanName = originalFilename
                .substring(0,dotIndex > 0 ? dotIndex : originalFilename.length()) // cette ligne est pour dire : si dotindex existe, alors mettre son index dans substring, si elle existe pas, ca va retourner -1 et cracher du coup on la remplace par la length du filename
                .replaceAll("[^a-zA-Z0-9_-]", "_");

        //Ajouter un UUID pour garantir lunicite
        String uniqueId = UUID.randomUUID().toString().substring(0,8); //UUID est un objet, donc on le convertit en string et on en prend que 8 caratcteres sinon trop long

        return cleanName + "_" + uniqueId + extension;

    }

    // Sauvegarder le fichier sur le disque. MultipartFile est le fichier tel qu'il arrive depuis HTTP, pas encore sur le disque, il arrive en memoire RAM et il est
    // temporraire, donc il faut lenregistrer sur le disque et store file transforme un MultipartFile http en un vrai fichier disque
    public String storeFile (MultipartFile file) throws IOException{
         //Validation
        validateFile(file);
        // creer le dossier si necessaire
        Path uploadPath = Paths.get(uploadDir); // on transforme la string en un objet path (plus sur que string)
        if(!Files.exists(uploadPath)){ // on cree le odssier si il existe pas
            Files.createDirectories(uploadPath);
        }
        // generer le nom unique
        String uniqueFilename = generateUniqueFilename(StringUtils.cleanPath(file.getOriginalFilename())); //clean path enleve tout chemin  non necessaire et garde juste le nom du fichier
        Path targetLocation = uploadPath.resolve(uniqueFilename); // si uploadDir = C:/uploads et uniqueFilename = cv_1234abcd.pdf => C:/uploads/cv_1234abcd.pdf
        Files.copy(
                file.getInputStream(), // lire le contenu du fichier depuis la RAM
                targetLocation, // destination sur le disque
                StandardCopyOption.REPLACE_EXISTING
        );
        return targetLocation.toString();


    }

    public void deleteFile (String filePath) throws IOException {
        Path path = Paths.get(filePath);
        Files.deleteIfExists(path);
    }

    }






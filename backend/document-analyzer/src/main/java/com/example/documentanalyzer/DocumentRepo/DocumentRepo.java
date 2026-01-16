package com.example.documentanalyzer.DocumentRepo;

import com.example.documentanalyzer.Model.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepo extends JpaRepository<Document,Integer> {

}

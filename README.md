# 📄 AI Smart Document Analyzer - Backend

Spring Boot backend for intelligent document analysis with automatic content extraction and AI-powered summary generation.

## 🎯 Project Overview

This backend enables document upload (PDF, DOCX, TXT), text extraction, and automatic analysis using:
- **Pre-trained AI Model** (HuggingFace) for summary generation
- **TF-IDF Algorithm** for keyword extraction
- **Apache Tika** for multi-format text extraction

---

## 🏗️ Architecture

### **Tech Stack**

| Technology | Version | Purpose |
|------------|---------|---------|
| **Java** | 17+ | Core language |
| **Spring Boot** | 3.5.9 | Backend framework |
| **PostgreSQL** | 14+ | Database |
| **Apache Tika** | 2.9.1 | Text extraction |
| **HuggingFace** 
| **Maven** | 3.6+ | Dependency management |

### Layered Architecture
CONTROLLER LAYER                     
DocumentController.java                                 
- POST   /api/documents/upload                         
- POST   /api/documents/{id}/analyze                   
- GET    /api/documents/{id}/analysis                  
- GET    /api/documents/{id}                           
- GET    /api/documents                                


SERVICE LAYER                        
DocumentService.java        - Document management      
AIAnalysisService.java      - Analysis orchestration   
TextExtractionService.java  - Text extraction (Tika)   
HuggingFaceClient.java      - AI & TF-IDF processing   
FileStorageService.java     - File storage handling   


REPOSITORY LAYER                     
DocumentRepo.java           - Database operations       


DATA LAYER                           
PostgreSQL Database                                     
- documents table (metadata + analysis results)        


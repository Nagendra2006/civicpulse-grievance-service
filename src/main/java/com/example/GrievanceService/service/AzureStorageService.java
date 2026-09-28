package com.example.GrievanceService.service;

import com.azure.storage.blob.*;
import com.azure.storage.blob.models.BlobHttpHeaders;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class AzureStorageService {

    private final BlobContainerClient containerClient;

    public AzureStorageService(
            @Value("${azure.storage.connection-string}") String connectionString,
            @Value("${azure.storage.container-name}") String containerName) {

        BlobServiceClient serviceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();

        this.containerClient = serviceClient.getBlobContainerClient(containerName);
    }

    public String uploadFile(MultipartFile file) {

        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            // Ensure container exists
            if (!containerClient.exists()) {
                containerClient.create();
            }

            BlobClient blobClient = containerClient.getBlobClient(fileName);

            blobClient.upload(file.getInputStream(), file.getSize(), true);

            blobClient.setHttpHeaders(new BlobHttpHeaders()
                    .setContentType(file.getContentType()));

            // 🔥 return public URL
            return blobClient.getBlobUrl();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Azure upload failed: " + e.getMessage(), e);
        }
    }
}

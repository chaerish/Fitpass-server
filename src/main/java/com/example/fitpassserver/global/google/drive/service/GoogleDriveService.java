package com.example.fitpassserver.global.google.drive.service;

import com.example.fitpassserver.global.google.drive.exception.GoogleDriveErrorCode;
import com.example.fitpassserver.global.google.drive.exception.GoogleDriveException;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleDriveService {

    private final Drive drive;

    public String uploadToGoogleDrive(byte[] fileBytes, String fileName, String mimeType, String folderId) {
        try {
            File fileMetadata = new File();
            fileMetadata.setName(fileName);
            fileMetadata.setParents(List.of(folderId));

            InputStreamContent mediaContent = new InputStreamContent(
                    mimeType,
                    new ByteArrayInputStream(fileBytes)
            );

            File uploadedFile = drive.files().create(fileMetadata, mediaContent)
                    .setFields("id")
                    .execute();

            return uploadedFile.getId();
        } catch (IOException e) {
            log.error("구글 드라이브 업로드 실패: {}", e.getMessage(), e);
            throw new GoogleDriveException(GoogleDriveErrorCode.UPLOAD_FAILED);
        }
    }
}

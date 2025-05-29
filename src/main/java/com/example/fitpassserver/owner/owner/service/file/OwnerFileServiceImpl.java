package com.example.fitpassserver.owner.owner.service.file;

import com.example.fitpassserver.global.aws.s3.service.S3Service;
import com.example.fitpassserver.global.google.drive.service.GoogleDriveService;
import com.example.fitpassserver.owner.owner.dto.OwnerRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OwnerFileServiceImpl implements OwnerFileService {
    private final S3Service s3Service;
    private final GoogleDriveService googleDriveService;

    @Value("${google.bank-folder}")
    private String bankCopyFolderId;
    @Value("${google.business-registration-folder}")
    private String businessRegistrationFolderId;

    @Async
    @Override
    public void uploadOwnerFile(OwnerRequestDTO.OwnerJoinDTO request) {
        String ownerId = request.getLoginId();

        String bankCopyKey = request.getBankCopyUrl();
        String businessRegistrationKey = request.getBusinessRegistrationUrl();

        byte[] bankCopyBytes = s3Service.downloadFileFromS3(bankCopyKey);
        byte[] businessRegistrationBytes = s3Service.downloadFileFromS3(businessRegistrationKey);

        String bankCopyMimeType = getMimeTypeFromFilename(bankCopyKey);
        String businessRegistrationMimeType = getMimeTypeFromFilename(businessRegistrationKey);

        String bankCopyFileName = ownerId + "_Bank" + extractFileExtension(bankCopyKey);
        String businessRegistrationFileName = ownerId + "_Business" + extractFileExtension(businessRegistrationKey);

        String bankCopyFileId = googleDriveService.uploadToGoogleDrive(
                bankCopyBytes,
                bankCopyFileName,
                bankCopyMimeType,
                bankCopyFolderId
        );

        String businessRegistrationFileId = googleDriveService.uploadToGoogleDrive(
                businessRegistrationBytes,
                businessRegistrationFileName,
                businessRegistrationMimeType,
                businessRegistrationFolderId
        );
    }

    private String extractFileExtension(String key) {
        return key.substring(key.lastIndexOf("."));
    }

    private String getMimeTypeFromFilename(String filename) {
        String extension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
        switch (extension) {
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            default:
                return "application/octet-stream";
        }
    }
}

package com.ecom.Services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileUpload {
    public String uploadImage(String path, MultipartFile multipartFile) throws IOException{
        String originalFilename = multipartFile.getOriginalFilename();
        String s = UUID.randomUUID().toString();
        String randomImageNameWithExtension = s.concat(originalFilename.substring(originalFilename.lastIndexOf(".")));
        String fullpath = path+File.separator + randomImageNameWithExtension;
        File folderFile = new File(path);
        if (!folderFile.exists()) {
            folderFile.mkdirs();
        }
        try {
            Files.copy(multipartFile.getInputStream(), Paths.get(fullpath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return randomImageNameWithExtension;
    }
}

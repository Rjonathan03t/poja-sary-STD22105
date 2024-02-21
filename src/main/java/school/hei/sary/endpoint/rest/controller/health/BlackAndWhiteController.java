package school.hei.sary.endpoint.rest.controller.health;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.hei.sary.file.BucketComponent;
import school.hei.sary.file.FileHash;

import java.io.File;

@RestController
public class BlackAndWhiteController {
    BucketComponent bucketComponent;

    @Autowired
    public BlackAndWhiteController(BucketComponent bucketComponent) {
        this.bucketComponent = bucketComponent;
    }
    @PostMapping ("/upload/{bucketKey}")
    public ResponseEntity<String> uploadFileToS3(
            @RequestBody File file,
            @PathVariable String bucketKey) {
        try {
            FileHash fileHash = bucketComponent.upload(file, bucketKey);
            return ResponseEntity.ok("File uploaded successfully to S3. Hash: " + fileHash);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file to S3.");
        }
    }
}

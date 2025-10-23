package com.jinternals.support.agent.etl.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.core.io.Resource;

import javax.imageio.IIOException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Slf4j
@UtilityClass
public class HashUtils {
    public String contentSha256(Resource resource){
        try (InputStream inputStream = resource.getInputStream()){
            return DigestUtils.sha256Hex(inputStream);
        }catch (IOException e){
            log.error("Failed to read resource: {}", resource, e);
            return "content not available";
        }
    }
    public String calculateHash(String cleanedSourcePath,Resource resource) {
        var contentHash = contentSha256(resource);
        var data = cleanedSourcePath + "//" + contentHash;
        return DigestUtils.sha256Hex(data);
    }
}

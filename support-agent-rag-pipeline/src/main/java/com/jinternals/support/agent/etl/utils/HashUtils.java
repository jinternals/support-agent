package com.jinternals.support.agent.etl.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.core.io.Resource;

@Slf4j
public class HashUtils {
    public static String calculateHash(Resource resource) {
        var lastModified = 0l;

        try {
            lastModified = resource.lastModified();
        } catch (Exception e) {
            log.warn("Failed to get last modified date for resource: {}", resource, e);
        }

        var original = resource.getDescription().toLowerCase() + "//" + lastModified;

        return DigestUtils.sha256Hex(original);
    }
}

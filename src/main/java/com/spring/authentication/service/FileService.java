package com.spring.authentication.service;

import com.spring.authentication.dto.ImageDTO;
import com.spring.authentication.dto.ImageDownloadDTO;
import org.springframework.core.io.Resource;

public interface FileService {

    ImageDTO uploadFile(ImageDTO imageDTO);

    ImageDownloadDTO downloadFile(Long imageId);
}

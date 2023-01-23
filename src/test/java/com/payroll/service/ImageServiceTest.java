package com.payroll.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.payroll.service.impl.ImageServiceImpl;
import java.io.File;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
class ImageServiceTest {

    @Mock
    private ImageService imageService;

    @BeforeEach
    void setUp() {
        imageService = new ImageServiceImpl();
    }

    @Test
    void getImage_whenFileExists_ShouldReturnFilePath() {
        String companyName = "testCompany";
        String filePath = "src/main/resources/static/images/testCompany.png";
        String result = imageService.getImage(companyName);
        assertEquals(filePath, result);
    }

    @Test
    void getImage_whenFileNotExists_ShouldCreateFile() {
        String companyName = "testCompany";
        String filePath = "src/main/resources/static/images/testCompany.png";
        File file = new File(filePath);
        imageService.getImage(companyName);
        assertTrue(file.exists());
    }
}
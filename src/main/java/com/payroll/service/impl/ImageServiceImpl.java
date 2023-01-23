package com.payroll.service.impl;

import com.payroll.service.ImageService;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.springframework.stereotype.Component;

@Component
public class ImageServiceImpl implements ImageService {

    @Override
    public String getImage(String companyName) {
        final String FILE_NAME = companyName + ".png";
        final String FilePath = "src/main/resources/static/images/" + FILE_NAME;
        try {
            File imageFile = new File(FilePath);
            //validate if file exists
            if (imageFile.exists()) {
                return FilePath;
            }

            byte[] image = createImage(companyName);
            try (FileOutputStream outputStream = new FileOutputStream(imageFile)) {
                outputStream.write(image);
            }

        } catch (IOException | TranscoderException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while creating the company image");
        }
        return FilePath;
    }


    public byte[] createImage(String text) throws IOException, TranscoderException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        createImage(text, outputStream);
        return outputStream.toByteArray();
    }

    public void createImage(String text, OutputStream outputStream)
        throws IOException, TranscoderException {

        TranscoderInput input = new TranscoderInput(new StringReader(getSvgTemplate(text)));
        TranscoderOutput output = new TranscoderOutput(outputStream);
        PNGTranscoder transcoder = new PNGTranscoder();
        transcoder.transcode(input, output);
        outputStream.flush();
    }

    private String getSvgTemplate(String text) {
        return "<svg width=\"400\" height=\"300\" xmlns=\"http://www.w3.org/2000/svg\">\n" +
            "\n" +
            " <g>\n" +
            "  <title>Layer 1</title>\n" +
            "  <text font-style=\"italic\" font-weight=\"bold\" transform=\"matrix(1.88438 0 0 4.38432 -74.288 -231.368)\" stroke=\"#000\" xml:space=\"preserve\" text-anchor=\"start\" font-family=\"Noto Sans JP\" font-size=\"24\" id=\"svg_2\" y=\"118.28262\" x=\"156.70293\" stroke-width=\"0\" fill=\"#000000\">" +
            text + "</text>\n" +
            "  <path transform=\"rotate(-10.0563 401.5 348.005)\" stroke=\"#000\" id=\"svg_3\" d=\"m187,294c0,0.82444 114.87441,107.17717 114.87441,108.00161c0,0.82444 314.12561,-65.95518 314.12561,-65.95518\"  stroke-width=\"0\" fill=\"#000000\"/>\n" +
            "  <path transform=\"rotate(168.285 352.5 157.005)\" stroke=\"#000\" id=\"svg_5\" d=\"m138,103c0,0.82444 114.87441,107.17717 114.87441,108.00161c0,0.82444 314.12561,-65.95518 314.12561,-65.95518\" stroke-width=\"0\" fill=\"#000000\"/>\n" +
            " </g>\n" +
            "</svg>";
    }

}

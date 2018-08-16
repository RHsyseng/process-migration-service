/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.redhat.syseng.soleng.rhpam.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import javax.imageio.ImageIO;


/**
 *
 * @author czhu
 */
public class ImageUtil {

    public static String bufferedImageToBase64ImageString(BufferedImage image, String type) {
        String base64Image = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {

            ImageIO.write(image, type, bos);
            byte[] imageBytes = bos.toByteArray();

            base64Image = Base64.getEncoder().encodeToString(imageBytes);
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return base64Image;
    }

    public static BufferedImage base64ImageStringToBufferedImage(String base64Image) {

        BufferedImage image = null;
        try {
            byte[] imageByte = Base64.getDecoder().decode(base64Image);
            //System.out.println(">??????????????????????????base64Image " + base64Image);
            System.out.println(">??????????????????????????imageByte.length: " + imageByte.length);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);
            System.out.println(">??????????????????????????BufferedImage: " + image);
            bis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

}

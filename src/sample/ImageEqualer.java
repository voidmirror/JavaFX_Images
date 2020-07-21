package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ImageEqualer {
    Image image1;
    Image image2;
    ImageView view;

    public ImageEqualer(Image image1, Image image2, ImageView view) {
        this.image1 = image1;
        this.image2 = image2;
        this.view = view;
    }

    public void isEqual() throws IOException {
//        BufferedImage im1 = new BufferedImage((int)image1.getWidth(), (int)image1.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
//        BufferedImage scaledImage = new BufferedImage((int)image1.getWidth(), (int)image1.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
//        Graphics2D graphics2D = scaledImage.createGraphics();
//        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
//        graphics2D.drawImage(SwingFXUtils.fromFXImage(image1, scaledImage), 0, 0, 64, 64, null);
//        graphics2D.dispose();
//        WritableImage writableImage = new WritableImage(64, 64);
//        SwingFXUtils.toFXImage(im1, writableImage);
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        File skem = new File("skem.png");
//        try {
//            ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", skem);
//            System.out.println("written");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        System.out.println("yep");
//        view.setImage(writableImage);


        System.out.println(pathTrimmer(image1.getUrl()));
        another(image1);
        another(image2);

//        ArrayList<Integer> firstBytes = grayscaleByteFormer(image1);
//        ArrayList<Integer> secondBytes = grayscaleByteFormer(image2);

    }

    public void grayscaleByteFormer(Image image) {

        try {
            ArrayList<Integer> arrayList = new ArrayList<>();
            File file = new File(pathFormer(image.getUrl()));
            BufferedImage source = ImageIO.read(file);
            BufferedImage result = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());

            for (int i = 0; i < source.getWidth(); i++) {
                for (int j = 0; j < source.getHeight(); j++) {
                    Color color = new Color(source.getRGB(i, j));
                    int blue = color.getBlue();
                    int red = color.getRed();
                    int green = color.getGreen();

                    int grey = (int) (red * 0.299 + green * 0.587 + blue * 0.114);

                    int newRed = grey;
                    int newGreen = grey;
                    int newBlue = grey;

                    Color newColor = new Color(newRed, newGreen, newBlue);
                    result.setRGB(i, j, newColor.getRGB());
                }
            }

            File output = new File(pathFormer(image.getUrl()));
            ImageIO.write(result, "jpg", output);

        } catch (IOException e) {
            System.out.println("Image cannot be found");
        }


    }

    public void transformerate() {
        AffineTransform transform = new AffineTransform(
                ((double) 64) / image1.getWidth(), 0, 0,
                ((double) 64) / image1.getHeight(), 0, 0);
        AffineTransformOp transformer = new AffineTransformOp(transform, new RenderingHints(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC));
        BufferedImage fullImage = new BufferedImage((int)image1.getWidth(), (int)image1.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        BufferedImage miniImage = new BufferedImage(64, 64, BufferedImage.TYPE_3BYTE_BGR);
        transformer.filter(fullImage, miniImage);
        view.setImage(SwingFXUtils.toFXImage(miniImage, null));
    }

    public void transformerate_other() {
        AffineTransform transform = new AffineTransform(
                ((double) 64) / image1.getWidth(), 0, 0,
                ((double) 64) / image1.getHeight(), 0, 0);
        AffineTransformOp transformer = new AffineTransformOp(transform, new RenderingHints(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR));
        BufferedImage fullImage = new BufferedImage((int)image1.getWidth(), (int)image1.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        BufferedImage miniImage = new BufferedImage(64, 64, BufferedImage.TYPE_3BYTE_BGR);
        transformer.filter(fullImage, miniImage);
        view.setImage(convertToFxImage(miniImage));
    }

    private static Image convertToFxImage(BufferedImage image) {
        WritableImage wr = null;
        if (image != null) {
            wr = new WritableImage(image.getWidth(), image.getHeight());
            PixelWriter pw = wr.getPixelWriter();
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    pw.setArgb(x, y, image.getRGB(x, y));
                }
            }
        }

        return new ImageView(wr).getImage();
    }

    public String pathTrimmer(String path) {
        StringBuffer s = new StringBuffer(path);
        for (int i = 0; i < 6; i++) {
            s.deleteCharAt(0);
        }

        System.out.println("TRIMMER");
        return s.toString();
    }

    public String pathFormer(String path) {
        StringBuffer s = new StringBuffer(path);
        int slashes = 0;
        char c;
        for (int i = 0; i < path.length(); i++) {
            c = path.charAt(i);
            System.out.print(path.charAt(i));
            if (c == '/') {
                slashes++;
            }
        }
        System.out.println();
        int slHere = 0;
        for (int i = 0; i < path.length(); i++) {
            c = path.charAt(i);
            s.deleteCharAt(0);
            if (c == '/') {
                slHere++;
                if (slHere == slashes) {
                    break;
                }
            }
        }
        System.out.println(s);

        return s.toString();
    }

    public void another(Image image) throws IOException {
//        System.out.println(pathTrimmer(image.getUrl()));
        ImageResizer imageResizer = new ImageResizer();
        imageResizer.resize(pathTrimmer(image.getUrl()), pathFormer(image.getUrl()), 64, 64);

    }

    public Image getImage1() {
        return image1;
    }

    public Image getImage2() {
        return image2;
    }
}

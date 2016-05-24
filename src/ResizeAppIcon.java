/**
 * Created by Shogo on 2016-02-27.
 */

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class ResizeAppIcon {

    public static void main (String[] args) {
        String inputImagePath = readInputImagePath();
        String androidProjectPath = readAndroidProjectPath();
        BufferedImage inputImage = loadImage(inputImagePath);
        resizeAppIcon(androidProjectPath, inputImage,  36, "ldpi");
        resizeAppIcon(androidProjectPath, inputImage,  48, "mdpi");
        resizeAppIcon(androidProjectPath, inputImage,  72, "hdpi");
        resizeAppIcon(androidProjectPath, inputImage,  96, "xhdpi");
        resizeAppIcon(androidProjectPath, inputImage, 144, "xxhdpi");
        resizeAppIcon(androidProjectPath, inputImage, 192, "xxxhdpi");
    }

    private static void resizeAppIcon
            (String androidProjectPath, BufferedImage inputImage, int sideLength, String resolution) {
        String iconPath = androidProjectPath + "mipmap-" + resolution;
        File iconDir = new File(iconPath);
        File oldIcon = new File(iconPath + "\\ic_launcher.png");
        String newIconPath = newIconPath(iconPath, iconDir);
        System.out.println("新しいアイコンを作成します： \"" + newIconPath + "\"");
        if (newIconPath != null && oldIcon.renameTo(new File(newIconPath))) {
            saveImage(resizeImage(inputImage, sideLength, sideLength), iconPath + "\\ic_launcher.png");
        }
        else {
            System.out.println("現在のアイコンのバックアップに失敗しました。");
            System.out.println("新しいアイコンへの置換を中止します。");
        }
    }

    private static String newIconPath (String iconPath, File iconDir) {
        try {
            return iconPath + "\\ic_launcher" + String.valueOf(iconDir.listFiles().length) + ".png";
        }
        catch (NullPointerException e) {
            return null;
        }
    }

    static BufferedImage resizeImage (BufferedImage inputImage,  int outputImageWidth, int outputImageHeight) {
        BufferedImage outputImage =
                new BufferedImage(outputImageWidth, outputImageHeight, BufferedImage.TYPE_INT_ARGB);
        outputImage.getGraphics().drawImage(
                inputImage.getScaledInstance(
                        outputImageWidth,
                        outputImageHeight,
                        Image.SCALE_AREA_AVERAGING),
                0,
                0,
                outputImageWidth,
                outputImageHeight,
                null
        );
        return outputImage;
    }

    private static String readInputImagePath () {
        String inputImagePath;
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");
        while (true) {
            System.out.println("入力画像のパスを入力して下さい。");
            System.out.print("> ");
            inputImagePath = scanner.next();
            if (isValidImage(inputImagePath)) {
                return inputImagePath;
            }
        }
    }

    private static BufferedImage loadImage (String path) {
        try {
            return ImageIO.read(new File(path));
        }
        catch (IOException e) {
            return null;
        }
    }

    private static boolean saveImage (BufferedImage image, String path) {
        try {
            ImageIO.write(image, "png", new File(path));
            return true;
        }
        catch (IOException e) {
            return false;
        }
    }

    private static boolean isValidImage (String path) {
        BufferedImage image = loadImage(path);
        if (image == null) {
            System.out.println("画像の読み込みに失敗しました。");
            return false;
        }
        else {
            return true;
        }
    }

    private static String readAndroidProjectPath () {
        String androidProjectPath;
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");
        while (true) {
            System.out.println("Android Project のパスを入力して下さい。");
            System.out.print("> ");
            androidProjectPath = scanner.next();
            File file = new File(androidProjectPath);
            if (file.exists()) {
                break;
            }
            else {
                System.out.println("パスが存在しません。");
            }
        }
        return androidProjectPath;
    }
}
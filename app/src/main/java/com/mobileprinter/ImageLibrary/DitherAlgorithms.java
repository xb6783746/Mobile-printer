package com.mobileprinter.ImageLibrary;

/**
 * Created by Влад on 24.10.2016.
 */

public class DitherAlgorithms {

    public static void floydSteinbergDithering(BinaryImage image) {
        float[][] kernel = new float[][]{
                new float[]{0, 0, 7 / 16f},
                new float[]{3 / 16f, 5 / 16f, 1 / 16f}
        };

        int width = image.getWidth();
        int height = image.getHeight();

        byte tmp, add;
        int current;
        for (int k = 0; k < height; k++) {
            for (int i = 0; i < width; i++) {
                current = image.getByte(i, k);

                add = (byte) (current < 127 ? current : current - 255);

                image.setByte(i, k, current < 127 ? 0 : 255);

                process(kernel, 1, add, image, i, k);

            }
        }
    }

    public static void atkinsonDithering(BinaryImage image) {
        float[][] kernel = new float[][]{
                new float[]{0, 0, 1 / 8f, 1 / 8f},
                new float[]{1 / 8f, 1 / 8f, 1 / 8f, 0},
                new float[]{0, 1 / 8f, 0, 0}
        };

        int width = image.getWidth();
        int height = image.getHeight();

        byte tmp, add;
        int current;

        for (int k = 0; k < height; k++) {
            for (int i = 0; i < width; i++) {
                current = image.getByte(i, k);

                add = (byte) (current < 127 ? current : current - 255);

                image.setByte(i, k, current < 127 ? 0 : 255);

                process(kernel, 1, add, image, i, k);

            }
        }
    }

    public static void stuckiDithering(BinaryImage image) {
        float[][] kernel = new float[][]{
                new float[]{0, 0, 0, 8 / 42f, 4 / 42f},
                new float[]{2 / 42f, 4 / 42f, 8 / 42f, 4 / 42f, 2 / 42f},
                new float[]{1 / 42f, 2 / 42f, 4 / 42f, 2 / 42f, 1 / 42f}
        };

        int width = image.getWidth();
        int height = image.getHeight();

        byte tmp, add;
        int current;

        for (int k = 0; k < height; k++) {
            for (int i = 0; i < width; i++) {
                current = image.getByte(i, k);

                add = (byte) (current < 127 ? current : current - 255);

                image.setByte(i, k, current < 127 ? 0 : 255);

                process(kernel, 2, add, image, i, k);

            }
        }
    }

    private static void add(BinaryImage image, int value, int x, int y) {

        int tmp = image.getByte(x, y);

        tmp += value;

        image.setByte(x, y, tmp);
    }

    private static void process(float[][] kernel, int offset, int error, BinaryImage image, int x, int y) { //offset - индекс текущего пикселя в первом массиве

        for (int i = 0; i < kernel.length; i++) {

            float[] row = kernel[i];

            for (int k = 0; k < row.length; k++) {

                int currX = x - offset + k;
                int currY = y + i;

                if (currX >= 0 && currX < image.getWidth() && currY < image.getHeight()) {

                    int tmp = (byte) (row[k] * error);

                    add(image, tmp, currX, currY);
                }
            }
        }
    }
}

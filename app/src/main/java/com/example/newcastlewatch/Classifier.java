package com.example.newcastlewatch;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
public class Classifier {

    private static final int INPUT_SIZE = 224;
    private static final int PIXEL_SIZE = 3;  // RGB
    private static final int IMAGE_MEAN = 128;
    private static final float IMAGE_STD = 128.0f;

    private Interpreter interpreter;

    public Classifier(Context context) throws IOException {
        try {
            // Load the TFLite model from the assets folder
            MappedByteBuffer tfliteModel = loadModelFile(context.getAssets(), "newcastle1.tflite");
            interpreter = new Interpreter(tfliteModel);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Error loading TFLite model", e);
        }
    }

    public Result classify(Bitmap bitmap) {
        // Preprocess the input image
        ByteBuffer inputBuffer = preprocessImage(bitmap);

        // Run inference
        TensorBuffer outputBuffer = TensorBuffer.createFixedSize(new int[]{1, 6}, DataType.FLOAT32);
        interpreter.run(inputBuffer, outputBuffer.getBuffer());

        float[] confidences = outputBuffer.getFloatArray();
        return new Result(confidences);
    }

    private MappedByteBuffer loadModelFile(AssetManager assetManager, String modelPath) throws IOException {
        AssetFileDescriptor fileDescriptor = assetManager.openFd(modelPath);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }





    private ByteBuffer preprocessImage(Bitmap bitmap) {
        // Resize the input bitmap to the desired INPUT_SIZE
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, true);

        // Allocate a ByteBuffer with the required size
        ByteBuffer inputBuffer = ByteBuffer.allocateDirect(INPUT_SIZE * INPUT_SIZE * PIXEL_SIZE * 4); // 4 bytes per float

        // Set the byte order to native order
        inputBuffer.order(ByteOrder.nativeOrder());

        // Loop through each pixel of the resized bitmap
        for (int y = 0; y < INPUT_SIZE; y++) {
            for (int x = 0; x < INPUT_SIZE; x++) {
                int pixel = resizedBitmap.getPixel(x, y);

                // Extract RGB components and normalize them to [0, 1]
                float normalizedRed = ((pixel >> 16) & 0xFF) / 255.0f;
                float normalizedGreen = ((pixel >> 8) & 0xFF) / 255.0f;
                float normalizedBlue = (pixel & 0xFF) / 255.0f;

                // Put the normalized RGB values into the buffer
                inputBuffer.putFloat(normalizedRed);
                inputBuffer.putFloat(normalizedGreen);
                inputBuffer.putFloat(normalizedBlue);
            }
        }

        // Rewind the buffer before returning
        inputBuffer.rewind();

        return inputBuffer;
    }






    //same as the above method

//    private ByteBuffer preprocessImage(Bitmap bitmap) {
//        int[] intValues = new int[INPUT_SIZE * INPUT_SIZE];
//        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
//
//        ByteBuffer inputBuffer = ByteBuffer.allocateDirect(4 * INPUT_SIZE * INPUT_SIZE * PIXEL_SIZE);
//        inputBuffer.order(ByteOrder.nativeOrder());
//
//        for (int pixelValue : intValues) {
//            float normalizedValue = (pixelValue & 0xFF) / 255.0f;
//            inputBuffer.putFloat(normalizedValue);
//            inputBuffer.putFloat(normalizedValue);
//            inputBuffer.putFloat(normalizedValue);
//        }
//
//        return inputBuffer;
//    }


    public static class Result {
        private final int index;
        private final float confidence;

        public Result(float[] confidences) {
            float maxConfidence = 0;
            int maxIndex = 0;

            for (int i = 0; i < confidences.length; i++) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i];
                    maxIndex = i;
                }
            }

            this.index = maxIndex;
            this.confidence = maxConfidence;
        }

        public int getIndex() {
            return index;
        }

        public float getConfidence() {
            return confidence;
        }
    }

    public void close() {
        interpreter.close();
    }


}


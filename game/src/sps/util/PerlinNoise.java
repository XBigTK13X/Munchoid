package sps.util;

import com.badlogic.gdx.graphics.Color;
import sps.core.RNG;

// Based off of: http://devmag.org.za/2009/04/25/perlin-noise/
public class PerlinNoise {
    public static float[][] generateWhiteNoise(int width, int height) {
        float[][] noise = new float[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                noise[i][j] = (float) RNG.next() % 1;
            }
        }

        return noise;
    }

    public static float interpolate(float x0, float x1, float alpha) {
        return x0 * (1 - alpha) + alpha * x1;
    }

    public static Color interpolate(Color col0, Color col1, float alpha) {
        float beta = 1 - alpha;
        return new Color((col0.r * alpha + col1.r * beta), (col0.g * alpha + col1.g * beta), (col0.b * alpha + col1.b * beta), 1f);
    }

    public static Color gertColor(Color gradientStart, Color gradientEnd, float t) {
        float u = 1 - t;

        Color color = new Color(gradientStart.r * u + gradientEnd.r * t, (gradientStart.g * u + gradientEnd.g * t), (gradientStart.b * u + gradientEnd.b * t), 1f);

        return color;
    }

    public static Color[][] mapGradient(Color gradientStart, Color gradientEnd, float[][] perlinNoise) {
        int width = perlinNoise.length;
        int height = perlinNoise[0].length;

        Color[][] image = new Color[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                image[i][j] = gertColor(gradientStart, gradientEnd, perlinNoise[i][j]);
            }
        }

        return image;
    }

    public static float[][] generateSmoothNoise(float[][] baseNoise, int octave) {
        int width = baseNoise.length;
        int height = baseNoise[0].length;

        float[][] smoothNoise = new float[width][height];

        int samplePeriod = 1 << octave; // calculates 2 ^ k
        float sampleFrequency = 1.0f / samplePeriod;

        for (int i = 0; i < width; i++) {
            //calculate the horizontal sampling indices
            int sample_i0 = (i / samplePeriod) * samplePeriod;
            int sample_i1 = (sample_i0 + samplePeriod) % width; //wrap around
            float horizontal_blend = (i - sample_i0) * sampleFrequency;

            for (int j = 0; j < height; j++) {
                //calculate the vertical sampling indices
                int sample_j0 = (j / samplePeriod) * samplePeriod;
                int sample_j1 = (sample_j0 + samplePeriod) % height; //wrap around
                float vertical_blend = (j - sample_j0) * sampleFrequency;

                //blend the top two corners
                float top = interpolate(baseNoise[sample_i0][sample_j0],
                        baseNoise[sample_i1][sample_j0], horizontal_blend);

                //blend the bottom two corners
                float bottom = interpolate(baseNoise[sample_i0][sample_j1],
                        baseNoise[sample_i1][sample_j1], horizontal_blend);

                //final blend
                smoothNoise[i][j] = interpolate(top, bottom, vertical_blend);
            }
        }

        return smoothNoise;
    }

    public static float[][] generatePerlinNoise(float[][] baseNoise, int octaveCount) {
        int width = baseNoise.length;
        int height = baseNoise[0].length;

        float[][][] smoothNoise = new float[octaveCount][][];

        float persistance = 0.7f;

        //generate smooth noise
        for (int i = 0; i < octaveCount; i++) {
            smoothNoise[i] = generateSmoothNoise(baseNoise, i);
        }

        float[][] perlinNoise = new float[width][height];

        float amplitude = 1.0f;
        float totalAmplitude = 0.0f;

        //blend noise together
        for (int octave = octaveCount - 1; octave >= 0; octave--) {
            amplitude *= persistance;
            totalAmplitude += amplitude;

            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    perlinNoise[i][j] += smoothNoise[octave][i][j] * amplitude;
                }
            }
        }

        //normalisation
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                perlinNoise[i][j] /= totalAmplitude;
            }
        }

        return perlinNoise;
    }

    public static float[][] generatePerlinNoise(int width, int height, int octaveCount) {
        float[][] baseNoise = generateWhiteNoise(width, height);

        return generatePerlinNoise(baseNoise, octaveCount);
    }

    public static Color[][] blendImages(Color[][] image1, Color[][] image2, float[][] perlinNoise) {
        int width = image1.length;
        int height = image1[0].length;

        Color[][] image = new Color[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                image[i][j] = interpolate(image1[i][j], image2[i][j], perlinNoise[i][j]);
            }
        }

        return image;
    }


    public static float[][] adjustLevels(float[][] image, float low, float high) {
        int width = image.length;
        int height = image[0].length;

        float[][] newImage = new float[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                float col = image[i][j];

                if (col <= low) {
                    newImage[i][j] = 0;
                }
                else if (col >= high) {
                    newImage[i][j] = 1;
                }
                else {
                    newImage[i][j] = (col - low) / (high - low);
                }
            }
        }

        return newImage;
    }

}


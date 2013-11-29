package sps.display.render;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

//Modified from: https://github.com/mattdesl/lwjgl-basics/wiki/LibGDX-Brightness-&-Contrast
public class ShaderBatch extends SpriteBatch {

    private static final int GLSL_VALUE_NOT_PRESENT = -1;

    private float _brightness = 1f;
    private float _contrast = 1f;
    private float _saturation = 0f;

    private float _lastBrightness = Float.MAX_VALUE;
    private float _lastContrast = Float.MAX_VALUE;
    private float _lastSaturation = Float.MAX_VALUE;

    private boolean _brightnessControlsSupported;
    private boolean _contrastControlsSupported;
    private boolean _saturationControlsSupported;

    private ShaderProgram _shaders;

    public ShaderBatch(ShaderProgram shaders) {
        _shaders = shaders;
        _brightnessControlsSupported = _shaders.getUniformLocation("u_brightness") != GLSL_VALUE_NOT_PRESENT;
        setBrightness(_brightness);
        _contrastControlsSupported = _shaders.getUniformLocation("u_contrast") != GLSL_VALUE_NOT_PRESENT;
        setContrast(_contrast);
        _saturationControlsSupported = _shaders.getUniformLocation("u_saturation") != GLSL_VALUE_NOT_PRESENT;
        setSaturation(_saturation);
        setShader(_shaders);
    }

    public void begin() {
        super.begin();
    }

    public void setBrightness(float brightness) {
        if (_brightnessControlsSupported) {
            _brightness = brightness;
            if (_lastBrightness != _brightness) {
                _shaders.setUniformf("u_brightness", _brightness);
                _lastBrightness = _brightness;
            }
        }
    }

    public void setContrast(float contrast) {
        if (_contrastControlsSupported) {
            _contrast = contrast;
            if (_lastContrast != _contrast) {
                _shaders.setUniformf("u_contrast", _contrast);
                _lastContrast = _contrast;
            }
        }
    }

    public void setSaturation(float saturation) {
        if (_saturationControlsSupported) {
            _saturation = saturation;
            if (_lastSaturation != _saturation) {
                _shaders.setUniformf("u_saturation", _saturation);
                _lastSaturation = _saturation;
            }
        }
    }
}
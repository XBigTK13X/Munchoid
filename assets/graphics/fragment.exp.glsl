//Draw everything in Black and White
#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;

void main() {
    gl_FragColor = v_color * texture2D(u_texture, v_texCoords);
    float luminance = gl_FragColor.r * 0.299 + gl_FragColor.g * 0.587 + gl_FragColor.b * 0.114;
    gl_FragColor = vec4(luminance, luminance, luminance, gl_FragColor.a);
}
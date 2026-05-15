package com.bubbles.common.json;

/**
 * <p>description:  Jackson ObjectMapper Builder Customizer 顺序控制 </p>
 *
 * @author CryptoNeedle
 * @date 2024-12-18
 */
public interface JacksonCustomizerOrder {
    
    int CUSTOMIZER_DEFAULT = 1;
    
    int CUSTOMIZER_XSS = CUSTOMIZER_DEFAULT + 1;
}
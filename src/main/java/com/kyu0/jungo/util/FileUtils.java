package com.kyu0.jungo.util;

import java.io.File;

public class FileUtils {
    
    /**
     * 전달받은 파일 이름에서, 가장 마지막에 있는 '.' 문자부터 끝까지의 문자열을 반환한다.
     * 
     * @param fileName 확장자가 포함된 파일의 이름
     * @return 파일의 확장자명
     */
    public static String getExtensionName(String fileName) {
        return fileName.substring(fileName.lastIndexOf('.'));
    }
}

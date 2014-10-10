package com.baidu.chopsearch.utils;

import java.io.File;

import android.text.TextUtils;

public class FileUtils
{
    //copy from baidu music sdk
    public static String filterFileName(String filename)
    {
        if (TextUtils.isEmpty(filename))
            return filename;
        
        filename = filename.replace(' ', '_');
        filename = filename.replace('"', '_');
        filename = filename.replace('\'', '_');
        filename = filename.replace('\\', '_');
        filename = filename.replace('/', '_');
        filename = filename.replace('<', '_');
        filename = filename.replace('>', '_');
        filename = filename.replace('|', '_');
        filename = filename.replace('?', '_');
        filename = filename.replace(':', '_');
        filename = filename.replace(',', '_');
        filename = filename.replace('*', '_');
        
        return filename;
    }
    
    public static boolean createNewDirectory(File file) {
        if (file.exists() && file.isDirectory()) {
            return false;
        }
        return file.mkdirs();
    }
    
}

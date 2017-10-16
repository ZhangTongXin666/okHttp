package javabean;

import java.io.File;

/**
 * @author : 老头儿
 * @email : 527672827@qq.com
 * @org : 河北北方学院 移动开发工程部 C508
 * @function : （功能）
 */
public class FileInput {

    public String key;
    public String fileName;
    public File file;

    public FileInput(String key, String fileName, File file) {
        this.key = key;
        this.fileName = fileName;
        this.file = file;
    }
}

package org.example.Exception;

public class FileStorageException extends RuntimeException{
    //只传入错误原因
    public FileStorageException(String message) {
        super(message);
    }
    //传入错误原因和错误信息
    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}

package com.shop.shop.exception;

// RuntimeException : 실행 중(Runtime)에서 발생하는 예외를 나타냄.
public class NotEnoughStockException extends RuntimeException {

    // 재고가 충분하지 않을때
    public NotEnoughStockException() {
        super();
    }

    // 예외 메세지를 보냄
    public NotEnoughStockException(String message) {
        super(message);
    }

    // 예외 메세지와 원인을 함께 보냄
    public NotEnoughStockException(String message, Throwable cause) {
        super(message, cause);
    }

    // 원인을 보냄
    public NotEnoughStockException(Throwable cause) {
        super(cause);
    }
}

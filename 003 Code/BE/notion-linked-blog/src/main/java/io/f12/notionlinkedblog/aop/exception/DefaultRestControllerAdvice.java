package io.f12.notionlinkedblog.aop.exception;

import java.net.MalformedURLException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

import io.f12.notionlinkedblog.domain.common.CommonErrorResponse;

@RestControllerAdvice
public class DefaultRestControllerAdvice {

	@ExceptionHandler(IllegalStateException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public CommonErrorResponse handleIllegalState(IllegalStateException ex) {
		return CommonErrorResponse.builder()
			.errorMassage(ex.getMessage())
			.errorCode(HttpStatus.UNAUTHORIZED.value()).build();
	}

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public CommonErrorResponse handleIllegalArgument(IllegalArgumentException ex) {
		return CommonErrorResponse.builder()
			.errorMassage(ex.getMessage())
			.errorCode(HttpStatus.NOT_FOUND.value()).build();
	}

	@ExceptionHandler(NullPointerException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public CommonErrorResponse handleNullPointer(NullPointerException ex) {
		return CommonErrorResponse.builder()
			.errorMassage(ex.getMessage())
			.errorCode(HttpStatus.UNAUTHORIZED.value()).build();
	}

	@ExceptionHandler(MalformedURLException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public CommonErrorResponse handleMalformedException(MalformedURLException ex) {
		return CommonErrorResponse.builder()
			.errorMassage(ex.getMessage())
			.errorCode(HttpStatus.NOT_FOUND.value()).build();
	}

	@ExceptionHandler(MultipartException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public CommonErrorResponse handleMultipartException(MultipartException ex) {
		return CommonErrorResponse.builder()
			.errorMassage(ex.getMessage())
			.errorCode(HttpStatus.BAD_REQUEST.value()).build();
	}

	@ExceptionHandler(NumberFormatException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public CommonErrorResponse handleNumberFormatException(NumberFormatException ex) {
		return CommonErrorResponse.builder()
			.errorMassage(ex.getMessage())
			.errorCode(HttpStatus.BAD_REQUEST.value()).build();
	}
}

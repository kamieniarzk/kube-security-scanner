package com.kcs.web;

import org.springframework.http.HttpStatus;

record RestError(String message, HttpStatus httpStatus) {
}

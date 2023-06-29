package com.hust.daotao.util;

public class Status {
	public static final Integer STATUS_NULL = 404;

	// status fail
	public static final Integer STATUS_FAIL = 400;
	public static final Integer STATUS_DELETE_FAIL = 401;
	public static final Integer STATUS_CHANGE_STATUS_FAIL = 402;
	public static final Integer STATUS_REQUEST_ERROR = 403;
	public static final Integer STATUS_ENTITY_NOT_EXIST = 405;
	public static final Integer STATUS_NOT_LOGIN = 406;

	// status success
	public static final Integer STATUS_DELETE_SUCCESS = 201;
	public static final Integer STATUS_CHANGE_STATUS_SUCCESS = 202;
	public static final Integer STATUS_ADD_SUCCESS = 203;
	public static final Integer STATUS_GET_ENTITY_SUCCESS = 204;
	public static final Integer STATUS_UPDATE_SUCCESS = 205;
	public static final Integer STATUS_REGISTER_SUCCESS = 206;
	public static final Integer STATUS_SUCCESS = 200;

}

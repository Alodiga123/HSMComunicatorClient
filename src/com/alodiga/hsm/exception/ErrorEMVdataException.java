package com.alodiga.hsm.exception;

public class ErrorEMVdataException extends Exception {

	/**
	 * Esta exception se es lanzada cuando por cualquier motivo no se puede acceder a la pagina del CNE
	 */
	private static final long serialVersionUID = 1L;

	public ErrorEMVdataException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ErrorEMVdataException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public ErrorEMVdataException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ErrorEMVdataException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ErrorEMVdataException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}


}

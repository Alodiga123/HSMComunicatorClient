package com.alodiga.hsm.exception;

public class EMVEmptydataException extends Exception {

	/**
	 * Esta exception se es lanzada cuando por cualquier motivo no se puede acceder a la pagina del CNE
	 */
	private static final long serialVersionUID = 1L;

	public EMVEmptydataException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EMVEmptydataException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public EMVEmptydataException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public EMVEmptydataException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public EMVEmptydataException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}


}

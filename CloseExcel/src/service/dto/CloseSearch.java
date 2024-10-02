package service.dto;

public class CloseSearch {
	private String closeCode;
	private String venderno;
	private String message;
	private String closeDt;
	
	public CloseSearch(Builder builder) {
		this.closeCode = builder.closeCode;
		this.venderno = builder.venderno;
		this.message = builder.message;
		this.closeDt = builder.closeDt;
	}
	public String getCloseCode() {
		return closeCode;
	}
	public void setCloseCode(String closeCode) {
		this.closeCode = closeCode;
	}
	public String getVenderno() {
		return venderno;
	}
	public void setVenderno(String venderno) {
		this.venderno = venderno;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getCloseDt() {
		return closeDt;
	}
	public void setCloseDt(String closeDt) {
		this.closeDt = closeDt;
	}

	public static class Builder {
		private String closeCode;
		private String venderno;
		private String message;
		private String closeDt;
		
		public Builder closeCode(String closeCode) {
			this.closeCode = closeCode;
			return this;
		}
		
		public Builder venderno(String venderno) {
			this.venderno = venderno;
			return this;
		}
		
		public Builder message(String message) {
			this.message = message;
			return this;
		}
		
		public Builder closeDt(String closeDt) {
			this.closeDt = closeDt;
			return this;
		}
		
		public CloseSearch build() {
			return new CloseSearch(this);
		}
	}
}

package service.dto;

public class CompanyState {
	private String venderno;
	private String state;
	private String closed;
	private String closeDt;
	private String dt;
	
	public CompanyState() {
		
	}
	
	public CompanyState(Builder builder) {
		this.venderno = builder.venderno;
		this.state = builder.state;
		this.closed = builder.closed;
		this.closeDt = builder.closeDt;
		this.dt = builder.dt;
	}
	
	public String getVenderno() {
		return venderno;
	}
	public void setVenderno(String venderno) {
		this.venderno = venderno;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getClosed() {
		return closed;
	}
	public void setClosed(String closed) {
		this.closed = closed;
	}
	public String getCloseDt() {
		return closeDt;
	}
	public void setCloseDt(String closeDt) {
		this.closeDt = closeDt;
	}
	public String getDt() {
		return dt;
	}
	public void setDt(String dt) {
		this.dt = dt;
	}
	
	public static class Builder {
		private String venderno;
		private String state;
		private String closed;
		private String closeDt;
		private String dt;
		
		public Builder venderno(String venderno) {
			this.venderno = venderno;
			return this;
		}
		
		public Builder state(String state) {
			this.state = state;
			return this;
		}
		
		public Builder closed(String closed) {
			this.closed = closed;
			return this;
		}
		
		public Builder closeDt(String closeDt) {
			this.closeDt = closeDt;
			return this;
		}
		
		public Builder dt(String dt) {
			this.dt = dt;
			return this;
		}
		
		public CompanyState builder() {
			return new CompanyState(this);
		}
		
	}
}

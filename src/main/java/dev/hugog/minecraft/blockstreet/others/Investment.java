package dev.hugog.minecraft.blockstreet.others;

public class Investment {

	private int id;
	private int stocksAmount;
	
	public Investment(int id, int stocksAmount) {
		this.id = id;
		this.stocksAmount = stocksAmount;
	}
	
	public int getId() {
		return id;
	}
	
	public int getStocksAmount() {
		return stocksAmount;
	}

	public void setStocksAmount(int stocksAmount) {
		this.stocksAmount = stocksAmount;
	}

	// Other Methods
	
	@Override
	public String toString() {
		return "Investment [id=" + id + ", stocksAmount=" + stocksAmount + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Investment other = (Investment) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
}

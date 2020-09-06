package hugog.blockstreet.others;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Company {

	private int id;
	private ConfigAccessor ymlReg;
	private String name;
	private int risk;
	private double stocksPrice;
	private int availableStocks;
	private List<String> companyHistoric; 
	
	//Constructors
	
	public Company (ConfigAccessor ymlReg, String name, int risk, double stocksPrice, int stocksNumber, List<String> companyHistoric) throws IllegalArgumentException{
		
		if (risk <= 0 || risk > 5) throw new IllegalArgumentException();
		
		ymlReg.reloadConfig();
		
		if (ymlReg.getConfig().get("Companies") != null) 
			this.id = ymlReg.getConfig().getConfigurationSection("Companies").getKeys(false).size() + 1; 
		else 
			this.id = 1;
		
		this.ymlReg = ymlReg;
		this.name = name;
		this.risk = risk;
		this.stocksPrice = stocksPrice;
		this.availableStocks = stocksNumber;
		this.companyHistoric = companyHistoric;
		
	}
	
	public Company (ConfigAccessor ymlReg, String name, int risk, double stocksPrice, int stocksNumber) {
		this(ymlReg, name, risk, stocksPrice, stocksNumber, 
				Arrays.asList(
					"+ " + 2.3 + "%", 
					"+ " + 4.8 + "%",
					"- " + 2.4 + "%", 
					"- " + 0.9 + "%", 
					"+ " + 1.1 + "%"
				)
		);		
	}
	
	public Company (ConfigAccessor ymlReg, String name, int risk, double stocksPrice) {
		this(ymlReg, name, risk, stocksPrice, -1, 
				Arrays.asList(
					"+ " + 2.3 + "%", 
					"+ " + 4.8 + "%",
					"- " + 2.4 + "%", 
					"- " + 0.9 + "%", 
					"+ " + 1.1 + "%"
				)
		);		
	}
	
	public Company (ConfigAccessor ymlReg, int id) {
		
		ymlReg.reloadConfig();
		
		this.id = id;
		this.ymlReg = ymlReg;
		this.name = ymlReg.getConfig().getString("Companies." + id + ".Name");
		this.risk = ymlReg.getConfig().getInt("Companies." + id + ".Risk");
		this.stocksPrice = Math.round(ymlReg.getConfig().getDouble("Companies." + id + ".Price") * 100.00) / 100.00;
		this.availableStocks = ymlReg.getConfig().getInt("Companies." + id + ".AvailableStocks");
		this.companyHistoric = ymlReg.getConfig().getStringList("Companies." + id + ".Historic");
		
	}
	
	//Getters
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public int getRisk() {
		return risk;
	}

	public double getStocksPrice() {
		return stocksPrice;
	}

	public int getAvailableStocks() {
		return availableStocks;
	}

	public List<String> getCompanyHistoric() {
		return companyHistoric;
	}

	// Setters
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setRisk(int risk) {
		this.risk = risk;
	}

	public void setStocksPrice(double stocksPrice) {
		this.stocksPrice = stocksPrice;
	}

	public void setAvailableStocks(int availableStocks) {
		if (this.availableStocks != -1) this.availableStocks = availableStocks;		
	}

	public void setCompanyHistoric(List<String> companyHistoric) {
		this.companyHistoric = companyHistoric;
	}
	
	// Other Methods
	
	public boolean exists() {
		if (this.name == null) {
			return false;
		}else {
			return true;
		}
	}
	
	public void removeStocks(int amount) {
		this.availableStocks -= amount;
	}
	
	public void saveToYML() {
		
		ymlReg.getConfig().set("Companies." + id + ".Name", this.name);
		ymlReg.getConfig().set("Companies." + id + ".Price", this.stocksPrice);
		ymlReg.getConfig().set("Companies." + id + ".Risk", this.risk);
		ymlReg.getConfig().set("Companies." + id + ".AvailableStocks", this.availableStocks);
		ymlReg.getConfig().set("Companies." + id + ".Historic", this.companyHistoric);
		
		ymlReg.saveConfig();
		
	}
	
	public void delete(boolean update) {
		
		ymlReg.reloadConfig();
		ymlReg.getConfig().set("Companies." + id, null);
		ymlReg.saveConfig();
		
		if (update) {
			updateCompaniesIds();
		}

	}

	private void updateCompaniesIds() {
		
		List<String> companiesIDs = new ArrayList<>();
		
		ymlReg.reloadConfig();
		
		if (ymlReg.getConfig().getConfigurationSection("Companies.") != null) 
			companiesIDs = new ArrayList<String>(ymlReg.getConfig().getConfigurationSection("Companies.").getKeys(false));
				
		Collections.sort(companiesIDs);
		
		for (String companyIDString : companiesIDs) {
	
			int companyId = Integer.parseInt(companyIDString);
			
			if (companyId > this.getId()) {

				Company updatedCompany = new Company(ymlReg, companyId);
				
				updatedCompany.setId(companyId-1);				
				updatedCompany.saveToYML();
				
			}
			
		}
		
		Company lastCompany = new Company(ymlReg, companiesIDs.size() + 1);
		lastCompany.delete(false);
		
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + availableStocks;
		result = prime * result + ((companyHistoric == null) ? 0 : companyHistoric.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + risk;
		long temp;
		temp = Double.doubleToLongBits(stocksPrice);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		Company other = (Company) obj;
		if (availableStocks != other.availableStocks)
			return false;
		if (companyHistoric == null) {
			if (other.companyHistoric != null)
				return false;
		} else if (!companyHistoric.equals(other.companyHistoric))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (risk != other.risk)
			return false;
		if (Double.doubleToLongBits(stocksPrice) != Double.doubleToLongBits(other.stocksPrice))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return this.name + ": Risk: " + this.risk + "; Price: " + this.stocksPrice + "; Available Stocks: " + this.availableStocks;
	}

}

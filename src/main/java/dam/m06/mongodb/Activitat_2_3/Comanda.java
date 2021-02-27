package dam.m06.mongodb.Activitat_2_3;

import java.time.LocalDate;


public class Comanda implements Comparable<Comanda>{

	private LocalDate data_comanda;
	private int import_comanda;
	private boolean pagada;	
	
	public Comanda(LocalDate data_comanda, int import_comanda, boolean pagada) {
		super();
		this.data_comanda = data_comanda;
		this.import_comanda = import_comanda;
		this.pagada = pagada;
	}
	
	public LocalDate getData_comanda() {
		return data_comanda;
	}
	public void setData_comanda(LocalDate data_comanda) {
		this.data_comanda = data_comanda;
	}
	public int getImport_comanda() {
		return import_comanda;
	}
	public void setImport_comanda(int import_comanda) {
		this.import_comanda = import_comanda;
	}
	public boolean isPagada() {
		return pagada;
	}
	public void setPagada(boolean pagada) {
		this.pagada = pagada;
	}

	@Override
	public int compareTo(Comanda comanda) {
		int compareValue = this.getData_comanda().compareTo(comanda.getData_comanda());
		if(compareValue == 0) {return this.getImport_comanda() - comanda.getImport_comanda();}
		else return compareValue;
	}
	
	@Override
	public String toString() {
		return "Comanda [data_comanda=" + data_comanda + ", import_comanda=" + import_comanda + ", pagada=" + pagada
				+ "]";
	}	
	
	//Enter a date with an specific format (dd-MM-yyyy)
	public static String enterDate()
	{
		String date = "";
		boolean rightParameter = false;
		do
		{
			System.out.print("Enter date (p.e. 03-09-2018): ");
			date = ClientDB.sc.nextLine();
			
			//check if date's content has 9 characters
			if(date.matches("[0-9]{1,2}-[0-9]{1,2}-[0-9]{4}"))
			{
				rightParameter = true;
			}
			else
			{
				System.out.println("'date' format must have the following format: dd-MM-yyyy (p.e. 03-09-2018)");
			}
			
		}while(!rightParameter);
		
		String[] splitedDate = date.split("-");
		return splitedDate[2]+"-"+splitedDate[1]+"-"+splitedDate[0];
		
		//return date;
	}
}

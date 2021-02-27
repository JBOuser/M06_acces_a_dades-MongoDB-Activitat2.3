package dam.m06.mongodb.Activitat_2_3;

import java.util.ArrayList;

public class Client implements Comparable<Client>{

	private String dni;
	private String nom;
	private int total_facturacio;
	private String telefon;
	private String email;
	private ArrayList<Comanda> comandes;	
	
	public Client(String dni, String nom, int total_facturacio) {
		super();
		this.dni = dni;
		this.nom = nom;
		this.total_facturacio = total_facturacio;
		this.telefon = "";
		this.email = "";
		this.comandes = new ArrayList<Comanda>();
	}
	
	public String getDni() {
		return dni;
	}
	public void setDni(String dni) {
		this.dni = dni;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public int getTotal_facturacio() {
		return total_facturacio;
	}
	public void setTotal_facturacio(int total_facturacio) {
		this.total_facturacio = total_facturacio;
	}
	public String getTelefon() {
		return telefon;
	}
	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public ArrayList<Comanda> getComandes() {
		return comandes;
	}

	@Override
	public String toString() {
		return "Client [dni=" + dni + ", nom=" + nom + ", total_facturacio=" + total_facturacio + ", telefon=" + telefon
				+ ", email=" + email + ", comandes=" + comandes + "]";
	}

	@Override
	public int compareTo(Client client) {

		// if this = client return 0, else if this > client return 1, else if this < client return -1
		int comparatorValue = this.getDni().compareTo(client.getDni());
		if(comparatorValue == 0) { return this.getNom().compareTo(client.getNom());}
		else return comparatorValue;
	}	
	
	//Enter the dni following the right format
	public static String enterDni()
	{
		String dni = "";
		boolean rightParameter = false;
		do
		{
			System.out.print("Enter dni (p.e. 12345678A): ");
			dni = ClientDB.sc.nextLine();
			//check if dni's content has 9 characters
			if(dni.length()==9)
			{
				if(dni.substring(0, 8).matches("[0-9]{8}") && dni.substring(8).matches("[A-Za-z]"))
				{
					rightParameter = true;
				}
				else
				{
					System.out.println("'dni' must have 8 numbers and 1 letter (p.e 12345678A)");
				}
			}
			else
			{
				System.out.println("'dni' must have 9 characters (8 numbers and 1 letter) (p.e 12345678A)");
			}			
		}while(!rightParameter);
		
		return dni;
	}
	
	
	//Enter the total_facturacio following the right format
	public static int enterInteger(String title)
	{
		String integer = "";
		do
		{
			System.out.printf(title);
			integer = ClientDB.sc.nextLine();
		}
		while(!integer.matches("^[0-9]+$"));
		
		return Integer.parseInt(integer);
	}
	
	
	//Enter the telefon following the right format
	public static String enterTelefon()
	{
		String telefon = "";
		boolean rightParameter = false;
		do
		{
			System.out.print("Enter telefon (p.e 931234567): ");
			telefon = ClientDB.sc.nextLine();
			//check if telefon's content has 9 characters
			if(telefon.length()==9 && telefon.matches("[0-9]{9}"))
			{
				rightParameter = true;
			}
			else
			{
				System.out.println("'telefon' must have 9 numbers (p.e 931234567)");
			}
			
		}while(!rightParameter);
		
		return telefon;
	}	
	
	
	//Enter the telefon following the right format
	public static String enterEmail()
	{
		String email = "";
		boolean rightParameter = false;
		do
		{
			System.out.print("Enter email (p.e. k@k.org): ");
			email = ClientDB.sc.nextLine();
			//check if email's content contains an @ and ends with '.' and 1-3 letters
			if(email.matches(".*@.*\\.[A-Za-z]{1,3}"))
			{
				rightParameter = true;
			}
			else
			{
				System.out.println("'email' must have contain @ and end with '.' and 1-3 letters (p.e k@k.org)");
			}
			
		}while(!rightParameter);
		
		return email;
	}		
	
	
	//check if there is any field with empty content
	public static String checkEmptyFields(Client client)
	{
		String emptyField = "";
		String[] clientFields = new String[] {"dni","nom","total_facturacio","telefon","email","comandes"};
		
		if(client.getDni().matches("")){emptyField = clientFields[0];}
		else if(client.getNom().matches("")){emptyField = clientFields[1];}
		//else if(client.getTotal_facturacio() == 0){emptyField = clientFields[2];}
		else if(client.getTelefon().matches("")){emptyField = clientFields[3];}
		else if(client.getEmail().matches("")){emptyField = clientFields[4];}
		//else if(client.getComandes().size()==0){emptyField = clientFields[5];}
		
		return emptyField;
	}
}
